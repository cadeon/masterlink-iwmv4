package com.mlink.iwm.entity.job;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.define.TaskTemplate;

/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="taskInstanceId"))
@Table(name = "TaskInstance")
public class TaskInstance extends AbstractEntity{

	private int estTime;
	private Set<JobTask> jobTasks = new HashSet<JobTask>(0);
	private boolean isStandAloneTask; //if true then enforce taskTemplate=null
	private Task task; //standalone 
	private TaskTemplate taskTemplate;
	//0..1 association
	private Asset maintainedAsset;
	private Job job;
	
	public TaskInstance() {
	}

	

	@Column(name ="estTime", nullable = false)
	public int getEstTime() {
		return estTime;
	}

	public void setEstTime(int estTime) {
		this.estTime = estTime;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy="taskInstance")
	public Set<JobTask> getJobTasks() {
		return jobTasks;
	}

	public void setJobTasks(Set<JobTask> jobTasks) {
		this.jobTasks = jobTasks;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@OneToOne
	@JoinColumn(name = "taskId")
	public Task getTask() {
		return task;
	}

	public void setTaskTemplate(TaskTemplate taskTemplate) {
		this.taskTemplate = taskTemplate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "taskTemplateId")
	public TaskTemplate getTaskTemplate() {
		return taskTemplate;
	}

	public void setIsStandAloneTask(boolean isStandAloneTask) {
		this.isStandAloneTask = isStandAloneTask;
	}

	public boolean getIsStandAloneTask() {
		return isStandAloneTask;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	@OneToOne(mappedBy="taskInstance") 
	public Job getJob() {
		return job;
	}
	
	
	@OneToOne
	@PrimaryKeyJoinColumn
	public Asset getMaintainedAsset() {
		return maintainedAsset;
	}

	public void setMaintainedAsset(Asset asset) {
		maintainedAsset = asset;
	}
	

}
