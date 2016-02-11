package com.mlink.iwm.scheduler.testSupport;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.mlink.iwm.scheduler.model.AssetTimeShare;
import com.mlink.iwm.scheduler.model.JobNAR;
import com.mlink.iwm.scheduler.model.NonAssetResource;
import com.mlink.iwm.scheduler.model.ProcessingError;
import com.mlink.iwm.scheduler.model.SchedulingOutputData;

class SODFileOutputGenerator {
	
	// main routine for nominal file output
	void processOutputFile(SchedulingOutputData sod, String filnam)   throws IOException {
		String delim = " ";	
		FileWriter fw = new FileWriter(filnam);
		
		// Output some control information
		
		Date date = new Date();
		fw.write("\n" + "//Tested On " + date.toString() +"\n");
	
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
		fw.write("\n" + "//  Error Severity   Text   ID" + "\n");
		fw.write(sod.getProcessingErrors().size() + "\n");
		for (ProcessingError pe: sod.getProcessingErrors()) {
			fw.write(pe.getSeverity() + delim + pe.getMessage() + delim + pe.getValue()  + "\n");	
		}
		fw.close();
	}
	
	
	
	void processFatalOutputFile(SchedulingOutputData sod, String filnam) throws IOException {
		String delim = " ";	
		Date date = new Date();
		System.out.println("FATAL ERROR: see output file");
		
		String outFileName = filnam + ".txt";
		FileWriter fw = new FileWriter(outFileName);
		fw.write("\n" + "//Tested On " + date.toString() +"\n");
		fw.write("\n" + "//FATAL INPUT ERRORS" +"\n");
		
		fw.write("\n" +"0" + "\n"); // jobs
		fw.write("\n" +"0" + "\n"); // assets
		fw.write("\n" +"0" + "\n"); // NARs

		// output the Errors
		fw.write("\n" + "//  Error Severity   Text           ID" + "\n");
		fw.write(sod.getProcessingErrors().size() + "\n");
		for (ProcessingError pe: sod.getProcessingErrors()) {
			fw.write(pe.getSeverity() + delim + pe.getMessage() + delim + (pe.getValue() != 0 ? pe.getValue() : "")  + "\n");	
		}
		fw.close();
	}


}
