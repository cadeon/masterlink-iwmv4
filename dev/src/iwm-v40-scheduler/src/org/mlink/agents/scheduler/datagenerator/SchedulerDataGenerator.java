package org.mlink.agents.scheduler.datagenerator;

public interface SchedulerDataGenerator {
	public void generate(String filename, int numJobs, int numAssets, int numNARs);
}
