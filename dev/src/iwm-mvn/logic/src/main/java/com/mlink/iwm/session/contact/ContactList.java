package com.mlink.iwm.session.contact;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.contact.Contact;

@SuppressWarnings("serial")  
 @Name("contactList")
public class ContactList extends EntityQuery<Contact> {

	private static final String EJBQL = "select contact from Contact contact";

	private static final String[] RESTRICTIONS = {
			"lower(contact.fname) like lower(concat(#{contactList.contact.fname},'%'))",
			"lower(contact.jobTitle) like lower(concat(#{contactList.contact.jobTitle},'%'))",
			"lower(contact.lname) like lower(concat(#{contactList.contact.lname},'%'))",
			"lower(contact.middle) like lower(concat(#{contactList.contact.middle},'%'))",
			"lower(contact.suffix) like lower(concat(#{contactList.contact.suffix},'%'))",};

	private Contact contact = new Contact();

	public ContactList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Contact getContact() {
		return contact;
	}
}
