package com.mlink.iwm.session.asset;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetTimeShare;
import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.session.job.JobHome;

@SuppressWarnings("serial")  
 @Name("assetTimeShareHome")
public class AssetTimeShareHome extends EntityHome<AssetTimeShare> {

	@In(create = true)
	AssetHome assetHome;
	@In(create = true)
	JobHome jobHome;

	public void setAssetTimeShareId(Long id) {
		setId(id);
	}

	public Long getAssetTimeShareId() {
		return (Long) getId();
	}
	
	@Override
	protected AssetTimeShare createInstance() {
		AssetTimeShare assetTimeShare = new AssetTimeShare();
		return assetTimeShare;
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
		Job jobAllocatedTo = jobHome.getDefinedInstance();
		if (jobAllocatedTo != null) {
			getInstance().setJobAllocatedTo(jobAllocatedTo);
		}
	}

	public boolean isWired() {
		return true;
	}

	public AssetTimeShare getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	
	/*
	 * (non-Javadoc)
	 *  com.mlink.iwm.framework.IAbstractEntityService#find(long)
	 */
	public AssetTimeShare find(Long id) {
		setAssetTimeShareId(id);
		return find();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public  String delete(AssetTimeShare ats) {
		 setInstance(ats);
	     return remove(); 
	}

	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public  String saveOrUpdate(AssetTimeShare ats) {
		 setInstance(ats);
		 if(isIdDefined()){
			return update();
		}
		return persist(); 
	}

	

}
