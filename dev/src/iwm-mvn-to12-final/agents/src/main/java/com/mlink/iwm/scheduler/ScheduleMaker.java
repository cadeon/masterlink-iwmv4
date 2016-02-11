
package com.mlink.iwm.scheduler;

import com.mlink.iwm.scheduler.model.Asset;
import com.mlink.iwm.scheduler.model.AssetResource;
import com.mlink.iwm.scheduler.model.Job;

interface ScheduleMaker {
	float makeSchedule(int hint);
	
	interface ScheduleHelper {
		boolean removeAssetFromAssetResource(Job j, AssetResource ar);
		void swapAssetsForAssetResources(AssetResource ar1, AssetResource ar2);
		void assignAssetToAssetResource(Job j, AssetResource ar, Asset a);
	}
}
