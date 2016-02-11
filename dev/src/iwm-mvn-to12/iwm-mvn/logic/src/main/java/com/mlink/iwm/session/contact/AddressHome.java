package com.mlink.iwm.session.contact;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.contact.Address;
import com.mlink.iwm.entity.contact.Contact;

@SuppressWarnings("serial")  
 @Name("addressHome")
public class AddressHome extends EntityHome<Address> {

	@In(create = true)
	ContactHome contactHome;

	public void setAddressId(Long id) {
		setId(id);
	}

	public Long getAddressId() {
		return (Long) getId();
	}

	@Override
	protected Address createInstance() {
		Address address = new Address();
		return address;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Contact contact = contactHome.getDefinedInstance();
		if (contact != null) {
			getInstance().setContact(contact);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Address getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
