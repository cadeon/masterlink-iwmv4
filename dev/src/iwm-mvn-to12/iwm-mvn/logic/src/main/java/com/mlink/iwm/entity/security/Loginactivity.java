package com.mlink.iwm.entity.security;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;

@SuppressWarnings("serial")

@Entity  
@AttributeOverride( name="id", column = @Column(name="loginActivityId"))
@Table(name = "loginactivity")
@Name("currentLoginActivity")
@Scope(ScopeType.SESSION)
public class Loginactivity extends AbstractEntity{


	private User user;
	private Date date;
	private Date loginTime;
	private Date logoutTime;

	public Loginactivity() {
	}

	public Loginactivity(User user, Date loginTime, Date logoutTime) {
		this.user = user;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
	}

	public Loginactivity(User user, Date date, Date loginTime, Date logoutTime) {
		this.user = user;
		this.date = date;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
	}

	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userID", nullable = false)
	@NotNull
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date", length = 10)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "loginTime", nullable = false, length = 19)
	@NotNull
	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "logoutTime", nullable = false, length = 19)
	@NotNull
	public Date getLogoutTime() {
		return this.logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

}
