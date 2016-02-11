package com.mlink.iwm.entity.asset;
import javax.persistence.AttributeOverride;  
import com.mlink.iwm.entity.AbstractEntity;
import com.mlink.iwm.entity.define.ActionTemplate;




import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.Length;

/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="actionId"))
@Table(name = "Action")
public class Action extends AbstractEntity{


	private Task task;
	
	private boolean isCustom;
	private String modifier;
	private String name;
	private int sequence;
	private String verb;
    
	private ActionTemplate actionTemplate;
	


	public Action() {
	}

	
	public Action(Task task,  boolean isCustom,
			String modifier, String name, int sequence, String verb,
			ActionTemplate at) {
		this.task = task;
		
		this.isCustom = isCustom;
		this.modifier = modifier;
		this.name = name;
		this.sequence = sequence;
		this.verb = verb;
		this.actionTemplate = at;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "taskId")
	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}



	@Column(name = "isCustom")
	public boolean getIsCustom() {
		return this.isCustom;
	}

	public void setIsCustom(boolean isCustom) {
		this.isCustom = isCustom;
	}

	@Column(name = "modifier", length = 50)
	@Length(max = 50)
	public String getModifier() {
		return this.modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	@Column(name = "name", length = 50)
	@Length(max = 50)
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

	@Column(name = "verb", length = 50)
	@Length(max = 50)
	public String getVerb() {
		return this.verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actionTemplateId")
	public ActionTemplate getActionTemplate() {
		return this.actionTemplate;
	}

	public void setActionTemplate(ActionTemplate at) {
		this.actionTemplate = at;
	}


}
