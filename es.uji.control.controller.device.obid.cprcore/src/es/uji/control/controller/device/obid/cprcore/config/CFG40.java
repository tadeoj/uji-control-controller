package es.uji.control.controller.device.obid.cprcore.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.feig.ReaderConfig.HostInterface;
import es.uji.control.controller.device.obid.cprcore.CPRAbstract;
import es.uji.control.controller.mifare.MifareControllerException;

public class CFG40 {
	
	final static public int REGISTER = 40;
	
	private CPRAbstract cpr;
	private MemoryType memoryType;
	private boolean dirty = false;
	
	private InetAddress ipAddress;
	private int ipPort;
	
	static public CFG40 readRegister(CPRAbstract cpr, MemoryType memoryType) throws MifareControllerException {
		
		cpr.readConfigurationRegister(REGISTER, memoryType);
		
		try {
			return new CFG40(
				cpr,
				memoryType,
				InetAddress.getByAddress(cpr.getDriver().getConfigParaAsByteArray(HostInterface.LAN.IPv4.IPAddress, memoryType.getValue())),
				cpr.getDriver().getConfigParaAsInteger(HostInterface.LAN.PortNumber, memoryType.getValue())
			);
		} catch (UnknownHostException e) {
			throw new MifareControllerException("Imposible transformar la configuracion es una InetAddress", e);
		}

	}
	
	private CFG40(
			CPRAbstract cpr,
			MemoryType memoryType,
			InetAddress ipAddress,
			int ipPort
			) {
		this.cpr = cpr;
		this.memoryType = memoryType;
		this.ipAddress = ipAddress;
		this.ipPort = ipPort;
	}
	
	public void writeRegister() throws MifareControllerException {
		cpr.getDriver().setConfigPara(HostInterface.LAN.IPv4.IPAddress, getIpAddress().getAddress(), memoryType.getValue());
		cpr.getDriver().setConfigPara(HostInterface.LAN.PortNumber, getIpPort(), memoryType.getValue());
		cpr.writeConfigurationRegister(REGISTER, memoryType);
		dirty = false;
	}
	
	public MemoryType getMemoryType() {
		return this.memoryType;
	}

	public InetAddress getIpAddress() {
		return this.ipAddress;
	}
	
	public void setDestinationAddress(InetAddress ipAddress) {
		if (!ipAddress.equals(this.ipAddress)) {
			this.ipAddress = ipAddress;
			dirty = true;
		}
	}
	
	public int getIpPort() {
		return ipPort;
	}
	
	public void setIpPort(int ipPort) {
		if (ipPort != getIpPort()) {
			this.ipPort = ipPort;;
			dirty = true;
		}
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(String.format("%s: %s\n", HostInterface.LAN.IPv4.IPAddress, getIpAddress()));
		buffer.append(String.format("%s: %d\n", HostInterface.LAN.PortNumber, getIpPort()));
		
		return buffer.toString();
	}

}
