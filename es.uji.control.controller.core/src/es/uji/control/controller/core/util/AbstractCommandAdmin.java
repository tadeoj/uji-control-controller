package es.uji.control.controller.core.util;

import es.uji.control.controller.core.service.ICommand;
import es.uji.control.controller.core.service.ICommandAdmin;
import es.uji.control.controller.core.service.UnavailableCommandException;

abstract public class AbstractCommandAdmin implements ICommandAdmin {

	private InterfaceManager interfaces;
	
	public AbstractCommandAdmin() {
		interfaces = new InterfaceManager();
	}
	
	@Override
	public void addCommand(Class<?> clazz, ICommand instance) {
		interfaces.add(clazz, instance);
		instance.enable(true);
	}

	@Override
	public ICommand getCommand(Class<?> commandClazz) {
		return (ICommand) interfaces.getImplementation(commandClazz);
	}
	
	public ICommand getValidCommand(Class<?> commandClazz) throws UnavailableCommandException {
		ICommand command = getCommand(commandClazz);
		if (command == null)
			throw new UnavailableCommandException("Unavailable command in this event.");
		return command;
	}
	
	@Override
	public ICommand removeCommand(Class<?> commandClazz) {
		ICommand command = (ICommand) interfaces.remove(commandClazz);
		if (command != null)
			command.enable(false);
		return command;
	}

	@Override
	public void disableAll() {
		for (Class<?> clazz :interfaces.getInterfaces()) {
			((ICommand) interfaces.getImplementation(clazz)).enable(false);
		}
		interfaces.clear();
	}

	abstract protected void initController();
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (Class<?> clazz :interfaces.getInterfaces()) {
			if (buffer.length() > 0) {
				buffer.append(",");
			}
			buffer.append(clazz.getName());
		}
		return buffer.toString();
	}
	
}
