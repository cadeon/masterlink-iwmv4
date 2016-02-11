package com.mlink.iwm.session.asset;

import java.util.List;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.framework.exception.AssetException;
import com.mlink.iwm.session.IEntityList;

public interface IAssetList extends IEntityList<Asset>{

	/**
	 * 
	 * @return list of Asset of kind worker
	 */
	public List<Asset> getWorkersOnly();

	
	/**
	 * 
	 * @return Asset list of kind vehicles
	 */
	public List<Asset> getVehiclesOnly();


	/**
	 * @return distinct qualification name list 
	 */
	public List<String> getDistinctQualifications();

	
	/**
	 * Retrieves the list of Asset prophiciencies associated with 
	 * a given Asset worker.
	 * 
	 * Validates that: 
	 * 1- worker has a valid AssetKind 
	 * 2- worker is a parent Asset
	 * 
	 * @param worker Asset to retrives the qualifications for
	 * @return list of prophiciencies associated with the worker
	 * @throws AssetException if worker's assetKind is invalid
	 */
	public List<Asset> getWorkerProficiencies(Asset worker)
			throws AssetException;


	
}
