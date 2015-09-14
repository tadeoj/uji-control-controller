package es.uji.control.controller.core.service;


public interface CommandScan extends ICommand {

	public void startScan() throws UnavailableControllerException, ControllerException;
	
	public void stopScan() throws UnavailableControllerException;
	
}
