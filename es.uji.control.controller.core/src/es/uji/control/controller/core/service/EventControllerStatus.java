package es.uji.control.controller.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;

public class EventControllerStatus extends AbstractControllerEvent {
	
	final static public String TOPIC = "EVENT_CONTROLLER_STATUS";

	static public String getTopic() {
		return getTopic(TOPIC);
	}
	
	static public void sendEvent(EventAdmin eventAdmin, ICommandQuery commandQuery, Date timestamp) {
		
		if (eventAdmin != null) {
			// Construimos el diccionario del evento
			Map<String,Object> properties = new HashMap<String,Object>();
			properties.put(COMMAND_QUERY_OBJ, commandQuery);
			properties.put(EventConstants.TIMESTAMP, timestamp.getTime());

			// Se envia el evento asincronamente
			eventAdmin.sendEvent(new Event(getTopic(TOPIC), properties));
		}
	}
	
	static public EventControllerStatus getEvent(Event event) {
		return new EventControllerStatus(
				event,
				(ICommandQuery) event.getProperty(COMMAND_QUERY_OBJ),
				new Date( (Long) event.getProperty(EventConstants.TIMESTAMP))
			);
	}
	
	/////////////////////////////////////////////////////////////////
	// Partes de la instancia
	/////////////////////////////////////////////////////////////////
	
	private Date timestamp;
	
	public EventControllerStatus(Event event, ICommandQuery commandQuery, Date timestamp) {
		super(event, commandQuery);
		this.timestamp = timestamp;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
}
