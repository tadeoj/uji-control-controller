package es.uji.control.controller.device.obid.cprcore;

import de.feig.FeHexConvert;
import de.feig.FePortDriverException;
import de.feig.FeReaderDriverException;
import de.feig.FedmBrmTableItem;
import de.feig.FedmException;
import de.feig.FedmIscReaderConst;
import de.feig.FedmTaskListener;
import de.feig.FedmTaskOption;
import es.uji.control.controller.core.service.OutputBuilder;
import es.uji.control.controller.core.service.OutputLedEnum;
import es.uji.control.controller.core.service.OutputModeEnum;
import es.uji.control.controller.core.service.OutputsInfo;
import es.uji.control.controller.mifare.MifareControllerException;

public class CPR5010Eth extends CPRAbstract {
	
	private NotificationListener notificationListener;
	
	public enum SystemResetMode {
		
		RF_CONTROLLER ((byte) 0),
		RF_DECODDER ((byte) 3);
		
		private byte id;
		
		private SystemResetMode(byte id) {
			this.id = id;
		}
		
		public byte getId() {
			return id;
		}
		
		public static SystemResetMode getById(byte id) throws IllegalArgumentException {
			for (SystemResetMode systemResetMode : SystemResetMode.values()) {
				if (id == systemResetMode.getId())
					return systemResetMode;
			}
			throw new IllegalArgumentException("Invalid Id to build CPR5010Eth.SystemResetMode.");
		}

	}
	
	@Override
	public void stop() throws MifareControllerException {
		stopNotifier();
		super.stop();
	}

