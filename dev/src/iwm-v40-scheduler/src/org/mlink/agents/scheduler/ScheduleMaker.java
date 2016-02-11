
package org.mlink.agents.scheduler;

import org.mlink.agents.scheduler.model.Asset;
import org.mlink.agents.scheduler.model.AssetResource;
import org.mlink.agents.scheduler.model.Job;

interface ScheduleMaker {
	float makeSchedule(int hint);
	
	interface ScheduleHelper {
		boolean removeAssetFromAssetResource(Job j, AssetResource ar);
		void swapAssetsForAssetResources(AssetResource ar1, AssetResource ar2);
		void assignAssetToAssetResource(Job j, AssetResource ar, Asset a);
	}
}
