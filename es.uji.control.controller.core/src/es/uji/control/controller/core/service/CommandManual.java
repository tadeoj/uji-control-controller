package es.uji.control.controller.core.service;


public interface CommandManual extends ICommand {
	
	public void getNextTag() throws UnavailableControllerException, ControllerException;
	
	public void cancelNextTag() throws UnavailableControllerException;
	

}
