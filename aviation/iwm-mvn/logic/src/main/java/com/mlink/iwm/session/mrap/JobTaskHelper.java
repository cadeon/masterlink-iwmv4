package com.mlink.iwm.session.mrap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.job.JobTask;
import com.mlink.iwm.entity.res.NonAssetResource;

public class JobTaskHelper {
	
	private Asset asset;
	private JobTask currentJobTask;
	private List<JobTask> jobtasks = new ArrayList<JobTask>();
	
	@Logger
	Log log;
	
	private Iterator<JobTask> jobTaskIterator;
	
	
	
	public Asset getAsset() { return asset; }
	public List<JobTask> getJobTasks() { return jobtasks; }
	
	public void setAsset(Asset a) { this.asset = a; }
	public void setJobTasks(List<JobTask> ljt) { jobtasks = ljt; }
	public void setTask(Task t) {
	}
	
	
	public List<Task> autocomplete(Object suggest) {
        String pref = (String)suggest;
        ArrayList<Task> result = new ArrayList<Task>();

        for (Task t : getTasks()) {
        	if (t.getDescription() != null && t.getDescription().toLowerCase().startsWith(pref.toLowerCase()) || "".equals(pref))
        		result.add(t);
        }
        return result;
    }

	/* Work Item helpers */
	private List<Task> getTasks() {
		ArrayList<Task> at = new ArrayList<Task>(asset.getMaintTasks());
		return at;
	} 
	
	public String getNextWorkItemDescription() {
		if (jobTaskIterator==null) {
			jobTaskIterator = jobtasks.iterator();
		}
		if (jobTaskIterator.hasNext()) {
			currentJobTask = jobTaskIterator.next();
			currentJobTask.getTaskInstance().getTask().getNonAssetResources();
			return currentJobTask.getDescription();
		} else { // end of list, return strings for work item methods
			currentJobTask = null;
		}
		return "";		
	}
	public String getWorkItemDefectCode() { 
		if (currentJobTask!=null) 
			return currentJobTask.getTaskCode(); 
		return "";
	}
	public int getWorkItemEstTime() {
		if (currentJobTask!=null) 
			return currentJobTask.getEstTime(); 
		return 0;
	}
	public Set<NonAssetResource> getWorkItemParts() {
		return currentJobTask.getTaskInstance().getTask().getNonAssetResources(); 
	}
}
