package com.mlink.iwm.session.asset;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetAttribute;
import com.mlink.iwm.entity.define.AssetAttributeTemplate;
import com.mlink.iwm.session.define.AssetAttributeTemplateHome;

@SuppressWarnings("serial")  
 @Name("assetAttributeHome")
public class AssetAttributeHome extends EntityHome<AssetAttribute> {

	@In(create = true)
	AssetAttributeTemplateHome assetAttributeTemplateHome;
	@In(create = true)
	AssetHome assetHome;

	

	public void setAssetAttributeId(Long id) {
		setId(id);
	}

	public Long getAssetAttributeId() {
		return (Long) getId();
	}
	
	@Override
	protected AssetAttribute createInstance() {
		AssetAttribute assetAttribute = new AssetAttribute();
		return assetAttribute;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		AssetAttributeTemplate aat = assetAttributeTemplateHome
				.getDefinedInstance();
		if (aat != null) {
			getInstance().setAat(aat);
		}
		Asset asset = assetHome.getDefinedInstance();
		if (asset != null) {
			getInstance().setAsset(asset);
		}
	}

	public boolean isWired() {
		return true;
	}

	public AssetAttribute getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
