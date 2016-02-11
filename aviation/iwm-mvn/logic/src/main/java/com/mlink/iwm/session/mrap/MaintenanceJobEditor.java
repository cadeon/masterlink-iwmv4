package com.mlink.iwm.session.mrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.contact.Organization;
import com.mlink.iwm.entity.factory.AssetKind;
import com.mlink.iwm.entity.factory.EomType;
import com.mlink.iwm.entity.factory.JobCat;
import com.mlink.iwm.entity.factory.JobStatus;
import com.mlink.iwm.entity.factory.Priority;
import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.entity.job.JobTask;
import com.mlink.iwm.entity.res.AssetResource;
import com.mlink.iwm.entity.res.NonAssetResource;
import com.mlink.iwm.session.asset.AssetList;
import com.mlink.iwm.session.contact.ContactHome;
import com.mlink.iwm.session.contact.OrganizationHome;
import com.mlink.iwm.session.job.JobHome;
import com.mlink.iwm.session.job.JobTaskHome;
import com.mlink.iwm.util.DateUtils;


@Name("maintjobedit")
@Scope(ScopeType.PAGE)
public class MaintenanceJobEditor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@In(create=true, required=false)
	private AssetList assetList;

	@In(create=true, required=false)
	private ContactHome contactHome;
	
	@In(create=true, required=false)
	private JobHome jobHome;
	
	@In(create=true, required=false) @Out
	private Job job;

	@In(create=true, required=false)
	private JobTaskHome jobTaskHome;

	@In(create=true, required=false)
	private OrganizationHome organizationHome;
	
	@Logger
	Log log;

	private JobTask currentJobTask;
	private JobStatus defaultJobStatus = JobStatus.JS1;
	private boolean forceStart = false;
	private Long jobId;
	private JobTaskHelper jobTaskHelper;
	private Iterator<JobTask> jobTaskIterator;
	private String pattern = "MM/dd/yyyy";
	
	public MaintenanceJobEditor() {
		
	}
	
	/* Property Accessors */
	public String getAssetDescription() { return MJob.getAttributeValue(job,"Description"); }
	public List<AssetResource> getAssetResources() { return (job!=null?new ArrayList<AssetResource>(job.getAssetResources()):null); }
	public JobCat getCategory() { return  (job!=null?job.getCat():null); }
	public JobCat[] getCategories() { return JobCat.values(); }
	public Contact getContact() { 
		if (job.getContact() == null) {
			// create new contact
			contactHome.clearInstance();
			job.setContact(contactHome.getInstance());
		}
		return  job.getContact(); 
	}
	public String getCreatedDate() { return (job!=null?DateUtils.get4CharDate(job.getCreatedDate()):""); }
	public String getDateClosedString() { return (job!=null?DateUtils.get4CharDate(job.getCompletedDate()):""); }
	public String getDatePattern() { return pattern; }
	public String getDescription() { return (job!=null?job.getDescription():"no description available"); }
	public int getDis() { return (job!=null?job.getDaysInShop():0); }
	public Date getDris() { return (job!=null?job.getReceivedInShopDate():null); }
	public EomType getEom() { return (job!=null?job.getEom():null); }
	public EomType[] getEoms() { return EomType.values(); }
	public String getEro() { return (job!=null?job.getEro():""); }
	public String getEroId() { return MJob.getAttributeValue(job, "Id"); }
	public int getEstTime() { return (job!=null?job.getEstTime():0); }
	public boolean getForceStart() { return forceStart; }
	public Job getJob() { return job; }
	public Long getJobId() { return (job!=null?job.getId():null); }
	public JobStatus getJobStatus() { return (job!=null?job.getStatus():defaultJobStatus); }
	public JobStatus[] getJobStatuses() { return JobStatus.values(); }
	public Date getLatestStart() { return (job!=null?job.getLatestStart():null); }
	public String getModel() { return MJob.getAttributeValue(job, "Model"); }
	public String getNote() { return (job!=null?job.getNote():""); }
	public int getNumResources(){ return (job!=null?job.getAssetResources().size():0); }
	public Organization getOrganization() {
		if (job.getOrganization()==null) {
			// create new organization
			organizationHome.clearInstance();
			job.setOrganization(organizationHome.getInstance());
		}
		return  job.getOrganization(); 
	}
	public Priority getPriority() { return (job!=null?job.getPriority():null); }
	public Priority[] getPriorities() { return Priority.values(); }
	public List<Asset> getQualifiedTechnicians() {
		List<Asset> al = assetList.getAll(); 
		List<Asset> techList = new ArrayList<Asset>();
		for (Asset a : al ) { // find the workers
			if (AssetKind.WO.equals(a.getAssetKind()))
				techList.add(a);
		}
		return techList;
	}
	public String getSerial() { return MJob.getAssetSerial(job); }
	public String getTam() { return MJob.getAttributeValue(job, "TAM"); }
	public List<JobTask> getWorkItems() { 
		return new ArrayList<JobTask>(job.getJobTasks()); 
	}
	
	public void setCategory(JobCat cat) { job.setCat(cat); }
	public void setCreatedDate() { job.setCreatedDate(DateUtils.now()); }
	// public void setDateClosed() -- this is set when the job status changes to closed
	// public void setDis() -- this is a calculation from DRIS to now
	public void setDris(Date d) { job.setReceivedInShopDate(d); }
	public void setEom(EomType eom) { job.setEom(eom); }
	public void setEro(String s) { job.setEro(s); }
	public void setEroId(String s) {}
	public void setEstTime(int i) { job.setEstTime(i); }
	public void setForceStart(boolean b) { forceStart = b; }
	public void setForceStart(String s) { 
		forceStart = Boolean.parseBoolean(s);
	}
	public void setJob(Job j) { job = j; }
	public void setJobId(Long l) {
		job = jobHome.find(l);
		if (job == null) 
			log.info("Job null after lookup");
		else 
			log.info("JobId: "+job.getId()+" - "+job);
	}
	public void setJobId(String s) {
		log.info("Job id: "+s);
		Long l = Long.parseLong(s);
		setJobId(l);
	}
	public void setJobStatus(String s) {
		if (s==null) return;
		setJobStatus(JobStatus.valueOf(s));
	}
	public void setJobStatus (JobStatus js) {
		if (js==null) return;
		if ("JOB CLOS".equalsIgnoreCase(js.getLabel())) { // set closed date
			job.setCompletedDate(DateUtils.now());
		}
		job.setStatus(js);
	}
	public void setLatestStart(Date d) {}
	public void setModel(String s) {}
	public void setNote(String s) { job.setNote(s); }
	public void setPriority(Priority p) { job.setPriority(p); }
	public void setSerial(String s) {}
	public void setTam(String s) {}
	
	/* Action Methods */
	public String viewJob(Job j) {
		job = j;
		return "job";
	}
	public String saveJob() {
		log.info("Saving job...");
		try {
		    if (job.getOrganization()!= null &&
		    	job.getOrganization().getName().length() >0 &&
		    	job.getOrganization().getOrgType()!=null ) {
		    	organizationHome.saveOrUpdate(job.getOrganization());
		    } else {
		    	job.setOrganization(null);
		    }
		    if (job.getContact()!=null &&
		    	job.getContact().getLname()!=null &&
		    	job.getContact().getFname()!=null ) {
		    	contactHome.saveOrUpdate(job.getContact());
		    } else {
		    	job.setContact(null);
		    }
			jobHome.saveOrUpdate(job);
			log.info("...job saved.");
		} catch (Exception e) {
			log.error("Save job failed", e);
			return "savefailed";
		}
		return "savesuccess";
	}
	public String saveWorkItemChanges() {
		log.info("Saving job tasks...");
		try {
		jobHome.saveOrUpdate(job);
	} catch (Exception e) {
		log.error("Save job tasks failed", e);
		return "savefailed";
	}
	return "savesuccess";
		
	}	
	public String back() {
		return "back";
	}
	public String cancel() {
		log.info("Canceling changes (reloading job & work items)");
		try {
			job = jobHome.find(job.getId());
		} catch (Exception e) {
			log.error("Job reload failed", e);
			return "cancelfailed";
		}
		return "cancelsuccess";
	}
	
	
	/* Work Item helpers */
	public String getNextWorkItemDescription() {
		if (jobTaskIterator==null) {
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
		if (currentJobTask!=null) 
			return currentJobTask.getTaskCode(); 
		return "";
	}
	public int getWorkItemEstTime() {
		if (currentJobTask!=null) 
			return currentJobTask.getEstTime(); 
		return 0;
	}
	public Set<NonAssetResource> getWorkItemParts() {
		return currentJobTask.getTaskInstance().getTask().getNonAssetResources(); 
	}
	
	public List<Task>  autocomplete(Object suggest) {
		if (jobTaskHelper==null) {
			log.info("Instantiating JobTaskHelper");
			jobTaskHelper = new JobTaskHelper();
		}
		//FIXME: Is this assignment required each time? How to tell if
		//FIXME: another job has been sent in, e.g does MaintenanceJobEditor
		//FIXME: live between invocations by JobManger?
		log.info("Getting JobTaskHelper");
		jobTaskHelper.setAsset(job.getTaskInstance().getMaintainedAsset());
		for (Task t : jobTaskHelper.autocomplete(suggest)) {
			log.info("Task: "+t.getDescription() + "("+t.getTaskCode()+")");
		}
		return jobTaskHelper.autocomplete(suggest);
	}
}


