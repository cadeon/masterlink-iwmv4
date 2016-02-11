package com.mlink.iwm.session.res;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.res.NonAssetResource;

@SuppressWarnings("serial")  
 @Name("nonAssetResourceList")
public class NonAssetResourceList extends EntityQuery<NonAssetResource> {

	private static final String EJBQL = "select nonAssetResource from NonAssetResource nonAssetResource";

	private static final String[] RESTRICTIONS = {
			"lower(nonAssetResource.desc) like lower(concat(#{nonAssetResourceList.nonAssetResource.desc},'%'))",
			"lower(nonAssetResource.tag) like lower(concat(#{nonAssetResourceList.nonAssetResource.tag},'%'))",};

	private NonAssetResource nonAssetResource = new NonAssetResource();

	public NonAssetResourceList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public NonAssetResource getNonAssetResource() {
		return nonAssetResource;
	}
}
