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
