package es.uji.control.controller.core.service;

import org.osgi.service.event.Event;


abstract public class AbstractControllerEvent implements ICommandQuery {

	public final static String COMMAND_QUERY_OBJ = "commandqueryobject";
	
	static private final String baseTopicName;
	
	static {
		baseTopicName = AbstractControllerEvent.class.getPackage().getName().replace('.', '/') + "/";
	}
	
	static public String getTopic(String topic) {
		return baseTopicName + topic;
	}
	
	protected Event event;
	protected ICommandQuery commandQuery;
	
	public AbstractControllerEvent(Event event, ICommandQuery commandQuery) {
		this.event = event;
		this.commandQuery = commandQuery;
	}
	
	public Event getEvent() {
		return event;
	}
	
	@Override
	public ICommand getCommand(Class<?> commandClazz) {
		return commandQuery.getCommand(commandClazz);
	}
	
	public ICommand getValidCommand(Class<?> commandClazz) throws UnavailableCommandException {
		ICommand command = getCommand(commandClazz);
		if (command == null)
			throw new UnavailableCommandException("Unavailable command in this event.");
		return command;
	}
	
	public String toString() {
		boolean first = true;
		StringBuffer buffer = new StringBuffer();
		for (String key : event.getPropertyNames()) {
			if (!first) {
				buffer.append(",");
			} else {
				first = false;
			}
			buffer.append(String.format("%s=%s", key, event.getProperty(key)));
		}
		return buffer.toString();
	}
	
}
