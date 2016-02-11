package com.mlink.iwm.scheduler.core;

import java.io.*; 
import java.util.*;

import com.mlink.iwm.scheduler.model.*;

/*
 *   
 * To do:
 *   Utility - (48T)
 *     -- Model lines 806-823(T), 953-982(T), 
 *   Search - (257T)
 *     -- Model lines 1205-1221(T), 1370-1384(T), 1894-2041(T)
 *     -- TabuSearch lines 1-79(T)
 *   Travel time - (64)
 *     -- Model lines 35-46, 466-474, 547-571  (travel time arrays)
 *     -- Model lines 1223-1250 (slack time)
 *     -- Model lines already included: 755-794
 *     -- Cache value computed for hard constraint, reuse for utility calculation
 */

public class SchedulerController {
	InputProcessor ip;  // parses input
	String inFileName;  // for file inputs
	SchedulingInputData sid;  // from Scheduler core or xlated from file inputs
	SchedulingOutputData sod;  // to Scheduler core or xlated into file outputs
	HashMap<Long,Asset> assetMap;   // maps asetResourceId to asset
	HashMap<Long,Long> managedAssetTimeAvailable; // maps managed asset iD to time available
	HashMap<Long,Long> assetTimeAvailable; // maps managed asset iD to time available
	RecalculateIndividualAssetsUtilityCalculator riac;
	public static final int TRAVEL_VECTOR_SIZE = 6;
	
	public SchedulerController() {
		System.out.println("Hello World");
		ip = new InputProcessor();
	}
	

		
	// this could be cached in a map
	public NonAssetResource getNARFromNsn(Long nsn) {
		for (NonAssetResource nar: sid.getNARs()) {
			if ((nar.getStockNumber() - nsn) == 0) {
				return nar;
			}
		}
		return null;
	}
	
	// this could be cached in a map
	public Asset getAssetFromID(Long assetId) {
		for (Asset a: sid.getAssets()) {
			if ((a.getId() - assetId) == 0) {
				return a;
			}
		}
		return null;
	}
	
	// this could be cached in a map
	public AssetResource getAssetResourceFromID(Long arId) {
		for (Job j: sid.getJobs()) {
			for (AssetResource ar: j.getAssetResources()) {
				if (ar.getId().equals(arId)) {
					return ar;
				}
			}
		}
		return null;
	}
	
	// map from AssetResource ID to Asset assigned to it
	public void initializeAssetMap() {
		for (Job j: sid.getJobs()) {
			for (AssetResource ar: j.getAssetResources()) {
				assetMap.put(ar.getId(), getAssetFromID(ar.getAssignedAssetId()));
			}
		}		
	}
	

