package es.uji.control.controller.core.service;

public class ControllerConstants {
	
	/***********************************************************************
	/* Tipo de interface del controlador: "USB", "SERIAL", "NETWORK"
	 **********************************************************************/
	static public String CONTROLLER_TYPE = "CONTROLLER_TYPE";
	static public String CONTROLLER_TYPE_SERIAL = "SERIAL";
	static public String CONTROLLER_TYPE_USB = "USB";
	static public String CONTROLLER_TYPE_NETWORK = "NETWORK";

	/***********************************************************************
	 * Puerto o direccion para comunicarse con el controlador:
	 *   USB: USB1, USB2, ...
	 *   SERIAL: COM1, COM2, ...
	 *   NETWORK: lector1.name.com, lector2.name.com
	 ***********************************************************************/
	static public String PORT = "PORT";
	
	/***********************************************************************
	 * Identificador que le da la aplicacion al controlador.
	 *   "ACCPER", "PERMETRO1", ...
	 ***********************************************************************/
	static public String NAME = "NAME";
	
	/***********************************************************************
	 * Identificador del fabricante del controlador:
	 *   "ACS", "OBID", ...
	 ***********************************************************************/
	static public String MANUFACTURER = "MANUFACTURER";
	
	/***********************************************************************
	 * Identificador del modelo del controlador.
	 ***********************************************************************/
	static public String MODEL = "MODEL";
	
	/***********************************************************************
	 * Identificador interno generado en tiempo de ejecucion que permitira
	 * asociar los eventos con el controlador que los genera.
	 ***********************************************************************/
	static public String ID = "CONTROLLERID";
	
}
