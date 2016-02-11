package com.mlink.iwm.entity.factory;

public enum JobType implements IEnumerated{

	JT1("CM"),
	JT2("PMCS");


	
	private final String label;
	
	private JobType(String l){
		this.label=l;
		
	}
	
	
	public String getLabel(){
		return label;
	}
	
	
}
