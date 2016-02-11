package com.mlink.mrap.entity.job;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.mlink.iwm.entity.contact.Contact;
import com.mlink.iwm.entity.AbstractEntity;
import com.mlink.iwm.entity.embeddable.JobCore;
import com.mlink.mrap.entity.factory.EomType;
import com.mlink.mrap.entity.factory.JobCat;



@SuppressWarnings("serial")
@Entity
@AttributeOverride( name="id", column = @Column(name="jobId") )
@Table(name = "MrapJob")
public class Job extends AbstractEntity {

	@Embedded 
	private JobCore jobCore;
	private String ero; //new equipment repair order
	private JobCat cat; //new
	private Date receivedInShopDate; //new when the asset showed up at the maint loc
	@Transient
	private int daysInShop; //new calculated: today - receivedInShopDate
	private Contact contact; //new POC for this job
	private Date completeBy;  
	private EomType eom;
	@Transient 
	private Double totatEstTime; //rollup of all jobTasks esttime

	public void setCat(JobCat cat) {
		this.cat = cat;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "jobCat")
	public JobCat getCat() {
		return cat;
	}

	public void setDaysInShop(int daysInShop) {
		this.daysInShop = daysInShop;
	}

	@Column(name = "dis")
	public int getDaysInShop() {
		return daysInShop;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	@OneToOne
	@PrimaryKeyJoinColumn
	public Contact getContact() {
		return contact;
	}

	public void setEro(String ero) {
		this.ero = ero;
	}


	@Column(name = "ero")
	public String getEro() {
		return ero;
	}

	public void setCompleteBy(Date completeBy) {
		this.completeBy = completeBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "combleteBy", length = 10)
	public Date getCompleteBy() {
		return completeBy;
	}

	public void setEom(EomType eom) {
		this.eom = eom;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "eom")
	public EomType getEom() {
		return eom;
	}

	public void setReceivedInShopDate(Date receivedInShopDate) {
		this.receivedInShopDate = receivedInShopDate;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "risd", length = 10)
	public Date getReceivedInShopDate() {
		return receivedInShopDate;
	}

	public void setTotatEstTime(Double totatEstTime) {
		this.totatEstTime = totatEstTime;
	}

	public Double getTotatEstTime() {
		return totatEstTime;
	}
	
	public void setJobCore(JobCore jc) {
		this.jobCore = jc;
	}
	public JobCore getJobCore() {
		return jobCore;
	}
	


}

