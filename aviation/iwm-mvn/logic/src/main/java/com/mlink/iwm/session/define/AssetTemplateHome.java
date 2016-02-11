package com.mlink.iwm.session.define;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.define.AssetAttributeTemplate;
import com.mlink.iwm.entity.define.AssetTemplate;
import com.mlink.iwm.entity.define.AssetType;
import com.mlink.iwm.entity.define.TaskTemplate;

@SuppressWarnings("serial")  
 @Name("assetTemplateHome")
public class AssetTemplateHome extends EntityHome<AssetTemplate> {

	@In(create = true)
	AssetTypeHome assetTypeHome;
	@In(create = true)
	AssetTemplateHome assetTemplateHome;

	public void setAssetTemplateId(Long id) {
		setId(id);
	}

	public Long getAssetTemplateId() {
		return (Long) getId();
	}

	@Override
	protected AssetTemplate createInstance() {
		AssetTemplate assetTemplate = new AssetTemplate();
		return assetTemplate;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		AssetType assetType = assetTypeHome.getDefinedInstance();
		if (assetType != null) {
			getInstance().setAssetType(assetType);
		}
	}

	public boolean isWired() {
		return true;
	}

	public AssetTemplate getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<AssetAttributeTemplate> getAssetAttributeTemplates() {
		return getInstance() == null
				? null
				: new ArrayList<AssetAttributeTemplate>(getInstance()
						.getAssetAttributeTemplates());
	}
	public List<AssetTemplate> getChildTemplates() {
		return getInstance() == null ? null : new ArrayList<AssetTemplate>(
				getInstance().getChildTemplates());
	}
	public List<TaskTemplate> getTaskTemplates() {
		return getInstance() == null ? null : new ArrayList<TaskTemplate>(
				getInstance().getTaskTemplates());
	}

}
