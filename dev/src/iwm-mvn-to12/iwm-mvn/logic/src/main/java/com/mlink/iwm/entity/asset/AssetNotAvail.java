package com.mlink.iwm.entity.asset;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.mlink.iwm.entity.AbstractEntity;

/**

 */
@SuppressWarnings({ "serial" })
@Entity
@AttributeOverride(name = "id", column = @Column(name = "assetNotAvailId"))
@Table(name = "AssetNotAvail")
public class AssetNotAvail extends AbstractEntity implements
		Comparable<AssetNotAvail> {

	private Date startDate;
	private Date endDate;
	private Asset asset;
	private boolean isAbsent;

	public AssetNotAvail() {
	}

	public AssetNotAvail(Date endDate, Date startDate, Asset asset) {

		this.endDate = endDate;
		this.startDate = startDate;
		this.asset = asset;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "endDate", length = 19)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startDate", length = 19)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assetId")
	public Asset getAsset() {
		return this.asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	
	@Column(name = "isAbsent")
	public boolean getIsAbsent() {
		return isAbsent;
	}
	public void setIsAbsent(boolean isAbsent) {
		this.isAbsent = isAbsent;
	}
	
	public int compareTo(AssetNotAvail ana) {
		if (ana == null)
			throw new NullPointerException();
		if (isSameNotAvailTime(ana))
			return 0;
		if (this.startDate.getTime() < ana.getStartDate().getTime()
				&& this.endDate.getTime() < ana.getEndDate().getTime())
			return -1;
		return 1;
	}

	public boolean isSameNotAvailTime(Object object) {
		if (object == null || !(object instanceof AssetNotAvail))
			return false;
		AssetNotAvail ana = (AssetNotAvail) object;
		if (ana.getAsset().equals(this.asset)
				&& ana.getEndDate().equals(this.endDate)
				&& ana.getStartDate().equals(this.startDate))
			return true;
		return false;
	}



}
