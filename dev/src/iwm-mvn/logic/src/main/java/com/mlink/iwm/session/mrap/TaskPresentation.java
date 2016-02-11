package com.mlink.iwm.session.mrap;

import com.mlink.iwm.entity.asset.Task;

public class TaskPresentation {

	private Long id;
	private String description;
	private String taskCode;
	private int estTime;

	public TaskPresentation(Task task) {
		this.id = task.getId();
		this.description = task.getDescription();
		this.taskCode = task.getTaskCode();
		this.estTime = task.getEstTime();
	}
	
	public void setTaskId(Long l) {
		id = l;
	}
	public Long getTaskId() { 
		return id; 
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setEstTime(int estTime) {
		this.estTime = estTime;
	}

	public int getEstTime() {
		return estTime;
	}
}
