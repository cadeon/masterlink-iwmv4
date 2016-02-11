package com.mlink.iwm.entity.asset;

import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="assetCalendarId"))
@Table(name = "AssetCalendar")
public class AssetCalendar extends AbstractEntity{
	private int day;
	private int month;
	private int year;
	private Asset asset;

	public AssetCalendar() {
	}

	
	public AssetCalendar(int day, int month,
			int year, Asset asset) {

		this.day = day;
		this.month = month;
		this.year = year;
		this.asset = asset;
	}



	@Column(name = "day")
	public int getDay() {
		return this.day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	@Column(name = "month")
	public int getMonth() {
		return this.month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	@Column(name = "year")
	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assetId")
	public Asset getAsset() {
		return this.asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

}
