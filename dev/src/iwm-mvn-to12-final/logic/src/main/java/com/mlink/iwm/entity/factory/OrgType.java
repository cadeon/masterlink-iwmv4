package com.mlink.iwm.entity.factory;

public enum OrgType implements IEnumerated{

	O1("OrgType1"),
	O2("OrgType2"),
	O3("OrgType3");

	
	private final String label;
	
	private OrgType(String l){
		this.label=l;
		
	}
	
	
	public String getLabel(){
		return label;
	}
	
	
}
