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
