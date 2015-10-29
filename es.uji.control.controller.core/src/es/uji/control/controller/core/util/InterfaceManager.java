/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InterfaceManager {
	
	private Map<Class<?>, Object> map;
	
	public InterfaceManager() {
		map = new HashMap<Class<?>, Object>();
	}
	
	public InterfaceManager add(Class<?> interfaze, Object implementation) {
		
		if (!interfaze.isInterface())
			throw new IllegalArgumentException("Se ha proporcinado como interface de referencia una clase en lugar de un interface.");
		
		if (!interfaze.isInstance(implementation))
			throw new IllegalArgumentException("La instancia que se ha proporcionado no implementa el interface de referencia.");
		
		if (map.containsKey(interfaze))
			throw new IllegalArgumentException("El interface de referencia ya esta registrado.");
		
		map.put(interfaze, implementation);
		
		return this;
	}
	
	public Object remove(Class<?> interfaze) {
		return map.remove(interfaze);
	}

	public Set<Class<?>> getInterfaces() {
		return map.keySet();
	}
	
	public boolean contains(Class<?> interfaze) {
		return getInterfaces().contains(interfaze);
	}
	
	public Object getImplementation(Class<?> interfaze) {
		return map.get(interfaze);
	}
	
	public void clear() {
		map.clear();
	}

}
