package com.mlink.iwm.session.job;

import com.mlink.iwm.entity.job.*;
import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.define.TaskTemplate;
import com.mlink.iwm.session.asset.TaskHome;
import com.mlink.iwm.session.define.TaskTemplateHome;

import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@SuppressWarnings("serial")
@Name("taskInstanceHome")
public class TaskInstanceHome extends EntityHome<TaskInstance> {

	@In(create = true)
	TaskHome taskHome;
	@In(create = true)
	TaskTemplateHome taskTemplateHome;
	@In(create = true)
	JobHome jobHome;

	public void setTaskInstanceId(Long id) {
		setId(id);
	}

	public Long getTaskInstanceId() {
		return (Long) getId();
	}

	@Override
	protected TaskInstance createInstance() {
		TaskInstance taskInstance = new TaskInstance();
		return taskInstance;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Task task = taskHome.getDefinedInstance();
		if (task != null) {
			getInstance().setTask(task);
		}
		TaskTemplate taskTemplate = taskTemplateHome.getDefinedInstance();
		if (taskTemplate != null) {
			getInstance().setTaskTemplate(taskTemplate);
		}
		Job job = jobHome.getDefinedInstance();
		if (job != null) {
			getInstance().setJob(job);
		}
	}

	public boolean isWired() {
		return true;
	}

	public TaskInstance getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<JobTask> getJobTasks() {
		return getInstance() == null ? null : new ArrayList<JobTask>(
				getInstance().getJobTasks());
	}
	
	public String delete(TaskInstance taskInstance) {
		setInstance(taskInstance);
		getInstance();
		return remove();
	}
	
}
