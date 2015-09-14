package es.uji.control.controller.device.obid.cprcore;

public class FeSetException extends Exception {
	
	private int errorCode;
	
	public FeSetException(int errorCode, String description) {
		super(String.format("0x%02x-: %s", errorCode, description));
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}

}
