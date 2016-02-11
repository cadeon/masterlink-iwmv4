package com.mlink.iwm.session.contact;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.contact.Phone;

@SuppressWarnings("serial")  
 @Name("phoneList")
public class PhoneList extends EntityQuery<Phone> {

	private static final String EJBQL = "select phone from Phone phone";

	private static final String[] RESTRICTIONS = {
			"lower(phone.ext) like lower(concat(#{phoneList.phone.ext},'%'))",
			"lower(phone.number) like lower(concat(#{phoneList.phone.number},'%'))",};

	private Phone phone = new Phone();

	public PhoneList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Phone getPhone() {
		return phone;
	}
}
