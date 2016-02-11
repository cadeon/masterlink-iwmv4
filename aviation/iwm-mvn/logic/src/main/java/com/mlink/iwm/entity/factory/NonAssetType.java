package com.mlink.iwm.entity.factory;

public enum NonAssetType implements IEnumerated{

	NA1("Repair Part"),
	NA2("Consumable"),
	NA3("Kit");

	
	private final String label;
	
	private NonAssetType(String l){
		this.label=l;
		
	}
	
	
	public String getLabel(){
		return label;
	}
	
	
}
