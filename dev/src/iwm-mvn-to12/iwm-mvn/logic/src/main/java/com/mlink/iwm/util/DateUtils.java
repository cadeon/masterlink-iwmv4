package com.mlink.iwm.util;

import java.util.Calendar;
import java.util.Date;
//import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtils {
  //public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	 public static Date now() {
	   Calendar cal = Calendar.getInstance();
	   return cal.getTime();
	
	 }
	 
	public static String get4CharDate(Date d) {
		if (d==null) return "";
		StringBuffer sb = new StringBuffer();
		// generate four-digit representation of date
		// e.g. last day of year & number of days to date, ex. 276th day
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int yr = c.get(Calendar.YEAR) % 10; // get least sig digit of year
		int numDays = c.get(Calendar.DAY_OF_YEAR); 
		sb.append(yr);
		sb.append(numDays);
		return sb.toString();
	}
	
	public static String getTodayDisplayString() {
		Calendar c = Calendar.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
		sb.append(", ");
		sb.append(get4CharDate(c.getTime()));
		return sb.toString();
	}
	
	public static boolean isToday(Calendar cal) {
		Calendar c = Calendar.getInstance();	
		return isSameDay(cal, c);
	}
	
	public static boolean isSameDay(Calendar dayOne, Calendar dayTwo) {
		return  ( (dayOne.get(Calendar.DAY_OF_MONTH) == dayTwo.get(Calendar.DAY_OF_MONTH)) &&
				  (dayOne.get(Calendar.MONTH)        == dayTwo.get(Calendar.MONTH)) && 
				  (dayOne.get(Calendar.YEAR)         == dayTwo.get(Calendar.YEAR)) );
	}
	
	public static boolean isTodayInRange(Date lower, Date upper) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(lower);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(upper);
		boolean isAfterStartDate = lower.before(DateUtils.now()) || DateUtils.isToday(startCal);
		boolean isBeforeEndDate = upper.after(DateUtils.now()) || DateUtils.isToday(endCal);
		return isAfterStartDate && isBeforeEndDate;
	}

}