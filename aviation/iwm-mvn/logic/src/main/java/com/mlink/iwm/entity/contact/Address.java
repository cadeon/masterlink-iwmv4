package com.mlink.iwm.entity.contact;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mlink.iwm.entity.factory.AddressType;
import com.mlink.iwm.entity.factory.State;

import org.hibernate.validator.Length;
import org.hibernate.validator.Pattern;

/**

 */

@SuppressWarnings("serial")
@Entity  
@AttributeOverride( name="id", column = @Column(name="addressId"))
@Table(name = "address")
public class Address extends AbstractEntity{


	private Contact contact;
	private String line1;
	private String line2;
	private String city;
	private State state;
	private boolean isMailingAddr;
	private String zip;
	private AddressType addrType;

	public Address() {
	}

	public Address(Contact contact, String line1,
			String line2, String city, State state, String zip,
			AddressType addrType, 
			boolean isMailingAddr) {
		this.isMailingAddr = isMailingAddr;
		this.contact = contact;
		this.line1 = line1;
		this.line2 = line2;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.addrType = addrType;
	
		
	}

	
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactId")
	public Contact getContact() {
		return this.contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	@Column(name = "line1", length = 50)
	@Length(max = 50)
	public String getLine1() {
		return this.line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	@Column(name = "line2", length = 50)
	@Length(max = 50)
	public String getLine2() {
		return this.line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	@Column(name = "city", length = 25)
	@Length(max = 25)
	@Pattern(regex = "[a-zA-Z]+", message = "City must only contain letters")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "state", length = 2, nullable = false)
	public State getState() {
		return this.state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Column(name = "zip", length = 50)
	@Length(max = 50)
	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "addrType", nullable = false)
	public AddressType getAddrType() {
		return this.addrType;
	}

	public void setAddrType(AddressType addrType) {
		this.addrType = addrType;
	}


	
	@Column(name = "isMailingAddr", nullable = true)
	public boolean getIsMailingAddr() {
		return this.isMailingAddr;
	}

	public void setIsMailingAddr(boolean isMailingAddr) {
		this.isMailingAddr = isMailingAddr;
	}

}
