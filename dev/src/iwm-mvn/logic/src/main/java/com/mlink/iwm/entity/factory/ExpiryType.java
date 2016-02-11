package com.mlink.iwm.entity.factory;

public enum ExpiryType implements IEnumerated{

	ET1("End Of Week"),
	ET2("End Of Month"),
	ET3("End Of Year"),
	ET4("Relative Number Of Days"),
	ET5("Never Expires");

	
	private final String label;
	
	private ExpiryType(String l){
		this.label=l;
		
	}

	public String getLabel(){
		return label;
	}
	
	
}
