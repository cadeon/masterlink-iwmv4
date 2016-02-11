package com.mlink.iwm.scheduler.model;


import java.util.ArrayList;
import java.util.List;

import com.mlink.iwm.scheduler.model.NonAssetResource;

public class SchedulingOutputData {
	List<AssetTimeShare> shares = new ArrayList<AssetTimeShare>();
	List<NonAssetResource> nars =new ArrayList<NonAssetResource>();
	List<JobNAR> jobNars =new ArrayList<JobNAR>();
	List<ProcessingError> errs =new ArrayList<ProcessingError>();;
	
	public void addAssetTimeShare(AssetTimeShare ats) {
		shares.add(ats);
	}
	
	public void addNonAssetResource(NonAssetResource nar) {
		nars.add(nar);
	}
	
	public void addJobNAR(JobNAR nar) {
		jobNars.add(nar);
	}
	
	public void addErrror(ProcessingError err) {
		errs.add(err);
	}
	
	public List<AssetTimeShare> getAssetTimeShares() {return shares;}
	
	public List<NonAssetResource> getNonAssetResources() {return nars;}
	
	public List<JobNAR> getjobNARs() {return jobNars;}
	
	public List<ProcessingError> getProcessingErrors() {return errs;}
	

}

