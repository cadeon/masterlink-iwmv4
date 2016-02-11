package com.mlink.iwm.session.job;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.job.TaskInstance;

@SuppressWarnings("serial")  
 @Name("taskInstanceList")
public class TaskInstanceList extends EntityQuery<TaskInstance> {

	private static final String EJBQL = "select taskInstance from TaskInstance taskInstance";

	private static final String[] RESTRICTIONS = {};

	private TaskInstance taskInstance = new TaskInstance();

	public TaskInstanceList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public TaskInstance getTaskInstance() {
		return taskInstance;
	}
}
