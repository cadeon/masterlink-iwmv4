-- SQL statements which are executed at application startup if hibernate.hbm2ddl.auto is 'create' or 'create-drop'
use iwmDemo;

insert into user values('1', null, null, 'nadia@masterlink.com', '1', 'Administrator',  'Administrator', 'admin', 'admin');
insert into role values('1', null,null,'IWM Application Administrator', 'admin');
insert into user_role values('1','1');


INSERT INTO `Organization` 
(orgid, archivedate, lastchange, name, orgtype)
VALUES 
(1,NULL,NULL,'20000 H','O1');


INSERT INTO `AssetType` 
(assettypeid, archivedate, lastchange, classid, code, description)
VALUES 
(1,NULL,NULL,1,1,'Universal Template'),
(2,NULL,NULL,2,2,'Worker Template'),
(3,NULL,NULL,3,3,'Proficiency Template');


INSERT INTO `AssetTemplate` 
(assettemplateid, archivedate, lastchange, assetkind, isparent, name, plan, assettypeid, parentid)
VALUES 
(1,NULL,NULL,'PA','\0','Universal Template',NULL,1,NULL),
(2,NULL,NULL,'WO','\0','Worker Template',NULL,2,NULL),
(3,NULL,NULL,'PR','\0','Technician',NULL,3,2);


INSERT INTO `TaskTemplate` 
(tasktemplateid, archivedate, lastchange, esthours, estmin, esttime, expnumdays, expirytype, freqdays, freqmonths, freqtype, priority, runhoursthresh, runhoursthreshinc, taskcode, taskdescription, tasktype, assettemplateid)
VALUES 
(1,NULL,NULL,0,0,100,0,'ET5',0,0,'FT1','P6',NULL,NULL,'M17','Rplc power steering pump ','TT1',1),
(2,NULL,NULL,0,0,90,0,'ET5',0,0,'FT1','P6',NULL,NULL,'M22','Rplc. pwr steering hose','TT1',1),
(3,NULL,NULL,0,0,15,0,'ET5',0,0,'FT1','P6',NULL,NULL,'H34','Rplc. water spray nozzle','TT1',1),
(4,NULL,NULL,0,0,160,0,'ET5',0,0,'FT1','P6',NULL,NULL,'E34','Rplc rear axle','TT1',1),
(5,NULL,NULL,0,0,240,0,'ET5',0,0,'FT1','P6',NULL,NULL,'52','Annual PMCS','TT2',1),
(6,NULL,NULL,0,0,240,0,'ET5',0,0,'FT1','P6',NULL,NULL,'816','Rplc. transmission seals','TT1',1),
(7,NULL,NULL,0,0,120,0,'ET5',0,0,'FT1','P6',NULL,NULL,'J17','Rplc. water pump','TT1',1),
(8,NULL,NULL,0,0,25,0,'ET5',0,0,'FT1','P6',NULL,NULL,'J11','Rplc. water spray hose','TT1',1),
(9,NULL,NULL,0,0,45,0,'ET5',0,0,'FT1','P6',NULL,NULL,'E31','Rplc shocks','TT1',1),
(10,NULL,NULL,0,0,45,0,'ET5',0,0,'FT1','P6',NULL,NULL,'J18','Rplc water valve','TT1',1),
(11,NULL,NULL,0,0,70,0,'ET5',0,0,'FT1','P6',NULL,NULL,'J19','Rplc AC compressor','TT1',1),
(12,NULL,NULL,0,0,240,0,'ET5',0,0,'FT7','P6',NULL,NULL,'53','Semi-Annual PMCS','TT2',1);


