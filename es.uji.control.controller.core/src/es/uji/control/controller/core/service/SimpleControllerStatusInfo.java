package es.uji.control.controller.core.service;

public class SimpleControllerStatusInfo implements ControllerStatusInfo {
	
	private String info;
	
	public SimpleControllerStatusInfo(String info) {
		this.info = info;
	}

	@Override
	public String getInfo() {
		return info;
	}

}