	public void boundMaintainabilities() {
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
	
	public boolean assetRemovableFromAR(Job j, AssetResource ar) {
	/*  
	     Conditions that will allow an asset to be removed from an asset resource:
	        jobAssignedID is 0 
	        jobRequestedId is 0
	        job status is in one of the states permissed by the meta-data
	*/
		return (ar.getAssignedAssetId() == 0) || (ar.getRequiredAssetId() == 0) 
	        	|| !sid.getMetaData().getParsedStatusAsSet().contains(j.getStatus());
	}
	
	public boolean removeAssetFromAssetResource(Job j, AssetResource ar) {		
		// remove asset assignment to asset resource requirement
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

	boolean assetSwapsFitAR(AssetResource ar1, AssetResource ar2) {
	    // Make sure old assignments are removable
		Job j1 = riac.assetResourceJobMap.get(ar1);
		Job j2 = riac.assetResourceJobMap.get(ar2);
	    if (!assetRemovableFromAR(j1, ar1) || !assetRemovableFromAR(j2, ar2)) return false;

	    // temporarily remove old assignments in preparation for test
	    Asset a1 = assetMap.get(ar1.getId());
	    Asset a2 = assetMap.get(ar2.getId());
	    removeAssetFromAssetResource(j1, ar1);
	    removeAssetFromAssetResource(j2 ,ar2);

	    // see if we can then assign the swap
	   boolean theResult =  assetFitsAR(j1, ar1, a1) && assetFitsAR(j2, ar2, a2);

	    // restore original assigments
	    assignAssetToAssetResource(j1, ar1, a1);
	    assignAssetToAssetResource(j2, ar2, a2);
	    return theResult;
	}
	
	public void swapAssetsForAssetResources(AssetResource ar1, AssetResource ar2) {
		Job j1 = riac.assetResourceJobMap.get(ar1);
		Job j2 = riac.assetResourceJobMap.get(ar2);
	    Asset a1 = assetMap.get(ar1.getId());
	    Asset a2 = assetMap.get(ar2.getId());
	    
	    removeAssetFromAssetResource(j1, ar1);
	    removeAssetFromAssetResource(j2 ,ar2);
	    assignAssetToAssetResource(j1, ar1, a2);
	    assignAssetToAssetResource(j2, ar2, a1);
	}

	public void dumpNonStickyJobs() {
		for (Job j: sid.getJobs()) {
			for (AssetResource ar: j.getAssetResources()) {
				if (assetRemovableFromAR(j, ar)) {
					removeAssetFromAssetResource(j, ar);
				}
			}
		}
	}

	public void printAssetMap() {
		Set<Map.Entry<Long, Asset>> set2 = assetMap.entrySet();
		for (Map.Entry<Long, Asset> me2: set2) {
			System.out.println("xxxxxxxx" +
					"xxxxx asset map asset res " + me2.getKey() + " asset assigned " + me2.getValue());
		}		
	}

	public void printAssetResourceMap() {
		HashSet<AssetResource> hs1;	
		System.out.println(" riac " + riac);
		Set<Map.Entry<Long,HashSet<AssetResource>>> set2 = riac.assetResourceMap.entrySet();		
		if (set2 != null) {
			for (Map.Entry<Long,HashSet<AssetResource>> me2: set2) {
				System.out.print("xxxxxxxxxxxxxxxxx asset resource map asset " + me2.getKey() + " ars assigned to ");
				hs1 = me2.getValue();
				if (hs1 != null) {
					for (AssetResource ar: hs1) {
						System.out.print("  " + ar.getId());
					}
					System.out.println(" ");
				}
			}		
		}	
	}	
	
	public void initializeSchedule() {
		initializeAssetMap();	// need to do this before initializing riac
		this.riac = new RecalculateIndividualAssetsUtilityCalculator();
		dumpNonStickyJobs();
	}
	
	public List<Asset> getQualifiedAssetsInClass(Job j, AssetResource ar) {
		Long id = ar.getClassificationId();
		List<Asset> ans = new ArrayList<Asset>();
		for (Asset a: sid.getAssets()) {
//			if (a.getClassificationId().equals(id)) System.out.println(" init asset " + a.getId() + " job " + j.getId()
//					+ (assetFitsAR(j,ar,a) ? " YES" : " NO"));
			if (a.getClassificationId().equals(id) && assetFitsAR(j,ar,a)) ans.add(a);
		}
		return ans;
	}
	
	public void assignAssetToAssetResource(Job j, AssetResource ar, Asset a) {
		if (jobHasNoAssetsAssigned(j)  && assetResourceValueAssigned(a)) {
			// set job start time, decrement NAR totals by number used by this job			
//			System.out.println("about to set start date for job  " + j.getId() + " ar " + ar.getId() + " asset id " + a.getId());
			j.setStartDateTime(1L);
			if (j.getNonAssetResources() != null) { 
				for (NonAssetResource jobNar: j.getNonAssetResources()) {
					NonAssetResource nar = getNARFromNsn(jobNar.getStockNumber());
					nar.setQuantityOnHand(nar.getQuantityOnHand() - jobNar.getQuantityOnHand());
				}	
	    	}
		}
		// assign Asset to AssetResource
//		System.out.println("about to do assignment asset res id " + ar.getId() + " asset id " + a.getId());
		assignAssetResource(ar, a);
	}
	
	public boolean tryToAssignAssetResourceSimple(Job j, AssetResource ar) {
		// AssetResource ar is currently unassigned
		// consider all assets of the class required by ar, 
		List<Asset> as = getQualifiedAssetsInClass(j, ar);	
		if (as.isEmpty()) { 
			return false;
		}	
		//  of those that may be assigned choose the one with the most free time
		Collections.sort(as, new Comparator<Asset>() {
			public int compare(Asset a, Asset b) {
				return (int)getFreeTime(b)  - (int)getFreeTime(a);
			}
		});
		assignAssetToAssetResource(j, ar, as.get(0));
		return true;
	}
	
	public boolean removeDanglingJars(List<Job> jobs) {
		boolean ans = false;
		for(Job j: jobs) {
			if (j.getStartDateTime() != 0) {
				if (numAssignedAssetResources(j) != j.getAssetResources().size()) {
					for(AssetResource ar: j.getAssetResources()) {
						if (assetResourceAssigned(ar)) {
							deassignAssetResource(ar);
							ans = true;
						}
					}
				}
			}
		}
		return ans;
	}
	

	//this is the same as doInitialSchedulling but calls tryToAssignAssetResourceSimple instead of tryToAssignAssetResource
	public void doSimpleScheduling() {
		// get the list of jobs sorted by priority x time
		List<Job> jobs = sid.getJobs();
		Collections.sort(jobs, new Comparator<Job>() {
			public int compare(Job a, Job b) {
				return b.getPriority() * b.getEstimatedTime() - a.getPriority() * a.getEstimatedTime();
			}
		});

		/*
		System.out.println("sorted jobs: ");
		for(Job j: jobs) {
			System.out.println("job " + j.getId() + " with priority " + j.getPriority() + " est time " + j.getEstimatedTime()); 
		}
*/		
		
		// Assign asset to the job assets in job order - use eligible asset with largest free time
		for(Job j: jobs) {
			for (AssetResource ar: j.getAssetResources()) {
				if (assetResourceUnassigned(ar)) {
					tryToAssignAssetResourceSimple(j, ar);
				}
			}
		}
		// all AssetResources must be assigned for a job; otherwise completely unassign
		removeDanglingJars(jobs);	
	}
	
	public void doInitialScheduling() {
		
		// get the list of jobs sorted by priority x time
		List<Job> jobs = sid.getJobs();
		Collections.sort(jobs, new Comparator<Job>() {
			public int compare(Job a, Job b) {
				return b.getPriority() * b.getEstimatedTime() - a.getPriority() * a.getEstimatedTime();
			}
		});

/*
		System.out.println("sorted jobs: ");
		for(Job j: jobs) {
			System.out.println("job " + j.getId() + " with priority " + j.getPriority() + " est time " + j.getEstimatedTime()); 
		}
*/		
		
		// Assign asset to the job assets in job order - use eligible asset with largest free time
		for(Job j: jobs) {
			for (AssetResource ar: j.getAssetResources()) {
				if (assetResourceUnassigned(ar)) {
					tryToAssignAssetResource(j, ar);
				}
			}
		}		
		
		// all AssetResources must be assigned for a job; otherwise completely unassign
//		printUtiliity();
		removeDanglingJars(jobs);	
//		printUtiliity();
	}	

	public void printUtiliity() {
		System.out.println(" complete utility = " + riac.getCompleteUtility());
/*
		for (Asset a: sid.getAssets()) {
			System.out.println("Asset " + a.getId() + " utility " + riac.getAssetUtility(a));
			System.out.print(" Asset Resources: ");
			for (AssetResource ar: riac.assetResourceMap.get(a.getId())) {
				System.out.print(ar.getId() + "  ");
			}
			System.out.println(" ");
		}
*/
	}
	
	public void doHillClimbing() {
/*		
	}
		 	Assign asset that maximizes delta utility, ties broken randomly, and schedules individual job assets.  

		    It requires three types of moves: assign asset to job asset, remove asset from job asset, and swap assets between two job assets.  
		    The basic strategy is to explore the neighborhood consisting of allowable add, swap and, where necessary, remove moves, and to
		     then make the move that has the highest delta utility (hill climbing).  

		    The three types of moves are used in a priority order:  If an add move is available, the best one is taken, where
		    best is defined as the highest delta objective utility of all add moves, but only if the delta utiliry is positive.
		    If no such add move is available, the highest delta utility swap move is taken, if there is one with positive delta utility. 
		    If neither an add move nor a swap move has positive delta utility,  then the remove move of highest delta utility is taken, 
		    if there is one of positive value.  If there is not, then any assets assigned to job asset resources for jobs that are not fully asseted 
		    are removed, and the scheduler begins another "ply" of search.  When all the ply have expired the search ends.
*/
		    float bestValue;
		    float negLarge = -1000F;
		 
		    int it = 0;                  // iteration counter
		    int itLimit = 20;            // max number of iterations 
		    int ply = 0;                 // ply counter (each ply has up to  itLImit iterations)
		    int plyLimit = 100;          //  max number of times to tear down and start again
		    doInitialScheduling();		//  first pass - try fill each asset resource with an asset that maximally increase overall utility
		    
			while (ply < plyLimit) {
			    ply++;
			    while (it < itLimit) {
			        it++;

		        //  1. Assess the value of assigning an Asset Resource - If there is an assignment with positive delta value, take it
		        Asset bestAsset = null; Job bestJob = null; AssetResource bestAssetResource = null; AssetResource bestAssetResource2 = null;

		        bestValue = negLarge; 
		        for (Job j: sid.getJobs()) {
		        	for (AssetResource ar: j.getAssetResources()) {
		        		if (assetResourceUnassigned(ar)) {  // Optimization:  Cache a list of unassigned AssetResources
		        			for (Asset a: sid.getAssets()) {
		        				if (a.getClassificationId().equals(ar.getClassificationId())) { // Optimization:  Cache a list of Assets of correct class for each AssetResources
		        					if (assetFitsAR(j, ar, a)) {
		        						float newValue = riac.getAddDeltaUtility(ar, a);
		        						if (newValue > bestValue) {
	        							bestValue = newValue;
		        							bestAsset = a;
		        							bestJob = j;
		        							bestAssetResource = ar;
		        						}
		        					}
		        				}
		        			}
		        		}
		        	}
		        }
			      if (bestValue > 0) {
	        		 assignAssetToAssetResource(bestJob, bestAssetResource, bestAsset);
	        		 continue;
	        	} 
		        //  2. Assess the value of swapping the Assets for two Asset Resources - If there is a swap with positive delta value, take it
	        	bestValue = negLarge; 
		        for (Job j: sid.getJobs()) {
		        	for (Job j2: sid.getJobs()) {
		        		if (!j.equals(j2)) {  // cheap optimization 
				        	for (AssetResource ar1: j.getAssetResources()) {
					        	for (AssetResource ar2: j2.getAssetResources()) {
					        		if (!(assetMap.get(ar1)==null && assetMap.get(ar2)== null) // if at least one asset resource assigned
					        				&& ((assetMap.get(ar1)==null) || (assetMap.get(ar2)== null)) // and if they are both assigned
					        				&& !assetMap.get(ar1).equals(assetMap.get(ar2))  // then to two different assets  
					        			&& !ar1.equals(ar2)	                                  // on two different asset resources
				        				&& ar1.getClassificationId().equals(ar2.getClassificationId()) // that have the same classification
				        				&& assetSwapsFitAR(ar1,ar2)) {                // and the swap preserves all the hard constraints
			    						float newValue = riac.getSwapDeltaUtility(ar1, assetMap.get(ar1), ar2, assetMap.get(ar2));
			    						System.out.println("trying swap delta");
										if (newValue > bestValue) {
											bestValue = newValue;
											bestAssetResource  = ar1;
											bestAssetResource2 = ar2;
						        		}
					        		}
					        	}
					        }
		        		}
		        	}
		    	}
        
		        if (bestValue > 0) {
	        		swapAssetsForAssetResources(bestAssetResource, bestAssetResource2);
	        		 continue;
	        	} 

	        	//  3. Assess the value of removing an Asset Resource - If there is one with positive delta value, take it
	        	bestValue = negLarge; 
 		        for (Job j: sid.getJobs()) {
 		        	for (AssetResource ar: j.getAssetResources()) {
 		        		if (assetResourceAssigned(ar) && assetRemovableFromAR(j, ar)) {  // Optimization:  Cache a list of unassigned AssetResources
 		        			float newValue = riac.getRemoveDeltaUtility(ar,assetMap.get(ar.getId()));
 		        			if (newValue > bestValue) {
    							bestValue = newValue;
    							bestJob = j;
    							bestAssetResource = ar;
 		        			}
 		        		}
 		        	}
 		        }
	        	if (bestValue > 0) {
	        		 removeAssetFromAssetResource(bestJob, bestAssetResource);
	        	}
	        	
	    		System.out.println(" *******end of iteration " + it + "  util = " + riac.getCompleteUtility());
//	    		printUtiliity();
	    		
                if (bestValue <= 0) {
	        	      break;  // no more hill climbing possible - end of ply
                }      
		    }  // end while it
			boolean didRemove = removeDanglingJars(sid.getJobs());  // prepare for next ply by removing any assets assigned to JARs for jobs that are not fully asseted
		    if (!didRemove) {
		    	break;  
		    }
	   }  // end while ply		  
	   removeDanglingJars(sid.getJobs()); 
		
		System.out.println(" *******end of HC sched  util = " + riac.getCompleteUtility());

	}
	
	public boolean tryToAssignAssetResource(Job j, AssetResource ar) {
		// AssetResource ar is currently unassigned
		// consider all assets of the class required by ar, 
		List<Asset> as = getQualifiedAssetsInClass(j, ar);	
//		System.out.println(" for job " + j.getId() + (as.isEmpty() ? " YES" : " NO"));
		if (as.isEmpty()) { 
			return false;
		}	
		//  of those that may be assigned choose the one with the highest utility
//		System.out.println(" got past isEmpty for job " + j.getId() + " ar " + ar.getId());
		Asset bestAsset = null;
		float bestAssetUtility = -1000F;
		
		for (Asset asset: as) {
			float thisAssetUtility = riac.getAddDeltaUtility(ar, asset);
			if (thisAssetUtility > bestAssetUtility) {
				bestAsset = asset;
				bestAssetUtility = thisAssetUtility;
			}
		}
//		System.out.println(" got to assign asset for job " + j.getId() + " ar " + ar.getId() + " asset " + bestAsset.getId());
		assignAssetToAssetResource(j, ar, bestAsset);
		return true;
	}	
	
	public void makeSchedule() throws Exception {
		initializeSchedule();
		printAssetResourceMap();
		doSimpleScheduling();
	}
	
	public void makeHCSchedule() throws Exception {
		initializeSchedule();
		doHillClimbing();
//		printUtiliity();
	}
	
	public void makeInitialSchedule() throws Exception {
		initializeSchedule();
		doInitialScheduling();
	}
		
	public SchedulingOutputData schedule(SchedulingInputData sid) throws Exception {
		this.sid = sid;
		processInput();
		makeSchedule();
		processOutput("SchedulerOutput");
		return sod;
	}
	
	public void schedule2(String filename) throws Exception {
		inFileName = filename;
		sid = ip.parse(filename);	
		processInput();
		makeSchedule();
		processOutput(filename);
	}
	
	public void scheduleHC(String filename) throws Exception {
		inFileName = filename;
		sid = ip.parse(filename);	
		long startNano = System.nanoTime();
		long startMilli = System.currentTimeMillis();
		processInput();
		makeHCSchedule();
		processOutput(filename);
		long endNano = System.nanoTime();
		long endMilli = System.currentTimeMillis();
		System.out.println(" elapsed time: " + (endNano-startNano)/1000000L + " millisec based on nano " + (endMilli-startMilli) + " sec based on milli ");
	}
	
	public void scheduleInitial(String filename) throws Exception {  // DIAGNOSTIC:  FOr testing end to end with initial HC scheduling
		inFileName = filename;
		sid = ip.parse(filename);	
		processInput();
		makeInitialSchedule();
		processOutput(filename);
	}
	
	public void setupManagedAssetTimeMap() {
		// walk over jobs and set map entry for its managed asset to sum of time on job maintainabilities
		for (Job j: sid.getJobs()) {
			Long id = j.getManagedAssetId();
			Long timeOnAsset = 0L;
			for (AssetAvailability aa: j.getMaintainabilities()) {
				timeOnAsset += (aa.getEndDateTime() - aa.getStartDateTime());
			}
			managedAssetTimeAvailable.put(id, timeOnAsset);
		}
	}
	
	public void setupAssetTimeMap() {
		// walk over assets and set map entry for its availability to sum of time on asset maintainabilities
		for (Asset a: sid.getAssets()) {
			Long timeOnAsset = 0L;
			for (AssetAvailability aa: a.getAvailabilities()) {
				timeOnAsset += (aa.getEndDateTime() - aa.getStartDateTime());
			}
			assetTimeAvailable.put(a.getId(), timeOnAsset);				
		}
	}
	
	public void processInput() throws Exception {
		assetMap = new HashMap<Long,Asset>();   // maps asetResourceId to asset
		managedAssetTimeAvailable = new HashMap<Long,Long>(); // maps managed asset iD to time available
		assetTimeAvailable = new HashMap<Long,Long>(); // maps managed asset iD to time available

		boundMaintainabilities();
		setupManagedAssetTimeMap(); // set up Map from MangedAssetId to Available Time
		setupAssetTimeMap();  // set up Map from Asset ID to Available Time
	
		Set<Long> kset = assetTimeAvailable.keySet();
		for (Long key: kset) {
		}
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("parsed input! " + sid);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
	
	public void writeOutput(String inFilename, String outFilename) throws Exception {
		FileWriter fw = new FileWriter(outFilename);
		FileReader fr = new FileReader(inFilename); 
		BufferedReader br = new BufferedReader(fr); 
		String s; 
		while((s = br.readLine()) != null) { 
			fw.write(s + "\n"); 
		} 
		fr.close(); 
		fw.close(); 
	}
	
	public boolean assetResourceAssigned(AssetResource ar) {
		return assetMap.get(ar.getId()) != null;
	}
	
	public boolean assetResourceUnassigned(AssetResource ar) {
		return assetMap.get(ar.getId()) == null;
	}
	
	public void deassignAssetResource(AssetResource ar) {
		riac.removeARfromAssetList(assetMap.get(ar.getId()), ar);  // BUG
		assetMap.put(ar.getId(), null);
	}
	
	public void assignAssetResource(AssetResource ar, Asset a) {
		assetMap.put(ar.getId(), a);
		riac.addARtoAssetList(a, ar);
	}
	
	public boolean assetResourceValueAssigned(Asset a) {
		return a != null;
	}
	
	public boolean assetResourceValueUnassigned(Asset a) {
		return a == null;
	}
	
	public int numAssignedAssetResources(Job j) {
		int ans = 0;
		for(AssetResource ar: j.getAssetResources()) {
			if (assetResourceAssigned(ar)) ans++;
		}
		return ans;
	}
	
	public boolean jobHasNoAssetsAssigned(Job j) {
		boolean ans = true;
		for (AssetResource ar: j.getAssetResources()) {
			if (assetResourceAssigned(ar)) ans = false;
		}
		return ans;
	}
	
	public boolean assetFitsAR(Job j, AssetResource ar, Asset a) {
/*
	    Extra tests if no constraints on asset resource or managed asset timespecs and n NARs:
	      1.  If aID  unassigned, return true (no new constraint violations possible)
	      2.  Time on jobs on a managed asset fit within the time allocated for that managed asset
	      3.  Quantity of each NAR NSN is non-negative
	    If these 3 steps pass, then the basic 7 tests are run
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
		return assetFitsARBasic(j, ar, a);
	}

	public boolean  assetFitsARBasic(Job j, AssetResource ar, Asset a) {

/* Test if asset fits asset resource:
     1. If this method is called with an unassigned aID then pass (no new constraint violations possible)
     2. The asset must not be assigned to any other job asset for this job
     3. The org on the job must be 0 (all) or the org of the asset
     4. If the job asset resource has a requested id, the assigned id (if the job is scheduled)
         is for the requested id
     5. The total amount of time on the asset availabilities must allow this job (incl. travel time)
     6. Proficiency test: level of asset func proficiency level of job asset resource
        (func is >= for e.g., skill).  We assume levels are positive.  
     7.Attribute test: for each attribute in the job asset resource, the corresponding asset attribute
        key must be listed and the level of asset func attribute level of job asset resource (func may be
         "notRequired" e.g., for just needing the attribute key, >= for e.g., horsepowerl)

      Other checks are done by the search routine (checks class id match)

	  Note: The checks for tests 3, 4, 6, and 7 should be cached:  Each jaID can map to a static set of aID's 
	   that it fits.  
*/
		
	    //  Step 1		
		if (assetResourceValueUnassigned(a)) return true;

		// Step 2
		for (AssetResource ar2: j.getAssetResources()) {
			Asset aa = assetMap.get(ar2.getId());
			if (aa != null && aa.getId().equals(a.getId())) return false;
		}	
		
		// Step 3
		if (j.getOrganizationId() != 0 && !j.getOrganizationId().equals(a.getOrganizationId())) return false;


		// Step 4
		if (ar.getRequiredAssetId() != 0 && !ar.getRequiredAssetId().equals(a.getId())) return false;
	
		// Step 5
		Asset oldAssignedAsset = assetMap.get(ar.getId());
		assetMap.put(ar.getId(), a);
		Long slack = getFreeTime(a);
		assetMap.put(ar.getId(), oldAssignedAsset);
		
//		System.out.println("slack " + slack);
		
		if (slack < 0) return false;
	
		// Step 6
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

		// Step 7
		if (ar.getAssetAttributes() == null) return true;

		for (AssetAttribute at: ar.getAssetAttributes()) {
			int jobAtrVal = at.getValue();
			String jobAtrKey = at.getKey();
			int  assetAtrVal = -1;
			for (AssetAttribute aat: a.getAssetAttributes()) {
				if (aat.getKey().equals(jobAtrKey)) {
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
		return true;
	}
	
	public long assetTravelTime(Asset a) {
		// TBD
		return 0L;
	}
	
	public long assetUsedTime(Asset a) {
		long tm = 0;
		for (Job j: sid.getJobs()) {
			for (AssetResource ar: j.getAssetResources()) {
				if (assetMap.get(ar) != null && assetMap.get(ar).getId().equals(a.getId())) { 
					tm += j.getEstimatedTime() * sid.getMetaData().getConversionFactor();
				}
			}
		}
//		System.out.println("used time for asset id " + a.getId() + " is " + tm);
		return tm;
	}
	
	public long getFreeTime(Asset a) {
		// NOTE:  Does not yet include travel time computation
		return assetTimeAvailable.get(a.getId()) - assetUsedTime(a) - assetTravelTime(a);
	}

	public AssetTimeShare buildAssetTimeShare(AssetResource ar) {
		AssetTimeShare ats = new AssetTimeShare();
		ats.setAssetID(assetMap.get(ar.getId()).getId()); 
		ats.setResourceID(ar.getId());
		ats.setJobID(ar.getJob().getId());
	//	System.out.println("asset time share job id = " + ats.getJobID());
		ats.setStartTime(1L);
		ats.setEndTime(1L+ar.getJob().getEstimatedTime());
		return ats;
	}
	
	public SchedulingOutputData buildOutputs() {
		sod = new SchedulingOutputData();
			
		// Enter the AssetTimeShare info
		for (Job j: sid.getJobs()) {
			if (j.getStartDateTime() != 0) {
				for (AssetResource ar: j.getAssetResources()) {
					if (assetMap.get(ar.getId()) != null) {  
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
		// NA for this version
		
		return sod;
	}
	
	public void processOutputFile(String filnam) throws Exception {
		String delim = " ";
	
		System.out.println("file name " + filnam);
	
		FileWriter fw = new FileWriter(filnam);
	
		// Output the asset time shares
		fw.write("\n" + "// Asset ID, Asset Resource ID, job ID, start time, end time" +"\n");
		fw.write(sod.getAssetTimeShares().size() + "\n");		
		for (AssetTimeShare ats: sod.getAssetTimeShares()) {
			fw.write(ats.getAssetID() + delim + ats.getResourceID() + delim + ats.getJobID() + delim + ats.getStartTime() + delim + ats.getEndTime() + "\n");
		}
		
		// output the Job Non Asset Resources
		fw.write("\n" + "//  NSN   Quantity Used,  Type,  Job ID" + "\n");
		fw.write(sod.getjobNARs().size() + "\n");
		for (JobNAR jnar: sod.getjobNARs()) {
			fw.write(jnar.getStockNumber() + delim + jnar.getQuantityOnHand() + delim + jnar.getType() + delim + jnar.getJobID() + "\n");
			
		}
	
		// output the  Non Asset Resources
		fw.write("\n" + "//  NSN   Quantity Used,  Type" + "\n");
		fw.write(sod.getNonAssetResources().size() + "\n");
		for (NonAssetResource nar: sod.getNonAssetResources()) {
			fw.write(nar.getStockNumber() + delim + nar.getQuantityOnHand() + delim + nar.getType() + "\n");	
		}
		
		// output the Errors
		fw.write("\n" + "//  Error Severity   Text" + "\n");
		fw.write(sod.getProcessingErrors().size() + "\n");
		for (ProcessingError pe: sod.getProcessingErrors()) {
			fw.write(pe.getSeverity() + delim + pe.getMessage()  + "\n");	
		}
		fw.close();
		}
	public void processOutput(String filename) throws Exception {
		buildOutputs();
		String outFileName = inFileName+ ".txt";
		processOutputFile(outFileName);		
	}
		public class RecalculateIndividualAssetsUtilityCalculator implements
		UtilityCalculator {
	
			HashMap<Asset, Float> assetResourceUtilityMap;
			
			HashMap<Long,HashSet<AssetResource>> assetResourceMap = new HashMap<Long,HashSet<AssetResource>>();   // maps asetId to AssetResource
	
			HashMap<Long,Job> assetResourceJobMap = new HashMap<Long,Job>();         // map assetResource ID to estimated time for job it is part of 
			
			RecalculateIndividualAssetsUtilityCalculator() {	
				initializeAssetResourceJobMap();    	// initialize map from AssetResource to estimated time for its job
				initializeAssetResourceMap();			// initialize map from Asset to AssetResource it is assigned
				initializeAssetUtilityMap();			//initialize map of AssetID to Utility
			}
			
			// setup map from Asset to utility
			public void initializeAssetUtilityMap() {
				assetResourceUtilityMap = new HashMap<Asset, Float>(sid.getAssets().size() * 2);
				for(Asset a: sid.getAssets()) {
					assetResourceUtilityMap.put(a, getAssetUtility(a));
				}				
			}
			
			// setup map from asset resource to job it is part of
			public void initializeAssetResourceJobMap() {
				for (Job j: sid.getJobs()) {
					for (AssetResource ar: j.getAssetResources()) {
						assetResourceJobMap.put(ar.getId(), j);
					}
				}
			}

			// setup as map from AssetID->Set of AssetResources where assigned	
			public void initializeAssetResourceMap() {	
				for(Asset a: sid.getAssets()) {
//					System.out.println(" putting " + a.getId() + " as key, ");
					assetResourceMap.put(a.getId(), new HashSet<AssetResource>());  
				}	
				Set<Map.Entry<Long, Asset>> set = assetMap.entrySet();
				for (Map.Entry<Long, Asset> me: set) {
					if (me.getValue() != null) {
						HashSet<AssetResource> arSet = assetResourceMap.get(me.getValue().getId());  // get current set
						arSet.add(getAssetResourceFromID(me.getKey()));                              // add this AssetResource
						assetResourceMap.put(me.getValue().getId(), arSet);                          // update map
					}
				}
			}

			
			public float getAssetUtility(Asset a) {
				Set<AssetResource> set = assetResourceMap.get(a.getId());  // set of AssetResources the asset maps to
				float utilVal;                                     // start off with perfect utility

				utilVal = getBasicUtility(a,set);                  // first compute basic utility				
				utilVal *= getProficiencyPenalties(a, set);        // then penalties for proficiencies 				
				utilVal *= getTravelPenalty(a, set);               // then penalties for travel 
//				System.out.println(" utility of asset " + a.getId() + " is " +utilVal);
				return utilVal;
			}
			
			public float getBasicUtility(Asset a, Set<AssetResource> set) {
				
				// compute total time for this asset on these resources
				int assetTotalJobTime = 0;                     // time on job asset resources
				int assetTotalJobPriorityTime = 0;             // time on job asset resources x priority
				if (set != null) {
					for (AssetResource ar: set) {
						int addedTime = assetResourceJobMap.get(ar.getId()).getEstimatedTime().intValue() * sid.getMetaData().getConversionFactor().intValue();
//						System.out.println(" for asset " + a.getId() + " asset res " + ar.getId() + " added time is " + addedTime);
						assetTotalJobTime += addedTime; 
						assetTotalJobPriorityTime += addedTime * assetResourceJobMap.get(ar.getId()).getPriority();
					}
				}
				float unweightedUtility = 1F*assetTotalJobTime/assetTimeAvailable.get(a.getId());
				float weightedUtility   = 1F*assetTotalJobPriorityTime/(assetTimeAvailable.get(a.getId()) * sid.getMetaData().getMaxPriority());
							
				float kValue = sid.getMetaData().getKValue()/100F;
				float ret = (kValue*weightedUtility) + ((1-kValue) * unweightedUtility);
				return ret;
			}
			
			public float getProficiencyPenalty(Asset a, Set<AssetResource> set, Proficiency pr) {
				
				// Find all the AssetResources that are of the same class as this proficiency and for each compute estimatedTime * (1-(delta grade/max grade)) 
				float ans = 0F; float importanceTime = 0F; int totalTime = 0;
				
				for (AssetResource ar: set) {
					for(Proficiency arProf: ar.getProficiencies()){                              // find all AssetResource Proficiencies
						if (arProf.getClassificationId().equals(pr.getClassificationId())) {     // with the same Class ID
							int timeOnTask = assetResourceJobMap.get(ar.getId()).getEstimatedTime(); // get time on task from Job
							totalTime += timeOnTask;                                             // compute total time on task for this prof. for asset
							importanceTime += timeOnTask * 1F*arProf.getImportance()/100;           // used to compute average importance = sum(importance x taskTime)/totalTaskTime
							int deltaValue;
							switch (arProf.getFunction()) {
							case 1:    // >=
								deltaValue = arProf.getLevel() - pr.getLevel();
								break;
							case 2:   // <=
								deltaValue = pr.getLevel() - arProf.getLevel();
								break;
							case 3:  // =
								deltaValue = Math.abs(pr.getLevel() - arProf.getLevel());
								break;
							case 4:  // existence of proficiency is enough
							default:
								deltaValue = 0;
								
							}
//							System.out.println(" deltaValue " + deltaValue + " time on task " + timeOnTask);
							ans += timeOnTask * (1-((deltaValue)/arProf.getMaxValue()));  // timeOnTask x (1-deltaLevel/maxLevel)						
						}	
					}
				}
				ans /= assetTimeAvailable.get(a.getId());  // divide by total scheduled time on asset to compute raw penalty
				float ret = totalTime==0 ? 1 : 1F-((importanceTime/totalTime) * ans);  // 1 - (importance * (1 - rawPenalty))
				return ret;
			}
			
			public float getProficiencyPenalties(Asset a, Set<AssetResource> set) {
				
//				System.out.println("prof penalty------- asset " + a.getId() + " set " + set + " asset res map" + assetResourceMap);
				
				// multiply penalties for each proficiency on the asset
				float ans = 1F;
				for (Proficiency pr: a.getProficiencies()) {
					ans *= getProficiencyPenalty(a, set, pr);
				}
				return ans;
			}
			
			public float getTravelPenalty(Asset a, Set<AssetResource> set) {
				// TBD - after demo
				return 1F;
			}
			
			@Override
			public float getCompleteUtility() {
				int numAssetsToCompute = 0;
				float totalUtility = 0F;
				for (Asset a:  assetResourceUtilityMap.keySet()) {
					if (a.getCalculateUtility() != 0) {
						numAssetsToCompute++;                 // count this one
						totalUtility += getAssetUtility(a);
					}
				}
				return numAssetsToCompute==0 ? 0 : totalUtility/numAssetsToCompute;
			}
			
			@Override
			public float getAddDeltaUtility(AssetResource addAr, Asset addAsset) {
				if (addAsset.getCalculateUtility() == 0) return 0F;

				float initialUtility = getAssetUtility(addAsset);
				addARtoAssetList(addAsset, addAr);
				float addedUtility = getAssetUtility(addAsset);
				removeARfromAssetList(addAsset, addAr);
				return addedUtility - initialUtility;
			}
			
			@Override
			public float getRemoveDeltaUtility(AssetResource removeAr, Asset removeAsset) {
				if (removeAsset.getCalculateUtility().equals(0)) return 0F;
				float initialUtility = getAssetUtility(removeAsset);
				removeARfromAssetList(removeAsset, removeAr);
				float removedUtility = getAssetUtility(removeAsset);
				addARtoAssetList(removeAsset, removeAr);
				return removedUtility - initialUtility;
			}
			
			@Override
			public float getSwapDeltaUtility(AssetResource addAr, Asset addAsset, AssetResource removeAr, Asset removeAsset) {
				return getAddDeltaUtility(addAr, addAsset) + getRemoveDeltaUtility(removeAr, removeAsset);
			}
			
			public void addARtoAssetList(Asset addAsset, AssetResource addAr) {
				HashSet<AssetResource> arSet = assetResourceMap.get(addAsset.getId());  // get current set
				arSet.add(addAr);                                                       // add this AssetResource
				assetResourceMap.put(addAsset.getId(), arSet);                          // update map	
			}
			
			public void removeARfromAssetList(Asset addAsset, AssetResource addAr) {
				HashSet<AssetResource> arSet = assetResourceMap.get(addAsset.getId());  // get current set
				arSet.remove(addAr);                                                    // reove this AssetResource
				assetResourceMap.put(addAsset.getId(), arSet);                          // update map					
			}
		}

}