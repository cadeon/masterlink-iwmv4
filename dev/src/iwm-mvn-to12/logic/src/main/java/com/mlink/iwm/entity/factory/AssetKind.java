package com.mlink.iwm.entity.factory;

public enum AssetKind implements IEnumerated{

	PR("Proficiency"),
	WO("Worker"),
	LO("Location"),
	//RO("Role"),
	PA("Pure Asset");
	
	private final String label;
	
	private AssetKind(String l){
		this.label=l;
		
	}
	

	public String getLabel(){
		return label;
	}
	
	
}
