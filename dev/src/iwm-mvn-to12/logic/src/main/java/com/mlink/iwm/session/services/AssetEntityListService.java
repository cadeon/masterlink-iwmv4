package com.mlink.iwm.session.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.factory.AssetKind;


//@Name("assetListService")
//@Scope(ScopeType.CONVERSATION)
public final class AssetEntityListService 
 {
	
	private static final String SELECT = "select asset from  Asset asset";

	@Logger
	private Log log;
	@In
	private EntityManager entityManager;
	private List<Asset> resultList;

	
	@SuppressWarnings("unchecked")
	public final List<Asset> findBySerial(String serial) {
	
		resultList = null;
		String qs= AssetEntityListService.SELECT + " where asset.tag = :serial";

		try {

			Query query = entityManager.createQuery(qs);
			query.setParameter("serial", serial);
			resultList	= query.getResultList();
			
		} catch (Exception ex) {
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		return resultList;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public final List<Asset> findByAssetKind(String ak) {
	
		resultList = null;
		String qs= AssetEntityListService.SELECT + " where asset.assetKind = :ak";

		try {

			Query query = entityManager.createQuery(qs).setParameter("ak", AssetKind.valueOf(ak));
			resultList	= query.getResultList();
			
		} catch (Exception ex) {
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		return resultList;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public final List<Asset> getAll() {

		
		resultList = null;;
		try {

			Query query = entityManager.createQuery(AssetEntityListService.SELECT);
			resultList= query.getResultList();
			
		} catch (Exception ex) {
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		return resultList;
		
	}



}
