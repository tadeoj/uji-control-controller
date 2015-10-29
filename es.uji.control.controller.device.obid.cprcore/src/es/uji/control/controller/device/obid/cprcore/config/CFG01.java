/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore.config;

import de.feig.ReaderConfig.OperatingMode;
import es.uji.control.controller.device.obid.cprcore.CPRAbstract;
import es.uji.control.controller.mifare.MifareControllerException;

public class CFG01 {
	
	final static public int REGISTER = 01;
	
	private CPRAbstract cpr;
	private MemoryType memoryType;
	private boolean dirty = false;
	
	private boolean notificationModeEnabled;
	
	static public CFG01 readRegister(CPRAbstract cpr, MemoryType memoryType) throws MifareControllerException {
		
		cpr.readConfigurationRegister(REGISTER, memoryType);
		
		return new CFG01(
			cpr,
			memoryType,
			((cpr.getDriver().getConfigParaAsByte(OperatingMode.Mode, memoryType.getValue()) & 0x40) != 0 ? true : false)
			//cpr.getDriver().getConfigParaAsBoolean(OperatingMode.Mode, memoryType.getValue())
		);

	}
	
	private CFG01(
			CPRAbstract cpr,
			MemoryType memoryType,
			boolean notificationModeEnabled
			) {
		this.cpr = cpr;
		this.memoryType = memoryType;
		this.notificationModeEnabled = notificationModeEnabled;
	}
	
	public void writeRegister() throws MifareControllerException {
		cpr.getDriver().setConfigPara(OperatingMode.Mode, (byte) (isNotificationModeEnabled() ? 0x40 : 0x0), memoryType.getValue());
		//cpr.getDriver().setConfigPara(OperatingMode.Mode, isNotificationModeEnabled(), memoryType.getValue());
		cpr.writeConfigurationRegister(REGISTER, memoryType);
		dirty = false;
	}
	
	public MemoryType getMemoryType() {
		return this.memoryType;
	}

	public boolean isNotificationModeEnabled() {
		return notificationModeEnabled;
	}
	
	public void setNotificationModeEnabled(boolean enabled) {
		if (enabled != isNotificationModeEnabled()) {
			notificationModeEnabled = enabled;
			dirty = true;
		}
	}

	public boolean isDirty() {
		return dirty;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(String.format("%s: %b\n", OperatingMode.Mode, isNotificationModeEnabled()));
		
		return buffer.toString();
	}

}
