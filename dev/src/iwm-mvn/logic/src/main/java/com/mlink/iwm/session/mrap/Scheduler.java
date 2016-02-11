package com.mlink.iwm.session.mrap;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

//NB 08/09- there is no more core package
//import com.mlink.iwm.scheduler.core.SchedulerController;
import com.mlink.iwm.scheduler.SchedulerController;
import com.mlink.iwm.scheduler.model.MetaData;
import com.mlink.iwm.scheduler.model.ProcessingError;
import com.mlink.iwm.scheduler.model.SchedulingInputData;
import com.mlink.iwm.scheduler.model.SchedulingOutputData;

/**
 * @author chrisduffy
 *
 * Generic Scheduler. Specific ones extend/override.
 * 
 * Schedulers handle their own execution. Run a Scheduler.invoke() to round trip.
 *
 */
public class Scheduler implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal schedulerUtility = BigDecimal.ZERO;
	private Float      schedulerRunTime = 0F;
	private Long       schedulerTotalSchedules = 0L;
	private Long       schedulerTotalLegalSchedules = 0L;
	private Boolean    schedulerRunComplete = Boolean.FALSE;
	
	public void runDemoScheduler(){
		SchedulerController scheduler = new SchedulerController();
		invoke(System.currentTimeMillis(), (long)8, "shop", scheduler);
	}

	
	/*
	 * Run commands specific to this scheduler. 
	 * Should always be overridden.
	 */
	public SchedulingOutputData schedule(SchedulingInputData sid) throws Exception {
		System.out.println("This scheduler doesn't know how to run itself!");
		return null;
	}
	
	/*
	 * Standard set of MetaData. Updated by invoke,
	 * overridden by individual schedulers as needed.
	 */
	public MetaData createMetaData(){
		MetaData md = new MetaData();
		md.setConversionFactor((long) 3600000); //everything is in mills.
		md.setStatusVector("2;3;4");
		md.setKValue(1);
		md.setMaxPriority(13);
		md.setTravelVector("10;20;40;60;80");
		md.setTravelImportance(40);
		md.setTravelFunction(1);
		
		return md;
	}
	
	/*
	 * Invoke prepares data calls run
	 * This should likely not get overridden. 
	 */
	public void invoke(Long periodStart, Long hours, String topLevelLocator, SchedulerController scheduler){
		schedulerRunComplete = false;
		schedulerUtility = BigDecimal.ZERO;
		schedulerRunTime = 0F;
		schedulerTotalSchedules = 0L;
		schedulerTotalLegalSchedules = 0L;
		
		SchedulerInterface si = new SchedulerInterface();
		SchedulingInputData sid = new SchedulingInputData();
		SchedulingOutputData sod = new SchedulingOutputData(); 
		
		//set up the metadata
		MetaData md = createMetaData();
		md.setPeriodStart(periodStart);
		md.setPeriodEnd(periodStart+(hours*3600000)); //hours x 3.6M (convert to mills)
		md.setTopLevelLocatorId(topLevelLocator);
		md.setConversionFactor(3600000L);
		
		sid = si.buildSid(md);
		if (!validateSid(sid)){
			//Validation Failed. Punt the invoke
			//TODO: Exception?
			return;
		}
		
		//We have a valid sid at this point. Run the scheduler
		try {
			sod = scheduler.schedule(sid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Validate sod. Don't persist and error
		if (sod == null){
			System.out.println("Scheduler Output is NULL!");
			return;} 
		else {
		if (!validateSod(sod)){
			//Validation Failed. Do not persist.
			//TODO: Exception?
			return;
		}}
		
		//We have a valid sod at this point. Persist the results!
		si.persistSod(sod,md.getPeriodStart());
		
		schedulerUtility = sod.getUtility();
		schedulerRunTime = sod.getRunTime();
		schedulerTotalSchedules = sod.getNumberSchedulesAttempted();
		schedulerTotalLegalSchedules = sod.getNumberLegalSchedulesAttempted();
		schedulerRunComplete = true;
		
	} // ALL DONE, END INVOKE
	
	/*
	 * Validate the return data. This just looks for errors
	 * in the srd and punts if it finds any over level 0.
	 * 0 INFO 1 WARNING 2 ERROR 3 FATAL
	 * Fails validation on 2 or 3.
	 */
	private boolean validateSod(SchedulingOutputData sod) {
		List<ProcessingError> errs = sod.getProcessingErrors();
		Boolean passed = true; // assume success
		if (errs.size() > 0){
			for (int e=0; e<errs.size(); e++){
				ProcessingError thisError = errs.get(e);
				if (thisError.getSeverity() == 2 || thisError.getSeverity() == 3){
					passed = false; //Too bad.
					System.out.println("Scheduler said "+ thisError.getMessage());
				}
			}
		}
		//What's done is done
		return passed;
	}

	/*
	 * Validation of input data happens in the scheduler,
	 * because different schedulers may have different validation requirements
	 * override as required. This generic validation may work in most cases.
	 * 
	 */
	private Boolean validateSid(SchedulingInputData sid) {
		
		if (sid.getJobs().size() == 0){
			System.out.println("No Jobs to schedule!");
			return false;
		};
		
		if (sid.getAssets().size() == 0){
			System.out.println("No Assets to Schedule!");
			return false;
		};
		
		if (sid.getMetaData() == null){
			System.out.println("No Meta Data!");
			return false;
		}
		
		//If we made it here, we passed validation
		return true;
		
	}
	
	public BigDecimal getSchedulerUtility() { return schedulerUtility; }
	public Float      getSchedulerRunTime() { return schedulerRunTime.floatValue(); }
	public Long       getSchedulerTotalSchedules() { return schedulerTotalSchedules; }
	public Long       getSchedulerTotalLegalSchedules() { return schedulerTotalLegalSchedules; }
	public Boolean	  getSchedulerRunComplete() { return schedulerRunComplete; }
	
}
