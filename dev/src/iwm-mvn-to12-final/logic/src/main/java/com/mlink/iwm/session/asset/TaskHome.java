package com.mlink.iwm.session.asset;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.Action;
import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.define.TaskTemplate;
import com.mlink.iwm.entity.res.AssetResource;
import com.mlink.iwm.entity.res.NonAssetResource;
import com.mlink.iwm.session.define.TaskTemplateHome;

@SuppressWarnings("serial")  
 @Name("taskHome")
public class TaskHome extends EntityHome<Task> {

	@In(create = true)
	TaskTemplateHome taskTemplateHome;
	
	public void setTaskId(Long id) {
		setId(id);
	}

	public Long getTaskId() {
		return (Long) getId();
	}
	

	@Override
	protected Task createInstance() {
		Task task = new Task();
		return task;
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

	public Task getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Action> getActions() {
		return getInstance() == null ? null : new ArrayList<Action>(
				getInstance().getActions());
	}
	public List<Asset> getStickyAssets() {
		return getInstance() == null ? null : new ArrayList<Asset>(
				getInstance().getStickyAssets());
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
