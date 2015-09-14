package es.uji.control.controller.device.obid.cpr4030usb.internal;

import java.util.Date;

import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import es.uji.control.controller.core.util.AbstractCommandAdmin;
import es.uji.control.controller.core.service.CommandInfo;
import es.uji.control.controller.core.service.CommandManager;
import es.uji.control.controller.core.service.CommandStatus;
import es.uji.control.controller.core.service.EventControllerStatus;
import es.uji.control.controller.mifare.CommandMifare;
import es.uji.control.controller.mifare.CommandMifareAdmin;
import es.uji.control.controller.mifare.EventMifareManualTagDetected;
import es.uji.control.controller.mifare.EventMifareScanTagDetected;
import es.uji.control.controller.mifare.EventMifareScanTagLosted;
import es.uji.control.controller.mifare.MifareTagId;

abstract public class CommandBase {
	
	protected ControllerContext controllerContext;
	private ServiceTracker eventAdminTracker;
	
	public CommandBase(ControllerContext controllerContext) {
		this.controllerContext = controllerContext;
		this.eventAdminTracker = new ServiceTracker(this.controllerContext.getContext(), EventAdmin.class.getName(), null);
		this.eventAdminTracker.open();
	}
	
	protected void sendEventMifareScanTagDetected(MifareTagId mifareTagId) {
		EventMifareScanTagDetected.sendEvent(
			(EventAdmin) eventAdminTracker.getService(), 
			new EventMifareScanTagDetectedImpl(), 
			new Date(), 
			controllerContext.getControllerInfo().getId(),
			mifareTagId
			);
	}

	protected void postEventMifareScanTagLosted(MifareTagId mifareTagId) {
		EventMifareScanTagLosted.postEvent(
			(EventAdmin) eventAdminTracker.getService(), 
			new EventMifareScanTagLostedImpl(), 
			new Date(), 
			controllerContext.getControllerInfo().getId(),
			mifareTagId 
			);
	}

	protected void sendEventMifareManualTagDetected(MifareTagId mifareTagId) {
		EventMifareManualTagDetected.sendEvent(
			(EventAdmin) eventAdminTracker.getService(), 
			new EventMifareManualTagDetectedImpl(), 
			new Date(), 
			controllerContext.getControllerInfo().getId(),
			mifareTagId
			);
	}

	protected void sendEventControllerStatus() {
		EventControllerStatus.sendEvent(
			(EventAdmin) eventAdminTracker.getService(), 
			new EventControllerStatusImpl(),
			new Date()
			);
	}

	private class EventMifareManualTagDetectedImpl extends AbstractCommandAdmin {
		
		public EventMifareManualTagDetectedImpl() {
			initController();
		}
		
		@Override
		protected void initController() {
			addCommand(CommandMifareAdmin.class, controllerContext.getCommandQuery().getCommand(CommandMifareAdmin.class));
			addCommand(CommandInfo.class, controllerContext.getCommandQuery().getCommand(CommandInfo.class));
			addCommand(CommandMifare.class, controllerContext.getCommandQuery().getCommand(CommandMifare.class));
		}
		
	}
	
	private class EventMifareScanTagDetectedImpl extends AbstractCommandAdmin {
		
		public EventMifareScanTagDetectedImpl() {
			initController();
		}
		
		@Override
		protected void initController() {
			addCommand(CommandInfo.class, controllerContext.getCommandQuery().getCommand(CommandInfo.class));
			addCommand(CommandMifare.class, controllerContext.getCommandQuery().getCommand(CommandMifare.class));
		}
		
	}
	
	private class EventMifareScanTagLostedImpl extends AbstractCommandAdmin {
		
		public EventMifareScanTagLostedImpl() {
			initController();
		}
		
		@Override
		protected void initController() {
			addCommand(CommandInfo.class, controllerContext.getCommandQuery().getCommand(CommandInfo.class));
		}
		
	}
	
	private class EventControllerStatusImpl extends AbstractCommandAdmin {
		
		public EventControllerStatusImpl() {
			initController();
		}
		
		@Override
		protected void initController() {
			addCommand(CommandManager.class, controllerContext.getCommandQuery().getCommand(CommandManager.class));
			addCommand(CommandInfo.class, controllerContext.getCommandQuery().getCommand(CommandInfo.class));
			addCommand(CommandStatus.class, controllerContext.getCommandQuery().getCommand(CommandStatus.class));
		}
		
	}
	
}
