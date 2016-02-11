package com.mlink.iwm.session.mrap;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.factory.EomType;
import com.mlink.iwm.entity.factory.JobCat;
import com.mlink.iwm.entity.factory.JobStatus;

@Name("reportManager")
public class ReportManager {
	@Logger
	private Log log;

	public String activityCode;
	public JobCat category;
	public String criteria = "Sample";
	public String defectCode;
	public EomType eom;
	public String ero;
	public String keyword; 
	public String model;
	public String owningOrganization;
	public Integer priorityLabel;
	public String qualification;
	public String serial;
	public JobStatus status;
	public String tam;
	public String technician;
	public String vId;
	public String workItem;
	
	public Date closedDateFrom;
	public Date closedDateTo;
	public Date createdDateFrom;
	public Date createdDateTo;
	public Date receivedInShopDateFrom;
	public Date receivedInShopDateTo;
	public Date startWorkDateFrom;
	public Date startWorkDateTo;
	
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public JobCat getCategory() {
		return category;
	}
	public void setCategory(JobCat category) {
		this.category = category;
	}
	public JobCat[] getCategories() {
		return JobCat.values();
	}
	public String getCriteria() {
		return criteria;
	}
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	public String getDefectCode() {
		return defectCode;
	}
	public void setDefectCode(String defectCode) {
		this.defectCode = defectCode;
	}
	public EomType getEom() {
		return eom;
	}
	public void setEom(EomType eom) {
		this.eom = eom;
	}
	public EomType[] getEoms() {
		return EomType.values();
	}
	public String getEro() {
		return ero;
	}
	public void setEro(String ero) {
		this.ero = ero;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getOwningOrganization() {
		return owningOrganization;
	}
	public void setOwningOrganization(String owningOrganization) {
		this.owningOrganization = owningOrganization;
	}
	public Integer getPriorityLabel() {
		return priorityLabel;
	}
	public void setPriorityLabel(Integer priorityLabel) {
		this.priorityLabel = priorityLabel;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public JobStatus getJobStatus() {
		return status;
	}
	public void setJobStatus(JobStatus status) {
		this.status = status;
	}
	public List<JobStatus> getJobStatuses() {
		ArrayList<JobStatus> al = new ArrayList<JobStatus>();
		al.addAll(Arrays.asList(JobStatus.values()));
		return al;
	}
	public String getTam() {
		return tam;
	}
	public void setTam(String tam) {
		this.tam = tam;
	}
	public String getTechnician() {
		return technician;
	}
	public void setTechnician(String technician) {
		this.technician = technician;
	}
	public String getvId() {
		return vId;
	}
	public void setvId(String vId) {
		this.vId = vId;
	}
	public String getWorkItem() {
		return workItem;
	}
	public void setWorkItem(String workItem) {
		this.workItem = workItem;
	}
	public Date getClosedDateFrom() { return closedDateFrom; }
	public void setClosedDateFrom(Date d) {	this.closedDateFrom = d; }
	
	public Date getClosedDateTo() { return closedDateTo; }
	public void setClosedDateTo(Date d) { this.closedDateTo = d; }
	
	public Date getCreatedDateFrom() { return createdDateFrom; }
	public void setCreatedDateFrom(Date d) { this.createdDateFrom = d; }
	
	public Date getCreatedDateTo() { return createdDateTo; }
	public void setCreatedDateTo(Date d) { this.createdDateTo = d; }
	
	public Date getReceivedInShopDateFrom() { return receivedInShopDateFrom; }
	public void setReceivedInShopDateFrom(Date d) { this.receivedInShopDateFrom = d; }
	
	public Date getReceivedInShopDateTo() { return receivedInShopDateTo; }
	public void setReceivedInShopDateTo(Date d) { this.receivedInShopDateTo = d; }
	
	public Date getStartWorkDateFrom() { return startWorkDateFrom; }
	public void setStartWorkDateFrom(Date d) { this.startWorkDateFrom = d; }
	
	public Date getStartWorkDateTo() { return startWorkDateTo; }
	public void setStartWorkDateTo(Date d) { this.startWorkDateTo = d; }
	
	public void add() {
		
	}
	public String search() {
		return "";
	}
}
