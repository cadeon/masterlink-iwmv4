package com.mlink.iwm.entity.factory;

public enum RequestType implements IEnumerated{

	RS1("reqType1"),
	RS2("reqType2"),
	RS3("reqType3");

	
	private final String label;
	
	private RequestType(String l){
		this.label=l;
		
	}
	
	
	public String getLabel(){
		return label;
	}
	
	
}
