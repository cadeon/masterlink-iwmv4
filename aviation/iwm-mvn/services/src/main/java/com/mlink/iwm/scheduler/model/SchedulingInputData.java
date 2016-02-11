package com.mlink.iwm.scheduler.model;

import com.mlink.iwm.scheduler.model.*;

import java.util.ArrayList;
import java.util.List;

public class SchedulingInputData {
	MetaData md;
	List<Job> jobs;
	List<Asset> assets;
	List<NonAssetResource> nars;
	
	public void addJob(Job jb) {
		if (jobs==null) jobs=new ArrayList<Job>();
		jobs.add(jb);
	}
	
	public void addAsset(Asset aa) {
		if (assets==null) assets=new ArrayList<Asset>();
		assets.add(aa);
	}
	
	public void addNAR(NonAssetResource nar) {
		if (nars==null) nars=new ArrayList<NonAssetResource>();
		nars.add(nar);
	}
	
	public void setMetaData(MetaData metaData) {
		md = metaData;
	}
	
	public MetaData getMetaData() {
		return md;
	}
	
	public List<Job> getJobs() {return jobs;}

	public List<Asset> getAssets() {return assets;}	
	
	public List<NonAssetResource> getNARs() {return nars;}
}
