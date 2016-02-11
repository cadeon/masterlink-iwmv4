package com.mlink.iwm.entity.asset;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.Length;

/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="calEventId"))
@Table(name = "CalEvent")
public class CalEvent extends AbstractEntity{

	private Date endTime;
	private String name;
	private Date startTime;
	private AssetCalendar assetCalendar;

	public CalEvent() {
	}

	
	public CalEvent(Date endTime, String name, Date startTime,
		AssetCalendar assetCalendar) {

		this.endTime = endTime;
		this.name = name;
		this.startTime = startTime;
		this.assetCalendar = assetCalendar;
	}

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "endTime", length = 19)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "name", length = 50)
	@Length(max = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startTime", length = 19)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assetCalendarId")
	public AssetCalendar getAssetCalendar() {
		return this.assetCalendar;
	}

	public void setAssetCalendar(AssetCalendar assetCalendar) {
		this.assetCalendar = assetCalendar;
	}

}
