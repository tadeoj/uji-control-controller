/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cpr5010eth;


public interface ICPR5010Eth {
	public CPR5010EthInstance open(String address);
	public CPR5010EthInstance open(String address, int port);
	public void close(CPR5010EthInstance instance);
}
