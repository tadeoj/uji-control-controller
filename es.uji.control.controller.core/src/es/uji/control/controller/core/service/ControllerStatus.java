package es.uji.control.controller.core.service;

public interface ControllerStatus {
	ControllerStatusEnum getControllerStatusEnum();
	ControllerException getControllerException();
	ControllerStatusInfo getControllerStatusInfo();
}
