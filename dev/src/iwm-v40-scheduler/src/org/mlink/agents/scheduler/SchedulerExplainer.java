package org.mlink.agents.scheduler;

import org.mlink.agents.scheduler.model.Asset;
import org.mlink.agents.scheduler.model.AssetResource;
import org.mlink.agents.scheduler.model.Job;
import org.mlink.agents.scheduler.model.NonAssetResource;
import org.mlink.agents.scheduler.model.SchedulingInputData;

import java.util.List;

/**
 * 
 * Used to determine why an asset was not assigned to an asset resource and similar inquiries
 * @author kenlevine
 * @version 1.0
 *
 */
interface SchedulerExplainer {
	/**
	 * This method is used to determine if an asset fits the static constraints for assignment to an asset resource
	 * @param j  The job
	 * @param ar The asset resource
	 * @param a  The asset to be assigned
	 * @return   True if assignment passes all static constraints
	 */
	boolean assetFitsARStatically(Job j, AssetResource ar, Asset a);
	
	/**
	 * This method is used to determine if a job can be scheduled, given the assets and NARs available
	 * @param j       The job to be scheduled
	 * @param assets  The list of available assets
	 * @param nars    The list of available non asset resources
	 * @return        True if there exist assets assignable to each asset resource and sufficient nars
	 */
	boolean jobSchedulableforAssets(Job j, List<Asset> assets, List<NonAssetResource> nars);
	
	/**
	 * Lost utility for making one assignment, compared to scheduler's nominal assignments
	 * @param sid SchedulingInputData
	 * @param ar  The asset resource to be assigned to
	 * @param a   The asset assigned
	 * @return    Lost utility compared to utility of scheduler's nominal assignments
	 */
	float   schedulingPenaltyForAssignment(SchedulingInputData sid, AssetResource ar, Asset a);
	
	/**
	 * Lost utility for making a set of assignments, compared to scheduler's nominal assignments
	 * @param  sid SchedulingInputData with asset assignments
	 * @return Lost utility compared to utility of scheduler's nominal assignments
	 */
	float   schedulingPenalty(SchedulingInputData sid);
}
