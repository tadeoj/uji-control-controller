/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.mifare;

public class SimpleMifareKey implements MifareKey {

	final MifareByteArray buffer;
	
	public SimpleMifareKey(byte[] bytes) {
		buffer = new SimpleMifareByteArray(bytes);
	}
	
	public byte[] getBytes() {
		return buffer.getBytes();
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("mifareKeyBytes=");
		buffer.append(MifareUtils.byteArrayToHexStringWithSpaces(getBytes()));
		return buffer.toString();
	}


}
