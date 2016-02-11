package com.mlink.iwm.session.contact;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.contact.Email;

@SuppressWarnings("serial")  
 @Name("emailHome")
public class EmailHome extends EntityHome<Email> {

	@In(create = true)
	ContactHome contactHome;

	public void setEmailId(Long id) {
		setId(id);
	}

	public Long getEmailId() {
		return (Long) getId();
	}

	@Override
	protected Email createInstance() {
		Email email = new Email();
		return email;
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

	public Email getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
