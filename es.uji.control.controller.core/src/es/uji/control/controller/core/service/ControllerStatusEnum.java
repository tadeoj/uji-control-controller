package es.uji.control.controller.core.service;

public enum ControllerStatusEnum {
	
	CONNECTING {
		@Override
		public boolean hasException() {
			return false;
		}
	},
	OK {
		@Override
		public boolean hasException() {
			return false;
		}
	},
	WAITING_RECONNECT {
		@Override
		public boolean hasException() {
			return true;
		}
	};
	
	public abstract boolean hasException();
}
