package com.mlink.iwm.session.security;


import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.security.Role;

@SuppressWarnings("serial")

 @Name("roleList")
public class RoleList extends EntityQuery<Role> {

	private static final String EJBQL = "select role from Role role ";

	private static final String[] RESTRICTIONS = { "lower(role.name) like lower(concat(#{roleList.role.name},'%'))", };

	private Role role = new Role();

	public RoleList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		
	}

	public Role getRole() {
		return role;
	}
}
