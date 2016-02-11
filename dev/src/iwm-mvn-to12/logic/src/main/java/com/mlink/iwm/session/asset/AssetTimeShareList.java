package com.mlink.iwm.session.asset;

import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.asset.AssetTimeShare;

@SuppressWarnings("serial")  
 @Name("assetTimeShareList")
public class AssetTimeShareList extends EntityQuery<AssetTimeShare> {
	@Logger
	private Log log;
	private static final String SELECT = "select assetTimeShare from AssetTimeShare assetTimeShare";
	private static final String DELETE = "delete from AssetTimeShare";
	
	private List<AssetTimeShare> resultList;
	
	//private static final String[] RESTRICTIONS = {};

	private AssetTimeShare assetTimeShare = new AssetTimeShare();

	public AssetTimeShareList() {
		setEjbql(SELECT);
		//setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AssetTimeShare getAssetTimeShare() {
		return assetTimeShare;
	}
	
	
	@SuppressWarnings("unchecked")
	public final List<AssetTimeShare> getAll() {

		
		refresh();
		try {

			Query query = getEntityManager().createQuery(SELECT);
			resultList= query.getResultList();
			
		} catch (Exception ex) {
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		return resultList;
		
	}
	
	
	//TODO Temporary method. to be removedat a later time
	@Transactional
	public final void deleteAll() {
		
		try {
			Query query = getEntityManager().createQuery(DELETE);
			 query.executeUpdate();
		
		} catch (Exception ex) {
			
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		
	}
	

	@Override
	public void refresh() {
		resultList=null;
		
	}
}
