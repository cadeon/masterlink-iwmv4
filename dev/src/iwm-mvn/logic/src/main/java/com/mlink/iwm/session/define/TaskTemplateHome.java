package com.mlink.iwm.session.define;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.define.ActionTemplate;
import com.mlink.iwm.entity.define.AssetTemplate;
import com.mlink.iwm.entity.define.TaskTemplate;
import com.mlink.iwm.entity.res.AssetResource;
import com.mlink.iwm.entity.res.NonAssetResource;

@SuppressWarnings("serial")  
 @Name("taskTemplateHome")
public class TaskTemplateHome extends EntityHome<TaskTemplate> {

	@In(create = true)
	AssetTemplateHome assetTemplateHome;

	
	public void setTaskTemplateId(Long id) {
		setId(id);
	}

	public Long getTaskTemplateId() {
		return (Long) getId();
	}
	
	@Override
	protected TaskTemplate createInstance() {
		TaskTemplate taskTemplate = new TaskTemplate();
		return taskTemplate;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		AssetTemplate assetTemplate = assetTemplateHome.getDefinedInstance();
		if (assetTemplate != null) {
			getInstance().setAssetTemplate(assetTemplate);
		}
	}

	public boolean isWired() {
		return true;
	}

	public TaskTemplate getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<ActionTemplate> getActionTemplates() {
		return getInstance() == null ? null : new ArrayList<ActionTemplate>(
				getInstance().getActionTemplates());
	}
	public List<AssetResource> getAssetResources() {
		return getInstance() == null ? null : new ArrayList<AssetResource>(
				getInstance().getAssetResources());
	}
	public List<NonAssetResource> getNonAssetResources() {
		return getInstance() == null ? null : new ArrayList<NonAssetResource>(
				getInstance().getNonAssetResources());
	}

}
