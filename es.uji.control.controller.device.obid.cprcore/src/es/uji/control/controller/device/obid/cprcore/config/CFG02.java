package es.uji.control.controller.device.obid.cprcore.config;

import de.feig.ReaderConfig.DigitalIO;
import es.uji.control.controller.device.obid.cprcore.CPRAbstract;
import es.uji.control.controller.mifare.MifareControllerException;


public class CFG02 {
	
	final static public int REGISTER = 02;
	
	
	public enum OutputMode {
		
		OFF ((byte) 0),
		ON ((byte) 1),
		FLASHING_SLOW ((byte) 2),
		FLASHIN_FAST ((byte) 3);
		
		private byte id;
		
		private OutputMode(byte id) {
			this.id = id;
		}
		
		public byte getId() {
			return id;
		}
		
		public static OutputMode getById(byte id) throws IllegalArgumentException {
			for (OutputMode outputMode : OutputMode.values()) {
				if (id == outputMode.getId())
					return outputMode;
			}
			throw new IllegalArgumentException("Invalid Id to build CFG2.OutputMode.");
		}

	}
	
	public enum InputEvent {
		
		DEACTIVATED ((byte) 0),
		RESPONSE_WHEN_INACTIVE_TO_ACTIVE ((byte) 1),
		RESPONSE_WHEN_ACTIVE_TO_INACTIVE ((byte) 2),
		RESPONSE_WHEN_ANY_CHANGE ((byte) 3);
		
		private byte id;
		
		private InputEvent(byte id) {
			this.id = id;
		}
		
		public byte getId() {
			return id;
		}
		
		public static InputEvent getById(byte id) throws IllegalArgumentException {
			for (InputEvent inputEvent : InputEvent.values()) {
				if (id == inputEvent.getId())
					return inputEvent;
			}
			throw new IllegalArgumentException("Invalid Id to build CFG2.OutputMode.");
		}

	}
	
	private CPRAbstract cpr;
	private MemoryType memoryType;
	private boolean dirty = false;
	
	private OutputMode onlineStateBlue;
	private OutputMode onlineStateRed;
	private OutputMode onlineStateGreen;
	private OutputMode onlineStateBuzzer;
	
	private OutputMode offlineStateBlue;
	private OutputMode offlineStateRed;
	private OutputMode offlineStateGreen;
	private OutputMode offlineStateBuzzer;
	private byte offLineDelay;
	
	/*private OutputMode tagDetectedStateBlue;
	private OutputMode tagDetectedStateRed;
	private OutputMode tagDetectedStateGreen;
	private OutputMode tagDetectedStateBuzzer;
	private byte tagDetectedActivationTime;
	
	// TODO: No sabemos que constantes del ReaderConfig son las correspondientes.
	private InputEvent inputEvent;*/
	// TODO: No sabemos que constantes del ReaderConfig son las correspondientes.
	private boolean cprioEnable;
	
