/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cpr4030usb.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.uji.control.controller.core.service.ControllerException;
import es.uji.control.controller.mifare.MifareControllerException;
import es.uji.control.controller.mifare.MifareTagId;

public class CommandScanImpl extends CommandBase {
	
	volatile private Thread thread;
	
	private HashMap<Long, MifareTagId> currentTags;
	
	public CommandScanImpl(ControllerContext controllerContext) {
		super(controllerContext);
	}
	
	public void startScan() throws ControllerException {
		synchronized (this) {
			currentTags = new HashMap<Long, MifareTagId>();
			synchronized (controllerContext.getMifareReader()) {
				controllerContext.getMifareReader().rfOn();
			}
			// Se incia la tarea
			if (thread == null) {
				// Lanzar la tarea con los handelers del terminal.
				thread = new MifareTask();
				thread.start();
			}
		}
	}

	public void stopScan() {
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
			// Se liberan los servicios de las tajetas que estaban presentes.
			for (MifareTagId tag: currentTags.values()) {
				postEventMifareScanTagLosted(tag);
			}
			currentTags.clear();
			// Se desconecta la antena
			synchronized (controllerContext.getMifareReader()) {
				try {
					controllerContext.getMifareReader().rfOff();
				} catch (MifareControllerException e) {
				}
			}
		}
	}
	
	public class MifareTask extends Thread {
		
		public MifareTask() {
			super("Mifare reader");
		}
		
		public void run() {
			
			while(thread != null) {
				try {
					// Una pausa.
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
					}
					
					// Leemos las tarjetas que estan dentro del alcance del lector y las procesamos
					synchronized (controllerContext.getMifareReader()) {
						List<MifareTagId> readedTagsId;
						try {
							// Se resetean los transaceivers que estan en el alcance de la antena
							controllerContext.getMifareReader().rfReset();

							// Se leen los identificadores de los transceivers que estan al alcance de la antena.
							readedTagsId = controllerContext.getMifareReader().getTagList();
							updateTags(readedTagsId);
							
						} catch (MifareControllerException mcEx) {
							mcEx.printStackTrace();						
						}
					}
				} catch (IllegalStateException ex) {
					// Si se ha producido un error irrecuperable, intentamos parar en orden.
					thread = null;
				}
			}
		}
		
	}
	
	private void updateTags(List<MifareTagId> readedTags) {
		
		// Esta lista almacenara los tags que yo no estan presentes.
		ArrayList<MifareTagId> lostedTags = new ArrayList<MifareTagId>();
		
		// Se calcula la diferencia entre los tags que acabamos de leer y los que
		// tenemos almacenados.
		for (MifareTagId currentTag: currentTags.values()) {
			boolean found = false;
			for (MifareTagId readedTag: readedTags) {
				if (readedTag.getCompactSerialNumber() == currentTag.getCompactSerialNumber()) {
					found = true;
					break;
				}
			}
			// Se ha encontrado
			if (!found) {
				lostedTags.add(currentTag);
			}
		}
		
		// Con la lista de Tags que se han perdido, lanzamos un evento para cada uno de ellos.
		for (MifareTagId lostedTag: lostedTags) {
			currentTags.remove(lostedTag.getCompactSerialNumber());
			postEventMifareScanTagLosted(lostedTag);
		}
		
		// Se calculan los tags que han aparecido nuevos.
		ArrayList<MifareTagId> addedTags = new ArrayList<MifareTagId>();
		for (MifareTagId readedTag: readedTags) {
			boolean found = false;
			for (MifareTagId currentTag: currentTags.values()) {
				if (currentTag.getCompactSerialNumber() == readedTag.getCompactSerialNumber()) {
					found = true;
					break;
				}
			}
			// Se ha encontrado
			if (!found) {
				addedTags.add(readedTag);
			}
		}
		for (MifareTagId addTag: addedTags) {
			currentTags.put(addTag.getCompactSerialNumber(), addTag);
			sendEventMifareScanTagDetected(addTag);
		}
		
	}
	
	
}
