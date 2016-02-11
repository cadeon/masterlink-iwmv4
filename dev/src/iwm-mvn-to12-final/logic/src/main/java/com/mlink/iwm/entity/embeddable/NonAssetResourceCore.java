package com.mlink.iwm.entity.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.define.TaskTemplate;
import com.mlink.iwm.entity.factory.NonAssetType;


@Embeddable
public class NonAssetResourceCore {

	private int quantity;
	private long unitRefId;
	private NonAssetType type;
	private String tag;
	private String desc;
	
	private TaskTemplate taskTemplate;
	private Task task;   //asset level

	
	public NonAssetResourceCore() {
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

	@Column(name = "serial")
	public String getTag() {
		return tag;
	}
	
	public void setDesc(String d) {
		this.desc = d;
	}

	@Column(name = "desc")
	public String getDesc() {
		return desc;
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
	@JoinColumn(name = "taskID")
	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
