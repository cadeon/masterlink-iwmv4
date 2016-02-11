package com.mlink.mrap.entity.factory;


import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;



@Name("mrapFactories")
public class MrapFactories {

	
	 
	 @Factory("eoms")
	   public EomType[] getEomTypes() {
	      return EomType.values();
	   } 
	 
	 
	 @Factory("jobCats")
	   public JobCat[] getJobCats() {
	      return JobCat.values();
	   } 
}

