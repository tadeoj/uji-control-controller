/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.mifare;

import es.uji.control.controller.core.service.ICommand;
import es.uji.control.controller.core.service.UnavailableControllerException;

public interface CommandMifareAdmin extends ICommand {

	public void retryLoginKeyA(MifareTagId mifareTagId, MifareKey key, int sector) throws UnavailableControllerException, MifareControllerException;
	public void retryLoginKeyB(MifareTagId mifareTagId, MifareKey key, int sector) throws UnavailableControllerException, MifareControllerException;
	
}
