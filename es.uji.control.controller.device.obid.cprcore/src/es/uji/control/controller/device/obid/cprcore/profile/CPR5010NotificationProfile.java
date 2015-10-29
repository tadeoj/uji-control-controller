/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore.profile;

import es.uji.control.controller.device.obid.cprcore.config.CFG01;
import es.uji.control.controller.device.obid.cprcore.config.CFG02;
import es.uji.control.controller.device.obid.cprcore.config.CFG03;
import es.uji.control.controller.device.obid.cprcore.config.CFG11;
import es.uji.control.controller.device.obid.cprcore.config.CFG12;
import es.uji.control.controller.device.obid.cprcore.config.CFG15;
import es.uji.control.controller.device.obid.cprcore.config.CFG49;
import es.uji.control.controller.device.obid.cprcore.config.MemoryType;
import es.uji.control.controller.device.obid.cprcore.config.CFG02.OutputMode;
import es.uji.control.controller.mifare.MifareControllerException;

public class CPR5010NotificationProfile extends CPRAbstractProfile {
	
	private CPR5010NotificationParameters parameters;
	
	public CPR5010NotificationProfile(CPR5010NotificationParameters parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public void configTables() throws MifareControllerException {
		if (profileTableConfiguration.getBRMTableSize() != 256) {
			profileTableConfiguration.setBRMTableSize(256);
		}
	}
	
	@Override
	public void configRegisters() throws MifareControllerException {
		
		CFG01 cfg01 = profileRegisterConfiguration.getCFG01(MemoryType.EEPROM);
		checkMode(cfg01);
		
		CFG02 cfg02 = profileRegisterConfiguration.getCFG02(MemoryType.EEPROM);
		checkIOConfig(cfg02);
		
		CFG03 cfg03 = profileRegisterConfiguration.getCFG03(MemoryType.EEPROM);
		checkRFInterfaceConfig(cfg03);
		
		CFG11 cfg11 = profileRegisterConfiguration.getCFG11(MemoryType.EEPROM);
		checkNotificationReadMode(cfg11);
		
		CFG12 cfg12 = profileRegisterConfiguration.getCFG12(MemoryType.EEPROM);
		checkNotificationValidTime(cfg12);
		
		CFG15 cfg15 = profileRegisterConfiguration.getCFG15(MemoryType.EEPROM);
		checkNotificationAuth(cfg15);
		
		CFG49 cfg49 = profileRegisterConfiguration.getCFG49(MemoryType.EEPROM);
		checkNotificationDestination(cfg49);
		
		if (cfg01.isDirty()) {
			cfg01.writeRegister();
			needCpuReset();
		}
		if (cfg02.isDirty()) {
			cfg02.writeRegister();
			needCpuReset();
		}
		if (cfg03.isDirty()) {
			cfg03.writeRegister();
			needCpuReset();
		}
		if (cfg11.isDirty()) {
			cfg11.writeRegister();
			needCpuReset();
		}
		if (cfg12.isDirty()) {
			cfg12.writeRegister();
			needCpuReset();
		}
		if (cfg15.isDirty()) {
			cfg15.writeRegister();
			needCpuReset();
		}
		if (cfg49.isDirty()) {
			cfg49.writeRegister();
			needCpuReset();
		}
	}
	
	private void checkMode(CFG01 cfg01) throws MifareControllerException {
		int count = 0;
		count += cfg01.isNotificationModeEnabled() ? 0 : 1;
		/*
		if (count > 0) {
			cfg01.setNotificationModeEnabled(true);
		}
		*/	
	}
	
	private void checkIOConfig(CFG02 cfg02) throws MifareControllerException {
		int count = 0;
		count += cfg02.getOfflineStateBlue() == OutputMode.OFF ? 0 : 1;
		count += cfg02.getOfflineStateGreen() == OutputMode.OFF ? 0 : 1;
		count += cfg02.getOfflineStateRed() == OutputMode.OFF ? 0 : 1;
		count += cfg02.getOfflineStateBuzzer() == OutputMode.OFF ? 0 : 1;
		count += cfg02.getOffLineDelay() == parameters.getOfflineDelay() ? 0 : 1;
		count += cfg02.getOnlineStateBlue() == OutputMode.OFF ? 0 : 1;
		count += cfg02.getOnlineStateGreen() == OutputMode.OFF ? 0 : 1;
		count += cfg02.getOnlineStateRed() == OutputMode.OFF ? 0 : 1;
		count += cfg02.getOnlineStateBuzzer() == OutputMode.OFF ? 0 : 1;
		/*count += cfg02.getTagDetectedStateBlue() != OutputMode.OFF ? 0 : 1;
		count += cfg02.getTagDetectedStateGreen() == OutputMode.OFF ? 0 : 1;
		count += cfg02.getTagDetectedStateRed() == OutputMode.OFF ? 0 : 1;
		count += cfg02.getTagDetectedStateBuzzer() == OutputMode.OFF ? 0 : 1;
		count += cfg02.getTagDetectedActivationTime() == parameters.getTagDetectedActivationTime() ? 0 : 1;*/
		if (count > 0) {
			cfg02.setOfflineStateBlue(OutputMode.OFF);
			cfg02.setOfflineStateGreen(OutputMode.OFF);
			cfg02.setOfflineStateRed(OutputMode.OFF);
			cfg02.setOfflineStateBuzzer(OutputMode.OFF);
			cfg02.setOffLineDelay(parameters.getOfflineDelay());
			cfg02.setOnlineStateBlue(OutputMode.OFF);
			cfg02.setOnlineStateGreen(OutputMode.OFF);
			cfg02.setOnlineStateRed(OutputMode.OFF);
			cfg02.setOnlineStateBuzzer(OutputMode.OFF);
			/*cfg02.setTagDetectedStateBlue(OutputMode.ON);
			cfg02.setTagDetectedStateGreen(OutputMode.OFF);
			cfg02.setTagDetectedStateRed(OutputMode.OFF);
			cfg02.setTagDetectedStateBuzzer(OutputMode.OFF);
			cfg02.setTagDetectedActivationTime(parameters.getTagDetectedActivationTime());*/
		}
	}
	
	private void checkRFInterfaceConfig(CFG03 cfg03) throws MifareControllerException {
		int count = 0;
		count += cfg03.isOptionalInventoryInfoEnabled() ? 0 : 1;
		if (count > 0) {
			cfg03.setOptionalInventoryInfoEnabled(true);
		}
	}
	
	private void checkNotificationReadMode(CFG11 cfg11) throws MifareControllerException {
		int count = 0;
		count += !cfg11.isSerialNumberTxEnabled() ? 1 : 0;
		count += cfg11.isDatablocksTxEnabled() == parameters.isDatablocksTxEnabled()? 0 : 1;
		count += cfg11.getDatablockAddress() == parameters.getDatablockAddress() ? 0 : 1;
		count += cfg11.getDatablockNumber() == parameters.getDatablockNumber() ? 0 : 1;
		count += cfg11.getByteOrder() == CFG11.ByteOrder.LSB ? 0 : 1;
		if (count > 0) {
			cfg11.setSerialNumberTxEnabled(true);
			cfg11.setDatablocksTxEnabled(parameters.isDatablocksTxEnabled());
			cfg11.setDatablockAddress(parameters.getDatablockAddress());
			cfg11.setDatablockNumber(parameters.getDatablockNumber());
			cfg11.setByteOrder(CFG11.ByteOrder.LSB);
		}
	}

	private void checkNotificationValidTime(CFG12 cfg12) throws MifareControllerException {
		int count = 0;
		count += cfg12.getValidTime() != parameters.getNotificationReadValidTime() ? 1 : 0;
		if (count > 0) {
			cfg12.setValidTime(parameters.getNotificationReadValidTime());
		}
	}

	private void checkNotificationAuth(CFG15 cfg15) throws MifareControllerException {
		int count = 0;
		count += cfg15.getMifareKeyType() != parameters.getMifareKeyType() ? 1 : 0;
		count += cfg15.getKeyAddress() != parameters.getKeyAddress() ? 1 : 0;
		if (count > 0) {
			cfg15.setKeyType(parameters.getMifareKeyType());
			cfg15.setKeyAddress(parameters.getKeyAddress());
		}
	}

	private void checkNotificationDestination(CFG49 cfg49) throws MifareControllerException {
		int count = 0;
		count += cfg49.getDestinationPort() != parameters.getDestinationPort() ? 1 : 0;
		count += cfg49.getDestinationAddress().equals(parameters.getDestAddress())  ? 1 : 0;
		count += cfg49.getConnectionHoldTime() != parameters.getHoldTime() ? 1 : 0;
		
		// TODO: Pendiente de la nueva version del SDK
		//keepAliveEnabled;
		//keepAliveTime;

		if (count > 0) {
			cfg49.setDestinationPort(parameters.getDestinationPort());
			cfg49.setDestinationAddress(parameters.getDestAddress());
			cfg49.setConnectionHoldTime(parameters.getHoldTime());
		}
	}

}
