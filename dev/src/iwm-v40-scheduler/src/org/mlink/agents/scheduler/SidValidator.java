package org.mlink.agents.scheduler;

import java.util.*;

import org.mlink.agents.scheduler.model.*;

public interface SidValidator {
	boolean validate(SchedulingInputData sid,  List<ProcessingError> errs);
	boolean validateMetadata(MetaData md, List<ProcessingError> errs);
	boolean validateJob(Job j, List<ProcessingError> errs);
	boolean validateAsset(Asset a,  List<ProcessingError> errs);
	boolean validateNar(NonAssetResource j,  List<ProcessingError> errs);
}
