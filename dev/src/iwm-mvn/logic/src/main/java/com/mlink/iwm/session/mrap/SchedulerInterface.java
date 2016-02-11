package com.mlink.iwm.session.mrap;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetNotAvail;
import com.mlink.iwm.entity.factory.AssetKind;
import com.mlink.iwm.entity.factory.JobStatus;
import com.mlink.iwm.entity.factory.Priority;
import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.entity.job.JobTask;
import com.mlink.iwm.scheduler.model.AssetAvailability;
import com.mlink.iwm.scheduler.model.AssetResource;
import com.mlink.iwm.scheduler.model.AssetTimeShare;
import com.mlink.iwm.scheduler.model.MetaData;
import com.mlink.iwm.scheduler.model.Proficiency;
import com.mlink.iwm.scheduler.model.SchedulingInputData;
import com.mlink.iwm.scheduler.model.SchedulingOutputData;
import com.mlink.iwm.session.asset.AssetHome;
import com.mlink.iwm.session.asset.AssetList;
import com.mlink.iwm.session.asset.AssetTimeShareHome;
import com.mlink.iwm.session.asset.AssetTimeShareList;
import com.mlink.iwm.session.job.JobHome;
import com.mlink.iwm.session.job.JobList;


/**
 * @author chrisduffy
 *
 *	Primarily responsible for populating and de-populating scheduling data
 *  
 *   
 */
