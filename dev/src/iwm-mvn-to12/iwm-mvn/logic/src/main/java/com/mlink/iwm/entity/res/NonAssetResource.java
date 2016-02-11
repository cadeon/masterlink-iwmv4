package com.mlink.iwm.entity.res;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mlink.iwm.entity.AbstractEntity;
import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.define.TaskTemplate;
import com.mlink.iwm.entity.factory.NonAssetType;
import com.mlink.iwm.entity.job.Job;

/**

 */
@SuppressWarnings("serial")

@Entity  
@AttributeOverride( name="id", column = @Column(name=" nonAssetResourceId"))
@Table(name = "NonAssetResource")
public class NonAssetResource extends AbstractEntity{

	private int quantity;
	private long unitRefId;
	private NonAssetType type;
	private String tag;
	private String desc;
	//below are obtained via the MEM/GCSS interface
    private String status;
    private String recDateStr;
    private String currentLoc;

	private TaskTemplate taskTemplate;
	private Task task;   //asset level
	private Job job; //job level

	
	public NonAssetResource() {
	}

	public NonAssetResource(int quantity, long unitRefId, NonAssetType type,
			String tag, String desc, TaskTemplate taskTemplate, Task task,
			Job job) {
		this.quantity = quantity;
		this.unitRefId = unitRefId;
		this.type = type;
		this.tag = tag;
		this.desc = desc;
		this.taskTemplate = taskTemplate;
		this.task = task;
		this.job = job;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Column(name = "quantity")
	public int getQuantity() {
		return quantity;
	}


	public void setUnitRefId(long unitRefId) {
		this.unitRefId = unitRefId;
	}
	
	@Column(name = "unitRefId")
	public long getUnitRefId() {
		return unitRefId;
	}


	public void setTag(String tag) {
		this.tag = tag;
	}

	@Column(name = "tag")
	public String getTag() {
		return tag;
	}
	
	public void setDesc(String d) {
		this.desc = d;
	}

	@Column(name = "description")
	public String getDesc() {
		return desc;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setRecDateStr(String recDateStr) {
		this.recDateStr = recDateStr;
	}

	public String getRecDateStr() {
		return recDateStr;
	}

	public void setCurrentLoc(String currentLoc) {
		this.currentLoc = currentLoc;
	}

	public String getCurrentLoc() {
		return currentLoc;
	}
	
	@Enumerated(EnumType.STRING)
	public NonAssetType getType() {
		return type;
	}


	public void setType(NonAssetType type) {
		this.type = type;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "taskTemplateId")
	public TaskTemplate getTaskTemplate() {
		return this.taskTemplate;
	}

	public void setTaskTemplate(TaskTemplate taskTemplate) {
		this.taskTemplate = taskTemplate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "taskId")
	public Task getTask() {
		return this.task;
	}

	



	public void setTask(Task task) {
		this.task = task;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jobId")
	public Job getJob() {
		return this.job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
}
