package org.mlink.agents.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.math.BigDecimal;
import java.math.MathContext;

import org.mlink.agents.scheduler.model.Asset;
import org.mlink.agents.scheduler.model.AssetAttribute;
import org.mlink.agents.scheduler.model.AssetAvailability;
import org.mlink.agents.scheduler.model.AssetResource;
import org.mlink.agents.scheduler.model.AssetTimeShare;
import org.mlink.agents.scheduler.model.Job;
import org.mlink.agents.scheduler.model.JobNAR;
import org.mlink.agents.scheduler.model.MetaData;
import org.mlink.agents.scheduler.model.NonAssetResource;
import org.mlink.agents.scheduler.model.ProcessingError;
import org.mlink.agents.scheduler.model.Proficiency;
import org.mlink.agents.scheduler.model.SchedulingInputData;
import org.mlink.agents.scheduler.model.SchedulingOutputData;

/* Introduction to the design:
 * 
 * The gateway functions make scheduling available - they implement the interface AssignAndSchedule.  Each takes in a SchedulingInputData object 
 * and returns a SchedulingOutputData object.  After initializing the various maps and other structures, the scheduling is done by the (tabu) search 
 * algorithms, beginning with the main search routine, doTabuScheduling().  These search algorithms are built upon a foundation that consists of the 
 * following components: validation (based on interface SidValidatior), tabu search (based on interface TabuList), the utility stack including the 
 * calculation of asset utility (based on interface UtilityCalculator), hard constraints, and routines for Adding/Swapping/Removing assets.
 * 
 * The utility stack is built on top of the implementation of interface UtilityCalculator.  On top of that are the utility manipulation routines, 
 * which test the effect on utility of adding, swapping, or removing assets.   These are used to determine which asset to assign, swap, or deassign; 
 * once this decision has been made, the next set of routines are used to actually add, swap, or remove asset assignments.
 * 
 * High Level (linear) Organization of the code in ScheduleController:
 * -- Basic fields and Constructor
 * -- sod = schedule(sid) - the gateway to scheduling
 * -- Initialization routines
 * -- Small helper routines
 * -- This is followed by the following as implemented in inner classes that implement interfaces:
 * -- Search Algorithms and helpers
 * -- Adding/Swapping/Removing assets
 * -- Utility manipulation routines (add/swap/remove utility and delta utility)
 * -- SOD generation 
 * -- Utility calculation
 * -- Hard Constraints
 * 
 * Two other classes in this package are InputProcessor (which is used by the test bed to convert a file input into a SchedulingInputData object) and
 * TabuSearch, which implements the interface TabuList and provides reusable functionality for short-term tabu search memory.
 * 
 * Details:
 * 1. Basic fields and constructor.  The class defines several maps that are used throughout the processing.  
 * These map asset resource (id) to assigned asset and to an array of Strings that represent its locator.  Other maps
 * from asset (id) to time available (for a resource asset or a managed asset), and to travel time,  A reference to the inner
 * class that is used for validation is defined, as well as a reference to the inner class that is used for utility calculations.
 * 
 * 2.  sod = schedule(sid).  This is the public gatway to scheduling.  It is defined in terms of a method that takes a SchedulingInputData
 * object and produces a SchedulingOutputData object.  Another signature for this method also takes a parameter that represents the maximum
 * number of iterations that the scheduler should run.  A third method can be used early in the development process to validate the incoming 
 * data format and contents.  Two switches are available to provide output and/or debugging output, for testing.  A number of structures uused
 * for the Tabu processing are then defined, including a map from an asset or asset resource id to an offset in the tabu tables.  Finally, the
 * constructor is defined, which initializes the inputProcessor object, and the error list (which is eventually returned to the user in sod.
 *  
 * 3.  Initialization routines.  The various structures are initialized.  The availabilities and maintainabilities are bounded by the overall
 * scheduling period,   Finally, any non-sticky jobs are removed from the schedule.
 * 
 * 4. Search Algorithm inner class.  This includes the initial search (quick population of assets assigned to asset resources) and the main Tabu Search algorithm 
 * (which is by far the longest method in the package, as modularizing it makes it less transparent to the reader).  Helper routines remove assignments from
 * jobs that are not completely asseted, and set the final asset assignments to the "best complete" one found during the search.
 * 
 * 5.  Hard constraint check inner class.  The search routines check whether an asset is compatible with an asset resource, given the overall state of the schedule,
 * by calling AssetFitsAR().  This includes a set of helper methods that make the various hard constraint checks required.  Note that the hard constraint
 * check of Classification ID is not included in this section - it is applied directly in the search routines.
 * 
 * 6. Adding/Swapping/Removing assets.  These routine add, swap, or remove assets assigned to asset resources, 
 * 
 * 7. Utility manipulation routines.  These routines test the consequences of and actually make the "moves" defined for this search: add, swap, and remove.
 * The Get add/swap/remove (complete or delta) utility routines perform "gedanken experiments" by temporarily making or retracting assignments, and then computing
 * utility under those conditions; the delta versions compare that value with the original utility value before the add/swap/remove was attempted.
 * 
 * 8. SchedulingOutputData generation inner class.  These methods build the AssetTimeShares, JobNAR, NAR, and Errors objects and uses them to populate the sod that is returned
 * to the user.  The main method is processOutput().  Optionally, a file is created with the output data (as defined by the spreadsheet).
 * 
 * 9. Helper routines.  
 * 
 * 11.  Utility inner class.  This implements the UtilityCalculator interface.  Main methods here include: BigDecimal getAssetUtility(),
 * BigDecimal getAddDeltaUtility(AssetResource, Asset), BigDecimal getRemoveDeltaUtility(AssetResource, Asset), and 
 * BigDecimal getSwapDeltaUtility(AssetResource, Asset, AssetResource, Asset).
 */

public class SchedulerController implements AssignAndSchedule {
	InputProcessor ip;  							// parses input file into sid
	String inFileName;  							// for file inputs
	SchedulingInputData sid;  						// from Scheduler core or xlated from file inputs
	SchedulingOutputData sod; 						// to Scheduler core or xlated into file outputs
	HashMap<Long,Asset> assetResourceToAssetMap;  	// maps assetResourceId to asset
	HashMap<Long,Set<AssetResource>> assetToAssetResourceMap;  // maps asset id to set of asset resources asset is assigned to
	HashMap<Long,Long> managedAssetTimeAvailable; 	// maps managed asset iD to time available
	HashMap<Long,Long> assetTimeAvailable; 			// maps asset iD to time available
	HashMap<Long,String[]> assetResourceLocators;   // maps asset resource id to components of locator (for time travel computations)
	
	SidValidator validator = new HardCodedSidValidator(); // implements SidValidator interface
	UtilityCalculator utilityCalculator;		   		  // inner class: implements UtilityCalculator interface 
	HardConstraints  hc = new SchedulingHardConstraints();// inner class; implements HardConstraints interface
	SchedulingOutputProcessor outputProcessor = new SchedulingSODGeneratorOutputProcessor(); // inner class; implements SchedulingOutputProcessor interface
	ScheduleMaker scheduleMaker = new TabuScheduling();  // inner class; implements tabu search routine 
	ScheduleMaker.ScheduleHelper scheduleHelper = ((TabuScheduling)scheduleMaker).new ScheduleCRUDHelper(); // inner inner class; implement ScheduleMaker.ScheduleHelper interface
	
	List<ProcessingError> errs;						// component of sod; built interactively during validation
	
	// for logging: 0 for no logging, 1 for just final summary, >1 for each iteration
	static int GENERATE_OUTPUT = 1; 

	// for Tabu Search
	static final int TABU_SIZE = 10; 			    // cycle size for tabu list
	static final int MAX_NO_CHANGE = 10;  			// number of iterations without change before "end of iteration" is forced
	
	// Keeping track of our efforts
    long totalTrys;    		    				     // counts all attempted moves
    long totalLegalTrys;							 // counts moves that pass hard constraints
	
	TabuList tabu;
	BigDecimal bestUtilitySeen;
	BigDecimal bestCompleteUtilitySeen;
	BigDecimal previousBest;
	BigDecimal previousBestForPly;
	HashMap<Long,Integer> jobARIDMap;			// maps from asset resource id to offset into tabu tables
	HashMap<Long,Integer> assetIDMap;			// maps from asset id to offset into tabu tables
	HashMap<Long,Asset> bestCompleteAssetMap;   // maps asetResourceId to asset for best complete assignment seen so far	
	HashMap<Long,Job> assetResourceJobMap;	    // maps assetResource ID to id for job it is part of 
	HashMap<Long,NonAssetResource> nsnToNARMap; // maps national stock number to Non Asset resource it is for
	
