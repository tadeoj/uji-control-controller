package es.uji.control.controller.device.obid.cprcore.profile;

import es.uji.control.controller.device.obid.cprcore.CPRAbstract;
import es.uji.control.controller.mifare.MifareControllerException;

public interface ICPRProfile {
	
	abstract public void init(CPRAbstract cpr, ICPRProfileRegisterConfiguration profileRegisterConfiguration, ICPRProfileTableConfiguration profileTableConfigration);
	
	abstract public void configTables() throws MifareControllerException;
	
	abstract public void configRegisters() throws MifareControllerException;
	
	public void finish() throws MifareControllerException;
	
}
