package com.mlink.iwm.session.asset;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.asset.AssetCalendar;

@SuppressWarnings("serial")  
 @Name("assetCalendarList")
public class AssetCalendarList extends EntityQuery<AssetCalendar> {

	private static final String EJBQL = "select assetCalendar from AssetCalendar assetCalendar";

	private static final String[] RESTRICTIONS = {};

	private AssetCalendar assetCalendar = new AssetCalendar();

	public AssetCalendarList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AssetCalendar getAssetCalendar() {
		return assetCalendar;
	}
}
