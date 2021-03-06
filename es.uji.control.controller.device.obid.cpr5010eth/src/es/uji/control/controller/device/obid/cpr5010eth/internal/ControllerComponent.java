/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cpr5010eth.internal;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;

import es.uji.control.controller.core.service.CommandInfo;
import es.uji.control.controller.core.service.CommandManager;
import es.uji.control.controller.core.service.CommandManual;
import es.uji.control.controller.core.service.CommandOutput;
import es.uji.control.controller.core.service.CommandScan;
import es.uji.control.controller.core.service.CommandStatus;
import es.uji.control.controller.core.service.ControllerException;
import es.uji.control.controller.core.service.ControllerInfo;
import es.uji.control.controller.core.service.ControllerService;
import es.uji.control.controller.core.service.ControllerStatus;
import es.uji.control.controller.core.service.ControllerStatusInstance;
import es.uji.control.controller.core.service.ICommandQuery;
import es.uji.control.controller.core.service.OutputBuilder;
import es.uji.control.controller.core.service.OutputsInfo;
import es.uji.control.controller.core.service.UnavailableControllerException;
import es.uji.control.controller.core.util.AbstractCommand;
import es.uji.control.controller.core.util.AbstractCommandAdmin;
import es.uji.control.controller.device.obid.cprcore.CPR5010Eth;
import es.uji.control.controller.device.obid.cprcore.profile.CPR5010HostParameters;
import es.uji.control.controller.device.obid.cprcore.profile.CPR5010HostProfile;
import es.uji.control.controller.mifare.CommandMifare;
import es.uji.control.controller.mifare.CommandMifareAdmin;
import es.uji.control.controller.mifare.MifareControllerException;
import es.uji.control.controller.mifare.MifareKey;
import es.uji.control.controller.mifare.MifareKeyType;
import es.uji.control.controller.mifare.MifareTagId;

public class ControllerComponent extends AbstractCommandAdmin implements ControllerService {
	
	private static int id = 0;
	
	private BundleContext context;

	private String address;
	private Integer port;
	
	private CPR5010Eth cpr5010Eth;
	private ControllerInfo controllerInfo;
	
	private ControllerStatusInstance statusInstance;
	
	private CommandManualImpl commandManual;
	private CommandScanImpl commandScan;
	
	private ControllerContext controllerContext;
	
	public void startup(BundleContext context, Map<String, Object> properties) {
		
		this.context = context;
		
		// Se obtienen los datos para lanzar el controlador.
		address = (String) properties.get(CPR5010EthUtil.PROPERTY_ADDRESS);
		port = (Integer) properties.get(CPR5010EthUtil.PROPERTY_PORT);
		
		// Se crea el estado del componente
		statusInstance = new ControllerStatusInstance();
		
		// se lanza el controlador
		startupController();
	}
	
	public void shutdown() {
		// se para el controlador
		shutdownController();
	}
	
	public void update(Map<String, Object> properties) {
		String newAddress = (String) properties.get(CPR5010EthUtil.PROPERTY_ADDRESS);
		Integer newPort = (Integer) properties.get(CPR5010EthUtil.PROPERTY_PORT);
		
		if (!newAddress.equals(address) || !newPort.equals(port)) {
			shutdownController();
			this.address = newAddress;
			this.port = newPort;
			startupController();
		}
		
	}
	
	private void startupController() {
		
		// Se prepara la informacion general del controlador
		int currentId = 0;
		synchronized (ControllerComponent.this) {
			currentId = ++id;
		}
		controllerInfo = CPR5010EthUtil.createControllerInfo(
				currentId,
				String.format("CPR5010-%s", address),
				String.format("%s/%d", address, port)
		);
		
		// Se inicializan los servicios proporcionados por el controlador
		initController();
		
		// Se crera el contexto para trasladar las funcionalidades a las clases en las que se ha descompuesto el controlador.
		controllerContext = new ControllerContextImpl();
		
		// Se instancia el reader
		cpr5010Eth = new CPR5010Eth();
		try {
			// Se abre la conexion con el controlador
			cpr5010Eth.start();
			cpr5010Eth.connectTcp(address, port);
			cpr5010Eth.setProfile(new CPR5010HostProfile(new CPR5010HostParameters()));
			
			// Configuracion inicial del controlador
			cpr5010Eth.rfOff();
			
		} catch (MifareControllerException mfcEx) {
			statusInstance.setWaitingReconnect("Imposible conectar con el controlador.", mfcEx);
		}

	}
	
	private void shutdownController() {
		
		// Se detienen los servicios proporcionados por el controlador
		disableAll();
		
		// Se cierra la conexion con el controlador
		try {
			cpr5010Eth.disconnect();
			cpr5010Eth.stop();
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
		addCommand(CommandOutput.class, new CommandOutputImpl());
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
		public CPR5010Eth getMifareReader() {
			return cpr5010Eth;
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
				synchronized (cpr5010Eth) {
					// Se ejecuta el reset
					cpr5010Eth.rfReset();
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
	
	private class CommandOutputImpl extends AbstractCommand implements CommandOutput {

		@Override
		public OutputsInfo getOutputInfo() throws UnavailableControllerException, ControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				return controllerContext.getMifareReader().getOutputInfo();
			}
		}

		@Override
		public void sendOutputs(OutputBuilder outputBuilder) throws UnavailableControllerException, ControllerException {
			synchronized (ControllerComponent.this) {
				checkCommand();
				controllerContext.getMifareReader().sendOutputs(outputBuilder);
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
