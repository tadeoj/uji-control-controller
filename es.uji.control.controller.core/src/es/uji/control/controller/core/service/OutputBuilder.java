/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.core.service;

import java.util.Collection;
import java.util.Vector;

public class OutputBuilder {
	
	private Collection<Entry> entries = new Vector<Entry>(4);
	
	public void addDigitalOutput(int digitalOutput, OutputModeEnum outputMode, int millisecs) {
		entries.add(Entry.createDigitalOutput(digitalOutput, outputMode, millisecs));
	}
	
	public void addRelay(int relay, OutputModeEnum outputMode, int millisecs) {
		entries.add(Entry.createRelay(relay, outputMode, millisecs));
	}
	
	public void addBuzzer(int buzzer, OutputModeEnum outputMode, int millisecs) {
		entries.add(Entry.createBuzzer(buzzer, outputMode, millisecs));
	}
	
	public void addLed(OutputLedEnum led, OutputModeEnum outputMode, int millisecs) {
		entries.add(Entry.createLed(led, outputMode, millisecs));
	}
	
	public Collection<Entry> getEntries(OutputsInfo outputsInfo) {
		Collection<Entry> result = new Vector<Entry>(4);
		
		for (Entry entry : entries) {
			switch (entry.getEntryEnum()) {
			case DIGITAL_OUTPUT:
				if (entry.getDigitalOutput() >= 0 && entry.getDigitalOutput() < outputsInfo.digitalOutputCount()) {
					result.add(entry);
				}
				break;
			case BUZZER:
				if (entry.getBuzzer() >= 0 && entry.getBuzzer() < outputsInfo.buzzerCount()) {
					result.add(entry);
				}
				break;
			case LED:
				if (entry.getRelay() >= 0 && entry.getRelay() < outputsInfo.relayCount()) {
					result.add(entry);
				}
				break;
			case RELAY:
				if (outputsInfo.hasLed(entry.getLed())) {
					result.add(entry);
				}
				break;
			}
		}
		return result;
	}
	
	static public enum EntryEnum {
		DIGITAL_OUTPUT,
		RELAY,
		BUZZER,
		LED
	}
	
	static public class Entry {
		private EntryEnum entryEnum;
		
		private int digitalOutput;
		private int relay;
		private int buzzer;
		private OutputLedEnum led;
		
		private OutputModeEnum outputMode;
		private int millisecs;
		
		static public Entry createDigitalOutput(int digitalOutput, OutputModeEnum outputMode, int millisecs) {
			Entry entry = new Entry();
			entry.entryEnum = EntryEnum.DIGITAL_OUTPUT;
			entry.digitalOutput = digitalOutput;
			entry.outputMode = outputMode;
			entry.millisecs = millisecs;
			return entry;
		}
		
		static public Entry createRelay(int relay, OutputModeEnum outputMode, int millisecs) {
			Entry entry = new Entry();
			entry.entryEnum = EntryEnum.RELAY;
			entry.relay = relay;
			entry.outputMode = outputMode;
			entry.millisecs = millisecs;
			return entry;
		}
		
		static public Entry createBuzzer(int buzzer, OutputModeEnum outputMode, int millisecs) {
			Entry entry = new Entry();
			entry.entryEnum = EntryEnum.BUZZER;
			entry.buzzer = buzzer;
			entry.outputMode = outputMode;
			entry.millisecs = millisecs;
			return entry;
		}
		
		static public Entry createLed(OutputLedEnum led, OutputModeEnum outputMode, int millisecs) {
			Entry entry = new Entry();
			entry.entryEnum = EntryEnum.LED;
			entry.led = led;
			entry.outputMode = outputMode;
			entry.millisecs = millisecs;
			return entry;
		}

		public EntryEnum getEntryEnum() {
			return entryEnum;
		}

		public int getDigitalOutput() {
			return digitalOutput;
		}

		public int getRelay() {
			return relay;
		}

		public int getBuzzer() {
			return buzzer;
		}

		public OutputLedEnum getLed() {
			return led;
		}

		public OutputModeEnum getOutputMode() {
			return outputMode;
		}

		public int getMillisecs() {
			return millisecs;
		}
		
	}

}
