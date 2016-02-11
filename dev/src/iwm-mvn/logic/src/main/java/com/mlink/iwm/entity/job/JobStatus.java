package com.mlink.iwm.entity.job;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.mlink.iwm.entity.AbstractEntity;



/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="jobStatusId"))
@Table(name = "JobStatus")
public class JobStatus extends AbstractEntity{
	
	com.mlink.iwm.entity.factory.JobStatus status;
	Date date;
	private Job job;
	
    @Enumerated(EnumType.STRING)
	@Column(name = "status")
	public com.mlink.iwm.entity.factory.JobStatus getStatus() {
		return status;
	}
	public void setStatus(com.mlink.iwm.entity.factory.JobStatus jobStat) {
		this.status = jobStat;
	}
	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date", length = 10)
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jobId")
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

}
