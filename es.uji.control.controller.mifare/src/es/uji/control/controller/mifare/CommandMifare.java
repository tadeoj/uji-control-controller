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

public interface CommandMifare extends ICommand {

	public void select(MifareTagId mifareTagId) throws UnavailableControllerException, MifareControllerException;
	
	public void loginKey(MifareTagId mifareTagId, MifareKey key, MifareKeyType mifareKeyType, int sector) throws UnavailableControllerException, MifareControllerException;
	public void loginKey(MifareTagId mifareTagId, int storedKey, MifareKeyType mifareKeyType, int sector) throws UnavailableControllerException, MifareControllerException;

	public void write(MifareTagId mifareTagId, int block, byte[] data) throws UnavailableControllerException, MifareControllerException;
	public byte[] read(MifareTagId mifareTagId, int block) throws UnavailableControllerException, MifareControllerException;

}
