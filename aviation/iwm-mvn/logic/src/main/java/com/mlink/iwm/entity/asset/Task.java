package com.mlink.iwm.entity.asset;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Length;

import com.mlink.iwm.entity.define.TaskTemplate;
import com.mlink.iwm.entity.factory.ExpiryType;
import com.mlink.iwm.entity.factory.FreqType;
import com.mlink.iwm.entity.factory.Priority;
import com.mlink.iwm.entity.factory.TaskType;
import com.mlink.iwm.entity.res.AssetResource;
import com.mlink.iwm.entity.res.NonAssetResource;

/**

 */

@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="taskId"))
@Table(name = "Task")
public class Task extends AbstractEntity{

	
	private String description;
	private int estTime;
	private int expiryDays;
	private ExpiryType expiryType;
	private int freqDays;
	private int freqMonths;
	private FreqType freqType;
	
	private boolean isActive;

	private Date lastPlannedDate;
	private Date lastServicedDate;
	
	private Priority priority;
	private Double runHoursThresh;
	private Double runHoursThreshInc;
	private Date startDate;
	private String taskCode; //inherited from template
	

	private TaskType taskType;
	private boolean isStandAlone; 
	//if any property value is updated from the inherited default template's 
	//then set isStandAlone to true and taskTemplate =null
	
	//assumes all assets are instances of the SAME template
	private Set<Asset> maintainedAssets; 
	
	private TaskTemplate taskTemplate;
	//copied from their template
	private Set<Action> actions = new HashSet<Action>(0);
	private Set<AssetResource> assetResources = new HashSet<AssetResource>(0);;
	private Set<NonAssetResource> nonAssetResources = new HashSet<NonAssetResource>(0);;
	
	
	//required resources are defined at the template level 
	//concrete instances of those are assigned by the planner according
	//to asset availibility.

	//or explicitly assigned manually if the task is modified
	//or created as stand alone
	//Enforce isStandAlone is true if below not null
	private Set<Asset> stickyAssetResources = new HashSet<Asset>(0) ;
	
	public Task() {
	}

	
	public Task( String description,
			int estTime, int expiryDays, ExpiryType expiryType,
			int freqDays, int freqMonths, FreqType freqType,
			boolean isActive, boolean isStandAlone, Date lastPlannedDate,
			Date lastServicedDate, Priority priority,
			Set<AssetResource> ar, String tc,
			Set<NonAssetResource> nar,
			Double runHoursThresh, Double runHoursThreshInc, Date startDate, TaskType taskType,
			 Set<Asset> sar, Set<Action> actions, Set<Asset> maintainedAssets) {

		this.description = description;
		this.estTime = estTime;
		this.expiryDays = expiryDays;
		this.expiryType = expiryType;
		this.freqDays = freqDays;
		this.freqMonths = freqMonths;
		this.freqType = freqType;
		this.isActive = isActive;
		this.isStandAlone = isStandAlone;
		this.lastPlannedDate = lastPlannedDate;
		this.lastServicedDate = lastServicedDate;
		this.priority = priority;
		this.taskCode=tc;
		this.runHoursThresh = runHoursThresh;
		this.runHoursThreshInc = runHoursThreshInc;
		this.startDate = startDate;
		this.stickyAssetResources = sar;
		this.assetResources= ar;
		this.nonAssetResources=nar;
        this.maintainedAssets= maintainedAssets;		
		this.actions = actions;
	}

	
	

