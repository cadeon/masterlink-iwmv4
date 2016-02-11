package com.mlink.iwm.session.contact;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.contact.Organization;

@SuppressWarnings("serial")  
 @Name("organizationHome")
public class OrganizationHome extends EntityHome<Organization> {

	public void setOrganizationId(Long id) {
		setId(id);
	}

	public Long getOrganizationId() {
		return (Long) getId();
	}

	@Override
	protected Organization createInstance() {
		Organization organization = new Organization();
		return organization;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Organization getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Contact> getContacts() {
		return getInstance() == null ? null : new ArrayList<Contact>(
				getInstance().getContacts());
		
	}
	/* *** Added by Mike *** */
	/*
	 * (non-Javadoc)
	 *  com.mlink.iwm.framework.IAbstractEntityService#find(long)
	 */
	public Organization find(Long id) {
		setOrganizationId(id);
		return find();	
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public  String delete(Organization organization) {
		 setInstance(organization);
	     return remove(); 
	}

	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public  String saveOrUpdate(Organization organization) {
		 setInstance(organization);
		 if(isIdDefined()){
			return update();
		}
		return persist();
	}
	
	
	

}
