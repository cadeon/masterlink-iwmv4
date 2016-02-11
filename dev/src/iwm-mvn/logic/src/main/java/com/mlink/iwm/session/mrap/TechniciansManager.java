package com.mlink.iwm.session.mrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.mlink.iwm.entity.asset.AssetNotAvail;
import com.mlink.iwm.entity.define.AssetTemplate;
import com.mlink.iwm.framework.exception.AssetException;
import com.mlink.iwm.session.asset.AssetHome;
import com.mlink.iwm.session.asset.AssetList;
import com.mlink.iwm.session.asset.AssetNotAvailList;

@Name("techsManager")
// NB- Reduced scoping from session to conv.
@Scope(ScopeType.CONVERSATION)
// @Scope(ScopeType.SESSION)
public class TechniciansManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Logger
	Log log;

	private List<TechnicianEditPresentation> technicianPresentList;

	private List<Asset> allTechList;
	private List<String> qualificationOptions;
	private DataModel techDataModel;

	@In(create = true, required = true)
	AssetList assetList;

	@In(create = true, required = true)
	private AssetHome assetHome;

	@In(create = true, required = true)
	private AssetNotAvailList assetNotAvailList;

	public TechniciansManager() {
		if (assetList == null)
			assetList = (AssetList) Component.getInstance(AssetList.class);

		if (assetHome == null)
			assetHome = (AssetHome) Component.getInstance(AssetHome.class);

		if (assetNotAvailList == null)
			assetNotAvailList = (AssetNotAvailList) Component.getInstance(AssetNotAvailList.class);

		initTechniciansData();

	}

	public void initTechniciansList() throws AssetException {
		for (Asset tech : allTechList) {
			List<Asset> quals = assetList.getWorkerProficiencies(tech);
			List<AssetNotAvail> leaveTimeList = assetNotAvailList.getLeaveTime(tech);
			tech.setAssetNotAvails(leaveTimeList);
			technicianPresentList.add(new TechnicianEditPresentation(tech, quals));

		}
	}

	public List<TechnicianEditPresentation> getTechnicianList() {
		return this.technicianPresentList;
	}

	public List<String> getQualificationConstants() {
		return this.qualificationOptions;
	}

	/**
	 * 
	 * @param event
	 * @return
	 * @throws AssetException
	 */
	public String addNewQualification(ActionEvent event) throws AssetException {
		TechnicianEditPresentation currentTech = (TechnicianEditPresentation) getTechDataModel().getRowData();

		Asset qual = assetHome.getNewQualification();
		qual.setParent(currentTech.getTechnician());
		currentTech.addQualification(qual);
		return "addedQualification";
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public String deleteQualification(ActionEvent event) {
		TechnicianEditPresentation currentTech = (TechnicianEditPresentation) getTechDataModel().getRowData();
		Asset qual = (Asset) currentTech.getQualificationsDataModel().getRowData();
		currentTech.deleteQualification(qual);
		// we need to explicitly delete the child
		assetHome.deleteQualification(currentTech.getTechnician(), qual);
		return "deletedQualification";
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public String addNewLeaveTime(ActionEvent event) {
		TechnicianEditPresentation currentTech = (TechnicianEditPresentation) getTechDataModel().getRowData();
		AssetNotAvail ana = createNewLeaveTime(currentTech);
		currentTech.addLeaveTime(ana);
		return "addedLeaveTime";
	}

	/**
	 * 
	 * @param tech
	 * @return
	 */
	private AssetNotAvail createNewLeaveTime(TechnicianEditPresentation tech) {
		AssetNotAvail ana = new AssetNotAvail();
		ana.setAsset(tech.getTechnician());
		return ana;
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public String deleteLeaveTime(ActionEvent event) {
		TechnicianEditPresentation currentTech = (TechnicianEditPresentation) getTechDataModel().getRowData();
		AssetNotAvail leaveTime = (AssetNotAvail) currentTech.getLeaveTimeDataModel().getRowData();
		currentTech.deleteLeaveTime(leaveTime);
		// we need to explicitly delete the child from the parent
		assetHome.deleteLeaveTime(currentTech.getTechnician(), leaveTime);
		return "deletedLeaveTime";
	}

	public String deleteTechnician(ActionEvent event) {
		TechnicianEditPresentation currentTech = (TechnicianEditPresentation) getTechDataModel().getRowData();
		this.allTechList.remove(currentTech.getTechnician());
		this.technicianPresentList.remove(currentTech);
		assetHome.delete(currentTech.getTechnician());
		return "deletedTechnician";
	}

	/**
	 * 
	 * @return
	 */
	public String addNewTechnician() {
		Asset tech = assetHome.getNewWorker();
		List<Asset> quals = new ArrayList<Asset>();
		Asset qual = assetHome.getNewQualification();
		qual.setParent(tech);
		quals.add(qual);

		TechnicianEditPresentation newTech = new TechnicianEditPresentation(tech, quals);
		this.technicianPresentList.add(newTech);
		return "addedNewTechinician";
	}

	public MilitaryRank[] getMilitaryRank() {
		return MilitaryRank.values();
	}

	public void setTechDataModel(DataModel techDataModel) {
		this.techDataModel = techDataModel;
	}

	public DataModel getTechDataModel() {
		return techDataModel;
	}

	@SuppressWarnings("unchecked")
	public String saveTechnicians() {
		TechnicianStatusReconciler statusReconciler = new TechnicianStatusReconciler(assetHome);
		for (TechnicianEditPresentation tech : this.technicianPresentList) {
			// let's replace our tech worker quals with the potentially new list
			List<Asset> updatedQuals = (List<Asset>) tech.getQualificationsDataModel().getWrappedData();
			for (Asset asset : updatedQuals) {
				if (asset.getAssetTemplate() == null ) { // set asset template
					asset.setAssetTemplate(assetList.getTemplateByName(asset.getName()));
				}
			}
			assetHome.attachQualificationsToWorker(tech.getTechnician(), updatedQuals);

			// let's replace our tech worker asset not availabe with the
			// potentially new list
			List<AssetNotAvail> updatedAnas = (List<AssetNotAvail>) tech.getLeaveTimeDataModel().getWrappedData();
			removeEmptyAnas(updatedAnas);
			statusReconciler.reconcileStatus(tech, updatedAnas);

			assetHome.attachLeaveTimesToWorker(tech.getTechnician(), updatedAnas);

			this.assetHome.saveOrUpdate(tech.getTechnician());
		}
		return "savedTechinicians";
	}

	private void removeEmptyAnas(List<AssetNotAvail> updatedAnas) {
		for (Iterator<AssetNotAvail> it = updatedAnas.iterator(); it.hasNext();) {
			AssetNotAvail leaveTime = it.next();
			if (leaveTime.getStartDate() == null || leaveTime.getEndDate() == null) {
				it.remove();
			}
		}
	}

	public void initTechniciansData() {

		this.technicianPresentList = new ArrayList<TechnicianEditPresentation>();
		this.allTechList = assetList.getWorkersOnly();
		this.qualificationOptions = assetList.getDistinctQualifications();
		setTechDataModel(new ListDataModel(this.technicianPresentList));
		try {
			initTechniciansList();
		} catch (AssetException e) {
			log.info("Exception in refreshTechData() :" + e.getMessage());
		}
	}

}
