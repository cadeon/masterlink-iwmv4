package com.mlink.iwm.session.contact;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.contact.Address;
import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.contact.Email;
import com.mlink.iwm.entity.contact.Organization;
import com.mlink.iwm.entity.contact.Phone;

@SuppressWarnings("serial")  
 @Name("contactHome")
public class ContactHome extends EntityHome<Contact> {

	@In(create = true)
	OrganizationHome organizationHome;

	public void setContactId(Long id) {
		setId(id);
	}

	public Long getContactId() {
		return (Long) getId();
	}

	@Override
	protected Contact createInstance() {
		Contact contact = new Contact();
		return contact;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Organization organization = organizationHome.getDefinedInstance();
		if (organization != null) {
			getInstance().setOrganization(organization);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Contact getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Address> getAddresses() {
		return getInstance() == null ? null : new ArrayList<Address>(
				getInstance().getAddresses());
	}
	/*public List<Email> getEmails() {
		return getInstance() == null ? null : new ArrayList<Email>(
				getInstance().getEmails());
	}
	public List<Phone> getPhones() {
		return getInstance() == null ? null : new ArrayList<Phone>(
				getInstance().getPhones());
	}*/
	
	/* *** Added by Mike *** */
	/*
	 * (non-Javadoc)
	 *  com.mlink.iwm.framework.IAbstractEntityService#find(long)
	 */
	public Contact find(Long id) {
		setContactId(id);
		return find();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public  String delete(Contact contact) {
		 setInstance(contact);
	     return remove(); 
	}

	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public  String saveOrUpdate(Contact contact) {
		 setInstance(contact);
		 return persist(); 
	}

}
