package com.mlink.iwm.entity.factory;

public enum JobStatus implements IEnumerated{
	
	JS1("AWTG INS"),
	JS2("FINL INS"),
	JS3("INS PRGS"),
	JS4("RPR COMP"),
	JS5("JOB CLOS"),
	JS6("RPR PRGS"),
	JS7("SHT PART"),
	JS8("SHT TEST"),
	JS9("SHT SPAC"),
	JS10("SHT TECH"),
	JS11("SHT FUND"),
	JS12("UNIT RCL"),
	JS13("WIR SUB"),
	JS14("EVC HECH"),
	JS15("EVC WASH"),
	JS16("INS COMP");

	
	private final String label;
	
	private JobStatus(String l){
		this.label=l;
		
	}
	
	
	public String getLabel(){
		return label;
	}
	
	
}
