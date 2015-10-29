/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore.profile;

import es.uji.control.controller.mifare.MifareControllerException;

public class CPR4030HostProfile extends CPRAbstractProfile {
	
	private CPR4030HostParameters parameters;
	
	public CPR4030HostProfile(CPR4030HostParameters parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public void configTables() throws MifareControllerException {
		if (profileTableConfiguration.getISOTableSize() != 128) {
			profileTableConfiguration.setISOTableSize(128);
		}
	}

	@Override
	public void configRegisters() throws MifareControllerException {
		
	}
	
}