	public void connectTcp(String address, int portNumber) throws MifareControllerException {
		// Se desconecta
		disconnect();
		// Codigo especifico para la apertura mediante USB
		try {
			// Conectamos con el primer lector USB
			driver.connectTCP(address, portNumber);
		} catch (FedmException e) {
			throw new MifareControllerException("Imposible conectar con el lector ETH", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException("Imposible conectar con el lector ETH", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException("Imposible conectar con el lector ETH", e);
		}
	}

	public OutputsInfo getOutputInfo() {
		
		return new OutputsInfo() {
			
			@Override
			public int digitalOutputCount() {
				return 0;
			}

			@Override
			public int relayCount() {
				return 1;
			}
			
			@Override
			public int buzzerCount() {
				return 1;
			}
			
			@Override
			public boolean hasLed(OutputLedEnum led) {
				return true;
			}
			
		};
		
	}
	
	public void systemReset(SystemResetMode systemResetMode) throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();
			// Se compone los parametros
			checkSet(driver.setData(FEDM_ISC_TMP_SYSTEM_RESET_MODE, systemResetMode.getId()));
			// Se enviar el comando para resetear el sistema
			checkStatus(driver.sendProtocol((byte) 0x64));
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


	public void sendOutputs(OutputBuilder outputBuilder) throws MifareControllerException {
		
		// Obtenemos las entradas validas.
		OutputBuilder.Entry[] entries = outputBuilder.getEntries(getOutputInfo()).toArray(new OutputBuilder.Entry[] {});
		int maxEntries = entries.length > 8 ? 8 : entries.length;  
		
		try {
			
			// Se verifica el lector
			checkReader();
			
			// Se compone los parametros
			checkSet(driver.setData(FEDM_ISC_TMP_0x72_OUT_MODE, (byte) 0x00));
			checkSet(driver.setData(FEDM_ISC_TMP_0x72_OUT_N, (byte) maxEntries));
			
			for (int idx = 0; idx < maxEntries; idx++) {
				
				OutputBuilder.Entry entry = entries[idx];
				
				checkSet(driver.setData(FEIG_ADDRESS_DATA[idx][0], transformOutputNumberToFeig(entry)));
				checkSet(driver.setData(FEIG_ADDRESS_DATA[idx][1], transformOutputTypeToFeigProtocol(entry.getEntryEnum())));
				checkSet(driver.setData(FEIG_ADDRESS_DATA[idx][2], transformOutputModeToFeigProtocol(entry.getOutputMode())));
				checkSet(driver.setData(FEIG_ADDRESS_DATA[idx][3], transformMillisecsToFeigProtocol(entry.getMillisecs())));
			}
		
			// Se enviar el comando para resetear CPU
			checkStatus(driver.sendProtocol((byte) 0x72));
			
		} catch (FeHandlerException e) {
			throw new MifareControllerException("Impopsible enviar un tren de outputs.", e);
		} catch (FeSetException e) {
			throw new MifareControllerException("Impopsible enviar un tren de outputs.", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException("Impopsible enviar un tren de outputs.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException("Impopsible enviar un tren de outputs.", e);
		} catch (FedmException e) {
			throw new MifareControllerException("Impopsible enviar un tren de outputs.", e);
		}
		
	}
	
	public ReaderDataBufferInfo getReaderDataBufferInfo() throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();
			// Se enviar el comando para resetear transponders
			checkStatus(driver.sendProtocol((byte) 0x31));
			// Se obtiene la respuesta
			return new ReaderDataBufferInfo(
				driver.getIntegerData(FEDM_ISCLR_TMP_TAB_SIZE), 
				driver.getIntegerData(FEDM_ISCLR_TMP_TAB_START), 
				driver.getIntegerData(FEDM_ISCLR_TMP_TAB_LEN)
			);
		} catch (FeHandlerException e) {
			throw new MifareControllerException("Imposible obtener la version del software.", e);
		} catch (FeSetException e) {
			throw new MifareControllerException("Imposible obtener la version del software.", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException("Imposible obtener la version del software.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException("Imposible obtener la version del software.", e);
		} catch (FedmException e) {
			throw new MifareControllerException("Imposible obtener la version del software.", e);
		}
	}
	
	public void initializeBuffer() throws MifareControllerException {
		try {
			// Se verifica el estado del lector.
			checkReader();
			// Se enviar el comando para resetear transponders
			checkStatus(driver.sendProtocol((byte) 0x33));
		} catch (FeHandlerException e) {
			throw new MifareControllerException("Imposible obtener la version del software.", e);
		} catch (FeSetException e) {
			throw new MifareControllerException("Imposible obtener la version del software.", e);
		} catch (FePortDriverException e) {
			throw new MifareControllerException("Imposible obtener la version del software.", e);
		} catch (FeReaderDriverException e) {
			throw new MifareControllerException("Imposible obtener la version del software.", e);
		} catch (FedmException e) {
			throw new MifareControllerException("Imposible obtener la version del software.", e);
		}
	}
	
	private byte transformDigitalOutputAddress(int address) {
		return (byte) address; // No se ha dado el caso y no sabemos si hay que hacer alguna transformacion.
	}
	
	private byte transformRelayAddress(int relay) {
		return (byte) (relay + 1);
	}
	
	private byte transformBuzzerAddress(int buzzer) {
		return (byte) (buzzer + 1);
	}
	
	private int transformMillisecsToFeigProtocol(int millisecs) {
		return (millisecs / 100);
	}
	
	private byte transformOutputTypeToFeigProtocol(OutputBuilder.EntryEnum entryEnum) {
		switch (entryEnum) {
		case BUZZER:
			return 0x2;
		case LED:
			return 0x1;
		case RELAY:
			return 0x4;
		default:
			throw new IllegalArgumentException("Invalid OutputType for FEDM_ISC_TMP_0x72_OUT_TYPE_X field");
		}
	}
	
	private byte transformOutputModeToFeigProtocol(OutputModeEnum outputMode) {
		switch (outputMode) {
		case OFF:
			return 0x0;
		case ON:
			return 0x1;
		case FLASHING_SLOW:
			return 0x2;
		case FLASHING_FAST:
			return 0x3;
		default:
			throw new IllegalArgumentException("Invalid OutputMode for FEDM_ISC_TMP_0x72_OUT_MODE_X field");
		}
	}
	
	private byte transformOutputNumberToFeig(OutputBuilder.Entry entry) {
		switch (entry.getEntryEnum()) {
		case DIGITAL_OUTPUT:
			return transformDigitalOutputAddress(entry.getDigitalOutput());
		case RELAY:
			return transformRelayAddress(entry.getRelay());
		case BUZZER:
			return transformBuzzerAddress(entry.getBuzzer());
		case LED:
			switch(entry.getLed()) {
			case SYSTEM:
				return 0x2;
			case ACK:
				return 0x1;
			case NACK:
				return 0x3;
			default:
				throw new IllegalArgumentException("Invalid LED type for FEDM_ISC_TMP_0x72_OUT_NR_X field");
			}
		default:
			throw new IllegalArgumentException("Invalid output type for calculate FEDM_ISC_TMP_0x72_OUT_NR_X field.");
		}
	}
	
	private String[][] FEIG_ADDRESS_DATA = new String[][] {
		{ FEDM_ISC_TMP_0x72_OUT_NR_1, FEDM_ISC_TMP_0x72_OUT_TYPE_1, FEDM_ISC_TMP_0x72_OUT_MODE_1, FEDM_ISC_TMP_0x72_OUT_TIME_1 },
		{ FEDM_ISC_TMP_0x72_OUT_NR_2, FEDM_ISC_TMP_0x72_OUT_TYPE_2, FEDM_ISC_TMP_0x72_OUT_MODE_2, FEDM_ISC_TMP_0x72_OUT_TIME_2 },
		{ FEDM_ISC_TMP_0x72_OUT_NR_3, FEDM_ISC_TMP_0x72_OUT_TYPE_3, FEDM_ISC_TMP_0x72_OUT_MODE_3, FEDM_ISC_TMP_0x72_OUT_TIME_3 },
		{ FEDM_ISC_TMP_0x72_OUT_NR_4, FEDM_ISC_TMP_0x72_OUT_TYPE_4, FEDM_ISC_TMP_0x72_OUT_MODE_4, FEDM_ISC_TMP_0x72_OUT_TIME_4 },
		{ FEDM_ISC_TMP_0x72_OUT_NR_5, FEDM_ISC_TMP_0x72_OUT_TYPE_5, FEDM_ISC_TMP_0x72_OUT_MODE_5, FEDM_ISC_TMP_0x72_OUT_TIME_5 },
		{ FEDM_ISC_TMP_0x72_OUT_NR_6, FEDM_ISC_TMP_0x72_OUT_TYPE_6, FEDM_ISC_TMP_0x72_OUT_MODE_6, FEDM_ISC_TMP_0x72_OUT_TIME_6 },
		{ FEDM_ISC_TMP_0x72_OUT_NR_7, FEDM_ISC_TMP_0x72_OUT_TYPE_7, FEDM_ISC_TMP_0x72_OUT_MODE_7, FEDM_ISC_TMP_0x72_OUT_TIME_7 },
		{ FEDM_ISC_TMP_0x72_OUT_NR_8, FEDM_ISC_TMP_0x72_OUT_TYPE_8, FEDM_ISC_TMP_0x72_OUT_MODE_8, FEDM_ISC_TMP_0x72_OUT_TIME_8 }
	};

	///////////////////////////////////////////////////////////////////////////
	// Codigo para el modo notificación
	///////////////////////////////////////////////////////////////////////////
	
	private FedmTaskListenerImpl taskListener = null; 
	
	public void startNotifier(NotificationListener notificationListener, int notifierPort) throws MifareControllerException {
		
		this.notificationListener = notificationListener;
		
		FedmTaskOption taskOption = new FedmTaskOption();
		taskOption.setIpPort(notifierPort);
		//taskOption.setNotifyWithAck(0);
		//taskOption.setInventoryTimeout((byte) 30); // 30 s
		
		try {
			taskListener = new FedmTaskListenerImpl();
			driver.startAsyncTask(FedmTaskOption.ID_NOTIFICATION, taskListener, taskOption);
			showTableInfo();
		} catch (FeReaderDriverException e) {
			taskListener = null;
			if (driver != null) 
				driver.cancelAsyncTask();
			throw new MifareControllerException("Imposible iniciar la tarea del notificador.", e);
		} catch (FedmException e) {
			taskListener = null;
			if (driver != null) 
				driver.cancelAsyncTask();
			throw new MifareControllerException("Imposible iniciar la tarea del notificador.", e);
		} catch (Exception e) {
			taskListener = null;
			if (driver != null) 
				driver.cancelAsyncTask();
			throw new MifareControllerException("Imposible iniciar la tarea del notificador.", e);
		}
		
	}
	
	public void stopNotifier() {
		if (taskListener != null) {
			driver.cancelAsyncTask();
		}
		notificationListener = null;
	}
	
	private class FedmTaskListenerImpl implements FedmTaskListener {
	
		/**
		 * Listener method for the transponder data received by background Apdu process.
		 * 
		 * @param error error code (<0) or OK (0)
		 */
		@Override
		public void onNewApduResponse(int error) {
			System.out.format("onNewApduResponse(error=%d)\n", error);
			showTableInfo();
		}
	
		/**
		 * Listener method for the transponder data comming with notification event.
		 * 
		 * @param error code (<0) or OK (0) or reader status byte (>0)
		 */
		@Override
		public void onNewNotification(int iError, String ip, int portNr) {
			
			FedmBrmTableItem[] brmItems = null;

			try {
				if (driver.getTableLength(FedmIscReaderConst.BRM_TABLE) > 0)
					brmItems = (FedmBrmTableItem[]) driver.getTable(FedmIscReaderConst.BRM_TABLE);

				if (brmItems != null) {
					
					for (int i = 0; i < brmItems.length; i++) {
						
						NewNotificationEventBuilder builder = new NewNotificationEventBuilder();
						
						builder.setIpAddress(ip);
						builder.setIpPort(portNr);
						builder.setNotificationError(iError);
						
						// Se extrae el numero de serie
						if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_SNR)) {
							builder.setSerialNumber(FeHexConvert.hexStringToByteArray(brmItems[i].getStringData(FedmIscReaderConst.DATA_SNR)));
						}

						// Se extraen los bloques.
						if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_RxDB)) {
							int blockAddress = brmItems[i].getBlockAddress();
							int blockCount = brmItems[i].getBlockCount();
							byte[] b = brmItems[i].getByteArrayData(FedmIscReaderConst.DATA_RxDB, blockAddress, blockCount);
							if (b != null && b.length > 0 && b.length == blockCount * 16) {
								byte[][] blocks = new byte[blockCount][16];
								for (int x = 0; x < blockCount; x++) {
									System.arraycopy(b, x * 16, blocks[x], 0, 16);
								}
								builder.setData(blocks);
								/*
								for (int idx = 0; idx < blocks.length; idx++) {
									System.out.println(FeHexConvert.hexStringToByteArray(FeHexConvert.byteArrayToHexString(blocks[idx])));
								}
								*/
							}
						}

						// El tipo de transponder
						if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_TRTYPE)) {			
							builder.setType(brmItems[i].getStringData(FedmIscReaderConst.DATA_TRTYPE));
						}

						// La hora ?
						if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_TIMER)) {
							builder.setTime(brmItems[i].getReaderTime().getTime());
						}

						// La fecha
						if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_DATE)) { // date
							builder.setDate(brmItems[i].getReaderTime().getDate());
						}

						// Numero de antena
						if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_ANT_NR)) {
							builder.setAntNr(brmItems[i].getStringData(FedmIscReaderConst.DATA_ANT_NR));
						}

						// Datos de entrada
						if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_INPUT)) {
							builder.setInput(brmItems[i].getStringData(FedmIscReaderConst.DATA_INPUT));
							builder.setState(brmItems[i].getStringData(FedmIscReaderConst.DATA_STATE));
						}
						
						//System.out.println(builder.build());
						NewNotificationEvent newNotificationEvent = builder.build();
						notificationListener.onNewNotification(newNotificationEvent);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	
		/**
		 * Listener method for the transponder data received by background Command Queue process.
		 * 
		 * @param error error code (<0) or OK (0) or reader status byte (>0)
		 */
		@Override
		public void onNewQueueResponse(int error) {
			System.out.format("onNewQueueResponse(error=%d)\n", error);
			showTableInfo();
		}
	
		/**
		 * Listener method for the reader diagnostic data comming with notification event.
		 * 
		 * @param error code (<0) or OK (0) or reader status byte (>0)
		 */
		@Override
		public void onNewReaderDiagnostic(int iError, String ip, int portNr) {
			System.out.format("onNewReaderDiagnostic(iError=%d, ip=%s, portNr=%d)\n", iError, ip, portNr);
			showTableInfo();
		}
	
		/**
		 * Listener method for the SAM data received by background process.
		 * 
		 * @param error error code (<0) or OK (0)
		 */
		@Override
		public void onNewSAMResponse(int error, byte[] responseData) {
			System.out.format("onNewSAMResponse");
		}
	
		/**
		 * Listener method for the transponder data received by background Inventory process.
		 * 
		 * @param error code (<0) or OK (0) or reader status byte (>0)
		 */
		@Override
		public void onNewTag(int iError) {
			System.out.format("onNewTag(iError=%d)\n", iError);
			showTableInfo();
		}

		@Override
		public void onNewPeopleCounterEvent(int arg0, int arg1, int arg2,
				int arg3, String arg4, int arg5, int arg6) {
			// TODO Nuevo metodo de la libreria revisar.
			
		}
		
	}
	
	private void showTableInfo() {
		FedmBrmTableItem[] table = null;
		
		if (driver.getTableLength(FedmIscReaderConst.BRM_TABLE) > 0) {
			try {
				table = (FedmBrmTableItem[]) driver.getTable(BRM_TABLE);
			} catch (FedmException e) {
				//TODO: Nueva versión de la libreria reviosar este catch
				e.printStackTrace();
			}
		}
		
		System.out.format("size=%d, lenght=%d\n", 
				driver.getTableSize(BRM_TABLE),
				table == null ? 0 : table.length
				);
	}
	
}