	private static final Logger LOGGER = Logger.getLogger("SchedulerController");
	
	public SchedulerController() {
		ip = new InputProcessor();				
		errs = new ArrayList<ProcessingError>();
	}
	
	// Beginning of gateway routines
	
	// schedule (use system default number of iterations)
	@Override
	public SchedulingOutputData schedule(SchedulingInputData sid){
		return schedule(sid, 100);
	}
	
	// schedule (use caller defined number of iterations)
	@Override
	public SchedulingOutputData schedule(SchedulingInputData sid, int numIterations) {
		float runTime = 0;
		this.sid = sid;
		if (validator.validate(sid, errs)) {
			processInput();
			initializeSchedule();
			runTime = scheduleMaker.makeSchedule(numIterations);
			outputProcessor.processOutput();		
		} else {
			outputProcessor.processFatalOutput();
		}
		sod.setRunTime(runTime);
		return sod;
	}
	
	// validate all data in sid - sod just returns ProcessingErrors
	@Override
	public SchedulingOutputData validateSchedulingData(SchedulingInputData sid) {
		this.sid = sid;
		validator.validate(sid, errs);
		sod = new SchedulingOutputData();
		sod.setProcessingErrors(errs);
		return sod;
	}
	
	// only used for test case - runs from file to sid to sod
	@Override
	public SchedulingOutputData scheduleFromFile(String filename, int numIterations) throws IOException {
		float runTime = 0;
		inFileName = filename;
		sid = ip.parse(filename);	
		if (validator.validate(sid, errs)) {
			processInput();
			initializeSchedule();
			runTime = scheduleMaker.makeSchedule(numIterations);
			outputProcessor.processOutput();
		} else {
			outputProcessor.processFatalOutput();
		}
		sod.setRunTime(runTime);
		return sod;
	}
	
				// End of gateway functions.  Beginning of initialization routines
	
	//setup maps used by search, bound maintainabilities by metadata start and end, set up maps from asset id to time of availabilities
	void processInput() {
		assetResourceToAssetMap = new HashMap<Long,Asset>();  // maps asetResourceId to asset
		assetToAssetResourceMap = new HashMap<Long,Set<AssetResource>>(); // maps asetId to set of assetresources asset is assigned to
		jobARIDMap = new HashMap<Long,Integer>();			  // for tabu search asset resource id -> offset
		assetIDMap = new HashMap<Long,Integer>();			  // for tabu search asset id -> offset
		bestCompleteAssetMap = new HashMap<Long,Asset>();     // maps asset resource id to asset 
		managedAssetTimeAvailable = new HashMap<Long,Long>(); // maps managed asset iD to time available
		assetTimeAvailable = new HashMap<Long,Long>();        // maps managed asset iD to time available
		assetResourceLocators = new HashMap<Long,String[]>(); // maps asset resource id to locator strings
		assetResourceJobMap = new HashMap<Long,Job>();  	  // maps asset resource id to encompassing job
		nsnToNARMap = new HashMap<Long,NonAssetResource>();  // maps national stock number to Non Asset Resource is is for
		
		boundMaintainabilities();							  // by period start and period end meta-data
		setupManagedAssetTimeMap(); 						  // set up Map from MangedAssetId to Available Time
		setupAssetTimeMap(); 								  // set up Map from Asset ID to Available Time
		setupNsnToNARMap();									  // set up map from national stock number to Non Asset Resource it is part of
	}
	
	// bound maintainabilities and availabilities by metadata period start and period end
	void boundMaintainabilities() {
		MetaData md = sid.getMetaData();
		Long st = md.getPeriodStart();  // get start and end of scheduling period
		Long ed = md.getPeriodEnd();
		
		for (Job j: sid.getJobs()) {
			for (AssetAvailability aa: j.getMaintainabilities()) {
				if (aa.getStartDateTime() < st) aa.setStartDateTime(st);
				if (aa.getEndDateTime() > ed) aa.setEndDateTime(ed);
			}
		}
		for (Asset a: sid.getAssets()) {
			for (AssetAvailability aa: a.getAvailabilities()) {
				if (aa.getStartDateTime() < st) aa.setStartDateTime(st);
				if (aa.getEndDateTime() > ed) aa.setEndDateTime(ed);
			}
		}
	}

	//set map entry for a job's managed asset to sum of time on job maintainabilities
	void setupManagedAssetTimeMap() {
		for (Job j: sid.getJobs()) {
			Long id = j.getManagedAssetId();
			Long timeOnAsset = 0L;
			for (AssetAvailability aa: j.getMaintainabilities()) {
				timeOnAsset += (aa.getEndDateTime() - aa.getStartDateTime());
			}
			managedAssetTimeAvailable.put(id, timeOnAsset);
		}
	}
	
	// set map entry for asset's availability to sum of time on asset availabilities
	void setupAssetTimeMap() {
		for (Asset a: sid.getAssets()) {
			Long timeOnAsset = 0L;
			for (AssetAvailability aa: a.getAvailabilities()) {
				timeOnAsset += (aa.getEndDateTime() - aa.getStartDateTime());
			}
			assetTimeAvailable.put(a.getId(), timeOnAsset);				
		}
	}
	
	// setup map from national stock number to Non Asset Resource is is for
	void setupNsnToNARMap() {
		for (NonAssetResource nar: sid.getNARs()) {
			nsnToNARMap.put(nar.getStockNumber(), nar);
		}
	}


	// set up variables and maps for tabu search
	 void initializeTabuList() {	 
	        tabu = new TabuSearch(assetResourceLocators.size()+1,sid.getAssets().size()+1,TABU_SIZE); 
	        bestUtilitySeen = new BigDecimal(-1);
	        bestCompleteUtilitySeen = new BigDecimal(-1);
	        
	        int jnum = 1;
	        int anum = 1;
	        
	        for (Job j: sid.getJobs()) {
	        	for (AssetResource ar: j.getAssetResources()) {  // map from asset resource id to offset into tabu lists
	        		jobARIDMap.put(ar.getId(), jnum++);
	        	}	
	        }
	        for (Asset a: sid.getAssets()) {					 // map from asset id to offset into tabu lists
	        	assetIDMap.put(a.getId(), anum++);
	        }
	    } 
		
	 // initialize various maps, create the utility calculator, remove all non-sticky jobs so the search routine will schedule them optimally
		void initializeSchedule() {
			initializeAssetMaps();					// initialize assetMap and bestCompleteAssetMap.  Need to do this before initializing utilityCalculator
			initializeAssetResourceLocators();		// initialize map from asset resource id to locators for the encompasing job
			initializeAssetResourceJobMap();     	// initialize map from AssetResource to estimated time for its job				
			utilityCalculator = new RecalculateIndividualAssetsUtilityCalculator();
			dumpNonStickyJobs();					// any non-sticky job should be assigned by search routine
		}

		// map from AssetResource ID to Asset assigned to it; initialize assetToAssetResourceMap to inverse;  initialize best complete map to nulls 
		void initializeAssetMaps() {
			for (Asset a: sid.getAssets()) {
				assetToAssetResourceMap.put(a.getId(), new HashSet<AssetResource>());
			}
			for (Job j: sid.getJobs()) {
				for (AssetResource ar: j.getAssetResources()) {
					bestCompleteAssetMap.put(ar.getId(), null);
					Long assetId = ar.getAssignedAssetId();
					assetResourceToAssetMap.put(ar.getId(), getAssetFromID(assetId));
					if (assetId != null && !assetId.equals(0L)) assetToAssetResourceMap.get(assetId).add(ar);
				}
			}		
		}
		
		// set up map from asset resource id to locator strings for encompassing job
		void initializeAssetResourceLocators() {
			for(Job j: sid.getJobs()) {
				String[] locators = parseLocator(j.getLocator());    // get the array {bldg, bldg.floor  etc }
				for(AssetResource ar: j.getAssetResources()) {
					assetResourceLocators.put(ar.getId(), locators); // set each asset resource to have this array for its locators
				}
			}
		}
		
		// setup map from asset resource to job it is part of
		void initializeAssetResourceJobMap() {
			for (Job j: sid.getJobs()) {
				for (AssetResource ar: j.getAssetResources()) {
					assetResourceJobMap.put(ar.getId(), j);
				}
			}
		}

