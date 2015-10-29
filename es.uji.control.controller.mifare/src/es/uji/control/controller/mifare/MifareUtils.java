/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.mifare;

public class MifareUtils {
	
	static public final MifareKey PHILIPS_TRANSPORT_KEY = new SimpleMifareKey(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF });
	
	public static MifareKey instantiateKey(byte[] sn) {
		return new SimpleMifareKey(sn);
	}
	
	// LittleEndian
	static public long serialNumberToLongLe(byte[] serialNumber) {
		if (serialNumber == null || serialNumber.length > 8) {
			throw new IllegalArgumentException("serialNumber is null or biggest than 8 bytes");
		}
        long result = 0;
        switch (serialNumber.length) {
        	case 8:
                result += ((long)serialNumber[7] << 56);
        	case 7:
                result += ((long)(serialNumber[6] & 255) << 48);
        	case 6:
                result += ((long)(serialNumber[5] & 255) << 40);
        	case 5:
                result += ((long)(serialNumber[4] & 255) << 32);
        	case 4:
                result += ((long)(serialNumber[3] & 255) << 24);
        	case 3:
                result += ((serialNumber[2] & 255) << 16);
        	case 2:
                result += ((serialNumber[1] & 255) <<  8);
        	case 1:
                result += ((serialNumber[0] & 255) <<  0);
        	case 0:
        }
        return result;
	}
	
	// LittleEndian
	static public byte[] serialNumberToByteArrayLe(long serialNumber, int maxBytes) {
		if (maxBytes > 8 || maxBytes < 0)
			throw new IllegalArgumentException("Invalid number of bytes in array");
		
		byte[] arr = new byte[8];
		
		arr[0] = (byte)(serialNumber >>>  0);
		arr[1] = (byte)(serialNumber >>>  8);
		arr[2] = (byte)(serialNumber >>> 16);
		arr[3] = (byte)(serialNumber >>> 24);
		arr[4] = (byte)(serialNumber >>> 32);
		arr[5] = (byte)(serialNumber >>> 40);
		arr[6] = (byte)(serialNumber >>> 48);
		arr[7] = (byte)(serialNumber >>> 56);
		
		byte[] arrTrim = new byte[maxBytes];
		System.arraycopy(arr, 0, arrTrim, 0, maxBytes);
		
		return arrTrim;
	}
	
	static public byte[] serialNumberToByteArrayLe(long serialNumber) {
		return serialNumberToByteArrayLe(serialNumber, 8);
	}
	
	

	static public byte[] byteArrayClone(byte[] source) {
		byte[] result = new byte[source.length];
		System.arraycopy(source, 0, result, 0, source.length);
		return result;
	}

	static public String byteArrayToHexStringWithSpaces(byte[] serialNumber) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < serialNumber.length; i++) {
			String digit = Integer.toHexString(serialNumber[i] & 0xFF);
			if (digit.length() < 2)
				buffer.append('0');
			buffer.append(digit);
			if (i != (serialNumber.length - 1))
				buffer.append(' ');
		}
		return buffer.toString();
	}
	
}