INSERT INTO `Task` 
(taskid, archivedate, lastchange, description, esttime, expirydays, expirytype, freqdays, freqmonths, freqtype, isactive, isstandalone, lastplanneddate, lastserviceddate, priority, runhoursthresh, runhoursthreshinc, startdate, taskcode, tasktype, tasktemplateid)
VALUES 
(1,NULL,NULL,'Rplc power steering pump ',100,0,'ET5',0,0,'FT1','','',NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','M17','TT1',1),
(2,NULL,NULL,'Rplc. pwr steering hose',60,0,'ET5',0,0,'FT1','','',NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','M22','TT1',2),
(3,NULL,NULL,'Rplc. water spray nozzle',20,0,'ET5',0,0,'FT1','','',NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','H34','TT1',3),
(4,NULL,NULL,'Rplc rear axle',360,0,'ET5',0,0,'FT1','','',NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','E34','TT1',4),
(5,NULL,NULL,'Annual PMCS',480,0,'ET5',0,0,'FT1','','',NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','52','TT2',5),
(6,NULL,NULL,'Rplc. transmission seals',240,0,'ET5',0,0,'FT1','','',NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','816','TT1',6),
(7,NULL,NULL,'Rplc. water pump',120,0,'ET5',0,0,'FT1','','',NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','J17','TT1',7),
(8,NULL,NULL,'Rplc. water spray hose',25,0,'ET5',0,0,'FT1','','',NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','J11','TT1',8),
(9,NULL,NULL,'Rplc shocks',180,0,'ET5',0,0,'FT1','','',NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','E31','TT1',9),
(10,NULL,NULL,'Rplc water valve',45,0,'ET5',0,0,'FT1','','',NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','J18','TT1',10),
(11,NULL,NULL,'Rplc AC compressor',70,0,'ET5',0,0,'FT1','','',NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','J19','TT1',11),
(12,NULL,NULL,'Semi-Annual PMCS',240,0,'ET5',0,0,'FT7','','',NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','53','TT2',12);


INSERT INTO `TaskInstance` 
(taskinstanceid, archivedate, lastchange, esttime, isstandalonetask, taskid, tasktemplateid)
VALUES 
(1,NULL,NULL,100,'',1,NULL),
(2,NULL,NULL,60,'',2,NULL),
(3,NULL,NULL,20,'',3,NULL),
(4,NULL,NULL,360,'',4,NULL),
(5,NULL,NULL,480,'',5,NULL),
(6,NULL,NULL,240,'',6,NULL),
(7,NULL,NULL,120,'',7,NULL),
(8,NULL,NULL,25,'',8,NULL),
(9,NULL,NULL,180,'',9,NULL),
(10,NULL,NULL,45,'',10,NULL),
(11,NULL,NULL,70,'',11,NULL),
(12,NULL,NULL,240,'',12,NULL);


INSERT INTO `Asset` 
(assetid, archivedate, lastchange, assetkind, createddate, hascustomdata, hascustomtask, isactive, iscustom, isparent, isstickyresource, runhours, startdate, tag, assettemplateid, assettypeid, orgid, parentid, taskid)
VALUES 
(1,NULL,NULL,'PA',NULL,'\0','\0','','\0','\0','\0',0,NULL,'619910',1,1,1,NULL,NULL),
(2,NULL,NULL,'PA',NULL,'\0','\0','','\0','\0','\0',0,NULL,'UR0095',1,1,1,NULL,NULL),
(3,NULL,NULL,'PA',NULL,'\0','\0','','\0','\0','\0',0,NULL,'654904',1,1,1,NULL,NULL),
(4,NULL,NULL,'PA',NULL,'\0','\0','','\0','\0','\0',0,NULL,'628290',1,1,1,NULL,NULL),
(5,NULL,NULL,'PA',NULL,'\0','\0','','\0','\0','\0',0,NULL,'620596',1,1,1,NULL,NULL),
(6,NULL,NULL,'PA',NULL,'\0','\0','','\0','\0','\0',0,NULL,'598084',1,1,1,NULL,NULL),
(7,NULL,NULL,'PA',NULL,'\0','\0','','\0','\0','\0',0,NULL,'630655',1,1,1,NULL,NULL),
(8,NULL,NULL,'PA',NULL,'\0','\0','','\0','\0','\0',0,NULL,'652863',1,1,1,NULL,NULL),
(9,NULL,NULL,'PA',NULL,'\0','\0','','\0','\0','\0',0,NULL,'10631A',1,1,1,NULL,NULL),
(22,NULL,NULL,'WO',NULL,'\0','\0','','\0','','\0',0,NULL,'Sgt Franco',2,2,1,NULL,NULL),
(23,NULL,NULL,'WO',NULL,'\0','\0','','\0','','\0',0,NULL,'Sgt Jones',2,2,1,NULL,NULL),
(24,NULL,NULL,'WO',NULL,'\0','\0','','\0','','\0',0,NULL,'Cpl Rivers',2,2,1,NULL,NULL),
(25,NULL,NULL,'WO',NULL,'\0','\0','','\0','','\0',0,NULL,'LCpl Smith',2,2,1,NULL,NULL),
(26,NULL,NULL,'WO',NULL,'\0','\0','','\0','','\0',0,NULL,'Sgt Abbot',2,2,1,NULL,NULL),
(27,NULL,NULL,'WO',NULL,'\0','\0','','\0','','\0',0,NULL,'Sgt Wong',2,2,1,NULL,NULL),
(28,NULL,NULL,'WO',NULL,'\0','\0','','\0','','\0',0,NULL,'Sgt Gomez',2,2,1,NULL,NULL),
(29,NULL,NULL,'WO',NULL,'\0','\0','','\0','','\0',0,NULL,'LCpl Carter',2,2,1,NULL,NULL),
(30,NULL,NULL,'WO',NULL,'\0','\0','','\0','','\0',0,NULL,'Sgt Hopper',2,2,1,NULL,NULL),
(31,NULL,NULL,'WO',NULL,'\0','\0','','\0','','\0',0,NULL,'Scpl. Carter',2,2,1,NULL,NULL),
(12,NULL,NULL,'PR',NULL,'\0','\0','','\0','\0','\0',0,NULL,'Technician',3,3,NULL,22,NULL),
(13,NULL,NULL,'PR',NULL,'\0','\0','','\0','\0','\0',0,NULL,'Technician',3,3,NULL,23,NULL),
(14,NULL,NULL,'PR',NULL,'\0','\0','','\0','\0','\0',0,NULL,'Technician',3,3,NULL,24,NULL),
(15,NULL,NULL,'PR',NULL,'\0','\0','','\0','\0','\0',0,NULL,'Technician',3,3,NULL,25,NULL),
(16,NULL,NULL,'PR',NULL,'\0','\0','','\0','\0','\0',0,NULL,'Technician',3,3,NULL,26,NULL),
(17,NULL,NULL,'PR',NULL,'\0','\0','','\0','\0','\0',0,NULL,'Technician',3,3,NULL,27,NULL),
(18,NULL,NULL,'PR',NULL,'\0','\0','','\0','\0','\0',0,NULL,'Technician',3,3,NULL,28,NULL),
(19,NULL,NULL,'PR',NULL,'\0','\0','','\0','\0','\0',0,NULL,'Technician',3,3,NULL,29,NULL),
(20,NULL,NULL,'PR',NULL,'\0','\0','','\0','\0','\0',0,NULL,'Technician',3,3,NULL,30,NULL),
(21,NULL,NULL,'PR',NULL,'\0','\0','','\0','\0','\0',0,NULL,'Technician',3,3,NULL,31,NULL);


INSERT INTO `AssetAttributeTemplate` 
(assetattributetemplateid, archivedate, lastchange, name, value, assettemplateid)
VALUES 
(1,NULL,NULL,'TAM',NULL,1),
(2,NULL,NULL,'ID',NULL,1),
(3,NULL,NULL,'OWNER',NULL,1),
(4,NULL,NULL,'NSN',NULL,1),
(5,NULL,NULL,'MODEL',NULL,1);


INSERT INTO `AssetAttribute` 
(assetattributeid, archivedate, lastchange, name, value, assetattributetemplateid, assetid)
VALUES 
(1,NULL,NULL,'TAM','D0022',1,1),
(2,NULL,NULL,'ID','II285A',2,1),
(5,NULL,NULL,'MODEL','M1152',5,1),
(6,NULL,NULL,'TAM','D0027',1,2),
(7,NULL,NULL,'ID','11291C',2,2),
(8,NULL,NULL,'MODEL','CAT II R633',5,2),
(9,NULL,NULL,'TAM','D0036',1,3),
(10,NULL,NULL,'ID','11803A',2,3),
(11,NULL,NULL,'MODEL','M-ATV',5,3),
(12,NULL,NULL,'TAM','D0030',1,4),
(13,NULL,NULL,'ID','11301B',2,4),
(14,NULL,NULL,'MODEL','M1151A1',5,4),
(15,NULL,NULL,'TAM','D0031',1,5),
(16,NULL,NULL,'ID','112858',2,5),
(17,NULL,NULL,'MODEL','M1165',5,5),
(18,NULL,NULL,'TAM','D0004',1,6),
(19,NULL,NULL,'ID','106290',2,6),
(20,NULL,NULL,'MODEL','AMK25',5,6),
(22,NULL,NULL,'TAM','D0022',1,7),
(23,NULL,NULL,'ID','11285A',2,7),
(24,NULL,NULL,'MODEL','M1152',5,7),
(25,NULL,NULL,'TAM','D0036',1,8),
(26,NULL,NULL,'ID','11803A',2,8),
(27,NULL,NULL,'MODEL','M-ATV',5,8),
(28,NULL,NULL,'TAM','D1062',1,9),
(29,NULL,NULL,'ID','10621A',2,9),
(30,NULL,NULL,'MODEL','NK-27',5,9);


INSERT INTO `Job` 
(jobid, archivedate, lastchange, jobcat, dueDate, completeddate, createdby, createddate, dis, description, dispatcheddate, earlieststart, eom, ero, esttime, jobtype, lateststart, note, priority, risd, scheduleddate, starteddate, status, sticky, totatesttime, orgid,assetid)
VALUES 
(1,NULL,NULL,'CAT10','2011-09-28',NULL,NULL,'2011-05-20',7,'Truck, utility, expanded capacity, enhanced, M1152',NULL,'2011-05-20','E2','0BB43',3,'JT2','2011-05-20','','P6','2011-05-20',NULL,NULL,'JS6','\0',180,1,1),
(3,NULL,NULL,'CAT6','2011-09-28',NULL,NULL,'2011-05-27',0,NULL,NULL,'2011-05-27','E2','0BB75',6,'JT2','2011-05-27',NULL,'P6','2011-05-27',NULL,NULL,'JS6','\0',360,1,3),
(4,NULL,NULL,'CAT6','2011-09-28',NULL,NULL,'2011-05-24',3,NULL,NULL,'2011-05-23','E2','0BB58',8,'JT1','2011-05-23',NULL,'P6','2011-05-23',NULL,NULL,'JS6','\0',480,1,4),
(5,NULL,NULL,'CAT6','2011-09-28',NULL,NULL,'2011-05-27',0,NULL,NULL,'2011-05-27','E2','0BB59',0,'JT2','2011-05-27',NULL,'P13','2011-05-27',NULL,NULL,'JS6','\0',0,1,5),
(6,NULL,NULL,'CAT6','2011-09-28',NULL,NULL,'2011-05-27',0,NULL,NULL,'2011-05-27','E2','0BB61',0,'JT2','2011-05-27',NULL,'P12','2011-05-27',NULL,NULL,'JS6','\0',0,1,6),
(7,NULL,NULL,'CAT6','2011-09-28',NULL,NULL,'2011-05-27',0,NULL,NULL,'2011-05-27','E2','0BB65',3,'JT2','2011-05-27',NULL,'P13','2011-05-27',NULL,NULL,'JS6','\0',180,1,7),
(8,NULL,NULL,'CAT10','2011-09-28',NULL,NULL,'2011-05-27',0,NULL,NULL,'2011-05-27','E2','0BB60',0,'JT2','2011-05-27',NULL,'P6','2011-05-27',NULL,NULL,'JS1','\0',0,1,8),
(9,NULL,NULL,'CAT10','2011-09-28',NULL,NULL,'2011-05-27',0,NULL,NULL,'2011-05-27','E2',NULL,0,'JT2','2011-05-27',NULL,'P7','2011-05-27',NULL,NULL,'JS1','\0',0,1,NULL),
(10,NULL,NULL,'CAT6','2011-09-28',NULL,NULL,'2011-05-27',0,NULL,NULL,'2011-05-27','E2',NULL,0,'JT1','2011-05-27',NULL,'P13','2011-05-27',NULL,NULL,'JS1','\0',0,1,NULL);


INSERT INTO `JobTask` 
(jobtaskid, archivedate, lastchange, actualtime, description, esttime, taskcode, jobid, taskinstanceid)
VALUES 
(1,NULL,NULL,0,'Rplc power steering pump ',2,'M17',1,1),
(2,NULL,NULL,0,'Rplc. pwr steering hose',2,'M22',1,2),
(3,NULL,NULL,0,'Rplc. water spray nozzle',2,'H34',1,3),
(4,NULL,NULL,0,'Rplc rear axle',8,'E34',3,4),
(5,NULL,NULL,0,'Annual PMCS',6,'52',4,5),
(6,NULL,NULL,0,'Rplc. transmission seals',8,'816',5,6),
(7,NULL,NULL,0,'Rplc. water pump',0,'J17',6,7),
(8,NULL,NULL,0,'Rplc. water spray hose',0,'J11',6,8),
(9,NULL,NULL,0,'Rplc shocks',3,'E31',7,9),
(10,NULL,NULL,0,'Rplc water valve',0,'J18',8,10),
(11,NULL,NULL,0,'Rplc AC compressor',3,'J19',9,11),
(12,NULL,NULL,0,'Semi-Annual PMCS',0,'53',10,12);




INSERT INTO `Task_Asset` 
(taskid, assetid) 
VALUES 
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),
(2,1),(2,2),(2,3),(2,4),(2,5),(2,6),(2,7),(2,8),(2,9),
(3,1),(3,2),(3,3),(3,4),(3,5),(3,6),(3,7),(3,8),(3,9),
(4,1),(4,2),(4,3),(4,4),(4,5),(4,6),(4,7),(4,8),(4,9),
(5,1),(5,2),(5,3),(5,4),(5,5),(5,6),(5,7),(5,8),(5,9),
(6,1),(6,2),(6,3),(6,4),(6,5),(6,6),(6,7),(6,8),(6,9),
(7,1),(7,2),(7,3),(7,4),(7,5),(7,6),(7,7),(7,8),(7,9),
(8,1),(8,2),(8,3),(8,4),(8,5),(8,6),(8,7),(8,8),(8,9),
(9,1),(9,2),(9,3),(9,4),(9,5),(9,6),(9,7),(9,8),(9,9),
(10,1),(10,2),(10,3),(10,4),(10,5),(10,6),(10,7),(10,8),(10,9),
(11,1),(11,2),(11,3),(11,4),(11,5),(11,6),(11,7),(11,8),(11,9),
(12,1),(12,2),(12,3),(12,4),(12,5),(12,6),(12,7),(12,8),(12,9);


INSERT INTO `AssetResource` 
(assetresourceid, archivedate, lastchange, ismanualassign, quantity, assettemplateid, jobid, stickyassetid, taskid, tasktemplateid)
VALUES 
(1,NULL,NULL,'\0',1,2,1,NULL,1,1),
(2,NULL,NULL,'\0',1,2,3,NULL,2,2),
(3,NULL,NULL,'\0',1,2,4,NULL,3,3),
(4,NULL,NULL,'\0',1,2,5,NULL,4,4),
(5,NULL,NULL,'\0',1,2,6,NULL,5,5),
(6,NULL,NULL,'\0',1,2,7,NULL,6,6),
(7,NULL,NULL,'\0',1,2,8,NULL,7,7),
(8,NULL,NULL,'\0',1,2,9,NULL,8,8),
(9,NULL,NULL,'\0',1,2,10,NULL,9,9);

INSERT INTO `NonAssetResource` 
(nonassetresourceid, archivedate, lastchange, description, quantity, tag, type, unitrefid, jobid, taskid, tasktemplateid, status, currentLoc, recDateStr)
VALUES 
(1,NULL,NULL,'Power steering pump',1,'2540-01-385-9031','NA1',0,1,1,1,'Received 1275','200000 H','2011-10-02'),
(2,NULL,NULL,'Brackets',1,'2540-01-300-8745','NA1',0,1,1,1,'Shipped 1275','Camp Pendelton','2011-10-03'),
(3,NULL,NULL,'Power steering hose',1,'4750-01-341-4912','NA1',0,1,2,2,'Canceled 1275','200000 H',NULL),
(4,NULL,NULL,'Water spray nozzle',1,'2540-01-184-5503','NA1',0,1,3,3,'Not ordered','200000 H',NULL),
(5,NULL,'2011-06-01 00:00:00','Rear Axle',1,'8675-30-986-7530','NA1',0,3,4,4,'Received 1277','200000 H','2011-10-04'),
(6,NULL,'2011-06-01 00:00:00','Engine oil 20w50',15,'0113-86-951-4351','NA2',1,4,5,5,'Received 1272','Camp Pendleton','2011-09-29'),
(7,NULL,'2011-06-01 00:00:00','M-ATV Annual PMCS Kit',1,'0014-80-145-2724','NA3',0,4,5,5,'Received 1272','Camp Pendleton','2011-09-29');

INSERT INTO  AssetTimeShare 
(assettimeshareid, archivedate, lastchange, asresource, isassigned, enddate, startdate, assetid, jobid)
VALUES 
(35,NULL,NULL,'','','1969-12-31 18:00:00','1969-12-31 18:00:00',22,4),
(36,NULL,NULL,'','','1969-12-31 18:00:00','1969-12-31 18:00:00',22,3),
(37,NULL,NULL,'','','1969-12-31 18:00:00','1969-12-31 18:00:00',22,1),
(38,NULL,NULL,'','','1969-12-31 18:00:00','1969-12-31 18:00:00',22,5),
(39,NULL,NULL,'','','1969-12-31 18:00:00','1969-12-31 18:00:00',22,6),
(40,NULL,NULL,'','','1969-12-31 18:00:00','1969-12-31 18:00:00',22,7);

INSERT INTO `contact` (contactid, archivedate, lastchange, fname, jobtitle, lname, middle, suffix, rolodexcardid, email, phone)
VALUES 
(1,NULL,NULL,'Mike','Sgt','Sgt. Castro',NULL,NULL,NULL,'castro@navy.mil','841-2818'),
(2,NULL,NULL,'Mark','Sgt','Sgt. Jones',NULL,NULL,NULL,NULL,NULL);
