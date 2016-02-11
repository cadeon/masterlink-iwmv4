package com.mlink.iwm.session.job;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.entity.job.JobAction;
import com.mlink.iwm.entity.job.JobTask;
import com.mlink.iwm.entity.job.TaskInstance;

@SuppressWarnings("serial")  
 @Name("jobTaskHome")
public class JobTaskHome extends EntityHome<JobTask> {

	@In(create = true)
	JobHome jobHome;
	@In(create = true)
	TaskInstanceHome taskInstanceHome;

	public void setJobTaskId(Long id) {
		setId(id);
	}

	public Long getJobTaskId() {
		return (Long) getId();
	}

	@Override
	protected JobTask createInstance() {
		JobTask jobTask = new JobTask();
		return jobTask;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Job job = jobHome.getDefinedInstance();
		if (job != null) {
			getInstance().setJob(job);
		}
		TaskInstance taskInstance = taskInstanceHome.getDefinedInstance();
		if (taskInstance != null) {
			getInstance().setTaskInstance(taskInstance);
		}
	}

	public boolean isWired() {
		return true;
	}

	public JobTask getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<JobAction> getJobActions() {
		return getInstance() == null ? null : new ArrayList<JobAction>(
				getInstance().getJobActions());
	}

}
