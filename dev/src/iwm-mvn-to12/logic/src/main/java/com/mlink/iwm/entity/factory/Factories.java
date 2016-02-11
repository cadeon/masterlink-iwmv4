package com.mlink.iwm.entity.factory;


import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;

@Name("factories")
public class Factories {

	
	 @Factory("assetKinds")
	   public AssetKind[] getAssetKinds() {
	      return AssetKind.values();
	   }
	
	 @Factory("phoneTypes")
	   public PhoneType[] getPhoneTypes() {
	      return PhoneType.values();
	   }
	
	 @Factory("orgTypes")
	   public OrgType[] getOrgTypes() {
	      return OrgType.values();
	   }
	
	 @Factory("states")
	   public State[] getStates() {
	      return State.values();
	   }
	 
	 @Factory("addressTypes")
	   public AddressType[] getAddressTypes() {
	      return AddressType.values();
	   }
	 
	 @Factory("jobStats")
	   public JobStatus[] getJobStatuses() {
	      return JobStatus.values();
	   }
	 
	 @Factory("jobTypes")
	   public JobType[] getJobTypes() {
	      return JobType.values();
	   }

	 
	 @Factory("priorities")
	   public Priority[] getPriorities() {
	      return Priority.values();
	   }
		
	 @Factory("requestTypes")
	   public RequestType[] getRequestTypes() {
	      return RequestType.values();
	   }
	 
	 @Factory("nonAssetTypes")
	   public NonAssetType[] getNonAssetTypes() {
	      return NonAssetType.values();
	   } 
	 
	 @Factory("taskTypes")
	   public TaskType[] getTaskTypes() {
	      return TaskType.values();
	   } 
	 
	 @Factory("expiryTypes")
	   public ExpiryType[] getExpiryTypes() {
	      return ExpiryType.values();
	   } 
	 
	 @Factory("freqTypes")
	   public FreqType[] getFreqTypes() {
	      return FreqType.values();
	   } 
	 
	 
}

