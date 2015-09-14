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
