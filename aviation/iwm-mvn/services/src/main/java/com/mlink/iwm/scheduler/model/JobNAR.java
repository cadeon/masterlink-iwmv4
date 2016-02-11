package com.mlink.iwm.scheduler.model;

import com.mlink.iwm.scheduler.model.NonAssetResource;

public class JobNAR extends NonAssetResource {
	Long jobID;
	
	public void setJobID(Long l) { jobID=l;}

	public Long getJobID() { return jobID;}
}
