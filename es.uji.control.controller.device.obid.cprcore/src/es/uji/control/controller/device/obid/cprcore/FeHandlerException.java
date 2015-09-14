package es.uji.control.controller.device.obid.cprcore;

public class FeHandlerException extends Exception {
	
	private StatusByteEnum statusByteEnum;
	
	public FeHandlerException(StatusByteEnum statusByteEnum) {
		super(statusByteEnum.toString());
		this.statusByteEnum = statusByteEnum;
	}
	
	public StatusByteEnum getStatusByteEnum() {
		return statusByteEnum;
	}

}