		// set up array of strings to represent each level of the locator (including all levels above it, e.g., a, a.b, a.b.c
		String[] parseLocator(String loc) {
			int numSegments = 1;
			int startAt = -1;
			char delimChar = '.';

			while( (startAt = loc.indexOf(delimChar, startAt+1)) != -1) numSegments++; // figure out how many elements in the matrix 

			String [] ans = new String[numSegments];
			startAt = -1;
			
			for (int i = 0; i <numSegments; i++) {         // fill in each entry with 
				startAt = loc.indexOf(delimChar, startAt+1);    // the substring from the beginning of the string until the n'th delim we are looking for
				ans[i] = loc.substring(0, (startAt == -1) ? loc.length() : startAt);
			}
			return ans;		
		}

		// used during initialization - all non-sticky jobs will be scheduled by search routines
		// post-condition:  job.startDateTime != 0 if and only if job has at least one asset assigned to it
		void dumpNonStickyJobs() {
			for (Job j: sid.getJobs()) {
				for (AssetResource ar: j.getAssetResources()) {
					if (hc.assetRemovableFromAR(j, ar)) {
						scheduleHelper.removeAssetFromAssetResource(j, ar);
					}
				}
	    	    if (jobHasNoAssetsAssigned(j)) {
	    	    	j.setStartDateTime(0L);
	    	    }
	    	    else {
	    	    	j.setStartDateTime(1L);
	    	    }
			}
		}

				// End of Initialization.   Beginning of Small Helper Routines
		
	boolean assetResourceAssigned(AssetResource ar) {
		return assetResourceToAssetMap.get(ar.getId()) != null;
	}
	
	boolean assetResourceUnassigned(AssetResource ar) {
		return assetResourceToAssetMap.get(ar.getId()) == null;
	}
	
	void deassignAssetResource(AssetResource ar) {
		Asset a = assetResourceToAssetMap.get(ar.getId());
		assetResourceToAssetMap.put(ar.getId(), null);
		if (a != null && !a.getId().equals(0L)) assetToAssetResourceMap.get(a.getId()).remove(ar);
	}
	
	void assignAssetResource(AssetResource ar, Asset a) {
		assetResourceToAssetMap.put(ar.getId(), a); 
		if (a != null && !a.getId().equals(0L)) assetToAssetResourceMap.get(a.getId()).add(ar);
	}
	
	boolean assetResourceValueAssigned(Asset a) {
		return a != null;
	}
	
	boolean assetResourceValueUnassigned(Asset a) {
		return a == null;
	}
	
	// compute number of asset resources that have been assigned
	int numAssignedAssetResources(Job j) {
		int ans = 0;
		for(AssetResource ar: j.getAssetResources()) {
			if (assetResourceAssigned(ar)) ans++;
		}
		return ans;
	}
	
	// determine if a job has not been asseted at all
	boolean jobHasNoAssetsAssigned(Job j) {
		boolean ans = true;
		for (AssetResource ar: j.getAssetResources()) {
			if (assetResourceAssigned(ar)) ans = false;
		}
		return ans;
	}
	
	// determine which asset resources have assigned this asset - unoptimized, could be a reverse map for assetMap
	Set<AssetResource> getAssetResourcesForAsset(Asset a) { 
		return assetToAssetResourceMap.get(a.getId());	
	}
	
	// map from national stock number to NAR - optimized
	NonAssetResource getNARFromNsn(Long nsn) {	
		if (nsn == null) return null;
		if (!nsnToNARMap.containsKey(nsn)) return null;
		return nsnToNARMap.get(nsn);
	}
	
	// map from asset id to asset - unoptimized, this is only used during initialization so was not considered worth optimizing with a map
	Asset getAssetFromID(Long assetId) {	
		if (assetId == null) return null;
		for (Asset a: sid.getAssets()) {
			if (a.getId() != null && a.getId().equals(assetId)) {
				return a;
			}
		}
		return null;
	}
	
	// compute how many jobs have at least one asset assigned
	int getNumberOfJobsAsseted() { 
		int numStarted = 0;
		for (Job j: sid.getJobs()) if (j.getStartDateTime() != 0) numStarted++;
		return numStarted;
	}
	
	// compute how many jobs have all assets assigned
	int getNumberOfJobsStarted() { 
		int numStarted = 0;
		for (Job j: sid.getJobs()) if ((j.getStartDateTime() != 0) && (numAssignedAssetResources(j) == j.getAssetResources().size()))  {
			numStarted++;
		}
		return numStarted;
	}

	
	float getPercentJobsStarted() {
		return 100F*getNumberOfJobsStarted()/sid.getJobs().size();
	}
		
	void printUtility() { // for debugging
		LOGGER.log(Level.INFO," complete utility = " + utilityCalculator.getCompleteUtility());
		for (Asset a: sid.getAssets()) {
			LOGGER.log(Level.INFO, "Asset " + a.getId() + " utility " + utilityCalculator.getAssetUtility(a));
		}
	}

	void printAssetMap() {	// for debugging
		LOGGER.log(Level.INFO,"xxxxxxxx ASSET MAPxxxxxxxxx");
		Set<Map.Entry<Long, Asset>> set2 = assetResourceToAssetMap.entrySet();
		for (Map.Entry<Long, Asset> me2: set2) {
			LOGGER.log(Level.INFO,"xxxxxxxx" +
					"xxxxx asset map asset res " + me2.getKey() + " asset assigned " + (me2.getValue() == null ? null : me2.getValue().getId()));
		}		
	}			
	
		// End of Small helper routines. Beginning of inner class for Utility calculation			
	
