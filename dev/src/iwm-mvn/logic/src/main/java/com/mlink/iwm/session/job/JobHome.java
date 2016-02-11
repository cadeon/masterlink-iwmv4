package com.mlink.iwm.session.job;

import static org.jboss.seam.international.StatusMessage.Severity.INFO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetTimeShare;
import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.contact.Organization;
import com.mlink.iwm.entity.factory.AssetKind;
import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.entity.job.JobTask;
import com.mlink.iwm.entity.job.TaskInstance;
import com.mlink.iwm.entity.res.AssetResource;
import com.mlink.iwm.entity.res.NonAssetResource;
import com.mlink.iwm.session.asset.AssetHome;
import com.mlink.iwm.session.asset.AssetTimeShareHome;
import com.mlink.iwm.session.contact.ContactHome;
import com.mlink.iwm.session.contact.OrganizationHome;

@SuppressWarnings("serial")
@Name("jobHome")
public class JobHome extends EntityHome<Job> {

	@In(create = true)
	AssetHome assetHome;
	@In(create = true)
	AssetTimeShareHome assetTimeShareHome;
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
	protected Job createInstance() {
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
		/*
		 * System.out.println(" pre-wire: maintained asset "+
		 * (getInstance().getMaintainedAsset
		 * ()!=null?getInstance().getMaintainedAsset().getId():" not set"));
		 * System.out.println(" pre-wire: organization "+
		 * (getInstance().getOrganization
		 * ()!=null?getInstance().getOrganization().getId():" not set"));
		 */
		Asset maintainedAsset = assetHome.getInstance();
		if (maintainedAsset != null) {
			getInstance().setMaintainedAsset(maintainedAsset);
		}
		Organization organization = organizationHome.getInstance();
		if (organization != null) {
			getInstance().setOrganization(organization);
		}
		TaskInstance taskInstance = taskInstanceHome.getInstance();
		if (taskInstance != null) {
			getInstance().setTaskInstance(taskInstance);
		}
		Contact contact = contactHome.getInstance();
		if (contact != null) {
			getInstance().setContact(contact);
		}
		/*
		 * System.out.println(" post wire: maintained asset "+
		 * getInstance().getMaintainedAsset().getId());
		 * System.out.println(" post wire: organization "+
		 * (getInstance().getOrganization
		 * ()!=null?getInstance().getOrganization().getId():" not set"));
		 */
	}

	public boolean isWired() {
		if (getInstance().getContact() == null)
			return false;
		return true;
	}

	public Job getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<AssetResource> getAssetResources() {
		return getInstance() == null ? null : new ArrayList<AssetResource>(
				getInstance().getAssetResources());
	}

	public List<AssetTimeShare> getAssetTimeShares() {
		return getInstance() == null ? null : new ArrayList<AssetTimeShare>(
				getInstance().getAssetTimeShares());
	}

	public List<JobTask> getJobTasks() {
		return getInstance() == null ? null : new ArrayList<JobTask>(
				getInstance().getJobTasks());
	}

	public List<NonAssetResource> getNonAssetResources() {
		return getInstance() == null ? null : new ArrayList<NonAssetResource>(
				getInstance().getNonAssetResources());
	}

	/*
	 * (non-Javadoc) com.mlink.iwm.framework.IAbstractEntityService#find(long)
	 */
	public Job find(Long id) {
		setJobId(id);
		return find();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity
	 * .AbstractEntity)
	 */
	public String delete(Job job) {
		setInstance(job);
		FacesMessages.instance().clearGlobalMessages();
		return remove();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity
	 * .AbstractEntity)
	 */
	public String saveOrUpdate(Job job) {
		setInstance(job);
		// this.getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(getInstance())
		if (isIdDefined()) {
			update();
			FacesMessages.instance().clearGlobalMessages();
			getStatusMessages().addFromResourceBundleOrDefault(INFO,
					getUpdatedMessageKey(),
					getUpdatedMessage().getExpressionString());

			return "updated";
		}

		persist();
		FacesMessages.instance().clearGlobalMessages();
		getStatusMessages().addFromResourceBundleOrDefault(INFO,
				getCreatedMessageKey(),
				getCreatedMessage().getExpressionString());

		return "persisted";
	}

	public void createManualAssignment(Job job, Asset technician) {

		// Manual assignment, so make asset sticky to asset resource
		List<AssetResource> ars = job.getAssetResources();
		for (Asset child : technician.getChildAssets()) {
			if (AssetKind.PR.equals(child.getAssetKind())) {
				for (AssetResource ar : ars) {
					if (ar.getAssetTemplate().equals(child.getAssetTemplate())) {
						// return first match
						ar.setStickyAsset(technician);
						ar.setIsManualAssign(true);
					}
				}
			}
		}
		setInstance(getEntityManager().merge(job));
		// Assumption: nothing else needed, no creation of asset time share.
		// Rely on scheduler run to find next available slot
		// for assignment
	}

	public void removeManualAssignment(Job job, Asset technician) {
		Iterator<AssetTimeShare> its = job.getAssetTimeShares().iterator();
		while (its.hasNext()) { // Remove asset time shares
			AssetTimeShare ats = its.next();
			if (ats.getAsset().equals(technician)) {
				its.remove();
				assetTimeShareHome.delete(ats);
				break;
			}
		}
		Iterator<AssetResource> itr = job.getAssetResources().iterator();
		while (itr.hasNext()) { // Remove as sticky asset on asset resource
			AssetResource ar = itr.next();
			if (ar.getIsManualAssign()
					&& ar.getStickyAsset().equals(technician)) {
				ar.setIsManualAssign(false);
				ar.setStickyAsset(null);
				break;
			}
		}
		setInstance(getEntityManager().merge(job));
	}

	public void refresh(Job job) {
		getEntityManager().refresh(job);
	}

	public void clearEntityManager() {
		getEntityManager().clear();
	}

	public void clearJob(Job job) {
		getEntityManager().detach(job);
	}
}
