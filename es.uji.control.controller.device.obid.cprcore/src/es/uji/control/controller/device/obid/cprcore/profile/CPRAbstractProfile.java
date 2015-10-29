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