		public class RecalculateIndividualAssetsUtilityCalculator implements
		UtilityCalculator {
	
			HashMap<Asset, BigDecimal> assetUtilityMap;
			
			RecalculateIndividualAssetsUtilityCalculator() {
				initializeAssetUtilityMap();					// initialize map of AssetID to Utility
			}
						
			// setup map from Asset to utility
			void initializeAssetUtilityMap() {
				assetUtilityMap = new HashMap<Asset, BigDecimal>(sid.getAssets().size() * 2);
				for(Asset a: sid.getAssets()) {
					assetUtilityMap.put(a, getAssetUtility(a));
				}				
			}

			// compute the utility of one asset
			@Override
			public BigDecimal getAssetUtility(Asset a) {
				if (a == null) return BigDecimal.ZERO;
				Set<AssetResource> set = getAssetResourcesForAsset(a);				
		
				BigDecimal utilVal;                                     // start off with perfect utility

				utilVal = getBasicUtility(a,set);                  // first compute basic utility
				utilVal = utilVal.multiply(getProficiencyPenalties(a, set));        // then penalties for proficiencies
				utilVal = utilVal.multiply(getTravelPenalty(a, set));               // then penalties for travel 
				return utilVal;
			}
			
			// compute the pre-penalty "basic" utility for an asset
			BigDecimal getBasicUtility(Asset a, Set<AssetResource> set) {
				
				// compute total time for this asset on these resources
				int assetTotalJobTime = 0;                     // time on job asset resources
				int assetTotalJobPriorityTime = 0;             // time on job asset resources x priority
				if (set != null) {
					for (AssetResource ar: set) {
						int addedTime = assetResourceJobMap.get(ar.getId()).getEstimatedTime().intValue() * sid.getMetaData().getConversionFactor().intValue();
						assetTotalJobTime += addedTime; 
						assetTotalJobPriorityTime += addedTime * assetResourceJobMap.get(ar.getId()).getPriority();
					}
				}
				BigDecimal unweightedUtility = new BigDecimal(assetTotalJobTime);
				unweightedUtility = unweightedUtility.divide(new BigDecimal(assetTimeAvailable.get(a.getId())), MathContext.DECIMAL32);
				BigDecimal denom = new BigDecimal(assetTimeAvailable.get(a.getId())).multiply(new BigDecimal(sid.getMetaData().getMaxPriority()));
				BigDecimal weightedUtility =  new BigDecimal(assetTotalJobPriorityTime).divide(denom, MathContext.DECIMAL32);
				BigDecimal kValue = new BigDecimal(sid.getMetaData().getKValue()).divide(new BigDecimal(100), MathContext.DECIMAL32);			
				BigDecimal weighContribution = kValue.multiply(weightedUtility);
				BigDecimal unweighContribution = BigDecimal.ONE.subtract(kValue).multiply(unweightedUtility);
				return weighContribution.add(unweighContribution);
				
			}
			
			// compute one proficiency penalty for an asset
			BigDecimal getProficiencyPenalty(Asset a, Set<AssetResource> set, Proficiency pr) {
				
				// Find all the AssetResources that are of the same class as this proficiency and for each compute estimatedTime * (1-(delta grade/max grade)) 
				BigDecimal ans = BigDecimal.ZERO; 
				BigDecimal importance = BigDecimal.ZERO; 
				BigDecimal totalImportance = BigDecimal.ZERO; 
				int totalTime = 0; 
				long timeOnTask;
				
				for (AssetResource ar: set) {
					if (ar.getProficiencies() != null)
						for(Proficiency arProf: ar.getProficiencies()){                              // find all AssetResource Proficiencies
							if (arProf.getClassificationId().equals(pr.getClassificationId())) {     // with the same Class ID
								importance = new BigDecimal(arProf.getImportance());
								importance = importance.divide(new BigDecimal(100), MathContext.DECIMAL32);
								timeOnTask = assetResourceJobMap.get(ar.getId()).getEstimatedTime() * sid.getMetaData().getConversionFactor(); // get time on task from Job
								totalTime += timeOnTask;        // compute total time on task for this prof. for asset
								BigDecimal impTot = importance.multiply(new BigDecimal(timeOnTask));
								totalImportance = totalImportance.add(impTot);
								int deltaValue;
								switch (arProf.getFunction()) {
								case 1:    // >=
									deltaValue = pr.getLevel() - arProf.getLevel();
									break;
								case 2:   // <=
									deltaValue = arProf.getLevel() - pr.getLevel();
									break;
								case 3:  // =
									deltaValue = Math.abs(pr.getLevel() - arProf.getLevel());
									break;
								case 4:  // existence of proficiency is enough
								default:
									deltaValue = 0;
									
								}
								BigDecimal val = new BigDecimal(deltaValue);
								val = val.divide(new BigDecimal(arProf.getMaxValue()), MathContext.DECIMAL32);
								val = BigDecimal.ONE.subtract(val);
								val = val.multiply(new BigDecimal(timeOnTask));
								ans = ans.add(val);
							}	
						}
				}
				ans = ans.divide(new BigDecimal(assetTimeAvailable.get(a.getId())), MathContext.DECIMAL32);
				if (totalTime==0) return BigDecimal.ONE;
				ans = BigDecimal.ONE.subtract(ans);
				ans = ans.multiply(totalImportance);
				ans = ans.divide(new BigDecimal(totalTime), MathContext.DECIMAL32);
				return BigDecimal.ONE.subtract(ans);
			}
			
			
			// compute complete proficiencies penalty for an asset
			BigDecimal getProficiencyPenalties(Asset a, Set<AssetResource> set) {

				if (a.getProficiencies() == null || a.getProficiencies().size() == 0) return BigDecimal.ONE;
				// multiply penalties for each proficiency on the asset
				BigDecimal ans = BigDecimal.ONE;
				for (Proficiency pr: a.getProficiencies()) {
					ans = ans.multiply(getProficiencyPenalty(a, set, pr));
				}
				return getRoot(ans, a.getProficiencies().size());
			}
			
			BigDecimal getRoot(BigDecimal inVal, int size) {
				return new BigDecimal(Math.pow(inVal.doubleValue(), 1D/size));
			}
			
			// compute travel penalty for an asset
			BigDecimal getTravelPenalty(Asset a, Set<AssetResource> set) {				
				// if not tracking travel or not assigned to any jobs, no penalty
				if (a.getTrackTravel()==0 || hc.assetUsedTime(a)==0) return BigDecimal.ONE;
				Long travelTime = hc.assetTravelTime(a);
				BigDecimal importance = new BigDecimal(sid.getMetaData().getTravelImportance());
				BigDecimal ans = importance.divide(new BigDecimal(100), MathContext.DECIMAL32);
				ans = ans.multiply(new BigDecimal(travelTime));
				ans = ans.multiply(new BigDecimal(sid.getMetaData().getConversionFactor()));
				ans = ans.divide(new BigDecimal(hc.assetUsedTime(a)), MathContext.DECIMAL32);
				return BigDecimal.ONE.subtract(ans);
			}
			
			// compute utility for all assets
			@Override
			public BigDecimal getCompleteUtility() {
				int numAssetsToCompute = 0;
				BigDecimal totalUtility = BigDecimal.ZERO;
				for (Asset a:  assetUtilityMap.keySet()) {
					if (a.getCalculateUtility() != 0) {
						numAssetsToCompute++;                 // count this one
						totalUtility = totalUtility.add(getAssetUtility(a));
					}
				}
				return numAssetsToCompute==0 ? BigDecimal.ONE : totalUtility.divide(new BigDecimal(numAssetsToCompute), MathContext.DECIMAL32);
			}
			
			// compute the utility if an asset is assigned
			@Override
			public BigDecimal getAddUtility(AssetResource addAr, Asset addAsset) {
				if (addAsset.getCalculateUtility() == 0) return getAssetUtility(addAsset);
				assignAssetResource(addAr, addAsset);
				BigDecimal addedUtility = getAssetUtility(addAsset);
				deassignAssetResource(addAr);
				return addedUtility;
			}

			// compute the utility if an asset is de-assigned
			@Override
			public BigDecimal getRemoveUtility(AssetResource removeAr, Asset removeAsset) {
				if (removeAsset == null) return BigDecimal.ZERO;
				if (removeAsset.getCalculateUtility().equals(0)) return getAssetUtility(removeAsset);
				deassignAssetResource(removeAr);				
				BigDecimal removedUtility = getAssetUtility(removeAsset);
				assignAssetResource(removeAr, removeAsset);				
				return removedUtility;
			}

			// compute the utility if two asset assignments are exchanged
			@Override
			public BigDecimal getSwapUtility(AssetResource ar1, Asset a1, AssetResource ar2, Asset a2) {
				deassignAssetResource(ar1);				
				deassignAssetResource(ar2);				
				assignAssetResource(ar1, a2);
				assignAssetResource(ar2, a1);
				BigDecimal finalUtility = getAssetUtility(a1).add(getAssetUtility(a2));
				deassignAssetResource(ar1);				
				deassignAssetResource(ar2);				
				assignAssetResource(ar1, a1);
				assignAssetResource(ar2, a2);
				return finalUtility; 
			}
			
			// compute the incremental utility if an asset is assigned
			@Override
			public BigDecimal getAddDeltaUtility(AssetResource addAr, Asset addAsset) {
				if (addAsset.getCalculateUtility().equals(BigDecimal.ZERO)) return BigDecimal.ZERO;
				BigDecimal initialUtility = getAssetUtility(addAsset);
				assignAssetResource(addAr, addAsset);
				BigDecimal addedUtility = getAssetUtility(addAsset);
				deassignAssetResource(addAr);
				return addedUtility.subtract(initialUtility);
			}
			
			// compute the incremental utility if an asset is de-assigned
			@Override
			public BigDecimal getRemoveDeltaUtility(AssetResource removeAr, Asset removeAsset) {
				if (removeAsset.getCalculateUtility().equals(BigDecimal.ZERO)) return BigDecimal.ZERO;
				BigDecimal initialUtility = getAssetUtility(removeAsset);
				deassignAssetResource(removeAr);				
				BigDecimal removedUtility = getAssetUtility(removeAsset);
				assignAssetResource(removeAr, removeAsset);				
				return removedUtility.subtract(initialUtility);
			}
			
			// compute the incremental utility if two asset assignments are exchanged
			@Override
			public BigDecimal getSwapDeltaUtility(AssetResource ar1, Asset a1, AssetResource ar2, Asset a2) {
				BigDecimal initialUtility = getAssetUtility(a1).add(getAssetUtility(a2));
				deassignAssetResource(ar1);				
				deassignAssetResource(ar2);				
				assignAssetResource(ar1, a2);
				assignAssetResource(ar2, a1);
				BigDecimal finalUtility = getAssetUtility(a1).add(getAssetUtility(a2));
				deassignAssetResource(ar1);				
				deassignAssetResource(ar2);				
				assignAssetResource(ar1, a1);
				assignAssetResource(ar2, a2);
				return finalUtility.subtract(initialUtility); 
			}			

			// get the utility of all complete jobs - if new maximum save the assignment 
			@Override
			public BigDecimal getCompleteJobsUtility(boolean saveIt) {
				HashMap<Long,Asset> oldAssignedAssetMap = new HashMap<Long,Asset>();   // maps asetResourceId to asset for assignment 
				BigDecimal ans = BigDecimal.ZERO;
				
				// copy asset value presently assigned for each asset resource
				for (Job j: sid.getJobs()) {
		        	for (AssetResource ar: j.getAssetResources()) { 
		        		oldAssignedAssetMap.put(ar.getId(),assetResourceToAssetMap.get(ar.getId()));
		        	}
			    }
				
				// get just the complete jobs
				for(Job j: sid.getJobs()) {
					if (j.getStartDateTime() != 0) {
						if (numAssignedAssetResources(j) != j.getAssetResources().size()) {
							for(AssetResource ar: j.getAssetResources()) {
								if (assetResourceAssigned(ar)) {
									scheduleHelper.removeAssetFromAssetResource(j, ar);
								}
							}
						}
					}
				}
				ans = utilityCalculator.getCompleteUtility();
				
				// See if this is a new best complete utility - if it is save the mapping of asset resources to assets for potential output
				if ((ans.compareTo(bestCompleteUtilitySeen) > 0) && saveIt) {	
			       for (Job j: sid.getJobs()) {
			        	for (AssetResource ar: j.getAssetResources()) { 
			        		bestCompleteAssetMap.put(ar.getId(),assetResourceToAssetMap.get(ar.getId()));  // current assignments 
			        	}
				   }
				}
				
				// put back the mapping of incomplete jobs
				for(Job j: sid.getJobs()) {
					for(AssetResource ar: j.getAssetResources()) {
						Asset oldAsset = oldAssignedAssetMap.get(ar.getId());
						Asset newAsset = assetResourceToAssetMap.get(ar.getId());
						if (oldAsset != null && newAsset != oldAsset) {
							scheduleHelper.assignAssetToAssetResource(j,ar, oldAsset);        			
						}
					}
				}
				return ans;
			}
		}
		
