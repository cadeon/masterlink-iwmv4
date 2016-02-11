package com.mlink.iwm.logic;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.jboss.seam.annotations.In;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mlink.iwm.entity.factory.JobStatus;
import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.session.job.JobHome;
import com.mlink.iwm.session.job.JobList;

//import de.akquinet.jbosscc.needle.db.InMemoryDatabaseUtil;

public class JobEntityPersistenceTest {

    //private InMemoryDatabaseUtil databaseUtil;

	//Required declaration 
	@In(create=true, required = false)
	private JobHome jobHome;
	@In(create=true, required = false)
	private JobList jobList;
	
	
	@Before
	public void setUp() {
		//clear all from db
//		databaseUtil = new InMemoryDatabaseUtil();
	}
	
	@Test
	public void saveOrUpdateTest() throws Exception {

		Job job= new Job();
		
        job.setCreatedBy("Doud");
		Assert.assertNull(jobHome.getJobId());
		jobHome.saveOrUpdate(job);
		Assert.assertNotNull(job.getId());
		jobHome.delete(job);
		

	}
	
	@Test
	public void findByStatusTest() throws Exception {

		Job job= new Job();
        job.setStatus(JobStatus.JS7);
		jobHome.saveOrUpdate(job);
		job = jobHome.find(jobHome.getJobId());
		List<Job> results = jobList.findByStatus(JobStatus.JS7.toString());
		Assert.assertTrue(results.contains(job));	
		jobHome.delete(job);
	}
	
	@Test
	public void findAllTest() throws Exception {
        //assumes SetUp()- 
		Job job1= new Job();
        job1.setStatus(JobStatus.JS7);
        
		Job job2= new Job();
        job2.setStatus(JobStatus.JS11);
        
		jobHome.saveOrUpdate(job1);
		jobHome.saveOrUpdate(job2);
		//Asset sice of findAll is 2
			
		jobHome.delete(job1);
		jobHome.delete(job2);
	}
	
	@Test
	public void deleteTest() throws Exception {

		Job job= new Job();
		
        job.setCreatedBy("Deleted");
        jobHome.saveOrUpdate(job);	
	    jobHome.delete(job) ;
	    job=jobHome.find();
		Assert.assertNull(jobHome.find(job.getId()));	
		jobHome.delete(job);

	}
	

	@After
	public void tearDown(){
	//TODO	
	//	databaseUtil.deleteAll();
	}

}
