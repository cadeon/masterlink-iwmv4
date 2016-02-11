package com.mlink.iwm.session.job;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.job.JobAction;

@SuppressWarnings("serial")  
 @Name("jobActionList")
public class JobActionList extends EntityQuery<JobAction> {

	private static final String EJBQL = "select jobAction from JobAction jobAction";

	private static final String[] RESTRICTIONS = {
			"lower(jobAction.fieldCondition) like lower(concat(#{jobActionList.jobAction.fieldCondition},'%'))",
			"lower(jobAction.modifier) like lower(concat(#{jobActionList.jobAction.modifier},'%'))",
			"lower(jobAction.name) like lower(concat(#{jobActionList.jobAction.name},'%'))",
			"lower(jobAction.note) like lower(concat(#{jobActionList.jobAction.note},'%'))",
			"lower(jobAction.sequence) like lower(concat(#{jobActionList.jobAction.sequence},'%'))",
			"lower(jobAction.verb) like lower(concat(#{jobActionList.jobAction.verb},'%'))",};

	private JobAction jobAction = new JobAction();

	public JobActionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public JobAction getJobAction() {
		return jobAction;
	}
}
