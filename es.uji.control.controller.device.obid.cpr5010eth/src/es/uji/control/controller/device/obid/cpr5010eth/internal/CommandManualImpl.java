/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cpr5010eth.internal;

import java.util.List;

import es.uji.control.controller.core.service.ControllerException;
import es.uji.control.controller.mifare.MifareControllerException;
import es.uji.control.controller.mifare.MifareTagId;

public class CommandManualImpl extends CommandBase {

	volatile private Thread thread;

	public CommandManualImpl(ControllerContext controllerContext) {
		super(controllerContext);
	}

	public void getNextTag() throws ControllerException {
		synchronized (this) {
			controllerContext.getMifareReader().rfOn();
			// Lanzar la tarea con los handlers del terminal.
			thread = new NextTagTask();
			thread.start();
		}
	}

	public void cancelNextTag() {
		synchronized (this) {
			// Se para el thread
			if (thread != null) {
				Thread stopThread = thread;
				thread = null;
				try {
					stopThread.join();
				} catch (InterruptedException e) {
				}
			}
			try {
				controllerContext.getMifareReader().rfOff();
			} catch (MifareControllerException e) {
			}
		}
	}


	public class NextTagTask extends Thread {

		public NextTagTask() {
			super("NextTagTask");
		}

		public void run() {
			while (thread != null) {
				// Una pausa.
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}

				// Leemos las tarjetas que estan dentro del alcance del lector y
				// las procesamos
				try {
					List<MifareTagId> readedTagsId = controllerContext.getMifareReader().getTagList();
					if (readedTagsId.size() > 0) {
						sendEventMifareManualTagDetected(readedTagsId.get(0));
						thread = null;
					}
					// Se resetea el estado
				} catch (ControllerException e) {
					// Actualizar el estado del controlador
					controllerContext.getStatusInstance().processRuntimeException(e);
					// Se envia un evento.
					sendEventControllerStatus();
				}
			}
		}

	}


}
