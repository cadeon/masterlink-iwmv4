package com.mlink.iwm.session.security;

import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import com.mlink.iwm.entity.security.Loginactivity;
import com.mlink.iwm.entity.security.Role;
import com.mlink.iwm.entity.security.User;
import com.mlink.iwm.util.DateUtils;

@Name("authenticator")
public class Authenticator {
	@Logger
	private Log log;
	
	@In(create = true, required = true)
	private UserHome userHome;

	@In
	private EntityManager entityManager;

	@In
	private Identity identity;
	@In
	private Credentials credentials;

	@In(create = true, required =false)
	@Out(required = false, scope = ScopeType.SESSION)
	private User currentUser;

	@In(create = true, required = true)
	private Loginactivity currentLoginActivity;


	
	@Transactional
	public boolean authenticate() {

		log.debug("authenticating {0}....", credentials.getUsername());
		User user = userHome.findUserByUid(credentials.getUsername());
		if (user == null) {
			FacesMessages.instance().add("Invalid username");
			return false;
		}
		log.debug("Found user by username {0}....", credentials.getUsername());
		
        //is account active?
		if (! user.getEnabled()){
			FacesMessages.instance().add("Account disbaled");
			return false;
		}
		
		//validate input password
		if (!credentials.getPassword().equals(user.getPassword())) {
			FacesMessages.instance().add("Invalid password");
			return false;
		}
		
		log.debug(credentials.getUsername()
				+ " successfuly authenticated");

		// default role for authenticated users
		identity.addRole("users");
		List<Role> roles = user.getRoles();

		if (roles != null) {
			for (Role role : roles) {
				identity.addRole(role.getName());
				log.debug("Added role: ", role.getName());
			}
		}

		currentUser = user;
		log.debug("set currentUser to {0}: ", user.getUid());
		return true;

	}

	@Transactional
	public void logout() {
		log.debug(">>>>>>>>> Called logout()");

		log.debug("user {0} clicked on logout()", currentUser
						.getUid());
		currentLoginActivity.setLogoutTime(DateUtils.now());
		currentLoginActivity.setUser(currentUser);

		try {

			entityManager.persist(currentLoginActivity);
			entityManager.flush();
			log.debug("currentLoginActivity persisted for userUid {0}",
					currentLoginActivity.getUser().getUid());
			;

		} catch (Exception e) {
			log.debug("currentLoginActivity persistence for userUid {0} failed!",
							currentLoginActivity.getUser().getUid());
		}
		identity.logout();
	}

}
