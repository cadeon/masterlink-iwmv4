package com.mlink.iwm.entity.contact;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;

import java.util.HashSet;
import java.util.Set;
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

	
	private String name;
	private OrgType orgType;
	private Set<Contact> contacts = new HashSet<Contact>(0);

	public Organization() {
	}

	public Organization(String name,

			Set<Contact> contacts) {
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
	@Column(name = "orgType", length=2, nullable=false)
	public OrgType getOrgType() {
		return this.orgType;
	}

	public void setOrgType(OrgType ot) {
		this.orgType = ot;
	}




	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
	public Set<Contact> getContacts() {
		return this.contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

}
