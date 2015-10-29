/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
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
