/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore.config;

import de.feig.ReaderConfig.Transponder;
import es.uji.control.controller.device.obid.cprcore.CPRAbstract;
import es.uji.control.controller.mifare.MifareControllerException;

public class CFG03 {
	
	final static public int REGISTER = 03;
	
	private CPRAbstract cpr;
	private MemoryType memoryType;
	private boolean dirty = false;
	
	private boolean optionalInventoryInfoEnabled;
	
	static public CFG03 readRegister(CPRAbstract cpr, MemoryType memoryType) throws MifareControllerException {
		
		cpr.readConfigurationRegister(REGISTER, memoryType);
		
		return new CFG03(
			cpr,
			memoryType,
			cpr.getDriver().getConfigParaAsBoolean(Transponder.HF.ISO_14443.TypeA.OptionalInventoryInfo, memoryType.getValue())
		);

	}
	
	private CFG03(
			CPRAbstract cpr,
			MemoryType memoryType,
			boolean optionalInventoryInfoEnabled
			) {
		this.cpr = cpr;
		this.memoryType = memoryType;
		this.optionalInventoryInfoEnabled = optionalInventoryInfoEnabled;
	}
	
	public void writeRegister() throws MifareControllerException {
		cpr.getDriver().setConfigPara(Transponder.HF.ISO_14443.TypeA.OptionalInventoryInfo, isOptionalInventoryInfoEnabled(), memoryType.getValue());
		cpr.writeConfigurationRegister(REGISTER, memoryType);
		dirty = false;
	}
	
	public MemoryType getMemoryType() {
		return this.memoryType;
	}

	public boolean isOptionalInventoryInfoEnabled() {
		return optionalInventoryInfoEnabled;
	}
	
	public void setOptionalInventoryInfoEnabled(boolean enabled) {
		if (enabled != isOptionalInventoryInfoEnabled()) {
			optionalInventoryInfoEnabled = enabled;
			dirty = true;
		}
	}

	public boolean isDirty() {
		return dirty;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(String.format("%s: %b\n", Transponder.HF.ISO_14443.TypeA.OptionalInventoryInfo, isOptionalInventoryInfoEnabled()));
		
		return buffer.toString();
	}

}
