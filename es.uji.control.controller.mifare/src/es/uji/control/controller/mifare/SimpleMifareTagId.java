/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.mifare;

public class SimpleMifareTagId implements MifareTagId {

	private MifareTagType mifareType;
	private MifareByteArray serialNumber;
	private long compactSerialNumber;

	public SimpleMifareTagId(MifareTagType mifareType, byte[] serialNumber) {
		this.mifareType = mifareType;
		this.serialNumber = new SimpleMifareByteArray(serialNumber);
		this.compactSerialNumber = MifareUtils.serialNumberToLongLe(serialNumber);
	}

	public byte[] getSerialNumber() {
		return serialNumber.getBytes();
	}

	public MifareTagType getTagType() {
		return mifareType;
	}
	
	public long getCompactSerialNumber() {
		return compactSerialNumber;
	}	
	
	@Override
	public boolean equals(Object other) {
		if (other == null){
			return false;
		}
		if (other instanceof MifareTagId) {
			MifareTagId otherMifareTagId = (MifareTagId) other;
			if (otherMifareTagId.getCompactSerialNumber() == getCompactSerialNumber()) { 
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		buffer.append("mifareType=");
		buffer.append(mifareType.toString());
		buffer.append(",compactSerialNumber=");
		buffer.append(compactSerialNumber);
		buffer.append(",serialNumber=");
		buffer.append(MifareUtils.byteArrayToHexStringWithSpaces(getSerialNumber()));
		buffer.append("]");
		return buffer.toString();
	}

}
