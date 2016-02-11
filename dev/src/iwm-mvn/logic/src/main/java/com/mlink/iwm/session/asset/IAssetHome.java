package com.mlink.iwm.session.asset;

import java.util.List;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetNotAvail;
import com.mlink.iwm.session.IEntityHome;

@SuppressWarnings("rawtypes")
public interface IAssetHome extends IEntityHome{
	
	/**
	 * Merges non managed child list with associated managed parent
	 * @param worker
	 * @param anas asset not avalaible dates
	 * @return
	 */
	public String attachLeaveTimesToWorker(Asset parent, List<AssetNotAvail> anas);

	/**
	 * 	 * Merges non managed child list with associated managed parent
	 * @param worker
	 * @param quals non managed qualification list
	 * @return
	 */
	public String attachQualificationsToWorker(Asset parent, List<Asset> quals);

}
