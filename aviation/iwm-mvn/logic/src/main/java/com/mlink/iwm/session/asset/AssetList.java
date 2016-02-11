package com.mlink.iwm.session.asset;

import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.asset.Asset;

@SuppressWarnings("serial")  
 @Name("assetList")
public class AssetList extends EntityQuery<Asset> {

	private static final String SELECT = "select asset from Asset asset";

	//private static final String[] RESTRICTIONS = {"lower(asset.tag) like lower(concat(#{assetList.asset.tag},'%'))",};
	@Logger
	private Log log;
	private Asset asset = new Asset();
	private List<Asset> resultList;
	
	public AssetList() {
		setEjbql(SELECT);
		//setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Asset getAsset() {
		return asset;
	}
	
	
	@SuppressWarnings("unchecked")
	public final List<Asset> getAll() {

		
		refresh();
		try {

			Query query = getEntityManager().createQuery(SELECT);
			resultList= query.getResultList();
			
		} catch (Exception ex) {
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		return resultList;
		
	}


	@Override
	public void refresh() {
		resultList=null;
		
	}

	
}
