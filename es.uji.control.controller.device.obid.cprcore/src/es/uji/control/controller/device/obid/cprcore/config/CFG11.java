/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore.config;

import de.feig.ReaderConfig.OperatingMode;
import es.uji.control.controller.device.obid.cprcore.CPRAbstract;
import es.uji.control.controller.mifare.MifareControllerException;

public class CFG11 {
	
	public enum ByteOrder {
		
		MSB (false),
		LSB (true);
		
		private boolean id;
		
		private ByteOrder(boolean id) {
			this.id = id;
		}
		
		public boolean getId() {
			return id;
		}
		
		public static ByteOrder getById(boolean value) throws IllegalArgumentException {
			for (ByteOrder byteOrder : ByteOrder.values()) {
				if (value == byteOrder.getId())
					return byteOrder;
			}
			throw new IllegalArgumentException("Invalid Id to build CFG15.OutputMode.KeyType");
		}

	}

	final static public int REGISTER = 11;
	
	private CPRAbstract cpr;
	private MemoryType memoryType;
	private boolean dirty = false;
	
	private boolean serialNumberTxEnabled;
	private boolean datablocksTxEnabled;
	private ByteOrder byteOrder;
	private int datablockAddress;
	private int datablockNumber;
	
	static public CFG11 readRegister(CPRAbstract cpr, MemoryType memoryType) throws MifareControllerException {
		
		cpr.readConfigurationRegister(REGISTER, memoryType);
		
		return new CFG11(
			cpr,
			memoryType,
			cpr.getDriver().getConfigParaAsBoolean(OperatingMode.NotificationMode.DataSelector.UID, memoryType.getValue()),
			cpr.getDriver().getConfigParaAsBoolean(OperatingMode.NotificationMode.DataSelector.Data, memoryType.getValue()),
			ByteOrder.getById(cpr.getDriver().getConfigParaAsBoolean(OperatingMode.NotificationMode.DataSource.ByteOrderOfData, memoryType.getValue())),
			cpr.getDriver().getConfigParaAsInteger(OperatingMode.NotificationMode.DataSource.FirstDataBlock, memoryType.getValue()),
			cpr.getDriver().getConfigParaAsInteger(OperatingMode.NotificationMode.DataSource.NoOfDataBlocks, memoryType.getValue())
		);

	}
	
	private CFG11(
			CPRAbstract cpr,
			MemoryType memoryType,
			boolean serialNumberTxEnabled,
			boolean datablocksTxEnabled,
			ByteOrder byteOrder,
			int datablockAddress,
			int datablockNumber
			) {
		this.cpr = cpr;
		this.memoryType = memoryType;
		this.serialNumberTxEnabled = serialNumberTxEnabled;
		this.datablocksTxEnabled = datablocksTxEnabled;
		this.byteOrder = byteOrder;
		this.datablockAddress = datablockAddress;
		this.datablockNumber = datablockNumber;
	}
	
	public void writeRegister() throws MifareControllerException {
		cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.DataSelector.UID, isSerialNumberTxEnabled(), memoryType.getValue());
		cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.DataSelector.Data, isDatablocksTxEnabled(), memoryType.getValue());
		cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.DataSource.ByteOrderOfData, getByteOrder().getId(), memoryType.getValue());
		cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.DataSource.FirstDataBlock, getDatablockAddress(), memoryType.getValue());
		cpr.getDriver().setConfigPara(OperatingMode.NotificationMode.DataSource.NoOfDataBlocks, getDatablockNumber(), memoryType.getValue());
		cpr.writeConfigurationRegister(REGISTER, memoryType);
		dirty = false;
	}
	
	public boolean isSerialNumberTxEnabled() {
		return serialNumberTxEnabled;
	}

	public void setSerialNumberTxEnabled(boolean serialNumberTxEnabled) {
		if (serialNumberTxEnabled != isSerialNumberTxEnabled()) {
			this.serialNumberTxEnabled = serialNumberTxEnabled;
			dirty = true;
		}
	}

	public boolean isDatablocksTxEnabled() {
		return datablocksTxEnabled;
	}

	public void setDatablocksTxEnabled(boolean datablocksTxEnabled) {
		if (datablocksTxEnabled != isDatablocksTxEnabled()) {
			this.datablocksTxEnabled = datablocksTxEnabled;
			dirty = true;
		}
	}

	public ByteOrder getByteOrder() {
		return byteOrder;
	}

	public void setByteOrder(ByteOrder byteOrder) {
		if (byteOrder != getByteOrder()) {
			this.byteOrder = byteOrder;
			dirty = true;
		}
	}

	public int getDatablockAddress() {
		return datablockAddress;
	}

	public void setDatablockAddress(int datablockAddress) {
		if (datablockAddress != getDatablockAddress()) {
			this.datablockAddress = datablockAddress;
			dirty = true;
		}
	}

	public int getDatablockNumber() {
		return datablockNumber;
	}

	public void setDatablockNumber(int datablockNumber) {
		if (datablockNumber != getDatablockNumber()) {
			this.datablockNumber = datablockNumber;
			dirty = true;
		}
	}

	public MemoryType getMemoryType() {
		return this.memoryType;
	}

	public boolean isDirty() {
		return dirty;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("%s: %b\n", OperatingMode.NotificationMode.DataSelector.UID, isSerialNumberTxEnabled()));
		buffer.append(String.format("%s: %b\n", OperatingMode.NotificationMode.DataSelector.Data, isDatablocksTxEnabled()));
		buffer.append(String.format("%s: %s\n", OperatingMode.NotificationMode.DataSource.ByteOrderOfData, getByteOrder().toString()));
		buffer.append(String.format("%s: %d\n", OperatingMode.NotificationMode.DataSource.FirstDataBlock, getDatablockAddress()));
		buffer.append(String.format("%s: %d\n", OperatingMode.NotificationMode.DataSource.NoOfDataBlocks, getDatablockNumber()));
		return buffer.toString();
	}

}
