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
