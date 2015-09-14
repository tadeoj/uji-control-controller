package es.uji.control.controller.core.service;


public interface ICommandAdmin extends ICommandQuery {
	
	public void addCommand(Class<?> clazz, ICommand instance);
	
	public ICommand removeCommand(Class<?> clazz); 

	public void disableAll();

}
