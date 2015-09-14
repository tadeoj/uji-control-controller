package es.uji.control.controller.mifare;

import es.uji.control.controller.core.service.ControllerException;

public class MifareControllerException extends ControllerException {
	
	private static final long serialVersionUID = 2563104878844976636L;
	
	private MifareControllerExceptionEnum code;
	private int manufacturerCode;
	
	public MifareControllerException(String message, Throwable th) {
		super(message, th);
	}
	
	public MifareControllerException(String message) {
		super(message);
		this.code = MifareControllerExceptionEnum.UNKNOWN_ERROR;
		this.manufacturerCode = 0;
	}
	
	public MifareControllerException(String message, MifareControllerExceptionEnum code) {
		super(message);
		this.code = code;
		this.manufacturerCode = 0;
	}
	
	public MifareControllerException(String message, MifareControllerExceptionEnum code, int manufacturerErrorCode) {
		super(message);
		this.code = code;
		this.manufacturerCode = manufacturerErrorCode;
	}

	public MifareControllerExceptionEnum getCode() {
		return code;
	}
	
	public int getManufacturerCode() {
		return manufacturerCode;
	}
	
}
