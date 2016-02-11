package com.mlink.iwm.scheduler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mlink.iwm.scheduler.SidValidator;

import com.mlink.iwm.scheduler.model.Asset;
import com.mlink.iwm.scheduler.model.AssetAttribute;
import com.mlink.iwm.scheduler.model.AssetAvailability;
import com.mlink.iwm.scheduler.model.AssetResource;
import com.mlink.iwm.scheduler.model.Job;
import com.mlink.iwm.scheduler.model.MetaData;
import com.mlink.iwm.scheduler.model.NonAssetResource;
import com.mlink.iwm.scheduler.model.ProcessingError;
import com.mlink.iwm.scheduler.model.Proficiency;
import com.mlink.iwm.scheduler.model.SchedulingInputData;
	
class HardCodedSidValidator implements SidValidator {
	static final int FATAL_ERRROR  = 3;
	static final int SEVERE_ERRROR = 3;
	
	int jobIdCount;
	int jobAssetIdCount;
	int assetIdCount;
	int narNSNCount;
	
	Integer theMaximumPriority = 0;
	
	SchedulingInputData sid;
	
	Set<Long> jobIdSet;
	Set<Long> jobAssetIdSet;
	Set<Long> assetIdSet;			
	Set<Long> narNSNSet;	
	HashMap<Long,Integer> maxLevels = new HashMap<Long,Integer>(); 			// maps proficiency classification iD to maximum level

	
	@Override			
	public boolean validate(SchedulingInputData sid, List<ProcessingError> errs) {
		int oldSize = errs.size();

		jobIdCount      = 0;
		jobAssetIdCount = 0;
		assetIdCount    = 0;
		narNSNCount     = 0;

		jobIdSet      = new HashSet<Long>();
		jobAssetIdSet = new HashSet<Long>();
		assetIdSet    = new HashSet<Long>();			
		narNSNSet     = new HashSet<Long>();	
		
		this.sid = sid;

		if (sid.getMetaData() == null)								addFatalErr("MetaData must be non-null", 10001, errs);
		else validateMetadata(sid.getMetaData(), errs);
		
		if(sid.getJobs() == null || sid.getJobs().size() == 0) 		addFatalErr("Must be at least one job", 10002, errs);
		else for(Job j: sid.getJobs()) validateJob(j, errs);

		if(sid.getAssets() == null || sid.getAssets().size() == 0) 	addFatalErr("Must be at least one asset", 10003, errs);
		else for(Asset a: sid.getAssets()) validateAsset(a, errs);
		
		if(sid.getNARs() != null) 
			for(NonAssetResource nar: sid.getNARs()) validateNar(nar, errs);
		
		if (jobIdSet.size() != jobIdCount)							addFatalErr("Job IDs must be unique", 4001, errs);
		if (jobAssetIdSet.size() !=jobAssetIdCount)					addFatalErr("Asset Resource IDs must be unique", 4002, errs);
		if (assetIdSet.size() != assetIdCount)						addFatalErr("Asset IDs must be unique", 4003, errs);
		if (narNSNSet.size() != narNSNCount)						addFatalErr("Non Asset Resource Stock Numbers must be unique", 4004, errs);
		
		return errs.size() == oldSize;
	}
		
/*
Checks on metadata - if these are bad, no jobs are scheduled and the error portion of the output includes at least one record of the error in the metadata:
Error conditions include:
-- Any null field
-- PeriodStart < 0  
-- PeriodEnd <= 0  
-- PeriodEnd <= periodStart
-- EstimatedTimeConversionFactor <= 0
-- k > 100, K < 0
-- maxPriority <= 0
-- status.vector elements < 0
-- travel.vector elements < 0
-- travelImportance > 100 or < 0
-- travelFunction != 1
*/
	@Override			
	public boolean validateMetadata(MetaData md, List<ProcessingError> errs) {
		int oldSize = errs.size();
		
		if (md != null && md.getMaxPriority() != null) theMaximumPriority = md.getMaxPriority();

		if (md.getPeriodStart() == null)			addFatalErr("period start must be non-null", 1001, errs);
		if (md.getPeriodEnd() == null)				addFatalErr("period end must be non-null", 1002, errs);
		if (md.getTopLevelLocatorId() == null)		addFatalErr("top level locator must be non-null", 1003, errs);
		if (md.getConversionFactor() == null)		addFatalErr("conversion factor must be non-null", 1004, errs);
		if (md.getStatusVector() == null)			addFatalErr("status vector must be non-null", 1005, errs);
		if (md.getKValue() == null)					addFatalErr("k value must be non-null", 1006, errs);
		if (md.getMaxPriority() == null)			addFatalErr("max priority must be non-null", 1007, errs);
		if (md.getTravelVector() == null)			addFatalErr("travel vector must be non-null", 1008, errs);
		if (md.getTravelImportance() == null)		addFatalErr("travel importance must be non-null", 1009, errs);
		if (md.getTravelFunction() == null)			addFatalErr("travel function must be non-null", 1010, errs);

		if (md.getPeriodStart() != null && md.getPeriodEnd() != null 
		 &&	md.getPeriodStart() > md.getPeriodEnd()) addFatalErr("period start, end inconsistent", 1, errs);

		if (md.getPeriodStart() != null
		 && md.getPeriodStart() < 0)          		addFatalErr("period start must be non-negative", 12, errs);				

		if (md.getPeriodEnd() != null
		 && md.getPeriodEnd() <= 0)          		addFatalErr("period end must be positive", 2, errs);				

		if (md.getConversionFactor() != null
		 && md.getConversionFactor() <= 0) 			addFatalErr("conversion factor inconsistent", 3, errs);

		if (md.getKValue() != null
		 && (md.getKValue() < 0 
		 || md.getKValue() > 100)) 					addFatalErr("k value inconsistent", 4, errs);
		
		if (md.getMaxPriority() != null
		 && md.getMaxPriority() <= 0) 				addFatalErr("max priority inconsistent", 5, errs);
		
		if (md.getStatusVector() != null)
			try {
				for (Integer st: md.getParsedStatusAsSet()) {
					if (st < 0) 					addFatalErr("status vector has negative values", 9, errs);
				}	
			} catch (Exception e) {
													addFatalErr("ill formatted status vector", 10, errs);	
		}

		
		if (md.getTravelVector() != null && md.getTravelVector().length() > 0)
			try {
				for (Integer tve: md.getParsedTravelVector()) {
					if (tve < 0) 					addFatalErr("travel vector has negative values", 6, errs);
				}
			} catch (Exception e) {
													addFatalErr("ill formatted travel vector", 11, errs);	
			}					
		
		if (md.getTravelImportance() != null
		&& (md.getTravelImportance() < 0 
		 || md.getTravelImportance() > 100)) 		addFatalErr("travel importance inconsistent", 7, errs);
		
		if (md.getTravelFunction() != null
		 && md.getTravelFunction() != 1) 			addFatalErr("travel function inconsistent", 8, errs);

		return errs.size() == oldSize;
	}
/*
Job: if any of the following cases occur, no jobs are scheduled and the error portion of the output includes at least one record of the error in the Job.  
-- Any non-list fields null, except AssignedAsset, RequestedAsset, Organization, EndTime, Proficiency ID and Attribute Key
-- Job.#job asset = 0.
-- Job.#Maintainability = 0.
-- Job.Maintainability.startTime >= Job.Maintainability.endTime, 
-- Job.Maintainability.startTime < 0
-- Job.Maintainability.endTime <= 0
-- Job.EstimatedTime <= 0.
-- Job.RecordedTime < 0.
-- Job.Priority < 0.
-- Job.EarliestStartTime >= Job.LatestEndTime
-- Job.EarliestStartTime < 0
-- Job.LatestEnd Time <= 0

-- Job.#AssetResources = 0.
-- Job.AssetResource.AssignedAssetID must be for an asset in the list of Assets (or 0 or null)
-- Job.AssetResource.RequiredAssetID must be for an asset in the list of Assets (or 0 or null)
-- Job.AssetResource.RequiredAssetID and Job.AssetResource.AssignedAssetID must be equal if they are both non-null and non-zero
-- Job.AssetResource.Proficiency.importance > 100 or < 0
-- Job.AssetResource.Proficiency.level < 0
-- Job.AssetResource.Proficiency.function < 0
-- Job.AssetResource.Proficiency.maxLevel  < 0 or < level
-- Job.AssetResource.Proficiency.maxLevel  not constant for a given Classification ID
-- Job.AssetResource.Attribute.function < 0

-- Job.NonAssetResource.NationalStockNumber must correspond to one of the NonAssetResource object's NSN
*/
	// make sure all required assets for a job are unique
	boolean checkUniqueRequiredAssets(Job j) {
		HashSet<Long> assetIdSet    = new HashSet<Long>();
		int count					= 0;
		
		for (AssetResource ar: j.getAssetResources()) {
			if (ar.getRequiredAssetId() != null && ar.getRequiredAssetId() != 0) {
				assetIdSet.add(ar.getRequiredAssetId());
				count++;
			}
		}
		return (assetIdSet.size() == count);
	}
	
