package com.mlink.iwm.dpr;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.mlink.iwm.dpr.model.*;

public class DprParser {	
	private String file="./src/org/mlink/dpr/DPR_1031.txt";
	private ArrayList<Job> jobs;
	private Job job;
	private String start;
	private String shopsection;
	private String subshop;
	
	public ArrayList<Job> parse() throws FileNotFoundException, IOException {
		jobs = new ArrayList<Job>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s;
		while ((s = br.readLine()) != null) {
			if (s.length()==0) { 
				continue;
			}
			start = s.substring(0,DprFields.START).trim();
			if (start.equals(DprFields.ZERO)){ // Start of ERO or header row
				String ero = s.substring(DprFields.START,DprFields.ERO).trim(); 
				if (ero.length()==5) { // start of ERO
					job = new Job();
					job.setShopsection(shopsection);
					job.setSubshop(subshop);
					job.setEro(ero);
					job.setTam(s.substring(DprFields.ERO,DprFields.TAM).trim());
					job.setTamId(s.substring(DprFields.TAM,DprFields.TAM_ID).trim());
					job.setSerial(s.substring(DprFields.TAM_ID,DprFields.SERIAL).trim());
					job.setCat(s.substring(DprFields.SERIAL,DprFields.CAT).trim());
					job.setRdd(s.substring(DprFields.CAT,DprFields.RDD).trim());
					job.setPri(s.substring(DprFields.RDD,DprFields.PRI).trim());
					job.setNsninmaint(s.substring(DprFields.PRI,DprFields.NSN_IN_MAINT).trim());
					job.setNomen(s.substring(DprFields.NSN_IN_MAINT,DprFields.NOMEN).trim());
					job.setDcd(s.substring(DprFields.NOMEN,DprFields.DCD).trim());
					job.setDris(s.substring(DprFields.DCD,DprFields.DRIS).trim());
					job.setBdi(s.substring(DprFields.DRIS,DprFields.BDI).trim());
					job.setEot(s.substring(DprFields.BDI,DprFields.EOT).trim());
					jobs.add(job);
				} // else is a header row - ignore
			} else { // is start of job status, continuation of job status, parts, new shop, or page change
				String awtgstat = s.substring(0,DprFields.AWTG_STAT).trim();
				if (awtgstat.length()>0) { // is start of job status line with awaiting status
					job.setAwtgstat(awtgstat);
				}
				String owner = s.substring(DprFields.AWTG_STAT,DprFields.OWNER).trim();
				if (owner.length()>0) { // is start of job status line, post awaiting status 
					job.setOwner(owner);
					// process rest of line before status
					job.setPartscharge(s.substring(DprFields.JOB_STATUS,DprFields.PARTS_CHARGE).trim());
					job.setEch(s.substring(DprFields.PARTS_CHARGE,DprFields.ECH).trim());
					job.setQty(s.substring(DprFields.ECH,DprFields.QTY).trim());
					job.setXeros(s.substring(DprFields.QTY,DprFields.X_EROS).trim());
					job.setMaresdate(s.substring(DprFields.X_EROS,DprFields.MARES_DATE).trim());
					job.setDefect(s.substring(DprFields.MARES_DATE,DprFields.DEFECT).trim());
					job.setDdl(s.substring(DprFields.DEFECT,DprFields.DDL).trim());
					job.setDis(s.substring(DprFields.DDL,DprFields.DIS).trim());
					job.setDestholder(s.substring(DprFields.DIS,DprFields.DEST_HOLDER).trim());
				}
				String statusdate = s.substring(DprFields.OWNER,DprFields.JOB_STATUS_DATE).trim();
				if (statusdate.length()==4) { // continuation of job status lines
					String jobstatus = s.substring(DprFields.JOB_STATUS_DATE+1,DprFields.JOB_STATUS).trim();
					JobStatus js = new JobStatus();
					js.setJobstatus(jobstatus);
					js.setJobstatusdate(statusdate);
					job.addJobStatus(js);
				} else { // is parts, new shop, or page change
					String document = s.substring(DprFields.RCVD,DprFields.DOCUMENT).trim();
					if (document.length()==15) { // start of parts row
						NonAssetResource part = new NonAssetResource();
						part.setDocumentnum(document);
						part.setUi(s.substring(DprFields.DOCUMENT,DprFields.U_I).trim());
						part.setQty(s.substring(DprFields.U_I,DprFields.QTY).trim());
						part.setPri(s.substring(DprFields.QTY,DprFields.PRI).trim());
						part.setPartnsn(s.substring(DprFields.PRI,DprFields.PART_NSN).trim());
						part.setPartname(s.substring(DprFields.PART_NSN,DprFields.PART_NAME).trim());
						part.setStat(s.substring(DprFields.PART_NAME,DprFields.STAT).trim());
						part.setDate(s.substring(DprFields.STAT,DprFields.DATE).trim());
						part.setDicexc(s.substring(DprFields.DATE,DprFields.DIC_EXC).trim());
						part.setNmcs(s.substring(DprFields.DIC_EXC,DprFields.NMCS).trim());
						part.setLkh(s.substring(DprFields.NMCS,DprFields.LKH).trim());
						part.setAdv(s.substring(DprFields.LKH,DprFields.ADV).trim());
						job.addPart(part);
					} else { // is new shop or page change
						shopsection = s.substring(0,DprFields.SHOP_SECTION).trim();
						if (shopsection.contains("SHOP SECTION")) {
							subshop = s.substring(DprFields.SHOP_SECTION,DprFields.SUB_SHOP);
							// dispose of next 3 header lines
							br.readLine();
							br.readLine();
							br.readLine();
						} // else is page change
					}
				}
			}
		} // end while
		return jobs;
	}

	public static void main(String[] args) {
		DprParser dpr = new DprParser();
		try {
			ArrayList<Job> jobs = dpr.parse();
			System.out.println("Number of Jobs: "+ jobs.size());			
			//Create SQL for populating mysql
			printSQL(jobs);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printSQL(ArrayList<Job> jobs2) {
		// Spits out likely invalid sql.
		//for each job
		for (Integer i=0; i<jobs2.size(); i++){
			//create a record for the job
			Job job = jobs2.get(i);
			
			//Make v4 entities and store them
			
			//

			//Create records for parts
			//for each part on this job
			for (Integer p=0; p<job.getParts().size(); p++){
				NonAssetResource part = job.getParts().get(p);
				//make v4 entities for each part on this job and store
				
			}
			
			//Create records for Statuses
			//for each status on this job
			for (Integer s=0; s<job.getJobstatus().size(); s++){
				JobStatus status = job.getJobstatus().get(s);
				//unique id
				String uid = i.toString() + s.toString();
				System.out.println(" "); //newline
				System.out.println("INSERT INTO jobstatus (job_id, id, jobstatus, jobstatusdate)");
				System.out.println(" VALUES (\"" + i + "\",\"" + uid + "\",\""
						+ status.getJobstatus() + "\",\""
						+ status.getJobstatusdate() + "\") on duplicate key update id = \"" + uid + "\" ;");
			}
		}
		
	}
}
