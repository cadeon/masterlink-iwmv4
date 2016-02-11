package com.mlink.iwm.session.job;

import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.factory.JobStatus;
import com.mlink.iwm.entity.job.Job;


@SuppressWarnings("serial")  
 @Name("jobList")
public class JobList extends EntityQuery<Job> {

	private static final String SELECT = "select job from Job job";


	@Logger
	public Log log;
	private List<Job> resultList;
	
	/*private static final String[] RESTRICTIONS = {
			"lower(job.createdBy) like lower(concat(#{jobList.job.createdBy},'%'))",
			"lower(job.description) like lower(concat(#{jobList.job.description},'%'))",
			"lower(job.ero) like lower(concat(#{jobList.job.ero},'%'))",
			"lower(job.note) like lower(concat(#{jobList.job.note},'%'))",};*/

	private Job job = new Job();

	public JobList() {
		setEjbql(SELECT);
		//setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Job getJob() {
		return job;
	}
	


	@SuppressWarnings("unchecked")
	public final List<Job> findByStatus(String status) {
	
		refresh();
		String qs= SELECT + " where job.status = :status";

		try {

			Query query = getEntityManager().createQuery(qs).setParameter("status", JobStatus.valueOf(status)) ;
			resultList= query.getResultList();
			
		} catch (Exception ex) {
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		return resultList;
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	public final List<Job> getAll() {

		refresh();
		try {

			Query query = getEntityManager().createQuery(SELECT);
			resultList= query.getResultList();
			
		} catch (Exception ex) {
			log.info("SQL ERROR:"+ex.getMessage());
				
		}
		return resultList;
		
	}




	@Override
	public void refresh() {
		resultList=null;
		
	}

	
	
	public void wire() {
		new JobList();
	}
	


	
}
