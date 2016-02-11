package com.mlink.iwm.session.define;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.define.AssetTemplate;

@SuppressWarnings("serial")  
 @Name("assetTemplateList")
public class AssetTemplateList extends EntityQuery<AssetTemplate> {

	private static final String EJBQL = "select assetTemplate from AssetTemplate assetTemplate";

	private static final String[] RESTRICTIONS = {
			"lower(assetTemplate.name) like lower(concat(#{assetTemplateList.assetTemplate.name},'%'))",
			"lower(assetTemplate.plan) like lower(concat(#{assetTemplateList.assetTemplate.plan},'%'))",};

	private AssetTemplate assetTemplate = new AssetTemplate();

	public AssetTemplateList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AssetTemplate getAssetTemplate() {
		return assetTemplate;
	}
}
