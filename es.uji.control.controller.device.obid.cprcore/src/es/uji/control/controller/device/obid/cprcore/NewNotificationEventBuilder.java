package es.uji.control.controller.device.obid.cprcore;

class NewNotificationEventBuilder {

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
	
	public NewNotificationEvent build() {
		return new NewNotificationEvent(
			notificationError, 
			ipAddress, 
			ipPort,
			serialNumber, 
			data, 
			type, 
			date, 
			time, 
			antNr, 
			input, 
			state
		);
	}
	
	public void setSerialNumber(byte[] serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public void setNotificationError(int notificationError) {
		this.notificationError = notificationError;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setIpPort(int ipPort) {
		this.ipPort = ipPort;
	}

	public void setData(byte[][] data) {
		this.data = data;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setAntNr(String antNr) {
		this.antNr = antNr;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
