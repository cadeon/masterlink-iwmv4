package com.mlink.iwm.session.res;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.define.AssetTemplate;
import com.mlink.iwm.entity.define.TaskTemplate;
import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.entity.res.AssetResource;
import com.mlink.iwm.session.asset.AssetHome;
import com.mlink.iwm.session.asset.TaskHome;
import com.mlink.iwm.session.define.AssetTemplateHome;
import com.mlink.iwm.session.define.TaskTemplateHome;
import com.mlink.iwm.session.job.JobHome;

@SuppressWarnings("serial")  
 @Name("assetResourceHome")
public class AssetResourceHome extends EntityHome<AssetResource> {

	@In(create = true)
	AssetTemplateHome assetTemplateHome;
	@In(create = true)
	AssetHome assetHome;
	@In(create = true)
	TaskHome taskHome;
	@In(create = true)
	TaskTemplateHome taskTemplateHome;
	@In(create = true)
	JobHome jobHome;

	public void setAssetResourceId(Long id) {
		setId(id);
	}

	public Long getAssetResourceId() {
		return (Long) getId();
	}

	@Override
	protected AssetResource createInstance() {
		AssetResource assetResource = new AssetResource();
		return assetResource;
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
		
	
		Asset stickyAsset = assetHome.getDefinedInstance();
		if (stickyAsset != null) {
			getInstance().setStickyAsset(stickyAsset);
		}
		Task task = taskHome.getDefinedInstance();
		if (task != null) {
			getInstance().setTask(task);
		}
		TaskTemplate taskTemplate = taskTemplateHome.getDefinedInstance();
		if (taskTemplate != null) {
			getInstance().setTaskTemplate(taskTemplate);
		}
		
		Job job = jobHome.getDefinedInstance();
		if (job != null) {
			getInstance().setJob(job);
		}
	
	}

	public boolean isWired() {
		return true;
	}

	public AssetResource getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
