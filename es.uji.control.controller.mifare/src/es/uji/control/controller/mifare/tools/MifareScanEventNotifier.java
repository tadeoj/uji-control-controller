package es.uji.control.controller.mifare.tools;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import es.uji.control.controller.mifare.EventMifareScanTagDetected;
import es.uji.control.controller.mifare.EventMifareScanTagLosted;
import es.uji.control.controller.core.service.ControllerException;

public class MifareScanEventNotifier {
	
	private BundleContext context;
	private ServiceRegistration eventHandlerRegistration;
	private MifareScanEventNotifierListener listener;
	
	public MifareScanEventNotifier(BundleContext context, MifareScanEventNotifierListener listener) {
		this.context = context;
		this.listener = listener;
	}
	
	public void startScann() throws ControllerException {
		
		String[] topics = new String[] {
				EventMifareScanTagDetected.getTopic(),
				EventMifareScanTagLosted.getTopic()
		};
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(EventConstants.EVENT_TOPIC, topics);
		eventHandlerRegistration = context.registerService(
			EventHandler.class.getName(), 
			new EventHandlerImpl(), 
			new Hashtable<String, Object>(properties)
		);
		
	}

	public void stopScann() {
		eventHandlerRegistration.unregister();
	}
	
	public class EventHandlerImpl implements EventHandler {

		@Override
		public void handleEvent(Event event) {
			if (event.getTopic().equals(EventMifareScanTagDetected.getTopic())) {
				EventMifareScanTagDetected eventMifareScanTagDetected = EventMifareScanTagDetected.getEvent(event); 
				//System.out.println(eventMifareScanTagDetected.toString());
				listener.newEvent(eventMifareScanTagDetected);
			} else if (event.getTopic().equals(EventMifareScanTagLosted.getTopic())){
				EventMifareScanTagLosted eventMifareScanTagLosted = EventMifareScanTagLosted.getEvent(event); 
				//System.out.println(eventMifareScanTagLosted.toString());
				listener.newEvent(eventMifareScanTagLosted);
			} else {
				// No deberia de ser posible
			}
		}
		
	}
	
}
