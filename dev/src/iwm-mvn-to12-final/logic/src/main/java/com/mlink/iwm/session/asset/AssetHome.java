package com.mlink.iwm.session.asset;

import java.util.ArrayList;
import static org.jboss.seam.international.StatusMessage.Severity.INFO;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.asset.Action;
import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetAttribute;
import com.mlink.iwm.entity.asset.AssetNotAvail;
import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.contact.Organization;
import com.mlink.iwm.entity.define.ActionTemplate;
import com.mlink.iwm.entity.define.AssetAttributeTemplate;
import com.mlink.iwm.entity.define.AssetTemplate;
import com.mlink.iwm.entity.define.AssetType;
import com.mlink.iwm.entity.define.TaskTemplate;
import com.mlink.iwm.entity.factory.AssetKind;
import com.mlink.iwm.session.contact.ContactHome;
import com.mlink.iwm.session.contact.OrganizationHome;
import com.mlink.iwm.session.define.AssetTemplateHome;
import com.mlink.iwm.session.define.AssetTypeHome;



@SuppressWarnings("serial")
@Name("assetHome")
public class AssetHome extends EntityHome<Asset> /* implements IAssetHome */{

	private static final String SELECT = "select asset from Asset asset";

	// TODO retrive id below based on codes
	private static final Long PA_ID = new Long(1);
	private static final Long WO_ID = new Long(2);
	private static final Long PR_ID = new Long(3);

	@In(create = true)
	AssetTemplateHome assetTemplateHome;
	@In(create = true)
	AssetTypeHome assetTypeHome;
	@In(create = true)
	AssetHome assetHome;
	@In(create = true)
	OrganizationHome organizationHome;
	@In(create = true)
	TaskHome taskHome;
	@In(create = true)
	AssetNotAvailHome assetNotAvailHome;
	@In(create = true)
	ContactHome contactHome;

	public void setAssetId(Long id) {
		setId(id);
	}

	public Long getAssetId() {
		return (Long) getId();
	}

