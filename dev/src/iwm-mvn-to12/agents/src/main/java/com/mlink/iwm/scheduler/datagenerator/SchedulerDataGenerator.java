package com.mlink.iwm.scheduler.datagenerator;

public interface SchedulerDataGenerator {
	public void generate(String filename, int numJobs, int numAssets, int numNARs);
}
