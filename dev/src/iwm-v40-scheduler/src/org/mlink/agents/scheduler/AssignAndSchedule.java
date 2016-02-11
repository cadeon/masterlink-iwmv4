package org.mlink.agents.scheduler;

import java.io.IOException;

import org.mlink.agents.scheduler.model.SchedulingInputData;
import org.mlink.agents.scheduler.model.SchedulingOutputData;

/**
 * This interface is the gateway to the Scheduler Core
 * @author kenneth m. levine
 * @version 1.0
 *
 */
public interface AssignAndSchedule {
	/**
	 * This method schedules with the default maximum number of iterations
	 * @param sid The scheduling input data object
	 * @return The scheduling output data object
	 */	
	public SchedulingOutputData schedule(SchedulingInputData sid);

	/**
	 * This method schedules with maximum number of iterations defined by user input
	 * @param sid  The scheduling input data object
	 * @param iterations The maximum number of iterations
	 * @return  The scheduling output data object
	 */
	public SchedulingOutputData schedule(SchedulingInputData sid, int iterations);
	
	/**
	 * 	This method validates the scheduling input data - the return contains error messages only
	 * @param sid The scheduling input data object
	 * @return The scheduling output data object containing ProcessingErrors only
	 */
	public SchedulingOutputData validateSchedulingData(SchedulingInputData sid);

	/**
	 * This method schedules with file input; the maximum number of iterations is defined by user input
	 * @param filename  The scheduling input data file name
	 * @param iterations The maximum number of iterations
	 * exception IOException on input errors
	 * @return  The scheduling output data object
	 */
	// schedule directly from file inputs - used by test programs
	public SchedulingOutputData scheduleFromFile(String filename, int numIterations) throws IOException;
}
