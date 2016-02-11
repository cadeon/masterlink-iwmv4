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

import com.mlink.iwm.entity.asset.AssetTimeShare;


//@Name("assetTimeShareListService")
//@Scope(ScopeType.CONVERSATION)
public final class AssetTimeShareEntityListService
 {
	
	private static final String SELECT = "select ats from AssetTimeShare ats";
	private static final String DELETE = "delete from AssetTimeShare";
	@Logger
	private Log log;
	@In
	private EntityManager entityManager;
	private List<AssetTimeShare> resultList;




	public final void deleteAll() {
	
		try {
			Query query = entityManager.createQuery(AssetTimeShareEntityListService.DELETE);
			 query.executeUpdate();
		
		} catch (Exception ex) {
			
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public final List<AssetTimeShare> getAll() {

		
		resultList = null;;
		try {

			Query query = entityManager.createQuery(AssetTimeShareEntityListService.SELECT);
			resultList= query.getResultList();
			
		} catch (Exception ex) {
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		return resultList;
		
	}



}
