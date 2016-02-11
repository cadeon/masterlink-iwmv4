package com.mlink.iwm.session.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.factory.JobStatus;
import com.mlink.iwm.entity.job.Job;


 @Name("jobListService")
@Scope(ScopeType.CONVERSATION)
public final class JobEntityListService //extends AbstractEntityListServiceBean<Job> 
 {
	
	private static final String SELECT = "select job from Job job";

	@Logger
	private Log log;
	@In
	private EntityManager entityManager;
	private List<Job> resultList;


	
	@SuppressWarnings("unchecked")
	public final List<Job> findByStatus(String status) {
	
		resultList = null;;
		String qs= JobEntityListService.SELECT + " where job.status = :status";

		try {

			Query query = entityManager.createQuery(qs).setParameter("status", JobStatus.valueOf(status)) ;
			resultList= query.getResultList();
			
		} catch (Exception ex) {
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		return resultList;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public final List<Job> getAll() {

		
		resultList = null;;
		try {

			Query query = entityManager.createQuery(JobEntityListService.SELECT);
			resultList= query.getResultList();
			
		} catch (Exception ex) {
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		return resultList;
		
	}



}
