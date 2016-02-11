package com.mlink.iwm.entity.factory;

public enum PhoneType implements IEnumerated{

	MO("Mobil"),
	HO("Home"),
	FA("Fax"),
	WO("Work");

	
	private final String label;
	
	private PhoneType(String l){
		this.label=l;
		
	}
	
	
	public String getLabel(){
		return label;
	}
	
	
}