	@Override
	protected Asset createInstance() {
		Asset asset = new Asset();
		return asset;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		AssetTemplate assetTemplate = assetTemplateHome.getDefinedInstance();
		if (assetTemplate != null) {
			getInstance().setAssetTemplate(assetTemplate);
		}
		AssetType assetType = assetTypeHome.getDefinedInstance();
		if (assetType != null) {
			getInstance().setAssetType(assetType);
		}
		Organization organization = organizationHome.getDefinedInstance();
		if (organization != null) {
			getInstance().setOrganization(organization);
		}
		Task stickyToTask = taskHome.getDefinedInstance();
		if (stickyToTask != null) {
			getInstance().setStickyToTask(stickyToTask);
		}
		Contact contact = contactHome.getDefinedInstance();
		if (contact != null) {
			getInstance().setContact(contact);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Asset getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<AssetAttribute> getAssetAttributes() {
		return getInstance() == null ? null : new ArrayList<AssetAttribute>(getInstance().getAssetAttributes());
	}

	public List<Asset> getChildAssets() {
		return getInstance() == null ? null : new ArrayList<Asset>(getInstance().getChildAssets());
	}

	public List<AssetNotAvail> getAssetNotAvails() {
		return getInstance() == null ? null : new ArrayList<AssetNotAvail>(getInstance().getAssetNotAvails());
	}

	/*
	 * (non-Javadoc) com.mlink.iwm.framework.IAbstractEntityService#find(long)
	 */
	public final Asset find(Long id) {
		setAssetId(id);
		return find();

	}

	/**
	 * 
	 * @param serial
	 * @return Asset with serial searial null there is no such asset
	 */
	public final Asset findBySerial(String serial) {

		String qs = SELECT + " where asset.tag = :serial";
		Asset asset = null;

		try {

			Query query = getEntityManager().createQuery(qs).setParameter("serial", serial);
			asset = (Asset) query.getSingleResult();

		} catch (Exception ex) {
			this.getLog().info("SQL ERROR:" + ex.getMessage());

		}
		this.setInstance(asset);
		this.setAssetId(asset.getId());
		return this.getInstance();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity
	 * .AbstractEntity)
	 */
	public final String saveOrUpdate(Asset asset) {
		
		setId(asset.getId());
		// load the instance or create a new one
		getInstance();
		// set/change entity being managed
		setInstance(asset);
		// TDOD uncomment beyond demo code...
		// synchronize();

		if (getId() != null) {	
			update();
			FacesMessages.instance().clearGlobalMessages();
			getStatusMessages().addFromResourceBundleOrDefault( INFO, getUpdatedMessageKey(), getUpdatedMessage().getExpressionString() );
            return "updated";
			
		} else { // new addition
			persist();
			FacesMessages.instance().clearGlobalMessages();
			getStatusMessages().addFromResourceBundleOrDefault( INFO, getCreatedMessageKey(), getCreatedMessage().getExpressionString() );		
			return "persisted";
			
		}

	}

	/**
	 * 
	 * @return
	 */
	public final Asset getNewQualification() {

		Asset qual = new Asset();
		qual.setAssetKind(AssetKind.PR);
		qual.setAssetTemplate(getEntityManager().find(AssetTemplate.class, PR_ID));
		qual.setAssetType(getEntityManager().find(AssetType.class, PR_ID));
		return qual;
	}

	/**
	 * 
	 * @return
	 */
	public final Asset getNewWorker() {
		Asset worker = new Asset();
		worker.setAssetKind(AssetKind.WO);
		worker.setAssetTemplate(getEntityManager().find(AssetTemplate.class, WO_ID));
		worker.setAssetType(getEntityManager().find(AssetType.class, WO_ID));
		return worker;
	}

	/**
	 * 
	 * @return
	 */
	public final Asset getNewVehicle() {
		Asset veh = new Asset();
		veh.setAssetKind(AssetKind.PA);
		veh.setAssetTemplate(getEntityManager().find(AssetTemplate.class, PA_ID));
		veh.setAssetType(getEntityManager().find(AssetType.class, PA_ID));
		return veh;
	}

	/**
	 * * Merges non managed child list with associated managed parent
	 * 
	 * @param worker
	 * @param quals
	 *            non managed qualification list
	 * @return
	 */
	public final String attachQualificationsToWorker(Asset parent, List<Asset> quals) {

		List<Asset> managedQuals = new ArrayList<Asset>();
		// merge the qualifactions and attach to worker
		for (Asset qual : quals) {

			// lazy initializatin override
			if (qual.getId() != null) {
				// load and merge if prsisted before
				getEntityManager().find(qual.getClass(), qual.getId());
				qual = getEntityManager().merge(qual);

			}
			// new & prior persists
			qual.setParent(parent);
			managedQuals.add(qual);
		}
		// set parent with managed quals
		parent.setChildAssets(managedQuals);
		return "success";
	}

	/**
	 * Merges non managed child list with associated managed parent
	 * 
	 * @param worker
	 * @param anas
	 *            (asset) not avalaible dates
	 * @return
	 */
	public final String attachLeaveTimesToWorker(Asset parent, List<AssetNotAvail> anas) {

		List<AssetNotAvail> managedAnas = new ArrayList<AssetNotAvail>();
		// merge the qualifactions and attach to worker
		for (AssetNotAvail ana : anas) {
			// lazy initializatin override
			if (ana.getId() != null) {
				// load and merge if prsisted before
				getEntityManager().find(ana.getClass(), ana.getId());
				ana = getEntityManager().merge(ana);

			}
			// new & prior persists
			ana.setAsset(parent);
			managedAnas.add(ana);
		}
		// set parent with managed quals
		parent.setAssetNotAvails(managedAnas);
		return "success";
	}

	/**
	 * Merges non managed child list with associated managed parent
	 * 
	 * @param vehicle
	 * @param organization
	 *            the vehicle belongs to
	 * @return
	 */
	public final String attachOrgAndContactToVehicle(Asset veh, Organization org, Contact contact) {

		// lazy initializatin override if not new 
		if (org.getId() != null) {
		// load if prsisted before
			getEntityManager().find(org.getClass(), org.getId());
		
		}
		if (contact.getId() != null) {
			// load if prsisted before
			getEntityManager().find(contact.getClass(), contact.getId());		
		}
			
		//set contact's org
		contact.setOrganization(org);
		
		//add or update org's primary contact
		if (!org.getContacts().isEmpty())
			//we have a previously persisted org with contacts
			org.getContacts().set(0, contact);
		else{
			
			ArrayList<Contact> cl= new ArrayList<Contact>(0); 
			cl.add(contact);
			org.setContacts(cl);
		}
		
		//merge
		org = getEntityManager().merge(org);
		contact = getEntityManager().merge(contact);
		
		
		//update veh
		veh.setContact(contact);
		veh.setOrganization(org);

		
		return "success";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity
	 * .AbstractEntity)
	 */
	public String delete(Asset asset) {
		setInstance(asset);
		getInstance();
		FacesMessages.instance().clearGlobalMessages();
		return remove();
	}

	/**
	 * invoked when the user checks isCustom property set template to null
	 */
	public void makeCustom() {
		this.instance.setAssetTemplate(null);
	}

	/**
	 * Explicitly invoked on pre-persist or Invoked when its template is updated
	 */
	private void synchronize() {

		// skip if this is a worker
		if (!(getInstance().getAssetKind() == AssetKind.PA))
			return;

		// skip if this is a custom asset
		if (getInstance().getIsCustom())
			return; // custom instances are not logically linked to their
					// templates

		AssetTemplate at = getInstance().getAssetTemplate();
		if (at == null)
			return; // can this scenario happen?

		// if this is an update of existing pure asset
		// then make it custome and detach from

		if (isIdDefined()) {
			getInstance().setIsCustom(true);
			getInstance().setAssetTemplate(null);
			return;
		}

		// newly created PA asset that we want to persist for the first time

		getInstance().setAssetType(at.getAssetType());
		getInstance().setAssetKind(at.getAssetKind());

		// deep copy other properties
		this.getLog().info(">>>>>>>>>>>>Synchronizing asset attributes");

		// attributes and default values
		synchronizeAttributes(at);

		// childTemplates

		// For pure assets, synchronize tasks and their required resources
		if (at.getAssetKind() == AssetKind.PA) {
			synchronizeTasks(at);

		}

	}

	/**
	 * 
	 * @param at
	 *            AssetTemplate to synchronize with
	 */
	private void synchronizeTasks(AssetTemplate at) {

		List<Task> tasks = getInstance().getMaintTasks();

		// if invoked when an update is made to the template, clear
		tasks.clear();

		List<TaskTemplate> tts = at.getTaskTemplates();
		for (TaskTemplate tt : tts) {
			Task task = new Task();
			task.setDescription(tt.getTaskDescription());
			// task.setEstTime(tt.getEstTime());
			task.setExpiryDays(tt.getExpNumDays());
			task.setExpiryType(tt.getExpiryType());
			task.setFreqDays(tt.getFreqDays());
			task.setFreqMonths(tt.getFreqMonths());
			task.setFreqType(tt.getFreqType());
			task.setTaskType(tt.getTaskType());
			task.setPriority(tt.getPriority());
			task.setTaskCode(tt.getTaskCode());

			// actions
			synchronizeTaskActions(task, tt);

			// required asset resources
			task.setAssetResources(tt.getAssetResources());

			// required non asset resources
			task.setNonAssetResources(tt.getNonAssetResources());

			tasks.add(task);

		}

	}

	/**
	 * 
	 * @param task
	 *            Asset task to synchronize
	 * @param tt
	 *            template task to sync with
	 */
	private void synchronizeTaskActions(Task task, TaskTemplate tt) {

		List<Action> actions = task.getActions();
		List<ActionTemplate> ats = tt.getActionTemplates();

		// if invoked when an update is made to the template, clear
		actions.clear();

		for (ActionTemplate at : ats) {
			Action action = new Action();
			action.setModifier(at.getModifier());
			action.setName(at.getName());
			action.setSequence(at.getSequence());
			action.setVerb(at.getVerb());
			action.setId(at.getId());
			actions.add(action);
		}

	}

	/**
	 * 
	 * @param at
	 *            AssetTemplate to synchronize with
	 */
	private void synchronizeAttributes(AssetTemplate at) {
		List<AssetAttribute> aas = getInstance().getAssetAttributes();

		// if invoked when an update is made to the template, clear prior
		// attributes
		aas.clear();

		List<AssetAttributeTemplate> aats = at.getAssetAttributeTemplates();
		for (AssetAttributeTemplate aat : aats) {
			AssetAttribute aa = new AssetAttribute(this.getInstance(), aat.getName(), aat.getValue(), aat);
			aas.add(aa);
		}

	}

	/**
	 * 
	 * @param asset
	 * @param leaveTime
	 * @return
	 */
	public String deleteLeaveTime(Asset parent, AssetNotAvail leaveTime) {
		// if this is not a managed entity there is nothing to do...
		if (leaveTime.getId() == null) {
			return "anaNotExist";
		}
		parent.getAssetNotAvails().remove(leaveTime);
		assetNotAvailHome.delete(leaveTime);
		return "anaDeleted";
	}

	/**
	 * 
	 * @param qual
	 * @return
	 */
	public String deleteQualification(Asset parent, Asset qual) {
		// if this is not a managed entity there is nothing to do...
		if (qual.getId() == null) {
			return "qualNotExist";
		}
		assetHome.delete(qual);
		parent.getChildAssets().remove(qual);
		return "qualDeleted";

	}

}
