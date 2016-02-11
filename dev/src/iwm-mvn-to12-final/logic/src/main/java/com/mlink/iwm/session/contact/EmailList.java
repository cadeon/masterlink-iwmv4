package com.mlink.iwm.session.contact;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.contact.Email;

@SuppressWarnings("serial")  
 @Name("emailList")
public class EmailList extends EntityQuery<Email> {

	private static final String EJBQL = "select email from Email email";

	private static final String[] RESTRICTIONS = {"lower(email.address) like lower(concat(#{emailList.email.address},'%'))",};

	private Email email = new Email();

	public EmailList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Email getEmail() {
		return email;
	}
}
