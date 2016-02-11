package org.mlink.agents.scheduler.testSupport;

import java.io.FileWriter; 
import java.io.IOException; 
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

/*
Probabilistic Data Generator - used to generate synthetic data sets for Scheduler testing

Limitations of this version:
-- changing any parameters must be done in the source code here (except number of jobs and assets which are given in the call to generate()): this is just as easy to do as calling a method
-- recorded time is set to 0 : not used by scheduler prototype
-- job start time is set to 0 : not used by scheduler prototype
-- job end time is set to 0 : not used by scheduler prototype
-- asset ID requested is set to 0: sticky logic checked previously
-- asset ID  assigned is set to 0: sticky logic checked previously
-- there is exactly one job maintainability: scheduler prototype just adds up the time in all maintainabilities so one is as good as several
-- there is exactly one asset maintainability: scheduler prototype just adds up the time in all maintainabilities so one is as good as several
-- there are no job asset resource attributes: utility calculation is not based on this; logic checked previously
-- there are no asset attributes: utility calculation is not based on this; logic checked previously
-- there are no job NARs: utility calculation is not based on this; logic checked previously
-- there are no NARs: utility calculation is not based on this; logic checked previously
-- proficiencies do not sample without replacement - this would be a method perhaps generateValueFromArrayForClassWithoutDuplication
*/


public class SchedulerProbabilisticDataGenerator implements SchedulerDataGenerator {
	int jobId;
	int jobARId;
	int assetId;
	int beginTime;
	int endTime;

	static final int JOBS_PER_MANAGED_ASSET = 4;  // defines how many managed assets to create based on number of jobs
	
	static class ValueGenerator {
	    int[] values;
	    float[] perCentages;
		Random random;

	    ValueGenerator(int[] theVals, float[] thePerCents) {
	    	int numValues = theVals.length;
	        values = new int[numValues];
	        perCentages = new float[numValues];     
	        random = new Random();
	   
	        System.arraycopy(theVals, 0, values, 0, numValues);
	        System.arraycopy(thePerCents, 0, perCentages, 0, numValues);
	    }
	    
	    ValueGenerator() {}

	    int  generateValue() {
	    	float cumPercent = 0F;
	    	float nextRandom = random.nextFloat();
//	    	System.out.println(" next random " + nextRandom);
	    	for (int i = 0; i < values.length; i++) {
	    		cumPercent += perCentages[i];
	    		if (cumPercent >= nextRandom) {
//	    	    	System.out.println(" next value " + values[i]);
	    			return values[i];
	    		}
	    	}
	    	return 0; // should not get here
	    }
	}
	static class StringValueGenerator {
	    String[] values;
	    float[] perCentages;
		Random random;

	    StringValueGenerator(String[] theVals, float[] thePerCents) {
	    	int numValues = theVals.length;
	        values = new String[numValues];
	        perCentages = new float[numValues];     
	        random = new Random();
	   
	        System.arraycopy(theVals, 0, values, 0, numValues);
	        System.arraycopy(thePerCents, 0, perCentages, 0, numValues);
	    }

	    String  generateValue() {
	    	float cumPercent = 0F;
	    	float nextRandom = random.nextFloat();
//	    	System.out.println(" next random " + nextRandom);
	    	for (int i = 0; i < values.length; i++) {
	    		cumPercent += perCentages[i];
	    		if (cumPercent >= nextRandom) {
//	    	    	System.out.println(" next value " + values[i]);
	    			return values[i];
	    		}
	    	}
	    	return ""; // should not get here
	    }
	}
	
	static class ClassValueGenerator {
		HashMap<Integer,ValueGenerator> classDict;
		Random random;

		ClassValueGenerator() {
			classDict = new HashMap<Integer,ValueGenerator>();
	        random = new Random();
		}
		
	    void addValuesForClass(int theClass, ValueGenerator gen) {
	        classDict.put(theClass, gen);
	    }

	    int generateValueForClass(int theClass) {
	            ValueGenerator theVG = classDict.get(theClass);
	            return theVG.generateValue();
	    }
	}
	
