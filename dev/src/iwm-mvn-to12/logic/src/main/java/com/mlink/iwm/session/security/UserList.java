package com.mlink.iwm.session.security;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mlink.iwm.entity.security.User;

@SuppressWarnings("serial")

 @Name("userList")
public class UserList extends EntityQuery<User> {

	private static final String EJBQL = "select user from User user";

	private static final String[] RESTRICTIONS = {
			"lower(user.email) like lower(concat(#{userList.user.email},'%'))",
			"lower(user.fname) like lower(concat(#{userList.user.fname},'%'))",
			"lower(user.lname) like lower(concat(#{userList.user.lname},'%'))",
			"lower(user.password) like lower(concat(#{userList.user.password},'%'))",
			"lower(user.uid) like lower(concat(#{userList.user.uid},'%'))", };

	private User user = new User();

	public UserList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public User getUser() {
		return user;
	}
}
