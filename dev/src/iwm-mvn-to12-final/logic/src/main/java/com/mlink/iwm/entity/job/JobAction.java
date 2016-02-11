package com.mlink.iwm.entity.job;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.Length;

import com.mlink.iwm.entity.asset.Action;

//equivalent to WorkItemStep
//created by the scheduler

/**

 */
@SuppressWarnings("serial")
@Entity 
@AttributeOverride( name="id", column = @Column(name="jobActionId"))
@Table(name = "JobAction")
public class JobAction extends AbstractEntity{

	private String note;
	private String fieldCondition;
	private String modifier;
	private String name;
	private String sequence;
	private String verb;
	private JobTask jobTask;
	private Action action;

	public JobAction() {
	}

	public JobAction(JobTask jobTask, String note, String fieldCondition,
			String modifier, String name, String sequence, String verb,
			Action action) {
		this.jobTask = jobTask;
		this.note = note;
		this.fieldCondition = fieldCondition;
		this.modifier = modifier;
		this.name = name;
		this.sequence = sequence;
		this.verb = verb;
		this.action = action;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jobTaskId")
	public JobTask getJobTask() {
		return jobTask;
	}

	public void setJobTask(JobTask jobTask) {
		this.jobTask = jobTask;
	}

	@Column(name = "note", length = 50)
	@Length(max = 125)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setFieldCondition(String fieldCondition) {
		this.fieldCondition = fieldCondition;
	}
	@Column(name = "fieldCondition", length = 25)
	public String getFieldCondition() {
		return fieldCondition;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	@Column(name = "modifier", length = 25)
	public String getModifier() {
		return modifier;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return name;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	@Column(name = "seq", length = 5)
	public String getSequence() {
		return sequence;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	@Column(name = "verb", length = 50)
	public String getVerb() {
		return verb;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actionId")
	public Action getAction() {
		return action;
	}

}
