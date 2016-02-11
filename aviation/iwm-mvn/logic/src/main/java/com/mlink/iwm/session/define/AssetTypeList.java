package com.mlink.iwm.session.define;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.define.AssetType;

@SuppressWarnings("serial")  
 @Name("assetTypeList")
public class AssetTypeList extends EntityQuery<AssetType> {

	private static final String EJBQL = "select assetType from AssetType assetType";

	private static final String[] RESTRICTIONS = {"lower(assetType.description) like lower(concat(#{assetTypeList.assetType.description},'%'))",};

	private AssetType assetType = new AssetType();

	public AssetTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AssetType getAssetType() {
		return assetType;
	}
}
