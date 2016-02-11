/**
 * 
 */
package com.mlink.iwm.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import com.mlink.iwm.scheduler.model.*;



/**
 * @author chrisduffy
 * A real scheduler is overkill. Do a BASIC 'round robin' here.
 * 
 * 10. Delete all existing AssetTimeShares (unassign all) (now done in the interface)
 * 
 * 50. Assign jobs in layers, add one job to everyone, add a second job to everyone, etc 
 * 
 * 70. Create AssetTimeShares to fill no more than the scheduling period (8hrs). 
 * 
 * Things to note: We're not even considering location or skill. 
 * We're assuming the only assets passed in are Workers.
 * All workers are assumed available
 * Every scheduling run is a complete reschedule. Should be fine considering ~100 jobs and no hill climb.
 * 
 * To use, create a DemoScheduler and then DemoScheduler.invoke(). 
 *
 */

public class DemoScheduler {
	
	
	public SchedulingOutputData schedule(SchedulingInputData sid) throws Exception{
		//Does it's thing
		SchedulingOutputData sod = new SchedulingOutputData();
		
		ArrayList<Job> jobs = (ArrayList<Job>) sid.getJobs();
		ArrayList<Asset> workers = (ArrayList<Asset>) sid.getAssets();
		ArrayList<ScheduleWrapper> schedules = new ArrayList<ScheduleWrapper>();
		
		
		//Sort jobs by priority
		Collections.sort(jobs, new Comparator<Job>(){
			public int compare(Job j1, Job j2) {
               return j1.getPriority().compareTo(j2.getPriority());
			}
		});
		
		//That's Ascending priority. We'd like Descending.
		Collections.reverse(jobs);
		
		//Our jobs are all set. Create ScheduleWrappers for the workers
		for (int wi=0; wi<workers.size(); wi++){
			schedules.add(new ScheduleWrapper(workers.get(wi)));
			
		}
		
		//Put Jobs on ScheduleWrappers.
		Integer si=0; //schedule iterator
		for (int ji=0; ji<jobs.size(); ji++){
			//Assign every job
			schedules.get(si).jobs.add(jobs.get(ji));
			//Increment the schedule
			if (si<schedules.size()){
				si++;
			} else {
				si=0;
			}
		}
		
		//Assignments made! Likely over-assigned- so check for that as we make AssetTimeShares.
		
		for (si=0; si<schedules.size(); si++){
			// Go thru each wrapper, create AssetTimeShares for each job, until they don't fit.
			// If it doesn't fit, go on to the next one, maybe it will.
			ScheduleWrapper schedule = schedules.get(si);
			
			for (int ji=0; ji<schedule.jobs.size(); ji++){
				Job job = schedule.jobs.get(ji);
				
				//Does it fit?
				if (job.getEstimatedTime()<schedule.remainingTime){
					// Yes, it does!
					schedule.remainingTime = schedule.remainingTime - job.getEstimatedTime();
					//Figure out times
					//Start of job = scheduling block end minus remaining time
					Long start = sid.getMetaData().getPeriodEnd() - schedule.remainingTime;
					//End = start + est
					Long end = start + job.getEstimatedTime();
					
					AssetTimeShare ats = new AssetTimeShare();
					ats.setJobID(job.getId());
					ats.setAssetID(schedule.worker.getId());
					ats.setResourceID((long) 1); //Indiates that this assignment is as a resource, not a target of work
					ats.setStartTime(start);
					ats.setEndTime(end);
					
					//Put it in the Output!
					sod.addAssetTimeShare(ats);
					
				} //No, it doesn't 
				//Try the next one!	
			}//End Job Loop
		}//End Schedule Loop
		
		
		//Send it all back for persist.
		return sod;
	}
	
	class ScheduleWrapper{
		Asset worker;
		ArrayList<Job> jobs;
		Long remainingTime;
		
		ScheduleWrapper(Asset worker){
			this.worker=worker;
			this.jobs = new ArrayList<Job>();
			this.remainingTime = (long) 28800000; //28.8 million milliseconds in 8 hrs 
		}
		
	}
	
}
