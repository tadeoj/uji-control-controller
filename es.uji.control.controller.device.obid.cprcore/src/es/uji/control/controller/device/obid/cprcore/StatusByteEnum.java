/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore;


public enum StatusByteEnum {

	OK (0x00, StatusClassEnum.GENERAL, 
			"1) Data / parameters have been read or stored without error.\n" +
			"2) Control command has been executed."
	),
			
	NO_TRANSPONDER (0x01, StatusClassEnum.TRANSPONDER_STATUS, 
			"1) No transponder is located within the detection range of the Reader.\n" +
			"2) The transponder in the detection range has been switched to mute.\n" + 
			"3) The communicactions between Reader and Transponder has been ionterfered and the Reader is not able to read the Transponder anymore" 
	),
	DATA_FALSE (0x02, StatusClassEnum.TRANSPONDER_STATUS,
		"1) CRC, parity or framing error at received data."
	),
	WRITE_ERROR (0x03, StatusClassEnum.TRANSPONDER_STATUS,
		"Negative plausibility check of the written data:\n" + 
		"1) Attempt to write on a read-only storing-area.\n" + 
		"2) Too much distance between Transponder and Reader antenna.\n" + 
		"3) Attempt to write in a noise area.\n"
	),
	ADDRESS_ERROR (0x04, StatusClassEnum.TRANSPONDER_STATUS,
		"The required data are outside of the logical or physical Transponder-address area:\n" + 
		"1) The address is beyond the max. address space of the Transponder.\n" + 
		"2) The address is beyond the configured address space of the Transponder.\n"
	),
	TRANSPORT_TYPE (0x05, StatusClassEnum.TRANSPONDER_STATUS,
		"This command is not applicable at the Transponder:\n" + 
		"1) Attempt to write on or read from a Transponder.\n" + 
		"2) A special command is not applicable to the Transponder.\n"
	),
	AUTHENT_ERROR (0x08, StatusClassEnum.TRANSPONDER_STATUS,
		"The reader could not identify itself to the transponder as authorized:\n" +
		"1) reader- and transponder Keys do not correspond."
	),
	GENERAL_ERROR (0x0E, StatusClassEnum.TRANSPONDER_STATUS,
		"The Transponder answered with an undefined or general error code."
	),
	COMUNICATION_ERROR (0x83, StatusClassEnum.TRANSPONDER_STATUS,
		"1) Anticollision could not be finished by the reader.\n" +
		"2) Corrupted or faulty data exchange between reader and Transponder."
	),
	DATA_BUFFER_OVERFLOW (0x93, StatusClassEnum.TRANSPONDER_STATUS,
		"There are more Transponders in reader field than could be handled by the reader " + 
		"(refer Fehler! Verweisquelle konnte nicht gefunden werden.)"
	),
	MORE_DATA (0x94, StatusClassEnum.TRANSPONDER_STATUS,
		"There are more Transponder data sets requested than the response protocol can transfer at once."
	),
	ISO15693_ERROR (0x95, StatusClassEnum.TRANSPONDER_STATUS,
		"An additional error code for ISO15693 Transponders is sent with response data."
	),
	ISO14443_ERROR (0x96, StatusClassEnum.TRANSPONDER_STATUS,
		"An additional error code for ISO14443 Transponders is sent with response data. " +
		"(see: Fehler! Verweisquelle konnte nicht gefunden werden.)"
	),
	CRYPTO_PROCESSING_ERROR (0x97, StatusClassEnum.TRANSPONDER_STATUS,
		"An additional code for soruce and reason of the error is sent with response data"
	),
	
	
	EEPROM_FAILURE (0x10, StatusClassEnum.PARAMETER_STATUS,
		"1) The EEPROM of the Reader is not able to be written on.\n" + 
		"2) Before writing onto the EEPROM a faulty checksum of parameters has been detected."
	),
	PARAMATER_RABGE_ERROR (0x11, StatusClassEnum.PARAMETER_STATUS,
		"The value range of the parameters was exceeded."
	),
	
	UNKNOWN_COMMAND (0x80, StatusClassEnum.INTERFACE_STATUS,
		"The Reader does not support the selected function."
	),
	LENGTH_ERROR (0x81, StatusClassEnum.INTERFACE_STATUS,	
		"The received protocol contains not the expected content."
	),
	CURRENTLY_NOT_AVAILABLE (0x82, StatusClassEnum.INTERFACE_STATUS,
		"The reader is configured in scan-mode and had received an ISO Host-mode command."
	),
	
	HARDWARE_WARNING (0xF1, StatusClassEnum.READER_STATUS,
		"The Firmware is incompatible with the hardware"
	),
	
	NO_SAM_DETECTED (0x31, StatusClassEnum.SAM_STATUS,
		"The reader get no response from the Smart Card"
	),
	SAM_IS_NOT_ACTIVATED (0x32, StatusClassEnum.SAM_STATUS,
		"The requested SAM is not activated by the SAM Activate command."
	),
	REQUEST_SAM_IS_ALREADY_ACTIVATE (0x33, StatusClassEnum.SAM_STATUS,
		"Requested SAM is already activated"
	),
	REQUEST_PROTOCOL_IS_NOT_SUPPORTED_BY_THE_SAM (0x34, StatusClassEnum.SAM_STATUS,
		"Check if T=0 or T=1 protocol is supported by the SAM"
	),
	SAM_COMMUNICATION_ERROR (0x35, StatusClassEnum.SAM_STATUS,
		"A data transmission error occurred while communication with the SAM"
	),
	SAM_TIMEOUT (0x36, StatusClassEnum.SAM_STATUS,
		"The Reader got no response from SAM within the defined timout"
	),
	UNSUPPORTED_SAM_BAUDRATE (0x37, StatusClassEnum.SAM_STATUS,
		"The used parameter of Fi and/or Di are not supported by the reader"
	),
	
	UNKNOWN_STATUS (0xFF, StatusClassEnum.UNKNOWN, 
			"Unknown status code."
	);

	private int id;
	private StatusClassEnum statusClass;
	private String description;
	
	private StatusByteEnum(int id, StatusClassEnum statusClass, String description) {
		this.id = id;
		this.statusClass = statusClass;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return super.toString();
	}
	
	public StatusClassEnum getStatusClass() {
		return statusClass;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static StatusByteEnum getById(int id) {
		for (StatusByteEnum statusByte : StatusByteEnum.values()) {
			if (id == statusByte.getId())
				return statusByte;
		}
		return null;
	}
	
	public String toString() {
		return String.format("%s=0x%02x (%s): \n%s", getName(), getId(), getStatusClass().toString(), getDescription());
	}
	
}
