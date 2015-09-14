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
