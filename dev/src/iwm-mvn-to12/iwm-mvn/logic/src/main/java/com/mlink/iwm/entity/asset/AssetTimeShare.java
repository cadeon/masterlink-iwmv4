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

import com.mlink.iwm.entity.job.Job;

/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="assetTimeShareId"))
@Table(name = "AssetTimeShare")
public class AssetTimeShare extends AbstractEntity{

	
	private boolean asResource;
	private boolean isAssigned;
	private Date endDate;
	private Date startDate;
	private Asset asset;
	private Job jobAllocatedTo; //as a resource

	public AssetTimeShare() {
	}

	
	public AssetTimeShare(boolean asResource,
			boolean isAssigned, Date endDate, Date startDate, Job jobAllocatedTo, Asset asset) {
	
		this.asResource = asResource;
		this.isAssigned = isAssigned;
		this.endDate = endDate;
		this.setJobAllocatedTo(jobAllocatedTo);
		this.startDate = startDate;
		this.asset = asset;
	}



	@Column(name = "asResource")
	public boolean getAsResource() {
		return this.asResource;
	}

	public void setAsResource(boolean asResource) {
		this.asResource = asResource;
	}

	@Column(name = "isAssigned")
	public boolean getAssigned() {
		return this.isAssigned;
	}

	public void setAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
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

	public void setJobAllocatedTo(Job jobAllocatedTo) {
		this.jobAllocatedTo = jobAllocatedTo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jobId")
	public Job getJobAllocatedTo() {
		return jobAllocatedTo;
	}

}
