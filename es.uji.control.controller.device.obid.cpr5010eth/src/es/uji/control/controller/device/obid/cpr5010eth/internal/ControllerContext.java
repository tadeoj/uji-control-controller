package es.uji.control.controller.device.obid.cpr5010eth.internal;

import org.osgi.framework.BundleContext;

import es.uji.control.controller.core.service.ControllerInfo;
import es.uji.control.controller.core.service.ControllerStatusInstance;
import es.uji.control.controller.core.service.ICommandQuery;
import es.uji.control.controller.device.obid.cprcore.CPR5010Eth;

public interface ControllerContext {
	public BundleContext getContext();
	public CPR5010Eth getMifareReader();
	public ControllerInfo getControllerInfo();
	public ControllerStatusInstance getStatusInstance();
	public ICommandQuery getCommandQuery();
}
