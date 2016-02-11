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
		return  ( (cal.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)) &&
				  (cal.get(Calendar.MONTH)        == c.get(Calendar.MONTH)) && 
				  (cal.get(Calendar.YEAR)         == c.get(Calendar.YEAR)) );
	}
}