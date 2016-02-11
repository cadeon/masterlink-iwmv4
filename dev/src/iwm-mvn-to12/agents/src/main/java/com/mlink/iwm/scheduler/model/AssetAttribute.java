package com.mlink.iwm.scheduler.model;

public class AssetAttribute {
	Long id;
	String key;
	Integer value;
	Integer function;
	
	public Long getId() { return id;}
	public String getKey() { return key;}
	public Integer getValue() { return value;}
	public Integer getFunction() { return function;}
	
	public void setId(Long l) { id=l;}
	public void setKey(String s) { key=s;}
	public void setValue(Integer i) { value=i;}
	public void setFunction(Integer i) { function=i;}
}
