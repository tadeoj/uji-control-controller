package es.uji.control.controller.core.util;

import java.util.Hashtable;
import java.util.Map;

import es.uji.control.controller.core.service.ControllerConstants;
import es.uji.control.controller.core.service.ControllerInfo;

public class ControllerInfoImpl implements ControllerInfo {
	
	final private long id;
	final private String name;
	final private String manufacturer;
	final private String model;
	final private String port;
	final private String type;
	
	public ControllerInfoImpl(long id, String name, String manufacturer, String model, String port, String type) {
		this.id = id;
		this.name = name;
		this.manufacturer = manufacturer;
		this.model = model;
		this.port = port;
		this.type = type;
	}
	
	public Map<String, Object> getProperties() {
		Hashtable<String, Object> properties = new Hashtable<String, Object>();
		
		properties.put(ControllerConstants.NAME, getName());
		properties.put(ControllerConstants.CONTROLLER_TYPE, getType());
		properties.put(ControllerConstants.PORT, getPort());
		properties.put(ControllerConstants.MANUFACTURER, getManufacturer());
		properties.put(ControllerConstants.MODEL, getModel());
		properties.put(ControllerConstants.ID, getId());
		
		return properties;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getManufacturer() {
		return manufacturer;
	}

	@Override
	public String getModel() {
		return model;
	}

	@Override
	public String getPort() {
		return port;
	}

	@Override
	public String getType() {
		return type;
	}

}
