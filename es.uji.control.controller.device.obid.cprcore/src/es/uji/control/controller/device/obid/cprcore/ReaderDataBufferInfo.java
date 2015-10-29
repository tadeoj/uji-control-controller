/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.device.obid.cprcore;

public class ReaderDataBufferInfo {
	
	private int tabSize;
	private int tabStart;
	private int tagLenght;
	
	public ReaderDataBufferInfo(int tabSize, int tabStart, int tagLenght) {
		this.tabSize = tabSize;
		this.tabStart = tabStart;
		this.tagLenght = tagLenght;
	}
	
	public int getTabSize() {
		return tabSize;
	}
	
	public int getTabStart() {
		return tabStart;
	}
	
	public int getTagLenght() {
		return tagLenght;
	}
	
	public String toString() {
		return String.format("tabSize=%d, tabStart=%d, tabLenght=%d", getTabSize(), getTabStart(), getTagLenght());
	}

}
