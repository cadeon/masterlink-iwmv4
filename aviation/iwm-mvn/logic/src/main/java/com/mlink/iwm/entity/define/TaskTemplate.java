package com.mlink.iwm.entity.define;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.mlink.iwm.entity.factory.ExpiryType;
import com.mlink.iwm.entity.factory.FreqType;
import com.mlink.iwm.entity.factory.Priority;
import com.mlink.iwm.entity.factory.TaskType;
import com.mlink.iwm.entity.res.AssetResource;
import com.mlink.iwm.entity.res.NonAssetResource;

import org.hibernate.validator.Length;

/**

 */

//single instance reference to instanciate a copy from for each
//concrete instance of an asset.

@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="taskTemplateId"))
@Table(name = "TaskTemplate")
public class TaskTemplate extends AbstractEntity{

	
	private AssetTemplate assetTemplate;
	
	private Set<ActionTemplate> actionTemplates = new HashSet<ActionTemplate>(0);
	private int estHours;
	private int estMin;
	private int expNumDays;
	private ExpiryType expiryType;
	private int estTime;
	private int freqDays;
	private int freqMonths;
	private FreqType freqType;
	private Priority priority;
	private Double runHoursThreshInc;
	private Double runHoursThresh;
	private String taskDescription;
	
	private String taskCode;
	
	private TaskType taskType;
	//what's needed to accomplish the task
	private Set<AssetResource> assetResources = new HashSet<AssetResource>(0);;
	private Set<NonAssetResource> nonAssetResources = new HashSet<NonAssetResource>(0);;
	
	
	public TaskTemplate() {
	}

	
	public TaskTemplate( AssetTemplate assetTemplate,
			int estTime, int estHours, int estMin, int expNumDays,
		ExpiryType expiryType, int freqDays, int freqMonths,
			FreqType freq, int numWorkers, Priority priority,
			Double runHoursThresh,
			Double runHoursThreshInc, Set<AssetResource> ar,
			Set<NonAssetResource> nar,
			String taskDescription, TaskType taskType, String taskCode,
			Set<ActionTemplate> actionTemplates) {

		this.assetTemplate = assetTemplate;
		this.estHours = estHours;
		this.estMin = estMin;
		this.expNumDays = expNumDays;
		this.expiryType = expiryType;
		this.freqDays = freqDays;
		this.estTime = estTime;
		this.freqMonths = freqMonths;
		this.freqType = freq;
		this.assetResources= ar;
		this.nonAssetResources=nar;
		this.priority = priority;
		this.taskCode=taskCode;
		this.runHoursThreshInc = runHoursThreshInc;
		this.runHoursThresh = runHoursThresh;
		this.taskDescription = taskDescription;
		this.taskType = taskType;
		
		this.actionTemplates = actionTemplates;
	}

	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assetTemplateId")
	public AssetTemplate getAssetTemplate() {
		return this.assetTemplate;
	}

	public void setAssetTemplate(AssetTemplate assetTemplate) {
		this.assetTemplate = assetTemplate;
	}

	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taskTemplate")
	public Set<ActionTemplate> getActionTemplates() {
		return this.actionTemplates;
	}

	public void setActionTemplates(Set<ActionTemplate> actionTemplates) {
		this.actionTemplates = actionTemplates;
	}
	
	@Column(name = "estTime")
	public int getEstTime() {
		return this.estTime;
	}

	public void setEstTime(int estTime) {
		this.estTime = estTime;
	}
	
	@Column(name = "estHours")
	public int getEstHours() {
		return this.estHours;
	}

	public void setEstHours(int estHours) {
		this.estHours = estHours;
	}

	@Column(name = "estMin")
	public int getEstMin() {
		return this.estMin;
	}
	
	public void setEstMin(int mn) {
		this.estMin = mn;
	}

	@Column(name = "taskCode")
	public String getTaskCode() {
		return this.taskCode;
	}

	public void setTaskCode(String tc) {
		this.taskCode = tc;
	}

	@Column(name = "expNumDays")
	public int getExpNumDays() {
		return this.expNumDays;
	}

	public void setExpNumDays(int expNumDays) {
		this.expNumDays = expNumDays;
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

	@Enumerated(EnumType.STRING)
	@Column(name = "freqType",  nullable = false)
	public FreqType getFreqType() {
		return this.freqType;
	}

	public void setFreqType(FreqType freqType) {
		this.freqType = freqType;
	}
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "priority",  nullable = false)
	public Priority getPriority() {
		return this.priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	@Column(name = "runHoursThreshInc", precision = 22, scale = 0)
	public Double getRunHoursThreshInc() {
		return this.runHoursThreshInc;
	}

	public void setRunHoursThreshInc(Double runHoursThreshInc) {
		this.runHoursThreshInc = runHoursThreshInc;
	}

	@Column(name = "runHoursThresh", precision = 22, scale = 0)
	public Double getRunHoursThresh() {
		return this.runHoursThresh;
	}

	public void setRunHoursThresh(Double v) {
		this.runHoursThresh = v;
	}

	
	

	@Column(name = "taskDescription", length = 45)
	@Length(max = 45)
	public String getTaskDescription() {
		return this.taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
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


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taskTemplate")
	public Set<AssetResource> getAssetResources() {
		return assetResources;
	}

	public void setNonAssetResources(Set<NonAssetResource> nonAssetResources) {
		this.nonAssetResources = nonAssetResources;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taskTemplate")
	public Set<NonAssetResource> getNonAssetResources() {
		return nonAssetResources;
	}

}
