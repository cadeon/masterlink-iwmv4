package com.mlink.iwm.entity.job;
import javax.persistence.AttributeOverride;  
import com.mlink.iwm.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.Length;

import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.factory.JobStatus;
import com.mlink.iwm.entity.factory.RequestType;

/**

 * 
 */

//delete for MRAP
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="tenantRequestId"))
@Table(name = "TenantRequest")
public class TenantRequest extends AbstractEntity{

	private Job job;
	private Contact contact;
	private boolean isUrgent;
	private String jobDescription;
	private JobStatus jobStatus;
	private String locationComment;
	private String note;
	private RequestType requestType;

	public TenantRequest() {
	}

	public TenantRequest(Job job, 
			Contact contact,
			boolean isUrgent, String jobDescription,
			JobStatus jobStatus, String locationComment, 
			String note, String problemId, RequestType requestType) {
		this.job = job;


		setContact(contact);
		this.isUrgent = isUrgent;
		this.jobDescription = jobDescription;
		this.jobStatus = jobStatus;
		this.locationComment = locationComment;
		
		this.note = note;
		this.requestType = requestType;

	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jobId")
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	

	@Column(name = "isUrgent")
	public boolean getIsUrgent() {
		return isUrgent;
	}

	public void setIsUrgent(boolean isUrgent) {
		this.isUrgent = isUrgent;
	}

	@Column(name = "jobDescription", length = 125)
	@Length(max = 500)
	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "jobstatus", nullable=false)
	public JobStatus getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus= jobStatus;
	}

	@Column(name = "locationComment", length = 50)
	@Length(max = 50)
	public String getLocationComment() {
		return locationComment;
	}

	public void setLocationComment(String locationComment) {
		this.locationComment = locationComment;
	}



	@Column(name = "note", length = 120)
	@Length(max = 500)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "requestType", nullable=false)
	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactId")
	public Contact getContact() {
		return contact;
	}




}
