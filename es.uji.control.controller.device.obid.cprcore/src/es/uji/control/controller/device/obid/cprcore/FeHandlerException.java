/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore;

public class FeHandlerException extends Exception {
	
	private StatusByteEnum statusByteEnum;
	
	public FeHandlerException(StatusByteEnum statusByteEnum) {
		super(statusByteEnum.toString());
		this.statusByteEnum = statusByteEnum;
	}
	
	public StatusByteEnum getStatusByteEnum() {
		return statusByteEnum;
	}

}
