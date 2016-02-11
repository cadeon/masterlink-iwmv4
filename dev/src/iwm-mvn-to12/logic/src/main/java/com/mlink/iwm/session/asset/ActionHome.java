package com.mlink.iwm.session.asset;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.Action;
import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.define.ActionTemplate;
import com.mlink.iwm.session.define.ActionTemplateHome;

@SuppressWarnings("serial")  
 @Name("actionHome")
public class ActionHome extends EntityHome<Action> {

	@In(create = true)
	TaskHome taskHome;
	@In(create = true)
	ActionTemplateHome actionTemplateHome;
	
	@Override
	protected Action createInstance() {
		Action action = new Action();
		return action;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void setActionId(Long id) {
		setId(id);
	}

	public Long getActionId() {
		return (Long) getId();
	}
	
	public void wire() {
		getInstance();
		Task task = taskHome.getDefinedInstance();
		if (task != null) {
			getInstance().setTask(task);
		}
		ActionTemplate actionTemplate = actionTemplateHome.getDefinedInstance();
		if (actionTemplate != null) {
			getInstance().setActionTemplate(actionTemplate);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Action getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
