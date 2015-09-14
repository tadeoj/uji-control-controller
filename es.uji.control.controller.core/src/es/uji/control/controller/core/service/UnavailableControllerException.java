package es.uji.control.controller.core.service;

public class UnavailableControllerException extends ControllerException {
	
	private static final long serialVersionUID = -860650016685050270L;

	public UnavailableControllerException(String message) {
		super(message);
	}
	
	public UnavailableControllerException(String message, Exception ex) {
		super(message, ex);
	}
	
}
