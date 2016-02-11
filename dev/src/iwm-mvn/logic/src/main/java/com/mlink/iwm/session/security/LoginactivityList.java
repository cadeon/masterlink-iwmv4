package com.mlink.iwm.session.security;


import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.security.Loginactivity;

@SuppressWarnings("serial")

 @Name("loginactivityList")
public class LoginactivityList extends EntityQuery<Loginactivity> {

	private static final String EJBQL = "select loginactivity from Loginactivity loginactivity";

	private static final String[] RESTRICTIONS = {};

	private Loginactivity loginactivity = new Loginactivity();

	public LoginactivityList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Loginactivity getLoginactivity() {
		return loginactivity;
	}
}
