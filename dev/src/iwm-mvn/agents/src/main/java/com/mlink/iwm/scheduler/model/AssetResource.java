package com.mlink.iwm.scheduler.model;

import java.util.ArrayList;
import java.util.List;

public class AssetResource {
	Long id;
	Job  job;
	Long requiredAssetId;
	Long assignedAssetId;
	Long classificationId;
	List<AssetAttribute> attributes;
	List<Proficiency> proficiencies;
	
	public Long getId() { return id;}
	public Job getJob() { return job;}
	public Long getRequiredAssetId() { return requiredAssetId;}
	public Long getAssignedAssetId() { return assignedAssetId;}
	public Long getClassificationId() { return classificationId;}
	public List<AssetAttribute> getAssetAttributes() {return attributes;}
	public List<Proficiency> getProficiencies() {return proficiencies;}
	
	
	public void setId(Long l) { id=l;}
	public void setJob(Job j) { job=j;}
	public void setRequiredAssetId(Long l) { requiredAssetId=l;}
	public void setAssignedAssetId(Long l) { assignedAssetId=l;}
	public void setClassificationId(Long l) { classificationId=l;}
	
	public void addAssetAttributes(AssetAttribute aa) {
		if (attributes==null) attributes=new ArrayList<AssetAttribute>();
		attributes.add(aa);
	}
	public void addProficiency(Proficiency p) {
		if (proficiencies==null) proficiencies=new ArrayList<Proficiency>();
		proficiencies.add(p);
	}	
}