		// end of inner class RecalculateIndividualAssetsUtilityCalculator  
		
		// beginning of inner class SchedulingHardConstraints
		
		class SchedulingHardConstraints implements HardConstraints {
			
			SchedulingHardConstraints() {}
			
			// test to see if asset assignment is not sticky
			@Override
			public boolean assetRemovableFromAR(Job j, AssetResource ar) {
			/*  
			     Conditions that will allow an asset to be removed from an asset resource:
			        jobAssignedID is 0 
			        jobRequestedId is 0
			        job status is in one of the states permissed by the meta-data
			*/
				return ar.getAssignedAssetId() == null || ar.getAssignedAssetId() == 0 
				    || ar.getRequiredAssetId() == null || ar.getRequiredAssetId() == 0 
			       	|| sid.getMetaData().getParsedStatusAsSet().contains(j.getStatus());
			}
			
			// see if asset resource can be assigned - used by initial scheduling
			@Override
			public boolean tryToAssignAssetResource(Job j, AssetResource ar) {
				// AssetResource ar is currently unassigned
				// consider all assets of the class required by ar, 
				List<Asset> as = getQualifiedAssetsInClass(j, ar);
				
				if (as.isEmpty()) { 
					return false;
				}	
				//  of those that may be assigned choose the one with the highest utility
				Asset bestAsset = null;
				BigDecimal bestAssetUtility = new BigDecimal(-1000);
				
				for (Asset asset: as) {
					BigDecimal thisAssetUtility = utilityCalculator.getAddDeltaUtility(ar, asset);
					if (thisAssetUtility.compareTo(bestAssetUtility) > 0) {
						bestAsset = asset;
						bestAssetUtility = thisAssetUtility;
					}
				}
				scheduleHelper.assignAssetToAssetResource(j, ar, bestAsset);
				return true;
			}	
			
			// get list of assets qualified to be assigned to a given asset resource - used by initial scheduling
			List<Asset> getQualifiedAssetsInClass(Job j, AssetResource ar) {
				Long id = ar.getClassificationId();
				List<Asset> ans = new ArrayList<Asset>();
				for (Asset a: sid.getAssets()) {
					if (a.getClassificationId().equals(id) && assetFitsAR(j,ar,a)) ans.add(a);
				}
				return ans;
			}
			
			// beginning of hard constraint tests - initial 3 tests
			@Override
			public boolean assetFitsAR(Job j, AssetResource ar, Asset a) {
		/*
			 1. If aID  unassigned, return true (no new constraint violations possible)
			 2. Time on jobs on a managed asset fit within the time allocated for that managed asset
			 3. Quantity of each NAR NSN is non-negative
		     4. The asset must not be assigned to any other job asset for this job
		     5. The organization on the job must be 0 (all) or the organization of the asset
		     6. If the job asset resource has a requested id, the assigned id (if the job is scheduled)
		         is for the requested id
		     7. Proficiency test: level of asset function proficiency level of job asset resource
		        (function is >= for e.g., skill).  We assume levels are positive.  
		     8. Attribute test: for each attribute in the job asset resource, the corresponding asset attribute
		        key must be listed and the level of asset func attribute level of job asset resource (function may be
		         "notRequired" e.g., for just needing the attribute key, >= for e.g., horsepower)
		     9. The total amount of time on the asset availabilities must allow this job (including travel time)
	
		      Other checks are done by the search routine (e.g., class id match)
		*/
			    // Step 1   
				if (assetResourceValueUnassigned(a)) return true;
				
				long oldStartTime = j.getStartDateTime();
				j.setStartDateTime(1L);  // set this job "started" for managed asset time computation
	
				// Step 2
				long maTime = 0;  // used to accumulate time on jobs with managed asset equal to this asset ID 
				long conv = sid.getMetaData().getConversionFactor();
				long maId = j.getManagedAssetId();
				for (Job jb: sid.getJobs()) {
					if (jb.getStartDateTime() != 0 && jb.getManagedAssetId().equals(maId)) {
						maTime += jb.getEstimatedTime()*conv;
					}
				}
				if (maTime > managedAssetTimeAvailable.get(maId)) {
					j.setStartDateTime(oldStartTime);
					return false;
				}
				j.setStartDateTime(oldStartTime);		
				 		
				//Step 3
				if (j.getNonAssetResources() != null && j.getNonAssetResources().size() > 0) {
					for (NonAssetResource nar: j.getNonAssetResources()) {
						Long stockNum = nar.getStockNumber();
						NonAssetResource overallNar = getNARFromNsn(stockNum);
						if (nar.getQuantityOnHand() > overallNar.getQuantityOnHand()) return false;
					}
				}
	
				// Step 4
				for (AssetResource ar2: j.getAssetResources()) {
					Asset aa = assetResourceToAssetMap.get(ar2.getId());
					if (aa != null && aa.getId().equals(a.getId())) return false;
				}	
				
				// Step 5
				if (j.getOrganizationId() != null && j.getOrganizationId() != 0 && !j.getOrganizationId().equals(a.getOrganizationId())) return false;
	
	
				// Step 6
				if (ar.getRequiredAssetId() != null && ar.getRequiredAssetId() != 0 && !ar.getRequiredAssetId().equals(a.getId())) return false;
			
				// Step 7
				if (ar.getProficiencies() != null) {		
					for (Proficiency pr: ar.getProficiencies()) {	
						int jobProfLvl = pr.getLevel();			
						Long jobProfClass = pr.getClassificationId();
						int  assetProfLvl = -1;
						for (Proficiency apr: a.getProficiencies()) {
							if (apr.getClassificationId().equals(jobProfClass)) {
								assetProfLvl = apr.getLevel();
							}
						}
						if (assetProfLvl == -1) return false; // none found
						switch (pr.getFunction()) {
						case 1: // >=
							if ((assetProfLvl - jobProfLvl) < 0) return false;
							break;
						case 2: // <=
							if ((assetProfLvl - jobProfLvl)> 0) return false;
							break;
						case 3: // ==
							if ((assetProfLvl - jobProfLvl) != 0) return false;
							break;
						case 4: // existence
							break;
						default: // 
							return false;
						}
					}
				}
				
				// Step 8
				if (ar.getAssetAttributes() != null)
					for (AssetAttribute at: ar.getAssetAttributes()) {
						int jobAtrVal = at.getValue();
						Long jobAtrId = at.getId();
						int  assetAtrVal = -1;
						for (AssetAttribute aat: a.getAssetAttributes()) {
							if (aat.getId().equals(jobAtrId)) {
								assetAtrVal = aat.getValue();
							}
						}
						if (assetAtrVal == -1) return false; // none found
						
						switch (at.getFunction()) {
						case 1: // >=
							if ((assetAtrVal - jobAtrVal) < 0) return false;
							break;
						case 2: // <=
							if ((assetAtrVal - jobAtrVal)> 0) return false;
							break;
						case 3: // ==
							if ((assetAtrVal - jobAtrVal) != 0) return false;
							break;
						case 4: // existence
							break;
						default: // >=
							return false;
						}
					}
				
				// Step 9
				Asset oldAssignedAsset = assetResourceToAssetMap.get(ar.getId());
				assignAssetResource(ar, a);
				Long slack = getFreeTime(a);
				deassignAssetResource(ar);
				assignAssetResource(ar, oldAssignedAsset);
				if (slack < 0) return false;
	
				// all steps pass
				return true;
			}
	
