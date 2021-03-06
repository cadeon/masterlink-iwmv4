package com.mlink.iwm.entity.embeddable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Length;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.asset.AssetAttribute;
import com.mlink.iwm.entity.asset.Task;
import com.mlink.iwm.entity.contact.Organization;
import com.mlink.iwm.entity.define.AssetTemplate;
import com.mlink.iwm.entity.define.AssetType;
import com.mlink.iwm.entity.factory.AssetKind;

@Embeddable
public class  AssetCore {
	
	private AssetTemplate assetTemplate;
	private boolean isActive;
	private AssetKind assetKind;
	private Date createdDate;
	private boolean isCustom;
	private boolean hasCustomData;
	private boolean hasCustomTask;
	private AssetType assetType;
	private Organization organization;
	private int runHours;
	
	private Date startDate;
	private String tag;   //this would need to be displayed as serial number
	private List<AssetAttribute> assetAttributes = new ArrayList<AssetAttribute>(0);
	
	private boolean isStickyResource;
	//task it is sticky to
	private Task stickyToTask;
	//private boolean isParent; 
	private Asset parent; //enforce that this is null if isParent is true
	private List<Asset> childAssets = new ArrayList <Asset>(0);
	
	//template inherited maintenace tasks for this asset : 
	private List<Task> maintTasks= new ArrayList <Task>(0);

	public AssetCore() {
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assetTemplateId")
	public AssetTemplate getAssetTemplate() {
		return this.assetTemplate;
	}

	public void setAssetTemplate(AssetTemplate assetTemplate) {
		this.assetTemplate = assetTemplate;
	}



	public void setIsActive(boolean active) {
		this.isActive = active;
	}

	


	@Temporal(TemporalType.DATE)
	@Column(name = "createdDate", length = 10)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "isCustom")
	public boolean getIsCustom() {
		return this.isCustom;
	}

	public void setIsCustom(boolean custom) {
		this.isCustom = custom;
	}

	@Column(name = "hasCustomData")
	public boolean getHasCustomData() {
		return this.hasCustomData;
	}

	public void setHasCustomData(boolean hasCustomData) {
		this.hasCustomData = hasCustomData;
	}

	@Column(name = "hasCustomTask")
	public boolean getHasCustomTask() {
		return this.hasCustomTask;
	}

	public void setHasCustomTask(boolean hasCustomTask) {
		this.hasCustomTask = hasCustomTask;
	}



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="assetTypeId")
	public AssetType getAssetType() {
		return this.assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	
	@Enumerated(EnumType.STRING)
	@Column(name = "assetKind", length=2, nullable=false)
	public AssetKind getAssetKind() {
		return this.assetKind;
	}

	public void setAssetKind(AssetKind assetKind) {
		this.assetKind = assetKind;
	}



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="orgId")
	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Column(name = "runHours")
	public int getRunHours() {
		return this.runHours;
	}

	public void setRunHours(int runHours) {
		this.runHours = runHours;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "startDate", length = 10)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "tag", length = 50)
	@Length(max = 50)
	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "asset")
	public List<AssetAttribute> getAssetAttributes() {
		return this.assetAttributes;
	}

	public void setAssetAttributes(List<AssetAttribute> assetAttributes) {
		this.assetAttributes = assetAttributes;
	}
	
	
	public void setStickyToTask(Task task) {
		this.stickyToTask= task;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="taskId")
	public Task getStickyToTask() {
		return this.stickyToTask;
	}
	
	
	public void setIsStickyResource(boolean isStickyResource) {
		this.isStickyResource = isStickyResource;
	}
	



	@Column(name = "isStickyResource", nullable = false)
	public boolean getIsStickyResource() {
		return isStickyResource;
	}
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="parentId")
	public Asset getParent() {
		return parent;
	}
	
	public void setParent(Asset parent) {
		this.parent = parent;
	}

	public void setChildAssets(List<Asset> childAssets) {
		this.childAssets = childAssets;
	}

	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "parent")
	public List<Asset> getChildAssets() {
		return childAssets;
	}

	
	@Column(name = "isActive")
	public boolean getIsActive() {
		return isActive;
	}
	
	
	public void setMaintTasks(List<Task> maintTasks) {
		this.maintTasks = maintTasks;
	}
	

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "Task_Asset", joinColumns = @JoinColumn(name = "taskId"), 
			  inverseJoinColumns = @JoinColumn(name = "assetId"))	
	public List<Task> getMaintTasks() {
		return maintTasks;
	}

}
