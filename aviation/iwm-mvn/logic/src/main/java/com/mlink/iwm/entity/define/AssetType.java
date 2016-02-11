package com.mlink.iwm.entity.define;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.validator.Length;

/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="assetTypeId"))
@Table(name = "AssetType")
public class AssetType extends AbstractEntity{


	private Set<AssetTemplate> assetTemplates= new HashSet<AssetTemplate>(0);
	private long classId;
	private int code;
	private String description;

	public AssetType() {
	}

	
	public AssetType(Set<AssetTemplate> assetTemplates,
			long classId, int code, String description) {
		
		this.assetTemplates = assetTemplates;
		this.classId = classId;
		this.code = code;
		this.description = description;
	}

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetType")
	public Set<AssetTemplate> getAssetTemplates() {
		return this.assetTemplates;
	}

	public void setAssetTemplates(Set<AssetTemplate> assetTemplates) {
		this.assetTemplates = assetTemplates;
	}
	

	@Column(name = "classId")
	public long getClassId() {
		return this.classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	@Column(name = "code")
	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Column(name = "description", length = 45)
	@Length(max = 45)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
