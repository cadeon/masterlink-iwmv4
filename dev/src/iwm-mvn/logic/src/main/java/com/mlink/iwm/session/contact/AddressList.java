package com.mlink.iwm.session.contact;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.contact.Address;

@SuppressWarnings("serial")  
 @Name("addressList")
public class AddressList extends EntityQuery<Address> {

	private static final String EJBQL = "select address from Address address";

	private static final String[] RESTRICTIONS = {
			"lower(address.city) like lower(concat(#{addressList.address.city},'%'))",
			"lower(address.line1) like lower(concat(#{addressList.address.line1},'%'))",
			"lower(address.line2) like lower(concat(#{addressList.address.line2},'%'))",
			"lower(address.zip) like lower(concat(#{addressList.address.zip},'%'))",};

	private Address address = new Address();

	public AddressList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Address getAddress() {
		return address;
	}
}
