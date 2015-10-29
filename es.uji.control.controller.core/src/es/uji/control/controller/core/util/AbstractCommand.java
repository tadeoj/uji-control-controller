/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.core.util;

import es.uji.control.controller.core.service.ICommand;
import es.uji.control.controller.core.service.UnavailableControllerException;

abstract public class AbstractCommand implements ICommand {
	
	boolean enabled = true;

	@Override
	public void enable(boolean enabled) {
		synchronized (this) {
			this.enabled = enabled;
		}
	}

	@Override
	public boolean isEnabled() {
		synchronized (this) {
			return enabled;
		}
	}
	protected void checkCommand() throws UnavailableControllerException{
		if (!this.isEnabled())
			throw new UnavailableControllerException("Command is disabled");
	}
	
}