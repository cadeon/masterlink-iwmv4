package com.mlink.iwm.scheduler.core;

import java.io.BufferedReader;
import java.io.FileReader;
import com.mlink.iwm.scheduler.model.*;

public class InputProcessor {
	MetaData md;
	
	public String nextLine(BufferedReader br) throws Exception {
	    String com = "//";
	    String s;
	    while (true) {
	    	s = br.readLine();
	    	if (s == null) return "";
	    	if (s.isEmpty() || s.startsWith(com)) continue;
	    	return s;
	    }
	}
	
	public InputProcessor() {
		System.out.println("in Input Processor");
		md = new MetaData();
	}
	
	public MetaData getMetaData() {
		return md;
	}
	
	public void getMetaData(BufferedReader br) throws Exception {
		String s; 
	    int numFixedHeaderFields = 10;
	    
	    // get next line which contains the meta-data
	    s = nextLine(br);
		System.out.println(" got " + s); 
		if (s.isEmpty()) {
			System.out.println("wrong number of header fields");
			System.exit(1);
		}
		
		// split into its fields
		String[] temp;
		String delim = ",";
		temp = s.split(delim);
		if (temp.length != numFixedHeaderFields) {
			System.out.println("wrong number of header fields:" + temp.length);
			System.exit(1);			
		}
		
		// store them in meta-data object
		md.setPeriodStart(Long.parseLong(temp[0].trim()));		
		md.setPeriodEnd(Long.parseLong(temp[1].trim()));
		md.setTopLevelLocatorId(temp[2]);
		md.setConversionFactor(Long.parseLong(temp[3].trim()));
		md.setStatusVector(temp[4]);
		md.setKValue(Integer.parseInt(temp[5].trim()));
		md.setMaxPriority(Integer.parseInt(temp[6].trim()));
		md.setTravelVector(temp[7]);
		md.setTravelImportance(Integer.parseInt(temp[8].trim()));
		md.setTravelFunction(Integer.parseInt(temp[9].trim()));
	}
	
	public int getNumItems(BufferedReader br, String dataName) throws Exception {
		String s1 = nextLine(br).trim();
		if (s1.isEmpty()) {
			System.out.println("at end of data at number of " + dataName);
			System.exit(1);			
		}
		return Integer.parseInt(s1);
	}
	
