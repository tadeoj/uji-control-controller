package es.uji.control.controller.device.obid.cprcore;

import de.feig.FePortDriverException;
import de.feig.FeReaderDriverException;
import de.feig.FedmException;
import es.uji.control.controller.device.obid.cprcore.config.CFG03;
import es.uji.control.controller.device.obid.cprcore.config.MemoryType;
import es.uji.control.controller.core.service.OutputBuilder;
import es.uji.control.controller.core.service.OutputLedEnum;
import es.uji.control.controller.core.service.OutputsInfo;
import es.uji.control.controller.mifare.MifareControllerException;

public class CPR4030Usb extends CPRAbstract {

	public void connectUsb() throws MifareControllerException {
		connectUsb(0);
	}

	public void connectUsb(long usbId) throws MifareControllerException {
		// Se desconecta
		disconnect();
		// Codigo especifico para la apertura mediante USB
		try {
			// Conectamos con el primer lector USB
			driver.connectUSB(usbId);
			
			// Hay que asegurarse de la configuracion es la deseada.
			checkConfig();
		} catch (FedmException e) {
			throw new MifareControllerException("Imposible conectar con el lector USB", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException("Imposible conectar con el lector USB", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException("Imposible conectar con el lector USB", e);
		}
	}

	protected void checkConfig() throws MifareControllerException {
		
		CFG03 cfg3 = CFG03.readRegister(this, MemoryType.EEPROM);
		checkRFInterfaceConfig(cfg3);
		
		int changes = 0;
		if (cfg3.isDirty()) {
			cfg3.writeRegister();
			changes++;
		}
		if (changes > 0) {
			cpuReset();
		}
		
	}
	
	protected void checkRFInterfaceConfig(CFG03 cfg3) throws MifareControllerException {
		
		// Informacion del inventory ampliada
		if (!cfg3.isOptionalInventoryInfoEnabled()) {
			cfg3.setOptionalInventoryInfoEnabled(true);
		}
		
	}
	
	@Override
	public OutputsInfo getOutputInfo() {
		return new OutputsInfo() {
			
			@Override
			public int digitalOutputCount() {
				return 0;
			}

			@Override
			public int relayCount() {
				return 0;
			}
			
			@Override
			public boolean hasLed(OutputLedEnum led) {
				return false;
			}
			
			@Override
			public int buzzerCount() {
				return 0;
			}
		};
	}

	@Override
	public void sendOutputs(OutputBuilder outputBuilder) {
	}

}
