package com.mlink.mrap.entity.res;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.mlink.iwm.entity.AbstractEntity;
import com.mlink.iwm.entity.embeddable.NonAssetResourceCore;

@SuppressWarnings("serial")
@Entity
@Table(name = "MrapPart")
@AttributeOverride( name="id", column = @Column(name="partId") )
public class NonAssetResource extends AbstractEntity  {   

	
	@Embedded 
	private NonAssetResourceCore narCore;
	
	//below are obtained via the MEM/GCSS interface
    private String status;
    private String recDateStr;
    private String currentLoc;
    
    

	public NonAssetResource() {	
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setRecDateStr(String recDateStr) {
		this.recDateStr = recDateStr;
	}

	public String getRecDateStr() {
		return recDateStr;
	}

	public void setCurrentLoc(String currentLoc) {
		this.currentLoc = currentLoc;
	}

	public String getCurrentLoc() {
		return currentLoc;
	}

	

	public void setNarcore(NonAssetResourceCore narcore) {
		this.narCore = narcore;
	}

	public NonAssetResourceCore getNarcore() {
		return narCore;
	}




}
