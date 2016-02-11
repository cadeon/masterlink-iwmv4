package com.mlink.iwm.entity.job;
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
import org.hibernate.validator.Length;


//equivalent to WorkItem
//created by the schedular

/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="jobTaskId"))
@Table(name = "JobTask")
public class JobTask extends AbstractEntity{

	private TaskInstance taskInstance;
	private String description;
	private int estTime;
	private String taskCode;
	
    //this is recorded by the worker	
	private int actualTime; 
	private Job job;
	private Set<JobAction> jobActions = new HashSet<JobAction>(0);

	public JobTask() {
	}

	public JobTask(TaskInstance taskInstance, Job job, String tc, String description,
			int estTime, Set<JobAction> jobActions) {
		this.taskInstance = taskInstance;
		this.description = description;
		taskCode=tc;
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
	
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jobId")
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public void setJobActions(Set<JobAction> jobActions) {
		this.jobActions = jobActions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "jobTask")
	public Set<JobAction> getJobActions() {
		return jobActions;
	}

}
