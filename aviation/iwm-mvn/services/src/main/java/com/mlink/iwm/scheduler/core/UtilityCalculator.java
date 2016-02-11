package com.mlink.iwm.scheduler.core;

import com.mlink.iwm.scheduler.model.AssetResource;
import com.mlink.iwm.scheduler.model.Asset;

public interface UtilityCalculator {
	float getCompleteUtility();
	float getAddDeltaUtility(AssetResource addAr, Asset addAsset);
	float getRemoveDeltaUtility(AssetResource removeAr, Asset removeAsset);
	float getSwapDeltaUtility(AssetResource addAr, Asset addAsset, AssetResource removeAr, Asset removeAsset);
}
