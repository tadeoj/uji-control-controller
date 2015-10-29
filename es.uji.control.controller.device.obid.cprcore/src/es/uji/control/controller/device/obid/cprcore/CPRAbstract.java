/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.feig.FeHexConvert;
import de.feig.FePortDriverException;
import de.feig.FeReaderDriverException;
import de.feig.Fedm;
import de.feig.FedmException;
import de.feig.FedmIscReader;
import de.feig.FedmIscReaderConst;
import de.feig.FedmIscReaderID;
import de.feig.FedmIscReaderInfo;
import de.feig.FedmIsoTableItem;
import es.uji.control.controller.core.service.OutputBuilder;
import es.uji.control.controller.core.service.OutputsInfo;
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
import es.uji.control.controller.device.obid.cprcore.profile.ICPRProfile;
import es.uji.control.controller.device.obid.cprcore.profile.ICPRProfileRegisterConfiguration;
import es.uji.control.controller.device.obid.cprcore.profile.ICPRProfileTableConfiguration;
import es.uji.control.controller.mifare.MifareControllerException;
import es.uji.control.controller.mifare.MifareControllerExceptionEnum;
import es.uji.control.controller.mifare.MifareKey;
import es.uji.control.controller.mifare.MifareKeyType;
import es.uji.control.controller.mifare.MifareTagId;
import es.uji.control.controller.mifare.MifareTagType;
import es.uji.control.controller.mifare.SimpleMifareTagId;

