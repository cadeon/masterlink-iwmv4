package com.mlink.iwm.session.asset;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.asset.AssetAttribute;

@SuppressWarnings("serial")  
 @Name("assetAttributeList")
public class AssetAttributeList extends EntityQuery<AssetAttribute> {

	private static final String EJBQL = "select assetAttribute from AssetAttribute assetAttribute";

	private static final String[] RESTRICTIONS = {
			"lower(assetAttribute.name) like lower(concat(#{assetAttributeList.assetAttribute.name},'%'))",
			"lower(assetAttribute.value) like lower(concat(#{assetAttributeList.assetAttribute.value},'%'))",};

	private AssetAttribute assetAttribute = new AssetAttribute();

	public AssetAttributeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AssetAttribute getAssetAttribute() {
		return assetAttribute;
	}
}