	@Column(name = "description", length = 50)
	@Length(max = 50)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "estTime")
	public int getEstTime() {
		return this.estTime;
	}

	public void setEstTime(int estTime) {
		this.estTime = estTime;
	}

	@Column(name = "expiryDays")
	public int getExpiryDays() {
		return this.expiryDays;
	}

	public void setExpiryDays(int expiryDays) {
		this.expiryDays = expiryDays;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "ExpiryType",  nullable = false)
	public ExpiryType getExpiryType() {
		return this.expiryType;
	}

	public void setExpiryType(ExpiryType expiryType) {
		this.expiryType = expiryType;
	}

	@Column(name = "freqDays")
	public int getFreqDays() {
		return this.freqDays;
	}

	public void setFreqDays(int freqDays) {
		this.freqDays = freqDays;
	}

	@Column(name = "freqMonths")
	public int getFreqMonths() {
		return this.freqMonths;
	}

	public void setFreqMonths(int freqMonths) {
		this.freqMonths = freqMonths;
	}

	@Column(name = "taskCode")
	public String getTaskCode() {
		return this.taskCode;
	}

	public void setTaskCode(String tc) {
		this.taskCode = tc;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "freqType",  nullable = false)
	public FreqType getFreqType() {
		return this.freqType;
	}

	public void setFreqType(FreqType freqType) {
		this.freqType = freqType;
	}

	@Column(name = "isActive")
	public boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Column(name = "isStandAlone")
	public boolean getIsStandAlone() {
		return this.isStandAlone;
	}

	public void setIsStandAlone(boolean isStandAlone) {
		this.isStandAlone = isStandAlone;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastPlannedDate", length = 19)
	public Date getLastPlannedDate() {
		return this.lastPlannedDate;
	}

	public void setLastPlannedDate(Date lastPlannedDate) {
		this.lastPlannedDate = lastPlannedDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastServicedDate", length = 19)
	public Date getLastServicedDate() {
		return this.lastServicedDate;
	}

	public void setLastServicedDate(Date lastServicedDate) {
		this.lastServicedDate = lastServicedDate;
	}

	
	@Enumerated(EnumType.STRING)
	@Column(name = "priority",  nullable = false)
	public Priority getPriority() {
		return this.priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	@Column(name = "runHoursThresh")
	public Double getRunHoursThresh() {
		return this.runHoursThresh;
	}

	public void setRunHoursThresh(Double runHoursThresh) {
		this.runHoursThresh = runHoursThresh;
	}

	@Column(name = "runHoursThreshInc")
	public Double getRunHoursThreshInc() {
		return this.runHoursThreshInc;
	}

	public void setRunHoursThreshInc(Double runHoursThreshInc) {
		this.runHoursThreshInc = runHoursThreshInc;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startDate", length = 19)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "stickyToTask")
	public Set<Asset> getStickyAssets() {
		return this.stickyAssetResources;
	}

	public void setStickyAssets(Set<Asset> assets) {
		this.stickyAssetResources = assets;
	}

	@ManyToMany(mappedBy = "maintTasks")
	public Set<Asset> getMaintainedAssets() {
		return this.maintainedAssets;
	}

	public void setMaintainedAssets(Set<Asset> ma) {
		this.maintainedAssets= ma ;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
	public Set<Action> getActions() {
		return this.actions;
	}

	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}
	
	public void setTaskTemplate(TaskTemplate taskTemplate) {
		this.taskTemplate = taskTemplate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "taskTemplateId")
	public TaskTemplate getTaskTemplate() {
		return taskTemplate;
	}

	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "taskType",  nullable = false)
	public TaskType getTaskType() {
		return this.taskType;
	}

	public void setTaskType(TaskType tt) {
		this.taskType = tt;
	}
	
	public void setAssetResources(Set<AssetResource> assetResources) {
		this.assetResources = assetResources;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
	public Set<AssetResource> getAssetResources() {
		return assetResources;
	}

	public void setNonAssetResources(Set<NonAssetResource> nonAssetResources) {
		this.nonAssetResources = nonAssetResources;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
	public Set<NonAssetResource> getNonAssetResources() {
		return nonAssetResources;
	}
	@Transient
	public List<NonAssetResource> getNonAssetResourceList() {
		return new ArrayList<NonAssetResource>(nonAssetResources);
	}
}