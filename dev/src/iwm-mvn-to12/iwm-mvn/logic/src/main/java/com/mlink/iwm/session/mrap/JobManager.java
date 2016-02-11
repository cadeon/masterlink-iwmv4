package com.mlink.iwm.session.mrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.mlink.iwm.entity.contact.Organization;
import com.mlink.iwm.entity.factory.JobStatus;
import com.mlink.iwm.entity.job.Job;
import com.mlink.iwm.session.asset.AssetHome;
import com.mlink.iwm.session.asset.AssetList;
import com.mlink.iwm.session.contact.ContactHome;
import com.mlink.iwm.session.contact.OrganizationHome;
import com.mlink.iwm.session.job.JobHome;
import com.mlink.iwm.session.job.JobList;
import com.mlink.iwm.util.DateUtils;

@Name("jobManager")
@Scope(ScopeType.SESSION)
public class JobManager implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * JS7("SHT PART"), JS8("SHT TEST"), JS9("SHT SPAC"), JS10("SHT TECH"),
	 * JS11("SHT FUND"),
	 */
	public static final String IN_PROGRESS_STATUS = "RPR PRGS";
	public static final String IN_SHORT_FUNDS_STATUS = "SHT FUND";
	public static final String IN_SHORT_PARTS_STATUS = "SHT PART";
	public static final String IN_SHORT_SPACE_STATUS = "SHT SPAC";
	public static final String IN_SHORT_TECH_STATUS = "SHT TECH";
	public static final String IN_SHORT_TEST_STATUS = "SHT TEST";

	public static final String DATE_HEADING = "Manage maintenance for ";
	public static final int BY_STATUS = 0;
	public static final int BY_PAST_DUE = 1;
	public static final int BY_MISSING_DATA = 2;
	public static final int BY_SHORT = 3; // Short funds/parts/technician/

	public static final int SCOPE_DAY = 3;
	public static final int SCOPE_WEEK = 4;
	public static final int SCOPE_MONTH = 5;
	public static final int SCOPE_YEAR = 6;

	public static final long JOB_ORGANIZATION = 1L;
	public static String HEADLINE = "Manage Maintenance for 20000 H";
	
	@In(create = true, required = false)
	AssetHome assetHome;
	@In(create = true, required = false)
	AssetList assetList;
	@In(create = true, required = false)
	ContactHome contactHome;
	@In(create = true, required = false)
	private JobHome jobHome;
	@In(create = true, required = false)
	private JobList jobList;
	@In(create = true, required = false)
	OrganizationHome organizationHome;

	@Logger
	private Log log;

	private List<MJob> mJobList = new ArrayList<MJob>(); // the list returned
															// for
															// screen display
	private HashMap<String, ArrayList<MJob>> jobsByStatus = new HashMap<String, ArrayList<MJob>>();
	private HashMap<String, ArrayList<MJob>> jobsPastDue = new HashMap<String, ArrayList<MJob>>();
	private HashMap<String, ArrayList<MJob>> jobsMissingData = new HashMap<String, ArrayList<MJob>>();
	private HashMap<String, ArrayList<MJob>> jobsShort = new HashMap<String, ArrayList<MJob>>();
	private Integer numInProgress, numPastDue, numMissingData, numShort;
	private Calendar calendar;
	private Calendar userSelectedDate;
	private String pattern = "MM/dd/yyyy";

	private int currentScope = SCOPE_DAY;
	private int currentSortOrder = BY_STATUS;

	private Scheduler scheduler = new Scheduler();

	public JobManager() {
		userSelectedDate = Calendar.getInstance();
		calendar = Calendar.getInstance();
		reset();
		sortByIPStatus();
	}

	public String getHeadline() {
		return HEADLINE;
	}
	public List<MJob> getJobs() {
		sortJobs(currentSortOrder);
		return mJobList;
	}

	public Integer getNumInProgress() {
		return numInProgress;
	}

	public Integer getNumPastDue() {
		return numPastDue;
	}

	public Integer getNumMissingData() {
		return numMissingData;
	}

	public Integer getNumShort() {
		return numShort;
	}

	public Date getSelectedDate() {
		return calendar.getTime();
	}

	public String getDatePattern() {
		return pattern;
	}

	public String getDisplayScope() {
		StringBuffer sb = new StringBuffer();
		switch (currentScope) {
		case SCOPE_DAY:
			sb.append(DATE_HEADING);
			// if (DateUtils.isToday(calendar)) sb.append("today, ");
			sb.append(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
			sb.append(" ");
			sb.append(alterCalendar(SCOPE_DAY));
			break;
		case SCOPE_WEEK:
			sb.append(DATE_HEADING);
			sb.append(" the week of ");
			sb.append(alterCalendar(SCOPE_WEEK));
			break;
		case SCOPE_MONTH:
			sb.append(DATE_HEADING);
			sb.append(" the month of ");
			sb.append(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
			sb.append(", ");
			sb.append(alterCalendar(SCOPE_MONTH));
			break;
		case SCOPE_YEAR:
			sb.append(DATE_HEADING);
			sb.append(alterCalendar(SCOPE_YEAR));
			break;
		default:
			break;
		}
		sb.append(". ");
		return sb.toString();
	}

	public String getToday() {
		return DateUtils.getTodayDisplayString();
	}

	public boolean getIsDayScope() {
		return currentScope == SCOPE_DAY;
	}

	public boolean getIsWeekScope() {
		return currentScope == SCOPE_WEEK;
	}

	public boolean getIsMonthScope() {
		return currentScope == SCOPE_MONTH;
	}

	public boolean getIsYearScope() {
		return currentScope == SCOPE_YEAR;
	}

	public void setScopeDay() {
		currentScope = SCOPE_DAY;
		setCalenderToPivotDate();
		sortByIPStatus();
	}

	public void setScopeWeek() {
		currentScope = SCOPE_WEEK;
		setCalenderToPivotDate();
		sortByIPStatus();
	}

	public void setScopeMonth() {
		currentScope = SCOPE_MONTH;
		setCalenderToPivotDate();
		sortByIPStatus();
	}

	public void setScopeYear() {
		currentScope = SCOPE_YEAR;
		setCalenderToPivotDate();
		sortByIPStatus();
	}

	public void setSelectedDate(Date d) {
		calendar.setTime(d);
	}

	public void setUserSelectedJobId(String userSelectedJobId) {
		jobHome.setId(Long.parseLong(userSelectedJobId));
	}

	public String getUserSelectedJobId() {
		return jobHome.getId().toString();
	}

	public String addJob() {
		jobHome.clearInstance();
		assetHome.clearInstance();
		contactHome.clearInstance();
		organizationHome.clearInstance();
		return "addJob";
	}

	public void help() {
	} // display faq

	public void reschedule() throws InterruptedException {
		log.info("Commencing scheduler run...");
		scheduler.runDemoScheduler();
		jobHome.clearEntityManager();
		log.info("...scheduling complete.");
	}

	public void update() {
		userSelectedDate.setTime(calendar.getTime());
		sortByIPStatus();
	}

	public String onJobRowClicked() {
		log.info("...onjobrow: job id is " + jobHome.getId());
		if (jobHome.getInstance().getMaintainedAsset()!=null) { 
			// set home variables to maintained asset values
			log.info("...asset id is "+jobHome.getInstance().getMaintainedAsset().getId());
			assetHome.setId(jobHome.getInstance().getMaintainedAsset().getId());
			organizationHome.setId(jobHome.getInstance().getOrganization().getId());
		}
		else {
			assetHome.clearInstance(); // Maintained asset not yet assigned
		}
		contactHome.setInstance(jobHome.getInstance().getContact());
		return "jobEditorRedirect";
	}

	/*** Sort Helpers ***/
	public void sortByIPStatus() {
		currentSortOrder = BY_STATUS;
	}

	public void sortByPastDue() {
		currentSortOrder = BY_PAST_DUE;
	}

	public void sortByMissingData() {
		currentSortOrder = BY_MISSING_DATA;
	}

	public void sortByShortStatus() {
		currentSortOrder = BY_SHORT;
	}

	private void sortJobs(int orderby) {
		reset();

		if (jobList == null) {
			// nadia
			// using log to get output on the Eclipse console as well;
			log.info("ERROR: JobList accessor is NULL");
			jobList = (JobList) Component.getInstance(JobList.class);
		}
		// Now we can request the list of jobs
		List<Job> jobs = jobList.findByDueDateRange(getLowerDate(), getUpperDate());
		for (Job j : jobs) {
			if (j.getStatus() == null) { // Job status should not be null
				j.setStatus(JobStatus.JS1); // Set to default status
			}
			String jobstatus = j.getStatus().getLabel();
			MJob mj = MJob.copyJob(j);
			switch (orderby) {
			case BY_PAST_DUE:
				if (isPastDue(mj)) {
					makeEntry(jobsPastDue, mj, jobstatus);
				} else
					makeEntry(jobsByStatus, mj, jobstatus);
				break;
			case BY_MISSING_DATA:
				if (isMissingData(mj)) {
					makeEntry(jobsMissingData, mj, jobstatus);
				} else
					makeEntry(jobsByStatus, mj, jobstatus);
				break;
			case BY_SHORT:
				if (isInShortStatus(jobstatus)) {
					makeEntry(jobsShort, mj, jobstatus);
				} else
					makeEntry(jobsByStatus, mj, jobstatus);
				break;
			case BY_STATUS:
			default:
				makeEntry(jobsByStatus, mj, jobstatus);
				break;
			}
		}

		switch (orderby) {
		case BY_PAST_DUE:
			mJobList.addAll(retrieveJobs(jobsPastDue));
			mJobList.addAll(retrieveJobs(jobsByStatus));
			break;
		case BY_MISSING_DATA:
			mJobList.addAll(retrieveJobs(jobsMissingData));
			mJobList.addAll(retrieveJobs(jobsByStatus));
			break;
		case BY_SHORT:
			mJobList.addAll(retrieveJobs(jobsShort));
			mJobList.addAll(retrieveJobs(jobsByStatus));
			break;
		case BY_STATUS:
		default:
			mJobList.addAll(retrieveJobs(jobsByStatus));
			break;
		}
	}

	private void reset() {
		// re-initialize lists & counts
		resetCounts();
		resetLists();
	}

	/*** Resets ***/
	private void resetCounts() {
		numInProgress = 0;
		numPastDue = 0;
		numMissingData = 0;
		numShort = 0;
	}

	private void resetLists() {
		mJobList.clear();
		jobsByStatus.clear();
		jobsPastDue.clear();
		jobsMissingData.clear();
		jobsShort.clear();
	}

	/*** List Helpers ***/
	/**
	 * Collects jobs, sorted into buckets by status
	 */
	private void makeEntry(HashMap<String, ArrayList<MJob>> hm, MJob mj, String status) {
		if (isInProgress(status))
			numInProgress++;
		if (isMissingData(mj))
			numMissingData++;
		if (isPastDue(mj))
			numPastDue++;
		if (isInShortStatus(status))
			numShort++;

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
		// Nadia 06/22/2011 changed from KeyList to keySet
		for (String s : hm.keySet()) {
			if (IN_PROGRESS_STATUS.equalsIgnoreCase(s)) {
				al.addAll(0, hm.get(s));
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
		// must check if it's the same day, as Calendar.after() returns true
		// when both dates are the same.
		boolean isSameDay = ((calendar.get(Calendar.ERA) == c.get(Calendar.ERA))
				&& (calendar.get(Calendar.YEAR) == c.get(Calendar.YEAR)) && (calendar.get(Calendar.DAY_OF_YEAR) == c
				.get(Calendar.DAY_OF_YEAR)));
		return calendar.after(c) && !isSameDay;
	}

	private boolean isMissingData(MJob mj) {
		if (mj.getEro() == null || mj.getEro().length() < 1)
			return true;
		else if (mj.getSerial() == null || mj.getSerial().length() < 1)
			return true; // serial sets TAM, ID, & Model
		else if (mj.getEstTime() == 0)
			return true;
		return false;
	}

	private boolean isInShortStatus(String s) {
		return (IN_SHORT_FUNDS_STATUS.equals(s) || IN_SHORT_PARTS_STATUS.equals(s) || IN_SHORT_SPACE_STATUS.equals(s)
				|| IN_SHORT_TECH_STATUS.equals(s) || IN_SHORT_TEST_STATUS.equals(s));
	}

	/*** Date Helpers ***/
	private String alterCalendar(int scope) {
		StringBuffer sb = new StringBuffer();

		// List to appropriate Scope,
		// week = Sunday of week, month = 1st of month, year = Jan 1 of year
		setCalenderToPivotDate();
		if (SCOPE_DAY == scope) {
			sb.append(DateUtils.get4CharDate(calendar.getTime()));
		}
		if (SCOPE_WEEK == scope) {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
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

	private void setCalenderToPivotDate() {
		calendar.setTime(userSelectedDate.getTime());
	}

	private Date getLowerDate() {
		Calendar lowerDate = Calendar.getInstance();
		lowerDate.setTime(calendar.getTime());
		switch (currentScope) {
		case SCOPE_WEEK:
			lowerDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			break;
		case SCOPE_MONTH:
			lowerDate.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case SCOPE_YEAR:
			lowerDate.set(Calendar.DAY_OF_YEAR, 1);
			break;
		}
		return lowerDate.getTime();
	}

	private Date getUpperDate() {
		Calendar upperDate = Calendar.getInstance();
		upperDate.setTime(calendar.getTime());
		switch (currentScope) {
		case SCOPE_WEEK:
			upperDate.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			break;
		case SCOPE_MONTH:
			upperDate.add(Calendar.MONTH, 1);
			upperDate.set(Calendar.DAY_OF_MONTH, 1);
			upperDate.add(Calendar.DATE, -1);
			break;
		case SCOPE_YEAR:
			upperDate.add(Calendar.YEAR, 1);
			upperDate.set(Calendar.DAY_OF_YEAR, 1);
			upperDate.add(Calendar.DATE, -1);
			break;
		}
		return upperDate.getTime();
	}
}
