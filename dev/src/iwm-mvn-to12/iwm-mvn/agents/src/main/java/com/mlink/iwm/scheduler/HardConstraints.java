package com.mlink.iwm.scheduler;

import com.mlink.iwm.scheduler.model.Asset;
import com.mlink.iwm.scheduler.model.AssetResource;
import com.mlink.iwm.scheduler.model.Job;

interface HardConstraints {

	boolean assetFitsAR(Job j, AssetResource ar, Asset a);
	boolean assetRemovableFromAR(Job j, AssetResource ar);
	boolean assetSwapsFitAR(AssetResource ar1, AssetResource ar2);
	long    assetTravelTime(Asset a);
	long    assetUsedTime(Asset a);
	boolean tryToAssignAssetResource(Job j, AssetResource ar);
}