package es.uji.control.controller.core.service;

public interface CommandStatus extends ICommand {
	
	public ControllerStatus getControllerStatus() throws UnavailableControllerException;

}
