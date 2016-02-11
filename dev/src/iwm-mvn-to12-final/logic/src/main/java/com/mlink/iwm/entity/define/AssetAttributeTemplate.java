package com.mlink.iwm.entity.define;

import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.Length;

import com.mlink.iwm.entity.define.AssetTemplate;

/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="assetAttributeTemplateId"))
@Table(name = "AssetAttributeTemplate")
public class AssetAttributeTemplate extends AbstractEntity{

	private AssetTemplate assetTemplate;
	private String name;
	private String value;
	
	public AssetAttributeTemplate() {
	}


	public AssetAttributeTemplate(
			AssetTemplate assetTemplate, String name, String value) {
			
	
		this.assetTemplate = assetTemplate;
		this.name = name;
		this.value = value;		
	}



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assetTemplateId")
	public AssetTemplate getAssetTemplate() {
		return this.assetTemplate;
	}

	public void setAssetTemplate(AssetTemplate assetTemplate) {
		this.assetTemplate = assetTemplate;
	}
	@Column(name = "name", length = 25, unique = true)
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
	


}
