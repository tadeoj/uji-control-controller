package es.uji.control.controller.device.obid.cprcore.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.feig.ReaderConfig.HostInterface;
import es.uji.control.controller.device.obid.cprcore.CPRAbstract;
import es.uji.control.controller.mifare.MifareControllerException;

public class CFG41 {
	
	final static public int REGISTER = 41;
	
	private CPRAbstract cpr;
	private MemoryType memoryType;
	private boolean dirty = false;
	
	private InetAddress subnetMask;
	private InetAddress gatewayAddress;
	private boolean dhcpEnabled;
	private boolean keepAliveEnabled;
	private int keepAliveRetries;
	private int keepAliveIntervalTime;
	
	
	static public CFG41 readRegister(CPRAbstract cpr, MemoryType memoryType) throws MifareControllerException {
		
		cpr.readConfigurationRegister(REGISTER, memoryType);
		
		try {
			return new CFG41(
				cpr,
				memoryType,
				InetAddress.getByAddress(cpr.getDriver().getConfigParaAsByteArray(HostInterface.LAN.IPv4.SubnetMask, memoryType.getValue())),
				InetAddress.getByAddress(cpr.getDriver().getConfigParaAsByteArray(HostInterface.LAN.IPv4.GatewayAddress, memoryType.getValue())),
				cpr.getDriver().getConfigParaAsBoolean(HostInterface.LAN.IPv4.Enable_DHCP, memoryType.getValue()),
				cpr.getDriver().getConfigParaAsBoolean(HostInterface.LAN.Keepalive.Enable, memoryType.getValue()),
				cpr.getDriver().getConfigParaAsInteger(HostInterface.LAN.Keepalive.RetransmissionCount, memoryType.getValue()),
				cpr.getDriver().getConfigParaAsInteger(HostInterface.LAN.Keepalive.IntervalTime, memoryType.getValue())
			);
		} catch (UnknownHostException e) {
			throw new MifareControllerException("Imposible transformar la configuracion es una InetAddress", e);
		}

	}
	
	private CFG41(
			CPRAbstract cpr,
			MemoryType memoryType,
			InetAddress subnetMask,
			InetAddress gatewayAddress,
			boolean dhcpEnabled,
			boolean keepAliveEnabled,
			int keepAliveRetries,
			int keepAliveIntervalTime
			) {
		this.cpr = cpr;
		this.memoryType = memoryType;
		this.subnetMask = subnetMask;
		this.gatewayAddress = gatewayAddress;
		this.dhcpEnabled = dhcpEnabled;		
		this.keepAliveEnabled = keepAliveEnabled;
		this.keepAliveRetries = keepAliveRetries;
		this.keepAliveIntervalTime = keepAliveIntervalTime;
	}
	
	public void writeRegister() throws MifareControllerException {
		cpr.getDriver().setConfigPara(HostInterface.LAN.IPv4.SubnetMask, getSubnetMask().getAddress(), memoryType.getValue());
		cpr.getDriver().setConfigPara(HostInterface.LAN.IPv4.GatewayAddress, getGatewayAddress().getAddress(), memoryType.getValue());
		cpr.getDriver().setConfigPara(HostInterface.LAN.IPv4.Enable_DHCP, isDhcpEnabled(), memoryType.getValue());
		cpr.getDriver().setConfigPara(HostInterface.LAN.Keepalive.Enable, isKeepAliveEnabled(), memoryType.getValue());
		cpr.getDriver().setConfigPara(HostInterface.LAN.Keepalive.RetransmissionCount, getKeepAliveRetries(), memoryType.getValue());
		cpr.getDriver().setConfigPara(HostInterface.LAN.Keepalive.IntervalTime, getKeepAliveIntervalTime(), memoryType.getValue());
		cpr.writeConfigurationRegister(REGISTER, memoryType);
		dirty = false;
	}
	
	public MemoryType getMemoryType() {
		return this.memoryType;
	}

	public InetAddress getSubnetMask() {
		return this.subnetMask;
	}
	
	public void setSubnetMask(InetAddress subnetMask) {
		if (!subnetMask.equals(this.subnetMask)) {
			this.subnetMask = subnetMask;
			dirty = true;
		}
	}
	
	public InetAddress getGatewayAddress() {
		return gatewayAddress;
	}
	
	public void setGatewayAddress(InetAddress gatewayAddress) {
		if (!gatewayAddress.equals(this.gatewayAddress)) {
			this.gatewayAddress = gatewayAddress;
			dirty = true;
		}
	}
	
	public boolean isDhcpEnabled() {
		return this.dhcpEnabled;
	}
	
	public void setDhcpEnabled(boolean enabled) {
		if (dhcpEnabled != enabled) {
			this.dhcpEnabled = enabled;
			dirty = true;
		}
	}
	
	public boolean isKeepAliveEnabled() {
		return this.keepAliveEnabled;
	}
	
	public void setKeepAliveEnabled(boolean enabled) {
		if (keepAliveEnabled != enabled) {
			this.keepAliveEnabled = enabled;
			dirty = true;
		}
	}

	public int getKeepAliveRetries() {
		return this.keepAliveRetries;
	}
	
	public void setKeepAliveRetries(int retries) {
		if (keepAliveRetries != retries) {
			this.keepAliveRetries = retries;
			dirty = true;
		}
	}
	
	public int getKeepAliveIntervalTime() {
		return this.keepAliveIntervalTime;
	}
	
	public void setKeepAliveIntervalTime(int time) {
		if (keepAliveIntervalTime != time) {
			this.keepAliveIntervalTime  = time;
			dirty = true;
		}
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(String.format("%s: %s\n", HostInterface.LAN.IPv4.SubnetMask, getSubnetMask()));
		buffer.append(String.format("%s: %s\n", HostInterface.LAN.IPv4.GatewayAddress, getGatewayAddress()));
		buffer.append(String.format("%s: %b\n", HostInterface.LAN.IPv4.Enable_DHCP, isDhcpEnabled()));
		buffer.append(String.format("%s: %b\n", HostInterface.LAN.Keepalive.Enable, isKeepAliveEnabled()));
		buffer.append(String.format("%s: %d\n", HostInterface.LAN.Keepalive.RetransmissionCount, getKeepAliveRetries()));
		buffer.append(String.format("%s: %d\n", HostInterface.LAN.Keepalive.IntervalTime, getKeepAliveIntervalTime()));
		
		return buffer.toString();
	}

}
