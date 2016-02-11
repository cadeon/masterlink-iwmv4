package com.mlink.iwm.session.job;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.AssetTimeShare;
import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.contact.Organization;
import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.entity.job.JobTask;
import com.mlink.iwm.entity.job.TaskInstance;
import com.mlink.iwm.entity.job.TenantRequest;
import com.mlink.iwm.entity.res.AssetResource;
import com.mlink.iwm.entity.res.NonAssetResource;
import com.mlink.iwm.session.contact.ContactHome;
import com.mlink.iwm.session.contact.OrganizationHome;

@SuppressWarnings("serial")  
 @Name("jobHome")

public class JobHome extends EntityHome<Job> {

	@In(create = true)
	OrganizationHome organizationHome;
	@In(create = true)
	TaskInstanceHome taskInstanceHome;
	@In(create = true)
	ContactHome contactHome;

	public void setJobId(Long id) {
		setId(id);
	}

	public Long getJobId() {
		return (Long) getId();
	}

	@Override
	public Job createInstance() {
		Job job = new Job();
		return job;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Organization organization = organizationHome.getDefinedInstance();
		if (organization != null) {
			getInstance().setOrganization(organization);
		}
		TaskInstance taskInstance = taskInstanceHome.getDefinedInstance();
		if (taskInstance != null) {
			getInstance().setTaskInstance(taskInstance);
		}
		Contact contact = contactHome.getDefinedInstance();
		if (contact != null) {
			getInstance().setContact(contact);
		}
	}

	public boolean isWired() {
		if (getInstance().getTaskInstance() == null)
			return false;
		if (getInstance().getContact() == null)
			return false;
		return true;
	}

	public Job getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<AssetTimeShare> getAssetTimeShares() {
		return getInstance() == null ? null : new ArrayList<AssetTimeShare>(
				getInstance().getAssetTimeShares());
	}
	public List<JobTask> getJobTasks() {
		return getInstance() == null ? null : new ArrayList<JobTask>(
				getInstance().getJobTasks());
	}
	public List<TenantRequest> getTenantRequests() {
		return getInstance() == null ? null : new ArrayList<TenantRequest>(
				getInstance().getTenantRequests());
	}

	public List<AssetResource> getAssetResources() {
		return getInstance() == null ? null : new ArrayList<AssetResource>(
				getInstance().getAssetResources());
	}
	public List<NonAssetResource> getNonAssetResources() {
		return getInstance() == null ? null : new ArrayList<NonAssetResource>(
				getInstance().getNonAssetResources());
	}
	

	/*
	 * (non-Javadoc)
	 *  com.mlink.iwm.framework.IAbstractEntityService#find(long)
	 */
	public Job find(Long id) {
		setJobId(id);
		return find();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public  String delete(Job job) {
		 setInstance(job);
	     return remove(); 
	}

	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public  String saveOrUpdate(Job job) {
		 setInstance(job);
		 return persist(); 
	}

	

	
}
