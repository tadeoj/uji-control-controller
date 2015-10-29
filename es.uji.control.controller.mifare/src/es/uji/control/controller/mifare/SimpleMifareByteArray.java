/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.mifare;

public class SimpleMifareByteArray implements MifareByteArray {
	
	final private byte[] buffer;
	
	public SimpleMifareByteArray(byte[] data) {
		buffer = MifareUtils.byteArrayClone(data);
	}

	@Override
	public byte[] getBytes() {
		return MifareUtils.byteArrayClone(buffer);
	}

}
