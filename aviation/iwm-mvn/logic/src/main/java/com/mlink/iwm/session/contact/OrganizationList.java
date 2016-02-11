package com.mlink.iwm.session.contact;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.contact.Organization;

@SuppressWarnings("serial")  
 @Name("organizationList")
public class OrganizationList extends EntityQuery<Organization> {

	private static final String EJBQL = "select organization from Organization organization";

	private static final String[] RESTRICTIONS = {"lower(organization.name) like lower(concat(#{organizationList.organization.name},'%'))",};

	private Organization organization = new Organization();

	public OrganizationList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Organization getOrganization() {
		return organization;
	}
}
