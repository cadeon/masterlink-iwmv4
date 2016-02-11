package com.mlink.iwm.entity.job;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.Length;

import com.mlink.iwm.entity.AbstractEntity;

//equivalent to WorkItem
//created by the schedular

/**

 */
@SuppressWarnings("serial")
@Entity
@AttributeOverride(name = "id", column = @Column(name = "jobTaskId"))
@Table(name = "JobTask")
public class JobTask extends AbstractEntity {

	private TaskInstance taskInstance;
	private String description;
	// copied from task
	private int estTime;
	private String taskCode;
	@Transient
	private transient boolean selected = false;
	@Transient
	private transient Long taskId;

	// this is recorded by the worker
	private int actualTime;
	private Job job;
	private List<JobAction> jobActions = new ArrayList<JobAction>(0);
	private String comments;

	public JobTask() {
	}

	public JobTask(TaskInstance taskInstance, Job job, String tc,
			String description, int estTime, List<JobAction> jobActions) {
		this.taskInstance = taskInstance;
		this.description = description;
		taskCode = tc;
		this.estTime = estTime;
		setJobActions(jobActions);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "taskInstanceId")
	public TaskInstance getTaskInstance() {
		return taskInstance;
	}

	public void setTaskInstance(TaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}

	@Column(name = "description", length = 50)
	@Length(max = 50)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "estTime")
	public int getEstTime() {
		return estTime;
	}

	public void setEstTime(int estTime) {
		this.estTime = estTime;
	}

	@Column(name = "taskCode")
	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String tc) {
		taskCode = tc;
	}

	public void setActualTime(int actualTime) {
		this.actualTime = actualTime;
	}

	@Column(name = "actualTime")
	public int getActualTime() {
		return actualTime;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "comments")
	public String getComments() {
		return comments;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jobId")
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public void setJobActions(List<JobAction> jobActions) {
		this.jobActions = jobActions;
	}

	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "jobTask")
	public List<JobAction> getJobActions() {
		return jobActions;
	}

	@Transient
	public boolean isSelected() {
		return selected;
	}

	@Transient
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Transient
	public Long getTaskId() { return taskId; }
	@Transient
	public void setTaskId(Long l) { taskId=l; }

}
