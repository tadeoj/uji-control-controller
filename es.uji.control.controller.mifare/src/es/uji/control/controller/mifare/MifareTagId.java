package es.uji.control.controller.mifare;

public interface MifareTagId {

	public abstract MifareTagType getTagType();

	public abstract byte[] getSerialNumber();
	
	public abstract long getCompactSerialNumber();

}