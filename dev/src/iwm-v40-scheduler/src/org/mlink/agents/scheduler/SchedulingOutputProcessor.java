package org.mlink.agents.scheduler;

import org.mlink.agents.scheduler.model.SchedulingOutputData;

public interface SchedulingOutputProcessor {
	SchedulingOutputData processOutput();
	void processFatalOutput();
}
