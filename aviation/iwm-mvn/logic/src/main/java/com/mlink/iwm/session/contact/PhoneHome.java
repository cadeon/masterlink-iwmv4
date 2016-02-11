package com.mlink.iwm.session.contact;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.contact.Phone;

@SuppressWarnings("serial")  
 @Name("phoneHome")
public class PhoneHome extends EntityHome<Phone> {

	@In(create = true)
	ContactHome contactHome;

	public void setPhoneId(Long id) {
		setId(id);
	}

	public Long getPhoneId() {
		return (Long) getId();
	}

	@Override
	protected Phone createInstance() {
		Phone phone = new Phone();
		return phone;
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

	public Phone getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
