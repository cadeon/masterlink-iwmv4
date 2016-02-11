package com.mlink.iwm.scheduler;

import java.util.ArrayList;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;

import com.mlink.iwm.scheduler.model.Asset;
import com.mlink.iwm.scheduler.model.AssetAvailability;
import com.mlink.iwm.scheduler.model.AssetResource;
import com.mlink.iwm.scheduler.model.Job;
import com.mlink.iwm.scheduler.model.MetaData;
import com.mlink.iwm.scheduler.model.SchedulingInputData;
import com.mlink.iwm.scheduler.model.SchedulingOutputData;
import com.mlink.iwm.scheduler.testSupport.SchedulerProbabilisticDataGenerator;

import junit.framework.TestCase;

//import org.junit.*;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;

public class SchedulerTestCase extends TestCase {
	SchedulerProbabilisticDataGenerator dg = null;
	SchedulerController sc = null;
	InputProcessor ip = null;

	@Before
	public void setUp() throws IOException {
		sc = new SchedulerController();
		dg = new SchedulerProbabilisticDataGenerator();
		ip = new InputProcessor();
	}

	@After
	public void tearDown() throws IOException {
	}
	
	/*
	PART 1: HARD CONSTRAINT TESTS

	The following tests use DataSet1:
	-- 1 job, 1 asset resource has requested id and assigned id null and requires 2 proficiencies and 1 attribute, 1 job NAR; 
	-- 1 asset, with classification id equal to the job asset's, proficiencies and attributes exactly matching, counts in utility;
	-- 1 NAR with same NSN and type and quantity exactly equal to the job's needs.
	-- Exact matches required on classification id, proficiencies, attributes between Asset and corresponding AssetResource.
	-- Time on asset = time on managed asset = job estimated time = period End - period Start 		 

	 * Hard Constraint Tests - Happy Path Cases
	 */

	// Happy path tests to make sure the file used for hard constraints tests is processed nominally, from the file and from objects	
	public void testHCOKInputHasNoProcessingErrorsFromFile() throws IOException {
		SchedulingOutputData sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt", 100);
		Assert.assertEquals("HC Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
	}

	public void testHCOKInputHasNoProcessingErrorsFromSID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("HC Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
	}

	public void test2JobOKInputHasNoProcessingErrorsFromFile() throws IOException { 
		SchedulingOutputData sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1a.txt", 100);
		Assert.assertEquals("2J1A Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
	}

	public void test2JobOKInputHasNoProcessingErrorsFromSID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1a.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("2J1A Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testHCRaisedConversionFactorDoesSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getMetaData().setConversionFactor(2L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Smaller scheduling when esitmated time conversion factor raise", sod.getAssetTimeShares().size(), 1);
	}

	
	public void testHCSamOrganizationDoesSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		long assetOrg = sid.getAssets().get(0).getOrganizationId();
		sid.getJobs().get(0).setOrganizationId(assetOrg);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Scheduling when Organization matches", sod.getAssetTimeShares().size(), 1);
	}
	
	public void testHCSameRequireedIDDoesSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Long assetId = sid.getAssets().get(0).getId();
		sid.getJobs().get(0).getAssetResources().get(0).setRequiredAssetId(assetId);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Scheduling when Required ID matches", sod.getAssetTimeShares().size(), 1);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
}
	
	public void testHCSameAssignedIDDoesSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Long assetId = sid.getAssets().get(0).getId();
		sid.getJobs().get(0).getAssetResources().get(0).setAssignedAssetId(assetId);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Scheduling when Assigned ID matches", sod.getAssetTimeShares().size(), 1);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testHCSameRequiredAndAssignedIDDoesSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Long assetId = sid.getAssets().get(0).getId();
		sid.getJobs().get(0).getAssetResources().get(0).setRequiredAssetId(assetId);
		sid.getJobs().get(0).getAssetResources().get(0).setAssignedAssetId(assetId);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Scheduling when Required and Assigned ID matches", sod.getAssetTimeShares().size(), 1);
	}
	
	public void testHCHigherAssetProficiencyLevelDoesSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetResourceLevel = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getLevel(); 
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(assetResourceLevel+1);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Scheduling when Asset Proficiency Level Too Higf", sod.getAssetTimeShares().size(), 1);
	}
	
	public void testHCAssetProficiencyLevelDoesScheduleForFunction2() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetResourceLevel = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(assetResourceLevel-1);
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setFunction(2);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Scheduling when Asset Proficiency Level Too Low for Function 2", sod.getAssetTimeShares().size(), 1);
	}

	public void testHCEqualAssetProficiencyLevelsDoScheduleForFunction3() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetResourceLevel = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(assetResourceLevel);
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setFunction(3);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Scheduling when Asset Proficiency Level Equal for Function 3", sod.getAssetTimeShares().size(), 1);
	}

	public void testHCAnyAssetProficiencyLevelsDoesScheduleForFunction4() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(0);
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setFunction(4);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Scheduling when Asset Proficiency Level Exists for Functin 4", sod.getAssetTimeShares().size(), 1);
	}
	
	public void testHCHigherAssetAttributeLevelDoesSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetResourceLevel = sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).getValue(); 
		sid.getAssets().get(0).getAssetAttributes().get(0).setValue(assetResourceLevel+1);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Scheduling when Asset Attribute Level Too High", sod.getAssetTimeShares().size(), 1);
	}
	
	public void testHCAssetAttributeLevelDoesScheduleForFunction2() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetResourceLevel = sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).getValue(); 
		sid.getAssets().get(0).getAssetAttributes().get(0).setValue(assetResourceLevel-1);
		sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).setFunction(2);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Scheduling when Asset Attribute Level Too Low for Function 2", sod.getAssetTimeShares().size(), 1);
	}

	public void testHCEqualAssetAttributeLevelsDoScheduleForFunction3() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetResourceLevel = sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).getValue(); 
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(assetResourceLevel);
		sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).setFunction(3);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Scheduling when Asset Attribute Level Equal for Function 3", sod.getAssetTimeShares().size(), 1);
	}

	public void testHCAnyAssetAttributeLevelsDoesScheduleForFunction4() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getAssets().get(0).getAssetAttributes().get(0).setValue(0);
		sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).setFunction(4);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Scheduling when Asset Attribute Level Exists for Function 4", sod.getAssetTimeShares().size(), 1);
	}

	/*
	 * Hard Constraint Tests - Cases that fail hard constraints
	 */

	public void testHCLowerMaangedAssetTimeDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getJobs().get(0).getMaintainabilities().get(0).setEndDateTime(50L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when managed asset time is too low", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}

	public void testHCAssetCannnotFillTwoAssetResourcesForSameJobDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a2ar.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when esitmated time is too high", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}	
	public void testHCRaisedJobEstimatedTimeDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getJobs().get(0).setEstimatedTime(10000);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when esitmated time is too high", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}	
	
	public void testHCRaisedConversionFactorDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getMetaData().setConversionFactor(20L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("No scheduling when esitmated time is too high", sod.getAssetTimeShares().size(), 0);
	}
	
	public void testHCLaterPeriodStartTimeDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getMetaData().setPeriodStart(sid.getMetaData().getPeriodEnd() -5);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when  period start time is too late", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testHCEarlierPeriodEndTimeDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getMetaData().setPeriodEnd(sid.getMetaData().getPeriodStart() +5);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when  period end time is too early", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testHCLowerNARLevelDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getNARs().get(0).setQuantityOnHand(2);
		sid.getNARs().get(1).setQuantityOnHand(2);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when NAR quantity is too low", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testHCRaiseJobNARLevelDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getJobs().get(0).getNonAssetResources().get(0).setQuantityOnHand(30);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when job NAR quantity is too high", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testHCDiffernetOrganizationDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getJobs().get(0).setOrganizationId(500L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Organization does not match", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testHCDiffernetRequestedIDDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setRequiredAssetId(99L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Required ID does not exist", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("Error on unknown asset ID good input", sod.getProcessingErrors().size(), 1);
	}
	
	public void testHCLowerAssetAvailabilityDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getAssets().get(0).getAvailabilities().get(0).setEndDateTime(25L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Asset Availability too small", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testHCDifferentClassificationIDsDoNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getAssets().get(0).setClassificationId(9999L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Classification ID does not match", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}		
	
	public void testHCDifferentPersistenceClassificationIDsDoNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getAssets().get(0).getProficiencies().get(0).setClassificationId(9999L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Proficiency Classification ID does not match", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}	
		
	public void testHCDifferentPersistenceClassificationIDsDoNotScheduleForFunction2() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setFunction(2);
		sid.getAssets().get(0).getProficiencies().get(0).setClassificationId(9999L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Proficiency Classification ID does not match for function 2", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}	
		
	public void testHCDifferentPersistenceClassificationIDsDoNotScheduleForFunction3() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setFunction(3);
		sid.getAssets().get(0).getProficiencies().get(0).setClassificationId(9999L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Proficiency Classification ID does not match for function 3", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}	
	
	public void testHCSamePersistenceClassificationIDsDoesScheduleForFunction4() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setFunction(4);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Does scheduling when Proficiency Classification ID for function 4", sod.getAssetTimeShares().size(), 1);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}	

	public void testHCDifferentPersistenceClassificationIDsDoNotScheduleForFunction4() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setFunction(4);
		sid.getAssets().get(0).getProficiencies().get(0).setClassificationId(9999L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Proficiency Classification ID does not match  for function 4", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}	
	
	public void testHCTravelTimeDoesNotSchedule() throws IOException { 
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1a.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when travel time does not fit", sod.getAssetTimeShares().size(), 1);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testHCHiAssetProficiencyLevelDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetResourceLevel = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getLevel(); 
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(assetResourceLevel-1);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Asset Proficiency Level Too Low", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testHCLowerAssetAttributeLevelDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetResourceLevel = sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).getValue();
		sid.getAssets().get(0).getAssetAttributes().get(0).setValue(assetResourceLevel-1);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Asset Attribute Level Too Low", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}	
	public void testHCHigherAssetResourceProficiencyLevelDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetLevel = sid.getAssets().get(0).getProficiencies().get(0).getLevel(); 
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setLevel(assetLevel + 1);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Asset Respirce Proficiency Level Too High", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testHCHigherAssetResourceAttributeLevelDoesNotSchedule() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetLevel = sid.getAssets().get(0).getAssetAttributes().get(0).getValue();
		sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).setValue(assetLevel + 1);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Asset Attribute Level Too Low", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
		
	public void testHCAssetProficiencyLevelDoesNotScheduleForFunction2() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetResourceLevel = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(assetResourceLevel+1);
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setFunction(2);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Asset Proficiency Level Too High for Function 2", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}

	public void testHCEqualAssetProficiencyLevelsDoNotScheduleForFunction3() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetResourceLevel = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(assetResourceLevel+1);
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setFunction(3);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Asset Proficiency Level Not-Equal for Function 3", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testHCAssetAttributeLevelDoesNotScheduleForFunction2() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetResourceLevel = sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).getValue(); 
		sid.getAssets().get(0).getAssetAttributes().get(0).setValue(assetResourceLevel+1);
		sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).setFunction(2);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Asset Attribute Level Too High for Function 2", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}

	public void testHCEqualAssetAttributeLevelsDoNotScheduleForFunction3() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		int assetResourceLevel = sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).getValue(); 
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(assetResourceLevel-1);
		sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).setFunction(3);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No scheduling when Asset Attribute Level Not Equal for Function 3", sod.getAssetTimeShares().size(), 0);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}

	/*
	 * PART 2: VALIDATE SCHEDULE FUNCTION TESTS
	 */

	// Happy path test of ValidateSchedulingData
	public void testOKInputValidateSchedulingData() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		SchedulingOutputData sod = sc.validateSchedulingData(sid);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}

	/*
 * PART 3: VALIDATION TESTS
 */
	
	// Happy path tests to make sure the file used for validation tests is processed nominally, from the file and from objects
	public void testOKInputHasNoProcessingErrorsFromFile() throws IOException {
		SchedulingOutputData sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt", 100);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}

	public void testOKInputHasNoProcessingErrorsFromSID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void test2Job2AssetOKInputHasNoProcessingErrorsFromFile() throws IOException { 
		SchedulingOutputData sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/SchedulerTests/TestData2j2a.txt", 100);
		Assert.assertEquals("2J2A Happy Path:  Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
	}

	public void test2Job2AssetOKInputHasNoProcessingErrorsFromSID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j2a.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("2J2A Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
	}

	// 1. MetaData Validation Tests
	//  A. Null Tests
	
	public void testNoMetaData() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.setMetaData(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for no MetaData", 21, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 10001 for no MetaData ", 10001, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testNoMetaDataValidteSchedulingData() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.setMetaData(null);
		SchedulingOutputData sod = sc.validateSchedulingData(sid);
		Assert.assertEquals("One error for no MetaData", 21, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 10001 for no MetaData ", 10001, sod.getProcessingErrors().get(0).getType());
	}

	public void testNullPeriodStart() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setPeriodStart(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null period start", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 1001 for null period start", 1001, sod.getProcessingErrors().get(0).getType());
	}

	public void testNullPeriodEnd() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setPeriodEnd(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null period end", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 1002 for null period end", 1002, sod.getProcessingErrors().get(0).getType());
	}

	public void testNullTopLevelLocatorID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setTopLevelLocatorId(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null top level locator id", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 1003 for null top level locator id", 1003, sod.getProcessingErrors().get(0).getType());
	}

	public void testNullConversionFactor() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setConversionFactor(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Conversion Factor", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 1004 for null Conversion Factor", 1004, sod.getProcessingErrors().get(0).getType());
	}

	public void testNullStatusVector() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setStatusVector(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Status Vector", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 1005 for null Status Vector", 1005, sod.getProcessingErrors().get(0).getType());
	}

	public void testNullKValue() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setKValue(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null K value", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 1006 for null kvalue", 1006, sod.getProcessingErrors().get(0).getType());
	}

	public void testNullMaxPriority() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setMaxPriority(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Max Priority", 21, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 1007 for null Max Priority", 1007, sod.getProcessingErrors().get(0).getType());
	}

	public void testNullTravelVector() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setTravelVector(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Travel Vector", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 1008 for null Travel Vector", 1008, sod.getProcessingErrors().get(0).getType());
	}

	public void testNullTravelImportance() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setTravelImportance(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Travel Importance", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 1009 for null Travel Importance", 1009, sod.getProcessingErrors().get(0).getType());
	}

	public void testNullTravelFunction() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setTravelFunction(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Travel Function", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 1010 for null Travel Function", 1010, sod.getProcessingErrors().get(0).getType());
	}
	// 1. MetaData Validation Tests
	//  B.  Other Tests
	public void testEmptyMetaData() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.setMetaData(new MetaData());
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Ten errors for no MetaData", 30, sod.getProcessingErrors().size());
	}

	public void testPeriodStartAfterPeriodEnd() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setPeriodStart(sid.getMetaData().getPeriodEnd()+10);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for Period Start after Period End", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 1 for Period Start after Period End", 1, sod.getProcessingErrors().get(0).getType());
	}

	public void testPeriodStartNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setPeriodStart(-20L);
		sid.getMetaData().setPeriodEnd(10L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for Period Start Negative", 1, sod.getProcessingErrors().size());// fails prev start < prev end test as well
		Assert.assertEquals("Error type should be 12 for Negative Period Start", 12, sod.getProcessingErrors().get(0).getType());
	}

	public void testPeriodStartAndEndNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setPeriodStart(-20L);
		sid.getMetaData().setPeriodEnd(-10L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Two errors for Period Start and End Negative", 2, sod.getProcessingErrors().size());// fails prev start < prev end test as well
		Assert.assertEquals("Error type should be 12 for Negative Period Start and End", 12, sod.getProcessingErrors().get(0).getType());
	}

	public void testConversionFactorNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setConversionFactor(-10L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for Conversion Factor Negative", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 3 for Conversion Factor Negative", 3, sod.getProcessingErrors().get(0).getType());
	}

	public void testNegativeKValue() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setKValue(-3);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for negative K value", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 4 for inconsistant kvalue", 4, sod.getProcessingErrors().get(0).getType());
	}

	public void testLargeKValue() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setKValue(101);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for large K value", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 4 for inconsistant kvalue", 4, sod.getProcessingErrors().get(0).getType());
	}

	public void testNegativeMaxPriority() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setMaxPriority(-3);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for negative Max Priority", 21, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 5 for inconsistant Max Priority", 5, sod.getProcessingErrors().get(0).getType());
	}

	public void testIllegalStatusVectorComponent() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setStatusVector("10;k;5");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for negative Status Vector Component", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 10 for inconsistant Status Vector Component", 10, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testIllegalTravelVectorComponent() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setTravelVector("10;k;5");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for negative Status Vector Component", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 11 for inconsistant Travel Vector Component", 11, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testNegativeStatusVectorComponent() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setStatusVector("10;-3;5");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for negative Status Vector Component", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 9 for inconsistant Status Vector Component", 9, sod.getProcessingErrors().get(0).getType());
	}

	public void testNegativeTravelVectorComponent() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setTravelVector("10;-3;5");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for negative Travel Vector Component", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 6 for inconsistant Travel Vector Component", 6, sod.getProcessingErrors().get(0).getType());
	}
	//	Test to make sure validation does not quit when it catches an error
	public void testNegativeTravelAndStatusVectorandNullJobEarliestStartComponent() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setTravelVector("10;-3;5");
		sid.getMetaData().setStatusVector("10;-3;5");
		sid.getJobs().get(0).setEarliestStart(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Three errors for negative Travel Vector, Status Vector and null Job Earliest Start", 3, sod.getProcessingErrors().size());
	}

	public void testNegativeTravelImportance() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setTravelImportance(-3);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for negative Travel Importance", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 7 for inconsistant Travel Importance", 7, sod.getProcessingErrors().get(0).getType());
	}

	public void testLargeTravelImportance() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setTravelImportance(201);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for large TravelImportance", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 7 for inconsistant TravelImportance", 7, sod.getProcessingErrors().get(0).getType());
	}

	public void testNegativeTravelFunction() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setTravelFunction(-3);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for negative Travel Function", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 8 for inconsistant Travel Function", 8, sod.getProcessingErrors().get(0).getType());
	} 

	public void testPositiveTravelFunctionNot1() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getMetaData().setTravelFunction(2);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for positive Travel Function not equal to 1", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 8 for inconsistant Travel Function", 8, sod.getProcessingErrors().get(0).getType());
	} 
	
	// 2. Job Validation Tests
	//  A.Null Tests	
	public void testNullJobList() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.setJobs(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for no Jobs", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 10002 for null Jobs ", 10002, sod.getProcessingErrors().get(0).getType());
	}

	public void testEmptyJobList() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.setJobs(new ArrayList<Job>());
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for no Jobs", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 10002 for no Jobs ", 10002, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testNullJobID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setId(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Job ID", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2001 for null Job ID", 2001, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullManagedAssetID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setManagedAssetId(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Managed Asset ID", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2002 for null Managed Asset ID", 2002, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullLocator() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setLocator(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Locator", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2003 for null Locator", 2003, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullStatus() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setStatus(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Status", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2004 for null Status", 2004, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullEstimatedTime() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setEstimatedTime(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Estimated Time", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2005 for null Estimated Time", 2005, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullRecordedTime() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setRecordedTime(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Recorded Time", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2006 for null Recorded Time", 2006, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullPriority() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setPriority(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Priority", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2007 for null Priority", 2007, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullStartDateTime() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setStartDateTime(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Start Date Time", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2008 for null Start Date Time", 2008, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullEndDateTimeNotAnError() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setEndDateTime(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No error for null End Date Time", 0, sod.getProcessingErrors().size());
	}	
	
	public void testNullEarliestStart() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setEarliestStart(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Earliest Start", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2010 for null Earliest Start", 2010, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullLatestEnd() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setLatestEnd(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Latest End", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2011 for null Latest End", 2011, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullMaintainabilities() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setMaintainabilities(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Maintainabilities", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 101 for null Maintainabilities", 101, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullMaintainabilityStartDateTime() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getMaintainabilities().get(0).setStartDateTime(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Maintainability Start Date Time", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2012 for null Maintainability Start Date Time", 2012, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullMaintainabilityEndDateTime() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getMaintainabilities().get(0).setEndDateTime(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Maintainability End Date Time", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2013 for null Maintainability End Date Time", 2013, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetResources() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setAssetResources(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Resources", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 110 for null Asset Resources", 110, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetResourceID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setId(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Resource ID", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2014 for null Asset Resource ID", 2014, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetResourceClassificationID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setClassificationId(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Resource Classification ID", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2015 for null Asset Resource Classification ID", 2015, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetResourceJob() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setJob(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Resource Job", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2016 for null Asset Resource Job", 2016, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetResourceProficiencyClassificationID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setClassificationId(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset ResourceProficiency Classification ID", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2018 for null Asset Resource Proficiency Classification ID", 2018, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetResourceProficiencyLevel() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setLevel(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Resource Proficiency Level", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2019 for null Asset Resource Proficiency Level", 2019, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetResourceProficiencyImportance() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setImportance(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset ResourceProficiency Importance", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2020 for null Asset Resource Proficiency Importance", 2020, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetResourceProficiencyFunction() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setFunction(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset ResourceProficiency Function", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2021 for null Asset Resource Proficiency Function", 2021, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetResourceProficiencyMaxValue() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setMaxValue(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset ResourceProficiency Max Value", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2022 for null Asset Resource Proficiency Max Value", 2022, sod.getProcessingErrors().get(0).getType());
	}			
	
	public void testNullAssetResourceAttributeId() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).setId(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Resource Attribute Id", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2023 for null Asset Resource Attribute Id", 2023, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetResourceAttributeValue() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).setValue(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Resource Attribute Value", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2024 for null Asset Resource Attribute Value", 2024, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetResourceAttributeFunction() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).setFunction(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Resource Attribute Function", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2025 for null Asset Resource Attribute Function", 2025, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullJobNonAssetResourceNSN() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getNonAssetResources().get(0).setStockNumber(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Job Non Asset Resource Stock Number", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2031 for null Job Non Asset Resource Stock Number", 2031, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullJobNonAssetResourceQty() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getNonAssetResources().get(0).setQuantityOnHand(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Job Non Asset Resource Qty", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2032 for null Job Non Asset Resource Qty", 2032, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullJobNonAssetResourceType() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getNonAssetResources().get(0).setType(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Job Non Asset Resource Type", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2033 for null Job Non Asset Resource Type", 2033, sod.getProcessingErrors().get(0).getType());
	}	
	
	// 2. Job Validation Tests
	//  B.Other Tests	

	public void testDuplicateJobIDs() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j2a.txt");
		sid.getJobs().get(0).setId(sid.getJobs().get(1).getId());
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for duplicate Job IDs", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 4001 for duplicate Job IDs", 4001, sod.getProcessingErrors().get(0).getType());
	}

	public void testDuplicateJobAssetResourceIDs() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j2a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setId(sid.getJobs().get(0).getAssetResources().get(1).getId());
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for duplicate Job IDs", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 4002 for duplicate Job IDs", 4002, sod.getProcessingErrors().get(0).getType());
	}

	public void testDuplicateJobAssetResourceAssignedAssetIDs() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a2ar.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setAssignedAssetId(1L);
		sid.getJobs().get(0).getAssetResources().get(1).setAssignedAssetId(1L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for duplicate Assigned Asset IDs", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2028 for duplicate Assigned Asset IDs", 2028, sod.getProcessingErrors().get(0).getType());
	}

	public void testDuplicateJobAssetResourceRequiredAssetIDs() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a2ar.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setRequiredAssetId(1L);
		sid.getJobs().get(0).getAssetResources().get(1).setRequiredAssetId(1L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for duplicate Required Asset IDs", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2029 for duplicate Required Asset IDs", 2029, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testNoMaintainabilities() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setMaintainabilities(new ArrayList<AssetAvailability>());
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Maintainabilities", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 101 for null Maintainabilities", 101, sod.getProcessingErrors().get(0).getType());
	}	

	public void testMaintainabilityStartAfteroMaintainabilityEnd() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		AssetAvailability aa = sid.getJobs().get(0).getMaintainabilities().get(0);
		aa.setStartDateTime(aa.getEndDateTime()+10);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for Maintainability Start after Maintainability End", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 102 for Maintainability Start after Maintainability End", 102, sod.getProcessingErrors().get(0).getType());
	}

	public void testMaintainabilityStartNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getMaintainabilities().get(0).setStartDateTime(-20L);
		sid.getJobs().get(0).getMaintainabilities().get(0).setEndDateTime(10L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for  Negative Maintainability Start", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 103 for Negative Maintainability End", 103, sod.getProcessingErrors().get(0).getType());
	}

	public void testMaintainabilityStartAndEndNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getMaintainabilities().get(0).setStartDateTime(-20L);
		sid.getJobs().get(0).getMaintainabilities().get(0).setEndDateTime(-10L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Two errors for  Negative Maintainability Start and End", 2, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 103 for Negative Maintainability Start and End", 103, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testNegativeEstimatedTime() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setEstimatedTime(-10);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for negative Estimated Time", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 104 for negative Estimated Time", 104, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNegativeRecordedTime() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setRecordedTime(-10);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for negative Recorded Time", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 105 for negative Recorded Time", 105, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNegativePriority() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setPriority(-10);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for negative Priority", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 106 for negative Priority", 106, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testPiorityHigherThanMaxPriority() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getJobs().get(0).setPriority(99);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Errors on max priority", sod.getProcessingErrors().size(), 1);
	}
	
	public void testEarliestStartAfteroLatestEnd() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setEarliestStart(sid.getJobs().get(0).getLatestEnd()+10);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for Earliest Start after Latest End", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 107 for Earliest Start after Latest End", 107, sod.getProcessingErrors().get(0).getType());
	}

	public void testEarliestStartNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setEarliestStart(-20L);
		sid.getJobs().get(0).setLatestEnd(10L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for  Negative Earliest Start", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 108 for Negative Earliest Start", 108, sod.getProcessingErrors().get(0).getType());
	}	

	public void testEarliestStartAndLatestEndNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setEarliestStart(-20L);
		sid.getJobs().get(0).setLatestEnd(-10L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Twp errors for  Negative Earliest Start and Latest End", 2, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 108 for Negative Earliest Start and  Latest End", 108, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNoAssetResources() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).setAssetResources(new ArrayList<AssetResource>());
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Resources", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 110 for null Asset Resources", 110, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testUnknownAssetResourceRequiredId() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setRequiredAssetId(9999L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for unknown Job  Asset Resource Required Asset ID", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2027 for unknown Job Asset Resource Required Asset ID", 2027, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testUnknownAssetResourceAssignedId() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setAssignedAssetId(9999L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for unknown Job  Asset Resource Assigned Asset ID", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2026 for unknown Job Asset Resource Assigned Asset ID", 2026, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testAssetResourceAssignedNotEqualRequired() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setAssignedAssetId(1L);
		sid.getJobs().get(0).getAssetResources().get(0).setRequiredAssetId(2L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for  Job  Asset Resource Assigned Asset ID not equal Required Asset ID", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2017 for Job Asset Resource Assigned Asset ID not equal to Required Asset ID", 2017, sod.getProcessingErrors().get(0).getType());
	}	

	public void testAssetResourceProficiencyLevelNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setLevel(-20);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for  Negative Proficiency Level", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 111 for Negative Proficiency Level", 111, sod.getProcessingErrors().get(0).getType());
	}

	public void testAssetResourceProficiencytMaxValueNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setMaxValue(-20);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Six errors for  Negative Proficiency Max Value", 6, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 112 for Negative Proficiency Max Value", 112, sod.getProcessingErrors().get(0).getType());
	}

	public void testAssetResourceProficiencyImportanceNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setImportance(-20);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for  Negative Proficiency Importance", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 113 for Negative Proficiency Importance", 113, sod.getProcessingErrors().get(0).getType());
	}

	public void testAssetResourceProficiencyFunctionlNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setFunction(-20);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for  Negative Proficiency Function", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 114 for Negative Proficiency Function", 114, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testDuplicateJobAssetProficiencyClassificationID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a2ar.txt");
		Long clId = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getClassificationId();
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(1).setClassificationId(clId);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Two errors for duplicate Job Asset Proficiency Classification IDs", 2, sod.getProcessingErrors().size());
	}		
	
	public void testConsistentMaxValueForProficiencyClassificationID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j1aProficiency.txt");
		Long clId = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getClassificationId();
		Integer maxLvl = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getMaxValue();
		sid.getJobs().get(1).getAssetResources().get(0).getProficiencies().get(0).setClassificationId(clId);
		sid.getJobs().get(1).getAssetResources().get(0).getProficiencies().get(0).setMaxValue(maxLvl);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors for inconsistent Job Asset Proficiency Maximum Levels", 0, sod.getProcessingErrors().size());
	}		
	
	public void testInconsistentMaxValueForProficiencyClassificationID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j1aProficiency.txt");
		Long clId = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getClassificationId();
		Integer maxLvl = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getMaxValue();
		sid.getJobs().get(1).getAssetResources().get(0).getProficiencies().get(0).setClassificationId(clId);
		sid.getJobs().get(1).getAssetResources().get(0).getProficiencies().get(0).setMaxValue(maxLvl+1);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for inconsistent Job Asset Proficiency Maximum Levels", 1, sod.getProcessingErrors().size());
	}		

	public void testAssetResourceAttributeFunctionlNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getAssetAttributes().get(0).setFunction(-20);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for  Negative Attribute Function", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 115 for Negative Attribute Function", 115, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testUnknownJobNonAssetResourceNSN() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getJobs().get(0).getNonAssetResources().get(0).setStockNumber(9999L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for unknown Job Non Asset Resource Stock Number", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 2034 for unknown Job Non Asset Resource Stock Number", 2034, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testStickyNoSchedulingWith2AssetResourcesSticky() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j2a2ar.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors for sticky 2 asset resource jobs", 0, sod.getProcessingErrors().size());
		Assert.assertEquals("Job is not scheduled when sticky", sod.getAssetTimeShares().size(), 0);
	}
	
	public void testStickyNoSchedulingWith2AssetResourcesNotSticky() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j2a2ar.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setRequiredAssetId(0L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors for non sticky 2 asset resource jobs", 0, sod.getProcessingErrors().size());
		Assert.assertEquals("Job is  scheduled when not sticky", sod.getAssetTimeShares().size(), 2);
	}	
	
	public void testStickySchedulingWith3JobsMetaDataPermits() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j2aSimpleSticky.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors for non sticky (metadata permits) 3 jobs", 0, sod.getProcessingErrors().size());
		Assert.assertEquals("Job 2 and 3 are scheduled when 3 jobs non sticky (metadata permits)", sod.getAssetTimeShares().size(), 2); 
	}
	
	public void testStickySchedulingWith3JobsNotSticky() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j2aSimpleSticky.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setRequiredAssetId(0L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors for non sticky 3 jobs", 0, sod.getProcessingErrors().size());
		Assert.assertEquals("Jobs 2 and 3 are scheduled when 3 jobs not sticky", sod.getAssetTimeShares().size(), 2);
	}
		
	public void testStickySchedulingWith3JobsSticky() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j2aSimpleSticky.txt");
		sid.getMetaData().setStatusVector("1");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors for non sticky 3 jobs", 0, sod.getProcessingErrors().size());
		Assert.assertEquals("Jobs 1 and 3 are scheduled when 3 jobs not sticky", sod.getAssetTimeShares().size(), 3);
	}
	
	public void testStickySchedulingWith3JobsNotStickyLowAvailability() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j2aSimpleSticky.txt");
		sid.getMetaData().setStatusVector("1");
		sid.getAssets().get(1).getAvailabilities().get(0).setEndDateTime(200L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors for non sticky 3 jobs", 0, sod.getProcessingErrors().size());
		Assert.assertEquals("Only one job is scheduled when 3 jobs sticky limited asset 2 time", sod.getAssetTimeShares().size(), 1);
		Assert.assertTrue("Only job 3 is scheduled when 3 jobs sticky limited asset 2 time", sod.getAssetTimeShares().get(0).getResourceID().equals(1004L));
	}	
	
	public void testStickyNoSchedulingWith2JobsMetaDataPermits() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j2a3arSimpleSticky.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors for sticky 2 jobs", 0, sod.getProcessingErrors().size());
		Assert.assertEquals("Job 2 is only job scheduled when 2 jobs not sticky (metadata premits) ", sod.getAssetTimeShares().size(), 1); 
	}
	
	public void testStickyNoSchedulingWith2JobsNotSticky() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j2a3arSimpleSticky.txt");
		sid.getJobs().get(0).getAssetResources().get(0).setRequiredAssetId(0L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors for non sticky 2 jobs", 0, sod.getProcessingErrors().size());
		Assert.assertEquals("Job 2 is scheduled when 2 jobs not sticky", sod.getAssetTimeShares().size(), 1);
	}
	
	public void testStickyNoSchedulingWith2JobsSticky() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j2a3arSimpleSticky.txt");
		sid.getMetaData().setStatusVector("1");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors for non sticky 2 jobs", 0, sod.getProcessingErrors().size());
		Assert.assertEquals("Job 1 is scheduled when 2 jobs  sticky", sod.getAssetTimeShares().size(), 2);
	}	
	
	// 3. Asset Validation Tests
	//  A.Null Tests
	public void testNullAssetList() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.setAssets(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for no Assets", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 10003 for null Assets ", 10003, sod.getProcessingErrors().get(0).getType());
	}

	public void testEmptyAssetList() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.setAssets(new ArrayList<Asset>());
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for no Assets", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 10003 for no Assets ", 10003, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testNullAssetID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).setId(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset ID", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 3001 for null Asset ID", 3001, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testNullAssetClassificationId() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).setClassificationId(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Classification ID", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 3002 for null Asset Classification ID", 3002, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testNullAssetOrganizationId() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).setOrganizationId(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Organization", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 3004 for null Asset Organization", 3004, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testNullAssetTrackTravel() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).setTrackTravel(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Track Travel", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 3005 for null Asset TrackTravel", 3005, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testNullAssetCalculateUtility() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).setCalculateUtility(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Calculate Utility", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 3006 for null Asset Calculate Utility", 3006, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testNullAvailabilities() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).setAvailabilities(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Availabilities", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 201 for null Availabilities", 201, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAvailabilityStartDateTime() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).getAvailabilities().get(0).setStartDateTime(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Availability Start Date Time", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 3007 for null Availability Start Date Time", 3007, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAvailabilityEndDateTime() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).getAvailabilities().get(0).setEndDateTime(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Availability End Date Time", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 3008 for null Availability End Date Time", 3008, sod.getProcessingErrors().get(0).getType());
	}	

	public void testNullAssetProficiencyClassificationID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).getProficiencies().get(0).setClassificationId(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Proficiency Classification ID", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 3010 for null Asset Proficiency Classification ID", 3010, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetProficiencyLevel() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Proficiency Level", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 3011 for null Asset  Proficiency Level", 3011, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullAssetAttributeId() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).getAssetAttributes().get(0).setId(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Attribute Id", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 3012 for null Asset  Attribute Id", 3012, sod.getProcessingErrors().get(0).getType());
	}	

	public void testNullAssetAttributeValue() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).getAssetAttributes().get(0).setValue(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null Asset Attribute Value", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 3014 for null Asset Attribute Value", 3014, sod.getProcessingErrors().get(0).getType());
	}					
	// 3. Asset Validation Tests 
	//  B.Other Tests
	
	public void testDuplicateAssetIDs() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j2a.txt");
		sid.getAssets().get(0).setId(sid.getAssets().get(1).getId());
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for duplicate Asset IDs", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 4003 for duplicate Asset IDs", 4003, sod.getProcessingErrors().get(0).getType());
	}

	public void testNoAvailabilities() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).setAvailabilities(new ArrayList<AssetAvailability>());
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for no Availabilities", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 201 for no Availabilities", 201, sod.getProcessingErrors().get(0).getType());
	}	

	public void testAvailabilityStartAfteroMaintainabilityEnd() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		AssetAvailability aa = sid.getAssets().get(0).getAvailabilities().get(0);
		aa.setStartDateTime(aa.getEndDateTime()+10);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for Availability Start after Availability End", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 202 for Availability Start after Availability End", 202, sod.getProcessingErrors().get(0).getType());
	}

	public void testAvailabilityStartNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).getAvailabilities().get(0).setStartDateTime(-20L); // if not done then #ProcessingErrrors = 2
		sid.getAssets().get(0).getAvailabilities().get(0).setEndDateTime(10L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for  Negative Availability Start", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 203 for Negative Availability Start", 203, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testAvailabilityStartAndEndNegative() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).getAvailabilities().get(0).setStartDateTime(-20L); // if not done then #ProcessingErrrors = 2
		sid.getAssets().get(0).getAvailabilities().get(0).setEndDateTime(-10L);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Two errors for  Negative Availability Start and End", 2, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 203 for Negative Availability Start and End", 203, sod.getProcessingErrors().get(0).getType());
	}
	
	public void testLevelConsistentWithMaxValueForProficiencyClassificationID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j1aProficiency.txt");
		Long clId = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getClassificationId();
		Integer maxLvl = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getMaxValue();
		sid.getAssets().get(0).getProficiencies().get(0).setClassificationId(clId);
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(maxLvl);
		sid.getJobs().get(1).getAssetResources().get(0).getProficiencies().get(0).setClassificationId(clId);
		sid.getJobs().get(1).getAssetResources().get(0).getProficiencies().get(0).setMaxValue(maxLvl);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors for Asset consistent Job Asset Proficiency Maximum Levels", 0, sod.getProcessingErrors().size());
	}		
	
	public void testLevelConsistentWithNoMaxValueForProficiencyClassificationID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j1aProficiency.txt");
		Long clId = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getClassificationId();
		Integer maxLvl = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getMaxValue();
		sid.getAssets().get(0).getProficiencies().get(0).setClassificationId(999999L);
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(maxLvl+21);
		sid.getJobs().get(1).getAssetResources().get(0).getProficiencies().get(0).setClassificationId(clId);
		sid.getJobs().get(1).getAssetResources().get(0).getProficiencies().get(0).setMaxValue(maxLvl);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors for Asset consistent Job Asset Proficiency Maximum Levels", 0, sod.getProcessingErrors().size());
	}		
	
	public void testLevelInconsistentMaxValueForProficiencyClassificationID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j1aProficiency.txt");
		Long clId = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getClassificationId();
		Integer maxLvl = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getMaxValue();
		sid.getAssets().get(0).getProficiencies().get(0).setClassificationId(clId);
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(maxLvl+1);
		sid.getJobs().get(1).getAssetResources().get(0).getProficiencies().get(0).setClassificationId(clId);
		sid.getJobs().get(1).getAssetResources().get(0).getProficiencies().get(0).setMaxValue(maxLvl);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for Asset inconsistent Job Asset Proficiency Maximum Levels", 1, sod.getProcessingErrors().size());
	}		
	
	public void testNegativeAssetProficiencyLevel() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(-1);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for negative Asset Proficiency Level", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 211 for negative Asset  Proficiency Level", 211, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testDuplicateAssetProficiencyClassificationID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a2ar.txt");
		Long clId = sid.getAssets().get(0).getProficiencies().get(0).getClassificationId();
		sid.getAssets().get(0).getProficiencies().get(1).setClassificationId(clId);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for duplicate Asset Proficiency Classification IDs", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 4021 for duplicate Asset  Proficiency Classification IDs", 4021, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void test1LocationTravelTimeHardConstraint() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aTravel.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Test Travel Time Hard Constraint 1 location: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling when travel time hard constraints ok", sod.getAssetTimeShares().size(), 2);
	}	

	public void test2LocationsBuidingsDifferTravelTimeHardConstraint() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aTravel.txt");
		sid.getJobs().get(0).setLocator("bldg2.fl1.rm1");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Test Travel Time Hard Constraint 2 locations buildings differ: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling when travel time hard constraints buildings differ not ok", sod.getAssetTimeShares().size(), 1);
	}	

	public void testCountTravelTimeHardConstraint() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aTravel.txt");
		sid.getJobs().get(0).setLocator("bldg2.fl1.rm1");
		sid.getAssets().get(0).setTrackTravel(0);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Test Count Travel Time in Hard Constraint 2 locations buildings differ: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling when travel time hard constraints buildings differ but not count travel ok", sod.getAssetTimeShares().size(), 2);
	}	

	public void test2LocationsFloorsDifferTravelTimeHardConstraint() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aTravel.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl2.rm1");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Test Travel Time Hard Constraint 2 locations floors differ: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling when travel time hard constraints floors differ not ok", sod.getAssetTimeShares().size(), 1);
	}	

	public void test2LocationsRoomsDifferTravelTimeHardConstraint() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aTravel.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Test Travel Time Hard Constraint 2 locations rooms differ: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling when travel time hard constraints rooms differ not ok", sod.getAssetTimeShares().size(), 1);
	}	
		
	// 4. NonAssetResource Validation Tests
	//  A.Null Tests

	
	public void testNullNARNsn() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getNARs().get(0).setStockNumber(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Two errors for null NAR Nsn", 2, sod.getProcessingErrors().size());
	}	
	
	public void testNullNARQuantity() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getNARs().get(0).setQuantityOnHand(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null NAR Qty", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 4002 for null NAR Qty", 4002, sod.getProcessingErrors().get(0).getType());
	}	
	
	public void testNullNARType() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getNARs().get(0).setType(null);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for null NAR Type", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 4003 for null NAR Type", 4003, sod.getProcessingErrors().get(0).getType());
	}	
	
	// 4. NonAssetResource Validation Tests
	//  B.Other Tests
	
	public void testDuplicateNonAssetResourceIDs() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j2a.txt");
		sid.getNARs().get(0).setStockNumber(sid.getNARs().get(1).getStockNumber());
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for duplicate NAR IDs", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 4004 for duplicate NAR IDs", 4004, sod.getProcessingErrors().get(0).getType());
	}

	public void testNegativeNARQuantity() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData20j5a.txt");
		sid.getNARs().get(0).setQuantityOnHand(-2);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("One error for Negative NAR Qty", 1, sod.getProcessingErrors().size());
		Assert.assertEquals("Error type should be 301 for Negative NAR Qty", 301, sod.getProcessingErrors().get(0).getType());
	}
	
	/*
	 * PART 3:  UTILITY TESTS
	 * 
	 * 3A:  Proficiency Utility Tests
	 */

	public void testUIncreasingAssetProficiencyLevelLowersUtilitySchedulesNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling base case", sod.getAssetTimeShares().size(), 1);
	}
	
	public void testUIncreasingAssetProficiencyLevelLowersUtilitySchedulesModifiedProficiencyLevel() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal baseUtility = sod.getUtility();
		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Integer oldLevel = sid.getAssets().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors on modified asset proficiency level input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling with higher asset proficiency level", sod.getAssetTimeShares().size(), 1);
		BigDecimal newUtility = sod.getUtility();
		assertTrue("Incrasing asset proficiency lowers utility", newUtility.compareTo(baseUtility) < 0);
	}
	
	public void testUIncreasingAssetProficiencyImportanceLowersUtilitySchedulesNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Integer oldLevel = sid.getAssets().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors on modified asset proficiency level input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling with higher asset proficiency level", sod.getAssetTimeShares().size(), 1);
	}
	
	public void testUIncreasingAssetProficiencyImportanceLowersUtilitySchedulesModifiedProficiencyImportance() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Integer oldLevel = sid.getAssets().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal newUtility = sod.getUtility();
		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Integer oldImportance = sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).getImportance();
		oldLevel = sid.getAssets().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setImportance(oldImportance+2);
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors on modified asset proficiency importance input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling with higher asset proficiency level and importance", sod.getAssetTimeShares().size(), 1);
		BigDecimal importanceUtility = sod.getUtility();
		assertTrue("Incrasing asset proficiency importance lowers utility more", importanceUtility.compareTo(newUtility) < 0);
	}		

	public void testUDecreasingAssetProficiencyLevelFunction2LowersUtilitySchedulesNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling base case", sod.getAssetTimeShares().size(), 1);
	}
	
	public void testUDecreasingAssetProficiencyLevelFunction2LowersUtilitySchedulesLevelAndFunctionSet() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal baseUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Integer oldLevel = sid.getAssets().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel-1);
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setFunction(2);
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors on modified asset proficiency level input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling with lower asset proficiency level and function 2", sod.getAssetTimeShares().size(), 1);
		BigDecimal newUtility = sod.getUtility();
		assertTrue("Decreasing asset proficiency for function 2 lowers utility", newUtility.compareTo(baseUtility) < 0);
	}
	
	public void testU4Job2AssetExactProficiencyLevelFunctionSchedules() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData4j2aExactProficiency.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("4j2a No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("4j2a schedules all 4 jobs", sod.getAssetTimeShares().size(), 4);
		BigDecimal one = new BigDecimal(1,MathContext.DECIMAL32);
		assertTrue("4j2a gives utility 1.0", sod.getUtility().compareTo(one)==0);
	}	
	
	public void testU4Job2AssetLowerProficiencyLevelFunctionSchedules() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData4j2aExactProficiency.txt");
		sid.getJobs().get(0).getAssetResources().get(0).getProficiencies().get(0).setLevel(2);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("4j2a Lower Proficiency No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("4j2a Lower Proficiency schedules all 4 jobs", sod.getAssetTimeShares().size(), 4);
		BigDecimal numer = new BigDecimal(995,MathContext.DECIMAL32);
		BigDecimal denom = new BigDecimal(1000,MathContext.DECIMAL32);
		BigDecimal comp = numer.divide(denom,MathContext.DECIMAL32);
		assertTrue("4j2a Lower Proficiency gives utility 0.995", sod.getUtility().compareTo(comp)==0);
	}
	
	public void testU4Job2AssetMaxPriorityExactProficiencyLevelFunctionSchedules() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData4j2aExactProficiency.txt");
		sid.getMetaData().setMaxPriority(10);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("4j2a No errors on max priority", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("4j2a schedules all 4 jobs max priority", sod.getAssetTimeShares().size(), 4);
		BigDecimal numer = new BigDecimal(75,MathContext.DECIMAL32);
		BigDecimal denom = new BigDecimal(100,MathContext.DECIMAL32);
		BigDecimal comp = numer.divide(denom,MathContext.DECIMAL32);
		assertTrue("4j2a gives utility 0.75 max priority", sod.getUtility().compareTo(comp)==0);
	}
	
	// 3 B. Job attribute utility tests
	
	public void testUIncreasingKValueLowersUtilitySchedulesK0() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Integer oldLevel = sid.getAssets().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		sid.getMetaData().setKValue(0);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with k value 0", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling with k value 0", sod.getAssetTimeShares().size(), 1);
	}
	
	public void testUIncreasingKValueLowersUtilitySchedulesK50() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Integer oldLevel = sid.getAssets().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		sid.getMetaData().setKValue(0);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal kminUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		sid.getMetaData().setKValue(50);
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with k value 50", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling with k value 50", sod.getAssetTimeShares().size(), 1);
		BigDecimal k50Utility = sod.getUtility();
		assertTrue("Incrasing kvalue lowers utility more",k50Utility.compareTo(kminUtility) < 0);
	}
	
	public void testUIncreasingKValueLowersUtilitySchedulesK100() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Integer oldLevel = sid.getAssets().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		sid.getMetaData().setKValue(50);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal k50Utility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		sid.getMetaData().setKValue(100);
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with k value 100", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling with k value 100", sod.getAssetTimeShares().size(), 1);
		BigDecimal k100Utility = sod.getUtility();
		assertTrue("Incrasing kvalue lowers utility more",k100Utility.compareTo(k50Utility) < 0);
	}	
	
	public void testUWithMaxPrioirityIncreasingKValueNoChangeToUtilitySchedulesK0() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Integer pri = sid.getJobs().get(0).getPriority();
		Integer oldLevel = sid.getAssets().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		sid.getMetaData().setKValue(0);
		sid.getMetaData().setMaxPriority(pri);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with k value 0 and max priority", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling with k value 0 and max priority", sod.getAssetTimeShares().size(), 1);
	}
	
	public void testUWithMaxPrioirityIncreasingKValueNoChangeToUtilitySchedulesK50() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Integer pri = sid.getJobs().get(0).getPriority();
		Integer oldLevel = sid.getAssets().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		sid.getMetaData().setKValue(0);
		sid.getMetaData().setMaxPriority(pri);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal kminUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		sid.getMetaData().setKValue(50);
		sid.getMetaData().setMaxPriority(pri);
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with k value 50 and max priority", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling with k value 50 and max priority", sod.getAssetTimeShares().size(), 1);
		BigDecimal k50Utility = sod.getUtility();
		assertEquals("Incrasing kvalue to 50 with max prioirity has no effect on utility",k50Utility.compareTo(kminUtility),0);
	}
	
	public void testUWithMaxPrioirityIncreasingKValueNoChangeToUtilitySchedulesK100() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		Integer pri = sid.getJobs().get(0).getPriority();
		Integer oldLevel = sid.getAssets().get(0).getProficiencies().get(0).getLevel();
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		sid.getMetaData().setKValue(50);
		sid.getMetaData().setMaxPriority(pri);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal k50Utility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt");
		sid.getAssets().get(0).getProficiencies().get(0).setLevel(oldLevel+1);
		sid.getMetaData().setKValue(100);
		sid.getMetaData().setMaxPriority(pri);
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with k value 100 and max priority", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling with k value 100 and max priority", sod.getAssetTimeShares().size(), 1);
		BigDecimal k100Utility = sod.getUtility();
		assertEquals("Incrasing kvalue to 100 with max prioirity has no effect on utility",k100Utility.compareTo(k50Utility), 0);
	}	

	public void test2J1ANoProficienciesHasNoProcessingErrorsFromSID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aNoProficiencies.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("HC Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	public void test2j2aSimpleHasNoProcessingErrorsFromSID2ARvs1AR() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j2aSimple.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("2j2aSimple Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("2j2aSimple Happy Path: 2 job assets scheduled", sod.getAssetTimeShares().size(), 2);
		assertTrue("Asset Resource 1003 is not scheduled in 1st time share", !sod.getAssetTimeShares().get(0).getResourceID().equals(1003L));
		assertTrue("Asset Resource 1003 is not scheduled in 2nd time share", !sod.getAssetTimeShares().get(1).getResourceID().equals(1003L));
		BigDecimal numer = new BigDecimal(75,MathContext.DECIMAL32);
		BigDecimal denom = new BigDecimal(100,MathContext.DECIMAL32);
		BigDecimal comp = numer.divide(denom,MathContext.DECIMAL32);
		assertEquals("Utility of 2j2aSimple base case is 0.75", sod.getUtility().compareTo(comp), 0);
	}
	
	public void test2j2aSimpleHasNoProcessingErrorsFromSIDDeltaMaxPriority() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j2aSimple.txt");
		sid.getMetaData().setMaxPriority(5);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("2j2aSimple Lower Max Priority: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("2j2aSimple Lower Max Priority: 2 job assets scheduled", sod.getAssetTimeShares().size(), 2);
		assertTrue("Asset Resource 1003 is not scheduled in 1st time share Lower Max Priority", !sod.getAssetTimeShares().get(0).getResourceID().equals(1003L));
		assertTrue("Asset Resource 1003 is not scheduled in 2nd time share Lower Max Priority", !sod.getAssetTimeShares().get(1).getResourceID().equals(1003L));
		assertEquals("Utility of 2j2aSimple Lower Max Priority is 1.0", sod.getUtility().compareTo(BigDecimal.ONE), 0);
	}

	public void test3j2aSimpleHasNoProcessingErrorsFromSIDNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j2aSimple.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("3j2aSimple Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("3j2aSimple Happy Path: 2 job assets scheduled", sod.getAssetTimeShares().size(), 2);
		assertTrue("3j2a Asset Resource 1003 is  scheduled in 1st time share", sod.getAssetTimeShares().get(0).getResourceID().equals(1003L));
		assertTrue("3j2a Asset Resource 1004 is  scheduled in 2nd time share", sod.getAssetTimeShares().get(1).getResourceID().equals(1004L));
		BigDecimal numer = new BigDecimal(75,MathContext.DECIMAL32);
		BigDecimal denom = new BigDecimal(100,MathContext.DECIMAL32);
		BigDecimal comp = numer.divide(denom,MathContext.DECIMAL32);
		assertEquals("Utility of 3j2aSimple base case is 0.75", sod.getUtility().compareTo(comp), 0);
	}
	
	public void test3j2aSimpleHasNoProcessingErrorsFromSIDLowerPriority() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j2aSimple.txt");
		sid.getJobs().get(0).setPriority(5);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("3j2aSimple Lower  Priority: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("3j2aSimple Lower  Priority: 2 job assets scheduled", sod.getAssetTimeShares().size(), 2);
		assertTrue("Asset Resource 1003 is not scheduled in 1st time share Lower  Priority", !sod.getAssetTimeShares().get(0).getResourceID().equals(1003L));
		assertTrue("Asset Resource 1003 is not scheduled in 2nd time share Lower  Priority", !sod.getAssetTimeShares().get(1).getResourceID().equals(1003L));
		assertTrue("Asset Resource 1004 is not scheduled in 1st time share Lower  Priority", !sod.getAssetTimeShares().get(0).getResourceID().equals(1004L));
		assertTrue("Asset Resource 1004 is not scheduled in 2nd time share Lower  Priority", !sod.getAssetTimeShares().get(1).getResourceID().equals(1004L));
		BigDecimal numer = new BigDecimal(75,MathContext.DECIMAL32);
		BigDecimal denom = new BigDecimal(100,MathContext.DECIMAL32);
		BigDecimal comp = numer.divide(denom,MathContext.DECIMAL32);
		assertEquals("Utility of 3j2aSimple Lower Priority is 0.75", sod.getUtility().compareTo(comp), 0);
	}

	public void test3j2aUtilityHasNoProcessingErrorsFromSIDNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j2aUtility.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("3j2aUtility Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("3j2aUtility Happy Path: 2 job assets scheduled", sod.getAssetTimeShares().size(), 2);
		assertTrue("3j2aUtility Asset Resource 1003 is  scheduled in 1st time share", sod.getAssetTimeShares().get(0).getResourceID().equals(1003L));
		assertTrue("3j2aUtility Asset Resource 1004 is  scheduled in 2nd time share", sod.getAssetTimeShares().get(1).getResourceID().equals(1004L));
		BigDecimal numer = new BigDecimal(725,MathContext.DECIMAL32);
		BigDecimal denom = new BigDecimal(1000,MathContext.DECIMAL32);
		BigDecimal comp = numer.divide(denom,MathContext.DECIMAL32);
		assertEquals("Utility of  3j2aUtility base case is 0.725", sod.getUtility().compareTo(comp), 0);
	}
	
	public void test3j2aUtilityHasNoProcessingErrorsFromSIDHigherPriority() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j2aUtility.txt");
		sid.getJobs().get(0).setPriority(5);
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("3j2aUtility Higher  Priority: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("3j2aUtility Higher  Priority: 2 job assets scheduled", sod.getAssetTimeShares().size(), 2);
		assertTrue("Asset Resource 1003 is not scheduled in 1st time share Higher  Priority", !sod.getAssetTimeShares().get(0).getResourceID().equals(1003L));
		assertTrue("Asset Resource 1003 is not scheduled in 2nd time share Higher  Priority", !sod.getAssetTimeShares().get(1).getResourceID().equals(1003L));
		assertTrue("Asset Resource 1004 is not scheduled in 1st time share Higher  Priority", !sod.getAssetTimeShares().get(0).getResourceID().equals(1004L));
		assertTrue("Asset Resource 1004 is not scheduled in 2nd time share Higher  Priority", !sod.getAssetTimeShares().get(1).getResourceID().equals(1004L));
		BigDecimal numer = new BigDecimal(75,MathContext.DECIMAL32);
		BigDecimal denom = new BigDecimal(100,MathContext.DECIMAL32);
		BigDecimal comp = numer.divide(denom,MathContext.DECIMAL32);
		assertEquals("Utility of 3j2aUtility Lower Priority is 0.75", sod.getUtility().compareTo(comp), 0);
	}
	
	// 3 C.  Travel time utility tests
	
	public void testULocatorDeltasDecreaseUtilityNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with identical locators", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with identical locators", sod.getAssetTimeShares().size(), 2);
	}
	
	public void testULocatorDeltasDecreaseUtilityRoomChange() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal travelMaxUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with different room locators", sod.getAssetTimeShares().size(), 2);
		BigDecimal roomPenaltyUtility = sod.getUtility();
		assertTrue("Room utility lower than utility for identical locators",travelMaxUtility.compareTo(roomPenaltyUtility)>0);
	}
	
	public void testULocatorDeltasDecreaseUtilityFloorChange() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal roomPenaltyUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl2.rm1");
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different floor locators", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with different floor locators", sod.getAssetTimeShares().size(), 2);
		BigDecimal floorPenaltyUtility = sod.getUtility();
		assertTrue("Floor utility lower than room utility",floorPenaltyUtility.compareTo(roomPenaltyUtility)<0);
	}
	
	public void testULocatorDeltasDecreaseUtilityFloorAndRoomChange() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl2.rm1");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal floorPenaltyUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl2.rm2");
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different floor and room locators", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with different floor and room locators", sod.getAssetTimeShares().size(), 2);
		BigDecimal floorRoomPenaltyUtility = sod.getUtility();
		assertTrue("Floor and room utility lower than floor utility",floorPenaltyUtility.compareTo(floorRoomPenaltyUtility)==0);
	}
	
	public void testUTravelVectorWithZerosOrBlankDoesNotChangeUtilityNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with nominal travel vector", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with nominal travel vector", sod.getAssetTimeShares().size(), 2);
	}
	
	public void testUTravelVectorWithZerosOrBlankDoesNotChangeUtilityZeroes() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal travelMaxUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		sid.getMetaData().setTravelVector("0;0;0;0");
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators and zero travel vector", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with different room locators and zero travel vector", sod.getAssetTimeShares().size(), 2);
		BigDecimal roomPenaltyUtility = sod.getUtility();
		assertTrue("Room utility same as utility for identical locators with zero travel vector",travelMaxUtility.compareTo(roomPenaltyUtility)==0);
	}
	
	public void testUTravelVectorWithZerosOrBlankDoesNotChangeUtilityBlank() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal travelMaxUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		sid.getMetaData().setTravelVector("");
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators and blank travel vector", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with different room locators and blank travel vector", sod.getAssetTimeShares().size(), 2);
		BigDecimal roomPenaltyUtility = sod.getUtility();
		assertTrue("Room utility same as  utility for identical locators with blank travel vector",travelMaxUtility.compareTo(roomPenaltyUtility)==0);
	}

	public void testUTravelPenaltiesDoNotApplyIfNotTrackingTravelAndDoesNotChangeUtilityNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with identical locators not tracking utility", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with identical locators not tracking utility", sod.getAssetTimeShares().size(), 2);
	}
	
	public void testUTravelPenaltiesDoNotApplyIfNotTrackingTravelAndDoesNotChangeUtilityNoTrackTravel() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal travelMaxUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		sid.getAssets().get(0).setTrackTravel(0);
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators and not tracking travel for asset", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with different room locators and not tracking travel for asset", sod.getAssetTimeShares().size(), 2);
		BigDecimal roomPenaltyUtility = sod.getUtility();
		assertTrue("Room utility same as utility for identical locators if not tracking travel",travelMaxUtility.compareTo(roomPenaltyUtility)==0);
	}

	public void testUTravelPenaltiesDoNotApplyIfZeroTravelImportanceDoesNotChangeUtilityNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with identical locators zero importance", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with identical locators zero importance", sod.getAssetTimeShares().size(), 2);
	}
	
	public void testUTravelPenaltiesDoNotApplyIfZeroTravelImportanceDoesNotChangeUtilityZeroTravelImportance() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with identical locators zero importance", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with identical locators zero importance", sod.getAssetTimeShares().size(), 2);
		BigDecimal travelMaxUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		sid.getMetaData().setTravelImportance(0);
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators and zero travel importance", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with different room locators and zero travel importance", sod.getAssetTimeShares().size(), 2);
		BigDecimal roomPenaltyUtility = sod.getUtility();
		assertTrue("Room utility same as utility for identical locators if zero travel importance",travelMaxUtility.compareTo(roomPenaltyUtility)==0);
	}
	
	public void testUHigherTravelVectorDecreasesMoreUtilityNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with identical locators and nominal vector", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with identical locators and nominal vector", sod.getAssetTimeShares().size(), 2);
	}
	
	public void testUHigherTravelVectorDecreasesMoreUtilityChangedLocator() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with identical locators and nominal vector", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with identical locators and nominal vector", sod.getAssetTimeShares().size(), 2);
		BigDecimal travelMaxUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators and nominal vector", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with different room locators and nominal vector", sod.getAssetTimeShares().size(), 2);
		BigDecimal roomPenaltyUtility = sod.getUtility();
		assertTrue("Room utility lower than utility for identical locators  and nominal vector",travelMaxUtility.compareTo(roomPenaltyUtility)>0);
	}
	
	public void testUHigherTravelVectorDecreasesMoreUtilityChangedLocatorAndTravelVector() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal roomPenaltyUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		sid.getMetaData().setTravelVector("44;18;15;10;4");
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators and big vector", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with different room locators and big vector", sod.getAssetTimeShares().size(), 2);
		BigDecimal roomPenaltyUtilityBigVector = sod.getUtility();
		assertTrue("Room utility with big vector lower than utility for identical locators  and nominal vector",roomPenaltyUtility.compareTo(roomPenaltyUtilityBigVector)>0);
	}
	
	public void testUHigherTravelImportanceDecreasesMoreUtilityNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with identical locators and nominal importance", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with identical locators and nominal importance", sod.getAssetTimeShares().size(), 2);
	}
	
	public void testUHigherTravelImportanceDecreasesMoreUtilityChangedLocator() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		BigDecimal travelMaxUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators and nominal importance", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with different room locators and nominal importance", sod.getAssetTimeShares().size(), 2);
		BigDecimal roomPenaltyUtility = sod.getUtility();
		assertTrue("Room utility lower than utility for identical locators  and nominal importance",travelMaxUtility.compareTo(roomPenaltyUtility)>0);
	}
	
	public void testUHigherTravelImportanceDecreasesMoreUtilityChangedLocatorAndTravelImportance() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators and nominal importance", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with different room locators and nominal importance", sod.getAssetTimeShares().size(), 2);
		BigDecimal roomPenaltyUtility = sod.getUtility();

		sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryLocator.txt");
		sid.getJobs().get(0).setLocator("bldg3.fl1.rm2");
		sid.getMetaData().setTravelImportance(80);
		sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators and big importance", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with different room locators and big importance", sod.getAssetTimeShares().size(), 2);
		BigDecimal roomPenaltyUtilityBigImportance = sod.getUtility();
		assertTrue("Room utility with big importance lower than utility for identical locators  and big importance",roomPenaltyUtility.compareTo(roomPenaltyUtilityBigImportance)>0);
	}
	
	public void testUTravelPenaltyTradessOffForEstimatedTimeUtilityNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j1aEstTime.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with identical locators measuring travel vs est time", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with identical locators measuring travel vs est time", sod.getAssetTimeShares().size(), 2);
		Assert.assertEquals("Scheduling two highest estimated time jobs(0)", sod.getAssetTimeShares().get(0).getResourceID().longValue(), 1001L);
		Assert.assertEquals("Scheduling two highest estimated time jobs(1)", sod.getAssetTimeShares().get(1).getResourceID().longValue(), 1002L);
	}
	
	public void testUTravelPenaltyTradessOffForEstimatedTimeUtilityChangedLocator() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j1aEstTime.txt");
		sid.getJobs().get(1).setLocator("bldg2.fl1.rm2");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators measuring travel vs est time", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling different jobs with different room locators measuring travel vs est time", sod.getAssetTimeShares().size(), 2);
		Assert.assertEquals("Scheduling travel based highest estimated time jobs(0)", sod.getAssetTimeShares().get(0).getResourceID().longValue(), 1001L);
		Assert.assertEquals("Scheduling travel based highest estimated time jobs(1)", sod.getAssetTimeShares().get(1).getResourceID().longValue(), 1003L);
	}
	
	public void testUTravelPenaltyTradessOffForPriorityUtilityNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j1aPriority.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with identical locators measuring travel vs Priority", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with identical locators measuring travel vs Priority", sod.getAssetTimeShares().size(), 2);
		Assert.assertEquals("Scheduling two highestPriority jobs(0)", sod.getAssetTimeShares().get(0).getResourceID().longValue(), 1001L);
		Assert.assertEquals("Scheduling two highest Priority jobs(1)", sod.getAssetTimeShares().get(1).getResourceID().longValue(), 1002L);
	}
	
	public void testUTravelPenaltyTradessOffForPriorityUtilityChangedLocator() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j1aPriority.txt");
		sid.getJobs().get(1).setLocator("bldg2.fl1.rm2");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators measuring travel vs Priority", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling different jobs with different room locators measuring travel vs Priority", sod.getAssetTimeShares().size(), 2);
		Assert.assertEquals("Scheduling travel based highest Priority jobs(0)", sod.getAssetTimeShares().get(0).getResourceID().longValue(), 1001L);
		Assert.assertEquals("Scheduling travel based highest Priority jobs(1)", sod.getAssetTimeShares().get(1).getResourceID().longValue(), 1003L);
	}
	
	public void testUTravelPenaltyTradessOffForProficiencyUtilityNominal() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j1aProficiency.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with identical locators measuring travel vs Proficiency", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling both jobs with identical locators measuring travel vs Proficiency", sod.getAssetTimeShares().size(), 2);
		Assert.assertEquals("Scheduling two highestPriority jobs(0)", sod.getAssetTimeShares().get(0).getResourceID().longValue(), 1001L);
		Assert.assertEquals("Scheduling two highest Priority jobs(1)", sod.getAssetTimeShares().get(1).getResourceID().longValue(), 1002L);
	}
	
	public void testUTravelPenaltyTradessOffForProficiencyUtilityChangeLocator() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData3j1aProficiency.txt");
		sid.getJobs().get(1).setLocator("bldg3.fl1.rm2");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("No errors with different room locators measuring travel vs Proficiency", sod.getProcessingErrors().size(), 0);
		Assert.assertEquals("Scheduling different jobs with different room locators measuring travel vs Proficiency", sod.getAssetTimeShares().size(), 2);
		Assert.assertEquals("Scheduling travel based highest Proficiency jobs(0)", sod.getAssetTimeShares().get(0).getResourceID().longValue(), 1001L);
		Assert.assertEquals("Scheduling travel based highest Proficiency jobs(1)", sod.getAssetTimeShares().get(1).getResourceID().longValue(), 1003L);
	}
	
	// 3 D.  Sticky Tests (effects on utility)
	
	public void test2J1AVaryPriorityStickyDoesNotChangeAssignment() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryPriority.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Sticky jobs: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertTrue(" One job assigned (sticky Priority) ", sod.getAssetTimeShares().size() > 0);
		Assert.assertTrue("Sticky jobs (Priority) should not move", sod.getAssetTimeShares().get(0).getResourceID().equals(1002L));
	}

	public void test2J1AVaryPriorityNonStickyDoesChangeAssignment() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryPriority.txt");
		sid.getMetaData().setStatusVector("3");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Sticky jobs: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertTrue(" One job assigned (sticky Priority) ", sod.getAssetTimeShares().size() > 0);
		Assert.assertTrue("Non Sticky jobs (Priority) should  move", !sod.getAssetTimeShares().get(0).getResourceID().equals(1002L));
	}
	
	public void test2J1AVaryEstimatedTimeStickyDoesNotChangeAssignment() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryEstimatedTime.txt");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Sticky jobs: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertTrue(" One job assigned (sticky Estimated Time) ", sod.getAssetTimeShares().size() > 0);
		Assert.assertTrue("Sticky jobs (Estimated Time) should not move", sod.getAssetTimeShares().get(0).getResourceID().equals(1002L));
	}

	public void test2J1AVaryEstimatedTimeNonStickyDoesChangeAssignment() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryEstimatedTime.txt");
		sid.getMetaData().setStatusVector("3");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Sticky jobs: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertTrue(" One job assigned (sticky Estimated Time) ", sod.getAssetTimeShares().size() > 0);
		Assert.assertTrue("Non Sticky jobs (Estimated Time) should  move", !sod.getAssetTimeShares().get(0).getResourceID().equals(1002L));
	}

	public void test2J1AVaryProficiencyNonStickyDoesChangeAssignment() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryProficiency.txt");
		sid.getMetaData().setStatusVector("3");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Sticky jobs: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertTrue(" One job assigned (sticky Proficiency) ", sod.getAssetTimeShares().size() > 0);
		Assert.assertTrue(" Sticky jobs (Proficiency) should  not move", sod.getAssetTimeShares().get(0).getResourceID().equals(1001L));
	}

	public void test2J1AVaryProficiencyStickyDoesNotChangeAssignment() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/SchedulerTests/TestData2j1aVaryProficiency.txt");
		sid.getMetaData().setStatusVector("2");
		SchedulingOutputData sod = sc.schedule(sid, 100);
		Assert.assertEquals("Sticky jobs: No errors on good input", sod.getProcessingErrors().size(), 0);
		Assert.assertTrue(" One job assigned (sticky Proficiency) ", sod.getAssetTimeShares().size() > 0);
		Assert.assertTrue("Non Sticky jobs (Proficiency) should  move", !sod.getAssetTimeShares().get(0).getResourceID().equals(1001L));
	}
	
	// Larger tests of happy path 

	public void testMediumInputHasNoProcessingErrorsFromSID() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/LargeSchedulerTests/TestData100j10a.txt");
		SchedulingOutputData sod = sc.schedule(sid, 1);
		Assert.assertEquals("HC Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
	}


	public void testBigInputHasNoProcessingErrorsFromFile() throws IOException {
		SchedulingOutputData sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/LargeSchedulerTests/TestData100j35a.txt", 500);
		Assert.assertEquals("HC Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
	}
	
	public void testSmallInputDuplicateJobDoesNotLowerUtilityFromFile() throws IOException {
		SchedulingOutputData sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1a.txt", 500);
		Assert.assertEquals("HC Happy Path: No errors on original input", sod.getProcessingErrors().size(), 0);
		BigDecimal originalUtility = sod.getUtility();
		sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1aExtraJob.txt", 500);
		Assert.assertEquals("HC Happy Path: No errors on duplicated job input", sod.getProcessingErrors().size(), 0);
		BigDecimal dupUtility = sod.getUtility();
		Assert.assertTrue("Adding duplicate job should not lower utility", dupUtility.compareTo(originalUtility) >= 0);
		sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/SchedulerTests/TestData1j1aExtraJobLowerEstimatedTime.txt", 500);
		BigDecimal fitsUtility = sod.getUtility();
		Assert.assertEquals("HC Happy Path: No errors on lower time job input", sod.getProcessingErrors().size(), 0);
		Assert.assertTrue("Adding duplicate lower estimated time jobs should not lower utility", fitsUtility.compareTo(originalUtility) >= 0);
	}
	
	public void testBigInputDuplicateJobDoesNotLowerUtilityFromFile() throws IOException {
		SchedulingOutputData sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/LargeSchedulerTests/TestData100j35a.txt", 500);
		Assert.assertEquals("HC Happy Path: No errors on original input", sod.getProcessingErrors().size(), 0);
		BigDecimal originalUtility = sod.getUtility();
		sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/LargeSchedulerTests/TestData101j35a.txt", 500);
		Assert.assertEquals("HC Happy Path: No errors on duplicated job input", sod.getProcessingErrors().size(), 0);
		BigDecimal dupUtility = sod.getUtility();
		sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/LargeSchedulerTests/TestData101j35aLowerEstimatedTime.txt", 500);
		Assert.assertEquals("HC Happy Path: No errors on lower time job input", sod.getProcessingErrors().size(), 0);
		BigDecimal fitsUtility = sod.getUtility();
		Assert.assertTrue("Adding duplicate job should not lower utility", dupUtility.compareTo(originalUtility) >=0);
		Assert.assertTrue("Adding duplicate lower estimated time jobs should not lower utility", fitsUtility.compareTo(originalUtility) >= 0);
	}
	
/*	
	public void testManagedAssetTimeHardConstraintWithDuplicateJobDoesNotLowerUtilityFromFile() throws IOException {
		SchedulingOutputData sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/SchedulerTests/MikeSmallInput.txt", 500);
		Assert.assertEquals("HC Happy Path: No errors on duplicated job input", sod.getProcessingErrors().size(), 0);
		BigDecimal noDupUtility = sod.getUtility();
		sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/SchedulerTests/MikeInput.txt", 500);
		Assert.assertEquals("HC Happy Path: No errors on original input", sod.getProcessingErrors().size(), 0);
		BigDecimal dupUtility = sod.getUtility();
		Assert.assertTrue("Adding duplicate job should not lower utility", dupUtility.compareTo(noDupUtility) >=0);
	}
*/
	
/*
	public void testBigger35InputHasNoProcessingErrorsFromFile() throws IOException {
		SchedulingOutputData sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/LargeSchedulerTests/TestData200j30a35.txt", 500);
		Assert.assertEquals("HC Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
	}
*/	

/*
	public void testBiggerInputHasNoProcessingErrorsFromFile() throws IOException {
		SchedulingOutputData sod = sc.scheduleFromFile("/Users/kenlevine/Desktop/LargeSchedulerTests/TestData201j50a.txt", 500);
		Assert.assertEquals("HC Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
	}
*/	
	
/*
	public void testBigInput35HasNoProcessingErrorsFromSIDTakeThatMrDuffy() throws IOException {
		SchedulingInputData sid = ip.parse("/Users/kenlevine/Desktop/V35SchedulerTests/TestData200j49a35.txt");
		SchedulingOutputData sod = sc.schedule(sid, 500);
		Assert.assertEquals("HC Happy Path: No errors on good input", sod.getProcessingErrors().size(), 0);
	}
*/
	

	public void testGeneratingData() {
			dg.generate("/Users/kenlevine/Desktop/LargeSchedulerTests/TestData201j50a.txt",201, 50, 0);
	}

	
/*
	public void testExit () {
		System.exit(0);
	}
*/
}
