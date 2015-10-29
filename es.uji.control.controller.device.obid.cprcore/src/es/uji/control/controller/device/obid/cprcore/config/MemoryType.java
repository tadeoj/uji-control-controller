/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore.config;

public enum MemoryType {
	
	EEPROM (true),
	RAM (false);
	
	private boolean value;
	
	private MemoryType(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
	
	public static MemoryType getByValue(boolean value) throws IllegalArgumentException {
		for (MemoryType memoryType : MemoryType.values()) {
			if (value == memoryType.getValue())
				return memoryType;
		}
		throw new IllegalArgumentException("Invalid Id to build CFG2.OutputMode.");
	}

}
