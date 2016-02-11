package com.mlink.iwm.session.res;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.define.TaskTemplate;
import com.mlink.iwm.entity.res.NonAssetResource;
import com.mlink.iwm.session.asset.TaskHome;
import com.mlink.iwm.session.define.TaskTemplateHome;

@SuppressWarnings("serial")  
 @Name("nonAssetResourceHome")
public class NonAssetResourceHome extends EntityHome<NonAssetResource> {

	@In(create = true)
	TaskHome taskHome;
	@In(create = true)
	TaskTemplateHome taskTemplateHome;

	public void setNonAssetResourceId(Long id) {
		setId(id);
	}

	public Long getNonAssetResourceId() {
		return (Long) getId();
	}

	@Override
	protected NonAssetResource createInstance() {
		NonAssetResource nonAssetResource = new NonAssetResource();
		return nonAssetResource;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Task task = taskHome.getDefinedInstance();
		if (task != null) {
			getInstance().setTask(task);
		}
		TaskTemplate taskTemplate = taskTemplateHome.getDefinedInstance();
		if (taskTemplate != null) {
			getInstance().setTaskTemplate(taskTemplate);
		}
	}

	public boolean isWired() {
		return true;
	}

	public NonAssetResource getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
