package com.mlink.iwm.session.asset;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.asset.Action;

@SuppressWarnings("serial")  
 @Name("actionList")
public class ActionList extends EntityQuery<Action> {

	private static final String EJBQL = "select action from Action action";

	private static final String[] RESTRICTIONS = {
			"lower(action.modifier) like lower(concat(#{actionList.action.modifier},'%'))",
			"lower(action.name) like lower(concat(#{actionList.action.name},'%'))",
			"lower(action.verb) like lower(concat(#{actionList.action.verb},'%'))",};

	private Action action = new Action();

	public ActionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Action getAction() {
		return action;
	}
}
