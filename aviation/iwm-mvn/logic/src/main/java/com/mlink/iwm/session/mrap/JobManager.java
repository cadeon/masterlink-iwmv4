package com.mlink.iwm.session.mrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Out;

import org.jboss.seam.log.Log;
import org.jboss.seam.ScopeType;

import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.session.job.JobList;
import com.mlink.iwm.util.DateUtils;

@Name("jobManager")
@Scope(ScopeType.SESSION)
public class JobManager implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String IN_PROGRESS_STATUS = "RPR PRGS";
	public static final int BY_STATUS = 0;
	public static final int BY_PAST_DUE = 1;
	public static final int BY_MISSING_DATA = 2;

	public static final int SCOPE_DAY = 3;
	public static final int SCOPE_WEEK = 4;
	public static final int SCOPE_MONTH = 5;
	public static final int SCOPE_YEAR = 6;

	
	// The JobList attribute below should be instantiated by 
	// injection from the Seam context
	@In(create=true, required=false)
	private JobList jobList;
	
	
	@Logger
	private static Log log;
	
	@Out
	Job job = new Job();
	
	private List<MJob> mJobList = new ArrayList<MJob>(); // the list returned for
														// screen display
	private HashMap<String, ArrayList<MJob>> jobsByStatus = new HashMap<String, ArrayList<MJob>>();
	private HashMap<String, ArrayList<MJob>> jobsPastDue = new HashMap<String, ArrayList<MJob>>();
	private HashMap<String, ArrayList<MJob>> jobsMissingData = new HashMap<String, ArrayList<MJob>>();
	private Integer numInProgress, numPastDue, numMissingData;
	private Calendar calendar;
    private String pattern = "MM/dd/yyyy";  

	private int currentScope = SCOPE_DAY;
	
	private Scheduler scheduler = new Scheduler();

	public JobManager() {
		// First instantiation of job list
		// not really needed due to the code
		// in sortJobs()
		jobList = new JobList();
		reset();
		sortByIP();
	}

	
	public List<MJob> getJobs() { return mJobList;}
	public Integer getNumInProgress() {	return numInProgress;}
	public Integer getNumPastDue() { return numPastDue;	}
	public Integer getNumMissingData() { return numMissingData;	}
	public Date    getSelectedDate() { return calendar.getTime(); }
	public String  getDatePattern() { return pattern; }
	public String  getDisplayScope() {
		StringBuffer sb = new StringBuffer();
		switch(currentScope) {
			case SCOPE_DAY  :
				sb.append(" ");
				// if (DateUtils.isToday(calendar)) sb.append("today, ");
				sb.append(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
				sb.append(" ");
				sb.append(alterCalendar(SCOPE_DAY));
				break;
			case SCOPE_WEEK  :
				sb.append(" the week of ");
				sb.append(alterCalendar(SCOPE_WEEK));
				break;
			case SCOPE_MONTH  :
				sb.append(" the month of ");
				sb.append(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
				sb.append(", ");
				sb.append(alterCalendar(SCOPE_MONTH));
				break;
			case SCOPE_YEAR  :
				sb.append(" ");
				sb.append(alterCalendar(SCOPE_YEAR));
				break;
			default : 
				break;
		}
		sb.append(". ");
		return sb.toString();
	}
	public String getToday() {
		return DateUtils.getTodayDisplayString();		
	}
	public boolean getIsDayScope() { return currentScope==SCOPE_DAY;}
	public boolean getIsWeekScope() { return currentScope==SCOPE_WEEK;}
	public boolean getIsMonthScope () { return currentScope==SCOPE_MONTH;}
	public boolean getIsYearScope() { return currentScope==SCOPE_YEAR;}

	public void setScopeDay()   { currentScope=SCOPE_DAY;   }
	public void setScopeWeek()  { currentScope=SCOPE_WEEK;  }
	public void setScopeMonth() { currentScope=SCOPE_MONTH; }
	public void setScopeYear()  { currentScope=SCOPE_YEAR;  }
	public void setSelectedDate(Date d) { calendar.setTime(d); }
	
	public String addJob() {
		job = new Job();
		return "addJob";
	}
	public void help() {} // display faq	
	public String reschedule() {
		log.info("Commencing scheduler run...");
		scheduler.runDemoScheduler();
		log.info("...scheduling complete.");
		return "rescheduled";
	}
	public void update() { sortByIP(); }
	

	
	/*** Sort Helpers ***/
	public void sortByIP() {
		sortJobs(BY_STATUS);
		mJobList.addAll(retrieveJobs(jobsByStatus));
	}

	public void sortByPastDue() {
		sortJobs(BY_PAST_DUE);
		mJobList.addAll(retrieveJobs(jobsPastDue));
		mJobList.addAll(retrieveJobs(jobsByStatus));
	}

	public void sortByMissingData() {
		sortJobs(BY_MISSING_DATA);
		mJobList.addAll(retrieveJobs(jobsMissingData));
		mJobList.addAll(retrieveJobs(jobsByStatus));
	}

	private void sortJobs(int orderby) {
		reset();
		//FIXME: Send in date range for jobList.getResultsList(fromDate, toDate)
		
		// Check if job list is null. "(jobList==null)" evaluates to true when one
		// of the sort links is clicked, e.g. "6 jobs in progress"
		//
		// This behavior looks like something is request scope rather than 
		// session scope.
		//
		// Note, however, that clicking "Day", "Week", "Month", "Year" 
		// does not trigger the re-instantiation.
		if (jobList==null) { 
			//nadia 
			//using log to get output on the Eclipse cosole as well;
			log.info("ERROR: JobList accessor is NULL");
			//System.err.println("ERROR: JobList accessor is NULL");
		} 
		// Now we can request the list of jobs
		for (Job j : jobList.getAll()) {
			String jobstatus = j.getStatus().getLabel();
			MJob mj = MJob.copyJob(j);
			if (isPastDue(mj)) {
				if (orderby == BY_PAST_DUE) { makeEntry(jobsPastDue, mj, jobstatus); }
				else { makeEntry(jobsByStatus, mj, jobstatus); }
				numPastDue++;
			} else if (isMissingData(mj)) {
				if (orderby == BY_MISSING_DATA) { makeEntry(jobsMissingData, mj, jobstatus); }
				else { makeEntry(jobsByStatus, mj, jobstatus); }
				numMissingData++;
			} else { // put in default status-sorted map
				makeEntry(jobsByStatus, mj, jobstatus);
			}
		}
	}

	private void reset() {
		// re-initialize lists & counts
		resetCalendar();
		resetCounts();
		resetLists();
	}

	/*** Resets ***/
	private void resetCalendar() {
		calendar = Calendar.getInstance();
	}

	private void resetCounts() {
		numInProgress = 0;
		numPastDue = 0;
		numMissingData = 0;
	}

	private void resetLists() {
		
		mJobList.clear();
		jobsByStatus.clear();
		jobsPastDue.clear();
		jobsMissingData.clear();
		

	}

	/*** List Helpers ***/
	/**
	 * Collects jobs, sorted into buckets by status
	 */
	private void makeEntry(HashMap<String, ArrayList<MJob>> hm, MJob mj, String status) {
		if (isInProgress(status))
			numInProgress++; // update here, as an IP job can also be past due

		if (hm.containsKey(status))
			hm.get(status).add(mj); // bucket exists, add
		else { // need to add new status bucket
			ArrayList<MJob> al = new ArrayList<MJob>();
			al.add(mj);
			hm.put(status, al);
		}
	}

	/**
	 * Extracts list of all the jobs in the specified map, sorted by status
	 */
	private List<MJob> retrieveJobs(HashMap<String, ArrayList<MJob>> hm) {
		ArrayList<MJob> al = new ArrayList<MJob>();
		for (String s : hm.keySet()) {
			if (IN_PROGRESS_STATUS.equalsIgnoreCase(s)) {
				al.addAll(0,hm.get(s));
			} else {
				al.addAll(hm.get(s));
			}
		}
		return al;
	}

	
	/*** Status Link Helpers ***/
	private boolean isInProgress(String s) {
		return IN_PROGRESS_STATUS.equals(s);
	}

	private boolean isPastDue(MJob mj) {
		Calendar c = Calendar.getInstance();
		if (null == mj.getCompleteBy())
			return false;
		c.setTime(mj.getCompleteBy());
		return calendar.after(c);
	}

	private boolean isMissingData(MJob mj) {
		if (mj.getEro() == null || mj.getEro().length() < 1) return true; 
		else if (mj.getSerial() == null || mj.getSerial().length() < 1) return true; // serial sets TAM, ID, & Model 
		else if (mj.getEstTime() == 0) return true;
		return false;
	}
	
	/*** Date Helpers ***/
	private String alterCalendar(int scope) {
		StringBuffer sb = new StringBuffer();
		// Set to appropriate Scope, 
		// week = Sunday of week, month = 1st of month, year = Jan 1 of year
		if (SCOPE_DAY == scope ) {
			sb.append(DateUtils.get4CharDate(calendar.getTime()));
		}
		if (SCOPE_WEEK == scope) {
			calendar.add(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() - calendar.get(Calendar.DAY_OF_WEEK));
			sb.append(DateUtils.get4CharDate(calendar.getTime()));
		}
		if (SCOPE_MONTH == scope) {
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			sb.append(DateUtils.get4CharDate(calendar.getTime()));
		}
		if (SCOPE_YEAR == scope) {
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			sb.append(calendar.get(Calendar.YEAR));
		}
		return sb.toString();
	}
	
}

