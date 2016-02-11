package com.mlink.iwm.session.security;



import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.security.Loginactivity;
import com.mlink.iwm.entity.security.User;
import com.mlink.iwm.util.DateUtils;

@SuppressWarnings("serial")  
 @Name("loginactivityHome")

public class LoginactivityHome extends EntityHome<Loginactivity> {

    @In(create = true, required = true)
	UserHome userHome;
  
	@Logger
	private Log log;
	
	@In
	@Out(required=false, scope=ScopeType.SESSION) 
    private Loginactivity currentLoginActivity;
	
	public void setLoginActivityId(Long id) {
		setId(id);
	}

	public Long getLoginActivityId() {
		return (Long) getId();
	}
	
	
	@Override
	protected Loginactivity createInstance() {
		Loginactivity loginactivity = new Loginactivity();
		return loginactivity;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		User user = userHome.getDefinedInstance();
		if (user != null) {
			getInstance().setUser(user);
		}
	}

	public boolean isWired() {
		if (getInstance().getUser() == null)
			return false;
		return true;
	}

	public Loginactivity getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	
	
	@Observer("org.jboss.seam.security.loginSuccessful")
	@Transactional
	public void userLoggedIn(){
		log.debug("caught loginSuccessful");
		this.getInstance().setDate(DateUtils.now());
		this.getInstance().setLoginTime(DateUtils.now());
		setCurrentLoginActivity(this.getInstance());
		
	}

	public void setCurrentLoginActivity(Loginactivity currentLoginActivity) {
		this.currentLoginActivity = currentLoginActivity;
	}

	public Loginactivity getCurrentLoginActivity() {
		return currentLoginActivity;
	} 
	
	//session has already been destroyed with the event...change
	//action of logout from identity.logout to authenticator.logout  
	//@Observer("event org.jboss.seam.security.loggedOut") 
	//@Transactional
	/*public void userLoggedOut(){
		
		 persist();
		
	}*/
	



}
