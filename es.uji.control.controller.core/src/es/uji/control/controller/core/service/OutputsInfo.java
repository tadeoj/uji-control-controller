package es.uji.control.controller.core.service;

public interface OutputsInfo {

	public int digitalOutputCount();
	public int relayCount();
	public int buzzerCount();
	public boolean hasLed(OutputLedEnum led);
	
}
