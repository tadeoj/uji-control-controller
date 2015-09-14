package es.uji.control.controller.device.obid.cpr5010eth.internal;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;

import es.uji.control.controller.device.obid.cpr5010eth.CPR5010EthInstance;
import es.uji.control.controller.device.obid.cpr5010eth.ICPR5010Eth;

public class ConfigComponent implements ICPR5010Eth {
	
	private ComponentFactory factory;
	private Map<CPR5010EthInstance, ComponentInstance> map;
	
	public ConfigComponent() {
		map = new HashMap<CPR5010EthInstance, ComponentInstance>();
	}
	
	public void setFactory(ComponentFactory factory) {
		this.factory = factory;
	}
	
	public void startup() {
	}
	
	public void shutdown() {
		for (CPR5010EthInstance current : map.keySet()) {
			ComponentInstance componentInstance = map.get(current);
			if (componentInstance != null) {
				componentInstance.dispose();
			}
		}
		map.clear();
	}
	
	@Override
	public CPR5010EthInstance open(String address) {
		return open(address, CPR5010EthUtil.DEFAULT_PORT);
	}
	
	@Override
	public CPR5010EthInstance open(String address, int port) {
		
		// Se prepara el identificador
		CPR5010EthInstanceImpl instance = new CPR5010EthInstanceImpl(address, port);
		
		// Se preparan las propiedades para instanciar el componente.
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(CPR5010EthUtil.PROPERTY_ADDRESS, address);
		properties.put(CPR5010EthUtil.PROPERTY_PORT, port);
		ComponentInstance componentInstance = factory.newInstance(properties);
		map.put(instance, componentInstance);
		
		// Se retorna el identificador
		return instance;
	}
	
	@Override
	public void close(CPR5010EthInstance instance) {
		ComponentInstance componentInstance = map.remove(instance);
		if (componentInstance != null) {
			componentInstance.dispose();
		}
	}

	public class CPR5010EthInstanceImpl implements CPR5010EthInstance {
		
		private String address;
		private int port;
		
		public CPR5010EthInstanceImpl(String address, int port) {
			this.address = address;
			this.port = port;
		}

		@Override
		public String getAddress() {
			return address;
		}

		@Override
		public int getPort() {
			return port;
		}

		public String toString() {
			return String.format("address:%s, port:%d", getAddress(), getPort());
		}

	}
	
}
