package com.mlink.iwm.entity.factory;

public enum AddressType implements IEnumerated{

	HO("Home"),
	CO("Corporate"),
	WO("Work"),
	LO("Branch");

	
	private final String label;
	
	private AddressType(String l){
		this.label=l;
		
	}

	public String getLabel(){
		return label;
	}
	
	
}
