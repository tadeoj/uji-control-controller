/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore.profile;

import es.uji.control.controller.device.obid.cprcore.CPRAbstract;
import es.uji.control.controller.mifare.MifareControllerException;

public interface ICPRProfile {
	
	abstract public void init(CPRAbstract cpr, ICPRProfileRegisterConfiguration profileRegisterConfiguration, ICPRProfileTableConfiguration profileTableConfigration);
	
	abstract public void configTables() throws MifareControllerException;
	
	abstract public void configRegisters() throws MifareControllerException;
	
	public void finish() throws MifareControllerException;
	
}
