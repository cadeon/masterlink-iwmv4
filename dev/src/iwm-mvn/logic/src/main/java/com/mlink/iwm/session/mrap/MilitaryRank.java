package com.mlink.iwm.session.mrap;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MilitaryRank {

	UNKN("Unknown", "Unknown"),
	PVT("Pvt", "Private"),
	PFC("PFC", "Private First Class"),
	LCPL("LCpl", "Lance Corporal"),
	CPL("Cpl", "Corporal"),
	SGT("Sgt", "Sergeant"),
	SSGT("SSgt", "Staff Sergeant"),
	GYSGT("GySgt", "Gunnery Sergeant"),
	W1("W1", "Warrant Officer 1"),
	W2("W2", "Chief Warrant Officer 2"),
	W3("W3", "Chief Warrant Officer 3"),
	W4("W4", "Chief Warrant Officer 4"),
	W5("W5", "Chief Warrant Officer 5");
	
	private static final Map<String, MilitaryRank> lookup = new HashMap<String, MilitaryRank>();
	
	static {
		for (MilitaryRank rank : EnumSet.allOf(MilitaryRank.class)) {
			lookup.put(rank.getAbbrv(), rank);
		}
	}
	
	private String abbrv;
	private String title;
	
	private MilitaryRank(String abbrv, String title) {
		this.abbrv = abbrv;
		this.title = title;
	}
	
	public String getAbbrv() {
		return this.abbrv;
	}
	
	public String getTitle() {
		return this.title;
	}

	public static MilitaryRank get(String abbrv) {
		MilitaryRank rank = lookup.get(abbrv);
		if (rank != null) {
			return rank; 
		}
		return UNKN;
	}
}
