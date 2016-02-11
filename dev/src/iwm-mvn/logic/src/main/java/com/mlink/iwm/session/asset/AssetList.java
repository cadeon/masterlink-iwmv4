package com.mlink.iwm.session.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.define.AssetTemplate;
import com.mlink.iwm.entity.factory.AssetKind;
import com.mlink.iwm.framework.exception.AssetException;

@SuppressWarnings("serial")
@Name("assetList")
public class AssetList extends EntityQuery<Asset> implements IAssetList {
	private static final String sq = "'";

	private static final String SELECT = "select asset from Asset asset";
	private static final String SELECT_WO = SELECT + " Where asset.assetKind = " + sq + "WO" + sq;
	private static final String SELECT_PA = SELECT + " Where asset.assetKind = " + sq + "PA" + sq
			+ " and asset.archiveDate is null";
	private static final String SELECT_WO_PR = SELECT + " Where asset.assetKind = " + sq + "PR" + sq
			+ " And asset.parent = :parent";

	private static final String SELECT_PR_LIST = "select distinct at from AssetTemplate at Where at.assetKind = "
			+ sq + "PR" + sq;

	@Logger
	private Log log;
	private Asset asset = new Asset();
	private List<Asset> resultList;
	private Map<String,AssetTemplate> templateMap = new HashMap<String,AssetTemplate>();

	public AssetList() {
		setEjbql(SELECT);
		setMaxResults(25);
	}

	public Asset getAsset() {
		return asset;
	}

	public final List<Asset> getAll() {

		return (getRestrictedList(SELECT));

	}

	/*
	 * The refresh method will cause the result to be cleared. The next access
	 * to the result set will cause the query to be executed.
	 */
	public void clear() {
		super.refresh();

	}

	public final List<Asset> getWorkersOnly() {
		return (getRestrictedList(SELECT_WO));

	}

	public final List<Asset> getVehiclesOnly() {

		return (getRestrictedList(SELECT_PA));

	}

	@SuppressWarnings("unchecked")
	private List<Asset> getRestrictedList(String select) {

		clear();
		try {

			Query query = getEntityManager().createQuery(select);
			resultList = query.getResultList();

		} catch (Exception ex) {

			log.error("SQL ERROR:" + ex.getMessage());

		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	private List<Asset> getRestrictedList(Query paramQuery) {

		clear();
		try {
			resultList = paramQuery.getResultList();

		} catch (Exception ex) {

			log.error("SQL ERROR:" + ex.getMessage());

		}
		return resultList;
	}

	/**
	 * @return list of distinct proficiency names
	 */
	@SuppressWarnings("unchecked")
	public final List<String> getDistinctQualifications() {
		List<String> distinctQuals = new ArrayList<String>();
		List<AssetTemplate> ats = (getEntityManager().createQuery(SELECT_PR_LIST).getResultList());
		for (AssetTemplate at : ats) {
			distinctQuals.add(at.getName());
			templateMap.put(at.getName(), at);
		}
		return distinctQuals;
	}
	public final AssetTemplate getTemplateByName(String name) {
		if (templateMap.isEmpty()) {
			getDistinctQualifications();
		}
		return templateMap.get(name);
	}

	/**
	 * Validates that: 1- worker has a valid AssetKind 2- worker is a parent
	 * Asset
	 * 
	 * retrieves the children prohiciencies associated with its id
	 * 
	 * @param worker
	 *            Asset to retrives the qualifications for
	 * @throws AssetException
	 *             if worker's assetKind is invalid
	 * 
	 * @return list of prophiciencies associated with the worker
	 */
	public final List<Asset> getWorkerProficiencies(Asset worker) throws AssetException {

		List<Asset> prophs = null;
		// validate as worker asset
		if (!isWorker(worker)) {
			String message = "Invalid assetKind for assetId " + worker.getId()
					+ " in conjunction with method invokation:assetList.getWorkerQualificationsnvokatio";
			log.error(message);
			throw new AssetException(message);
		}

		if (hasQualifications(worker)) {

			Query query = getEntityManager().createQuery(SELECT_WO_PR);
			query.setParameter("parent", worker);

			prophs = getRestrictedList(query);

		}

		return prophs;
	}

	public boolean hasQualifications(Asset worker) {
		// make sure we're dealing with a worker
		if (!isWorker(worker))
			return false;

		// make sure we have child assets
		if (worker.getChildAssets() == null)
			return false;

		// assumes adding a qualification sets the parent to this asset
		return true;
	}

	public boolean isWorker(Asset worker) {
		if (worker.getAssetKind() != null)
			return (AssetKind.WO == worker.getAssetKind());

		return false;

	}

}
