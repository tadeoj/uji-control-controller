/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cpr5010eth.internal;

import es.uji.control.controller.core.service.ControllerConstants;
import es.uji.control.controller.core.service.ControllerInfo;
import es.uji.control.controller.core.util.ControllerInfoImpl;

public class CPR5010EthUtil {
	
	public final static String PROPERTY_ADDRESS = "address";
	
	public final static String PROPERTY_PORT = "port";
	
	public final static int DEFAULT_PORT = 10001;
	
	public final static String MANUFACTURER_ID = "OBID";
	
	public final static String MODEL_ID = "CPR5010E";

	static public ControllerInfo createControllerInfo(long id, String name, String port) {
		return new ControllerInfoImpl(
				id,
				name, 
				CPR5010EthUtil.MANUFACTURER_ID,
				CPR5010EthUtil.MODEL_ID,
				port,
				ControllerConstants.CONTROLLER_TYPE_NETWORK);
	}
	 
}