	public void getJobAssetAttribute(BufferedReader br, AssetAttribute aa)  throws Exception {
		int numAAFields = 4;
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			System.out.println("at end of data within job asset attribute");
			System.exit(1);			
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numAAFields) {
			System.out.println("wrong number of job asset attribute fields expected:" + numAAFields + " got: " + temp.length);
			System.exit(1);			
		}
		aa.setId(Long.parseLong(temp[0].trim()));
		aa.setKey(temp[1]);
		aa.setValue(Integer.parseInt(temp[2].trim()));
		aa.setFunction(Integer.parseInt(temp[3].trim()));
	}
	
	public void getAssetAttribute(BufferedReader br, AssetAttribute aa)  throws Exception {
		int numAAFields = 3;
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			System.out.println("at end of data within  asset attribute");
			System.exit(1);			
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numAAFields) {
			System.out.println("wrong number of  asset attribute fields expected:" + numAAFields + " got: " + temp.length);
			System.exit(1);			
		}
		aa.setId(Long.parseLong(temp[0].trim()));
		aa.setKey(temp[1]);
		aa.setValue(Integer.parseInt(temp[2].trim()));
	}
	
	public void getJobAssetProficiency(BufferedReader br, Proficiency ap)  throws Exception {
		int numAPFields = 5;
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			System.out.println("at end of data within job asset proficiency");
			System.exit(1);			
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numAPFields) {
			System.out.println("wrong number of job asset proficiency fields expected:" + numAPFields + " got: " + temp.length);
			System.exit(1);			
		}
		ap.setClassificationId(Long.parseLong(temp[0].trim()));
		ap.setLevel(Integer.parseInt(temp[1].trim()));
		ap.setImportance(Integer.parseInt(temp[2].trim()));
		ap.setFunction(Integer.parseInt(temp[3].trim()));
		ap.setMaxValue(Integer.parseInt(temp[4].trim()));
	}
	public void getAssetProficiency(BufferedReader br, Proficiency ap)  throws Exception {
		int numAPFields = 2;
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			System.out.println("at end of data within asset proficiency");
			System.exit(1);			
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numAPFields) {
			System.out.println("wrong number of  asset proficiency fields expected:" + numAPFields + " got: " + temp.length);
			System.exit(1);			
		}
		ap.setClassificationId(Long.parseLong(temp[0].trim()));
		ap.setLevel(Integer.parseInt(temp[1].trim()));
	}

	public void getJobNonAssetResource(BufferedReader br, NonAssetResource nar) throws Exception {
		int numNARFields = 3;
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			System.out.println("at end of data within job non asset resources");
			System.exit(1);			
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numNARFields) {
			System.out.println("wrong number of job non asset resource fields expected:" + numNARFields + " got: " + temp.length);
			System.exit(1);			
		}
		nar.setStockNumber(Long.parseLong(temp[0].trim()));
		nar.setQuantityOnHand(Integer.parseInt(temp[1].trim()));
		nar.setType(Integer.parseInt(temp[2].trim()));
	}	
		
	public void getJobAssetResource(BufferedReader br, AssetResource ar, Job job) throws Exception {
		int numARFields = 6;
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			System.out.println("at end of data within job asset resources");
			System.exit(1);			
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numARFields) {
			System.out.println("wrong number of job asset resource fields expected:" + numARFields + " got: " + temp.length);
			System.exit(1);			
		}
		
		// basic AR data
		ar.setId(Long.parseLong(temp[0].trim()));
		ar.setJob(job);
		ar.setRequiredAssetId(Long.parseLong(temp[1].trim()));
		ar.setAssignedAssetId(Long.parseLong(temp[2].trim()));
		ar.setClassificationId(Long.parseLong(temp[3].trim()));
		int numAttrs = Integer.parseInt(temp[4].trim());
		int numProfs = Integer.parseInt(temp[5].trim());
		
		// AR attributes
		for (int k = 0; k < numAttrs; k++) {
			AssetAttribute aa = new AssetAttribute();
			getJobAssetAttribute(br, aa);
			ar.addAssetAttributes(aa);
		}
		
		// AR proficiencies
		for (int k = 0; k < numProfs; k++) {
			Proficiency ap = new Proficiency();
			getJobAssetProficiency(br, ap);
			ar.addProficiency(ap);
		}
	}
	
	public void getMaintainability(BufferedReader br, AssetAvailability aa) throws Exception {
		int numMaintainFields = 2;
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			System.out.println("at end of data within job maintainabilities");
			System.exit(1);			
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numMaintainFields) {
			System.out.println("wrong number of job maintain fields expected:" + numMaintainFields + " got: " + temp.length);
			System.exit(1);			
		}
		aa.setStartDateTime(Long.parseLong(temp[0].trim()));
		aa.setEndDateTime(Long.parseLong(temp[1].trim()));		
	}
	
	public void getNonAssetResource(BufferedReader br, NonAssetResource nar) throws Exception {
	    int numNARFields = 3;

	    String s1;
		s1 = nextLine(br);
		if (s1.isEmpty()) {
			System.out.println("at end of data within NARs");
			System.exit(1);			
		}
		
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numNARFields) {
			System.out.println("wrong number of NAR fields:" + temp.length);
			System.exit(1);			
		}
		nar.setStockNumber(Long.parseLong(temp[0].trim()));
		nar.setQuantityOnHand(Integer.parseInt(temp[1].trim()));
		nar.setType(Integer.parseInt(temp[2].trim()));;
	}
	
	public void getAsset(BufferedReader br, Asset aset) throws Exception {
	    int numAssetFields = 8;

	    String s1;
		s1 = nextLine(br);
		if (s1.isEmpty()) {
			System.out.println("at end of data within assets");
			System.exit(1);			
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numAssetFields) {
			System.out.println("wrong number of asset fields:" + temp.length);
			System.exit(1);			
		}
		
		// basic asset fields
		aset.setId(Long.parseLong(temp[0].trim()));
		aset.setClassificationId(Long.parseLong(temp[1].trim()));
		aset.setOrganizationId(Long.parseLong(temp[2].trim()));
		aset.setTrackTravel(Integer.parseInt(temp[3].trim()));
		aset.setCalculateUtility(Integer.parseInt(temp[4].trim()));
		int numAttrs = Integer.parseInt(temp[5].trim());
		int numProfs = Integer.parseInt(temp[6].trim());
		int numMaintain = Integer.parseInt(temp[7].trim());

		// Asset attributes
		for (int k = 0; k < numAttrs; k++) {
			AssetAttribute aa = new AssetAttribute();
			getAssetAttribute(br, aa);
			aset.addAssetAttributes(aa);
		}
		
		// Asset proficiencies
		for (int k = 0; k < numProfs; k++) {
			Proficiency ap = new Proficiency();
			getAssetProficiency(br, ap);
			aset.addProficiency(ap);
		}
		
		// Asset Maintainabilities
		for (int k = 0; k < numMaintain; k++) {
			AssetAvailability aa = new AssetAvailability();
			getMaintainability(br, aa);
			aset.addAvailability(aa);
		}

	}
	
	public void getJob(BufferedReader br, Job job) throws Exception {
	    int numJobFields = 15;
	    String s1;
		s1 = nextLine(br);
		if (s1.isEmpty()) {
			System.out.println("at end of data within jobs");
			System.exit(1);			
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numJobFields) {
			System.out.println("wrong number of job fields:" + temp.length);
			System.exit(1);			
		}
		
		// basic job fields
		job.setId(Long.parseLong(temp[0].trim()));
		job.setManagedAssetId(Long.parseLong(temp[1].trim()));
		job.setLocator(temp[2]);
		job.setStatus(Integer.parseInt(temp[3].trim()));
		job.setEstimatedTime(Integer.parseInt(temp[4].trim()));
		job.setRecordedTime(Integer.parseInt(temp[5].trim()));
		job.setPriority(Integer.parseInt(temp[6].trim()));
		job.setOrganizationId(Long.parseLong(temp[7].trim()));
		job.setStartDateTime(Long.parseLong(temp[8].trim()));
		job.setEndDateTime(Long.parseLong(temp[9].trim()));
		int numMaintain = Integer.parseInt(temp[10].trim());
		int numAR = Integer.parseInt(temp[11].trim());
		int numNAR = Integer.parseInt(temp[12].trim());
		job.setEarliestStart(Long.parseLong(temp[13].trim()));
		job.setLatestEnd(Long.parseLong(temp[14].trim()));
		
		// Job Maintainabilities
		for (int k = 0; k < numMaintain; k++) {
			AssetAvailability aa = new AssetAvailability();
			getMaintainability(br, aa);
			job.addMaintainability(aa);
		}
		
		// Job Asset Resources
		for (int k = 0; k < numAR; k++) {
			AssetResource ar = new AssetResource();
			getJobAssetResource(br, ar, job);
			job.addAssetResource(ar);
		}
		
		// Job Non Asset Resources
		for (int k = 0; k < numNAR; k++) {
			NonAssetResource nar = new NonAssetResource();
			getJobNonAssetResource(br, nar);
			job.addNonAssetResource(nar);
		}
		
	}
	
	public SchedulingInputData parse(String filename) throws Exception {
		SchedulingInputData sid = new SchedulingInputData();
		FileReader fr = new FileReader(filename);        // setup to read input file
		BufferedReader br = new BufferedReader(fr); 
		getMetaData(br);        // get meta data from inputs
		sid.setMetaData(md);
				
		// Process Jobs
		String s;
		int numJobs = getNumItems(br, "Jobs");     
		System.out.println("number of Jobs = " + numJobs );
		for (int i = 0; i < numJobs; i++) {
			Job job = new Job();
			getJob(br, job);
			sid.addJob(job);
		}
		
		// Process Assets
		int numAssets = getNumItems(br, "Assets");     
		System.out.println("number of Assets = " + numAssets );
		for (int i = 0; i < numAssets; i++) {
			Asset aset = new Asset();
			getAsset(br, aset);
			sid.addAsset(aset);
		}

		// Process Non Asset Resources
		int numNARs = getNumItems(br, "NARs");     
		for (int i = 0; i < numNARs; i++) {
			NonAssetResource nar = new NonAssetResource();
			getNonAssetResource(br, nar);
			sid.addNAR(nar);
		}
		
		// Dump the rest - should not be any
		while((s = nextLine(br)) != "") { 
		System.out.println(s); 
		} 
		fr.close(); 	
		
		return sid;
	}
}
