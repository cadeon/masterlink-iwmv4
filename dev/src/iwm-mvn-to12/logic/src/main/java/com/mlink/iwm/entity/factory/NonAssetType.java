package com.mlink.iwm.entity.factory;

public enum NonAssetType implements IEnumerated{

	NA2("Consumable"),
	NA3("Kit"),
	NA1("Repair Part");
	

	
	private final String label;
	
	private NonAssetType(String l){
		this.label=l;
		
	}
	
	
	public String getLabel(){
		return label;
	}
	
	
}
