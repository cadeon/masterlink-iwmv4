package com.mlink.iwm.session.asset;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.asset.CalEvent;

@SuppressWarnings("serial")  
 @Name("calEventList")
public class CalEventList extends EntityQuery<CalEvent> {

	private static final String EJBQL = "select calEvent from CalEvent calEvent";

	private static final String[] RESTRICTIONS = {"lower(calEvent.name) like lower(concat(#{calEventList.calEvent.name},'%'))",};

	private CalEvent calEvent = new CalEvent();

	public CalEventList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public CalEvent getCalEvent() {
		return calEvent;
	}
}