@Name("schedulerInterface")
@Scope(ScopeType.SESSION)
public class SchedulerInterface implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final long INC_DAY = 24 * 60 * 60 * 1000; // one day in milliseconds
	
	private static final int PROFICIENCY_IMPORTANCE = 100;
	private static final int PROFICIENCY_LEVEL      = 1;
	private static final int PROFICIENCY_MAX_VALUE  = 1;
	
	@In(create=true, required=false)
	private JobList jobList;

	@In(value="assetList", create=true, required=false)
	private AssetList aels;

	@In(value="assetTimeShareList", create=true, required=false)
	private AssetTimeShareList atsels;
	

	@In(value="jobHome", create=true, required=false)
	private JobHome jes;
	

	@In(value="assetHome", create=true, required=false)
	private AssetHome aes;

	@In(value="assetTimeShareHome", create=true, required=false)
	private AssetTimeShareHome atses;
	
	@Logger
	private static Log log;
	
	
	/*
	 * Populates the sid. DOES NOT CREATE THE METADATA
	 * that happens in scheduler invocation. 
	 */
	public SchedulingInputData buildSid(MetaData md){
		SchedulingInputData sid = new SchedulingInputData();
		sid.setMetaData(md);
		
		//Get jobs from Model
		//ArrayList<Job> modelJobs = (ArrayList<Job>) jobList.findByStatus("RPR PRGS"); //For scheduler purposes, this is the only status
		if (jobList==null) {
			log.info("JobList instance not injected. Reinstantiate job list.");
			jobList = (JobList) Component.getInstance(JobList.class);;
		}
		List<Job> modelJobs = new ArrayList<Job>();
		for (Job j : jobList.getAll()) {
			if (JobStatus.JS6.equals(j.getStatus())) { // job ready for scheduling
				modelJobs.add(j);
			}
		}
		System.err.println("# jobs: "+ modelJobs.size());
		//turn them into schedulerJobs
		List<com.mlink.iwm.scheduler.model.Job> schedulerJobs = translateJobs(modelJobs, md);
		//add to sid
		for (com.mlink.iwm.scheduler.model.Job j : schedulerJobs ){
			sid.addJob(j);
		}
		
		//On to Assets!
		//For the demo, we only need workers. This will need to be more advanced in the future.
		if (aels==null) aels = new AssetList();
		List<Asset> modelAssets = new ArrayList<Asset>();
		for (Asset a : aels.getAll()) {
			if (AssetKind.WO.equals(a.getAssetKind())) { // got a worker
				modelAssets.add(a);
			}
		}
		System.err.println("SchedulerInterface: buildSid: # Assets: "+ modelAssets.size());
		//Therefore, we're only going to translate worker assets. 	
		List<com.mlink.iwm.scheduler.model.Asset> schedulerAssets = translateAssets(modelAssets, md);
		
		//add to sid
		for (com.mlink.iwm.scheduler.model.Asset a : schedulerAssets) {
			sid.addAsset(a);
		}
			
		log.info("=================================");
		log.info("Input data:::");
		log.info(sid);
		log.info("=================================");
		return sid;
	}
	

	private List<com.mlink.iwm.scheduler.model.Asset> translateAssets(
			List<Asset> modelAssets, MetaData md) {
		// Takes modelAssets, makes schedulerAssets
		// Only translates worker assets- for the demo 
		ArrayList<com.mlink.iwm.scheduler.model.Asset> schedulerAssets = 
			new ArrayList<com.mlink.iwm.scheduler.model.Asset>();
		
		for (Asset modelAsset : modelAssets){
			com.mlink.iwm.scheduler.model.Asset schedulerAsset = new com.mlink.iwm.scheduler.model.Asset();
			if (modelAsset.getIsActive() && AssetKind.WO.equals(modelAsset.getAssetKind())) { //no need to give it to the scheduler if it's not active
				//TODO: Increase scope for production
					
				//the basics
				log.info("Method translateAsset: got asset with id "+ modelAsset.getId());
				schedulerAsset.setId(modelAsset.getId());
				schedulerAsset.setClassificationId(modelAsset.getAssetTemplate().getId()); //Class id is template id
				schedulerAsset.setType(modelAsset.getAssetType().getCode());
				schedulerAsset.setOrganizationId(modelAsset.getOrganization().getId());
				//TODO: missing from model
				schedulerAsset.setTrackTravel(1);
				schedulerAsset.setCalculateUtility(1);
				
				//Asset Availabilities
				List<AssetAvailability> aas = getAvailabilities(modelAsset, md.getPeriodStart(), md.getPeriodEnd());
				if (aas.size()==0) continue; // no availabilities during this period, skip
				for (AssetAvailability aa : aas) {
					schedulerAsset.addAvailability(aa);
				}
				//Asset Attributes
				for (com.mlink.iwm.entity.asset.AssetAttribute maattr: modelAsset.getAssetAttributes()){
					com.mlink.iwm.scheduler.model.AssetAttribute saattr = new com.mlink.iwm.scheduler.model.AssetAttribute();
					
					//Check to see if this is a numerical aa. if not, we don't care.
					boolean isInt = true; //assume success
					Integer value = 0;
					try {
						value = Integer.parseInt(maattr.getValue());
					} catch(Exception e)	{
						isInt = false;
					};
					
					if (isInt){
						saattr.setId(maattr.getId());
						saattr.setKey(maattr.getName());
						saattr.setValue(value);
						saattr.setFunction(0); // Enum { ==, <, >, <=, >=, !=} - defaulting to == always, TODO missing from model
						schedulerAsset.addAssetAttributes(saattr);
					}
					
				} //End of AssetAttribute adds
				
				//Proficiencies!
				//We're going to get assets one level down from this one
				//push type PR to schedulerAsset.proficiencies
				List<Asset> modelChildren = modelAsset.getChildAssets(); //"Model Children" ha.
				
				for (Asset modelChild:modelChildren){
					if (modelChild.getAssetKind().getLabel() == "Proficiency"){
						Proficiency schedulerPr = new com.mlink.iwm.scheduler.model.Proficiency();
						schedulerPr.setClassificationId(modelChild.getAssetTemplate().getId());
						schedulerPr.setId(modelChild.getId());
						schedulerPr.setFunction(4); // Enum { ==, <, >, <=, >=, !=} - defaulting to >= (typical behavior for skill)
						schedulerPr.setLevel(1); // For the demo, everyone is level 1. TODO: sheesh, how do we store this in the model?
						schedulerPr.setImportance(1); //I have no idea what this is, but it's here now.
						schedulerPr.setMaxValue(md.getMaxPriority()); //Max priority is on the metadata
						//Add it!
						schedulerAsset.addProficiency(schedulerPr);
					}
				}//End proficiencies
				//Add this one!
				schedulerAssets.add(schedulerAsset);
			}//End isActive check
			
		}//End Asset loop
		//All done!
		return schedulerAssets;
	}


	private List<AssetAvailability> getAvailabilities(Asset asset, Long start, Long end) {
		List<AssetAvailability> aas = new ArrayList<AssetAvailability>();
		List<AssetNotAvail> anas = asset.getAssetNotAvails();
		if (anas.size()==0) {
			aas.add(createAvailability(start,end));
			return aas;
		}
		Collections.sort(anas);
		for (AssetNotAvail ana : anas) {
			if (ana.getStartDate()==null || ana.getEndDate()==null) continue; //ignore bad data
			if (ana.getStartDate().getTime() <= start) { // leave time starts on or before scheduling period start
				if (ana.getEndDate().getTime() < end) { // leave time ends before end of scheduling period
					start = start + ana.getEndDate().getTime()+INC_DAY; // reset scheduling period start to after end of leave time
				} else { // end date on or after scheduling period end, return empty list
					aas.clear();
					return aas;
				}
			} else { // start date after scheduling period start
				if (ana.getStartDate().getTime() <= end) { // leave time starts during scheduling period
					// create AssetAvailability from scheduling period start to just before start of leave time
					aas.add(createAvailability(start,ana.getStartDate().getTime()-INC_DAY));
				} else { // leave time starts after scheduling period
					     // create AssetAvailability for the duration of the scheduling period
					aas.add(createAvailability(start,end));
				}
				if (ana.getEndDate().getTime() >= end ) { // no other availabilities, return list
					return aas;
				} else { // availabilities lie on either side of leave time
					// move scheduling period start to after end of leave time and continue
					start = ana.getEndDate().getTime()+INC_DAY;
				}
			}
		}
		return aas;
	}
	private AssetAvailability createAvailability(Long start, Long end) {
		AssetAvailability aa = new AssetAvailability();
		aa.setEndDateTime(end);
		aa.setStartDateTime(start);
		return aa;
	}
	private ArrayList<com.mlink.iwm.scheduler.model.Job> translateJobs(List<Job> modelJobs, MetaData md) {
		// Takes modelJobs, makes schedulerJobs
		
		ArrayList<com.mlink.iwm.scheduler.model.Job> schedulerJobs = new ArrayList<com.mlink.iwm.scheduler.model.Job>();
		
		for (int ji=0; ji<modelJobs.size(); ji++){
			com.mlink.iwm.scheduler.model.Job schedulerJob = new com.mlink.iwm.scheduler.model.Job();
			Job modelJob = modelJobs.get(ji);
			
			schedulerJob.setId(modelJob.getId());
			
			schedulerJob.setLocator("shop"); //FIXME: Blasphemy.
			
			schedulerJob.setManagedAssetId(
					//06/29/11 Nadia-bypass taskInstance 
					modelJob.getMaintainedAsset().getId());
			
			schedulerJob.setStatus(1); //"RPR PGRS" - all jobs are in this status thanks to the get.
			
			schedulerJob.setEstimatedTime(modelJob.getEstTime());
			
			//Get recorded time. Requires getting actuals from JobTasks.
			List<JobTask> modelJTs = modelJob.getJobTasks();
			Integer recordedTime=0;
			Iterator<JobTask> mjti = modelJTs.iterator();
			while(mjti.hasNext()){
				recordedTime=recordedTime + mjti.next().getActualTime();
			}
			
			schedulerJob.setRecordedTime(recordedTime); 
			
			//TODO: Priority for the demo is inverse of the label. This will be a ref table later in life.
			String modelPri = modelJob.getPriority().getLabel();
			Integer schedulerPri = Integer.parseInt(modelPri);
			
			// convert model max priority to scheduler max
			schedulerPri = Priority.MAX_PRIORITY.getIntLabel() - schedulerPri + 1;
			
			schedulerJob.setPriority(schedulerPri);
			
			schedulerJob.setOrganizationId(modelJob.getOrganization().getId());
			
			//Date Wrangling
			schedulerJob.setEarliestStart(modelJob.getEarliestStart().getTime()); 
			schedulerJob.setStartDateTime(modelJob.getEarliestStart().getTime());
			
			//Latest *END* is *DIFFERENT* than Latest *START*
			Long estMills = new Long(schedulerJob.getEstimatedTime()); 
			estMills=estMills * 60000L; //hours to Milliseconds
			
			schedulerJob.setLatestEnd(md.getPeriodEnd() + estMills); //This allows jobs to go past the end of the scheduling scope, which is ok if it's a huge job
			schedulerJob.setEndDateTime(modelJob.getDueDate().getTime());
			
			//Listup Maintainability
			//Assume that every asset to be maintained is available over the scope of this run
			//TODO: Does not reflect reality
			AssetAvailability aa = new AssetAvailability();
			aa.setStartDateTime(md.getPeriodStart());
			aa.setEndDateTime(md.getPeriodEnd());
			
			schedulerJob.addMaintainability(aa);
			
			//Get AssetResources and add them
			//TODO: DOES NOT ACCOUNT FOR MULTIPLE TASKS ON JOB (which is probably ok considering incoming changes)
			List<com.mlink.iwm.entity.res.AssetResource> modelArs = modelJob.getAssetResources();  
			for (com.mlink.iwm.entity.res.AssetResource modelAr : modelArs) {
				log.info("Method translateJobs: Job "+ schedulerJob.getId() +" has "+ modelAr.getQuantity() +
						" asset resources for qualification '"+modelAr.getAssetTemplate().getName()+"'");
				for (int i=0;i<modelAr.getQuantity();i++) {
					AssetResource schedulerAr = new AssetResource();
					//schedulerAr.setAssignedAssetId(); //TODO: Assignment doesn't happen this way... doesn't exist in modelAr
					schedulerAr.setClassificationId(2L); // Worker Template Id
					// FIXME: Can't have the same asset resource id, yet currently
					// FIXME: both are represented by the quantity attribute on a 
					// FIXME: single asset resource
					schedulerAr.setId(modelAr.getId()+(i*1000)); 
					schedulerAr.setJob(schedulerJob);
					Proficiency p = new Proficiency();
					p.setLevel(PROFICIENCY_LEVEL);
					p.setMaxValue(PROFICIENCY_MAX_VALUE);
					p.setImportance(PROFICIENCY_IMPORTANCE);
					p.setClassificationId(modelAr.getAssetTemplate().getId());
					p.setFunction(4); 
					schedulerAr.addProficiency(p);
					schedulerJob.addAssetResource(schedulerAr);
					if (modelAr.getStickyAsset()!= null) 
						schedulerAr.setRequiredAssetId(modelAr.getStickyAsset().getId());
				}
			}
			/* Commenting NARs as we don't need them for the demo & I don't want them blocking assignments
			 
			//Get NonAssetResources and add them
			//TODO: DOES NOT ACCOUNT FOR MULTIPLE TASKS ON JOB (which is probably ok considering incoming changes)
			List<com.mlink.iwm.entity.res.NonAssetResource> modelNars = modelJob.getTaskInstance().getTask().getNonAssetResources();  
			
			Iterator<com.mlink.iwm.entity.res.NonAssetResource> nari = modelNars.iterator();
			while (nari.hasNext()){
				com.mlink.iwm.entity.res.NonAssetResource modelNar = nari.next();
				NonAssetResource schedulerNar = new NonAssetResource();
				schedulerNar.setQuantityOnHand(modelNar.getQuantity());
				schedulerNar.setStockNumber(modelNar.getId()); //TODO: I think id works for this?
				schedulerNar.setType(1); //TODO: Not correct due to enum not ref
				
			}
			*/
			//Add this job!
			schedulerJobs.add(schedulerJob);
			
		} //Done with jobs
		
		return schedulerJobs;
	}


	/*
	 * Persists scheduling results to the model
	 */
	public void persistSod(SchedulingOutputData sod, Long scopeStart){
		
		log.info("Utility from this run: "+ sod.getUtility());
		
		//For the demo, we blow away existing ATSs
		if (atsels==null) atsels = new AssetTimeShareList();
		atsels.deleteAll();
		
		//TODO: Existing ATS merge?
		//TODO: Write NARs
		
		 //Write new ATS
		List<com.mlink.iwm.scheduler.model.AssetTimeShare> schedulerATSs = sod.getAssetTimeShares();
		log.info("AssetTimeShares: "+ schedulerATSs.size());
		for (AssetTimeShare schedulerATS : schedulerATSs) {
			if (atses==null) {
				log.info("persistSod: AssetTimeShareHome not injected. Reinit home");
				atses = (AssetTimeShareHome)Component.getInstance(AssetTimeShareHome.class);
			}
			atses.clearInstance();
			com.mlink.iwm.entity.asset.AssetTimeShare modelATS = atses.getInstance();
			
			modelATS.setAssigned(true);
			
			if (aes==null) aes = (AssetHome)Component.getInstance(AssetHome.class);
			if (jes==null) jes = (JobHome)Component.getInstance(JobHome.class);
			modelATS.setAsset((Asset) aes.find(schedulerATS.getAssetID()));
			modelATS.setJobAllocatedTo((Job) jes.find(schedulerATS.getJobID()));
			
			//modelATS.setLastChange(new Date(System.currentTimeMillis())); //sql date, not util date
			
			//Scheduler returns time in the units of the estimated time. In this case, that means hours.
			//Scheduler also returns time shares referenced to the scheduling scope, not the actual datetime
			
			Long startTime = schedulerATS.getStartTime();
			Long endTime = schedulerATS.getEndTime();
			
			//Convert to hours.
			startTime = startTime * 3600000; //hours to millis, x3.6M
			endTime = endTime * 3600000;
			
			//These times are relative to the scheduler scope. Make them relative to real time.
			startTime = scopeStart + startTime;
			endTime = scopeStart + endTime;
			
			modelATS.setStartDate(new Date(startTime));
			modelATS.setEndDate(new Date(endTime));
			
			if (schedulerATS.getResourceID() != null){
				//being used as a resource
				modelATS.setAsResource(true);
			} else {
				modelATS.setAsResource(false);
			}
			
			//Since this is the demo, ATS is empty. I can assign ids here safely.
			
			//All done, save it
			if (atses==null) {
				log.info("persistSod: AssetTimeShareHome instance is null, cannot call saveOrUpdate. Reinstantiating. ");
				atses = (AssetTimeShareHome)Component.getInstance(AssetTimeShareHome.class);
			}
			atses.saveOrUpdate(modelATS);
			
		}
	}
	
}
