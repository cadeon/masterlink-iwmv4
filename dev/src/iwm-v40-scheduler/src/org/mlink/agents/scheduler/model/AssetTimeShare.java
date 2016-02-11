package org.mlink.agents.scheduler.model;

public class AssetTimeShare {
	Long assetId;
	Long assetResourceId;
	Long jobId;
	Long startDateTime;
	Long endDateTime;
	
	public Long getResourceID() {
		return assetResourceId;
	}
	
	public Long getAssetID() {
		return assetId;
	}
	
	public Long getJobID() {
		return jobId;
	}
	
	public Long getStartTime() {
		return startDateTime;
	}
	
	public Long getEndTime() {
		return endDateTime;
	}	
	
	public void setAssetID(Long id) {
		assetId = id;
	}
	
	public void setJobID(Long id) {
		jobId = id;
	}
	
	public void setResourceID(Long id) {
		assetResourceId = id;
	}
	
	public void setStartTime(Long tim) {
		startDateTime = tim;
	}
	
	
	public void setEndTime(Long tim) {
		endDateTime = tim;
	}
}
