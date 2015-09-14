package es.uji.control.controller.core.service;


public class ControllerStatusInstance implements ControllerStatus {
	
	private ControllerStatusEnum status;
	private ControllerStatusInfo info;
	private ControllerException ex;
	
	public ControllerStatusInstance() {
		setConnecting("");
	}

	public void setConnecting(String info) {
		setConnecting(new SimpleControllerStatusInfo(info));
	}
	
	public void setConnecting(ControllerStatusInfo info) {
		status = ControllerStatusEnum.CONNECTING;
		this.info = info;
		this.ex = null;
	}
	
	public void setOk(String info) {
		setOk(new SimpleControllerStatusInfo(info));
	}
	
	public void setOk(ControllerStatusInfo info) {
		status = ControllerStatusEnum.OK;
		this.info = info;
		this.ex = null;
	}
	
	public void setWaitingReconnect(String info, ControllerException ex) {
		setWaitingReconnect(new SimpleControllerStatusInfo(info), ex);
	}
	
	public void setWaitingReconnect(ControllerStatusInfo info, ControllerException ex) {
		status = ControllerStatusEnum.WAITING_RECONNECT;
		this.info = info;
		this.ex = ex;
	}
	
	@Override
	public ControllerStatusEnum getControllerStatusEnum() {
		return status;
	}

	@Override
	public ControllerStatusInfo getControllerStatusInfo() {
		return info;
	}

	@Override
	public ControllerException getControllerException() {
		return ex;
	}
	
	public void processRuntimeException(Throwable tr) {
		tr.printStackTrace();
	}
	
}
