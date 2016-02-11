package com.mlink.iwm.entity.job;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.Length;

import com.mlink.iwm.entity.asset.AssetTimeShare;
import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.contact.Organization;
import com.mlink.iwm.entity.factory.JobStatus;
import com.mlink.iwm.entity.factory.JobType;
import com.mlink.iwm.entity.factory.Priority;
import javax.persistence.AttributeOverride;  
import com.mlink.iwm.entity.AbstractEntity;
import com.mlink.iwm.entity.factory.EomType;
import com.mlink.iwm.entity.factory.JobCat;
import com.mlink.iwm.entity.res.AssetResource;
import com.mlink.iwm.entity.res.NonAssetResource;
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="jobId"))
@Table(name = "Job")
public class Job extends AbstractEntity{

	private Date completedDate;
	private String createdBy;
	private Date createdDate;
	private String description;
	private Date dispatchedDate;
	private Date earliestStart;
	private int estTime; //this is copied from the selected task
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
	private TaskInstance taskInstance;
	
	//booked to this job
	private Set<AssetTimeShare> assetTimeShares = new HashSet<AssetTimeShare>(0);
	
	private Set<JobTask> jobTasks = new HashSet<JobTask>(0);
	
	//copied from the selected Asset->task->TaskTemplate resources
	//what's needed to accomplish the taskinstance corresponding to this job
	private Set<AssetResource> assetResources = new HashSet<AssetResource>(0);;
	private Set<NonAssetResource> nonAssetResources = new HashSet<NonAssetResource>(0);;
	
	
	//task requested by?
	private Set<TenantRequest> tenantRequests = new HashSet<TenantRequest>(0);

	
	//-------MRAP specific
	//TODO refactor
	private String ero; //new equipment repair order
	private JobCat cat; //new
	private Date receivedInShopDate; //new when the asset showed up at the maint loc
	@Transient
	private int daysInShop; //new calculated: today - receivedInShopDate
	private Contact contact; //new POC for this job
	private Date completeBy;  
	private EomType eom;
	@Transient 
	private Double totatEstTime; //rollup of all jobTasks esttime

	
	

	public Job() {
	}

	public Job(
			Date completedDate, String createdBy, Date createdDate,
			String description, Date dispatchedDate, Date earliestStart,
			int estTime, JobType jobType,
			Date latestStart,  String note,
			Organization organization,
			Priority priority, Date scheduledDate,
			Date startedDate,
			TaskInstance ti,
			JobStatus status, boolean sticky, 
			Set<AssetTimeShare> assetTimeShares,
			Set<JobTask> jobTasks,
			Set<AssetResource> ar,
			Set<NonAssetResource> nar,
			Set<TenantRequest> tenantRequests) {

		this.completedDate = completedDate;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.description = description;
		taskInstance=ti;
		this.dispatchedDate = dispatchedDate;
		this.earliestStart = earliestStart;
		this.estTime = estTime;
		
		this.jobType = jobType;
		this.latestStart = latestStart;

		this.note = note;
		assetResources= ar;
		nonAssetResources=nar;
		setOrganization(organization);
		setPriority(priority);
		this.scheduledDate = scheduledDate;
	
		this.startedDate = startedDate;
		this.status = status;
		this.sticky = sticky;
		this.assetTimeShares = assetTimeShares;
		this.jobTasks = jobTasks;

		this.tenantRequests = tenantRequests;
	}

	

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
	public Set<JobTask> getJobTasks() {
		return jobTasks;
	}

	public void setJobTasks(Set<JobTask> jobTasks) {
		this.jobTasks = jobTasks;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "completedDate", length = 10)
	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	@Column(name = "createdBy", length = 50)
	@Length(max = 50)
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "createdDate", length = 10)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "description", length = 50)
	@Length(max = 50)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dispatchedDate", length = 10)
	public Date getDispatchedDate() {
		return dispatchedDate;
	}

	public void setDispatchedDate(Date dispatchedDate) {
		this.dispatchedDate = dispatchedDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "earliestStart", length = 10)
	public Date getEarliestStart() {
		return earliestStart;
	}

	public void setEarliestStart(Date earliestStart) {
		this.earliestStart = earliestStart;
	}

	@Column(name = "estTime")
	public int getEstTime() {
		return estTime;
	}

	public void setEstTime(int estTime) {
		this.estTime = estTime;
	}


	
    @Enumerated(EnumType.STRING)
	@Column(name = "jobType")
	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "latestStart", length = 10)
	public Date getLatestStart() {
		return latestStart;
	}

	public void setLatestStart(Date latestStart) {
		this.latestStart = latestStart;
	}

	

	@Column(name = "note", length = 125)
	@Length(max = 500)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orgId")
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization org) {
		organization = org;
	}

	
    @Enumerated(EnumType.STRING)
	@Column(name = "priority")
	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "scheduledDate", length = 10)
	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	

	@Temporal(TemporalType.DATE)
	@Column(name = "startedDate", length = 10)
	public Date getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(Date startedDate) {
		this.startedDate = startedDate;
	}

	
    @Enumerated(EnumType.STRING)
	@Column(name = "status")
	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	@Column(name = "sticky")
	public boolean getSticky() {
		return sticky;
	}

	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
	public Set<TenantRequest> getTenantRequests() {
		return tenantRequests;
	}

	public void setTenantRequests(Set<TenantRequest> tenantRequests) {
		this.tenantRequests = tenantRequests;
	}

	public void setAssetTimeShares(Set<AssetTimeShare> assetTimeShares) {
		this.assetTimeShares = assetTimeShares;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "jobAllocatedTo")
	public Set<AssetTimeShare> getAssetTimeShares() {
		return assetTimeShares;
	}

	@OneToOne
	@PrimaryKeyJoinColumn
	public TaskInstance getTaskInstance() {
		return taskInstance;
	}

	public void setTaskInstance(TaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}
	
	public void setCat(JobCat cat) {
		this.cat = cat;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "jobCat")
	public JobCat getCat() {
		return cat;
	}

	public void setDaysInShop(int daysInShop) {
		this.daysInShop = daysInShop;
	}

	@Column(name = "dis")
	public int getDaysInShop() {
		return daysInShop;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	@OneToOne
	@PrimaryKeyJoinColumn
	public Contact getContact() {
		return contact;
	}

	public void setEro(String ero) {
		this.ero = ero;
	}


	@Column(name = "ero")
	public String getEro() {
		return ero;
	}

	public void setCompleteBy(Date completeBy) {
		this.completeBy = completeBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "combleteBy", length = 10)
	public Date getCompleteBy() {
		return completeBy;
	}

	public void setEom(EomType eom) {
		this.eom = eom;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "eom")
	public EomType getEom() {
		return eom;
	}

	public void setReceivedInShopDate(Date receivedInShopDate) {
		this.receivedInShopDate = receivedInShopDate;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "risd", length = 10)
	public Date getReceivedInShopDate() {
		return receivedInShopDate;
	}

	public void setTotatEstTime(Double totatEstTime) {
		this.totatEstTime = totatEstTime;
	}

	public Double getTotatEstTime() {
		return totatEstTime;
	}
	
	public void setNonAssetResources(Set<NonAssetResource> nonAssetResources) {
		this.nonAssetResources = nonAssetResources;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy ="job")
	public Set<NonAssetResource> getNonAssetResources() {
		return nonAssetResources;
	}

	public void setAssetResources(Set<AssetResource> assetResources) {
		this.assetResources = assetResources;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
	public Set<AssetResource> getAssetResources() {
		return assetResources;
	}

	

}

