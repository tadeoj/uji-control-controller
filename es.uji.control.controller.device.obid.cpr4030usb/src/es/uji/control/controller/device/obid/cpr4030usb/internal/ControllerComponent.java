package es.uji.control.controller.device.obid.cpr4030usb.internal;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;

import es.uji.control.controller.core.util.AbstractCommand;
import es.uji.control.controller.core.util.AbstractCommandAdmin;
import es.uji.control.controller.core.service.CommandInfo;
import es.uji.control.controller.core.service.CommandManager;
import es.uji.control.controller.core.service.CommandManual;
import es.uji.control.controller.core.service.CommandScan;
import es.uji.control.controller.core.service.CommandStatus;
import es.uji.control.controller.core.service.ControllerException;
import es.uji.control.controller.core.service.ControllerInfo;
import es.uji.control.controller.core.service.ControllerService;
import es.uji.control.controller.core.service.ControllerStatus;
import es.uji.control.controller.core.service.ControllerStatusInstance;
import es.uji.control.controller.core.service.ICommandQuery;
import es.uji.control.controller.core.service.UnavailableControllerException;
import es.uji.control.controller.device.obid.cprcore.CPR4030Usb;
import es.uji.control.controller.device.obid.cprcore.profile.CPR4030HostParameters;
import es.uji.control.controller.device.obid.cprcore.profile.CPR4030HostProfile;
import es.uji.control.controller.mifare.CommandMifare;
import es.uji.control.controller.mifare.CommandMifareAdmin;
import es.uji.control.controller.mifare.MifareControllerException;
import es.uji.control.controller.mifare.MifareKey;
import es.uji.control.controller.mifare.MifareKeyType;
import es.uji.control.controller.mifare.MifareTagId;

public class ControllerComponent extends AbstractCommandAdmin implements ControllerService {

	private BundleContext context;
	private CPR4030Usb cpr4030Usb;
	private ControllerInfo controllerInfo;
	private ControllerStatusInstance statusInstance;
	
	private CommandManualImpl commandManual;
	private CommandScanImpl commandScan;
	
	private ControllerContext controllerContext;
	
	public void startup(BundleContext context, Map<String, Object> properties) {
		// Retenemos el contexto del framework OSGI
		this.context = context;

		//Se crea el estado del componente
		statusInstance = new ControllerStatusInstance();
		
		//Se lanza el controlador
		startupController();
	}
	
	public void shutdown() {
		// se para el controlador
		shutdownController();
	}
	
	private void startupController() {
		int id = 0;

		controllerInfo = CPR4030UsbUtil.createControllerInfo(
				id, 
				String.format("CPR4030-%d", id), 
				String.format("%d", 0)
		);
		
		// Se inicializan los servicios proporcionados por el controlador
		initController();
		
		// Se crera el contexto para trasladar las funcionalidades a las clases en las que se ha descompuesto el controlador.
		controllerContext = new ControllerContextImpl();
		// Se instancia el reader
		cpr4030Usb = new CPR4030Usb();
		
		try {
			// Se abre la conexión con el controlador
			cpr4030Usb.start();
			cpr4030Usb.connectUsb(0);
			cpr4030Usb.setProfile(new CPR4030HostProfile(new CPR4030HostParameters()));
			
			// Configuración inicial del controlador
			cpr4030Usb.rfOff();
			
			// El estado es correcto
			statusInstance.setOk("Connected");
			
		} catch (MifareControllerException mfcEx) {
			statusInstance.setWaitingReconnect("Imposible conectar con el controlador.", mfcEx);
		}
	}
	
	private void shutdownController() {
		
		// Se detienen los servicios proporcionados por el controlador
		disableAll();
		
		// Se cierra la conexion con el controlador
		try {
			cpr4030Usb.disconnect();
			cpr4030Usb.stop();
		} catch (MifareControllerException e) {
			
		}
	}
	
	@Override
	protected void initController() {
		addCommand(CommandManager.class, new CommandManagerImpl());
		addCommand(CommandManual.class, new CommandManualProxy());
		addCommand(CommandScan.class, new CommandScanProxy());
		addCommand(CommandInfo.class, new CommandInfoImpl());
		addCommand(CommandStatus.class, new CommandStatusImpl());
		addCommand(CommandMifare.class, new CommandMifareImpl());
		addCommand(CommandMifareAdmin.class, new CommandMifareAdminImpl());
	}

	public class ControllerContextImpl implements ControllerContext {

		@Override
		public BundleContext getContext() {
			return context;
		}

		@Override
		public ControllerInfo getControllerInfo() {
			return controllerInfo;
		}

		@Override
		public ControllerStatusInstance getStatusInstance() {
			return statusInstance;
		}

		@Override
		public ICommandQuery getCommandQuery() {
			return ControllerComponent.this;
		}

		@Override
		public CPR4030Usb getMifareReader() {
			return cpr4030Usb;
		}
		
	}

	private class CommandInfoImpl extends AbstractCommand implements CommandInfo {
		
