package com.mlink.iwm.session.security;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.security.User;

 @Name("userManager")
public class UserManager {
	
	@In(create=true)
	private UserHome userHome;

	@In private User currentUser;
	
	
	@Logger
	private Log log;


    @Transactional
	public String register() {

		if (isUsernameAvailable(userHome.getInstance().getUid().trim())) {

			// verify password confirm
			// may or may not found the user- use userHome
			if (isEmailAvailable(userHome.getInstance().getEmail().trim())) {
				
				if (! passwordVerified(userHome.getInstance().getPassword())) {
					FacesMessages.instance().addToControl("confirm",
							"Passwords do not match");
					FacesMessages.instance().addToControl("password",
					"Passwords do not match");
					return null;
				}
				log.debug("password confirmed");
				//TODO default is active account...extend functionality with mail to admin
				userHome.getInstance().setEnabled(true);
				// persist new user
				userHome.getEntityManager().joinTransaction();
				userHome.persist();
				log.debug("Registered new user "
						+ userHome.getInstance().getUid());
				userHome.getEntityManager().flush();
				return("registered");
				
			} else {
				FacesMessages.instance().add(
						"Email " + userHome.getInstance().getEmail()
								+ " is already registered by another user");
				
			}

		} else {
			FacesMessages.instance().add(
					"User " + userHome.getInstance().getUid()
							+ " already exists");
		
		}
		return null;

	}

	/**
	 * 
	 * @param email
	 * @return
	 */
	public boolean isEmailAvailable(String email) {
		User user = userHome.findUser(email);

		if (user == null)
			return true;

		return false;
	}

	/**
	 * 
	 * @param uid
	 * @return
	 */
	public boolean isUsernameAvailable(String uid) {
		User user = userHome.findUserByUid(uid);

		if (user == null)
			return true;

		return false;
	}
	
	
	/*
	 * 
	 */
    @Transactional
	public String updateAccount(){
		User u = userHome.findUser(currentUser.getEmail(), currentUser.getUid());

		if (u != null){
			FacesMessages.instance().add(
					"Email " + currentUser.getEmail()
							+ " is already registered by userID: "+ u.getUid());
			return null;
					
		}

		//update user account

		userHome.getEntityManager().joinTransaction();
	      
		userHome.getEntityManager().createQuery("update User u " +
				"set u.lname= :lname where u.id = :id ")
		.setParameter("lname",currentUser.getLname())
		.setParameter("id", currentUser.getId()).executeUpdate();
		
		userHome.getEntityManager().createQuery("update User u " +
		"set u.fname= :fname where u.id = :id ")
		.setParameter("fname",currentUser.getFname())
		.setParameter("id", currentUser.getId()).executeUpdate();
		
		userHome.getEntityManager().createQuery("update User u " +
		"set u.email= :email where u.id = :id ")
		.setParameter("email",currentUser.getEmail())
		.setParameter("id", currentUser.getId()).executeUpdate();
		
		
		FacesMessages.instance().add(
				"User Account {0} updated", currentUser.getUid());
		userHome.getEntityManager().flush();
		return "updated";
	}
    
    /**
     * 
     * @return
     */
    @Transactional
	public String changePassword(){
		//verify current password matches what is store

		User user = userHome.findUserByUid(currentUser.getUid());

		if (user == null){
			FacesMessages.instance().add(
					"could not find user with {0}: ", currentUser.getUid());
			return null;
					
		}
		log.debug("User found passwd is: {0}", user.getPassword());
		log.debug("input current pswd {0}: ", userHome.getInstance().getCurrentpswd());
		if (! currentPasswordVerified(user.getPassword())){
			FacesMessages.instance().addToControl(
					"curPassword","Current and input passwords do not match");
			return null;
					
		}
		log.debug("Current and input pswds match");
		//verify new password matches confirmed new password
		if (! passwordVerified(userHome.getInstance().getNewpswd())) {
			FacesMessages.instance().addToControl("newPassword",
					"Passwords do not match");
			FacesMessages.instance().addToControl("confirmNewPassword",
			"Passwords do not match");
			return null;
		}
		
		userHome.getEntityManager().joinTransaction();
		//now update user new password
		userHome.getEntityManager().createQuery("update User u " +
				"set u.password = :pswd where u.id = :id ")
		.setParameter("pswd",userHome.getInstance().getNewpswd())
		.setParameter("id", currentUser.getId()).executeUpdate();
		
		FacesMessages.instance().add(
				"User Account {0} updated", currentUser.getUid());
		userHome.getEntityManager().flush();
		return "updated";
	}
    
    /**
     * 
     * @return
     */
    private boolean passwordVerified(String inputpswd){
		return inputpswd
				.equals(userHome.getInstance().getConfirmpswd());

    }
    /**
     * 
     * @return
     */
    private boolean currentPasswordVerified(String dbpswd){
		
		return (dbpswd.equals(userHome.getInstance().getCurrentpswd()));
				
    }
}
