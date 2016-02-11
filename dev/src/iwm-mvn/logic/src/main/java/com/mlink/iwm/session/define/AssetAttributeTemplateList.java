package com.mlink.iwm.session.define;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.define.AssetAttributeTemplate;

@SuppressWarnings("serial")  
 @Name("assetAttributeTemplateList")
public class AssetAttributeTemplateList
		extends
			EntityQuery<AssetAttributeTemplate> {

	private static final String EJBQL = "select assetAttributeTemplate from AssetAttributeTemplate assetAttributeTemplate";

	private static final String[] RESTRICTIONS = {
			"lower(assetAttributeTemplate.name) like lower(concat(#{assetAttributeTemplateList.assetAttributeTemplate.name},'%'))",
			"lower(assetAttributeTemplate.value) like lower(concat(#{assetAttributeTemplateList.assetAttributeTemplate.value},'%'))",};

	private AssetAttributeTemplate assetAttributeTemplate = new AssetAttributeTemplate();

	public AssetAttributeTemplateList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AssetAttributeTemplate getAssetAttributeTemplate() {
		return assetAttributeTemplate;
	}
}
