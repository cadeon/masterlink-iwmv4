package com.mlink.iwm.entity.factory;

public enum TaskType implements IEnumerated{

	TT1("Corrective"),
	TT2("Preventive");


	
	private final String label;
	
	private TaskType(String l){
		this.label=l;
		
	}
	
	
	public String getLabel(){
		return label;
	}
	
	
}
