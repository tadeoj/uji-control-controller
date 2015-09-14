package es.uji.control.controller.device.obid.cprcore.config;

import de.feig.ReaderConfig.OperatingMode;
import es.uji.control.controller.device.obid.cprcore.CPRAbstract;
import es.uji.control.controller.mifare.MifareControllerException;
import es.uji.control.controller.mifare.MifareKeyType;

public class CFG15 {
	
	final static public int REGISTER = 15;
	
	private CPRAbstract cpr;
	private MemoryType memoryType;
	private boolean dirty = false;
	
	private MifareKeyType mifareKeyType;
	private int keyAddress;
	
	static public CFG15 readRegister(CPRAbstract cpr, MemoryType memoryType) throws MifareControllerException {
		
		cpr.readConfigurationRegister(REGISTER, memoryType);
		
		return new CFG15(
			cpr,
			memoryType,
			toMifareKeyType(cpr.getDriver().getConfigParaAsBoolean(OperatingMode.NotificationMode.DataSource.Mifare.Classic.KeyType, memoryType.getValue())),
			cpr.getDriver().getConfigParaAsInteger(OperatingMode.NotificationMode.DataSource.Mifare.Classic.KeyAddress, memoryType.getValue())
		);

	}
	
	private CFG15(
			CPRAbstract cpr,
			MemoryType memoryType,
			MifareKeyType mifareKeyType,
			int keyAddress
			) {
		this.cpr = cpr;
		this.memoryType = memoryType;
		this.mifareKeyType = mifareKeyType;
		this.keyAddress = keyAddress;
	}
	
	public void writeRegister() throws MifareControllerException {
		cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.DataSource.Mifare.Classic.KeyType, fromMifareKeyType(getMifareKeyType()), memoryType.getValue());
		cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.DataSource.Mifare.Classic.KeyAddress, getKeyAddress(), memoryType.getValue());
		cpr.writeConfigurationRegister(REGISTER, memoryType);
		dirty = false;
	}
	
	static private MifareKeyType toMifareKeyType(boolean type) {
		return type ? MifareKeyType.KEY_B : MifareKeyType.KEY_A;
	}
	
	private boolean fromMifareKeyType(MifareKeyType mifareKeyType) {
		return mifareKeyType == MifareKeyType.KEY_A ? false : true;
	}
	
	public MemoryType getMemoryType() {
		return this.memoryType;
	}

	public MifareKeyType getMifareKeyType() {
		return mifareKeyType;
	}
	
	public void setKeyType(MifareKeyType mifareKeyType) {
		if (mifareKeyType != getMifareKeyType()) {
			this.mifareKeyType = mifareKeyType;
			dirty = true;
		}
	}
	
	public int getKeyAddress() {
		return this.keyAddress;
	}

	public void setKeyAddress(int address) {
		if (getKeyAddress() != address) {
			keyAddress = address;
			dirty = true;
		}
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(String.format("%s: %s\n", OperatingMode.NotificationMode.DataSource.Mifare.Classic.KeyType, getMifareKeyType().toString()));
		buffer.append(String.format("%s: %d\n", OperatingMode.NotificationMode.DataSource.Mifare.Classic.KeyAddress, getKeyAddress()));
		
		return buffer.toString();
	}

}
