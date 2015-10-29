/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
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
