package es.uji.control.controller.device.obid.cprcore.profile;

import java.net.InetAddress;
import java.net.UnknownHostException;

import es.uji.control.controller.mifare.MifareKeyType;

public class CPR5010NotificationParameters {

	private int datablockAddress;
	private int datablockNumber;
	private MifareKeyType mifareKeyType;
	private int keyAddress;
	private InetAddress destAddress;
	private int destinationPort;
	private int holdTime;
	private boolean keepAliveEnabled;
	private int keepAliveTime;
	private boolean datablocksTxEnabled;
	private int offlineDelay;
	private int tagDetectedActivationTime;
	private int notificationReadValidTime;

	
	public CPR5010NotificationParameters() {
		datablockAddress = 4;
		datablockNumber = 3;
		mifareKeyType = MifareKeyType.KEY_A;
		keyAddress = 0;
		try {
			destAddress = InetAddress.getByName("192.168.1.1");
		} catch (UnknownHostException e) {
			destAddress = null;
		}
		destinationPort = 10002;
		holdTime = 10;
		keepAliveEnabled = false;
		keepAliveTime = 1;
		datablocksTxEnabled = false;
		offlineDelay = 20; // 2 segundos (20 decimas de segundo)
		tagDetectedActivationTime = 5; // (5 decimas de segundo)
		notificationReadValidTime = 55; // 5.5 segundos (55 decimas de segundo)
	}

	public int getDatablockAddress() {
		return datablockAddress;
	}

	public void setDatablockAddress(int datablockAddress) {
		this.datablockAddress = datablockAddress;
	}

	public int getDatablockNumber() {
		return datablockNumber;
	}

	public void setDatablockNumber(int datablockNumber) {
		this.datablockNumber = datablockNumber;
	}

	public MifareKeyType getMifareKeyType() {
		return mifareKeyType;
	}

	public void setMifareKeyType(MifareKeyType mifareKeyType) {
		this.mifareKeyType = mifareKeyType;
	}

	public int getKeyAddress() {
		return keyAddress;
	}

	public void setKeyAddress(int keyAddress) {
		this.keyAddress = keyAddress;
	}

	public InetAddress getDestAddress() {
		return destAddress;
	}

	public void setDestAddress(InetAddress destAddress) {
		this.destAddress = destAddress;
	}

	public int getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
	}

	public int getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(int holdTime) {
		this.holdTime = holdTime;
	}

	public boolean isKeepAliveEnabled() {
		return keepAliveEnabled;
	}

	public void setKeepAliveEnabled(boolean keepAliveEnabled) {
		this.keepAliveEnabled = keepAliveEnabled;
	}

	public int getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public boolean isDatablocksTxEnabled() {
		return datablocksTxEnabled;
	}

	public void setDatablocksTxEnabled(boolean datablocksTxEnabled) {
		this.datablocksTxEnabled = datablocksTxEnabled;
	}
	
	public int getOfflineDelay() {
		return offlineDelay;
	}

	public void setOfflineDelay(int offlineDelay) {
		this.offlineDelay = offlineDelay;
	}

	public int getTagDetectedActivationTime() {
		return tagDetectedActivationTime;
	}

	public void setTagDetectedActivationTime(int tagDetectedActivationTime) {
		this.tagDetectedActivationTime = tagDetectedActivationTime;
	}

	public int getNotificationReadValidTime() {
		return notificationReadValidTime;
	}

	public void setNotificationReadValidTime(int notificationReadValidTime) {
		this.notificationReadValidTime = notificationReadValidTime;
	}

}
