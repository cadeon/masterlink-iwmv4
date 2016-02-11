package com.mlink.iwm.entity.asset;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.Length;

import com.mlink.iwm.entity.asset.Asset;
import com.mlink.iwm.entity.define.AssetAttributeTemplate;

@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="assetAttributeId"))
@Table(name = "AssetAttribute")
public class AssetAttribute extends AbstractEntity{
	private Asset asset;
	String name;
	String value;
	
	private AssetAttributeTemplate aat;


	public AssetAttribute() {
	}

	public AssetAttribute(Asset asset, String name, String value,
			AssetAttributeTemplate aat) {
		this.asset = asset;
		this.name = name;
		this.value = value;	;
		this.aat = aat;
	}

	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assetId")
	public Asset getAsset() {
		return this.asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	@Column(name = "name", length = 25)
	@Length(max = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "value", length = 25)
	@Length(max = 45)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assetAttributeTemplateId")
	public AssetAttributeTemplate getAat() {
		return this.aat;
	}

	public void setAat(
			AssetAttributeTemplate aat) {
		this.aat = aat;
	}

}
