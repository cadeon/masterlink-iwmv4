package com.mlink.iwm.scheduler;

import com.mlink.iwm.scheduler.model.SchedulingOutputData;

public interface SchedulingOutputProcessor {
	SchedulingOutputData processOutput();
	void processFatalOutput();
}