		@Override
		public ControllerInfo getControllerInfo() throws UnavailableControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				return controllerInfo;
			}
		}
		
	}
	
	private class CommandManagerImpl extends AbstractCommand implements CommandManager {

		@Override
		public void resetController() throws UnavailableControllerException, ControllerException {
			synchronized (ControllerComponent.this) {
				// Verificamos que el comando esta activo.
				checkCommand();
				// Realizamos las operaciones sobre el controlador.
				synchronized (cpr4030Usb) {
					// Se ejecuta el reset
					cpr4030Usb.rfReset();
				}
			}

		}
	}

	private class CommandManualProxy extends AbstractCommand implements CommandManual {
		
		@Override
		public void getNextTag() throws UnavailableControllerException, ControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				if (commandScan != null) {
					throw new IllegalStateException("En estos momentos se estan escaneando Tags");
				} else {
					if (commandManual != null) {
						try {
							commandManual.cancelNextTag();
						} catch (Throwable tr) {
						}
					}
					commandManual = new CommandManualImpl(controllerContext);
					commandManual.getNextTag();
					}
			}
		}

		@Override
		public void cancelNextTag() throws UnavailableControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				if (commandManual != null) {
					commandManual.cancelNextTag();
					commandManual = null;
				}
			}
		}

	}

	private class CommandScanProxy extends AbstractCommand implements CommandScan {

		@Override
		public void startScan() throws UnavailableControllerException, ControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				if (commandManual != null) {
					throw new IllegalStateException("En estos momentos se esta esperando el siguiente TAG");
				}
				if (commandScan != null) {
					throw new IllegalStateException("En estos momentos se estan escaneando Tags");
				}
				commandScan = new CommandScanImpl(controllerContext);
				commandScan.startScan();
			}
		}

		@Override
		public void stopScan() throws UnavailableControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				if (commandScan != null) {
					try {
						commandScan.stopScan();
					} catch (Throwable tr) {
					}
					commandScan = null;
				}
			}
		}
	}
	

	private class CommandMifareAdminImpl extends AbstractCommand implements CommandMifareAdmin {

		@Override
		public void retryLoginKeyA(MifareTagId mifareTagId, MifareKey key, int sector) throws UnavailableControllerException, MifareControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				controllerContext.getMifareReader().rfReset();
				MifareTagId found = null;
				for(MifareTagId current : controllerContext.getMifareReader().getTagList()) {
					if (current.equals(mifareTagId)) {
						found = current;
						break;
					}
				}
				if (found != null) {
					controllerContext.getMifareReader().select(mifareTagId);
					controllerContext.getMifareReader().loginParamKey(sector, key, MifareKeyType.KEY_A);
				} else {
					throw new MifareControllerException("No se puede reintentar la autenticación debido a que el tag ya no esta presente");
				}
			}
		}

		@Override
		public void retryLoginKeyB(MifareTagId mifareTagId, MifareKey key, int sector) throws UnavailableControllerException, MifareControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				controllerContext.getMifareReader().rfReset();
				MifareTagId found = null;
				for(MifareTagId current : controllerContext.getMifareReader().getTagList()) {
					if (current.equals(mifareTagId)) {
						found = current;
						break;
					}
				}
				if (found != null) {
					controllerContext.getMifareReader().select(mifareTagId);
					controllerContext.getMifareReader().loginParamKey(sector, key, MifareKeyType.KEY_B);
				} else {
					throw new MifareControllerException("No se puede reintentar la autenticación debido a que el tag ya no esta presente");
				}
			}
		}
		
	}
	
	private class CommandMifareImpl extends AbstractCommand implements CommandMifare {
		
		@Override
		public void select(MifareTagId mifareTagId) throws UnavailableControllerException, MifareControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				controllerContext.getMifareReader().select(mifareTagId);
			}
		}
		
		@Override
		public void loginKey(MifareTagId mifareTagId, MifareKey key, MifareKeyType mifareKeyType, int sector) throws UnavailableControllerException, MifareControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				controllerContext.getMifareReader().loginParamKey(sector, key, mifareKeyType);
			}
		}
		
		@Override
		public void loginKey(MifareTagId mifareTagId, int storedKey, MifareKeyType mifareKeyType, int sector) throws UnavailableControllerException, MifareControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				controllerContext.getMifareReader().loginStoredKey(sector, storedKey, mifareKeyType);
			}
		}

		@Override
		public byte[] read(MifareTagId mifareTagId, int block) throws UnavailableControllerException, MifareControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				return controllerContext.getMifareReader().read(mifareTagId, block);
			}
		}

		@Override
		public void write(MifareTagId mifareTagId, int block, byte[] data) throws UnavailableControllerException, MifareControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				controllerContext.getMifareReader().write(mifareTagId, block, data);
			}
		}
	}
	
	private class CommandStatusImpl extends AbstractCommand implements CommandStatus {

		@Override
		public ControllerStatus getControllerStatus() throws UnavailableControllerException {
			synchronized (ControllerComponent.this) {
				return statusInstance;
			}
		}
	}
	
	public String toString() {
		Map<String,Object> properties = new HashMap<String, Object>();
		properties.putAll(controllerInfo.getProperties());
		StringBuffer buffer = new StringBuffer();
		buffer.append(properties.toString());
		buffer.append(", COMMANDS = ");
		buffer.append(super.toString());
		return buffer.toString();
	}

}
