package com.mlink.iwm.scheduler.model;

import java.util.ArrayList;
import java.util.List;


public class Asset {
	Long id;
	Long classificationId;
	Integer type;
	Long organizationId;
	Integer trackTravel;
	Integer calculateUtility;
	List<AssetAvailability>availabilities;
	List<AssetAttribute> attributes;
	List<Proficiency> proficiencies;
	
	public Long getId() { return id;}
	public Long getClassificationId() { return classificationId;}
	public Integer getType() { return type;}
	public Long getOrganizationId() { return organizationId;}
	public Integer getTrackTravel() { return trackTravel;}
	public Integer getCalculateUtility() { return calculateUtility;}
	public List<AssetAvailability> getAvailabilities() {return availabilities;}
	public List<AssetAttribute> getAssetAttributes() {return attributes;}
	public List<Proficiency> getProficiencies() {return proficiencies;}
	
	public void setId(Long l) { id=l;}
	public void setClassificationId(Long l) { classificationId=l;}
	public void setType(Integer i) { type=i;}
	public void setOrganizationId(Long l) { organizationId=l;}
	public void setTrackTravel(Integer i) { trackTravel=i;}
	public void setCalculateUtility(Integer i) { calculateUtility=i;}

	public void addAvailability(AssetAvailability aa) {
		if (availabilities==null) availabilities=new ArrayList<AssetAvailability>();
		availabilities.add(aa);
	}
	public void addAssetAttributes(AssetAttribute aa) {
		if (attributes==null) attributes=new ArrayList<AssetAttribute>();
		attributes.add(aa);
	}
	public void addProficiency(Proficiency p) {
		if (proficiencies==null) proficiencies=new ArrayList<Proficiency>();
		proficiencies.add(p);
	}	
}
