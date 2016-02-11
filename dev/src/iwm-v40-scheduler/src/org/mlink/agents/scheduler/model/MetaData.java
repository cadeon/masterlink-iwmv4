package org.mlink.agents.scheduler.model;

import java.util.*;

public class MetaData {
	String description="";
	String input="";
	String output="";
    Long periodStart;
    Long periodEnd;
    String topLevelLocatorId;
    Long conversionFactor;
    String statusVector;
    Integer kValue;
    Integer maxPriority;
    String travelVector;
    Integer travelImportance;
    Integer travelFunction;

	public String getDescription() { return description;}
	public String getInput() { return input;}
	public String getOutput() { return output;}
    public Long getPeriodStart() { return periodStart;}
    public Long getPeriodEnd() { return periodEnd;}
    public String getTopLevelLocatorId() { return topLevelLocatorId;}
    public Long getConversionFactor() { return conversionFactor;}
    public String getStatusVector() { return statusVector;}
    public Integer getKValue() { return kValue;}
    public Integer getMaxPriority() { return maxPriority;}
    public String getTravelVector() { return travelVector;}
    public Integer getTravelImportance() { return travelImportance;}
    public Integer getTravelFunction() { return travelFunction;} 

	public void setDescription(String s) { description=s;}
	public void setInput(String s) { input=s;}
	public void setOutput(String s) { output=s;}
    public void setPeriodStart(Long l) { periodStart=l;}
    public void setPeriodEnd(Long l) { periodEnd=l;}
    public void setTopLevelLocatorId(String s) { topLevelLocatorId=s;}
    public void setConversionFactor(Long l) { conversionFactor=l;}
    public void setStatusVector(String s) { statusVector=s;}
    public void setKValue(Integer i) { kValue=i;}
    public void setMaxPriority(Integer i) { maxPriority=i;}
    public void setTravelVector(String s) { travelVector=s;}
    public void setTravelImportance(Integer i) { travelImportance=i;}
    public void setTravelFunction(Integer i) { travelFunction=i;} 
    
    public Set<Integer> getParsedStatusAsSet() { // assumes statusVector non null
    	String[] temp;
		String delim = ";";
		temp = statusVector.split(delim);
		
		Set<Integer> stat = new HashSet<Integer>();
		for (String st: temp) {
			stat.add(Integer.parseInt(st.trim()));
		}		
		return stat;
    }
    
    public int[] getParsedTravelVector() {      // assumes travelVector non null
       	String[] temp;
		String delim = ";";
		
		if (travelVector.length() == 0) return new int[0];
		temp = travelVector.split(delim);

		int[] vectors = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			vectors[i] = Integer.parseInt(temp[i].trim());
		}
	
		return vectors;
    }
}
