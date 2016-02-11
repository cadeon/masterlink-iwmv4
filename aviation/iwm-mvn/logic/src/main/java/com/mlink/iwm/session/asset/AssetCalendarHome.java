package com.mlink.iwm.session.asset;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetCalendar;

@SuppressWarnings("serial")  
 @Name("assetCalendarHome")
public class AssetCalendarHome extends EntityHome<AssetCalendar> {

	@In(create = true)
	AssetHome assetHome;

	public void setAssetCalendarId(Long id) {
		setId(id);
	}

	public Long getAssetCalendarId() {
		return (Long) getId();
	}
	
	@Override
	protected AssetCalendar createInstance() {
		AssetCalendar assetCalendar = new AssetCalendar();
		return assetCalendar;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Asset asset = assetHome.getDefinedInstance();
		if (asset != null) {
			getInstance().setAsset(asset);
		}
	}

	public boolean isWired() {
		return true;
	}

	public AssetCalendar getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
