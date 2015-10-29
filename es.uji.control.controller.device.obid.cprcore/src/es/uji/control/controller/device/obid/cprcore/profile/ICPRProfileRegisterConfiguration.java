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
import es.uji.control.controller.device.obid.cprcore.config.CFG40;
import es.uji.control.controller.device.obid.cprcore.config.CFG41;
import es.uji.control.controller.device.obid.cprcore.config.CFG49;
import es.uji.control.controller.device.obid.cprcore.config.MemoryType;
import es.uji.control.controller.mifare.MifareControllerException;

public interface ICPRProfileRegisterConfiguration {

	public abstract CFG01 getCFG01(MemoryType memoryType) throws MifareControllerException;

	public abstract CFG02 getCFG02(MemoryType memoryType) throws MifareControllerException;

	public abstract CFG03 getCFG03(MemoryType memoryType) throws MifareControllerException;

	public abstract CFG11 getCFG11(MemoryType memoryType) throws MifareControllerException;

	public abstract CFG12 getCFG12(MemoryType memoryType) throws MifareControllerException;

	public abstract CFG15 getCFG15(MemoryType memoryType) throws MifareControllerException;

	public abstract CFG40 getCFG40(MemoryType memoryType) throws MifareControllerException;

	public abstract CFG41 getCFG41(MemoryType memoryType) throws MifareControllerException;

	public abstract CFG49 getCFG49(MemoryType memoryType) throws MifareControllerException;

}