	static class ClassValueArrayGenerator {
		HashMap<Integer,ValueGenerator[]> classDict;
		Random random;

		ClassValueArrayGenerator() {
			classDict = new HashMap<Integer,ValueGenerator[]>();
	        random = new Random();
		}
					
	    void addValueToArrayForClass(int theClass, ValueGenerator gen, int pos) {
            ValueGenerator[] theVG = classDict.get(theClass);
            if (theVG == null) { // lazy initialization of the array - largest value must appear first in the "data"
            	ValueGenerator[] newArray = new ValueGenerator[pos+1];
            	newArray[pos] = gen; 
            	theVG = newArray;
            }
            theVG[pos] = gen;
	        classDict.put(theClass, theVG);
	    }

	    int generateValueFromArrayForClass(int theClass, int pos) {
	            ValueGenerator theVG = classDict.get(theClass)[pos];
	            return theVG.generateValue();
	    }
	    // FUTURE: generate a value by sampling without replacement - extra parameter is array of already generated values not to duplicate
	    //   int generateValueFromArrayForClassWithoutDuplication(int theClass, int pos, int[] previouslyGeneratedValues);

	    int getSize(int theClass) {
	        return classDict.get(theClass).length;
	    }

	}
	
	public SchedulerProbabilisticDataGenerator() {
        jobId = 1;                             // incremented for each job
        jobARId = 1;                           // incremented for each job asset resource
        assetId = 1;                           // incremented for each asset
        beginTime = 0;                         // start of scheduling period
        endTime = 800;                         // end of scheduling period
	}
	
