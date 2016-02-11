package com.mlink.iwm.entity.res;
import javax.persistence.AttributeOverride;  
import com.mlink.iwm.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.define.AssetTemplate;
import com.mlink.iwm.entity.define.TaskTemplate;
import com.mlink.iwm.entity.job.Job;

/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="assetResourceId"))
@Table(name = "AssetResource")
public class AssetResource extends AbstractEntity{

	
	private int quantity;
	private AssetTemplate assetTemplate; 
	private Asset stickyAsset; //ex scenario: Same asset need to be re-used to finish a job 
	private boolean isManualAssign; //if true the user is displayed a list of assets to select from (enforce assetTemplate is null. 
	private TaskTemplate taskTemplate;  //template level
	private Task task;   //asset level
	private Job job;  //job level

	public AssetResource() {
	}

	

	public AssetResource(int quantity, AssetTemplate assetTemplate,
			Asset stickyAsset, boolean isManualAssign,
			TaskTemplate taskTemplate, Task task, Job job) {

		this.quantity = quantity;
		this.assetTemplate = assetTemplate;
		this.stickyAsset = stickyAsset;
		this.isManualAssign = isManualAssign;
		this.taskTemplate = taskTemplate;
		this.task = task;
		this.job = job;
	}




	public void setAssetTemplate(AssetTemplate assetTemplate) {
		this.assetTemplate = assetTemplate;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Column(name = "quantity")
	public int getQuantity() {
		return quantity;
	}

	
	@OneToOne
	@JoinColumn(name = "assetTemplateId")
	public AssetTemplate getAssetTemplate() {
		return assetTemplate;
	}

	public void setStickyAsset(Asset asset) {
		this.stickyAsset = asset;
	}

	@OneToOne
	@JoinColumn(name = "stickyAssetId")
	public Asset getStickyAsset() {
		return this.stickyAsset;
	}

	public void setIsManualAssign(boolean isManualAssign) {
		this.isManualAssign = isManualAssign;
	}

	@Column(name = "isManualAssign")
	public boolean getIsManualAssign() {
		return isManualAssign;
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
