/**
 * 
 * Update job rows with forward looking duedates etc.
 * 
 * Inserts job rows due today 
 * 
 * Note: This mysql is supposed to be ran on a daily basis early, using
 * the following command:
 * 
 * mysql --database=iwmDemo --protocol=tcp --user=root --password=mlinkdb < update-job-data.sql
 * 
 */

USE iwmDemo;

UPDATE `Job` 
SET createdDate= DATE_ADD( curdate(), INTERVAL -3 DAY), 
    dueDate= DATE_ADD( curdate(), INTERVAL +2 DAY),
    risd=DATE_ADD( curdate(), INTERVAL -3 DAY),
    earliestStart=curDate(),
    latestStart= DATE_ADD( curdate(), INTERVAL +3 DAY);
    
/*due today */  
INSERT INTO `Job` (`createdDate`,`dis`,`description`,`dispatchedDate`,`dueDate`,`earliestStart`,`eom`,`ero`,`estTime`,
  `jobType`,`latestStart`,`note`,`priority`,`risd`,`scheduledDate`,`startedDate`,`status`,`sticky`,
  `totatEstTime`,`assetId`,`orgId`,`taskInstanceId`) VALUES 
  (curdate(),7,'Truck, utility, expanded capacity, enhanced, M1152',NULL,
  curdate(),curdate(),'E2','0BB43',3,'JT2',curdate(),'this job is due today',
  'P6',DATE_ADD( curdate(), INTERVAL -3 DAY), NULL,NULL,'JS6',0x00,
  180,1,1,NULL);
    
INSERT INTO `Job` 
  (`createdDate`,`dis`,`description`,`dispatchedDate`,`dueDate`,`earliestStart`,`eom`,`ero`,`estTime`,
  `jobType`,`latestStart`,`note`,`priority`,`risd`,`scheduledDate`,`startedDate`,`status`,`sticky`,
  `totatEstTime`,`assetId`,`orgId`,`taskInstanceId`) VALUES 
  (curdate(),7,'Truck, utility, expanded capacity, enhanced, M1152',NULL,
  curdate(),curdate(),'E2','0BB43',3,'JT2',curdate(),'
  this job is due today','P5',
  DATE_ADD( curdate(), INTERVAL -3 DAY),NULL,NULL,'JS5',0x00,
  170,1,1,NULL);
  
      
INSERT INTO `Job` 
  (`createdDate`,`dis`,`description`,`dispatchedDate`,`dueDate`,`earliestStart`,`eom`,`ero`,`estTime`,
  `jobType`,`latestStart`,`note`,`priority`,`risd`,`scheduledDate`,`startedDate`,`status`,`sticky`,
  `totatEstTime`,`assetId`,`orgId`,`taskInstanceId`) VALUES 
  (curdate(),7,'Truck, utility, expanded capacity, enhanced, M1152',NULL,
  curdate(),curdate(),'E2','0BB43',3,'JT1',curdate(),'this job is due today','P1',
  DATE_ADD( curdate(), INTERVAL -3 DAY),NULL,NULL,'JS3',0x00,
  170,1,1,NULL);

INSERT INTO `Job` 
  (`createdDate`,`dis`,`description`,`dispatchedDate`,`dueDate`,`earliestStart`,`eom`,`ero`,`estTime`,
  `jobType`,`latestStart`,`note`,`priority`,`risd`,`scheduledDate`,`startedDate`,`status`,`sticky`,
  `totatEstTime`,`assetId`,`orgId`,`taskInstanceId`) VALUES 
  (curdate(),7,'Truck, utility, expanded capacity, enhanced, M1152',NULL,
  curdate(),curdate(),'E2','0BB43',3,'JT1',curdate(),'this job is due today','P2',
  DATE_ADD( curdate(), INTERVAL -3 DAY),NULL,NULL,'JS2',0x00,
  170,1,1,NULL);

INSERT INTO `Job` 
  (`createdDate`,`dis`,`description`,`dispatchedDate`,`dueDate`,`earliestStart`,`eom`,`ero`,`estTime`,
  `jobType`,`latestStart`,`note`,`priority`,`risd`,`scheduledDate`,`startedDate`,`status`,`sticky`,
  `totatEstTime`,`assetId`,`orgId`,`taskInstanceId`) VALUES 
  (curdate(),7,'Truck, utility, expanded capacity, enhanced, M1152',NULL,
  curdate(),curdate(),'E2','0BB43',3,'JT1',curdate(),'this job is due today','P3',
  DATE_ADD( curdate(), INTERVAL -3 DAY),NULL,NULL,'JS3',0x00,
  170,1,1,NULL);

INSERT INTO `Job` 
  (`createdDate`,`dis`,`description`,`dispatchedDate`,`dueDate`,`earliestStart`,`eom`,`ero`,`estTime`,
  `jobType`,`latestStart`,`note`,`priority`,`risd`,`scheduledDate`,`startedDate`,`status`,`sticky`,
  `totatEstTime`,`assetId`,`orgId`,`taskInstanceId`) VALUES 
  (curdate(),7,'Truck, utility, expanded capacity, enhanced, M1152',NULL,
  curdate(),curdate(),'E2','0BB43',3,'JT1',curdate(),'this job is due today','P1',
  DATE_ADD( curdate(), INTERVAL -3 DAY),NULL,NULL,'JS2',0x00,
  170,1,1,NULL);