package com.mlink.iwm.session.mrap;

import java.util.List;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetAttribute;
import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.contact.Organization;

public class VehicleEditPresentation {

	private enum AttrName {
		MODEL, ID, TAM;
	}

	private Asset vehicle;
	private AssetAttribute modelAttr;
	private AssetAttribute idAttr;
	private AssetAttribute tamAttr;
	private Contact contact;
	private Organization organization;

	public VehicleEditPresentation(Asset vehicle) {
		this.vehicle = vehicle;
		initOrganization();
		initContact();
		initAttributes();
	}

	private void initOrganization() {
		if (vehicle.getOrganization() == null) {
			this.organization = new Organization();
			vehicle.setOrganization(this.organization);
		} else {
			this.organization = vehicle.getOrganization();
		}
	}

	private void initContact() {
		if (vehicle.getContact() == null) {
			this.contact = new Contact();
			//NB 08/22 set vehicle's contact
			vehicle.setContact(this.contact);
		} else {
			
			this.contact = vehicle.getContact();
		}
	}

	private void initAttributes() {
		List<AssetAttribute> assetAttrs = vehicle.getAssetAttributes();
		for (AssetAttribute attr : assetAttrs) {
			AttrName attrName = AttrName.valueOf(attr.getName());
			switch (attrName) {
			case MODEL:
				this.modelAttr = attr;
				break;
			case ID:
				this.idAttr = attr;
				break;
			case TAM:
				this.tamAttr = attr;
				break;
			}
		}

		if (this.modelAttr == null) {
			this.modelAttr = createMissingAttribute(assetAttrs, AttrName.MODEL);
		}
		if (this.idAttr == null) {
			this.idAttr = createMissingAttribute(assetAttrs, AttrName.ID);
		}
		if (this.tamAttr == null) {
			this.tamAttr = createMissingAttribute(assetAttrs, AttrName.TAM);
		}
	}

	private AssetAttribute createMissingAttribute(List<AssetAttribute> assetAttrs, AttrName attrName) {
		AssetAttribute assetAttr = new AssetAttribute();
		assetAttr.setName(attrName.name());
		assetAttr.setValue("");
		assetAttr.setAsset(vehicle);
		assetAttrs.add(assetAttr);
		return assetAttr;
	}

	public String getVehicleId() {
		return this.idAttr.getValue();
	}

	public void setVehicleId(String id) {
		this.idAttr.setValue(id);
	}

	public String getTam() {
		return this.tamAttr.getValue();
	}

	public void setTam(String tam) {
		this.tamAttr.setValue(tam);
	}

	public String getModel() {
		return this.modelAttr.getValue();
	}

	public void setModel(String model) {
		this.modelAttr.setValue(model);
	}

	public String getSerialNumber() {
		return this.vehicle.getTag();
	}

	public void setSerialNumber(String serial) {
		this.vehicle.setTag(serial);
	}

	public String getOwnerName() {
		return contact.getLname();
	}

	public void setOwnerName(String name) {
		contact.setLname(name);
	}

	public String getPhone() {
		return contact.getPhone();
	}

	public void setPhone(String phone) {
		this.contact.setPhone(phone);
	}

	public String getEmail() {
		return this.contact.getEmail();
	}

	public void setEmail(String email) {
		this.contact.setEmail(email);
	}

	public String getOrganizationName() {
		return this.organization.getName();
	}

	public void setOrganizationName(String name) {
		this.organization.setName(name);
	}

	public String getActivityCode() {
		return this.organization.getActivityCode();
	}

	public void setActivityCode(String code) {
		this.organization.setActivityCode(code);
	}

	public Asset getVehicle() {
		return this.vehicle;
	}
	
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Contact getContact() {
		return this.contact;
	}
}
