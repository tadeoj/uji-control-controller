package es.uji.control.controller.device.obid.cprcore;

public class NewNotificationEvent {
	
	private int notificationError;
	private String ipAddress;
	private int ipPort;
	private byte[] serialNumber;
	private byte[][] data;
	private String type;
	private String date;
	private String time;
	private String antNr;
	private String input;
	private String state;

	public NewNotificationEvent(int notificationError, String ipAddress, int ipPort, byte[] serialNumber, byte[][] data, String type, String date, String time, String antNr, String input, String state) {
		super();
		this.notificationError = notificationError;
		this.ipAddress = ipAddress;
		this.ipPort = ipPort;
		this.serialNumber = serialNumber;
		this.data = data;
		this.type = type;
		this.date = date;
		this.time = time;
		this.antNr = antNr;
		this.input = input;
		this.state = state;
	}

	public int getNotificationError() {
		return notificationError;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public int getIpPort() {
		return ipPort;
	}

	public byte[] getSerialNumber() {
		return serialNumber;
	}

	public byte[][] getData() {
		return data;
	}

	public String getType() {
		return type;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getAntNr() {
		return antNr;
	}

	public String getInput() {
		return input;
	}

	public String getState() {
		return state;
	}
	
	public String toString() {
		return String.format("ipAddress=%s, ipPort=%d, notificationError=%d, serialNumber=%s, data=%s, type=%s, date=%s, time=%s, antNr=%s, input=%s, state=%s", 
				getIpAddress(), getIpPort(), getNotificationError(), getSerialNumber(), getData(), getType(), getDate(), getTime(), getAntNr(), getInput(), getState());
	}

}
