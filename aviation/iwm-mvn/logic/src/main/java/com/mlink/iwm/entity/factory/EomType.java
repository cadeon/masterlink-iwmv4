package com.mlink.iwm.entity.factory;

public enum EomType implements com.mlink.iwm.entity.factory.IEnumerated{

	E1("EOM 1"),
	E2("EOM 2"),
	E3("EOM 3"),
	E4("EOM 4"),
	E5("EOM 5");
	
	private final String label;
	
	private EomType(String l){
		this.label=l;
		
	}
	
	
	public String getLabel(){
		return label;
	}
	
	
}
