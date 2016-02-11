package org.mlink.agents.scheduler.model;


import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates the input data for the scheduling functions in the AssignAndSchedule interface
 * @author kenneth m. levine
 * @version 1.0
 *
 */
public class SchedulingInputData {
	MetaData md;
	List<Job> jobs=new ArrayList<Job>();
	List<Asset> assets=new ArrayList<Asset>();
	List<NonAssetResource> nars=new ArrayList<NonAssetResource>();
	
	/**
	 * This method is used to include a job in the scheduling input data
	 * @param jb The job to be added
	 */
	public void addJob(Job jb) {
		jobs.add(jb);
	}
		
	/**
	 * This method is used to include an asset in the scheduling input data
	 * @param aa the asset to be added
	 */
	public void addAsset(Asset aa) {
		assets.add(aa);
	}
	
	/**
	 * This method is used to include a non asset resource in the scheduling input data
	 * @param nar The non asset resource to be added
	 */
	public void addNAR(NonAssetResource nar) {
		nars.add(nar);
	}
	
	/**
	 * This method is used to set the metadata in the scheduling input data
	 * @param metaData The metadata to be set
	 */
	public void setMetaData(MetaData metaData) {
		md = metaData;
	}
	
	/**
	 * This method obtains the metadata in the scheduling input data
	 * @return metadata  The metadata for the scheduling run
	 */
	public MetaData getMetaData() {
		return md;
	}
	
	/**
	 * This method is used to set the jobs in the scheduling input data
	 * @param jb The jobs to be included
	 */
	public void setJobs(List<Job> jbs) {
		jobs = jbs;
	}
	
	/**
	 * This method obtains the jobs in the scheduling input data
	 * @return jobs The jobs in the scheduling input data
	 */
	public List<Job> getJobs() {return jobs;}

	/**
	 * This method is used to set the assets in the scheduling input data
	 * @param as The assets to be included
	 */

	public void setAssets(List<Asset> as) {
		assets = as;
	}

	/**
	 * This method obtains the assets in the scheduling input data
	 * @return assets The assets in the scheduling input data
	 */
	public List<Asset> getAssets() {return assets;}	
	
	/**
	 * This method is used to set the non asset resources in the scheduling input data
	 * @param nrs The non asset resouces to be included
	 */
	public void setNARs(List<NonAssetResource> nrs) {
		nars = nrs;
	}
	
	/**
	 * This method obtains the non asset resources in the scheduling input data
	 * @return nars The non asset resources in the scheduling input data
	 */
	public List<NonAssetResource> getNARs() {return nars;}
}
