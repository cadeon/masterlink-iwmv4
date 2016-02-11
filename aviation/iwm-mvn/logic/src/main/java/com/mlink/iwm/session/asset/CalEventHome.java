package com.mlink.iwm.session.asset;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.AssetCalendar;
import com.mlink.iwm.entity.asset.CalEvent;

@SuppressWarnings("serial")  
 @Name("calEventHome")
public class CalEventHome extends EntityHome<CalEvent> {

	@In(create = true)
	AssetCalendarHome assetCalendarHome;

	public void setCalEventId(Long id) {
		setId(id);
	}

	public Long getCalEventId() {
		return (Long) getId();
	}
	
	@Override
	protected CalEvent createInstance() {
		CalEvent calEvent = new CalEvent();
		return calEvent;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		AssetCalendar assetCalendar = assetCalendarHome.getDefinedInstance();
		if (assetCalendar != null) {
			getInstance().setAssetCalendar(assetCalendar);
		}
	}

	public boolean isWired() {
		return true;
	}

	public CalEvent getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
