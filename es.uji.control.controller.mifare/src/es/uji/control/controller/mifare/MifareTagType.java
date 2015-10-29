/*******************************************************************************
 * Copyright © Universitat Jaume I de Castelló 2015.
 * Aquest programari es distribueix sota les condicions de llicència EUPL 
 * o de qualsevol altra que la substituisca en el futur.
 * La llicència completa es pot descarregar de 
 * https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11
 *******************************************************************************/
package es.uji.control.controller.mifare;

public enum MifareTagType {

	/*
	 * El valor 2 corresponde a MifareClassic1K segun lectores ACR
	 */
    CLASSIC_1K (2) {
    	public int getSectors() {
			return 16; // 16 sectores de 4 bloques
    	}
    	public int getBlocks() {
			return  64;
    	}
    	public int getFirstBlockOfSector(int sector) {
			if (sector < 0 || sector >= 16)
				throw new RuntimeException("Invalid sector number");
			return (sector * 4);
    	}
    	public int getTrailerBlockOfSector(int sector) {
			if (sector < 0 || sector >= 16)
				throw new RuntimeException("Invalid sector number");
			return (sector * 4) + 3;
    	}
    	public int getSectorBlocks(int sector) {
			if (sector < 0 || sector >= 16)
				throw new RuntimeException("Invalid sector number");
			return 4;
    	}
    	public int getSectorForBlock(int block) {
			if (block < 0 || block >= 64)  
				throw new RuntimeException("Invalid block number");
			else
				return block / 4;
    	}
    	public int getSerialNumberSize() {
    		return 4;
    	}
	},
	/*
	 * El valor 3 corresponde a MifareClassic1K segun lectores ACR
	 */
    CLASSIC_4K (3) {
    	public int getSectors() {
			return 40; // 32 sectores de 4 bloques + 4 sec de 16 bloques
    	}
    	public int getBlocks() {
			return 272;
    	}
    	public int getFirstBlockOfSector(int sector) {
			if (sector < 0 || sector >= 40)
				throw new RuntimeException("Invalid sector number");
			if (sector < 32)
				return (sector * 4);
			else
				return (32 * 4) + ((sector - 32) * 16);
    	}
    	public int getTrailerBlockOfSector(int sector) {
			if (sector < 0 || sector >= 40)
				throw new RuntimeException("Invalid sector number");
			if (sector < 32)
				return (sector * 4) + 3;
			else
				return (32 * 4) + ((sector - 32) * 16) + 15;
    	}
    	public int getSectorBlocks(int sector) {
			if (sector < 0 || sector >= 36)
				throw new RuntimeException("Invalid sector number");
			if (sector < 32)
				return 4;
			else
				return 16;
    	}
    	public int getSectorForBlock(int block) {
			if (block < 0 || block >= 272)
				throw new RuntimeException("Invalid block number");
			if (block < 128)
				return block / 4;
			else
				return 32 + ((block - 128) / 16);
    	}
    	public int getSerialNumberSize() {
    		return 4;
    	}
	},
    ISO14443A (4) {
    	public int getSectors() {
			return 40; // 32 sectores de 4 bloques + 4 sec de 16 bloques
    	}
    	public int getBlocks() {
			return 272;
    	}
    	public int getFirstBlockOfSector(int sector) {
			if (sector < 0 || sector >= 40)
				throw new RuntimeException("Invalid sector number");
			if (sector < 32)
				return (sector * 4);
			else
				return (32 * 4) + ((sector - 32) * 16);
    	}
    	public int getTrailerBlockOfSector(int sector) {
			if (sector < 0 || sector >= 40)
				throw new RuntimeException("Invalid sector number");
			if (sector < 32)
				return (sector * 4) + 3;
			else
				return (32 * 4) + ((sector - 32) * 16) + 15;
    	}
    	public int getSectorBlocks(int sector) {
			if (sector < 0 || sector >= 36)
				throw new RuntimeException("Invalid sector number");
			if (sector < 32)
				return 4;
			else
				return 16;
    	}
    	public int getSectorForBlock(int block) {
			if (block < 0 || block >= 272)
				throw new RuntimeException("Invalid block number");
			if (block < 128)
				return block / 4;
			else
				return 32 + ((block - 128) / 16);
    	}
    	public int getSerialNumberSize() {
    		return 4;
    	}
	};

	private int id;
	
	MifareTagType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public abstract int getSectors();
	public abstract int getBlocks();
	public abstract int getFirstBlockOfSector(int sector);
	public abstract int getTrailerBlockOfSector(int sector);
	public abstract int getSectorBlocks(int sector);
	public abstract int getSectorForBlock(int block);
	public abstract int getSerialNumberSize();
	
	public static MifareTagType getById(int id) {
		for (MifareTagType mifareType : MifareTagType.values()) {
			if (id == mifareType.getId())
				return mifareType;
		}
		return null;
	}
	
}