	// make sure all assigned assets for a job are unique
	boolean checkUniqueAssignedAssets(Job j) {
		HashSet<Long> assetIdSet    = new HashSet<Long>();
		int count					= 0;
		
		for (AssetResource ar: j.getAssetResources()) {
			if (ar.getAssignedAssetId() != null && ar.getAssignedAssetId() != 0) {
				assetIdSet.add(ar.getAssignedAssetId());
				count++;
			}
		}
		return (assetIdSet.size() == count);
	}
	
	boolean checkConsistentMaxLevels(long classificationId, Integer newMaxLevel) {
		Integer oldMaxLevel;
		
		if ((oldMaxLevel = maxLevels.get(classificationId)) == null) {  // lazy initialize it
			maxLevels.put(classificationId, newMaxLevel);
			return true;
		}
		else if (oldMaxLevel != newMaxLevel) {  // must be constant for any give Classification ID
			return false;
		}
		return true;
	}


	// main routine for job data validation
	@Override			
	public boolean validateJob(Job j, List<ProcessingError> errs) {
		int oldSize = errs.size();
		long theId = (j.getId() == null ? 0 : j.getId());
		if (j.getId() == null)						addSevereErr("Job ID must be non-null", 2001, theId, errs);
		else {
			jobIdSet.add(j.getId());  // add to list of job id's to test for uniqueness
			jobIdCount++;              // add to count of how many we have
		} 
		if (j.getManagedAssetId() == null)			addSevereErr("Job Managed Asset ID must be non-null", 2002, theId, errs);
		if (j.getLocator() == null)					addSevereErr("Job Locator must be non-null", 2003, theId, errs);
		if (j.getStatus() == null)					addSevereErr("Job Status must be non-null", 2004, theId, errs);
		if (j.getEstimatedTime() == null)			addSevereErr("Job Estimated Time must be non-null", 2005, theId, errs);
		if (j.getRecordedTime() == null)			addSevereErr("Job Recorded Time must be non-null", 2006, theId, errs);
		if (j.getPriority() == null)				addSevereErr("Job Priority must be non-null", 2007, theId, errs);
		if (j.getStartDateTime() == null)			addSevereErr("Job Start must be non-null", 2008, theId, errs);
		if (j.getEarliestStart() == null)			addSevereErr("Job Earliest Start must be non-null", 2010, theId, errs);
		if (j.getLatestEnd() == null)				addSevereErr("Job Latest End must be non-null", 2011, theId, errs);
		
		if (j.getMaintainabilities() == null 
		 || j.getMaintainabilities().isEmpty() ) 	addSevereErr("no job maintainabilities ", 101, theId, errs);
		else {
			for (AssetAvailability aa: j.getMaintainabilities()) {
				if (aa.getStartDateTime() == null)	addSevereErr("Job Maintainability start date must be non-null", 2012, theId, errs);
				if (aa.getEndDateTime() == null)	addSevereErr("Job Maintainability end date must be non-null", 2013, theId, errs);

				if (aa.getStartDateTime() != null && aa.getEndDateTime() != null
				 && aa.getStartDateTime() > aa.getEndDateTime()) addSevereErr("inconsistent job maintainability for job ", 102, theId, errs);

				if (aa.getStartDateTime() != null
						 && aa.getStartDateTime() < 0)  addSevereErr("job maintainability start must be non-negitive ", 103, theId, errs);
				if (aa.getEndDateTime() != null
						 && aa.getEndDateTime() <= 0) 	addSevereErr("job maintainability end must be positive ", 103, theId, errs);
			}
		}				
		if (j.getEstimatedTime() != null
		 && j.getEstimatedTime() <= 0) 				 addSevereErr("inconsistent job estimated time ", 104, theId, errs);

		if (j.getRecordedTime() != null 
		 && j.getRecordedTime() < 0) 				 addSevereErr("inconsistent job recorded time ", 105, theId, errs);

		if (j.getPriority() != null 
		 && j.getPriority() < 0) 					 addSevereErr("inconsistent job priority ", 106, theId, errs);

		if (j.getPriority() != null 
				 && j.getPriority() > theMaximumPriority) 	 addSevereErr("inconsistent job priority ", 106, theId, errs);
		
		if (j.getEarliestStart() != null && j.getLatestEnd() != null
		 && j.getEarliestStart() > j.getLatestEnd()) addSevereErr("inconsistent job earliest start, latest end ", 107, theId, errs);

		if (j.getEarliestStart() != null
				 && j.getEarliestStart() < 0 ) 				 addSevereErr("job earliest start must be non-negative ", 108, theId, errs);
		if (j.getLatestEnd() != null
				 && j.getLatestEnd() <= 0 ) 				 addSevereErr("job latest end must be positive ", 108, theId, errs);
		
		if (j.getAssetResources() == null 
		 || j.getAssetResources().isEmpty() ) 		 addSevereErr("no job asset resources ", 110, theId, errs);
		else {
			if (!checkUniqueAssignedAssets(j))		addSevereErr("Job Asset Resource Assigned Asset IDs must be unique", 2028, theId, errs);
			if (!checkUniqueRequiredAssets(j))		addSevereErr("Job Asset Resource Required Asset IDs must be unique", 2029, theId, errs);
			for (AssetResource ar: j.getAssetResources()) {
				if (ar.getId() == null)				addSevereErr("Job Asset Resource ID must be non-null", 2014, theId, errs);						
				else {
					jobAssetIdSet.add(ar.getId());  // add to list of job asset resource id's to test for uniqueness
					jobAssetIdCount++;              // add to count of how many we have
				}
				if (ar.getClassificationId() == null)								addSevereErr("Job Asset Resource Classification ID must be non-null", 2015, theId, errs);						
				if (ar.getJob() == null)											addSevereErr("Job Asset Resource Job ID must be non-null", 2016, theId, errs);
				if (ar.getAssignedAssetId() != null && ar.getAssignedAssetId() != 0
				 && !HaveAssetWithId(ar.getAssignedAssetId())) 						addSevereErr("Job Asset Resource Assigned ID must be for a schedulable Asset", 2026, theId, errs);
				if (ar.getRequiredAssetId() != null && ar.getRequiredAssetId() != 0 
				 && !HaveAssetWithId(ar.getRequiredAssetId())) 						addSevereErr("Job Asset Resource Required ID must be for a schedulable Asset", 2027, theId, errs);
				if (ar.getRequiredAssetId() != null && ar.getRequiredAssetId() != 0
				 && ar.getAssignedAssetId() != null && ar.getAssignedAssetId() != 0
				 && !ar.getAssignedAssetId().equals(ar.getRequiredAssetId())) 		addSevereErr("Job Asset Resource Assigned Asset ID must equal Requested ID", 2017, theId, errs);
				if (ar.getProficiencies() != null ) {
					int classIdCount     = 0;
					Set<Long> classIdSet = new HashSet<Long>();
					
					for (Proficiency pr: ar.getProficiencies()) {
						if (pr.getClassificationId() == null)						  addSevereErr("Job AR Proficiency Classification ID must be non-null", 2018, theId, errs);
						else {
							classIdSet.add(pr.getClassificationId());
							classIdCount++;
						}
						if (pr.getLevel() == null)									  addSevereErr("Job AR Proficiency Level must be non-null", 2019, theId, errs);
						if (pr.getImportance() == null)								  addSevereErr("Job AR Proficiency Importance must be non-null", 2020, theId, errs);
						if (pr.getFunction() == null)								  addSevereErr("Job AR Proficiency Function must be non-null", 2021, theId, errs);
						if (pr.getMaxValue() == null)								  addSevereErr("Job AR Proficiency Max Value must be non-null", 2022, theId, errs);

						if (pr.getLevel() != null
						 && pr.getLevel() < 0) 										  addSevereErr("inconsistent proficiency level ", 111, theId, errs);
						
						if (pr.getMaxValue() != null && pr.getLevel() != null
						 && (pr.getMaxValue() < 0 || pr.getMaxValue() < pr.getLevel())) addSevereErr("inconsistent proficiency max value ", 112, theId, errs);

						if (pr.getMaxValue() != null && pr.getClassificationId() != null &&
							!checkConsistentMaxLevels(pr.getClassificationId(), pr.getMaxValue())) {
								addSevereErr("Maximum Level for a proficiency must be the same for all of that Classification ID", 2026, theId, errs);
						}
						
						if (pr.getImportance() != null
						 && (pr.getImportance() < 0 || pr.getImportance() > 100))       addSevereErr("inconsistent proficiency importance ", 113, theId, errs);
						
						if (pr.getFunction() != null 
						 && pr.getFunction() < 0)   								  addSevereErr("inconsistent proficiency function ", 114, theId, errs);
					}
					if (classIdSet.size() != classIdCount)						 	  addFatalErr("Job Asset Proficiency Classification IDs must be unique", 4022, errs);
				}								
				if (ar.getAssetAttributes() != null ) {
					for (AssetAttribute aa: ar.getAssetAttributes()) {
						if (aa.getId() == null)							   			  addSevereErr("Job AR Attribute ID must be non-null", 2023, theId, errs);
						if (aa.getValue() == null)						   			  addSevereErr("Job AR Attribute Value must be non-null", 2024, theId, errs);
						if (aa.getFunction() == null)					   			  addSevereErr("Job AR Attribute Function must be non-null", 2025, theId, errs);

						if (aa.getFunction() != null
						 && aa.getFunction() < 0) 									  addSevereErr("inconsistent attribute function ", 115, theId, errs);
					}
				}								

			}
			if (j.getNonAssetResources() != null && !j.getNonAssetResources().isEmpty()) {
				for (NonAssetResource nar: j.getNonAssetResources()) {
					if (nar.getStockNumber() == null)								  addSevereErr("inconsistent Job NAR NSN", 2031, theId, errs);
					if (nar.getQuantityOnHand() == null)							  addSevereErr("inconsistent Job NAR Qty", 2032, theId, errs);
					if (nar.getType() == null)										  addSevereErr("inconsistent Job NAR Type", 2033, theId, errs);
					if (nar.getStockNumber() != null &&  !HaveNARWithNsn(nar.getStockNumber())) addSevereErr("inconsistent Job NAR NSN must be for a schedulable NAR", 2034, theId, errs);											}
			}
		}
		return errs.size() == oldSize;
	}
/*
Checks on Asset: if any of the following cases occur, no jobs are scheduled and the error portion of the output includes at least one record of the error in the Asset. 
		 -- All non-list fields must be non null except Asset Type and Asset Proficiency ID and Asset Attribute Key
		 -- Asset.#availabilities == 0
		 -- Asset.availability.startTime >=  Asset.Maintainability.endTime.
		 -- Asset.availability.startTime < 0.
		 -- Asset.availability.endTime <= 0.
		 -- Asset.Proficiency.level < 0
		 -- Asset.Proficiency.level > maxLevel for that classification ID
*/			
	@Override			
	public boolean validateAsset(Asset a,  List<ProcessingError> errs) {
		int oldSize = errs.size();
		long theId = (a.getId() == null ? 0 : a.getId());
		Integer maxLevel;   // maximum Level for a proficiency with a given Classification ID, as set by the jobs

		if (a.getId() == null)						addSevereErr("Asset ID must be non-null", 3001, theId, errs);
		else {
			assetIdSet.add(a.getId());  // add to list of  asset id's to test for uniqueness
			assetIdCount++;              // add to count of how many we have
		} 
		if (a.getClassificationId() == null)		addSevereErr("Asset Classification ID must be non-null", 3002, theId, errs);
		if (a.getOrganizationId() == null)			addSevereErr("Asset Organization must be non-null", 3004, theId, errs);
		if (a.getTrackTravel() == null)				addSevereErr("Asset Track Travel must be non-null", 3005, theId, errs);
		if (a.getCalculateUtility() == null)		addSevereErr("Asset Calculate Utility must be non-null", 3006, theId, errs);

		if (a.getAvailabilities() == null || a.getAvailabilities().isEmpty() ) 	addSevereErr("no asset availabilities ", 201, theId, errs);
		else {
			for (AssetAvailability aa: a.getAvailabilities()) {
				if (aa.getStartDateTime() == null)				addSevereErr("Asset Availability start date must be non-null", 3007, theId, errs);
				if (aa.getEndDateTime() == null)				addSevereErr("Asset availability end date must be non-null", 3008, theId, errs);						

				if (aa.getStartDateTime() != null && aa.getEndDateTime() != null
				 && aa.getStartDateTime() > aa.getEndDateTime()) addSevereErr("inconsistent asset availability ", 202, theId, errs);

				if (aa.getStartDateTime() != null
						 && aa.getStartDateTime() < 0) 		 	 addSevereErr("asset availability start must be non-negative ", 203, theId, errs);
				if (aa.getEndDateTime() != null
						 && aa.getEndDateTime() <= 0) 			 addSevereErr("asset availability end must be positive ", 203, theId, errs);
			}
		}				
		if (a.getProficiencies() != null ) {
			int classIdCount     = 0;
			Set<Long> classIdSet = new HashSet<Long>();
			
			for (Proficiency pr: a.getProficiencies()) {
				if (pr.getClassificationId() == null)						  addSevereErr("Asset Proficiency Classification ID must be non-null", 3010, theId, errs);
				else {
					classIdSet.add(pr.getClassificationId());
					classIdCount++;
				}
				if (pr.getLevel() == null)									  addSevereErr("Asset Proficiency Level must be non-null", 3011, theId, errs);

				if (pr.getLevel() != null
				 && pr.getLevel() < 0) 							  			  addSevereErr("Asset Proficiency Level must be positive ", 211, theId, errs);

				if (pr.getClassificationId() != null && pr.getLevel() != null 
						&& (maxLevel = maxLevels.get(pr.getClassificationId())) != null
						&& pr.getLevel() > maxLevel ) {					  addSevereErr("Asset Proficiency Level must be no larger than maxLevel for that Classification ID ", 211, theId, errs);		
				}

			}
			
			if (classIdSet.size() != classIdCount)						 	  addFatalErr("Asset Proficiency Classification IDs must be unique", 4021, errs);
		}
		
		if (a.getAssetAttributes() != null ) {
			for (AssetAttribute aa: a.getAssetAttributes()) {
				if (aa.getId() == null)							   			  addSevereErr("Asset Attribute ID must be non-null", 3012, theId, errs);
				if (aa.getValue() == null)						   			  addSevereErr("Asset Attribute Value must be non-null", 3014, theId, errs);
			}
		}												
		
		return errs.size() == oldSize;
	}
/*
Checks on NAR:  if any of the following cases occur, no jobs are scheduled and the error portion of the output includes at least one record of the error in the NAR. 
-- NAR Nsn, quantity and type must be non-null
-- NAR.quantity < 0.
*/
	@Override			
	public boolean validateNar(NonAssetResource nar,  List<ProcessingError> errs) {
		int oldSize = errs.size();
		long theId = (nar.getStockNumber() == null ? 0 : nar.getStockNumber()); 
		if (nar.getStockNumber() == null)		addSevereErr("Non Asset Resource Stock Number must be non-null", 4001, 0, errs);				
		else {
			narNSNSet.add(nar.getStockNumber());  // add to list of non asset resource stock numbers to test for uniqueness
			narNSNCount++;                        // add to count of how many we have
		}
		if (nar.getQuantityOnHand() == null)	addSevereErr("Non Asset Resource Quantity on Hand must be non-null", 4002, theId, errs);				
		if (nar.getType() == null)				addSevereErr("Non Asset Resource Type must be non-null", 4003, theId, errs);				

		if (nar.getQuantityOnHand() != null
		 && nar.getQuantityOnHand() < 0 ) 		addSevereErr(" Non Asset Resource Quantity inconsistent", 301, theId, errs);

		return errs.size() == oldSize;
	}
	