			// see if a sway passes all hard constraints
			@Override
			public boolean assetSwapsFitAR(AssetResource ar1, AssetResource ar2) {
			    // Make sure old assignments are removable
				Job j1 = assetResourceJobMap.get(ar1.getId());
				Job j2 = assetResourceJobMap.get(ar2.getId());
			    if (!assetRemovableFromAR(j1, ar1) || !assetRemovableFromAR(j2, ar2)) {
			    	return false;
			    }
			    // temporarily remove old assignments in preparation for test
			    Asset a1 = assetResourceToAssetMap.get(ar1.getId());
				if (a1 != null) scheduleHelper.removeAssetFromAssetResource(j1, ar1);
			    Asset a2 = assetResourceToAssetMap.get(ar2.getId());
			    if (a2 != null) scheduleHelper.removeAssetFromAssetResource(j2 ,ar2);	    	
	
			    // see if we can then assign the swap
			    boolean theResult =  assetFitsAR(j1, ar1, a2) && assetFitsAR(j2, ar2, a1);
	
			    // restore original assignments
			    if (a1 != null) scheduleHelper.assignAssetToAssetResource(j1, ar1, a1);
			    if (a2 != null) scheduleHelper.assignAssetToAssetResource(j2, ar2, a2);
			    return theResult;
			}
			
			// compute travel time for one asset
			@Override
			public long assetTravelTime(Asset a) {
				 long ans = 0;
				 	 
				 // create sets to hold locators for each level of the locator (max getParsedTravelVector().length)
				 int travelVectorSize = sid.getMetaData().getParsedTravelVector().length;
				 @SuppressWarnings("unchecked")
				 Set<String> locStrings[] = new HashSet[travelVectorSize];
				 for (int i = 0; i < travelVectorSize; i++) {
					 locStrings[i] = new HashSet<String>();
				 }
				 // for each asset resource that is assigned this asset, add his locator component to each level
				 Set<AssetResource> ars = getAssetResourcesForAsset(a); // get asset resources this asset is assigned to
				 for (AssetResource ar: ars) {
					 String[] locatorComponents = assetResourceLocators.get(ar.getId());
					 for (int i = 0; i < Math.min (travelVectorSize,locatorComponents.length); i++) {
						 locStrings[i].add(locatorComponents[i].trim());
					 }
				 }
	
				 // add up the penalties at each level
				 for (int i = 0; i < travelVectorSize; i++) {
					 if (locStrings[i].size() > 1) ans += (locStrings[i].size()-1) * sid.getMetaData().getParsedTravelVector()[i];
				 }
			 return ans;
			}
			
			// compute how much time is required for asset resource assignments for one asset
			@Override
			public long assetUsedTime(Asset a) {
				long tm = 0;
				for (Job j: sid.getJobs()) {
					for (AssetResource ar: j.getAssetResources()) {
						if (assetResourceToAssetMap.get(ar.getId()) != null && assetResourceToAssetMap.get(ar.getId()).getId().equals(a.getId())) { 
							tm += j.getEstimatedTime() * sid.getMetaData().getConversionFactor();
						}
					}
				}
				return tm;
			}
			
			// compute how much time is unallocated for one asset
			long getFreeTime(Asset a) {
				return assetTimeAvailable.get(a.getId()) - assetUsedTime(a) - (a.getTrackTravel()==0 ? 0 : assetTravelTime(a));
			}
		}
		
		// end of inner class SchedulingHardConstraints
		
		// beginning of inner class TabuScheduling
		
		class TabuScheduling implements ScheduleMaker {
			
			TabuScheduling() {}
	
			// initialize and run tabu search
			@Override
			public float makeSchedule(int numIterations) {
				long startNano = System.nanoTime();			
				initializeTabuList();
				int it = doTabuScheduling(numIterations);
				float timeNano = (System.nanoTime()-startNano)/1000000.0f;
				if 	(GENERATE_OUTPUT != 0) {
					LOGGER.log(Level.INFO, "ran " + it + " iterations out of maximum " + numIterations + " elapsed time: " + timeNano/1000f+ " seconds"
							+ " totalSchedulesAttempted " + totalTrys + " totalLegalSchedulesMeasured " + totalLegalTrys);
				}
	
				return timeNano;
			}
			