	static public CFG02 readRegister(CPRAbstract cpr, MemoryType memoryType) throws MifareControllerException {
		
		cpr.readConfigurationRegister(REGISTER, memoryType);
		
		return new CFG02(
			cpr,
			memoryType,
			OutputMode.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.LED.Blue.OnlineState, memoryType.getValue())),
			OutputMode.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.LED.Red.OnlineState, memoryType.getValue())),
			OutputMode.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.LED.Green.OnlineState, memoryType.getValue())),
			OutputMode.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.Buzzer.OnlineState, memoryType.getValue())),
			
			OutputMode.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.LED.Blue.OfflineState, memoryType.getValue())),
			OutputMode.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.LED.Red.OfflineState, memoryType.getValue())),
			OutputMode.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.LED.Green.OfflineState, memoryType.getValue())),
			OutputMode.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.Buzzer.OfflineState, memoryType.getValue())),
			cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.OfflineDelay, memoryType.getValue()),
			
			/*OutputMode.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.LED.Blue.TagDetectState, memoryType.getValue())),
			OutputMode.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.LED.Red.TagDetectState, memoryType.getValue())),
			OutputMode.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.LED.Green.TagDetectState, memoryType.getValue())),
			OutputMode.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.Buzzer.TagDetectState, memoryType.getValue())),
			cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.TagDetectActiveTime, memoryType.getValue()),
			
			InputEvent.getById(cpr.getDriver().getConfigParaAsByte(DigitalIO.Signaler.EventSource.StartupSignal, memoryType.getValue())),*/
			true
		);

	}
	
	private CFG02(
			CPRAbstract cpr,
			MemoryType memoryType,
			OutputMode onlineStateBlue,
			OutputMode onlineStateRed,
			OutputMode onlineStateGreen,
			OutputMode onlineStateBuzzer,
			OutputMode offlineStateBlue,
			OutputMode offlineStateRed,
			OutputMode offlineStateGreen,
			OutputMode offlineStateBuzzer,
			byte offLineDelay,
		/*	OutputMode tagDetectedStateBlue,
			OutputMode tagDetectedStateRed,
			OutputMode tagDetectedStateGreen,
			OutputMode tagDetectedStateBuzzer,
			byte tagDetectedActivationTime,
			InputEvent inputEvent,*/
			boolean cprioEnable
			) {
		this.cpr = cpr;
		this.memoryType = memoryType;
		
		this.onlineStateBlue = onlineStateBlue;
		this.onlineStateRed = onlineStateRed;
		this.onlineStateGreen = onlineStateGreen;
		this.onlineStateBuzzer = onlineStateBuzzer;
		
		this.offlineStateBlue = offlineStateBlue;
		this.offlineStateRed = offlineStateRed;
		this.offlineStateGreen = offlineStateGreen;
		this.offlineStateBuzzer = offlineStateBuzzer;
		this.offLineDelay = offLineDelay;
		
		/*this.tagDetectedStateBlue = tagDetectedStateBlue;
		this.tagDetectedStateRed = tagDetectedStateRed;
		this.tagDetectedStateGreen = tagDetectedStateGreen;
		this.tagDetectedStateBuzzer = tagDetectedStateBuzzer;
		this.tagDetectedActivationTime = tagDetectedActivationTime;
		
		this.inputEvent = inputEvent;*/
		this.cprioEnable = cprioEnable;
	}
	
	public void writeRegister() throws MifareControllerException {
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.LED.Blue.OnlineState, onlineStateBlue.getId(), memoryType.getValue());
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.LED.Red.OnlineState, onlineStateRed.getId(), memoryType.getValue());
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.LED.Green.OnlineState, onlineStateGreen.getId(), memoryType.getValue());
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.Buzzer.OnlineState, onlineStateBuzzer.getId(), memoryType.getValue());
		
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.LED.Blue.OfflineState, offlineStateBlue.getId(), memoryType.getValue());
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.LED.Red.OfflineState, offlineStateRed.getId(), memoryType.getValue());
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.LED.Green.OfflineState, offlineStateGreen.getId(), memoryType.getValue());
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.Buzzer.OfflineState, offlineStateBuzzer.getId(), memoryType.getValue());
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.OfflineDelay, offLineDelay, memoryType.getValue());
		
		/*cpr.getDriver().setConfigPara(DigitalIO.Signaler.LED.Blue.TagDetectState, tagDetectedStateBlue.getId(), memoryType.getValue());
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.LED.Red.TagDetectState, tagDetectedStateRed.getId(), memoryType.getValue());
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.LED.Green.TagDetectState, tagDetectedStateGreen.getId(), memoryType.getValue());
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.Buzzer.TagDetectState, tagDetectedStateBuzzer.getId(), memoryType.getValue());
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.TagDetectActiveTime, tagDetectedActivationTime, memoryType.getValue());
		
		cpr.getDriver().setConfigPara(DigitalIO.Signaler.EventSource.StartupSignal, inputEvent.getId(), memoryType.getValue());*/
		
		cpr.writeConfigurationRegister(REGISTER, memoryType);
		dirty = false;
	}
	
	public MemoryType getMemoryType() {
		return this.memoryType;
	}

	public OutputMode getOnlineStateBlue() {
		return onlineStateBlue;
	}

	public void setOnlineStateBlue(OutputMode onlineStateBlue) {
		if (this.onlineStateBlue != onlineStateBlue) {
			dirty = true;
			this.onlineStateBlue = onlineStateBlue;
		}
	}

	public OutputMode getOnlineStateRed() {
		return onlineStateRed;
	}

	public void setOnlineStateRed(OutputMode onlineStateRed) {
		if (this.onlineStateRed != onlineStateRed) {
			dirty = true;
			this.onlineStateRed = onlineStateRed;
		}
	}

	public OutputMode getOnlineStateGreen() {
		return onlineStateGreen;
	}

	public void setOnlineStateGreen(OutputMode onlineStateGreen) {
		if (this.onlineStateGreen != onlineStateGreen) {
			dirty = true;
			this.onlineStateGreen = onlineStateGreen;
		}
	}

	public OutputMode getOnlineStateBuzzer() {
		return onlineStateBuzzer;
	}

	public void setOnlineStateBuzzer(OutputMode onlineStateBuzzer) {
		if (this.onlineStateBuzzer != onlineStateBuzzer) {
			dirty = true;
			this.onlineStateBuzzer = onlineStateBuzzer;
		}
	}

	public OutputMode getOfflineStateBlue() {
		return offlineStateBlue;
	}

	public void setOfflineStateBlue(OutputMode offlineStateBlue) {
		if (this.offlineStateBlue != offlineStateBlue) {
			dirty = true;
			this.offlineStateBlue = offlineStateBlue;
		}
	}

	public OutputMode getOfflineStateRed() {
		return offlineStateRed;
	}

	public void setOfflineStateRed(OutputMode offlineStateRed) {
		if (this.offlineStateRed != offlineStateRed) {
			dirty = true;
			this.offlineStateRed = offlineStateRed;
		}
	}

	public OutputMode getOfflineStateGreen() {
		return offlineStateGreen;
	}

	public void setOfflineStateGreen(OutputMode offlineStateGreen) {
		if (this.offlineStateGreen != offlineStateGreen) {
			dirty = true;
			this.offlineStateGreen = offlineStateGreen;
		}
	}

	public OutputMode getOfflineStateBuzzer() {
		return offlineStateBuzzer;
	}

	public void setOfflineStateBuzzer(OutputMode offlineStateBuzzer) {
		if (this.offlineStateBuzzer != offlineStateBuzzer) {
			dirty = true;
			this.offlineStateBuzzer = offlineStateBuzzer;
		}
	}

	public int getOffLineDelay() {
		return offLineDelay;
	}

	public void setOffLineDelay(int offLineDelay) {
		if (this.offLineDelay != (byte) offLineDelay) {
			dirty = true;
			this.offLineDelay = (byte) offLineDelay;
		}
	}
