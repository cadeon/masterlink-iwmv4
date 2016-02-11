package com.mlink.iwm.session.asset;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.asset.Task;

@SuppressWarnings("serial")  
 @Name("taskList")
public class TaskList extends EntityQuery<Task> {

	private static final String EJBQL = "select task from Task task";

	private static final String[] RESTRICTIONS = {"lower(task.description) like lower(concat(#{taskList.task.description},'%'))",};

	private Task task = new Task();

	public TaskList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Task getTask() {
		return task;
	}
}
