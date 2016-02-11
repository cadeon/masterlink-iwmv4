package com.mlink.iwm.session.asset;

import java.util.Date;
import com.mlink.iwm.util.DateUtils;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetNotAvail;

@SuppressWarnings("serial")  
 @Name("assetNotAvailList")
public class AssetNotAvailList extends EntityQuery<AssetNotAvail> {
	@Logger
	private Log log;
	private static final String SELECT = "select assetNotAvail from AssetNotAvail assetNotAvail";
	private static final String END_DATE_POST_TODAY = " where assetNotAvail.endDate > :postDate";
	private static final String ASSET_LEAVE_TIME = SELECT+" where assetNotAvail.asset = :asset";

	private static final String DELETE = "delete from AssetNotAvail";
	
	private List<AssetNotAvail> resultList;	
	private AssetNotAvail assetNotAvail = new AssetNotAvail();

	public AssetNotAvailList() {
		setEjbql(SELECT);
		setMaxResults(25);
	}

	public AssetNotAvail getAssetNotAvail() {
		return assetNotAvail;
	}
	
	
	
	/**
	 * 
	 * @param date restriction post date for endDate
	 *        null value defaults to today's date 
	 *        
	 * @return List of AssetNotAvail for which endDate >= date
	 */
	public final List<AssetNotAvail> getPostEndDate(Date date) {
		
		clear();
		if(date == null)
			date= DateUtils.now();
		
		Query query = getEntityManager().createQuery(SELECT+END_DATE_POST_TODAY);
		return(getRestrictedList(query));
		
	}
	
	
	public final List<AssetNotAvail> getLeaveTime(Asset asset) {
		
		refresh();
		
		Query query = getEntityManager().createQuery(ASSET_LEAVE_TIME);
		query.setParameter("asset", asset);
		return(getRestrictedList(query));
		
	}
	
	public void refresh() {
		super.refresh();

	}

	
	/**
	 * 
	 * @return
	 */
	public final List<AssetNotAvail> getAll() {	
		clear();
		
		return(getRestrictedList(SELECT));
		
		
	}
	
	//TODO Temporary method. to be removed  at later time
	@Transactional
	public final void deleteAll() {
		
		try {
			Query query = getEntityManager().createQuery(DELETE);
			 query.executeUpdate();
		
		} catch (Exception ex) {
			
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		
	}
	


	@SuppressWarnings("unchecked")
	private List<AssetNotAvail> getRestrictedList(Query paramQuery) {

		clear();
		try {
			resultList =  paramQuery.getResultList();

		} catch (Exception ex) {

			log.error("SQL ERROR:" + ex.getMessage());

		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	private List<AssetNotAvail> getRestrictedList(String select) {

		clear();
		try {

			Query query = getEntityManager().createQuery(select);
			resultList = query.getResultList();

		} catch (Exception ex) {

			log.error("SQL ERROR:" + ex.getMessage());

		}
		return resultList;
	}
	
	/*
	 * The refresh method will cause the result to be cleared. The next access
	 * to the result set will cause the query to be executed.
	 */
	public void clear() {
		super.refresh();

	}
}
