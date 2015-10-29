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
import es.uji.control.controller.device.obid.cprcore.config.MemoryType;
import es.uji.control.controller.device.obid.cprcore.config.CFG02.OutputMode;
import es.uji.control.controller.mifare.MifareControllerException;

public class CPR5010HostProfile extends CPRAbstractProfile {
	
	private CPR5010HostParameters parameters;
	
	public CPR5010HostProfile(CPR5010HostParameters parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public void configTables() throws MifareControllerException {
		if (profileTableConfiguration.getISOTableSize() != 128) {
			profileTableConfiguration.setISOTableSize(128);
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
	}
	
	private void checkMode(CFG01 cfg01) throws MifareControllerException {
		int count = 0;
		count += !cfg01.isNotificationModeEnabled() ? 0 : 1;
		/*
		if (count > 0) {
			cfg01.setNotificationModeEnabled(false);
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
		count += cfg02.getOnlineStateBlue() == OutputMode.FLASHING_SLOW ? 0 : 1;
		count += cfg02.getOnlineStateGreen() == OutputMode.OFF ? 0 : 1;
		count += cfg02.getOnlineStateRed() == OutputMode.OFF ? 0 : 1;
		count += cfg02.getOnlineStateBuzzer() == OutputMode.OFF ? 0 : 1;
		/*count += cfg02.getTagDetectedStateBlue() != OutputMode.ON ? 0 : 1;
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
			cfg02.setOnlineStateBlue(OutputMode.FLASHING_SLOW);
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

}
