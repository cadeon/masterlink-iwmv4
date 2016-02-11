package org.mlink.agents.scheduler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.mlink.agents.scheduler.model.Asset;
import org.mlink.agents.scheduler.model.AssetAttribute;
import org.mlink.agents.scheduler.model.AssetAvailability;
import org.mlink.agents.scheduler.model.AssetResource;
import org.mlink.agents.scheduler.model.Job;
import org.mlink.agents.scheduler.model.MetaData;
import org.mlink.agents.scheduler.model.NonAssetResource;
import org.mlink.agents.scheduler.model.Proficiency;
import org.mlink.agents.scheduler.model.SchedulingInputData;

public class InputProcessor {
	MetaData md;
	
	static final int numFixedHeaderFields = 10;
	static final int numJAAFields = 4;
	static final int numAAFields = 3;
	static final int numJAPFields = 5;
	static final int numAPFields = 2;
	static final int numJNARFields = 3; 
	static final int numARFields = 6;
	static final int numMaintainFields = 2;
    static final int numNARFields = 3;
    static final int numAssetFields = 8;
    static final int numJobFields = 15;

	public InputProcessor() {
		md = new MetaData();
	}
	
	// Main routine:  Create SchedulingInputData object from file input
	public SchedulingInputData parse(String filename) throws IOException {
		SchedulingInputData sid = new SchedulingInputData();
		FileReader fr = new FileReader(filename);        // setup to read input file
		BufferedReader br = new BufferedReader(fr); 
		getMetaData(br);        // get meta data from inputs
		sid.setMetaData(md);
				
		// Process Jobs
		int numJobs = getNumItems(br, "Jobs");     
		for (int i = 0; i < numJobs; i++) {
			Job job = new Job();
			getJob(br, job);
			sid.addJob(job);
		}
		
		// Process Assets
		int numAssets = getNumItems(br, "Assets");     
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
		fr.close(); 	
		
		return sid;
	}
	
	public MetaData getMetaData() {
		return md;
	}
	
	public void getMetaData(BufferedReader br) throws IOException {
		String s; 
	    
	    // get next line which contains the meta-data
	    s = nextLine(br);
		if (s.isEmpty()) {
			throw new IWMSchedulingFileInputException("wrong number of header fields");
		}
		
		// split into its fields
		String[] temp;
		String delim = ",";
		temp = s.split(delim);
		if (temp.length != numFixedHeaderFields) {
			throw new IWMSchedulingFileInputException("wrong number of header fields:" + temp.length);
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
	
	public int getNumItems(BufferedReader br, String dataName) throws IOException {
		String s1 = nextLine(br).trim();
		if (s1.isEmpty()) {
			throw new IWMSchedulingFileInputException("at end of data at number of " + dataName);
		}
		return Integer.parseInt(s1);
	}
	
	public void getJobAssetAttribute(BufferedReader br, AssetAttribute aa)  throws IOException {
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			throw new IWMSchedulingFileInputException("at end of data within job asset attribute");
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numJAAFields) {
			throw new IWMSchedulingFileInputException("wrong number of job asset attribute fields expected:" + numJAAFields + " got: " + temp.length);
		}
		aa.setId(Long.parseLong(temp[0].trim()));
		aa.setKey(temp[1]);
		aa.setValue(Integer.parseInt(temp[2].trim()));
		aa.setFunction(Integer.parseInt(temp[3].trim()));
	}
	
	public void getAssetAttribute(BufferedReader br, AssetAttribute aa)  throws IOException {
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			throw new IWMSchedulingFileInputException("at end of data within  asset attribute");
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numAAFields) {
			throw new IWMSchedulingFileInputException("wrong number of  asset attribute fields expected:" + numAAFields + " got: " + temp.length);
		}
		aa.setId(Long.parseLong(temp[0].trim()));
		aa.setKey(temp[1]);
		aa.setValue(Integer.parseInt(temp[2].trim()));
	}
	
