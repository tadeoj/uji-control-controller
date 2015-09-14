package es.uji.control.controller.core.service;

public class UnavailableCommandException extends ControllerException {
	
	private static final long serialVersionUID = -860650016685050270L;

	public UnavailableCommandException(String message) {
		super(message);
	}
	
	public UnavailableCommandException(String message, Exception ex) {
		super(message, ex);
	}
	
}
