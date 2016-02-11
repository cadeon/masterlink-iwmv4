package com.mlink.iwm.session.mrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetAttribute;
import com.mlink.iwm.entity.asset.AssetNotAvail;
import com.mlink.iwm.entity.asset.AssetTimeShare;
import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.contact.Organization;
import com.mlink.iwm.entity.factory.AssetKind;
import com.mlink.iwm.entity.factory.EomType;
import com.mlink.iwm.entity.factory.JobCat;
import com.mlink.iwm.entity.factory.JobStatus;
import com.mlink.iwm.entity.factory.Priority;
import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.entity.job.JobTask;
import com.mlink.iwm.entity.job.TaskInstance;
import com.mlink.iwm.entity.res.AssetResource;
import com.mlink.iwm.entity.res.NonAssetResource;
import com.mlink.iwm.session.asset.AssetHome;
import com.mlink.iwm.session.asset.AssetList;
import com.mlink.iwm.session.asset.TaskHome;
import com.mlink.iwm.session.contact.ContactHome;
import com.mlink.iwm.session.contact.OrganizationHome;
import com.mlink.iwm.session.job.JobHome;
import com.mlink.iwm.session.job.JobTaskHome;
import com.mlink.iwm.session.job.TaskInstanceHome;
import com.mlink.iwm.session.res.AssetResourceHome;
import com.mlink.iwm.util.DateUtils;

