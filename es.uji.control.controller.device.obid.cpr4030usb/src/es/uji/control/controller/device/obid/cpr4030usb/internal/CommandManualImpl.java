package es.uji.control.controller.device.obid.cpr4030usb.internal;

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
			try {
				controllerContext.getMifareReader().rfOn();
			} catch(MifareControllerException mcEx) {
				throw new ControllerException(mcEx.getMessage(), mcEx);
			}
			// Lanzar la tarea con los handlers del terminal.
			thread = new NextTagTask();
			thread.start();
		}
	}

	public void cancelNextTag()  {
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
			} catch (MifareControllerException mcEx) {
				mcEx.printStackTrace();
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
					// Reseteamos todos los transceivers que estan dentro del alcance de la antena
					controllerContext.getMifareReader().rfReset();
					// Leemos la lista de transceivers que estan dentro del alcance de la antena
					List<MifareTagId> readedTagsId = controllerContext.getMifareReader().getTagList();
					if (readedTagsId.size() > 0) {
						sendEventMifareManualTagDetected(readedTagsId.get(0));
						thread = null;
					}
				} catch (MifareControllerException mcEx) {
					// Actualizar el estado del controlador
					controllerContext.getStatusInstance().processRuntimeException(mcEx);
					// Se envia un evento.
					sendEventControllerStatus();
				}
			}
		}

	}


}
