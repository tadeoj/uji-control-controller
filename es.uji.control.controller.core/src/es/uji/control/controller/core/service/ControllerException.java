package es.uji.control.controller.core.service;

public class ControllerException extends Exception {
	
	public ControllerException(String msg, Throwable th) {
		super(msg, th);
	}

	private static final long serialVersionUID = 3774766847679787467L;
	
	public ControllerException(String message) {
		super(message);
	}
	
}
