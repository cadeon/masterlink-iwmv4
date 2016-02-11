package com.mlink.iwm.scheduler.model;

import java.util.ArrayList;
import java.util.List;

public class Job {
	String description="";
	Long id;
	Long managedAssetId;
	String locator;
	Integer status;
	Integer estimatedTime;
	Integer recordedTime;
	Integer priority;
	Long organizationId;
	Long startDateTime;
	Long endDateTime;
	Long earliestStart;
	Long latestEnd;
	List<AssetAvailability>maintainabilities;
	List<AssetResource>assetResources;
	List<NonAssetResource>nonAssetResources;

	public String getDescription() { return description;}
	public Long getId() { return id;}
	public Long getManagedAssetId() { return managedAssetId;}
	public String getLocator() { return locator;}
	public Integer getStatus() { return status;}
	public Integer getEstimatedTime() { return estimatedTime;}
	public Integer getRecordedTime() { return recordedTime;}
	public Integer getPriority() { return priority;}
	public Long getOrganizationId() { return organizationId;}
	public Long getStartDateTime() { return startDateTime;}
	public Long getEndDateTime() { return endDateTime;}
	public Long getEarliestStart() { return earliestStart;}
	public Long getLatestEnd() { return latestEnd;}
	public List<AssetAvailability> getMaintainabilities() {return maintainabilities;}
	public List<AssetResource> getAssetResources() {return assetResources;}
	public List<NonAssetResource> getNonAssetResources() {return nonAssetResources;}
	

	public void setDescription(String s) { description=s;}
	public void setId(Long l) { id=l;}
	public void setManagedAssetId(Long l) { managedAssetId=l;}
	public void setLocator(String s) { locator=s;}
	public void setStatus(Integer i) { status=i;}
	public void setEstimatedTime(Integer i) { estimatedTime=i;}
	public void setRecordedTime(Integer i) { recordedTime=i;}
	public void setPriority(Integer i) { priority=i;}
	public void setOrganizationId(Long l) { organizationId=l;}
	public void setStartDateTime(Long l) { startDateTime=l;}
	public void setEndDateTime(Long l) { endDateTime=l;}
	public void setEarliestStart(Long l) { earliestStart=l;}
	public void setLatestEnd(Long l) { latestEnd=l;}

	public void addMaintainability(AssetAvailability aa) {
		if (maintainabilities==null) maintainabilities=new ArrayList<AssetAvailability>();
		maintainabilities.add(aa);
	}
	public void addAssetResource(AssetResource ar) {
		if (assetResources==null) assetResources=new ArrayList<AssetResource>();
		assetResources.add(ar);
	}
	public void addNonAssetResource(NonAssetResource nar) {
		if (nonAssetResources==null) nonAssetResources=new ArrayList<NonAssetResource>();
		nonAssetResources.add(nar);
	}
}