@Name("maintjobedit")
// @Scope(ScopeType.PAGE)
// NB- 07/12/2011 Changed scoping
@Scope(ScopeType.CONVERSATION)
public class MaintenanceJobEditor implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String HEADLINE = "View/Edit ";

	@In(create = true, required = false)
	private AssetHome assetHome;

	@In(create = true, required = false)
	private AssetList assetList;

	@In(create = true, required = false)
	private AssetResourceHome assetResourceHome;

	@In(create = true, required = false)
	private ContactHome contactHome;

	@In(create = true, required = false)
	private JobHome jobHome;

	@In(create = true, required = false)
	private JobTaskHome jobTaskHome;

	@In(create = true, required = false)
	private OrganizationHome organizationHome;
	
	@In(create = true, required = false)
	private TaskHome taskHome;

	@In(create = true, required = false)
	private TaskInstanceHome taskInstanceHome;
	
	private Job job;

	@Logger
	Log log;

	private AssetHelper assetHelper;
	private JobTask currentJobTask;
	private JobStatus defaultJobStatus = JobStatus.JS1;
	private JobTaskHelper jobTaskHelper;
	private Iterator<JobTask> jobTaskIterator;
	private String pattern = "MM/dd/yyyy";
	private List<TechnicianPresentation> technicianList;
	private Map<Long,Boolean> previouslyAssigned;
	private List<JobTask> taskList;
	private List<JobTask> deletedTaskList;
	private DataModel taskDataModel;

	public MaintenanceJobEditor() {
		technicianList = new ArrayList<TechnicianPresentation>();
		previouslyAssigned  = new HashMap<Long,Boolean>();
		deletedTaskList = new ArrayList<JobTask>	();
	}

	public void initJob() {
		job = jobHome.getInstance();
		if (job != null) {
			jobHome.wire();
			taskList= job.getJobTasks();
			taskDataModel = new ListDataModel(taskList);
			log.info("JobId: " + job.getId() + " - " + job);
		} else {
			log.info("Job not found for JobId: " + jobHome.getId());
		}
	}

	/* Property Accessors */
	public String getAssetDescription() { return MJob.getAttributeValue(job, "Description");}
	public List<AssetResource> getAssetResources() { return (job != null ? job.getAssetResources() : null);}
	public List<AssetResourceRollup> getAssetRollups() { return AssetResourceRollup.join(job.getAssetResources()); }
	public Boolean getHasAssetResources() {
		if (job!=null && job.getAssetResources().size()>0) return true;
		return false;
	}
	public JobCat getCategory() { return (job != null ? job.getCat() : null);	}
	public JobCat[] getCategories() { return JobCat.values();}
	public Contact getContact() {
		if (contactNotSet()) {
			if (contactHome == null)
				contactHome = (ContactHome) Component.getInstance(ContactHome.class);
			contactHome.clearInstance();
			job.setContact(contactHome.getInstance());
			if (job.getMaintainedAsset() != null && job.getMaintainedAsset().getOrganization() != null) {
				if (!job.getMaintainedAsset().getOrganization().getContacts().isEmpty()) {
					List<Contact> contacts = job.getMaintainedAsset().getOrganization().getContacts();
					job.getContact().setEmail(contacts.get(0).getEmail());
					job.getContact().setLname(contacts.get(0).getLname());
					job.getContact().setOrganization(contacts.get(0).getOrganization());
					job.getContact().setPhone(contacts.get(0).getPhone());
				}
			}
		}
		return job.getContact();
	}
	private boolean contactNotSet() {
		if (job==null              ||
			job.getContact()==null || 
		    (job.getContact().getLname()==null && 
		     job.getContact().getEmail()==null && 
		     job.getContact().getPhone()==null) ) {
			return true;
		}
		return false;
	}
	public String getCreatedDate() { return (job != null ? DateUtils.get4CharDate(job.getCreatedDate()) : "");}
	public String getDateClosedString() { return (job != null ? DateUtils.get4CharDate(job.getCompletedDate()) : "");	}
	public String getDatePattern() { return pattern;}
	public String getDescription() { return (job != null ? job.getDescription() : "no description available");}
	public int getDis() { return (job != null ? job.getDaysInShop() : 0);}
	public Date getDris() { return (job != null ? job.getReceivedInShopDate() : null);}
	public EomType getEom() { return (job.getEom() != null ? job.getEom() : EomType.E2);}
	public EomType[] getEoms() { return EomType.values();}
	public String getEro() { return (job != null ? job.getEro() : "");}
	public String getEroId() { return MJob.getAttributeValue(job, "Id");}
	public int getEstTime() { return (job != null ? job.getEstTime() : 0);	}
	public Date getDueDate() { return (job != null ? job.getDueDate() : null);	}
	public String getHeadline() { return HEADLINE + (getEro()!=null&&getEro().length()>0? "ERO #"+getEro():"");}
	public Job getJob() { return job;	}
	public Long getJobId() { return (job != null ? job.getId() : null);	}
	public JobStatus getJobStatus() { return (job.getStatus() != null ? job.getStatus() : defaultJobStatus);	}
	public JobStatus[] getJobStatuses() {	return JobStatus.values();	}
	public Date getLatestStart() {	return (job != null ? job.getLatestStart() : null);	}
	public String getModel() {	return MJob.getAttributeValue(job, "Model");}
	public String getNote() { return (job != null ? job.getNote() : "");}
	public int getNumResources() {	return (job != null ? job.getAssetResources().size() : 0);}
	public Organization getOrganization() {	return job.getOrganization();}
	public String getOrganizationName() {
		if (job.getContact() == null ||
			job.getContact().getOrganization() == null)
			return "";
		return job.getContact().getOrganization().getName();
	}
	public String getOrganizationActivityCode() {
		if (job.getContact() == null ||
			job.getContact().getOrganization() == null)
			return "";
		return job.getContact().getOrganization().getActivityCode();
	}
	public Integer getPriorityLabel() {	
		return ((job != null&& job.getPriority()!=null) ? job.getPriority().getIntLabel() : 1);
	}
	public Priority[] getPriorities() { return Priority.values();}
	public List<TechnicianPresentation> getQualifiedTechnicians() {
		technicianList.clear();
		previouslyAssigned.clear();
		List<Asset> al = assetList.getWorkersOnly();
		for (Asset a : al) { // find the workers
			if (fitsJobAR(a)) {
				TechnicianPresentation tech = checkAssignedTechnician(a);
				technicianList.add(tech);
				// capture state before rendering view
				// this will be the state to compare to upon "save"
				previouslyAssigned.put(tech.getId(), tech.getSelected());
			}
		}
		Collections.sort(technicianList);

		return technicianList;
	}


	@SuppressWarnings("unused")
	private boolean availableToday(Asset a) {
		List<AssetNotAvail> nas = a.getAssetNotAvails();
		for (AssetNotAvail na : nas) {
			long now = DateUtils.now().getTime();
			long start = (na.getStartDate() != null ? na.getStartDate().getTime() : 0L);
			long end = (na.getEndDate() != null ? na.getEndDate().getTime() : 0L);
			if (now >= start && now <= end) {
				return false;
			}
		}
		return true;
	}

	private boolean fitsJobAR(Asset a) {
		if (findMatchingJobAssetResource(a) != null)
			return true;
		return false;
	}

	private AssetResource findMatchingJobAssetResource(Asset asset) {
		List<AssetResource> ars = job.getAssetResources();
		for (Asset child : asset.getChildAssets()) {
			if (AssetKind.PR.equals(child.getAssetKind())) {
				for (AssetResource ar : ars) {
					if (ar.getAssetTemplate().equals(child.getAssetTemplate())) {
						// return first match
						return ar;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get a String concatenation of the assigned technicians for this job.
	 * This method is used by the Record Work tab.
	 * 
	 * @return
	 */
	public String getAssignedTechnicians() {
		StringBuffer sb = new StringBuffer();
		for (AssetTimeShare ats : job.getAssetTimeShares()) {
			String rank = "";
			List<AssetAttribute> aas = ats.getAsset().getAssetAttributes();
			for (AssetAttribute aa : aas) {
				if ("RANK".equalsIgnoreCase(aa.getName()))
					rank = aa.getValue();
			}
			sb.append(rank +" "+ ats.getAsset().getName()).append("<br/>");
		}
		return sb.toString();
	}
	/** 
	 * Return a TechnicianPresentation that contains whether or not the
	 * specified technician asset has been assigned to this job.
	 * This method is used by the Work Items & Assignments tab.
	 * 
	 * @param a The Asset representing the technician to check
	 * @return TechnicianPresentation which contains the asset info, and
	 * includes whether the technician has an assignment to this job
	 */
	private TechnicianPresentation checkAssignedTechnician(Asset a) {
		List<AssetTimeShare> timeShares = job.getAssetTimeShares();
		boolean sticky = false;
		boolean selected = false;
		// Can be assigned one of two ways:
		// 1. Scheduler assigns & creates asset time share
		// 2. Maintenance officer/chief assigns manually 
		//    (no asset time share till scheduler run)
		for (AssetTimeShare ats : timeShares) {
			if (ats.getAsset().getId().equals(a.getId())) {
				selected = true;
				break;
			}
		}
		for (AssetResource ar : job.getAssetResources()) {
			// tech can be assigned to one asset resource for the job
			if (ar.getIsManualAssign() &&
				ar.getStickyAsset().getId().equals(a.getId())) {
				selected = true;
				sticky = true;
				break;
			}
		}
		return new TechnicianPresentation(a,sticky, selected);		
	}

	public String getSerial() {	return MJob.getAssetSerial(job);	}
	public String getTam() { return MJob.getAttributeValue(job, "TAM");	}
	public DataModel getTaskDataModel() { return taskDataModel;	}
	
	public void setAssetResourceRollups(List<AssetResourceRollup> arrs) {
		List<Long> atIds = AssetResourceRollup.split(arrs);
		for (Long atId : atIds) {
			
		}
	}
	public void setCategory(JobCat cat) { job.setCat(cat);	}
	public void setCreatedDate() {	job.setCreatedDate(DateUtils.now());	}
	// public void setDateClosed() -- this is set when the job status changes to closed
	// public void setDis() -- this is a calculation from DRIS to now
	public void setDris(Date d) { job.setReceivedInShopDate(d);	}
	public void setDueDate(Date d) { job.setDueDate(d);	}
	public void setEom(EomType eom) { job.setEom(eom);	}
	public void setEro(String s) { job.setEro(s);	}
	public void setEroId(String s) {} // do not alter vehicle data via the job screen
	public void setEstTime(int i) {	job.setEstTime(i);	}
	public void setJob(Job j) {	job = j; }
	public void setJobStatus(String s) {
		if (s == null)
			return;
		setJobStatus(JobStatus.valueOf(s));
	}
	public void setJobStatus(JobStatus js) {
		if (js == null)
			return;
		if ("JOB CLOS".equalsIgnoreCase(js.getLabel())) { // set closed date
			job.setCompletedDate(DateUtils.now());
		}
		job.setStatus(js);
	}
	public void setLatestStart(Date d) { } // ignored by v40 scheduler
	public void setModel(String s) {} // do not alter vehicle data via the job screen
	public void setNote(String s) {	job.setNote(s);	}
	public void setOrganizationName(String s) { } // do not alter organization data via the job screen
	public void setOrganizationActivityCode(String activityCode) {	} // do not alter organization data via the job screen
	public void setPriorityLabel(Integer i) { job.setPriority(Priority.getEnum(i));	}
	//public void setSerial(String s) { } 
	public void setTam(String s) {} // do not alter vehicle data via the job screen

	/* Action Methods */
	public String viewJob(Job j) {
		job = j;
		return "job";
	}

	/* Work Item/Job Task helpers */
	public String addTaskItem() {
		JobTask workItem = new JobTask();
		//job.getJobTasks().add(workItem);
		workItem.setJob(job);
		taskList.add(workItem);
		return "addedJobTask";
	}

	public String deleteWorkItem(ActionEvent event) {
		/*Iterator<JobTask> jobTaskItr = taskList.iterator(); //job.getJobTasks().iterator();
		while (jobTaskItr.hasNext()) {
			JobTask jobTask = jobTaskItr.next();
			if (jobTask.isSelected()) {
				// remove from view
				jobTaskItr.remove();
				// dissociate from job entity
				//job.getJobTasks().remove(jobTask);
				taskList.remove(jobTask);
			}
		}*/
		JobTask currentTask = (JobTask)taskDataModel.getRowData();
		log.info("Deleting job task "+ currentTask.getId());
		deletedTaskList.add(currentTask);
		taskList.remove(currentTask);
		return "deletedJobTasks";
	}

	
	//** Persistence invocations
	public String saveJob() {
		log.info(">>>>>>>>>>>>>>>>>> Saving job...");
		String status = null;
		try {
			attachContact();
			if (job.getTaskInstance()!=null && job.getTaskInstance().getId()==null) {
				// persist task instance
				taskInstanceHome.setInstance(job.getTaskInstance());
				taskInstanceHome.persist();
			}
			if (organizationHome==null) 
				organizationHome = (OrganizationHome)Component.getInstance(OrganizationHome.class);
			job.setOrganization(organizationHome.find(JobManager.JOB_ORGANIZATION));
			if (job.getCreatedDate() == null)
				job.setCreatedDate(DateUtils.now());
			if (job.getEarliestStart() == null)
				job.setEarliestStart(DateUtils.now());
			if (job.getLatestStart() == null)
				job.setLatestStart(DateUtils.now());
			if (job.getReceivedInShopDate() == null)
				job.setReceivedInShopDate(DateUtils.now()); // This one should be set by screen
			if (job.getDueDate() == null) // This one should have a default, and screen access
				job.setDueDate(DateUtils.now()); 
			status = jobHome.saveOrUpdate(job);
			if ("updated".equals(status) || "persisted".equals(status)) {
				log.info("...job saved for jobId: " + job.getId());
			} else {
				log.info("Unsuccessful update for jobId: " + job.getId());
			}

		} catch (Exception e) {
			log.error("Save job failed", e);
		}
		return status;
	}

	private void attachContact() {
		if (job.getContact() != null && job.getContact().getLname() != null && job.getContact().getLname().length() > 0
				&& job.getContact().getEmail() != null && job.getContact().getEmail().length() > 0
				&& job.getContact().getPhone() != null && job.getContact().getPhone().length() > 0) {
			log.info("...saving contact...");
			contactHome.saveOrUpdate(job.getContact());
		} else { // remove empty contact before save to avoid transient object
					// error
			log.info("...contact not yet set...");
			job.setContact(null);
		}
	}

	public void setSerial(String serial) {
		if (serial != null && serial.length() > 0) {
			if (job.getMaintainedAsset() == null || job.getMaintainedAsset().getId() == null) {
				// Associate w/ Asset if an asset serial number is supplied
				log.info("...search for asset by serial " + serial + "...");
				if (assetHome == null)
					assetHome = (AssetHome) Component.getInstance(AssetHome.class);
				Asset a = assetHome.findBySerial(serial);
				log.info("... found asset with serial " + a.getTag());
				job.setMaintainedAsset(a);
				Contact assetContact = a.getOrganization().getContacts().get(0);
				Contact jobContact = contactHome.getInstance();
				jobContact.setEmail(assetContact.getEmail());
				jobContact.setJobTitle(assetContact.getJobTitle());
				jobContact.setLname(assetContact.getLname());
				jobContact.setOrganization(assetContact.getOrganization());
				jobContact.setPhone(assetContact.getPhone());
				job.setContact(jobContact);
				//job.setOrganization(a.getOrganization());
			}
		} else {
			if (job.getMaintainedAsset() != null && job.getMaintainedAsset().getId() != null) {
				log.info("...user attempted to remove asset from job...");
			} else { // remove empty asset before save to avoid transient object
						// error
				log.info("...asset not yet set...");
				job.setMaintainedAsset(null);
			}
		}
	}

	public String saveWorkItemChanges() {
		log.info("Saving job tasks & technician assignment...");
		String status = null;
		// save job
		try {
			// remove any deleted job tasks & return associated asset resources
			// (note this modified taskList)
			Set<Long> deleteArs = removeDeletedJobTasks();
			// update job's job tasks and asset resources
			Set<Long> retainedArs  = updateJob();
			// remove deleted job tasks' associated asset resources
			removeDeletedJobAssetResources(deleteArs,retainedArs);
			// update any new/removed assignments
			updateAssignments();
			//  check updates to estimated time
			updateEstTime();

			// Set current task list
			job.setJobTasks(taskList);
			status = jobHome.saveOrUpdate(job);
		} catch (Exception e) {
			log.error("Save job tasks failed", e);
			status = "Error: "+ e.getMessage();
		}

		if ("updated".equals(status) || "persisted".equals(status)) {
			log.info("...job saved.");
		} else {
			log.info("Unsuccessful update for jobId: " + job.getId() + "; Status was " + status);
		}
		deletedTaskList.clear();
		return status;
	}

	private void updateEstTime() {
		int estTime = 0;
		for (JobTask jt : job.getJobTasks()) {
			estTime += jt.getEstTime();
		}
		if (estTime != job.getEstTime())
			job.setEstTime(estTime);
	}
	/**
	 * Removes job tasks from the list of job tasks for this job
	 * @return Set (non-duplicating) of asset template ids for asset resources to remove
	 */
	private Set<Long> removeDeletedJobTasks() {
		Set<Long> ars = new HashSet<Long>();
		// iterator used for deleted task list as a for loop would cause
		// a concurrent modification exception when calling deletedTaskList.remove()
		Iterator<JobTask> jtIterator = deletedTaskList.iterator();
		while (jtIterator.hasNext()) {		
			JobTask jobTask = jtIterator.next();
			// collect deleted job tasks' asset resources & return asset template ids
			for (AssetResource dar : getAssetResources(jobTask)) {
					ars.add(dar.getAssetTemplate().getId());
					
			}
			// only remove from persistence if it's a managed entity
			if (jobTask.getId()!=null ) { 
				// remove from persistence
				log.info("Removing job task "+jobTask.getId());
				try {
					// remove jobtask from task instance & delete task instance
					TaskInstance ti = jobTask.getTaskInstance();
					ti.getJobTasks().clear();
					ti.setTask(null);
					jobTask.setTaskInstance(null);
					log.info("Removing task instance "+ ti.getId());
					taskInstanceHome.delete(ti);
					taskInstanceHome.clearInstance();
				} catch (NullPointerException e) { // no route to task instance?
					log.error("Caught NPE trying to get task instance for job task "+jobTask.getId());
				}
				jobTask.setJob(null);
				job.getJobTasks().remove(jobTask);
				jobTaskHome.getEntityManager().merge(jobTask);
				jobTaskHome.delete(jobTask);
				jobTaskHome.clearInstance();
			}
		}
		return ars;
	}

	private Set<Long> updateJob() {
		Set<Long> addedArs = new HashSet<Long>();
		Set<Long> retainedArs = new HashSet<Long>();
		// Make list of new job tasks that need to be processed
		// as job tasks are being persisted before they are ready
		// due to calling any ***Home.persist/delete
		// and checking for a null job task id is no longer a good
		// way to determine if the job task is new.
		List<JobTask> newJobTasks = new ArrayList<JobTask>();
		Iterator<JobTask> pruneJts = taskList.iterator();
		while (pruneJts.hasNext()) {
			JobTask jt = pruneJts.next();
			if (jt.getId() == null) {
				if (jt.getTaskId()==null) {
					pruneJts.remove();
					continue; // empty job task
				}
				newJobTasks.add(jt);
			} else { // retained job task, so retain asset resources
				List<AssetResource> keepers = getAssetResources(jt);
				for (AssetResource ar : keepers) {
					// really we are retaining the template id;
					// asset template ids are unique per job.
					// The hash set will de-dupe the values so each
					// asset template id occurs once
					retainedArs.add(ar.getAssetTemplate().getId());
				}
			}
		}
		Iterator<JobTask> jtIterator = newJobTasks.iterator();
		while (jtIterator.hasNext()) {
			JobTask jobTask = jtIterator.next();
			// add new job tasks
			createAddedJobTask(jobTask);
			// associate proper asset resources
			addedArs.addAll(updateJobAssetResources(jobTask));
			// associate proper non asset resources
			updateJobNonAssetResources(jobTask);
		}
		// union the two lists
		addedArs.addAll(retainedArs);
		return addedArs;
	}

	private void createAddedJobTask(JobTask jobTask) {
		log.info("Adding job task : "+jobTask);
		// create new taskInstance for new job task
		taskInstanceHome.clearInstance();
		TaskInstance ti = taskInstanceHome.getInstance();
		taskInstanceHome.persist();
		taskInstanceHome.clearInstance();
		jobTask.setTaskInstance(ti);
		// associate proper task to job task -> task instance
		taskHome.setId(jobTask.getTaskId());
		jobTask.getTaskInstance().setTask(taskHome.getInstance());
	}

	/**
	 * Adds new asset resources to job
	 * @param jobTask
	 * @param deleteArs
	 * @return Set (non-duplicating) of asset template ids for the added asset resources
	 */
	private Set<Long> updateJobAssetResources(JobTask jobTask) {
		Set<Long> addedArs = new HashSet<Long>();
		for (AssetResource ar : getAssetResources(jobTask)) {
			if (job.getAssetResources().isEmpty()) {
				AssetResource newAr = cloneAssetResource(job,ar);
				job.getAssetResources().add(newAr);
			} 
			boolean add = true;
			for (AssetResource jar : job.getAssetResources()) {
				// check to see if job already has this asset resource
				if (jar.getAssetTemplate().getId().equals(ar.getAssetTemplate().getId())) 
					add = false;
			}
			if (add) {
				AssetResource newAr = cloneAssetResource(job,ar);
				job.getAssetResources().add(newAr);
			}
			// keep track of asset template ids
			addedArs.add(ar.getAssetTemplate().getId());
		}
		return addedArs;
	}
	
	private void updateJobNonAssetResources(JobTask jobTask) {
		for (NonAssetResource nar : getNonAssetResources(jobTask)) {
			if (job.getNonAssetResources().isEmpty()) {
				job.getNonAssetResources().add(nar);
			} 
			boolean add = true;
			for (NonAssetResource jnar : job.getNonAssetResources()) {
				if (jnar.getTag().equals(nar.getTag())) 
					add=false;
			}
			if (add) job.getNonAssetResources().add(nar);
		}
	}

	private void removeDeletedJobAssetResources(Set<Long> deleteArs, 
			Set<Long> addedArs) 
	{
		// Don't delete asset template ids that were retained/added
		deleteArs.removeAll(addedArs);
		if (!deleteArs.isEmpty()) {
			// now delete asset resources that no longer apply
			Iterator<AssetResource> jarIterator = job.getAssetResources().iterator();
			while (jarIterator.hasNext()) {
				AssetResource jar = jarIterator.next();
				if (deleteArs.contains(jar.getAssetTemplate().getId())) {
					/*jar.setTask(null);
					jar.setTaskTemplate(null);
					jar.setAssetTemplate(null);
					jar.setJob(null);*/
					jarIterator.remove();
					assetResourceHome.setId(jar.getId());
					assetResourceHome.getInstance();
					jar.getJob().getAssetResources().remove(jar);
					assetResourceHome.remove();
					assetResourceHome.clearInstance();
				}
			}
		}
	}
	
	private List<AssetResource> getAssetResources(JobTask jobTask) {
		try {
			return jobTask.getTaskInstance().getTask().getAssetResources();
		} catch (NullPointerException npe) {
			log.error("Caught NPE trying to get asset resources on job task "+jobTask.getId());
			return new ArrayList<AssetResource>();
		}
	}
	private List<NonAssetResource> getNonAssetResources(JobTask jobTask) {
		try {
			return jobTask.getTaskInstance().getTask().getNonAssetResources();
		} catch (NullPointerException npe) 	{
			log.error("Caught NPE trying to get non-asset resources on job task "+jobTask.getId());
			return new ArrayList<NonAssetResource>();
		}
	}

	
	private AssetResource cloneAssetResource(Job job, AssetResource ar) {
		assetResourceHome.clearInstance();
		AssetResource newAr = assetResourceHome.getInstance();
		newAr.setArchiveDate(ar.getArchiveDate());
		newAr.setAssetTemplate(ar.getAssetTemplate());
		newAr.setIsManualAssign(ar.getIsManualAssign());
		newAr.setJob(job);
		newAr.setLastChange(ar.getLastChange());
		newAr.setQuantity(ar.getQuantity());
		newAr.setStickyAsset(ar.getStickyAsset());
		newAr.setTask(ar.getTask());
		newAr.setTaskTemplate(ar.getTaskTemplate());
		assetResourceHome.persist();
		return newAr;
	}

	private void updateAssignments() {
		for (TechnicianPresentation tech: technicianList) {
			log.info("Technician "+ tech.getDisplayName() +" was "+ 
					(tech.getSelected()? "selected": "not selected"));
			if (tech.getSelected() && !previouslyAssigned.get(tech.getId())) {
				// Create new assignment
				log.info("Creating new assignment for "+tech.getDisplayName()); 
				Asset technician = assetHome.find(tech.getId());

				jobHome.createManualAssignment(job, technician);
			} else if (previouslyAssigned.get(tech.getId()) && !tech.getSelected()) {
				// Remove previous assignment
				log.info("Removing "+tech.getDisplayName()+"'s previous assignment"); 
				Asset technician = assetHome.find(tech.getId());
				jobHome.removeManualAssignment(job, technician);
			}
		}
		// put back maintained asset into asset home instance
		assetHome.setId(job.getMaintainedAsset().getId());
	}
	public String back() {
		return "back";
	}

	public void cancel() {
		log.info("Canceling changes (reloading job & work items)");
		if (job.getId()!=null) jobHome.refresh(job);
	}

	public void cancelWorkItemsForm() {
		Iterator<JobTask> jobTaskItr = job.getJobTasks().iterator();
		while (jobTaskItr.hasNext()) {
			JobTask jobTask = jobTaskItr.next();
			if (jobTask.getId() == null) {
				// remove from view
				jobTaskItr.remove();
				// dissociate from job entity
				job.getJobTasks().remove(jobTask);
			}
		}
		cancel();
	}
	
	public void saveRecordWork() {
		try {
			String status = jobHome.saveOrUpdate(job);
			if ("updated".equals(status) || "persisted".equals(status)) {
				log.info("...job saved for jobId: " + job.getId());
			} else {
				log.info("Unsuccessful update for jobId: " + job.getId());
			}

		} catch (Exception e) {
			log.error("Save job failed", e);
		}
	}

	/* Work Item helpers */
	public String getNextWorkItemDescription() {
		if (jobTaskIterator == null) {
			jobTaskIterator = job.getJobTasks().iterator();
		}
		if (jobTaskIterator.hasNext()) {
			currentJobTask = jobTaskIterator.next();
			currentJobTask.getTaskInstance().getTask().getNonAssetResources();
			return currentJobTask.getDescription();
		} else { // end of list, return strings for work item methods
			currentJobTask = null;
		}
		return "";
	}

	public String getWorkItemDefectCode() {
		if (currentJobTask != null)
			return currentJobTask.getTaskCode();
		return "";
	}

	public int getWorkItemEstTime() {
		if (currentJobTask != null)
			return currentJobTask.getEstTime();
		return 0;
	}

	public List<NonAssetResource> getWorkItemParts() {
		return currentJobTask.getTaskInstance().getTask().getNonAssetResources();
	}

	public List<TaskPresentation> autocomplete(Object suggest) {
		if (jobTaskHelper == null) {
			log.info("Instantiating JobTaskHelper");
			jobTaskHelper = new JobTaskHelper();
		}
		log.info("Getting JobTaskHelper");
		// 06/29/11 Nadia-bypass taskInstance
		jobTaskHelper.setAsset(job.getMaintainedAsset());
		return jobTaskHelper.autocomplete(suggest);
	}

	public List<AssetPresentation> autocompleteSerial(Object suggest) {
		if (assetHelper == null) {
			log.info("Instantiating assetHelper");
			assetHelper = new AssetHelper();
		}
		log.info("Getting assetHelper");

		return assetHelper.autocomplete(suggest);
	}

}