abstract public class CPRAbstract implements FedmIscReaderID,
		FedmIscReaderConst {

	// Driver para gestionar el lector.
	protected FedmIscReader driver;

	private ICPRProfileRegisterConfiguration profileRegisterConfiguration;
	private ICPRProfileTableConfiguration profileTableConfiguration;

	private ICPRProfile profile;

	public CPRAbstract() {
		this.profileRegisterConfiguration = new CPRProfileRegisterConfigurationImpl();
		this.profileTableConfiguration = new CPRProfileTableConfigurationImpl();
	}

	public void start() throws MifareControllerException {
		try {
			// Instanciamos el driver
			FedmIscReader localDriver = new FedmIscReader();
			// Todo a funcionado correctamente, el reader esta operativo
			driver = localDriver;
		} catch (FedmException e) {
			throw new MifareControllerException("Imposible abrir el lector.", e);
		} catch (Exception e) {
			throw new MifareControllerException("Imposible abrir el lector.", e);
		}
	}

	public void stop() throws MifareControllerException {
		disconnect();
		if (driver != null) {
			// Liberamos recursos
			driver.destroy();
			driver = null;
		}
	}

	public void disconnect() throws MifareControllerException {
		try {
			if (driver != null) {
				// Tiene que estar abierto.
				if (driver.isConnected()) {
					// Desconectamos el lector de el puerto USB
					driver.disConnect();
				}
			}
		} catch (FePortDriverException e) {
			throw new MifareControllerException(
					"Problemas al cerrar el lector.", e);
		} catch (FedmException e) {
			throw new MifareControllerException(
					"Problemas al cerrar el lector.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException(
					"Problemas al cerrar el lector.", e);
		}
	}

	public FedmIscReader getDriver() {
		return driver;
	}

	public void setProfile(ICPRProfile profile)
			throws MifareControllerException {
		this.profile = profile;
		this.profile.init(this, profileRegisterConfiguration,
				profileTableConfiguration);
		this.profile.configRegisters();
		this.profile.configTables();
		this.profile.finish();
	}

	public ICPRProfile getProfile() {
		return this.profile;
	}

	protected void checkReader() {
		if (driver == null) {
			throw new IllegalStateException("MifareReader isn't open.");
		}
	}

	protected int checkStatus(int handler) throws FeSetException,
			FeHandlerException {
		return checkSend(handler, null).getId();
	}

	protected StatusByteEnum checkSend(int handler,
			StatusByteEnum[] correctStatus) throws FeSetException,
			FeHandlerException {
		if (handler < 0)
			throw new FeSetException(handler, driver.getErrorText(handler));
		else {
			StatusByteEnum currentStatus = StatusByteEnum.getById(handler);
			if (currentStatus == null)
				currentStatus = StatusByteEnum.UNKNOWN_STATUS;
			if (currentStatus == StatusByteEnum.OK)
				return currentStatus;
			if (correctStatus != null) {
				for (int i = 0; i < correctStatus.length; i++) {
					if (correctStatus[i] == currentStatus)
						return currentStatus;
				}
			}
			throw new FeHandlerException(currentStatus);
		}
	}

	protected boolean checkSet(int code) throws FeSetException {
		switch (code) {
		case Fedm.MODIFIED:
			return true;
		case Fedm.OK:
			return false;
		default:
			throw new FeSetException(code, driver.getErrorText(code));
		}
	}

	protected int checkFind(int code) throws FeSetException {
		if (code < 0)
			throw new FeSetException(code, driver.getErrorText(code));
		return code;
	}

	public void cpuReset() throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();
			// Se enviar el comando para resetear CPU
			checkStatus(driver.sendProtocol((byte) 0x63));
		} catch (FeHandlerException e) {
			throw new MifareControllerException("Imposible resetear la CPU.", e);
		} catch (FeSetException e) {
			throw new MifareControllerException("Imposible resetear la CPU.", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException("Imposible resetear la CPU.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException("Imposible resetear la CPU.", e);
		} catch (FedmException e) {
			throw new MifareControllerException("Imposible resetear la CPU.", e);
		}
	}

	public void rfReset() throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();
			// Se enviar el comando para resetear transponders
			checkStatus(driver.sendProtocol((byte) 0x69));
		} catch (FeHandlerException e) {
			throw new MifareControllerException(
					"Imposible resetear los transponders.", e);
		} catch (FeSetException e) {
			throw new MifareControllerException(
					"Imposible resetear los transponders.", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException(
					"Imposible resetear los transponders.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException(
					"Imposible resetear los transponders.", e);
		} catch (FedmException e) {
			throw new MifareControllerException(
					"Imposible resetear los transponders.", e);
		}
	}

	public String getSoftwareVersion() throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();
			// Se enviar el comando para resetear transponders
			checkStatus(driver.sendProtocol((byte) 0x65));
			// Se obtiene la respuesta
			return driver.getStringData(FEDM_ISC_TMP_SOFTVER);
		} catch (FeHandlerException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FeSetException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FedmException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		}
	}

	public Map<String, Map<String, String>> getInfo() {

		HashMap<String, Map<String, String>> info = new HashMap<String, Map<String, String>>();

		// Se obtiene la informacion desde el dispositivo.
		FedmIscReaderInfo deviceInfo = driver.getReaderInfo();
		if (deviceInfo == null) {
			return info;
		}

		HashMap<String, String> acController = new HashMap<String, String>();
		acController.put("[accSwVer] Major and minor version number",
				FeHexConvert
						.byteArrayToHexStringWithSpaces(deviceInfo.accSwVer));
		acController.put("[accDevVer] Developer version number",
				FeHexConvert.byteToHexString(deviceInfo.accDevVer));
		acController.put("[accHwType] Flag field with hardware options",
				FeHexConvert.byteToHexString(deviceInfo.accHwType));
		info.put("AC-CONTROLLER", acController);

		HashMap<String, String> usbController = new HashMap<String, String>();
		usbController.put("[usbSwVer] Major and minor version number",
				FeHexConvert
						.byteArrayToHexStringWithSpaces(deviceInfo.usbSwVer));
		usbController.put("[usbDevVer] Developer version numbers",
				FeHexConvert.byteToHexString(deviceInfo.usbDevVer));
		usbController.put("[usbHwType] Flag field with hardware options",
				FeHexConvert.byteToHexString(deviceInfo.usbHwType));
		info.put("USB-CONTROLLER", usbController);

		HashMap<String, String> fpgaController = new HashMap<String, String>();
		fpgaController.put("[fpgaSwVer] Major and minor version number",
				FeHexConvert
						.byteArrayToHexStringWithSpaces(deviceInfo.fpgaSwVer));
		fpgaController.put("[fpgaDevVer] Developer version number",
				FeHexConvert.byteToHexString(deviceInfo.fpgaDevVer));
		fpgaController.put("[fpgaHwType] Flag field with hardware options",
				FeHexConvert.byteToHexString(deviceInfo.fpgaHwType));
		info.put("FPGA-CONTROLLER", fpgaController);

		// TODO : Revisar cprFctList
		HashMap<String, String> cprController = new HashMap<String, String>();
		cprController.put("[cprFctTemplate] Template",
				FeHexConvert.byteToHexString(deviceInfo.cprFctTemplate));
		cprController.put("[cprFctList] Flag field with function list 0",
				FeHexConvert.byteToHexString(deviceInfo.cprFctList0));
		cprController.put("[cprFctList] Flag field with function list 1",
				FeHexConvert.byteToHexString(deviceInfo.cprFctList1));
		cprController.put("[cprFctList] Flag field with function list 2",
				FeHexConvert.byteToHexString(deviceInfo.cprFctList2));
		info.put("CPR-CONTROLLER", cprController);

		HashMap<String, String> lrLruReader = new HashMap<String, String>();
		lrLruReader.put("[fwIdentifier] Firmware identifier", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.fwIdentifier));
		info.put("LR/LRU-READER", lrLruReader);

		HashMap<String, String> bootLoader = new HashMap<String, String>();
		bootLoader.put("[bootSwVer] Major and minor version number",
				FeHexConvert
						.byteArrayToHexStringWithSpaces(deviceInfo.bootSwVer));
		info.put("BOOT-LOADER", bootLoader);

		HashMap<String, String> rfController = new HashMap<String, String>();
		rfController.put("[rfcSwVer] Major and minor version number",
				FeHexConvert
						.byteArrayToHexStringWithSpaces(deviceInfo.rfcSwVer));
		rfController.put("[rfcDevVer Developer version number",
				FeHexConvert.byteToHexString(deviceInfo.rfcDevVer));
		rfController.put("[rfcHwType] Flag field with hardware options",
				FeHexConvert.byteToHexString(deviceInfo.rfcHwType));
		rfController.put("[readerType] Software type == reader type",
				FeHexConvert.byteToHexString(deviceInfo.readerType));
		rfController.put(
				"[rfcTrType] Flag field with supported transponder types",
				FeHexConvert
						.byteArrayToHexStringWithSpaces(deviceInfo.rfcTrType));
		rfController
				.put("[rfcUhfTrType] Flag field with supported UHF transponder types, if Reader is dual-frequency (HF and UHF) Reader",
						FeHexConvert
								.byteArrayToHexStringWithSpaces(deviceInfo.rfcUhfTrType));
		rfController
				.put("[rfcRxBufferSize] Size of reader's receive buffer",
						FeHexConvert
								.byteArrayToHexStringWithSpaces(deviceInfo.rfcRxBufferSize));
		rfController
				.put("[rfcTxBufferSize] Size of reader's transmit buffer",
						FeHexConvert
								.byteArrayToHexStringWithSpaces(deviceInfo.rfcTxBufferSize));
		rfController.put("[decoderType] Decoder type information", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.decoderType));
		rfController.put("[selfTest] Self test result",
				FeHexConvert.byteToHexString(deviceInfo.selfTest));
		rfController.put("[rfcHwInfo] Flag field with hardware info",
				FeHexConvert
						.byteArrayToHexStringWithSpaces(deviceInfo.rfcHwInfo));
		rfController.put("[rfcDHw] For internal use",
				FeHexConvert.byteArrayToHexStringWithSpaces(deviceInfo.rfcDHw));
		rfController.put("[rfcAHw] For internal use",
				FeHexConvert.byteArrayToHexStringWithSpaces(deviceInfo.rfcAHw));
		rfController
				.put("[rfcFrequency] Frequency information (HF, UHF, ..., EU, FCC) from Reader",
						FeHexConvert.byteToHexString(deviceInfo.rfcFrequency));
		rfController.put("[rfcPortTypes] Flag field with supported port types",
				FeHexConvert.byteToHexString(deviceInfo.rfcPortTypes));
		rfController.put("[rfcInfo] RFC-Info byte",
				FeHexConvert.byteToHexString(deviceInfo.rfcInfo));
		rfController
				.put("[noOfCfgPages] Number of configuration pages in the reader",
						FeHexConvert
								.byteArrayToHexStringWithSpaces(deviceInfo.noOfCfgPages));
		rfController
				.put("[cfgReadPermissions] Flag field with read permissions of configuration pages",
						FeHexConvert
								.byteArrayToHexStringWithSpaces(deviceInfo.cfgReadPermissions));
		rfController
				.put("[cfgWritePermissions] Flag field with write permissions of configuration pages",
						FeHexConvert
								.byteArrayToHexStringWithSpaces(deviceInfo.cfgWritePermissions));
		rfController.put("[rfcDevID] Reader's serial number", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.rfcDevID));
		rfController
				.put("[rfcLicenseCustom] Licensed customer version",
						FeHexConvert
								.byteArrayToHexStringWithSpaces(deviceInfo.rfcLicenseCustom));
		rfController
				.put("[rfcLicenseFw] Licensed firmware version",
						FeHexConvert
								.byteArrayToHexStringWithSpaces(deviceInfo.rfcLicenseFw));
		rfController
				.put("[rfcLicenseTrType] Licensed transponder driver",
						FeHexConvert
								.byteArrayToHexStringWithSpaces(deviceInfo.rfcLicenseTrType));
		rfController.put("[rfcLicenseFct] Licensed functions", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.rfcLicenseFct));
		rfController
				.put("[rfcLicenseUhfTrType] Licensed transponder driver\n"
						+ "Flag field with licensed UHF transponder types, if Reader is dual-frequency (HF and UHF) Reader.\n"
						+ "If Reader is a single-frequency (UHR or HF) Reader, than transponder types are located in rfcLicenseTrType",
						FeHexConvert
								.byteArrayToHexStringWithSpaces(deviceInfo.rfcLicenseUhfTrType));
		rfController
				.put("[rfcLicenseUhfFct] Licensed functions, if Reader is dual-frequency (HF and UHF) Reader\n"
						+ "If Reader is a single-frequency (UHR or HF) Reader, than transponder types are located in rfcLicenseFct",
						FeHexConvert
								.byteArrayToHexStringWithSpaces(deviceInfo.rfcLicenseUhfFct));
		info.put("RF-CONTROLLER", rfController);

		HashMap<String, String> reader = new HashMap<String, String>();
		reader.put(
				"[lanMac] MAC-Address (media access control address) for LAN",
				FeHexConvert.byteArrayToHexStringWithSpaces(deviceInfo.lanMac));
		reader.put("[lanIpV4] IPv4-Address for LAN",
				FeHexConvert.byteArrayToHexStringWithSpaces(deviceInfo.lanIpV4));
		reader.put("[lanIpV6] IPv6-Address for LAN",
				FeHexConvert.byteArrayToHexStringWithSpaces(deviceInfo.lanIpV6));
		reader.put("[lanNetmaskV4] Netmask for IPv4 for LAN", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.lanNetmaskV4));
		reader.put("[lanNetmaskV6] Netmask for IPv6 for LAN", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.lanNetmaskV6));
		reader.put("[lanGatewayV4] Gateway for IPv4 for LAN", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.lanGatewayV4));
		reader.put("[lanGatewayV6] Gateway for IPv6 for LAN", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.lanGatewayV6));
		reader.put(
				"[wlanMac] MAC-Address (media access control address) for WLAN",
				FeHexConvert.byteArrayToHexStringWithSpaces(deviceInfo.wlanMac));
		reader.put("[wlanIpV4] IPv4-Address for WLAN", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.wlanIpV4));
		reader.put("[wlanIpV6] IPv6-Address for WLAN", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.wlanIpV6));
		reader.put("[wlanNetmaskV4] Netmask for IPv4 for WLAN", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.wlanNetmaskV4));
		reader.put("[wlanNetmaskV6] Netmask for IPv6 for WLAN", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.wlanNetmaskV6));
		reader.put("[wlanGatewayV4] Gateway for IPv4 for WLAN", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.wlanGatewayV4));
		reader.put("[wlanGatewayV6] Gateway for IPv6 for WLAN", FeHexConvert
				.byteArrayToHexStringWithSpaces(deviceInfo.wlanGatewayV6));
		info.put("READER", reader);

		HashMap<String, String> ioCapabilities = new HashMap<String, String>();
		ioCapabilities.put("[noOfInputs] Number of digital inputs",
				FeHexConvert.byteToHexString(deviceInfo.noOfInputs));
		ioCapabilities.put("[noOfOutputs] Number of digital outputs",
				FeHexConvert.byteToHexString(deviceInfo.noOfOutputs));
		ioCapabilities.put("[noOfRelays] Number of relays",
				FeHexConvert.byteToHexString(deviceInfo.noOfRelays));
		info.put("IO-CAPABILITIES", ioCapabilities);

		return info;
	}

	public void rfOn() throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();
			// Se proporciona los datos necesarios para enviar el comando
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_RF_ONOFF,
					(byte) 1));
			// Se envia el comando
			checkStatus(driver.sendProtocol((byte) 0x6A));
		} catch (FeHandlerException e) {
			throw new MifareControllerException("Imposible activar la antena.",
					e);
		} catch (FedmException e) {
			throw new MifareControllerException("Imposible activar la antena.",
					e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException("Imposible activar la antena.",
					e);
		} catch (FeSetException e) {
			throw new MifareControllerException("Imposible activar la antena.",
					e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException("Imposible activar la antena.",
					e);
		}
	}

	public void rfOff() throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();
			// Se proporciona los datos necesarios para enviar el comando
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_RF_ONOFF,
					(byte) 0));
			// Se envia el comando
			checkStatus(driver.sendProtocol((byte) 0x6A));
		} catch (FeHandlerException e) {
			throw new MifareControllerException(
					"Imposible desactivar la antena.", e);
		} catch (FedmException e) {
			throw new MifareControllerException(
					"Imposible desactivar la antena.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException(
					"Imposible desactivar la antena.", e);
		} catch (FeSetException e) {
			throw new MifareControllerException(
					"Imposible desactivar la antena.", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException(
					"Imposible desactivar la antena.", e);
		}
	}

	public List<MifareTagId> getTagList() throws MifareControllerException {
		ArrayList<MifareTagId> tags = new ArrayList<MifareTagId>();

		// Se hace la peticion
		try {

			// Se compone los parametros para el comando Inventory
			checkSet(driver.setData(FEDM_ISC_TMP_B0_CMD, (byte) 0x01));
			checkSet(driver.setData(FEDM_ISC_TMP_B0_MODE, (byte) 0x00));

			// Se resetea la tabla ISO donde se recibira la respuesta
			checkStatus(driver.deleteTable(ISO_TABLE));

			// Se envia el comando ISO Standard (B0)
			switch (checkSend(driver.sendProtocol((byte) 0xB0),
					new StatusByteEnum[] { StatusByteEnum.NO_TRANSPONDER,
							StatusByteEnum.COMUNICATION_ERROR })) {
			case NO_TRANSPONDER:
				return tags;
			case COMUNICATION_ERROR:
				return tags;
			}

		} catch (FeHandlerException fehEx) {
			throw new MifareControllerException(
					"Imposible obtener el inventario de transponders.", fehEx);
		} catch (FePortDriverException fpdEx) {
			throw new MifareControllerException(
					"Imposible obtener el inventario de transponders.", fpdEx);
		} catch (FeReaderDriverException frdEx) {
			throw new MifareControllerException(
					"Imposible obtener el inventario de transponders.", frdEx);
		} catch (FedmException fEx) {
			throw new MifareControllerException(
					"Imposible obtener el inventario de transponders.", fEx);
		} catch (FeSetException hosEx) {
			throw new MifareControllerException(
					"Imposible obtener el inventario de transponders.", hosEx);
		}

		// Se obtiene la ISO table (solo si hay algun transponder)
		if (driver.getTableLength(ISO_TABLE) > 0) {

			// Como hay algun transponder, procesamos la tabla.
			FedmIsoTableItem[] isoTable;
			try {
				isoTable = (FedmIsoTableItem[]) driver.getTable(ISO_TABLE);

				// Se procesa cada una de las tuplas
				for (int cnt = 0; cnt < isoTable.length; cnt++) {

					// Obtengo el tipo del transponder de la entrada actual de
					// la tabla ISO
					byte trType = isoTable[cnt].getByteData(DATA_TRTYPE);

					// Se trata cada uno de los tipos soportados
					switch (trType) {
					case 0x04: // Transponder de tipo 14443A

						// TR_INFO.
						byte trInfo = isoTable[cnt].getByteData(DATA_TRINFO);

						// OPT_INFO (Este datos depende de la configuracion del
						// REG3)
						byte optInfo = isoTable[cnt].getByteData(DATA_OPTINFO);
						System.out.println("optInfo=" + optInfo);

						// Se extrae el numero de serie
						byte[] uid = isoTable[cnt].getByteArrayData(DATA_SNR);

						// Se incorpora el TagId correspondiente a la lista
						tags.add(createTagId(trType, optInfo, uid));
						break;
					default:
						// System.out.println("Transponder no soportado: " +
						// FeHexConvert.byteToHexString(trType));
						break;
					}

				}
			} catch (FedmException e) {
				// TODO Revisar Catch
				e.printStackTrace();
			}

		}

		return tags;
	}

	private MifareTagId createTagId(byte trType, byte optInfo, byte[] uid) {

		// Consideraciones:
		// 1) 'trType' no sabemos de momento como interpretarlo.
		// 2) 'optType' no sabemos de momento como interpretarlo y no esta
		// activado en la configuracion del lector.
		// 3) Suponemos que simpre se nos entregan 8 bytes (aunque el S/N sea de
		// .
		// 4) Hasta ahora los posibles tipos de tag eran (MifareType.CLASSIC_1K
		// y MifareType.CLASSIC_4K).
		// Como no podemos discrimar el tipo de TAG con este lector, se ha
		// creado MifareType.ISO14443A que es una copia de
		// MifareType.CLASSIC_4K para ofrecer el maximo de direccionamiento
		// aunque
		// sin garantizar que existan los 4 K (podria ser un tag de 1K).
		// 5) El layout de los bytes de S/N es: (LSB-MSB: 0, 0, 0, 0, sn3, sn2,
		// sn1, sn0
		//
		// Por tanto hay que realizar todas las transformaciones necesarias para
		// utilizar las APIs generales de Mifare.

		MifareTagType tagType = MifareTagType.ISO14443A;
		return new SimpleMifareTagId(tagType, serianNumberFromFeigToApi(uid));
	}

	private byte[] serianNumberFromFeigToApi(byte[] feigSN) {
		return new byte[] { feigSN[7], feigSN[6], feigSN[5], feigSN[4] };
	}

	private byte[] serialNumberFromApiToFeig(byte[] apiSN) {
		return new byte[] { 0, 0, 0, 0, apiSN[3], apiSN[2], apiSN[1], apiSN[0] };
	}

	public void select(MifareTagId mifareTagId)
			throws MifareControllerException {

		try {
			// Se proporcinan los datos necesarios para componer la trama.
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B0_CMD,
					(byte) 0x25));
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B0_MODE,
					(byte) 0x00));
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B0_MODE_ADR,
					(byte) 0x01));
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B0_REQ_UID,
					serialNumberFromApiToFeig(mifareTagId.getSerialNumber())));

			// Se envia la trama del protocolo
			checkStatus(driver.sendProtocol((byte) 0xB0));
		} catch (FeHandlerException e) {
			throw new MifareControllerException(
					"Imposible seleccionar un transponder.", e);
		} catch (FePortDriverException fpdEx) {
			throw new MifareControllerException(
					"Imposible seleccionar un transponder.", fpdEx.getCause());
		} catch (FeReaderDriverException frdEx) {
			throw new MifareControllerException(
					"Imposible seleccionar un transponder.", frdEx.getCause());
		} catch (FedmException fEx) {
			throw new MifareControllerException(
					"Imposible seleccionar un transponder.", fEx.getCause());
		} catch (FeSetException hosEx) {
			throw new MifareControllerException(
					"Imposible seleccionar un transponder.", hosEx.getCause());
		}

	}

	public void loginParamKey(int sector, MifareKey mifareKey,
			MifareKeyType keyType) throws MifareControllerException {

		// Se calcula el primer bloque del sector del cual queremos obtener el
		// acceso..
		int dbAddress = MifareTagType.ISO14443A.getFirstBlockOfSector(sector);

		// Convierto los bytes de la clave en una String
		String keyArray = FeHexConvert.byteArrayToHexString(mifareKey
				.getBytes());

		try {
			// Se proporcionan los datos necesarios para componer la trama.
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B2_CMD,
					(byte) 0xB0));

			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B2_MODE,
					(byte) 0x00));
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B2_MODE_ADR,
					(byte) ISO_MODE_SEL));
			// Desde el parametro
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B2_MODE_KL, 1));
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B2_REQ_DB_ADR,
					(byte) dbAddress));
			// key-a
			checkSet(driver.setData(
					FedmIscReaderID.FEDM_ISC_TMP_B2_REQ_KEY_TYPE,
					(byte) (keyType == MifareKeyType.KEY_A ? 0 : 1)));
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_ISO14443A_KEY,
					keyArray));

			// Se envia la trama del protocolo
			checkStatus(driver.sendProtocol((byte) 0xB2));
		} catch (FeHandlerException e) {
			if (e.getStatusByteEnum().getId() == 8) {
				throw new MifareControllerException("Imposible autenticar.",
						MifareControllerExceptionEnum.AUTH_OPERATION_ERROR);
			} else {
				throw new MifareControllerException("Imposible autenticar.", e);
			}
			// throw new
			// MifareControllerException("Imposible autenticar con la KeyA.",
			// e);
		} catch (FePortDriverException fpdEx) {
			throw new MifareControllerException("Imposible autenticar.",
					fpdEx.getCause());
		} catch (FeReaderDriverException frdEx) {
			throw new MifareControllerException("Imposible autenticar.",
					frdEx.getCause());
		} catch (FedmException fEx) {
			throw new MifareControllerException("Imposible autenticar.",
					fEx.getCause());
		} catch (FeSetException hosEx) {
			throw new MifareControllerException("Imposible autenticar.",
					hosEx.getCause());
		}
	}

	public void writeMifareReaderKey(int address, MifareKey mifareKey,
			MifareKeyType keyType) throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();

			// Se compone los parametros para el comando Inventory
			checkSet(driver.setData(FEDM_ISC_TMP_ISO14443A_KEY_TYPE,
					(byte) (keyType == MifareKeyType.KEY_A ? 0 : 1)));
			checkSet(driver.setData(FEDM_ISC_TMP_ISO14443A_KEY_ADR,
					(byte) address));
			checkSet(driver.setData(FEDM_ISC_TMP_ISO14443A_KEY,
					mifareKey.getBytes()));

			// Se enviar el comando para resetear transponders
			checkStatus(driver.sendProtocol((byte) 0xA2));
		} catch (FeHandlerException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FeSetException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FedmException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		}
	}

	public void loginStoredKey(int sector, int stored, MifareKeyType keyType)
			throws MifareControllerException {

		// Se calcula el primer bloque del sector del cual queremos obtener el
		// acceso..
		int dbAddress = MifareTagType.ISO14443A.getFirstBlockOfSector(sector);

		try {
			// Se proporcionan los datos necesarios para componer la trama.
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B2_CMD,
					(byte) 0xB0));
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B2_MODE,
					(byte) 0x00));
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B2_MODE_ADR,
					(byte) ISO_MODE_SEL));
			// Desde la eeprom
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B2_MODE_KL, 0));
			checkSet(driver.setData(FedmIscReaderID.FEDM_ISC_TMP_B2_REQ_DB_ADR,
					(byte) dbAddress));
			// key-a
			checkSet(driver.setData(
					FedmIscReaderID.FEDM_ISC_TMP_B2_REQ_KEY_TYPE,
					(byte) (keyType == MifareKeyType.KEY_A ? 0 : 1)));
			checkSet(driver.setData(
					FedmIscReaderID.FEDM_ISC_TMP_B2_REQ_KEY_ADR, stored));

			// Se envia la trama del protocolo
			checkStatus(driver.sendProtocol((byte) 0xB2));
		} catch (FeHandlerException e) {
			if (e.getStatusByteEnum().getId() == 8) {
				throw new MifareControllerException(
						"Imposible autenticar con la KeyA almacenada.",
						MifareControllerExceptionEnum.AUTH_OPERATION_ERROR);
			} else {
				throw new MifareControllerException(
						"Imposible autenticar con la KeyA almacenada.", e);
			}
			// throw new
			// MifareControllerException("Imposible autenticar con la KeyA almacenada.",
			// e);
		} catch (FePortDriverException fpdEx) {
			throw new MifareControllerException(
					"Imposible autenticar con la KeyA almacenada.",
					fpdEx.getCause());
		} catch (FeReaderDriverException frdEx) {
			throw new MifareControllerException(
					"Imposible autenticar con la KeyA almacenada.",
					frdEx.getCause());
		} catch (FedmException fEx) {
			throw new MifareControllerException(
					"Imposible autenticar con la KeyA almacenada.",
					fEx.getCause());
		} catch (FeSetException hosEx) {
			throw new MifareControllerException(
					"Imposible autenticar con la KeyA almacenada.",
					hosEx.getCause());
		}
	}

	private String getFeigSerialNumber(MifareTagId mifareTagId) {
		return FeHexConvert
				.byteArrayToHexString(serialNumberFromApiToFeig(mifareTagId
						.getSerialNumber()));
	}

	private int findIsoTableItemIndex(MifareTagId mifareTagId)
			throws FeSetException {
		return checkFind(driver.findTableIndex(0, ISO_TABLE, DATA_SNR,
				getFeigSerialNumber(mifareTagId)));
	}

	private FedmIsoTableItem getIsoTableItem(int index) throws FedmException {
		return (FedmIsoTableItem) driver.getTableItem(index, ISO_TABLE);
	}

	private FedmIsoTableItem findIsoTableItem(MifareTagId mifareTagId)
			throws FeSetException, FedmException {
		return getIsoTableItem(findIsoTableItemIndex(mifareTagId));
	}

	public byte[] read(MifareTagId mifareTagId, int block)
			throws MifareControllerException {

		byte dbAddress = (byte) block;

		// Se compone y envia la peticion de lectura.
		try {

			// Obtenemos la entrada de la ISOTable correspondiente al
			// transceiver
			FedmIsoTableItem beforeIsoTableItem = findIsoTableItem(mifareTagId);

			// Se actualiza la informacion del transponder obtenida por el
			// comando INVENTORY para indicar los bytes de cada bloque (para
			// Mifare).
			beforeIsoTableItem.setData(DATA_BLOCK_SIZE, 16);

			// Se compone los parametros para el comando de lecturas de bloques
			checkSet(driver.setData(FEDM_ISC_TMP_B0_CMD, (byte) 0x23));

			// Limpieza del modo.
			checkSet(driver.setData(FEDM_ISC_TMP_B0_MODE, (byte) 0x00));

			// Direccionamiento = SELECTED
			checkSet(driver.setData(FEDM_ISC_TMP_B0_MODE_ADR, (byte) 0x02));

			// Bloque inicial desde el que se quiere leer
			checkSet(driver.setData(FEDM_ISC_TMP_B0_REQ_DB_ADR, dbAddress));

			// Numero de bloques
			checkSet(driver.setData(FEDM_ISC_TMP_B0_REQ_DBN, (byte) 0x01));

			// Se envia el comando ISO Standard (B0)
			checkStatus(driver.sendProtocol((byte) 0xB0));

			// Obtenemos de nuevo la ISOTable
			FedmIsoTableItem afterIsoTableItem = findIsoTableItem(mifareTagId);

			// Se procesa la tupla con la contestacion
			// byte blockSize = afterIsoTableRow.getByteData(DATA_BLOCK_SIZE);
			byte[] blockData = afterIsoTableItem.getByteArrayData(DATA_RxDB,
					dbAddress);

			// Ya se puede retornar
			return blockData;

		} catch (FeHandlerException e) {
			throw new MifareControllerException("Imposible obtener un bloque.",
					e);
		} catch (FePortDriverException fpdEx) {
			throw new MifareControllerException("Imposible obtener un bloque.",
					fpdEx.getCause());
		} catch (FeReaderDriverException frdEx) {
			throw new MifareControllerException("Imposible obtener un bloque.",
					frdEx.getCause());
		} catch (FedmException fEx) {
			throw new MifareControllerException("Imposible obtener un bloque.",
					fEx.getCause());
		} catch (FeSetException hosEx) {
			throw new MifareControllerException("Imposible obtener un bloque.",
					hosEx.getCause());
		}

	}

	public void write(MifareTagId mifareTagId, int block, byte[] data)
			throws MifareControllerException {

		byte dbAddress = (byte) block;

		// Se compone y envia la peticion de lectura.
		try {

			// Se compone los parametros para el comando de lecturas de bloques
			checkSet(driver.setData(FEDM_ISC_TMP_B0_CMD, (byte) 0x24));
			// Limpieza del modo.
			checkSet(driver.setData(FEDM_ISC_TMP_B0_MODE, (byte) 0x00));
			// Direccionamiento = ADRESSED
			checkSet(driver.setData(FEDM_ISC_TMP_B0_MODE_ADR, (byte) 0x02));
			// Numero de bloques
			checkSet(driver.setData(FEDM_ISC_TMP_B0_REQ_DBN, (byte) 0x01));
			// Bloque inicial desde el que se quiere escribir
			checkSet(driver.setData(FEDM_ISC_TMP_B0_REQ_DB_ADR, dbAddress));

			// Se actualiza la informacion del transponder obtenida por el
			// comando INVENTORY para indicar los bytes de cada bloque (para
			// Mifare).
			driver.setTableData(findIsoTableItemIndex(mifareTagId), ISO_TABLE,
					DATA_BLOCK_SIZE, 16);
			// isoTableItem.setData(DATA_TxDB, dbAddress, snString);
			driver.setTableData(findIsoTableItemIndex(mifareTagId), ISO_TABLE,
					DATA_TxDB, dbAddress, data);

			// Se envia el comando ISO Standard (B0)
			checkStatus(driver.sendProtocol((byte) 0xB0));

		} catch (FeHandlerException e) {
			throw new MifareControllerException(
					"Imposible actualizar un bloque.", e);
		} catch (FePortDriverException fpdEx) {
			throw new MifareControllerException(
					"Imposible actualizar un bloque.", fpdEx.getCause());
		} catch (FeReaderDriverException frdEx) {
			throw new MifareControllerException(
					"Imposible actualizar un bloque.", frdEx.getCause());
		} catch (FedmException fEx) {
			throw new MifareControllerException(
					"Imposible actualizar un bloque.", fEx.getCause());
		} catch (FeSetException hosEx) {
			throw new MifareControllerException(
					"Imposible actualizar un bloque.", hosEx.getCause());
		}

	}

	public void readConfigurationRegister(int reg, MemoryType memoryType)
			throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();

			// Se compone los parametros para el comando Inventory
			checkSet(driver.setData(FEDM_ISC_TMP_READ_CFG, (byte) 0x00));
			checkSet(driver.setData(FEDM_ISC_TMP_READ_CFG_ADR, (byte) reg));
			checkSet(driver.setData(FEDM_ISC_TMP_READ_CFG_LOC,
					memoryType.getValue()));

			// Se enviar el comando para resetear transponders
			checkStatus(driver.sendProtocol((byte) 0x80));
		} catch (FeHandlerException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FeSetException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FedmException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		}
	}

	public void writeConfigurationRegister(int reg, MemoryType memoryType)
			throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();

			// Se compone los parametros para el comando Inventory
			checkSet(driver.setData(FEDM_ISC_TMP_WRITE_CFG, (byte) 0x00));
			checkSet(driver.setData(FEDM_ISC_TMP_WRITE_CFG_ADR, (byte) reg));
			checkSet(driver.setData(FEDM_ISC_TMP_WRITE_CFG_LOC,
					memoryType.getValue()));

			// Se enviar el comando para resetear transponders
			checkStatus(driver.sendProtocol((byte) 0x81));
		} catch (FeHandlerException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FeSetException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FedmException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		}
	}

	public void saveConfigurationRegisterFromRamToEEprom(int reg)
			throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();

			// Se compone los parametros para el comando Inventory
			checkSet(driver.setData(FEDM_ISC_TMP_SAVE_CFG, (byte) 0x00));
			checkSet(driver.setData(FEDM_ISC_TMP_SAVE_CFG_ADR, (byte) reg));
			checkSet(driver.setData(FEDM_ISC_TMP_SAVE_CFG_MODE, false));

			// Se enviar el comando para resetear transponders
			checkStatus(driver.sendProtocol((byte) 0x82));
		} catch (FeHandlerException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FeSetException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FedmException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		}
	}

	public void setDefaultConfiguration(int reg, MemoryType memoryType)
			throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();

			// Se compone los parametros para el comando Inventory
			checkSet(driver.setData(FEDM_ISC_TMP_RESET_CFG, (byte) 0x00));
			checkSet(driver.setData(FEDM_ISC_TMP_RESET_CFG_ADR, (byte) reg));
			checkSet(driver.setData(FEDM_ISC_TMP_RESET_CFG_MODE, false));
			checkSet(driver.setData(FEDM_ISC_TMP_RESET_CFG_LOC,
					memoryType.getValue()));

			// Se enviar el comando para resetear transponders
			checkStatus(driver.sendProtocol((byte) 0x83));
		} catch (FeHandlerException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FeSetException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		} catch (FedmException e) {
			throw new MifareControllerException(
					"Imposible obtener la version del software.", e);
		}
	}

	abstract public OutputsInfo getOutputInfo();

	abstract public void sendOutputs(OutputBuilder outputBuilder)
			throws MifareControllerException;

	public class CPRProfileTableConfigurationImpl implements
			ICPRProfileTableConfiguration {

		@Override
		public void setBRMTableSize(int size) {
			driver.setTableSize(BRM_TABLE, size);
		}

		@Override
		public int getBRMTableSize() {
			return driver.getTableSize(BRM_TABLE);
		}

		@Override
		public void setISOTableSize(int size) {
			driver.setTableSize(ISO_TABLE, size);
		}

		@Override
		public int getISOTableSize() {
			return driver.getTableSize(ISO_TABLE);
		}

	}

	public class CPRProfileRegisterConfigurationImpl implements
			ICPRProfileRegisterConfiguration {

		public CFG01 getCFG01(MemoryType memoryType)
				throws MifareControllerException {
			return CFG01.readRegister(CPRAbstract.this, memoryType);
		}

		public CFG02 getCFG02(MemoryType memoryType)
				throws MifareControllerException {
			return CFG02.readRegister(CPRAbstract.this, memoryType);
		}

		public CFG03 getCFG03(MemoryType memoryType)
				throws MifareControllerException {
			return CFG03.readRegister(CPRAbstract.this, memoryType);
		}

		public CFG11 getCFG11(MemoryType memoryType)
				throws MifareControllerException {
			return CFG11.readRegister(CPRAbstract.this, memoryType);
		}

		public CFG12 getCFG12(MemoryType memoryType)
				throws MifareControllerException {
			return CFG12.readRegister(CPRAbstract.this, memoryType);
		}

		public CFG15 getCFG15(MemoryType memoryType)
				throws MifareControllerException {
			return CFG15.readRegister(CPRAbstract.this, memoryType);
		}

		public CFG40 getCFG40(MemoryType memoryType)
				throws MifareControllerException {
			return CFG40.readRegister(CPRAbstract.this, memoryType);
		}

		public CFG41 getCFG41(MemoryType memoryType)
				throws MifareControllerException {
			return CFG41.readRegister(CPRAbstract.this, memoryType);
		}

		public CFG49 getCFG49(MemoryType memoryType)
				throws MifareControllerException {
			return CFG49.readRegister(CPRAbstract.this, memoryType);
		}

	}

}
