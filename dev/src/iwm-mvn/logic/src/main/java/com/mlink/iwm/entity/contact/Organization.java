package com.mlink.iwm.entity.contact;

import javax.persistence.AttributeOverride; 
import com.mlink.iwm.entity.AbstractEntity;
import javax.persistence.CascadeType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.mlink.iwm.entity.factory.OrgType;

/**

 */
@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="orgId"))
@Table(name = "Organization")
public class Organization extends AbstractEntity{

	
	private String name; //used for organization and owner activity code
	private OrgType orgType;
	private List<Contact> contacts = new ArrayList<Contact>(0);
	private String activityCode;

	public Organization() {
	}

	public Organization(String name,

			List<Contact> contacts) {
		this.name = name;
	
		this.contacts = contacts;
	}

	

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Enumerated(EnumType.STRING)
	@Column(name = "orgType", length=2)
	public OrgType getOrgType() {
		return this.orgType;
	}

	public void setOrgType(OrgType ot) {
		this.orgType = ot;
	}




	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "organization")
	public List<Contact> getContacts() {
		return this.contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	@Column(name = "actCode", length = 50)
	public String getActivityCode() {
		return activityCode;
	}

}
