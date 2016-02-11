package com.mlink.iwm.session.define;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.define.TaskTemplate;

@SuppressWarnings("serial")  
 @Name("taskTemplateList")
public class TaskTemplateList extends EntityQuery<TaskTemplate> {

	private static final String EJBQL = "select taskTemplate from TaskTemplate taskTemplate";

	private static final String[] RESTRICTIONS = {"lower(taskTemplate.taskDescription) like lower(concat(#{taskTemplateList.taskTemplate.taskDescription},'%'))",};

	private TaskTemplate taskTemplate = new TaskTemplate();

	public TaskTemplateList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public TaskTemplate getTaskTemplate() {
		return taskTemplate;
	}
}
