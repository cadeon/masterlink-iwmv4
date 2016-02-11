package com.mlink.iwm.entity.contact;

//import javax.persistence.AttributeOverride;  
import com.mlink.iwm.entity.AbstractEntity;
import javax.persistence.Column;
//import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
//import javax.persistence.Table;
import com.mlink.iwm.entity.factory.PhoneType;

import org.hibernate.validator.Length;
import org.hibernate.validator.Pattern;

/**

 */

//@SuppressWarnings("serial")
//@Entity  
//@AttributeOverride( name="id", column = @Column(name="phoneId"))
//@Table(name = "phone")
@SuppressWarnings("serial")
public class Phone extends AbstractEntity{

	
	private Contact contact;

	private String number;
	private String ext;
	private PhoneType phoneType;

	public Phone() {
	}

	public Phone(Contact contact, String number,
			String ext, PhoneType phoneType) {
		this.contact = contact;
		this.number = number;
		this.ext = ext;
		this.phoneType = phoneType;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactId")
	public Contact getContact() {
		return this.contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}



	@Column(name = "number", length = 10)
	@Length(max = 10)
	@Pattern(regex = "\\d{10}", message = "Phone number must be 10 digits")
	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Column(name = "ext", length = 10)
	@Length(max = 10)
	public String getExt() {
		return this.ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "phoneType",  nullable = false)
	public PhoneType getPhoneType() {
		return this.phoneType;
	}

	public void setPhoneType(PhoneType phoneType) {
		this.phoneType = phoneType;
	}
}
