package es.uji.control.controller.device.obid.cpr4030usb.internal;

import es.uji.control.controller.core.util.ControllerInfoImpl;
import es.uji.control.controller.core.service.ControllerConstants;
import es.uji.control.controller.core.service.ControllerInfo;

public class CPR4030UsbUtil {
	
	public final static String PROPERTY_PORT = "port";
	
	public final static int DEFAULT_PORT = 0;
	
	public final static String MANUFACTURER_ID = "OBID";
	
	public final static String MODEL_ID = "CPR4030USB";

	static public ControllerInfo createControllerInfo(long id, String name, String port) {
		return new ControllerInfoImpl(
				id,
				name, 
				CPR4030UsbUtil.MANUFACTURER_ID,
				CPR4030UsbUtil.MODEL_ID,
				port,
				ControllerConstants.CONTROLLER_TYPE_USB);
	}
	
}
