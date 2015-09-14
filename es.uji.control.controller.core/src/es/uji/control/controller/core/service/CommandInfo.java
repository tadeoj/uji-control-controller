package es.uji.control.controller.core.service;

public interface CommandInfo extends ICommand {
	
	public ControllerInfo getControllerInfo() throws UnavailableControllerException;

}
