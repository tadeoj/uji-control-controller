package es.uji.control.controller.mifare;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;

import es.uji.control.controller.core.service.AbstractControllerEvent;
import es.uji.control.controller.core.service.ControllerConstants;
import es.uji.control.controller.core.service.ICommandQuery;

public class EventMifareScanTagDetected extends AbstractControllerEvent {
	
	final static public String TOPIC = "EVENT_MIFARE_SCAN_TAG_DETECTED";

	public final static String MIFARE_TAG_ID = "mifaretagid";
	
	static public String getTopic() {
		return AbstractControllerEvent.getTopic(TOPIC);
	}
	
	static public void sendEvent(EventAdmin eventAdmin, ICommandQuery commandQuery, Date timestamp, 
			long controllerId, MifareTagId mifareTagId) {
		
		if (eventAdmin != null) {
			// Construimos el diccionario del evento
			HashMap<String,Object> properties = new HashMap<String,Object>();
			properties.put(COMMAND_QUERY_OBJ, commandQuery);
			properties.put(EventConstants.TIMESTAMP, timestamp.getTime());
			properties.put(ControllerConstants.ID, controllerId);
			properties.put(MIFARE_TAG_ID, mifareTagId);

			// Se envia el evento asincronamente
			eventAdmin.sendEvent(new Event(getTopic(TOPIC), properties));
		}
	}
	
	static public EventMifareScanTagDetected getEvent(Event event) {
		return new EventMifareScanTagDetected(
				event,
				(ICommandQuery) event.getProperty(COMMAND_QUERY_OBJ),
				new Date( (Long) event.getProperty(EventConstants.TIMESTAMP)),
				(Long) event.getProperty(ControllerConstants.ID),
				(MifareTagId) event.getProperty(MIFARE_TAG_ID)
			);
	}
	
	/////////////////////////////////////////////////////////////////
	// Partes de la instancia
	/////////////////////////////////////////////////////////////////
	
	private Date timestamp;
	private Long controllerId;
	private MifareTagId mifareTagId;
	
	public EventMifareScanTagDetected(Event event, ICommandQuery commandQuery, Date timestamp, long controllerId, MifareTagId mifareTagId) {
		super(event, commandQuery);
		this.timestamp = timestamp;
		this.controllerId = controllerId;
		this.mifareTagId = mifareTagId;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public long getControllerId() {
		return controllerId;
	}
	
	public MifareTagId getMifareTagId() {
		return mifareTagId;
	}
	
}