	void addSevereErr(String msg, int type, long value, List<ProcessingError> errs) {
		addErr(msg, SEVERE_ERRROR, type, value, errs);
	}
	
	void addFatalErr(String msg, int type, List<ProcessingError> errs) {
		addErr(msg, FATAL_ERRROR, type, 0L, errs);
	}
	
	void addErr(String msg, int severity, int type, long value, List<ProcessingError> errs) {
		ProcessingError pe = new ProcessingError();
		pe.setMessage(msg);
		pe.setSeverity(severity);
		pe.setValue(value);
		pe.setType(type);
		errs.add(pe);
	}

	boolean HaveAssetWithId(Long Id) {
		return getAssetFromID(Id) != null;
	}
	
	// map from asset id to asset - unoptimized, this is only used during initialization so was not considered worth optimizing with a map
	Asset getAssetFromID(Long assetId) {	
		if (assetId == null) return null;
		for (Asset a: sid.getAssets()) {
			if (a.getId() != null && a.getId().equals(assetId)) {
				return a;
			}
		}
		return null;
	}
	
	boolean HaveNARWithNsn(Long nsn) {
		return getNARFromNsnForValidation(nsn) != null;
	}
		
	// map from national stock number to NAR - unoptimized, only used during validation (map is not yet set up) 
	NonAssetResource getNARFromNsnForValidation(Long nsn) {	
		for (NonAssetResource nar: sid.getNARs()) {
			if (nar.getStockNumber() != null && (nar.getStockNumber().equals(nsn))) {
				return nar;
			}
		}
		return null;
	}

}
