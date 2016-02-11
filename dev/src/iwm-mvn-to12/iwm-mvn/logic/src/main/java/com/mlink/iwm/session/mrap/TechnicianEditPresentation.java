package com.mlink.iwm.session.mrap;

import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetAttribute;
import com.mlink.iwm.entity.asset.AssetNotAvail;
import com.mlink.iwm.session.asset.AssetHome;
import com.mlink.iwm.util.DateUtils;

public class TechnicianEditPresentation {

	public enum Status {
		IN, ABSENT;
	}

	private enum AttrName {
		RANK, PHONE, EMAIL;
	}

	private Asset technician;
	private AssetAttribute rankAttr;
	private AssetAttribute emailAttr;
	private AssetAttribute phoneAttr;
	private List<Asset> qualificationList;
	private DataModel qualDataModel;
	private List<AssetNotAvail> leaveTimeList;
	private DataModel leaveTimeDataModel;
	private Status statusToday;
	private Status calculatedStatusToday;
	// NB refactor- use a single injection point in a gelper cass
	@In(create = true, required = true)
	private AssetHome assetHome;

	public TechnicianEditPresentation(Asset tech, List<Asset> quals) {

		// NB added below
		if (assetHome == null)
			assetHome = (AssetHome) Component.getInstance(AssetHome.class);

		this.setTechnician(tech);
		this.qualificationList = quals;
		this.leaveTimeList = tech.getAssetNotAvails();
		this.qualDataModel = new ListDataModel(this.qualificationList);
		this.leaveTimeDataModel = new ListDataModel(this.leaveTimeList);
		initAttributes();
		calculateStatusToday();
	}

	private void calculateStatusToday() {
		this.calculatedStatusToday = Status.IN;
		this.statusToday = Status.IN;

		for (AssetNotAvail leaveTime : this.leaveTimeList) {
			if (leaveTime.getStartDate() != null && leaveTime.getEndDate() != null) {
				if (DateUtils.isTodayInRange(leaveTime.getStartDate(), leaveTime.getEndDate())) {
					this.calculatedStatusToday = Status.ABSENT;
					this.statusToday = Status.ABSENT;
					break;
				}
			}
		}
	}

	private void initAttributes() {
		List<AssetAttribute> assetAttrs = getTechnician().getAssetAttributes();
		for (AssetAttribute attr : assetAttrs) {
			AttrName attrName = AttrName.valueOf(attr.getName());
			switch (attrName) {
			case RANK:
				this.rankAttr = attr;
				break;
			case EMAIL:
				this.emailAttr = attr;
				break;
			case PHONE:
				this.phoneAttr = attr;
				break;
			}
		}

		if (this.rankAttr == null) {
			this.rankAttr = createMissingAttribute(assetAttrs, AttrName.RANK);
		}
		if (this.emailAttr == null) {
			this.emailAttr = createMissingAttribute(assetAttrs, AttrName.EMAIL);
		}
		if (this.phoneAttr == null) {
			this.phoneAttr = createMissingAttribute(assetAttrs, AttrName.PHONE);
		}
	}

	private AssetAttribute createMissingAttribute(List<AssetAttribute> assetAttrs, AttrName attrName) {
		AssetAttribute assetAttr = new AssetAttribute();
		assetAttr.setName(attrName.name());
		assetAttr.setValue("");
		assetAttr.setAsset(technician);
		assetAttrs.add(assetAttr);
		return assetAttr;
	}

	public MilitaryRank getRank() {
		MilitaryRank rank = MilitaryRank.get(rankAttr.getValue());
		if (rank.equals(MilitaryRank.UNKN)) {
			return null;
		}
		return rank;
	}

	public void setRank(MilitaryRank rank) {
		if (rank == null) {
			this.rankAttr.setValue(MilitaryRank.UNKN.getAbbrv());
		} else {
			this.rankAttr.setValue(rank.getAbbrv());
		}
	}

	public String getEmail() {
		return this.emailAttr.getValue();
	}

	public void setEmail(String email) {
		this.emailAttr.setValue(email);
	}

	public String getPhone() {
		return this.phoneAttr.getValue();
	}

	public void setPhone(String phone) {
		this.phoneAttr.setValue(phone);
	}

	public String getTechName() {
		return this.getTechnician().getName();
	}

	public void setTechName(String name) {
		this.getTechnician().setName(name);
	}

	public DataModel getQualificationsDataModel() {
		if (this.qualificationList.isEmpty()) {
			// Asset qual = new Asset();
			// NB replaced above with below
			Asset qual = assetHome.getNewQualification();
			qual.setParent(technician);
			this.qualificationList.add(qual);
		}
		return this.qualDataModel;
	}

	public void setTechnician(Asset technician) {
		this.technician = technician;
	}

	public Asset getTechnician() {
		return technician;
	}

	public DataModel getLeaveTimeDataModel() {
		if (leaveTimeList.isEmpty()) {
			AssetNotAvail ana = new AssetNotAvail();
			ana.setAsset(technician);
			leaveTimeList.add(ana);
		}
		return leaveTimeDataModel;
	}

	public void addQualification(Asset qual) {
		this.qualificationList.add(qual);
	}

	public void deleteQualification(Asset qual) {
		this.qualificationList.remove(qual);
	}

	public void addLeaveTime(AssetNotAvail ana) {
		this.leaveTimeList.add(ana);
	}

	public void deleteLeaveTime(AssetNotAvail leaveTime) {
		this.leaveTimeList.remove(leaveTime);
	}

	public void setStatusToday(Status status) {
		this.statusToday = status;
	}

	public Status getStatusToday() {
		return statusToday;
	}

	public void setCalculatedStatusToday(Status status) {
		this.calculatedStatusToday = status;
	}

	public Status getCalculatedStatusToday() {
		return calculatedStatusToday;
	}

	public Status getInStatus() {
		return Status.IN;
	}

	public Status getAbsentStatus() {
		return Status.ABSENT;
	}

}
