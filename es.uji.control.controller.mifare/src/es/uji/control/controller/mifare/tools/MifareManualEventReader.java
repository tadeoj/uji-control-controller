/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.mifare.tools;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import es.uji.control.controller.mifare.EventMifareManualTagDetected;
import es.uji.control.controller.core.service.CommandInfo;
import es.uji.control.controller.core.service.CommandManual;
import es.uji.control.controller.core.service.ControllerConstants;
import es.uji.control.controller.core.service.ControllerException;
import es.uji.control.controller.core.service.ControllerService;
import es.uji.control.controller.core.service.UnavailableControllerException;

public class MifareManualEventReader {
	
	private BundleContext context;
	private ControllerService controllerService;
	
	private CommandManual commandManual ;
	private EventMifareManualTagDetected eventMifare;
	
	volatile private boolean cancelGet;
	
	public MifareManualEventReader(BundleContext context, ControllerService controllerService) {
		this.context = context;
		this.controllerService = controllerService;
	}
	
	public EventMifareManualTagDetected getEvent(int millisecs) throws UnavailableControllerException, ControllerException {
		
		// Se resetea el resultado
		eventMifare = null;
		
		// Obtenemos los comandos del controlador
		CommandInfo commandInfo = (CommandInfo) controllerService.getValidCommand(CommandInfo.class);
		commandManual = (CommandManual) controllerService.getValidCommand(CommandManual.class);
		
		// Se registra el Handler
		ServiceRegistration eventHandlerRegistration = context.registerService(
				EventHandler.class.getName(), 
				new EventHandlerImpl(), 
				new Hashtable<String, Object>(buildProperties(commandInfo))
			);
		
		// Se le envia la orden de lectura al controlador.
		commandManual.getNextTag();
		
		// Esperamos a recibir el evento o a que pase el tiempo indicado.
		long finish = System.currentTimeMillis() + millisecs;
		cancelGet = false;
		synchronized (this) {
			while(!cancelGet && eventMifare == null && System.currentTimeMillis() < finish) {
				try {
					this.wait(50);
				} catch (InterruptedException e) {
				}
			}
		}
		
		// Se desregistra el handler
		eventHandlerRegistration.unregister();
		
		// Se retorna el evento si lo hay.
		return eventMifare;
			
	}
	
	private Map<String,Object> buildProperties(CommandInfo commandInfo) throws UnavailableControllerException {
		
		long controllerId = commandInfo.getControllerInfo().getId();

		// Se prepara un filtro para que solo acepte eventos de este controlador
		String filter = String.format("(%s=%s)", ControllerConstants.ID, controllerId );
		
		// Se crear el filtro.
		String[] topics = new String[] {
				EventMifareManualTagDetected.getTopic()
		};
		
		// Se preparan las propiedades
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(EventConstants.EVENT_TOPIC, topics);
		properties.put(EventConstants.EVENT_FILTER, filter);
		
		return properties;
	}
	
	public void cancel() throws UnavailableControllerException {
		synchronized (this) {
			cancelGet = true;
			commandManual.cancelNextTag();
			this.notifyAll();
		}
	}
	
	public class EventHandlerImpl implements EventHandler {

		@Override
		public void handleEvent(Event event) {
			if (event.getTopic().equals(EventMifareManualTagDetected.getTopic())) {
				eventMifare = EventMifareManualTagDetected.getEvent(event);
				//System.out.println(eventMifare.toString());
				synchronized (MifareManualEventReader.this) {
					MifareManualEventReader.this.notifyAll();
				}
			}
		}
		
	}
	
}