			// main tabu search routine
			int doTabuScheduling(int numIterations) {
				/*
				 *    V4.0 Search Strategy doTabuScheduling does a one-time initialization (job queue ordered by priority x time on job, job asset resorces 
			    assigned asset that maximizes delta utility, ties broken randomly) and then schedules individual job assets.  
	
			    It requires three types of moves: assign asset to job asset, deassign asset from job asset, and swap assets between two job assets.  
			    The basic strategy is to explore the neighborhood consisting of allowable add, swap and, where necessary, delete moves, and to
			     then make the move that has the highest delta utility (hill climbing).  
	
			    The three types of moves are used equally:  If an add, swap or remve  move is available, the best one is taken, where
			    best is defined as the highest delta utility of all non-tabu moves.  The move is recoded in the tabu list.  If no such  move is available, then any assets
			    assigned to job asset resources fo jobs that are not fully asseted are deassigned, and the scheduler begins another "ply" of search.  
			    When all the ply have expired the search ends.
	
			   Search doTabuScheduling  varyies the length of the tabu list in the following way:
			     -- if the move is for a new best or a new complete best, and the tabu list is longer than the minimum length, then increment the length of the tabu list
			     -- if the move is not for a new best or a new complete best, and the tabu list is shorter than the maximum length, then decrement the length of the tabu list
			*/
			    BigDecimal      addBestValue, swapBestValue, removeBestValue;
			    Job             addBestJob = null, removeBestJob = null;
			    Asset           addBestAsset = null, removeBestAsset = null;
			    AssetResource   addBestAR = null, swapBestAR1 = null, swapBestAR2 = null, removeBestAR = null;
			    
			    totalTrys = 0l;
			    totalLegalTrys = 0l;
			    
			    BigDecimal negLarge = new BigDecimal(-1000);
			    previousBest = new BigDecimal(-1000);
			    previousBestForPly = new BigDecimal(-1000);
			    bestUtilitySeen = new BigDecimal(-1000);
			    bestCompleteUtilitySeen = new BigDecimal(-1000);

			    int it = 0;                  // iteration counter
			    int itNoChange = 0;
			    int ply = 0;                 // ply counter (each ply has up to  itLImit iterations)
			    int plyLimit = 100;          //  max number of times to tear down and start again
			    doInitialScheduling();		//  first pass - try fill each asset resource with an asset that maximally increase overall utility
			    checkForBestUtility(it);
				while (ply < plyLimit) {
				    ply++;
				    while (it < numIterations) {
				        it++;
				        tabu.nextIteration();

				        //  1. Assess the value of assigning a JAR 
				        addBestValue = negLarge; 
				        for (Job j: sid.getJobs()) {
				        	for (AssetResource ar: j.getAssetResources()) {
				        		totalTrys++;
				        		if (assetResourceUnassigned(ar)) {  // Optimization:  Cache a list of unassigned AssetResources
				        			for (Asset a: sid.getAssets()) {
				        				if (a.getClassificationId().equals(ar.getClassificationId()) && hc.assetFitsAR(j, ar, a)) {
				        					totalLegalTrys++;
				        					if (acceptAddMove(ar, a)) {
				        						BigDecimal newValue = utilityCalculator.getAddDeltaUtility(ar, a);
				        						if (newValue.compareTo(addBestValue) > 0) {
				        							addBestValue = newValue;
				        							addBestAsset = a;
				        							addBestJob = j;
				        							addBestAR = ar;
				        						}
				        					}
				        				}
				        			}
				        		}
				        	}
				        }

				        //  2. Assess the value of swapping the assets assigned to two JARs  
				        swapBestValue = negLarge; 
				        for (Job j1: sid.getJobs()) {
				        	for (Job j2: sid.getJobs()) {
				        		if (!j1.getId().equals(j2.getId())) {  // cheap optimization 
						        	for (AssetResource ar1: j1.getAssetResources()) {
							        	for (AssetResource ar2: j2.getAssetResources()) {
							        		totalTrys++;
							        		if 	(ar1.getClassificationId().equals(ar2.getClassificationId()) // that have the same classification
			                                   && ar1.getId() > ar2.getId()                                  // on two different asset resources
							        		   && !(assetResourceUnassigned(ar1) &&  assetResourceUnassigned(ar2)) // at least one asset resource assigned
							        		   && (assetResourceUnassigned(ar1) || assetResourceUnassigned(ar2)   
							        			   || !assetResourceToAssetMap.get(ar1.getId()).getId().equals(assetResourceToAssetMap.get(ar2.getId()).getId()))   // can't be same asset
							        		   && (assetResourceUnassigned(ar1) || assetResourceUnassigned(ar2) // no point if both are assigned and neither calculates utility
						        				 || assetResourceToAssetMap.get(ar1.getId()).getCalculateUtility() != 0
						        				 || assetResourceToAssetMap.get(ar2.getId()).getCalculateUtility() != 0)
							        	       && hc.assetSwapsFitAR(ar1, ar2)) {                                       // if it meets the hard constraints
							        			totalLegalTrys++;
							        			if (acceptSwapMove(ar1, ar2)) {
							        			 BigDecimal newValue = utilityCalculator.getSwapDeltaUtility(ar1,assetResourceToAssetMap.get(ar1.getId()),ar2,assetResourceToAssetMap.get(ar2.getId()));
				        						 if (newValue.compareTo(addBestValue) > 0) {
				        							swapBestValue = newValue;
				        							swapBestAR1 = ar1;
				        							swapBestAR2 = ar2;
				        						 }
							        			}
							        	    }
					        		   }
					        	   }
				        	   }
				           }
				        }

				        // 3. Assess the value of removing an asset from an asset resource  
				        removeBestValue = negLarge; 
				        for (Job j: sid.getJobs()) {
				        	for (AssetResource ar: j.getAssetResources()) {
				        		totalTrys++;
		 		        		if (assetResourceAssigned(ar) && hc.assetRemovableFromAR(j,ar)) {
		 		        			totalLegalTrys++;
		 		        			if (acceptRemoveMove(ar)) {
		 		        					Asset at = assetResourceToAssetMap.get(ar.getId());
			        						BigDecimal newValue = utilityCalculator.getRemoveDeltaUtility(ar, at);
			        						if (newValue.compareTo(removeBestValue)>0) {
			        							removeBestValue = newValue;
			        							removeBestJob = j;
			        							removeBestAR = ar;
			        							removeBestAsset = at;
			        						}
		 		        			}
				        		}
				        	}
				        }
	
					        // 4. take the best move 
							 if (addBestValue.compareTo(negLarge) <= 0 && swapBestValue.compareTo(negLarge) <= 0 && removeBestValue.compareTo(negLarge) <= 0) {   // no more non-tabu moves possible - end of ply
			 		        	; 
					         }    
					         
					        else if (addBestValue.compareTo(swapBestValue) >= 0 && addBestValue.compareTo(removeBestValue) >= 0) {
					        	scheduleHelper.assignAssetToAssetResource(addBestJob, addBestAR, addBestAsset);   
					             boolean newBest = checkForBestUtility(it);
					             tabu.makeAddTabu(jobARIDMap.get(addBestAR.getId()),assetIDMap.get(addBestAsset.getId()), newBest);
			 		         }
	
					        else if (swapBestValue.compareTo(addBestValue) >= 0 && swapBestValue.compareTo(removeBestValue) >= 0 ) {
					        	 scheduleHelper.swapAssetsForAssetResources(swapBestAR1, swapBestAR2); 
			 		             boolean newBest = checkForBestUtility(it);
					             tabu.makeSwapTabu(jobARIDMap.get(swapBestAR1.getId()),jobARIDMap.get(swapBestAR2.getId()), newBest);  
					             tabu.makeSwapTabu(jobARIDMap.get(swapBestAR2.getId()),jobARIDMap.get(swapBestAR1.getId()), newBest);  
			 		         }  
					         
					        else if (removeBestValue.compareTo(addBestValue) >= 0 && removeBestValue.compareTo(swapBestValue) >= 0) {
					        	scheduleHelper.removeAssetFromAssetResource(removeBestJob, removeBestAR);   
					             boolean newBest = checkForBestUtility(it);
					             tabu.makeRemoveTabu(jobARIDMap.get(removeBestAR.getId()),assetIDMap.get(removeBestAsset.getId()), newBest);
					        }
					        
					        // end of iteration
				    		if (GENERATE_OUTPUT > 1)
				    			LOGGER.log(Level.INFO," *******end of iteration " + it + "  best complete util = " + bestCompleteUtilitySeen 
				    				+ " " + getNumberOfJobsAsseted() + " jobs asseted " 
				    				+ "Starting " + getNumberOfJobsStarted() + " of " + sid.getJobs().size() + " jobs ");
	
				    		boolean somethingChanged = (bestCompleteUtilitySeen != previousBest);
					        previousBest = bestCompleteUtilitySeen;
				    	    if (somethingChanged) {
				    	    	itNoChange = 0;
				    	    }
				    	    else if (++itNoChange >= MAX_NO_CHANGE) {
				    	    	break;
				    	    }
				    	    
					    }  // end of ply
			    		if (GENERATE_OUTPUT  > 1) 
			    			LOGGER.log(Level.INFO," *******end of ply " + ply);
	
					    itNoChange=0;                                         // prepare for next ply by resetting iteration counter
			    	    boolean somethingChangedForPly = (bestCompleteUtilitySeen != previousBestForPly);
				        previousBestForPly = bestCompleteUtilitySeen;
			    	    if (!somethingChangedForPly) {
			    	    	break;
			    	    }
	
			    	    boolean didRemove = removeDanglingJars(sid.getJobs());  // prepare for next ply by removing any assets assigned to JARs for jobs that are not fully asseted
						if (!didRemove) {
					    	break;
					    }
					}
	
					// end of tabu search - clean up
				    removeDanglingJars(sid.getJobs()); 
				    setFinalAssignments();
					if (GENERATE_OUTPUT  > 1)
						LOGGER.log(Level.INFO," *******end of Tabu Search sched  util = " + bestCompleteUtilitySeen+ " " + getNumberOfJobsStarted() + " complete jobs started");
				    return it;
				   }
			
			// get the list of jobs sorted by priority x time; assign asset to the job assets in job order - use eligible asset with largest free time
			void doInitialScheduling() {
				List<Job> jobs = sid.getJobs();
				Collections.sort(jobs, new Comparator<Job>() {
					public int compare(Job a, Job b) {
						return b.getPriority() * b.getEstimatedTime() - a.getPriority() * a.getEstimatedTime();
					}
				});
				
				for(Job j: jobs) {
					for (AssetResource ar: j.getAssetResources()) {
						if (assetResourceUnassigned(ar)) {
							hc.tryToAssignAssetResource(j, ar);
						}
					}
				}		
			}	
			
			// see if we have a new maximum utility for all jobs, or one for all completely asseted jobs
			boolean checkForBestUtility(int it) {
			  boolean ans = false;
	
			  // check if the current assignment is the best so far; if so remember it's utility in bestUtility
			  BigDecimal bestSoFar = utilityCalculator.getCompleteUtility();
			  if (bestSoFar.compareTo(bestUtilitySeen)>0) {     // new bests value - capture it
		        bestUtilitySeen = bestSoFar;
		        ans = true;
		      }
	
			  // check if the current assignment is the best so far for complete jobs; if so remember it's utility in bestCompleteUtility and the assignments
			  BigDecimal bestCompleteSoFar = utilityCalculator.getCompleteJobsUtility(true);
			  if (bestCompleteSoFar.compareTo(bestCompleteUtilitySeen) > 0) {     // new bests value for complet jobs - capure it
			    bestCompleteUtilitySeen = bestCompleteSoFar;                             // set new best value
			    ans = true;
			  }  
			  return ans;
			}
	
			// if the job is partially but not fully asseted, remove any asset assignments that are not sticky
			boolean removeDanglingJars(List<Job> jobs) {
				boolean ans = false;
				for(Job j: jobs) {
					if (j.getStartDateTime() != 0) {
						if (numAssignedAssetResources(j) != j.getAssetResources().size()) {
							for(AssetResource ar: j.getAssetResources()) {
								if (assetResourceAssigned(ar) && hc.assetRemovableFromAR(j,ar)) {
									scheduleHelper.removeAssetFromAssetResource(j, ar);
									ans = true;
								}
							}
						}
					}
				}
				return ans;
			}
			
