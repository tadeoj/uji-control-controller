package es.uji.control.controller.core.service;


public interface CommandManager extends ICommand {
	
	public void resetController() throws UnavailableControllerException, ControllerException;

}
