package org.mlink.agents.scheduler.model;


import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * This class encapsulates the output data for the scheduling functions in the AssignAndSchedule interface
 * @author kenneth m. levine
 * @version 1.0
 *
 */
public class SchedulingOutputData {
	List<AssetTimeShare> shares = new ArrayList<AssetTimeShare>();
	List<NonAssetResource> nars =new ArrayList<NonAssetResource>();
	List<JobNAR> jobNars =new ArrayList<JobNAR>();
	List<ProcessingError> errs =new ArrayList<ProcessingError>();
	BigDecimal utility;
	float runTime;
	long numberSchedulesAttempted;
	long numberLegalSchedulesAttempted;

	/**
	 * This method is used to include an asset time share in the scheduling output data
	 * @param ats The asset time share to be included
	 */
	public void addAssetTimeShare(AssetTimeShare ats) {
		shares.add(ats);
	}
	
	/**
	 * This method is used to include a non asset resource in the scheduling output data
	 * @param ats The non asset resource to be included
	 */
	public void addNonAssetResource(NonAssetResource nar) {
		nars.add(nar);
	}
	
	/**
	 * This method is used to include a job non asset resource in the scheduling output data
	 * @param nar The job non asset resource to be included
	 */

	public void addJobNAR(JobNAR nar) {
		jobNars.add(nar);
	}
	
	/**
	 * This method is used to include a ProcessingError in the scheduling output data
	 * @param err The ProcessingError to be included
	 */
	public void addErrror(ProcessingError err) {
		errs.add(err);
	}
	
	/**
	 * This method is used to set the Processing Errors in the scheduling output data
	 * @param ers The Processing Errors to be included
	 */
	public void setProcessingErrors(List<ProcessingError> ers) {
		errs = ers;
	}
	
	/**
	 * This method is used to get the run time in the scheduling output data
	 * @return the run time
	 */
	public Float getRunTime() {
		return runTime;
	}
	
	/**
	 * This method is used to set the run time in the scheduling output data
	 * @param val The run time to be included
	 */
	public void setRunTime(Float val) {
		runTime = val;
	}
	
	/**
	 * This method is used to get the utility in the scheduling output data
	 * @return the utility
	 */
	public BigDecimal getUtility() {
		return utility;
	}
	
	/**
	 * This method is used to set the utility in the scheduling output data
	 * @param val The utility to be included
	 */
	public void setUtility(BigDecimal val) {
		utility = val;
	}
	
	/**
	 * This method is used to get the number of legal schedules attempted in the search
	 * @return the number of legal schedules attempted
	 */
	public Long getNumberLegalSchedulesAttempted() {
		return numberLegalSchedulesAttempted;
	}
	
	/**
	 * This method is used to set the number of schedules attempted in the search
	 * @param val The number of legal schedules attempted
	 */
	public void setNumberSchedulesAttempted(Long val) {
		numberSchedulesAttempted = val;
	}
	
	/**
	 * This method is used to get the number of schedules attempted in the search
	 * @return the number of legal schedules attempted
	 */
	public Long getNumberSchedulesAttempted() {
		return numberSchedulesAttempted;
	}
	
	/**
	 * This method is used to set the number of legal schedules attempted in the search
	 * @param val The number of legal schedules attempted
	 */
	public void setNumberLegalSchedulesAttempted(Long val) {
		numberLegalSchedulesAttempted = val;
	}
	
	/**
	 * This method is used to get the Asset Time Shares in the scheduling output data
	 * @return the Asset Time Shares
	 */
	public List<AssetTimeShare> getAssetTimeShares() {return shares;}
		
	/**
	 * This method is used to get the non asset resources in the scheduling output data
	 * @return the non asset resources
	 */
	public List<NonAssetResource> getNonAssetResources() {return nars;}
	
	/**
	 * This method is used to get the job non asset resources in the scheduling output data
	 * @return the job non asset resources
	 */public List<JobNAR> getjobNARs() {return jobNars;}
	
		/**
		 * This method is used to set the Processing Errors in the scheduling output data
		 * @param val The Processing Errors to be included
		 */
	public List<ProcessingError> getProcessingErrors() {return errs;}
	

}

