package com.mlink.iwm.session.define;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.define.AssetTemplate;
import com.mlink.iwm.entity.define.AssetType;

@SuppressWarnings("serial")  
 @Name("assetTypeHome")
public class AssetTypeHome extends EntityHome<AssetType> {

	public void setAssetTypeId(Long id) {
		setId(id);
	}

	public Long getAssetTypeId() {
		return (Long) getId();
	}
	
	@Override
	protected AssetType createInstance() {
		AssetType assetType = new AssetType();
		return assetType;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public AssetType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<AssetTemplate> getAssetTemplates() {
		return getInstance() == null ? null : new ArrayList<AssetTemplate>(
				getInstance().getAssetTemplates());
	}

}
