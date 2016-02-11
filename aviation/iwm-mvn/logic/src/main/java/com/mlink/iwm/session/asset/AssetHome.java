package com.mlink.iwm.session.asset;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.asset.Action;
import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetAttribute;
import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.define.ActionTemplate;
import com.mlink.iwm.entity.define.AssetAttributeTemplate;
import com.mlink.iwm.entity.define.AssetTemplate;
import com.mlink.iwm.entity.define.AssetType;
import com.mlink.iwm.entity.define.TaskTemplate;
import com.mlink.iwm.entity.factory.AssetKind;
import com.mlink.iwm.entity.factory.JobStatus;
import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.session.define.AssetTemplateHome;
import com.mlink.iwm.session.define.AssetTypeHome;

@SuppressWarnings("serial")  
 @Name("assetHome")
public class AssetHome extends EntityHome<Asset> {
	
	private static final String SELECT = "select asset from Asset asset";
	@Logger
	private Log log;
	@In(create = true)
	AssetTemplateHome assetTemplateHome;
	@In(create = true)
	AssetTypeHome assetTypeHome;
	@In(create = true)
	AssetHome assetHome;
	@In(create = true)
	TaskHome taskHome;

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
		Task stickyToTask = taskHome.getDefinedInstance();
		if (stickyToTask != null) {
			getInstance().setStickyToTask(stickyToTask);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Asset getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<AssetAttribute> getAssetAttributes() {
		return getInstance() == null ? null : new ArrayList<AssetAttribute>(
				getInstance().getAssetAttributes());
	}
	public List<Asset> getChildAssets() {
		return getInstance() == null ? null : new ArrayList<Asset>(
				getInstance().getChildAssets());
	}
	
	/*
	 * (non-Javadoc)
	 *  com.mlink.iwm.framework.IAbstractEntityService#find(long)
	 */
	public Asset find(Long id) {
		setAssetId(id);
		return find();
		
	}
	
	
	
	public final Asset findBySerial(String serial) {
	
	
		String qs= SELECT + " where job.tag = :serial";
		Asset asset=null;
		
		try {

			Query query = getEntityManager().createQuery(qs).setParameter("tag", serial) ;
			asset = (Asset) query.getSingleResult();
		
			
		} catch (Exception ex) {
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		this.setInstance(asset);
		this.setAssetId(asset.getId());
		return this.getInstance();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public  String delete(Asset asset) {
		 setInstance(asset);
	     return remove(); 
	}

	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public  String saveOrUpdate(Asset asset) {
		 setInstance(asset);
		 return persist(); 
	}

	
	
	
	@Override
	@Transactional
	 public String persist()
	 {
		 log.info(">>>>>>> Invoked assetHome.persist");
		 //synchronize with corresponding template
		 this.synchronize();	 
         return super.persist();
	   }

	 @Override
	 @Transactional
	   public String remove()
	   {
	     //TODO
		 //set archive date
		 //return "removed";
		  return super.remove();
		
	   }
	 
	 @Override
	@Transactional
	   public String update()
	   {
		 //TODO
		 //what got updated?;
	      return super.update();
	   }
	 
	 
	 
	 
	 /**
	  * invoked when the user checks isCustom property 
	  * set template to null
	  */
	 public void makeCustom(){
		 this.instance.setAssetTemplate(null);
	 }
	 
	 /**
	  * Explicitly invoked on pre-persist 
	  * or 
	  * Invoked when its template is updated  
	  */	 
	 private void synchronize(){
		   //skip if this is a custom asset 
	        if(this.instance.getIsCustom())
	            return; // custom instances are not logically linked to their templates

	        AssetTemplate at = getInstance().getAssetTemplate();
	        if(at == null)
	            return;  //can this scenario happen?
	        
	        getInstance().setAssetType(at.getAssetType());
	        getInstance().setIsParent(at.getIsParent());
	        getInstance().setAssetKind(at.getAssetKind());
	        
	        //deep copy other properties
	        log.info(">>>>>>>>>>>>Synchronizing asset attributes");
	        
	        //attributes and default values
	        synchronizeAttributes(at);
	        
	        //childTemplates
	        
	        //For pure assets, synchronize tasks and their required resources
	        if(at.getAssetKind() == AssetKind.PA){
	        	 synchronizeTasks( at);
	        	
	        }
	       
	    
	        
	    }
	 
	 
	 
	 /**
	  * 
	  * @param at AssetTemplate to synchronize with
	  */
	 private void synchronizeTasks(AssetTemplate at){
		
			Set<Task> tasks = getInstance().getMaintTasks();
			
			//if invoked when an update is made to the template, clear 
			tasks.clear();

			Set<TaskTemplate> tts = at.getTaskTemplates();
			for (TaskTemplate tt : tts){
				Task task = new Task();
				task.setDescription(tt.getTaskDescription());
				//task.setEstTime(tt.getEstTime());
				task.setExpiryDays(tt.getExpNumDays());
				task.setExpiryType(tt.getExpiryType());
				task.setFreqDays(tt.getFreqDays());
				task.setFreqMonths(tt.getFreqMonths());
				task.setFreqType(tt.getFreqType());
                task.setTaskType(tt.getTaskType());
				task.setPriority(tt.getPriority());	
				task.setTaskCode(tt.getTaskCode());
				
				//actions
				synchronizeTaskActions(task, tt);
				
				//required asset resources
				task.setAssetResources(tt.getAssetResources());
				
				//required non asset resources
				task.setNonAssetResources(tt.getNonAssetResources());
				
				tasks.add(task);
			
			}
			
	 }
	 
	 /**
	  * 
	  * @param task Asset task to synchronize
	  * @param tt  template task to sync with
	  */
	 private void synchronizeTaskActions(Task task, TaskTemplate tt){
			
			Set<Action> actions = task.getActions();
			Set<ActionTemplate> ats = tt.getActionTemplates();
	
			//if invoked when an update is made to the template, clear 
			actions.clear();
			
			for (ActionTemplate at : ats){
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
	  * @param at AssetTemplate to synchronize with
	  */
	 private void synchronizeAttributes( AssetTemplate at ){
		Set<AssetAttribute> aas = getInstance().getAssetAttributes();
		 
		//if invoked when an update is made to the template, clear prior attributes
		aas.clear();
		 
		Set<AssetAttributeTemplate> aats = at.getAssetAttributeTemplates();
		for (AssetAttributeTemplate aat : aats){
			AssetAttribute aa = new AssetAttribute(this.getInstance(), aat.getName(), aat.getValue(), aat);
			aas.add(aa);
		}
		
	 }
	 
	
}

