package com.mlink.iwm.session.mrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.ListDataModel;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.session.asset.AssetHome;
import com.mlink.iwm.session.asset.AssetList;
import com.mlink.iwm.session.contact.ContactHome;
import com.mlink.iwm.session.contact.OrganizationList;
import com.mlink.iwm.util.DateUtils;

@Name("vehiclesManager")
// NB- Reduced scoping from session to conv.
@Scope(ScopeType.CONVERSATION)
public class VehiclesManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Logger
	Log log;

	private List<VehicleEditPresentation> vehicleList;
	private OrganizationList orgList;
	private List<Asset> allVehicleList;
	private List<String> organizationOptions;

	// private AssetHome assetHome;
	// private AssetList assetList;
	// NB replaced above by injection use
	@In(create = true, required = false)
	AssetList assetList;

	@In(create = true, required = false)
	private AssetHome assetHome;

	@In(create = true, required = false)
	private ContactHome contactHome;

	private ListDataModel vehicleDataModel;

	public VehiclesManager() {

		// this.assetList = new AssetList();
		// this.assetHome = new AssetHome();

		// NB- replaced above by verifying orrect injection, getting explicitely
		// the component otherwise
		if (assetList == null)
			assetList = (AssetList) Component.getInstance(AssetList.class);

		if (assetHome == null)
			assetHome = (AssetHome) Component.getInstance(AssetHome.class);

		if (contactHome == null)
			contactHome = (ContactHome) Component.getInstance(ContactHome.class);

		// NB- 08/11
		/*
		 * this.vehicleList = new ArrayList<VehicleEditPresentation>();
		 * this.orgList = new OrganizationList(); this.organizationOptions =
		 * orgList.getDistinctOrgNames(); this.allVehicleList =
		 * assetList.getVehiclesOnly(); initVehiclesList();
		 */

		initVehiclesData();
	}

	public void initVehiclesList() {
		for (Asset vehicle : allVehicleList) {
			vehicleList.add(new VehicleEditPresentation(vehicle));
		}
		this.vehicleDataModel = new ListDataModel(vehicleList);
	}

	public List<String> getDistinctOrganization() {
		return this.organizationOptions;
	}

	public List<VehicleEditPresentation> getVehicleList() {
		return this.vehicleList;
	}

	public ListDataModel getVehicleDataModel() {
		return vehicleDataModel;
	}

	public String deleteVehicle(ActionEvent event) {
		VehicleEditPresentation currentVehicle = (VehicleEditPresentation) this.vehicleDataModel.getRowData();
		if (currentVehicle.getVehicle().getId() == null) {
			this.vehicleList.remove(currentVehicle);
		} else {
			currentVehicle.getVehicle().setArchiveDate(DateUtils.now());
			saveVehicle(currentVehicle);
		}
		return "deletedVehicle";
	}

	public String addNewVehicle() {
		this.vehicleList.add(new VehicleEditPresentation(assetHome.getNewVehicle()));
		return "addedNewVehicle";
	}

	public String saveVehicles() {
		for (VehicleEditPresentation vehicle : this.vehicleList) {
			saveVehicle(vehicle);
		}
		return "savedVehicles";
	}

	private void saveVehicle(VehicleEditPresentation vehicle) {
		if (vehicle.getOrganization().getId() == null) {
			assetHome.attachOrgAndContactToVehicle(vehicle.getVehicle(), vehicle.getOrganization(),
					vehicle.getContact());
		}
		this.assetHome.saveOrUpdate(vehicle.getVehicle());
	}

	public void initVehiclesData() {
		this.vehicleList = new ArrayList<VehicleEditPresentation>();
		this.orgList = new OrganizationList();
		this.organizationOptions = orgList.getDistinctOrgNames();
		this.allVehicleList = assetList.getVehiclesOnly();
		initVehiclesList();
	}

}
