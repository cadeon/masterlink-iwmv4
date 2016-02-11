package com.mlink.iwm.scheduler.model;

public class ProcessingError {
	int severity;
	String msg;
	
	public void setSeverity(int sev) {
		severity = sev;
	}
	
	public void setMessage(String message) {
		msg = message;
	}
	
	public int getSeverity() {
		return severity;
	}
	
	public String getMessage() {
		return msg;
	}
}
