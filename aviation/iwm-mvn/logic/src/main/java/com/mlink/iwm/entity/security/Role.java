package com.mlink.iwm.entity.security;
import javax.persistence.AttributeOverride;  import com.mlink.iwm.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.Length;

/**

 */

@SuppressWarnings("serial")

@Entity  
@AttributeOverride( name="id", column = @Column(name="roleId"))
@Table(name = "role",  uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Role extends AbstractEntity{

	private String name;
	private String description;

	public Role() {
	}

	public Role(String name, String desc) {
		this.name = name;
		this.description=desc;
	}



	@Column(name = "name", unique = true, nullable = false, length = 25)
	@Length(max = 25)
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "description",nullable = true, length = 50)
	@Length(max = 125)
	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	/*public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> users) {
		this.members = users;
	}*/

}
