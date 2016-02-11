package com.mlink.iwm.entity.security;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.hibernate.validator.Email;

import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import org.hibernate.validator.Pattern;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.ScopeType;

/**

 */
@SuppressWarnings("serial")

@Entity  @AttributeOverride( name="id", column = @Column(name="userId"))
@Table(name = "user", uniqueConstraints = {
		@UniqueConstraint(columnNames = "uid"),
		@UniqueConstraint(columnNames = "email") })
@Name("currentUser")
@Scope(ScopeType.SESSION)
@Startup
public class User extends AbstractEntity{

	private String fname;
	private String lname;
	private String uid;
	private String password;
	private String currentpswd;
	private String confirmpswd;
	private String newpswd;
	private boolean enabled;
	private String email;
	private List<Loginactivity> loginactivities = new ArrayList<Loginactivity>(
			0);
	private List<Role> roles = new ArrayList<Role>();


	
	public User() {
	}

	public User(String uid, String email) {
		this.uid = uid;
		this.email = email;
	}

	public User(String fname, String lname, String uid, String password,
			boolean enabled, String email, List<Loginactivity> loginactivities) {
		this.fname = fname;
		this.lname = lname;
		this.uid = uid;
		this.password = password;
		this.enabled = enabled;
		this.email = email;
		this.loginactivities = loginactivities;
	}

	

	@Column(name = "fname", nullable = false, length = 25)
	@Length(max = 25)
	@Pattern(regex = "[a-zA-Z]+", message = "Last name must only contain letters")
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "lname", nullable = false, length = 50)
	@Length(max = 50)
	@Pattern(regex = "[a-zA-Z]+", message = "First name must only contain letters")
	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	@Column(name = "uid", unique = true, nullable = false, length = 50)
	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	// @UserPassword //(hash = "md5")
	@Column(name = "password", length = 25, nullable = false)
	@Length(max = 25)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// @UserEnabled
	@Column(name = "enabled", nullable=false)
	public boolean getEnabled() {

		return this.enabled;

	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name = "email", unique = true, nullable = false, length = 45)
	@NotNull
	@Email
	@Length(max = 45)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public List<Loginactivity> getLoginactivities() {
		return this.loginactivities;
	}

	public void setLoginactivities(List<Loginactivity> loginactivities) {
		this.loginactivities = loginactivities;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Transient
	public String getConfirmpswd() {
		return confirmpswd;
	}

	public void setConfirmpswd(String confirm) {
		this.confirmpswd = confirm;
	}
	
	@Transient
	public String getNewpswd() {
		return newpswd;
	}

	public void setNewpswd(String newpswd) {
		this.newpswd = newpswd;
	}
	
	@Transient
	public String getCurrentpswd() {
		return currentpswd;
	}

	public void setCurrentpswd(String currentpswd) {
		this.currentpswd = currentpswd;
	}
	
	@Transient
	public String getRolesString() {
		StringBuilder sb = new StringBuilder();
		for (Iterator<Role> i = roles.iterator(); i.hasNext();) {
			sb.append(i.next().getName());
			if (i.hasNext())
				sb.append(" || ");
		}
		return sb.toString();
	}

	
}
