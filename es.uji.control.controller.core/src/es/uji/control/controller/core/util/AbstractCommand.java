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