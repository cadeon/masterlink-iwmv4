package com.mlink.iwm.session.contact;


import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.contact.Organization;

@SuppressWarnings("serial")  
 @Name("organizationList")
public class OrganizationList extends EntityQuery<Organization> {

	private static final String SELECT = "select organization from Organization organization";

	private static final String SELECT_NAME_LIST = "select distinct name from Organization";
	

	private Organization organization = new Organization();

	public OrganizationList() {
		setEjbql(SELECT);
		setMaxResults(25);
	}

	public Organization getOrganization() {
		return organization;
	}
	
	
	/**
     * @return list of distinct orgnanization names
     */
	@SuppressWarnings("unchecked")
	public final List<String> getDistinctOrgNames() 
	{
		
		return( getEntityManager().createQuery(SELECT_NAME_LIST).getResultList());
	}
}
