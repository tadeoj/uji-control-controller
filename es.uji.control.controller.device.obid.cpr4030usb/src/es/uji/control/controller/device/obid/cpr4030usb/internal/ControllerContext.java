package es.uji.control.controller.device.obid.cpr4030usb.internal;

import org.osgi.framework.BundleContext;

import es.uji.control.controller.core.service.ControllerInfo;
import es.uji.control.controller.core.service.ControllerStatusInstance;
import es.uji.control.controller.core.service.ICommandQuery;
import es.uji.control.controller.device.obid.cprcore.CPR4030Usb;

public interface ControllerContext {
	public BundleContext getContext();
	public CPR4030Usb getMifareReader();
	public ControllerInfo getControllerInfo();
	public ControllerStatusInstance getStatusInstance();
	public ICommandQuery getCommandQuery();
}
