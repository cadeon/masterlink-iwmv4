package com.mlink.iwm.session.define;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.define.ActionTemplate;
import com.mlink.iwm.entity.define.TaskTemplate;

@SuppressWarnings("serial")  
 @Name("actionTemplateHome")
public class ActionTemplateHome extends EntityHome<ActionTemplate> {

	@In(create = true)
	TaskTemplateHome taskTemplateHome;
	
	public void setActionTemplateId(Long id) {
		setId(id);
	}

	public Long getActionTemplateId() {
		return (Long) getId();
	}


	@Override
	protected ActionTemplate createInstance() {
		ActionTemplate actionTemplate = new ActionTemplate();
		return actionTemplate;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		TaskTemplate taskTemplate = taskTemplateHome.getDefinedInstance();
		if (taskTemplate != null) {
			getInstance().setTaskTemplate(taskTemplate);
		}
	}

	public boolean isWired() {
		return true;
	}

	public ActionTemplate getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
