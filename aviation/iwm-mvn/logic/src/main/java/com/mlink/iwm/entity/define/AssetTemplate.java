package com.mlink.iwm.entity.define;

import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.Length;

import com.mlink.iwm.entity.factory.AssetKind;



/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="assetTemplateId"))
@Table(name = "AssetTemplate" )
public class AssetTemplate extends AbstractEntity{

	private boolean isParent; 
	private AssetTemplate parent; //enforce that this is null if isParent is true
	private Set<AssetTemplate> childTemplates = new HashSet <AssetTemplate>(0);
	private AssetKind assetKind;
	private String name;
	//plan is used by the planner: comma separated 0's and 1's  that maps to different times (days of weeks, months, seasons) 
	//that the asset is available to have jobs created for it
	private String plan;
	private Set<AssetAttributeTemplate> assetAttributeTemplates = new HashSet<AssetAttributeTemplate>(0);
	private Set<TaskTemplate> taskTemplates = new HashSet<TaskTemplate>(0);
	private AssetType assetType;
	
 
	
	public AssetTemplate() {
	}

	


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetTemplate")
	public Set<AssetAttributeTemplate> getAssetAttributeTemplates() {
		return this.assetAttributeTemplates;
	}

	public void setAssetAttributeTemplates(
			Set<AssetAttributeTemplate> assetAttributeTemplates) {
		this.assetAttributeTemplates = assetAttributeTemplates;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetTemplate")
	public Set<TaskTemplate> getTaskTemplates() {
		return this.taskTemplates;
	}

	public void setTaskTemplates(Set<TaskTemplate> taskTemplates) {
		this.taskTemplates = taskTemplates;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="assetTypeID")
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

	@Column(name = "plan", length = 25)
	@Length(max = 25)
	public String getPlan() {
		return this.plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}
	
	@Column(name = "name", length = 25, unique = true, nullable=false)
	@Length(max = 25)
	public String getName() {
		return this.name;
	}

	public void setName(String cn) {
		this.name = cn;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="parentId")
	public AssetTemplate getParent() {
		return parent;
	}
	
	public void setParent(AssetTemplate parent) {
		this.parent = parent;
	}

	public void setChildTemplates(Set<AssetTemplate> childTemplates) {
		this.childTemplates = childTemplates;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	public Set<AssetTemplate> getChildTemplates() {
		return childTemplates;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	@Column(name = "isParent", nullable = false)
	public boolean getIsParent() {
		return isParent;
	}

	


}