	public void getJobAssetProficiency(BufferedReader br, Proficiency ap)  throws IOException {
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			throw new IWMSchedulingFileInputException("at end of data within job asset proficiency");
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numJAPFields) {
			throw new IWMSchedulingFileInputException("wrong number of job asset proficiency fields expected:" + numJAPFields + " got: " + temp.length);
		}
		ap.setClassificationId(Long.parseLong(temp[0].trim()));
		ap.setLevel(Integer.parseInt(temp[1].trim()));
		ap.setImportance(Integer.parseInt(temp[2].trim()));
		ap.setFunction(Integer.parseInt(temp[3].trim()));
		ap.setMaxValue(Integer.parseInt(temp[4].trim()));
	}
	
	public void getAssetProficiency(BufferedReader br, Proficiency ap)  throws IOException {
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			throw new IWMSchedulingFileInputException("at end of data within asset proficiency");
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numAPFields) {
			throw new IWMSchedulingFileInputException("wrong number of  asset proficiency fields expected:" + numAPFields + " got: " + temp.length);
		}
		ap.setClassificationId(Long.parseLong(temp[0].trim()));
		ap.setLevel(Integer.parseInt(temp[1].trim()));
	}

	public void getJobNonAssetResource(BufferedReader br, NonAssetResource nar) throws IOException {
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			throw new IWMSchedulingFileInputException("at end of data within job non asset resources");
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numJNARFields) {
			throw new IWMSchedulingFileInputException("wrong number of job non asset resource fields expected:" + numJNARFields + " got: " + temp.length);
		}
		nar.setStockNumber(Long.parseLong(temp[0].trim()));
		nar.setQuantityOnHand(Integer.parseInt(temp[1].trim()));
		nar.setType(Integer.parseInt(temp[2].trim()));
	}	
		
	public void getJobAssetResource(BufferedReader br, AssetResource ar, Job job) throws IOException {
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			throw new IWMSchedulingFileInputException("at end of data within job asset resources");
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numARFields) {
			throw new IWMSchedulingFileInputException("wrong number of job asset resource fields expected:" + numARFields + " got: " + temp.length);
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
	
	public void getMaintainability(BufferedReader br, AssetAvailability aa) throws IOException {
		String s1 = nextLine(br);
		if (s1.isEmpty()) {
			throw new IWMSchedulingFileInputException("at end of data within job maintainabilities");
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numMaintainFields) {
			throw new IWMSchedulingFileInputException("wrong number of job maintain fields expected:" + numMaintainFields + " got: " + temp.length);
		}
		aa.setStartDateTime(Long.parseLong(temp[0].trim()));
		aa.setEndDateTime(Long.parseLong(temp[1].trim()));		
	}
	
	public void getNonAssetResource(BufferedReader br, NonAssetResource nar) throws IOException {
	    String s1;
		s1 = nextLine(br);
		if (s1.isEmpty()) {
			throw new IWMSchedulingFileInputException("at end of data within NARs");
		}
		
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numNARFields) {
			throw new IWMSchedulingFileInputException("wrong number of NAR fields:" + temp.length);
		}
		nar.setStockNumber(Long.parseLong(temp[0].trim()));
		nar.setQuantityOnHand(Integer.parseInt(temp[1].trim()));
		nar.setType(Integer.parseInt(temp[2].trim()));;
	}
	
	public void getAsset(BufferedReader br, Asset aset) throws IOException {

	    String s1;
		s1 = nextLine(br);
		if (s1.isEmpty()) {
			throw new IWMSchedulingFileInputException("at end of data within assets");
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numAssetFields) {
			throw new IWMSchedulingFileInputException("wrong number of asset fields:" + temp.length);
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
	
	public void getJob(BufferedReader br, Job job) throws IOException {
	    String s1;
		s1 = nextLine(br);
		if (s1.isEmpty()) {
			throw new IWMSchedulingFileInputException("at end of data within jobs");
		}
		String[] temp;
		String delim = ",";
		temp = s1.split(delim);
		if (temp.length != numJobFields) {
			throw new IWMSchedulingFileInputException("wrong number of job fields:" + temp.length);
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
	
	public String nextLine(BufferedReader br) throws IOException {
	    String com = "//";
	    String s;
	    while (true) {
	    	s = br.readLine();
	    	if (s == null) return "";
	    	if (s.isEmpty() || s.startsWith(com)) continue;
	    	return s;
	    }
	}
}
