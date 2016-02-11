package com.mlink.iwm.session.mrap;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetAttribute;

public class TechnicianPresentation implements
		Comparable<TechnicianPresentation> {

	private Asset technician;
	private MilitaryRank rank;
	private boolean selected;
    private boolean sticky;

	public TechnicianPresentation(Asset tech) {
		this.technician = tech;
		this.rank = getTechRank();
	}
	public TechnicianPresentation(Asset tech, boolean sticky, boolean selected) {
		this.technician = tech;
		this.rank = getTechRank();
		this.sticky = sticky;
		this.selected = selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean getSelected() {
		return selected;
	}

	public String getDisplayName() {
		return technician.getName() + (sticky? " (manually assigned)" : "");
	}
	
	public String getName() {
		return technician.getName();
	}

	public Long getId() {
		return technician.getId();
	}
	
	public boolean getIsSticky() {
		return this.sticky;
	}

	public MilitaryRank getRank() {
		return this.rank;
	}

	private MilitaryRank getTechRank() {
		for (AssetAttribute assetAttr : technician.getAssetAttributes()) {
			if ("rank".equalsIgnoreCase(assetAttr.getName())) {
				return MilitaryRank.get(assetAttr.getValue());
			}
		}
		return MilitaryRank.UNKN;
	}

	public int compareTo(TechnicianPresentation o) {
		int result = 0;
		if (o != null) {
			result = o.getRank().compareTo(this.rank);
			if (result == 0) {
				result = technician.getName().compareTo(o.getName());
			}
		}
		return result;
	}
}
