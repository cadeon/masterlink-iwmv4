package com.mlink.iwm.session.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.security.Loginactivity;
import com.mlink.iwm.entity.security.User;

@SuppressWarnings("serial")

 @Name("userHome")
public class UserHome extends EntityHome<User> {

	@Logger
	private Log log;
	
	public void setUserId(Long id) {
		setId(id);
	}

	public Long getUserId() {
		return (Long) getId();
	}

	@Override
	protected User createInstance() {
		User user = new User();
		
		return user;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();	
	}

	public boolean isWired() {
		return true;
	}

	public User getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Loginactivity> getLoginactivities() {
		return getInstance() == null ? null : new ArrayList<Loginactivity>(
				getInstance().getLoginactivities());
	}

	/**
	 * 
	 */
	@Override
	public String persist() {
		String result = super.persist();
		return result;
	}

	/**
	 * 
	 * @param useruid
	 * @return
	 */
	public User findUserByUid(String useruid) {
		User user = null;

		StringBuilder query = new StringBuilder(
				"select distinct u from User u where");
		query.append(" u.uid = :useruid");
		log.debug("findUserByUid(){0}: ", useruid);
		try {

			user = (User) getEntityManager().createQuery(query.toString())
					.setParameter("useruid", useruid).getSingleResult();

		} catch (EntityNotFoundException ex) {
			log.debug("findUserByUid() user {0} not found", useruid);
		} catch (Exception ex) {
			log.debug("findUserByUid() exception " + ex.getMessage());
		}

		return user;
	}

	/**
	 * email should be case incentive unique
	 * @param emailAdr
	 * @return
	 */
	public User findUser(String emailAdr) {
		User user = null;
		StringBuilder query = new StringBuilder(
				"select distinct u from User u where");
		query.append(" u.email like :email");

		try {
			user = (User) getEntityManager().createQuery(query.toString())
					.setParameter("email", emailAdr).getSingleResult();

		} catch (NoResultException ex) {
			log.debug("findUserByEmail()email {0} not found", emailAdr);

		} catch (Exception ex) {
			log.debug("findUserByEmail() exception " + ex.getMessage());
		}
		return user;
	}

	/**
	 * 
	 * @param email
	 * @param uid
	 * @return
	 */
	public User findUser(String email, String uid) {
		User user = null;
		StringBuilder q = new StringBuilder(
				"select distinct u from User u where");
		q.append(" u.email like :email and not u.uid = :uid");

		try {

			Query query = getEntityManager().createQuery(q.toString());

			query.setParameter("email", email);
			query.setParameter("uid", uid);

			user = (User) query.getSingleResult();

		} catch (Exception ex) {
			log.debug("findUser(String email, String uid)"
					+ ex.getMessage());
		}
		return user;
	}
	
	
	
}
