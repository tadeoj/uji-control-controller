package es.uji.control.controller.core.service;

import java.util.Map;

public interface ControllerInfo {
	long getId();
	String getName();
	String getManufacturer();
	String getModel();
	String getPort();
	String getType();
	Map<String, Object> getProperties();
}
