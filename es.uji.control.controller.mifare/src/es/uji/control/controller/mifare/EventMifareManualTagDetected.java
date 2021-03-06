/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
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

public class EventMifareManualTagDetected extends AbstractControllerEvent {
	
	final static public String TOPIC = "EVENT_MIFARE_MANUAL_TAG_DETECTED";

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
	
	static public EventMifareManualTagDetected getEvent(Event event) {
		return new EventMifareManualTagDetected(
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
	private long controllerId;
	private MifareTagId mifareTagId;
	
	public EventMifareManualTagDetected(Event event, ICommandQuery commandQuery, Date timestamp, long controllerId, MifareTagId mifareTagId) {
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
