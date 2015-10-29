/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.feig.ReaderConfig.OperatingMode;
import es.uji.control.controller.device.obid.cprcore.CPRAbstract;
import es.uji.control.controller.mifare.MifareControllerException;

public class CFG49 {
	
	final static public int REGISTER = 49;
	
	private CPRAbstract cpr;
	private MemoryType memoryType;
	private boolean dirty = false;
	
	private InetAddress destAddress;
	private int destinationPort;
	private int holdTime;
	//private boolean keepAliveEnabled;
	//private int keepAliveTime;
	
	static public CFG49 readRegister(CPRAbstract cpr, MemoryType memoryType) throws MifareControllerException {
		
		cpr.readConfigurationRegister(REGISTER, memoryType);
		
		try {
			return new CFG49(
				cpr,
				memoryType,
				InetAddress.getByAddress(cpr.getDriver().getConfigParaAsByteArray(OperatingMode.NotificationMode.Transmission.Destination.IPv4.IPAddress, memoryType.getValue())),
				cpr.getDriver().getConfigParaAsInteger(OperatingMode.NotificationMode.Transmission.Destination.PortNumber, memoryType.getValue()),
				cpr.getDriver().getConfigParaAsInteger(OperatingMode.NotificationMode.Transmission.Destination.ConnectionHoldTime, memoryType.getValue())
				//cpr.getDriver().getConfigParaAsBoolean(OperatingMode.NotificationMode.Transmission.KeepAlive.Enable, memoryType.getValue()),
				//cpr.getDriver().getConfigParaAsInteger(OperatingMode.NotificationMode.Transmission.KeepAlive.IntervalTime, memoryType.getValue())
			);
		} catch (UnknownHostException e) {
			throw new MifareControllerException("Imposible transformar la configuracion es una InetAddress", e);
		}

	}
	
	private CFG49(
			CPRAbstract cpr,
			MemoryType memoryType,
			InetAddress destAddress,
			int destinationPort,
			int holdTime
			//boolean keepAliveEnabled,
			//int keepAliveTime
			) {
		this.cpr = cpr;
		this.memoryType = memoryType;
		this.destAddress = destAddress;
		this.destinationPort = destinationPort;
		this.holdTime = holdTime;
		//this.keepAliveEnabled = keepAliveEnabled;
		//this.keepAliveTime = keepAliveTime;
	}
	
	public void writeRegister() throws MifareControllerException {
		cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.Transmission.Destination.IPv4.IPAddress, getDestinationAddress().getAddress(), memoryType.getValue());
		cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.Transmission.Destination.PortNumber, getDestinationPort(), memoryType.getValue());
		cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.Transmission.Destination.ConnectionHoldTime, getConnectionHoldTime(), memoryType.getValue());
		//cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.Transmission.KeepAlive.Enable, isKeepAliveEnabled(), memoryType.getValue());
		//cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.Transmission.KeepAlive.IntervalTime, getKeepAliveIntervalTime(), memoryType.getValue());
		cpr.writeConfigurationRegister(REGISTER, memoryType);
		dirty = false;
	}
	
	public MemoryType getMemoryType() {
		return this.memoryType;
	}

	public InetAddress getDestinationAddress() {
		return this.destAddress;
	}
	
	public void setDestinationAddress(InetAddress destinationAddress) {
		if (!destinationAddress.equals(this.destAddress)) {
			this.destAddress = destinationAddress;
			dirty = true;
		}
	}
	
	public int getDestinationPort() {
		return destinationPort;
	}
	
	public void setDestinationPort(int destionationPort) {
		if (destionationPort != getDestinationPort()) {
			this.destinationPort = destionationPort;;
			dirty = true;
		}
	}
	
	public int getConnectionHoldTime() {
		return this.holdTime;
	}
	
	public void setConnectionHoldTime(int holdTime) {
		if (holdTime != this.holdTime) {
			this.holdTime = holdTime;
			dirty = true;
		}
	}
	
	/*
	public boolean isKeepAliveEnabled() {
		return this.keepAliveEnabled;
	}
	
	public void setKeepAliveEnabled(boolean enabled) {
		if (this.keepAliveEnabled != enabled) {
			this.keepAliveEnabled = enabled;
			dirty = true;
		}
	}
	
	public int getKeepAliveIntervalTime() {
		return this.keepAliveTime;
	}
	
	public void setKeepAliveIntervalTime(int intervalTime) {
		if (this.keepAliveTime != intervalTime) {
			this.keepAliveTime = intervalTime;
			dirty = true;
		}
	}
	*/

	public boolean isDirty() {
		return dirty;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(String.format("%s: %s\n", OperatingMode.NotificationMode.Transmission.Destination.IPv4.IPAddress, getDestinationAddress()));
		buffer.append(String.format("%s: %d\n", OperatingMode.NotificationMode.Transmission.Destination.PortNumber, getDestinationPort()));
		buffer.append(String.format("%s: %d\n", OperatingMode.NotificationMode.Transmission.Destination.ConnectionHoldTime, getConnectionHoldTime()));
		//buffer.append(String.format("%s: %d\n", OperatingMode.NotificationMode.Transmission.KeepAlive.Enable, isKeepAliveEnabled()));
		//buffer.append(String.format("%s: %d\n", OperatingMode.NotificationMode.Transmission.KeepAlive.IntervalTime, getKeepAliveIntervalTime()));
		
		return buffer.toString();
	}

}
