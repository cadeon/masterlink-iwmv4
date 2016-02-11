package com.mlink.iwm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;




/**
 * Designates a class whose mapping information is applied to 
 * the entities that inherit from it. 
 * A mapped superclass has no separate table defined for it
 */

@MappedSuperclass
public class AbstractEntity implements Serializable {

	protected static final long serialVersionUID = 1L;
	protected Long id;
	protected Date lastChange;
	protected Date archiveDate;

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
  public Long getId()
   {
      return id;
   }
   public void setId(Long id)
   {
      this.id = id;
   }
   
	
	
	public AbstractEntity() {
		
	}
	
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)	
	@Column(nullable=true)
	public Date getLastChange() {
		return lastChange;
	}
	public void setLastChange(Date lc) {
		archiveDate=lc;
	}
	
	
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	public Date getArchiveDate() {
		return archiveDate;
	}
	public void setArchiveDate(Date ad) {
		archiveDate=ad;
	}
	
	
	
}
