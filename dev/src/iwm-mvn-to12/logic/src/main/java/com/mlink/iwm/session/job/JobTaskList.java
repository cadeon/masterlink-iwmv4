package com.mlink.iwm.session.job;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.job.JobTask;

@SuppressWarnings("serial")  
 @Name("jobTaskList")
public class JobTaskList extends EntityQuery<JobTask> {

	private static final String EJBQL = "select jobTask from JobTask jobTask";

	private static final String[] RESTRICTIONS = {
			"lower(jobTask.description) like lower(concat(#{jobTaskList.jobTask.description},'%'))",
			"lower(jobTask.taskCode) like lower(concat(#{jobTaskList.jobTask.taskCode},'%'))",};

	private JobTask jobTask = new JobTask();

	public JobTaskList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public JobTask getJobTask() {
		return jobTask;
	}
}
