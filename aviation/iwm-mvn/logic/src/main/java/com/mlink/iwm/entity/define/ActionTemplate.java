package com.mlink.iwm.entity.define;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import com.mlink.iwm.entity.define.TaskTemplate;

/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="actionTemplateId"))
@Table(name = "ActionTemplate")
public class ActionTemplate extends AbstractEntity{

	private TaskTemplate taskTemplate;
	private String modifier;
	private String name;
	private int sequence;
	private String verb;

	public ActionTemplate() {
	}


	public ActionTemplate(TaskTemplate tasktemplate,
			String modifier, String name, int sequence, String verb) {
	
		this.taskTemplate = tasktemplate;
		this.modifier = modifier;
		this.name = name;
		this.sequence = sequence;
		this.verb = verb;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "taskTemplateId")
	public TaskTemplate getTaskTemplate() {
		return this.taskTemplate;
	}

	public void setTaskTemplate(TaskTemplate taskTemplate) {
		this.taskTemplate = taskTemplate;
	}
	
	@Column(name = "modifier", length = 45)
	@Length(max = 45)
	public String getModifier() {
		return this.modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	@Column(name = "name", nullable = false, length = 45)
	@NotNull
	@Length(max = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "sequence")
	public int getSequence() {
		return this.sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	@Column(name = "verb", nullable = false, length = 45)
	@NotNull
	@Length(max = 45)
	public String getVerb() {
		return this.verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

}
