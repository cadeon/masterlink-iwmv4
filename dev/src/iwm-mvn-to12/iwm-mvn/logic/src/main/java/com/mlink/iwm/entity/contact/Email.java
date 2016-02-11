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

import com.mlink.iwm.entity.factory.AddressType;

import org.hibernate.validator.Length;
import org.hibernate.validator.Pattern;

/**

 */

/*@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="emailId"))
@Table(name = "email")*/
@SuppressWarnings("serial")
public class Email extends AbstractEntity{

	
	private Contact contact;
	private String address;
	private AddressType addrType;

	public Email() {
	}

	public Email(Contact contact,  String address,AddressType addrType) {
		this.contact = contact;	
		this.addrType = addrType;
		this.address = address;
	}

	



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactId")
	public Contact getContact() {
		return this.contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	@Column(name = "address", length = 50, nullable = false)
    @Pattern(regex = ".+@.+\\..+", message = "Invalid email address format")
	@Length(max = 50)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "addrType")
	public AddressType getAddrType() {
		return this.addrType;
	}

	public void setAddrType(AddressType addrType) {
		this.addrType = addrType;
	}
}
