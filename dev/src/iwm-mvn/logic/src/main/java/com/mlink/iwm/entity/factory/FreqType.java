package com.mlink.iwm.entity.factory;

public enum FreqType implements IEnumerated{

	FT1("Annually"),
	FT2("BiMonthly"),
	FT3("BiWeekly"),
	FT4("Daily"),
	FT5("Monthly"),
	FT6("Quarlerly"),
	FT7("SemiAnnually"),
	FT8("Weekly");
	     
	
	private final String label;
	
	private FreqType(String l){
		this.label=l;
		
	}
	
	
	public String getLabel(){
		return label;
	}
	
	
}
