/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore.profile;

public class CPR5010HostParameters {

	private int offlineDelay;
	private int tagDetectedActivationTime;
	
	public CPR5010HostParameters() {
		offlineDelay =  20; // 2 segundos (20 decimas de segundo)
		tagDetectedActivationTime = 5; // (5 decimas de segundo);
	}

	public int getOfflineDelay() {
		return offlineDelay;
	}

	public void setOfflineDelay(int offlineDelay) {
		this.offlineDelay = offlineDelay;
	}

	public int getTagDetectedActivationTime() {
		return tagDetectedActivationTime;
	}

	public void setTagDetectedActivationTime(int tagDetectedActivationTime) {
		this.tagDetectedActivationTime = tagDetectedActivationTime;
	}
	
	
}
