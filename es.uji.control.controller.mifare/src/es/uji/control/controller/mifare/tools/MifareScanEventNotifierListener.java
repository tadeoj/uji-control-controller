package es.uji.control.controller.mifare.tools;

import es.uji.control.controller.mifare.EventMifareScanTagDetected;
import es.uji.control.controller.mifare.EventMifareScanTagLosted;

public interface MifareScanEventNotifierListener {
	public void newEvent(EventMifareScanTagDetected event);
	public void newEvent(EventMifareScanTagLosted event);
}
