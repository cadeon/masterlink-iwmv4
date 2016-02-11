package com.mlink.iwm.session.asset;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetNotAvail;


@SuppressWarnings("serial")  
@Name("assetNotAvailHome")
public class AssetNotAvailHome extends EntityHome<AssetNotAvail> {

	@In(create = true)
	AssetHome assetHome;


	public void setAssetNotAvailId(Long id) {
		setId(id);
	}

	public Long getAssetNotAvailId() {
		return (Long) getId();
	}
	
	@Override
	protected AssetNotAvail createInstance() {
		AssetNotAvail assetNotAvail = new AssetNotAvail();
		return assetNotAvail;
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

	public AssetNotAvail getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	
	/*
	 * (non-Javadoc)
	 *  com.mlink.iwm.framework.IAbstractEntityService#find(long)
	 */
	public AssetNotAvail find(Long id) {
		setAssetNotAvailId(id);
		return find();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public  String delete(AssetNotAvail ats) {
		 setInstance(ats);
	     getInstance();
	     return remove(); 
	}

	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public  String saveOrUpdate(AssetNotAvail ats) {
		 setInstance(ats);
		 if(isIdDefined()){
			return update();
		}
		return persist();
	}

	

}
