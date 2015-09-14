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
