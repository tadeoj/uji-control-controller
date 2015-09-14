package es.uji.control.controller.device.obid.cprcore.profile;

import es.uji.control.controller.device.obid.cprcore.CPRAbstract;
import es.uji.control.controller.mifare.MifareControllerException;

abstract public class CPRAbstractProfile implements ICPRProfile {

	protected CPRAbstract cpr;
	protected ICPRProfileRegisterConfiguration profileRegisterConfiguration;
	protected ICPRProfileTableConfiguration profileTableConfiguration;
	
	private boolean needCpuReset = false;

	@Override
	public void init(CPRAbstract cpr, ICPRProfileRegisterConfiguration profileRegisterConfiguration, ICPRProfileTableConfiguration profileTableConfigration) {
		this.cpr = cpr;
		this.profileRegisterConfiguration = profileRegisterConfiguration;
		this.profileTableConfiguration = profileTableConfigration;
	}
	
	protected void needCpuReset() {
		needCpuReset = true;
	}
	
	public void finish() throws MifareControllerException {
		if (needCpuReset) {
			cpr.cpuReset();
			needCpuReset = false;
		}
	}
	
}
