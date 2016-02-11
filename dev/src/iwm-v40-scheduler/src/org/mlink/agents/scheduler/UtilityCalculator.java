package org.mlink.agents.scheduler;

import java.math.BigDecimal;

import org.mlink.agents.scheduler.model.AssetResource;
import org.mlink.agents.scheduler.model.Asset;

public interface UtilityCalculator {
	BigDecimal getCompleteUtility();
	BigDecimal getCompleteJobsUtility(boolean saveIt);
	BigDecimal getAssetUtility(Asset as);
	BigDecimal getAddUtility(AssetResource addAr, Asset addAsset);
	BigDecimal getRemoveUtility(AssetResource removeAr, Asset removeAsset);
	BigDecimal getSwapUtility(AssetResource addAr, Asset addAsset, AssetResource removeAr, Asset removeAsset);
	BigDecimal getAddDeltaUtility(AssetResource addAr, Asset addAsset);
	BigDecimal getRemoveDeltaUtility(AssetResource removeAr, Asset removeAsset);
	BigDecimal getSwapDeltaUtility(AssetResource addAr, Asset addAsset, AssetResource removeAr, Asset removeAsset);
}
