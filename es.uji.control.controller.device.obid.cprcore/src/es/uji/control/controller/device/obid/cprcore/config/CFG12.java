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

public class CFG12 {
	
	final static public int REGISTER = 12;
	
	private CPRAbstract cpr;
	private MemoryType memoryType;
	private boolean dirty = false;
	
	private int validTime;
	
	static public CFG12 readRegister(CPRAbstract cpr, MemoryType memoryType) throws MifareControllerException {
		
		cpr.readConfigurationRegister(REGISTER, memoryType);
		
		return new CFG12(
			cpr,
			memoryType,
			cpr.getDriver().getConfigParaAsInteger(OperatingMode.NotificationMode.Filter.TransponderValidTime, memoryType.getValue())
		);

	}
	
	private CFG12(
			CPRAbstract cpr,
			MemoryType memoryType,
			int validTime
			) {
		this.cpr = cpr;
		this.memoryType = memoryType;
		this.validTime = validTime;
	}
	
	public void writeRegister() throws MifareControllerException {
		cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.Filter.TransponderValidTime, getValidTime(), memoryType.getValue());
		cpr.writeConfigurationRegister(REGISTER, memoryType);
		dirty = false;
	}
	
	public MemoryType getMemoryType() {
		return this.memoryType;
	}

	public int getValidTime() {
		return validTime;
	}
	
	public void setValidTime(int validTime) {
		if (validTime != getValidTime()) {
			this.validTime = validTime;
			dirty = true;
		}
	}

	public boolean isDirty() {
		return dirty;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(String.format("%s: %d\n", OperatingMode.NotificationMode.Filter.TransponderValidTime, getValidTime()));
		
		return buffer.toString();
	}

}
