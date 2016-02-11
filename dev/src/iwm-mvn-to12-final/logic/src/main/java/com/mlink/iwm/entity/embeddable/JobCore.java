package com.mlink.iwm.entity.embeddable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Length;

import com.mlink.iwm.entity.contact.Organization;
import com.mlink.iwm.entity.factory.JobStatus;
import com.mlink.iwm.entity.factory.JobType;
import com.mlink.iwm.entity.factory.Priority;


@Embeddable
public class JobCore {

	private Date completedDate;
	private String createdBy;
	private Date createdDate;
	private String description;
	private Date dispatchedDate;
	private Date earliestStart;
	private int estTime;
	private JobType jobType; //refer to enums
	private Date latestStart; //grace period before the job is canceled
	private String note;
	
	private Organization organization;
	private Priority priority;
	private Date scheduledDate;
	private Date startedDate;
	private JobStatus status;
	private boolean sticky;   // for manually created jobs all assets 
							  // are automatically considered sticky (to this job)  

	public JobCore() {
	}

	
	@Temporal(TemporalType.DATE)
	@Column(name = "completedDate", length = 10)
	public Date getCompletedDate() {
		return this.completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	@Column(name = "createdBy", length = 50)
	@Length(max = 50)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "createdDate", length = 10)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "description", length = 50)
	@Length(max = 50)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dispatchedDate", length = 10)
	public Date getDispatchedDate() {
		return this.dispatchedDate;
	}

	public void setDispatchedDate(Date dispatchedDate) {
		this.dispatchedDate = dispatchedDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "earliestStart", length = 10)
	public Date getEarliestStart() {
		return this.earliestStart;
	}

	public void setEarliestStart(Date earliestStart) {
		this.earliestStart = earliestStart;
	}

	@Column(name = "estTime")
	public int getEstTime() {
		return this.estTime;
	}

	public void setEstTime(int estTime) {
		this.estTime = estTime;
	}


	
    @Enumerated(EnumType.STRING)
	@Column(name = "jobType")
	public JobType getJobType() {
		return this.jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "latestStart", length = 10)
	public Date getLatestStart() {
		return this.latestStart;
	}

	public void setLatestStart(Date latestStart) {
		this.latestStart = latestStart;
	}

	

	@Column(name = "note", length = 125)
	@Length(max = 500)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orgId")
	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization org) {
		this.organization = org;
	}

	
    @Enumerated(EnumType.STRING)
	@Column(name = "priority")
	public Priority getPriority() {
		return this.priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "scheduledDate", length = 10)
	public Date getScheduledDate() {
		return this.scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	

	@Temporal(TemporalType.DATE)
	@Column(name = "startedDate", length = 10)
	public Date getStartedDate() {
		return this.startedDate;
	}

	public void setStartedDate(Date startedDate) {
		this.startedDate = startedDate;
	}

	
    @Enumerated(EnumType.STRING)
	@Column(name = "status")
	public JobStatus getStatus() {
		return this.status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	@Column(name = "sticky")
	public boolean getSticky() {
		return this.sticky;
	}

	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}

	

}

