package com.mlink.iwm.entity.job;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	private List<JobTask> jobTasks = new ArrayList<JobTask>(0);
	private boolean isStandAloneTask; //if true then enforce taskTemplate=null
	private Task task; //standalone 
	private TaskTemplate taskTemplate;

	private Job job;
	
	public TaskInstance() {
	}
	
	public TaskInstance(int estTime, List<JobTask> jobTasks,
			boolean isStandAloneTask, Task task, TaskTemplate taskTemplate,
			Job job) {
		super();
		this.estTime = estTime;
		this.jobTasks = jobTasks;
		this.isStandAloneTask = isStandAloneTask;
		this.task = task;
		this.taskTemplate = taskTemplate;

		this.job = job;
	}
	

	@Column(name ="estTime", nullable = false)
	public int getEstTime() {
		return estTime;
	}

	public void setEstTime(int estTime) {
		this.estTime = estTime;
	}

	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY, mappedBy="taskInstance")
	public List<JobTask> getJobTasks() {
		return jobTasks;
	}

	public void setJobTasks(List<JobTask> jobTasks) {
		this.jobTasks = jobTasks;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@OneToOne( cascade=CascadeType.ALL)
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

	@OneToOne(cascade=CascadeType.ALL, mappedBy="taskInstance") 
	public Job getJob() {
		return job;
	}
	
	

	

}
