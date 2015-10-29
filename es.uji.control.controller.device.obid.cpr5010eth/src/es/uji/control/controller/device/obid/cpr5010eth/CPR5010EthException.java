/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cpr5010eth;

public class CPR5010EthException extends Exception {
	
	private static final long serialVersionUID = -673838629404992302L;

	public CPR5010EthException(String message, Throwable cause) {
		super(message, cause);
	}

	public CPR5010EthException(String message) {
		super(message);
	}
	
}
