package com.mlink.iwm.session.define;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.define.ActionTemplate;

@SuppressWarnings("serial")  
 @Name("actionTemplateList")
public class ActionTemplateList extends EntityQuery<ActionTemplate> {

	private static final String EJBQL = "select actionTemplate from ActionTemplate actionTemplate";

	private static final String[] RESTRICTIONS = {
			"lower(actionTemplate.modifier) like lower(concat(#{actionTemplateList.actionTemplate.modifier},'%'))",
			"lower(actionTemplate.name) like lower(concat(#{actionTemplateList.actionTemplate.name},'%'))",
			"lower(actionTemplate.verb) like lower(concat(#{actionTemplateList.actionTemplate.verb},'%'))",};

	private ActionTemplate actionTemplate = new ActionTemplate();

	public ActionTemplateList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ActionTemplate getActionTemplate() {
		return actionTemplate;
	}
}
