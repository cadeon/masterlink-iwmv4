package com.mlink.iwm.entity.contact;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.Length;

import com.mlink.iwm.entity.AbstractEntity;

//import org.hibernate.validator.Pattern;

/**

 */

@SuppressWarnings("serial")
@Entity
@AttributeOverride(name = "id", column = @Column(name = "contactId"))
@Table(name = "contact")
public class Contact extends AbstractEntity {

	private Organization organization;
	private String fname;
	private String middle;
	private String lname;
	private String suffix;
	private String jobTitle;
	private List<Address> addresses = new ArrayList<Address>(0);
	// private List<Email> emails = new ArrayList<Email>(0);
	// private List<Phone> phones = new ArrayList<Phone>(0);

	private String email;
	private String phone;

	public Contact() {
	}

	public Contact(Organization organization, String fname, String middle, String lname, String suffix,
			String jobTitle, List<Address> addresses, String email, String phone) {
		this.organization = organization;
		this.fname = fname;
		this.middle = middle;
		this.lname = lname;
		this.suffix = suffix;
		this.jobTitle = jobTitle;
		this.addresses = addresses;
		this.setEmail(email);
		this.setPhone(phone);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orgId")
	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Column(name = "fname", length = 50)
	@Length(max = 50)
	// @Pattern(regex = "[a-zA-Z]+", message =
	// "First name must only contain letters")
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "middle", length = 50)
	@Length(max = 50)
	// @Pattern(regex = "[a-zA-Z]+", message =
	// "Middle name must only contain letters")
	public String getMiddle() {
		return this.middle;
	}

	public void setMiddle(String middle) {
		this.middle = middle;
	}

	@Column(name = "lname", length = 50)
	@Length(max = 50)
	// @Pattern(regex = "[a-zA-Z]+", message =
	// "Last name must only contain letters")
	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	@Column(name = "suffix", length = 25)
	@Length(max = 25)
	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Column(name = "jobTitle", length = 50)
	@Length(max = 50)
	public String getJobTitle() {
		return this.jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contact")
	public List<Address> getAddresses() {
		return this.addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "email")
	// @Pattern(regex = ".+@.+\\..+", message = "Invalid email address format")
	@Length(max = 50)
	public String getEmail() {
		return email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	// @Pattern
	@Column(name = "phone")
	public String getPhone() {
		return phone;
	}

	/*
	 * @OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY, mappedBy =
	 * "contact") public List<Email> getEmails() { return this.emails; }
	 * 
	 * public void setEmails(List<Email> emails) { this.emails = emails; }
	 * 
	 * @OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY, mappedBy =
	 * "contact") public List<Phone> getPhones() { return this.phones; }
	 * 
	 * public void setPhones(List<Phone> phones) { this.phones = phones; }
	 */

}
