package com.mlink.mrap.entity.asset;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.embeddable.AssetCore;
import com.mlink.mrap.entity.AbstractEntity;


@SuppressWarnings("serial")
@Entity
@Table(name = "MrapAsset")
@AttributeOverride( name="id", column = @Column(name="assetId") )
public class Asset extends AbstractEntity {
	
	@Embedded 
	private AssetCore asset;
	
	   private Contact owner;
	
	//Note: the template level has an attribute defined as 
	//OwnerActivity set to some default value
	

	public Asset() {
	}

	
	public void setOwner(Contact owner) {
		this.owner = owner;
	}

	public Contact getOwner() {
		return owner;
	}


	public void setAsset(AssetCore asset) {
		this.asset = asset;
	}


	public AssetCore getAsset() {
		return asset;
	}

	


}
