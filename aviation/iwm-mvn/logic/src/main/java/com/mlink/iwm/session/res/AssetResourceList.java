package com.mlink.iwm.session.res;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.res.AssetResource;

@SuppressWarnings("serial")  
 @Name("assetResourceList")
public class AssetResourceList extends EntityQuery<AssetResource> {

	private static final String EJBQL = "select assetResource from AssetResource assetResource";

	private static final String[] RESTRICTIONS = {};

	private AssetResource assetResource = new AssetResource();

	public AssetResourceList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AssetResource getAssetResource() {
		return assetResource;
	}
}