/*
	public OutputMode getTagDetectedStateBlue() {
		return tagDetectedStateBlue;
	}

	public void setTagDetectedStateBlue(OutputMode tagDetectedStateBlue) {
		if (this.tagDetectedStateBlue != tagDetectedStateBlue) {
			dirty = true;
			this.tagDetectedStateBlue = tagDetectedStateBlue;
		}
	}

	public OutputMode getTagDetectedStateRed() {
		return tagDetectedStateRed;
	}

	public void setTagDetectedStateRed(OutputMode tagDetectedStateRed) {
		if (this.tagDetectedStateRed != tagDetectedStateRed) {
			dirty = true;
			this.tagDetectedStateRed = tagDetectedStateRed;
		}
	}

	public OutputMode getTagDetectedStateGreen() {
		return tagDetectedStateGreen;
	}

	public void setTagDetectedStateGreen(OutputMode tagDetectedStateGreen) {
		if (this.tagDetectedStateGreen != tagDetectedStateGreen) {
			dirty = true;
			this.tagDetectedStateGreen = tagDetectedStateGreen;
		}
	}

	public OutputMode getTagDetectedStateBuzzer() {
		return tagDetectedStateBuzzer;
	}

	public void setTagDetectedStateBuzzer(OutputMode tagDetectedStateBuzzer) {
		if (this.tagDetectedStateBuzzer != tagDetectedStateBuzzer) {
			dirty = true;
			this.tagDetectedStateBuzzer = tagDetectedStateBuzzer;
		}
	}

	public int getTagDetectedActivationTime() {
		return tagDetectedActivationTime;
	}

	public void setTagDetectedActivationTime(int tagDetectedActivationTime) {
		if (this.tagDetectedActivationTime != (byte) tagDetectedActivationTime) {
			dirty = true;
			this.tagDetectedActivationTime = (byte) tagDetectedActivationTime;
		}
	}

	public InputEvent getInputEvent() {
		return inputEvent;
	}

	public void setInputEvent(InputEvent inputEvent) {
		if (this.inputEvent != inputEvent) {
			dirty = true;
			this.inputEvent = inputEvent;
		}
	}*/

	public boolean isCprioEnable() {
		return cprioEnable;
	}

	public void setCprioEnable(boolean cprioEnable) {
		if (this.cprioEnable != cprioEnable) {
			dirty = true;
			this.cprioEnable = cprioEnable;
		}
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.LED.Green.OfflineState, getOfflineStateGreen()));
		buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.LED.Blue.OfflineState, getOfflineStateBlue()));
		buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.LED.Red.OfflineState, getOfflineStateRed()));
		buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.Buzzer.OfflineState, getOfflineStateBuzzer()));
		buffer.append(String.format("%s: %d\n", DigitalIO.Signaler.OfflineDelay, getOffLineDelay()));
		
		buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.LED.Green.OnlineState, getOnlineStateGreen()));
		buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.LED.Blue.OnlineState, getOnlineStateBlue()));
		buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.LED.Red.OnlineState, getOnlineStateRed()));
		buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.Buzzer.OnlineState, getOnlineStateBuzzer()));
		
		/*buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.LED.Green.TagDetectState, getTagDetectedStateGreen()));
		buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.LED.Blue.TagDetectState, getTagDetectedStateBlue()));
		buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.LED.Red.TagDetectState, getTagDetectedStateRed()));
		buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.Buzzer.TagDetectState, getTagDetectedStateBuzzer()));
		buffer.append(String.format("%s: %d\n", DigitalIO.Signaler.TagDetectActiveTime, getTagDetectedActivationTime()));
		
		buffer.append(String.format("%s: %s\n", DigitalIO.Signaler.EventSource.StartupSignal, getInputEvent()));*/
		
		return buffer.toString();
	}

}
