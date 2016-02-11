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
	
	public static Priority MAX_PRIORITY = P13;

	
	private final String label;
	
	private Priority(String l){
		this.label=l;
		
	}
	public Integer getIntLabel(){
		return Integer.parseInt(label);
	}
	
	public String getLabel(){
		return label;
	}
	
	public static Priority getEnum(int i) {
		switch (i) {																			
		case 1: return P1;
		case 2: return P2;
		case 3: return P3;
		case 4: return P4;
		case 5: return P5;
		case 6: return P6;
		case 7: return P7;
		case 8: return P8;
		case 9: return P9;
		case 10: return P10;
		case 11: return P11;
		case 12: return P12;
		case 13: return P13;
		default: throw new IndexOutOfBoundsException("No such priority: "+ i);
		}
	}
	
}
