package es.uji.control.controller.core.service;


public interface CommandOutput extends ICommand {

	public OutputsInfo getOutputInfo() throws UnavailableControllerException, ControllerException;
	public void sendOutputs(OutputBuilder outputBuilder) throws UnavailableControllerException, ControllerException;
	
}