	public void generate(String fileName, int numJobs, int numAssets, int numNARs) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);
			generateHeader(fw, numJobs, numAssets, numNARs);
			generateJobs(fw, numJobs);
			generateAssets(fw, numAssets);
			generateNARs(fw, numNARs);
		} catch (IOException e) {
			System.out.println("caught exception in generating data" + e.getMessage());
		} finally {
			try {
				if (fw != null) fw.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	void generateHeader(FileWriter fw, int numJobs, int numAssets, int numNARs) throws IOException {
		String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	    String weekday[] = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday" };
	    String sep = " ";
	    String colon = ":";
	    String comma = ", ";

	    int conversionFactor = 1;                 // estimated time conversion factor
	    String validStatuses = "1;2";             // job statuses that can be rescheduled (non-sticky)
	    int kvalue = 50;                          // for interpolation for basic utility, range [0,100]
	    int maxPriority = 10;                     // maximum priority
	    String travelVector = "40;5;10;5;2";      // penalties for travel (per level)
	    int travelImportance = 40;                // for weighing travel penalties in utility, range [0,100]     
	    int travelFunction = 1;                   // 1  for greater-or-equal, 2 for less-or-equal,  3 for equal, 4 for existance (no level required)


	    Calendar calendar = Calendar.getInstance();

	    String header = "// Generated    " +  weekday[calendar.get(Calendar.DAY_OF_WEEK)-1] + sep + months[calendar.get(Calendar.MONTH)]  
	                                       +  sep + calendar.get(Calendar.DATE) + sep
                                           + calendar.get(Calendar.YEAR) + sep  +  calendar.get(Calendar.HOUR)  
                                           + colon + (calendar.get(Calendar.MINUTE) < 10 ? "0" : "") + calendar.get(Calendar.MINUTE);

	    // first the meta-data: top level locator, utility an today's date and time
	    fw.write("// (" + numJobs + " jobs, " + numAssets + " assets) test (synthetic data)" + "\n");
	    fw.write(header + "\n" + "\n");
	    fw.write("// meta-data:" + "\n");
	    fw.write(beginTime + comma + endTime + comma + numJobs + "jobs" + numAssets + "assets" + comma + conversionFactor + comma
	    		+ validStatuses + comma + kvalue + comma + maxPriority + comma + travelVector + comma + travelImportance + comma
	    		+ travelFunction + "\n");
	}
	
	void generateJobs(FileWriter fw, int numJobs)  throws IOException {
	    String comma = ", ";
        int maxProficiencyValue = 5;
        int numARs;
        int managedAssetEndTime = 0;
        int profClass;
        int assetClassID;
        int maID;
//      int partialShift = 3 * endTime / 4;
        
        // Part 1:  Define value generators
        
        // generator for managed assets - number of managed assets based on number of jobs
        int numManagedAsset = numJobs/4;
        int managedAssetEndTimes[] = new int[numManagedAsset];     
        int managedAssetIds[] = new int[numManagedAsset];     
        float managedAssetProb[] = new float[numManagedAsset];     

        // data values and their frequencies are defined by ValueGenerator and StringValueGenerator objects.  These data following here could be in e.g., java property files, XML, the database.

        for (int i = 0; i <  numManagedAsset; i++) {
            managedAssetEndTimes[i] = endTime;
            managedAssetIds[i] = i+1;
            managedAssetProb[i] = 1F/numManagedAsset;
        }
        ValueGenerator maintainedAssetId = new ValueGenerator(managedAssetIds, managedAssetProb);

        // value generators for job locator, status, estimated time, priority, organization, number of job asset resources, asset classes required

        String locators[] = {"bldg1.fl1.rm1", "bldg1.fl1.rm2", "bldg1.fl1.rm3", "bldg1.fl1.rm4", "bldg1.fl2.rm1", "bldg1.fl2.rm2", "bldg2.fl1.rm1", "bldg2.fl1.rm2", "bldg2.fl2.rm1", "bldg3.fl1.rm1"};
        float locatorProb[] =  {0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F};
        StringValueGenerator locator = new StringValueGenerator(locators, locatorProb);
 
        int jobStatusVals[] = {1,2,3,4};
        float jobStatusProb[] =  {0.2F,0.1F,0.3F,0.4F};
        ValueGenerator jobStatus = new ValueGenerator(jobStatusVals, jobStatusProb);

        int estTimes[] = {25,50,75,100,125,150,200,250,300,400};
        float estTimeProb[] =  {0.07F,0.20F,0.15F,0.2F,0.05F, 0.1F, 0.1F, 0.1F,0.02F,0.01F};
        ValueGenerator estimatedTime = new ValueGenerator(estTimes, estTimeProb);

        int priorities[] = {1,2,3,4,5,6,7,8,9,10};
        float priorityProb[] =  {0.3F,0.3F,0.2F,0.05F,0.05F, 0F, 0F, 0F, 0F, 0.1F};
        ValueGenerator priority = new ValueGenerator(priorities, priorityProb);

        int orgVals[] = {0,100,200,300};
        float orgProb[] =  {0.85F,0.05F,0.05F,0.05F};
        ValueGenerator organization = new ValueGenerator(orgVals, orgProb);

        int numOfAR[] = {1,2,3};
        float numARProb[] =  {0.6F,0.35F,0.05F};
//        float numARProb[] =  {1.0F,0.F,0.F};   // VALUES TO COMPARE TO VERSION 3.5
        ValueGenerator numAR = new ValueGenerator(numOfAR,  numARProb);

        int classIDs[] = {1000,2000,3000};
        float classIDProb[] =  {0.6F,0.3F,0.1F};
        ValueGenerator classID = new ValueGenerator(classIDs,  classIDProb);
        
        // define proficiency class value generator for each asset class id
        int pClass1IDs[] = {1000,2000,3000};
        float pClass1IDProb[] =  {0.4F,0.3F,0.3F};
        ValueGenerator pClass1ID = new ValueGenerator(pClass1IDs,  pClass1IDProb);

        int pClass2IDs[] = {4000,5000,6000};
        float pClass2IDProb[] =  {0.45F,0.45F,0.1F};
        ValueGenerator pClass2ID = new ValueGenerator(pClass2IDs,  pClass2IDProb);

        int pClass101IDs[] = {10,20,30};
        float pClass101IDProb[] =  {0.45F,0.45F,0.1F};
        ValueGenerator pClass101ID = new ValueGenerator(pClass101IDs,  pClass101IDProb);

        int pClass102IDs[] = {101,102};
        float pClass102IDProb[] =  {0.85F,0.15F};
        ValueGenerator pClass102ID = new ValueGenerator(pClass102IDs,  pClass102IDProb);

        // put together the value generators for each proficiency class id 
        ClassValueArrayGenerator jobProfClassValues = new ClassValueArrayGenerator(); 
        
         jobProfClassValues.addValueToArrayForClass(1000,  pClass2ID, 1);        
         jobProfClassValues.addValueToArrayForClass(1000,  pClass1ID, 0);        
        
         jobProfClassValues.addValueToArrayForClass(2000,  pClass101ID, 2);        
         jobProfClassValues.addValueToArrayForClass(2000,  pClass2ID, 1);        
         jobProfClassValues.addValueToArrayForClass(2000,  pClass1ID, 0);        
        
         jobProfClassValues.addValueToArrayForClass(3000,  pClass102ID, 1);        
         jobProfClassValues.addValueToArrayForClass(3000,  pClass101ID, 0);        

         // proficiency level is defined for each proficiency class
         ClassValueGenerator jobProficiencyLevel = new ClassValueGenerator();

         int  class10Vals[] = {1,2};
         float  class10Prob[] =  {0.7F, 0.3F};
         ValueGenerator class10Proficiency = new ValueGenerator(class10Vals,  class10Prob);        
         jobProficiencyLevel.addValuesForClass(10,  class10Proficiency);

         int  class20Vals[] = {1,2};
         float  class20Prob[] =  {0.8F, 0.2F};
         ValueGenerator class20Proficiency = new ValueGenerator(class20Vals,  class20Prob);        
         jobProficiencyLevel.addValuesForClass(20,  class20Proficiency);

         int  class30Vals[] = {1,2};
         float  class30Prob[] =  {0.9F, 0.1F};
         ValueGenerator class30Proficiency= new ValueGenerator(class30Vals,  class30Prob);        
         jobProficiencyLevel.addValuesForClass(30,  class30Proficiency);

         int  class101Vals[] = {1,2,3};
         float  class101Prob[] =  {0.7F, 0.2F, 0.1F};
         ValueGenerator class101Proficiency = new ValueGenerator(class101Vals,  class101Prob);        
         jobProficiencyLevel.addValuesForClass(101,  class101Proficiency);

         int  class102Vals[] = {1,2,3};
         float  class102Prob[] =  {0.9F, 0.08F, 0.02F};
         ValueGenerator class102Proficiency = new ValueGenerator(class102Vals,  class102Prob);        
         jobProficiencyLevel.addValuesForClass(102,  class102Proficiency);

         int  class1000Vals[] = {1,2,3,4,5};
         float  class1000Prob[] =  {0.3F, 0.3F, 0.2F, 0.1F,0.1F};
         ValueGenerator class1000Proficiency = new ValueGenerator(class1000Vals,  class1000Prob);        
         jobProficiencyLevel.addValuesForClass(1000,  class1000Proficiency);

         int  class2000Vals[] = {1,2,3,4,5};
         float  class2000Prob[] =  {0.3F, 0.3F, 0.2F, 0.1F,0.1F};
         ValueGenerator class2000Proficiency = new ValueGenerator(class2000Vals,  class2000Prob);        
         jobProficiencyLevel.addValuesForClass(2000,  class2000Proficiency);

         int  class3000Vals[] = {1,2};
         float  class3000Prob[] =  {0.5F, 0.5F};
         ValueGenerator class3000Proficiency = new ValueGenerator(class3000Vals,  class3000Prob);        
         jobProficiencyLevel.addValuesForClass(3000,  class3000Proficiency);

         int  class4000Vals[] = {1,2,3,4,5};
         float  class4000Prob[] =  {0.3F, 0.3F, 0.2F, 0.1F,0.1F};
         ValueGenerator class4000Proficiency = new ValueGenerator(class4000Vals,  class4000Prob);        
         jobProficiencyLevel.addValuesForClass(4000,  class4000Proficiency);

         int  class5000Vals[] = {1,2,3,4,5};
         float  class5000Prob[] =  {0.3F, 0.F, 0.2F, 0.1F,0.1F};
         ValueGenerator class5000Proficiency = new ValueGenerator(class5000Vals,  class5000Prob);        
         jobProficiencyLevel.addValuesForClass(5000,  class5000Proficiency);

         int  class6000Vals[] = {1,2,3,4,5};
         float  class6000Prob[] =  {0.3F, 0.3F, 0.2F, 0.1F,0.1F};
         ValueGenerator class6000Proficiency = new ValueGenerator(class6000Vals,  class6000Prob);        
         jobProficiencyLevel.addValuesForClass(6000,  class6000Proficiency);

         // proficiency importance
         int profImpVals[] = {60,80,100};
         float profImpProb[] =  {0.2F, 0.2F, 0.6F};
         ValueGenerator proficiencyImportance = new ValueGenerator(profImpVals, profImpProb);

        // Part 2:  Output the values to the file
        
		fw.write("\n" + "// Number of Jobs" + "\n");
		fw.write(numJobs+ "\n");
		
		for (int i = 0; i < numJobs; i++) { 
			if (i > 0) fw.write("\n");
			fw.write("\n" + "// Job " + (i+1) + "\n");
			fw.write((jobId++) + comma);                                                     // job id
			maID = maintainedAssetId.generateValue();
            for (int k = 0; k < managedAssetIds.length; k++) {
                if (maID == managedAssetIds[k]) {
                    managedAssetEndTime = managedAssetEndTimes[k];
                } 
            }            
	        fw.write(maID + comma);                                                           // managed asset id  
	        fw.write(locator.generateValue() + comma);                                        // job locator
	        fw.write(jobStatus.generateValue() + comma);                                      // job status
	        fw.write(estimatedTime.generateValue() + comma + 0 + comma);                          // estimated time, recorded time
	        fw.write(priority.generateValue() + comma);    									  // priority                                
	        fw.write(organization.generateValue() + comma + 0 + comma + 0 + comma + 1 + comma);  // organization, start time, end time, # maintainabilities
	        numARs = numAR.generateValue();
	        fw.write(numARs + comma + 0 + comma + beginTime + comma + endTime + "\n");        // #asset resources, # job NARs, earliest start, latest end
	        fw.write(0 + comma + managedAssetEndTime);									      // the maintainability
	        
	        for (int j = 0; j < numARs; j++) {
	        	fw.write("\n" + (jobId*1000 - 1000 +  jobARId++) + comma + 0 + comma + 0 + comma); // job asset resource id, asset id requested, asset id assigned  
	            assetClassID = classID.generateValue();                                            
	            fw.write(assetClassID + comma + 0 + comma);                                  // asset class id required, # asset resource attributes                                                             // number of job asset resource attributes (zero for this version)
	            int sz = jobProfClassValues.getSize(assetClassID);
//	            int sz = 1;  // FOR VERSION 3.5
	            fw.write(sz + "\n");                                                         // number of job asset resource proficiencies            
                for (int k = 0; k < sz; k++) {
                     profClass = jobProfClassValues.generateValueFromArrayForClass(assetClassID, k);
                     fw.write(profClass + comma + jobProficiencyLevel.generateValueForClass(profClass) + comma); // asset proficiency class, asset proficiency level required                  
                     fw.write(proficiencyImportance.generateValue() + comma + 1 + comma);   // asset proficiency importance, function
                     fw.write(maxProficiencyValue + "\n");                                   // max asset proficiency level    
                }
	        }
		}
	}
	
	void generateAssets(FileWriter fw, int numAssets)  throws IOException {
        String comma = ", ";
        int travelTrack = 0;
        int utilityCount = 0;
        int profClass;
        
        // Part 1:  Define value generators        

        // value generator for class id, organization

        int trackTravel[] = {1,1,0,0};
//        int trackTravel[] = {1,1,1,1};  // VERSION 3.5
        int countInUtility[] = {1,1,0,0};
//        int countInUtility[] = {1,1,1,1}; // VERSION 3.5
        int classIDs[] = {1000,2000,3000,4000};
        float classIDProb[] =  {0.7F,0.1F,0.1F, 0.1F};
        ValueGenerator classID = new ValueGenerator(classIDs,  classIDProb);

        int orgVals[] = {100,200,300, 400};
        float orgProb[] = {0.35F,0.15F,0.25F,0.25F};
        ValueGenerator organization = new ValueGenerator(orgVals,  orgProb);
         
         // define proficiency class value generator for each asset class id

         int pClass1IDs[] = {1000,2000,3000};
         float pClass1IDProb[] =  {0.4F,0.3F,0.3F};
         ValueGenerator pClass1ID = new ValueGenerator(pClass1IDs,  pClass1IDProb);

         int pClass2IDs[] = {4000,5000};
         float pClass2IDProb[] =  {0.5F,0.5F};
         ValueGenerator pClass2ID = new ValueGenerator(pClass2IDs,  pClass2IDProb);

         int pClass3IDs[] = {6000,6000};  // same as each other for now
         float pClass3IDProb[] =  {0.5F,0.5F};
         ValueGenerator pClass3ID = new ValueGenerator(pClass3IDs, pClass3IDProb);

         int pClass101IDs[] = {10,20,30};
         float pClass101IDProb[] =  {0.45F,0.45F,0.1F};
         ValueGenerator pClass101ID = new ValueGenerator(pClass101IDs,  pClass101IDProb);

         int pClass102IDs[] = {101,102};
         float pClass102IDProb[] =  {0.85F,0.15F};
         ValueGenerator pClass102ID = new ValueGenerator(pClass102IDs,  pClass102IDProb);

         // put together the value generators for each proficiency class id 
         ClassValueArrayGenerator assetProfClassValues = new ClassValueArrayGenerator();

          assetProfClassValues.addValueToArrayForClass(1000,  pClass2ID, 1);        
          assetProfClassValues.addValueToArrayForClass(1000,  pClass1ID, 0);        
         
          assetProfClassValues.addValueToArrayForClass(2000,  pClass3ID, 2);        
          assetProfClassValues.addValueToArrayForClass(2000,  pClass2ID, 1);        
          assetProfClassValues.addValueToArrayForClass(2000,  pClass1ID, 0);        
         
          assetProfClassValues.addValueToArrayForClass(3000,  pClass102ID, 1);        
          assetProfClassValues.addValueToArrayForClass(3000,  pClass101ID, 0);        
         
          assetProfClassValues.addValueToArrayForClass(4000,  pClass102ID, 1);        
          assetProfClassValues.addValueToArrayForClass(4000,  pClass101ID, 0); 
          
          // proficiency level is defined for each proficiency class
          ClassValueGenerator assetProficiencyLevel = new ClassValueGenerator();

          int  class10Vals[] = {1,2};
          float  class10Prob[] =  {0.7F, 0.3F};
          ValueGenerator class10Proficiency = new ValueGenerator(class10Vals,  class10Prob);        
          assetProficiencyLevel.addValuesForClass(10,  class10Proficiency);

          int  class20Vals[] = {1,2};
          float  class20Prob[] =  {0.8F, 0.2F};
          ValueGenerator class20Proficiency = new ValueGenerator(class20Vals,  class20Prob);        
          assetProficiencyLevel.addValuesForClass(20,  class20Proficiency);

          int  class30Vals[] = {1,2};
          float  class30Prob[] =  {0.9F, 0.1F};
          ValueGenerator class30Proficiency = new ValueGenerator(class30Vals,  class30Prob);        
          assetProficiencyLevel.addValuesForClass(30,  class30Proficiency);

          int  class101Vals[] = {1,2,3};
          float class101Prob[] = {0.7F, 0.2F, 0.1F};
          ValueGenerator class101Proficiency = new ValueGenerator(class101Vals,  class101Prob);        
          assetProficiencyLevel.addValuesForClass(101,  class101Proficiency);

          int  class102Vals[] = {1,2,3};
          float  class102Prob[] =  {0.9F, 0.08F, 0.02F};
          ValueGenerator class102Proficiency = new ValueGenerator(class102Vals,  class102Prob);        
          assetProficiencyLevel.addValuesForClass(102,  class102Proficiency);

          int  class1000Vals[] = {1,2,3,4,5};
          float  class1000Prob[] =  {0.3F, 0.3F, 0.2F, 0.1F, 0.1F};
          ValueGenerator class1000Proficiency = new ValueGenerator(class1000Vals,  class1000Prob);        
          assetProficiencyLevel.addValuesForClass(1000,  class1000Proficiency);

          int  class2000Vals[] = {1,2,3,4,5};
          float  class2000Prob[] =  {0.3F, 0.3F, 0.2F, 0.1F, 0.1F};
          ValueGenerator class2000Proficiency = new ValueGenerator(class2000Vals,  class2000Prob);        
          assetProficiencyLevel.addValuesForClass(2000,  class2000Proficiency);

          int  class3000Vals[] = {1,2};
          float  class3000Prob[] =  {0.5F, 0.5F};
          ValueGenerator class3000Proficiency = new ValueGenerator(class3000Vals,  class3000Prob);        
          assetProficiencyLevel.addValuesForClass(3000,  class3000Proficiency);

          int  class4000Vals[] = {1,2,3,4,5};
          float  class4000Prob[] =  {0.3F, 0.3F, 0.2F, 0.1F, 0.1F};
          ValueGenerator class4000Proficiency = new ValueGenerator(class4000Vals,  class4000Prob);        
          assetProficiencyLevel.addValuesForClass(4000,  class4000Proficiency);

          int  class5000Vals[] = {1,2,3,4,5};
          float  class5000Prob[] =  {0.3F, 0.3F, 0.2F, 0.1F, 0.1F};
          ValueGenerator class5000Proficiency = new ValueGenerator(class5000Vals,  class5000Prob);        
          assetProficiencyLevel.addValuesForClass(5000,  class5000Proficiency);

          int  class6000Vals[] = {1,2,3,4,5};
          float  class6000Prob[] =  {0.3F, 0.3F, 0.2F, 0.1F, 0.1F};
          ValueGenerator class6000Proficiency = new ValueGenerator(class6000Vals,  class6000Prob);        
          assetProficiencyLevel.addValuesForClass(6000,  class6000Proficiency);       

          // Value generator for asset Mainetnance Time
          int assetMaintVals[] = {800,500};
          float assetMaintProb[] =  {0.9F,0.1F};
          ValueGenerator assetMaintTime = new ValueGenerator(assetMaintVals,  assetMaintProb);

         // Part 2:  Output the values to the file
 		fw.write("\n" + "// Number of Assets" + "\n");
		fw.write(numAssets+ "\n");		         

		for (int m = 0; m < numAssets; m++) { 
			if (m > 0) fw.write("\n");
			fw.write("\n" + "// Asset " + (m+1) + "\n");
            int theClass = classID.generateValue();
			fw.write((assetId++) + comma + theClass + comma + organization.generateValue() + comma);  // asset id, class id, organization
            for (int k = 0; k < classIDs.length; k++) {
                if (theClass == classIDs[k]) {
                    travelTrack = trackTravel[k];
                    utilityCount = countInUtility[k];
                } 
           } 
            fw.write(travelTrack + comma + utilityCount + comma + 0 + comma);                        // track travel, count utility, 0 asset attributes
          fw.write(assetProfClassValues.getSize(theClass) + comma + 1 + "\n");                     // number of asset resource proficiencies
//            fw.write(1 + comma + 1 + "\n");                     //VERSION 3.5
          for (int j = 0; j < assetProfClassValues.getSize(theClass); j++) {
//            for (int j = 0; j < 1; j++) {
                profClass = assetProfClassValues.generateValueFromArrayForClass(theClass, j);
                fw.write(profClass + comma);                                                          // asset proficiency class                    
                fw.write(assetProficiencyLevel.generateValueForClass(profClass)+ "\n");             // asset proficiency level required
           }
           fw.write(0 + comma + assetMaintTime.generateValue());                                      // asset maintainability start. end            
		}
	}
	
	void generateNARs(FileWriter fw, int numNARs)  throws IOException {
		fw.write("\n" + "\n" + "// Number of NARs" + "\n");
		fw.write(numNARs+ "\n");
		// TBD
	}
}
