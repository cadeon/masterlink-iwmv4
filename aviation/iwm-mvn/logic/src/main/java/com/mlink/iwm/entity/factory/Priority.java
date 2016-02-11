package com.mlink.iwm.entity.factory;

//job priority
public enum Priority implements IEnumerated{

	P1("01"),
	P2("02"),
	P3("03"),
	P4("04"),
	P5("05"),
	P6("06"),
	P7("07"),
	P8("08"),
	P9("09"),
	P10("10"),
	P11("11"),
	P12("12"),
	P13("13");

	
	private final String label;
	
	private Priority(String l){
		this.label=l;
		
	}
	
	
	public String getLabel(){
		return label;
	}
	
	
}
