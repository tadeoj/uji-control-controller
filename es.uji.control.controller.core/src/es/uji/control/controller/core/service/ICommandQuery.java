package es.uji.control.controller.core.service;

public interface ICommandQuery {
	
	public ICommand getCommand(Class<?> commandClazz);
	public ICommand getValidCommand(Class<?> commandClazz) throws UnavailableCommandException;

}
