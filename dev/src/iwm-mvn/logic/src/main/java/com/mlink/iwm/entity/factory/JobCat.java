package com.mlink.iwm.entity.factory;

import com.mlink.iwm.entity.factory.IEnumerated;

public enum JobCat implements IEnumerated{
	CAT1("C"),
	CAT2("D"),
	CAT3("F"),
	CAT4("H"),
	CAT5("M"),
	CAT6("N"),
	CAT7("O"),
	CAT8("P"),
	CAT9("S"),
	CAT10("X"),
	CAT11("K");


	
	private final String label;
	
	private JobCat(String l){
		this.label=l;
	
		
	}

	public String getLabel(){
		return label;
	}
	
	
}
