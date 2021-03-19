package com.NBK.MineZ.chest;

public enum EnumChestMetadata {

	Name("Name"),
	IsOpen("IsOpen"),
	FaceDirection("FaceDirection");
	
	private String key;
	
	private EnumChestMetadata(final String k) {
		this.key = k;
	}
	
	public String getKey() {
		return key;
	}
	
}
