package com.mlink.iwm.session.define;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.define.AssetAttributeTemplate;
import com.mlink.iwm.entity.define.AssetTemplate;

@SuppressWarnings("serial")  
 @Name("assetAttributeTemplateHome")
public class AssetAttributeTemplateHome
		extends
			EntityHome<AssetAttributeTemplate> {

	@In(create = true)
	AssetTemplateHome assetTemplateHome;



	public void setAssetAttributeTemplateId(Long id) {
		setId(id);
	}

	public Long getAssetAttributeTemplateId() {
		return (Long) getId();
	}
	
	@Override
	protected AssetAttributeTemplate createInstance() {
		AssetAttributeTemplate assetAttributeTemplate = new AssetAttributeTemplate();
		return assetAttributeTemplate;
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

	public AssetAttributeTemplate getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
