package es.uji.control.controller.mifare;

import es.uji.control.controller.core.service.ICommand;
import es.uji.control.controller.core.service.UnavailableControllerException;

public interface CommandMifareAdmin extends ICommand {

	public void retryLoginKeyA(MifareTagId mifareTagId, MifareKey key, int sector) throws UnavailableControllerException, MifareControllerException;
	public void retryLoginKeyB(MifareTagId mifareTagId, MifareKey key, int sector) throws UnavailableControllerException, MifareControllerException;
	
}
