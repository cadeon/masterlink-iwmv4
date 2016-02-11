package com.mlink.iwm.scheduler.model;

public class ProcessingError {
	int severity;
	int type;
	long value;
	String msg;
	
	public void setSeverity(int sev) {
		severity = sev;
	}
	
	public void setType(int typ) {
		type = typ;
	}
	
	public void setValue(long val) {
		value = val;
	}
	
	public void setMessage(String message) {
		msg = message;
	}
	
	public int getSeverity() {
		return severity;
	}
	
	public long getValue() {
		return value;
	}
	
	public int getType() {
		return type;
	}
	
	public String getMessage() {
		return msg;
	}
}