			// get best complete solution found; dump any dangling assignments (e.g., for sticky assignments)
			void setFinalAssignments() {
		        for (Job j: sid.getJobs()) {  // first dump all assignments
		        	for (AssetResource ar: j.getAssetResources()) {
		        		scheduleHelper.removeAssetFromAssetResource(j, ar);
		        	}
		        }	
		        for (Job j: sid.getJobs()) { // then assign the best complete result
		        	for (AssetResource ar: j.getAssetResources()) {
		        		scheduleHelper.assignAssetToAssetResource(j,ar, bestCompleteAssetMap.get(ar.getId()));
		        	}
		        }        
				for(Job j: sid.getJobs()) { // dumping partially allocated jobs (which are due to required/assigned being set for the asset resource)
					if (j.getStartDateTime() != 0) { // we didn't dump these until the very end as they were sticky
						if (numAssignedAssetResources(j) != j.getAssetResources().size()) {
							for(AssetResource ar: j.getAssetResources()) {
								if (assetResourceAssigned(ar)) {
									scheduleHelper.removeAssetFromAssetResource(j, ar);
								}
							}
						}
					}
				}		
			}
				
		// see if an add move is acceptable
		boolean acceptAddMove(AssetResource ar, Asset a) {
		    if(!tabu.isAddTabu(jobARIDMap.get(ar.getId()), assetIDMap.get(a.getId()))) return true;
		    BigDecimal addUtil = utilityCalculator.getAddUtility(ar, a);
		    if (addUtil.compareTo(bestUtilitySeen) > 0) return true;                        // aspiration criterion
		    if(utilityCalculator.getCompleteJobsUtility(false).compareTo(bestCompleteUtilitySeen) > 0) return true;
		    return false;
		}

		// see if a swap move is acceptable
		boolean acceptSwapMove(AssetResource ar1, AssetResource ar2) {
		    if (!tabu.isSwapTabu(jobARIDMap.get(ar1.getId()), jobARIDMap.get(ar2.getId()))) return true;
		    BigDecimal swapUtil = utilityCalculator.getSwapUtility(ar1,assetResourceToAssetMap.get(ar1.getId()),ar2,assetResourceToAssetMap.get(ar2.getId())); 	
		    if (swapUtil.compareTo(bestUtilitySeen) > 0) return true;
		    if(utilityCalculator.getCompleteJobsUtility(false).compareTo(bestCompleteUtilitySeen) > 0) return true;
		    return false;
		}

		// see if a remove move is acceptable
		boolean acceptRemoveMove(AssetResource ar) {
		    if(!tabu.isRemoveTabu(jobARIDMap.get(ar.getId()), assetIDMap.get(assetResourceToAssetMap.get(ar.getId()).getId()))) return true;
		    BigDecimal removeUtil = utilityCalculator.getRemoveUtility(ar, assetResourceToAssetMap.get(ar.getId()));
			if (removeUtil.compareTo(bestUtilitySeen) > 0) return true;
		    if(utilityCalculator.getCompleteJobsUtility(false).compareTo(bestCompleteUtilitySeen) > 0)	return true;
		    return false;
		}				
		
		 class ScheduleCRUDHelper implements ScheduleMaker.ScheduleHelper { 
			
			 ScheduleCRUDHelper() {}
			 
	    	// remove asset assignment to asset resource requirement
			@Override
	    	public boolean removeAssetFromAssetResource(Job j, AssetResource ar) {		
	    	    if (assetResourceUnassigned(ar))  return false;
	    	    deassignAssetResource(ar);
	    	    
	    	    // if this is last assignment for this job, 
	    	    // set start time to zero and increment NAR quantity by amount used by this asset
	    	    if (jobHasNoAssetsAssigned(j)) {
	    	    	j.setStartDateTime(0L);
	    			if (j.getNonAssetResources() != null) { 
	    				for (NonAssetResource jobNar: j.getNonAssetResources()) {
	    					NonAssetResource nar = getNARFromNsn(jobNar.getStockNumber());
	    					nar.setQuantityOnHand(nar.getQuantityOnHand() + jobNar.getQuantityOnHand());
	    				}
	    	    	}
	    	    }
	    	    return true;
	    	}

	    	// exchange two asset resource assignments
			@Override
	    	public void swapAssetsForAssetResources(AssetResource ar1, AssetResource ar2) {
	    		Job j1 = assetResourceJobMap.get(ar1.getId());
	    		Job j2 = assetResourceJobMap.get(ar2.getId());
	    	    Asset a1 = assetResourceToAssetMap.get(ar1.getId());
	    	    Asset a2 = assetResourceToAssetMap.get(ar2.getId());
	    	    
	    	    removeAssetFromAssetResource(j1, ar1);
	    	    removeAssetFromAssetResource(j2 ,ar2);
	    	    assignAssetToAssetResource(j1, ar1, a2);
	    	    assignAssetToAssetResource(j2, ar2, a1);
	    	}

	    	// assign an asset to an asset resource; if this is the first assignment for this job set start and adjust NARs
			@Override
	    	public void assignAssetToAssetResource(Job j, AssetResource ar, Asset a) {
	    		if (jobHasNoAssetsAssigned(j)  && assetResourceValueAssigned(a)) {
	    			// set job start time, decrement NAR totals by number used by this job			
	    			j.setStartDateTime(1L);
	    			if (j.getNonAssetResources() != null) { 
	    				for (NonAssetResource jobNar: j.getNonAssetResources()) {
	    					NonAssetResource nar = getNARFromNsn(jobNar.getStockNumber());
	    					nar.setQuantityOnHand(nar.getQuantityOnHand() - jobNar.getQuantityOnHand());
	    				}	
	    	    	}
	    		}
	    		assignAssetResource(ar, a);
	    	}	
		} 		//	End of inner inner class ScheduleCRUDHelper.
	}    		// end of inner class TabuScheduling
		
		// beginning of inner class SchedulingSODGeneratorOutputProcessor
	
		class SchedulingSODGeneratorOutputProcessor implements SchedulingOutputProcessor {
			
			SchedulingSODGeneratorOutputProcessor() {}


			// generate SchedulingOutputData object (Note: sod.runTime set by caller of this routine)
			@Override
			public SchedulingOutputData processOutput() {
				sod = new SchedulingOutputData();
				
				sod.setUtility(bestCompleteUtilitySeen);
				sod.setNumberSchedulesAttempted(totalTrys);
				sod.setNumberLegalSchedulesAttempted(totalLegalTrys);
					
				// Enter the AssetTimeShare info
				for (Job j: sid.getJobs()) {
					if (j.getStartDateTime() != 0) {
						for (AssetResource ar: j.getAssetResources()) {
							if (assetResourceToAssetMap.get(ar.getId()) != null) {  
								AssetTimeShare ats = buildAssetTimeShare(ar);
								sod.addAssetTimeShare(ats);
							}
						}
					}
				}
			
				// Enter NAR on Jobs info
				for (Job j: sid.getJobs()) {
					if (j.getStartDateTime() != 0) {
						if (j.getNonAssetResources() != null) {
							for(NonAssetResource nar: j.getNonAssetResources()) {
								JobNAR jn = new JobNAR();
								jn.setJobID(j.getId());
								jn.setStockNumber(nar.getStockNumber());
								jn.setQuantityOnHand(nar.getQuantityOnHand());
								jn.setType(nar.getType());
								sod.addJobNAR(jn);
							}
						}
					}
				}
			
				// Enter NAR not on jobs info
				if (sid.getNARs() != null) {
					for(NonAssetResource nar:  sid.getNARs()) {
						sod.addNonAssetResource(nar);
					}
				}
				
				// Enter Error info
				sod.setProcessingErrors(errs);
				
				return sod;
			}
	
			// build output data for assignments
			AssetTimeShare buildAssetTimeShare(AssetResource ar) {
				AssetTimeShare ats = new AssetTimeShare();
				ats.setAssetID(assetResourceToAssetMap.get(ar.getId()).getId()); 
				ats.setResourceID(ar.getId());
				ats.setJobID(ar.getJob().getId());
				ats.setStartTime(0L);  // set all jobs to start at time 0
				ats.setEndTime(0L+ar.getJob().getEstimatedTime());
				return ats;
			}
				
			// main routine to provide output when processing errors > 0
			@Override
			public void processFatalOutput() {
				sod = new SchedulingOutputData();
				sod.setProcessingErrors(errs);
				return;
			}
		}
}