package com.mlink.iwm.session.services;


import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.In;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;


import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.session.job.JobHome;
import com.mlink.iwm.session.job.JobList;



 @Name("jobEntityService")
public  class JobEntityService //extends AbstractEntityListServiceBean<Job>{
{
	@In(create = true, required = false)
	JobHome jobHome;
	
	@In(create = true, required = false)
	JobList jobList;
	
	@In(required = false)
	@Out(required = false)
	private Job job;

	//private List<Job> resultList;
	
	
	/*
	 * (non-Javadoc)
	 *  com.mlink.iwm.framework.IAbstractEntityService#find(long)
	 */
	public final Job find(long id) {
		jobHome.setId(id);
		return jobHome.find();
		
	}
	
	/**
	 * a restriction could be something like
     * 'f.bar = #{foo.bar}'
     * 
	 * @param jobStat
	 * @return
	 */
	public final List<Job> findByStatus(String jobStat) {
		
		String[] rest = {"(job.status.value) like "+ jobStat};
		
		jobList.setRestrictionExpressionStrings(Arrays.asList(rest));
		return jobList.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mlink.iwm.framework.IAbstractEntityService#delete(com.mlink.iwm.entity.AbstractEntity)
	 */
	public final String delete(Job entity) {
		jobHome.setInstance(job);
	     return jobHome.remove(); 
	}

	
	public final String saveOrUpdate(Job entity) {
		jobHome.setInstance(job);
		 return jobHome.update(); 
	}


	
	
	

	
	
	
}
