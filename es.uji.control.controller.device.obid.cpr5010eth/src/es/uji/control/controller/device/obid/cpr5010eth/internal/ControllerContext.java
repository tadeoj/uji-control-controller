/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
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
