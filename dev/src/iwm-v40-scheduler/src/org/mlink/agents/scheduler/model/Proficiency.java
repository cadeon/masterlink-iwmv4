package org.mlink.agents.scheduler.model;

public class Proficiency {
	Long id;
	Long classificationId;
	Integer level;
	Integer importance;
	Integer function;
	Integer maxValue;
	
	public Long getId() { return id;}
	public Long getClassificationId() { return classificationId;}
	public Integer getLevel() { return level;}
	public Integer getImportance() { return importance;}
	public Integer getFunction() { return function;}
	public Integer getMaxValue() { return maxValue;}
	
	public void setId(Long l) { id=l;}
	public void setClassificationId(Long l) { classificationId=l;}
	public void setLevel(Integer i) { level=i;}
	public void setImportance(Integer i) { importance=i;}
	public void setFunction(Integer i) { function=i;}
	public void setMaxValue(Integer i) { maxValue=i;}
}
