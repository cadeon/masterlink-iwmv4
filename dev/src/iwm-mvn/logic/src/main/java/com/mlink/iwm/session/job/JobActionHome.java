package com.mlink.iwm.session.job;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.Action;
import com.mlink.iwm.entity.job.JobAction;
import com.mlink.iwm.entity.job.JobTask;
import com.mlink.iwm.session.asset.ActionHome;

@SuppressWarnings("serial")  
 @Name("jobActionHome")
public class JobActionHome extends EntityHome<JobAction> {

	@In(create = true)
	ActionHome actionHome;
	@In(create = true)
	JobTaskHome jobTaskHome;

	public void setJobActionId(Long id) {
		setId(id);
	}

	public Long getJobActionId() {
		return (Long) getId();
	}

	@Override
	protected JobAction createInstance() {
		JobAction jobAction = new JobAction();
		return jobAction;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Action action = actionHome.getDefinedInstance();
		if (action != null) {
			getInstance().setAction(action);
		}
		JobTask jobTask = jobTaskHome.getDefinedInstance();
		if (jobTask != null) {
			getInstance().setJobTask(jobTask);
		}
	}

	public boolean isWired() {
		return true;
	}

	public JobAction getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
