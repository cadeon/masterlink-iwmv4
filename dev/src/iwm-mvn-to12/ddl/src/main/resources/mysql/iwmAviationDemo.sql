# Sequel Pro dump
# Version 2492
# http://code.google.com/p/sequel-pro
#
# Host: 127.0.0.1 (MySQL 5.5.10)
# Database: iwmDemo
# Generation Time: 2011-06-15 14:12:29 -0400
# ************************************************************

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table Action
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Action`;

CREATE TABLE `Action` (
  `actionId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `isCustom` bit(1) DEFAULT NULL,
  `modifier` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `verb` varchar(50) DEFAULT NULL,
  `actionTemplateId` bigint(20) DEFAULT NULL,
  `taskId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`actionId`),
  KEY `FK74946A5662824246` (`actionTemplateId`),
  KEY `FK74946A56B0D5174B` (`taskId`),
  CONSTRAINT `FK74946A5662824246` FOREIGN KEY (`actionTemplateId`) REFERENCES `ActionTemplate` (`actionTemplateId`),
  CONSTRAINT `FK74946A56B0D5174B` FOREIGN KEY (`taskId`) REFERENCES `Task` (`taskId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table ActionTemplate
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ActionTemplate`;

CREATE TABLE `ActionTemplate` (
  `actionTemplateId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `modifier` varchar(45) DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  `sequence` int(11) DEFAULT NULL,
  `verb` varchar(45) NOT NULL,
  `taskTemplateId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`actionTemplateId`),
  KEY `FK7A9CE4F0B74849E4` (`taskTemplateId`),
  CONSTRAINT `FK7A9CE4F0B74849E4` FOREIGN KEY (`taskTemplateId`) REFERENCES `TaskTemplate` (`taskTemplateId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table address
# ------------------------------------------------------------

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
  `addressId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `addrType` varchar(255) NOT NULL,
  `city` varchar(25) DEFAULT NULL,
  `isMailingAddr` bit(1) DEFAULT NULL,
  `line1` varchar(50) DEFAULT NULL,
  `line2` varchar(50) DEFAULT NULL,
  `state` varchar(2) NOT NULL,
  `zip` varchar(50) DEFAULT NULL,
  `contactId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`addressId`),
  KEY `FKBB979BF43ECAEA65` (`contactId`),
  CONSTRAINT `FKBB979BF43ECAEA65` FOREIGN KEY (`contactId`) REFERENCES `contact` (`contactId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table Asset
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Asset`;

CREATE TABLE `Asset` (
  `assetId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `archivedDate` date DEFAULT NULL,
  `assetKind` varchar(2) NOT NULL,
  `createdDate` date DEFAULT NULL,
  `hasCustomData` bit(1) DEFAULT NULL,
  `hasCustomTask` bit(1) DEFAULT NULL,
  `isActive` bit(1) DEFAULT NULL,
  `isCustom` bit(1) DEFAULT NULL,
  `isParent` bit(1) NOT NULL,
  `isStickyResource` bit(1) NOT NULL,
  `runHours` int(11) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `tag` varchar(50) DEFAULT NULL,
  `assetTemplateId` bigint(20) DEFAULT NULL,
  `assetTypeId` bigint(20) DEFAULT NULL,
  `orgId` bigint(20) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `taskId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`assetId`),
  KEY `FK3C9FAD0A9585884` (`assetTemplateId`),
  KEY `FK3C9FAD0B0D5174B` (`taskId`),
  KEY `FK3C9FAD0A761E8F` (`parentId`),
  KEY `FK3C9FAD090BBF968` (`orgId`),
  KEY `FK3C9FAD02746F084` (`assetTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;

LOCK TABLES `Asset` WRITE;
/*!40000 ALTER TABLE `Asset` DISABLE KEYS */;
INSERT INTO `Asset` (`assetId`,`archiveDate`,`lastChange`,`archivedDate`,`assetKind`,`createdDate`,`hasCustomData`,`hasCustomTask`,`isActive`,`isCustom`,`isParent`,`isStickyResource`,`runHours`,`startDate`,`tag`,`assetTemplateId`,`assetTypeId`,`orgId`,`parentId`,`taskId`)
VALUES
	(1,NULL,NULL,NULL,'PA',NULL,0,0,1,0,0,0,0,NULL,'N51545',1,1,1,NULL,NULL),
	(2,NULL,NULL,NULL,'PA',NULL,0,0,1,0,0,0,0,NULL,'N115HM',1,1,1,NULL,NULL),
	(3,NULL,NULL,NULL,'PA',NULL,0,0,1,0,0,0,0,NULL,'N6466B',1,1,1,NULL,NULL),
	(4,NULL,NULL,NULL,'PA',NULL,0,0,1,0,0,0,0,NULL,'N4522X',1,1,1,NULL,NULL),
	(5,NULL,NULL,NULL,'PA',NULL,0,0,1,0,0,0,0,NULL,'N787ER',1,1,1,NULL,NULL),
	(6,NULL,NULL,NULL,'PA',NULL,0,0,1,0,0,0,0,NULL,'N64545',1,1,1,NULL,NULL),
	(22,NULL,NULL,NULL,'WO',NULL,0,0,1,0,1,0,0,NULL,'Chris Duffy',2,2,1,NULL,NULL),
	(23,NULL,NULL,NULL,'WO',NULL,0,0,1,0,1,0,0,NULL,'Gary Friar',2,2,1,NULL,NULL);

/*!40000 ALTER TABLE `Asset` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table AssetAssignement
# ------------------------------------------------------------

DROP TABLE IF EXISTS `AssetAssignement`;

CREATE TABLE `AssetAssignement` (
  `assetAssignementId` bigint(20) NOT NULL AUTO_INCREMENT,
  `assetTimeShareId` bigint(20) DEFAULT NULL,
  `assignmentDate` datetime DEFAULT NULL,
  `removalDate` datetime DEFAULT NULL,
  PRIMARY KEY (`assetAssignementId`),
  UNIQUE KEY `assetAssignementId` (`assetAssignementId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table AssetAttribute
# ------------------------------------------------------------

DROP TABLE IF EXISTS `AssetAttribute`;

CREATE TABLE `AssetAttribute` (
  `assetAttributeId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `value` varchar(45) DEFAULT NULL,
  `assetAttributeTemplateId` bigint(20) DEFAULT NULL,
  `assetId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`assetAttributeId`),
  KEY `FKDAB4F9AC53E29492` (`assetAttributeTemplateId`),
  KEY `FKDAB4F9AC9A688755` (`assetId`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;

LOCK TABLES `AssetAttribute` WRITE;
/*!40000 ALTER TABLE `AssetAttribute` DISABLE KEYS */;
INSERT INTO `AssetAttribute` (`assetAttributeId`,`archiveDate`,`lastChange`,`name`,`value`,`assetAttributeTemplateId`,`assetId`)
VALUES
	(1,NULL,NULL,'TAM','172',1,1),
	(2,NULL,NULL,'ID','P',2,1),
	(5,NULL,NULL,'MODEL','C172P',5,1),
	(6,NULL,NULL,'TAM','172',1,2),
	(7,NULL,NULL,'ID','P',2,2),
	(8,NULL,NULL,'MODEL','C172P',5,2),
	(9,NULL,NULL,'TAM','172',1,3),
	(10,NULL,NULL,'ID','R',2,3),
	(11,NULL,NULL,'MODEL','C172SP',5,3),
	(12,NULL,NULL,'TAM','172',1,4),
	(13,NULL,NULL,'ID','SP',2,4),
	(14,NULL,NULL,'MODEL','C172R',5,4),
	(31,NULL,NULL,'MODEL','C172R',5,5),
	(32,NULL,NULL,'MODEL','C172SP',5,6);

/*!40000 ALTER TABLE `AssetAttribute` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table AssetAttributeTemplate
# ------------------------------------------------------------

DROP TABLE IF EXISTS `AssetAttributeTemplate`;

CREATE TABLE `AssetAttributeTemplate` (
  `assetAttributeTemplateId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `value` varchar(45) DEFAULT NULL,
  `assetTemplateId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`assetAttributeTemplateId`),
  UNIQUE KEY `name` (`name`),
  KEY `FKC9BBBE46A9585884` (`assetTemplateId`),
  CONSTRAINT `FKC9BBBE46A9585884` FOREIGN KEY (`assetTemplateId`) REFERENCES `AssetTemplate` (`assetTemplateId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

LOCK TABLES `AssetAttributeTemplate` WRITE;
/*!40000 ALTER TABLE `AssetAttributeTemplate` DISABLE KEYS */;
INSERT INTO `AssetAttributeTemplate` (`assetAttributeTemplateId`,`archiveDate`,`lastChange`,`name`,`value`,`assetTemplateId`)
VALUES
	(1,NULL,NULL,'TAM',NULL,1),
	(2,NULL,NULL,'ID',NULL,1),
	(3,NULL,NULL,'OWNER',NULL,1),
	(4,NULL,NULL,'NSN',NULL,1),
	(5,NULL,NULL,'MODEL',NULL,1);

/*!40000 ALTER TABLE `AssetAttributeTemplate` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table AssetCalendar
# ------------------------------------------------------------

DROP TABLE IF EXISTS `AssetCalendar`;

CREATE TABLE `AssetCalendar` (
  `assetCalendarId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `day` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `assetId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`assetCalendarId`),
  KEY `FK70038A2E9A688755` (`assetId`),
  CONSTRAINT `FK70038A2E9A688755` FOREIGN KEY (`assetId`) REFERENCES `Asset` (`assetId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table AssetResource
# ------------------------------------------------------------

DROP TABLE IF EXISTS `AssetResource`;

CREATE TABLE `AssetResource` (
  `assetResourceId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `isManualAssign` bit(1) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `assetTemplateId` bigint(20) DEFAULT NULL,
  `jobId` bigint(20) DEFAULT NULL,
  `stickyAssetId` bigint(20) DEFAULT NULL,
  `taskId` bigint(20) DEFAULT NULL,
  `taskTemplateId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`assetResourceId`),
  KEY `FK665052FED8000D4C` (`stickyAssetId`),
  KEY `FK665052FEA9585884` (`assetTemplateId`),
  KEY `FK665052FEB74849E4` (`taskTemplateId`),
  KEY `FK665052FEB0D5174B` (`taskId`),
  KEY `FK665052FE1165187C` (`jobId`),
  CONSTRAINT `FK665052FE1165187C` FOREIGN KEY (`jobId`) REFERENCES `Job` (`jobId`),
  CONSTRAINT `FK665052FEA9585884` FOREIGN KEY (`assetTemplateId`) REFERENCES `AssetTemplate` (`assetTemplateId`),
  CONSTRAINT `FK665052FEB0D5174B` FOREIGN KEY (`taskId`) REFERENCES `Task` (`taskId`),
  CONSTRAINT `FK665052FEB74849E4` FOREIGN KEY (`taskTemplateId`) REFERENCES `TaskTemplate` (`taskTemplateId`),
  CONSTRAINT `FK665052FED8000D4C` FOREIGN KEY (`stickyAssetId`) REFERENCES `Asset` (`assetId`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

LOCK TABLES `AssetResource` WRITE;
/*!40000 ALTER TABLE `AssetResource` DISABLE KEYS */;
INSERT INTO `AssetResource` (`assetResourceId`,`archiveDate`,`lastChange`,`isManualAssign`,`quantity`,`assetTemplateId`,`jobId`,`stickyAssetId`,`taskId`,`taskTemplateId`)
VALUES
	(1,NULL,NULL,0,1,3,1,NULL,1,1),
	(2,NULL,NULL,0,1,3,3,NULL,2,2),
	(3,NULL,NULL,0,1,3,4,NULL,3,3),
	(4,NULL,NULL,0,1,3,5,NULL,4,4),
	(5,NULL,NULL,0,1,3,6,NULL,5,5),
	(6,NULL,NULL,0,1,3,7,NULL,6,6),
	(7,NULL,NULL,0,1,3,8,NULL,7,7),
	(8,NULL,NULL,0,1,3,9,NULL,8,8),
	(9,NULL,NULL,0,1,3,10,NULL,9,9);

/*!40000 ALTER TABLE `AssetResource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table AssetTemplate
# ------------------------------------------------------------

DROP TABLE IF EXISTS `AssetTemplate`;

CREATE TABLE `AssetTemplate` (
  `assetTemplateId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `assetKind` varchar(2) NOT NULL,
  `isParent` bit(1) NOT NULL,
  `name` varchar(25) NOT NULL,
  `plan` varchar(25) DEFAULT NULL,
  `assetTypeID` bigint(20) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`assetTemplateId`),
  UNIQUE KEY `name` (`name`),
  KEY `FK2BDF5B6A795249C4` (`parentId`),
  KEY `FK2BDF5B6A2746F084` (`assetTypeID`),
  CONSTRAINT `FK2BDF5B6A2746F084` FOREIGN KEY (`assetTypeID`) REFERENCES `AssetType` (`assetTypeId`),
  CONSTRAINT `FK2BDF5B6A795249C4` FOREIGN KEY (`parentId`) REFERENCES `AssetTemplate` (`assetTemplateId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

LOCK TABLES `AssetTemplate` WRITE;
/*!40000 ALTER TABLE `AssetTemplate` DISABLE KEYS */;
INSERT INTO `AssetTemplate` (`assetTemplateId`,`archiveDate`,`lastChange`,`assetKind`,`isParent`,`name`,`plan`,`assetTypeID`,`parentId`)
VALUES
	(1,NULL,NULL,'PA',0,'Universal Template',NULL,1,NULL),
	(2,NULL,NULL,'WO',0,'Worker Template',NULL,2,NULL),
	(3,NULL,NULL,'PR',0,'Technician',NULL,3,2);

/*!40000 ALTER TABLE `AssetTemplate` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table AssetTimeShare
# ------------------------------------------------------------

DROP TABLE IF EXISTS `AssetTimeShare`;

CREATE TABLE `AssetTimeShare` (
  `assetTimeShareId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `asResource` bit(1) DEFAULT NULL,
  `isAssigned` bit(1) DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `assetId` bigint(20) DEFAULT NULL,
  `jobId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`assetTimeShareId`),
  KEY `FKDB6E14221165187C` (`jobId`),
  KEY `FKDB6E14229A688755` (`assetId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

LOCK TABLES `AssetTimeShare` WRITE;
/*!40000 ALTER TABLE `AssetTimeShare` DISABLE KEYS */;
INSERT INTO `AssetTimeShare` (`assetTimeShareId`,`archiveDate`,`lastChange`,`asResource`,`isAssigned`,`endDate`,`startDate`,`assetId`,`jobId`)
VALUES
	(3,NULL,NULL,1,1,'2011-06-09 00:00:00','2011-06-09 00:00:00',23,3),
	(4,NULL,NULL,1,1,'2011-06-09 00:00:00','2011-06-09 00:00:00',22,5),
	(5,NULL,NULL,1,1,'2011-06-09 00:00:00','2011-06-09 00:00:00',23,6),
	(6,NULL,NULL,1,1,'2011-06-09 00:00:00','2011-06-09 00:00:00',23,7),
	(7,NULL,NULL,1,1,'2011-06-09 00:00:00','2011-06-09 00:00:00',23,4);

/*!40000 ALTER TABLE `AssetTimeShare` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table AssetType
# ------------------------------------------------------------

DROP TABLE IF EXISTS `AssetType`;

CREATE TABLE `AssetType` (
  `assetTypeId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `classId` bigint(20) DEFAULT NULL,
  `code` int(11) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`assetTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

LOCK TABLES `AssetType` WRITE;
/*!40000 ALTER TABLE `AssetType` DISABLE KEYS */;
INSERT INTO `AssetType` (`assetTypeId`,`archiveDate`,`lastChange`,`classId`,`code`,`description`)
VALUES
	(1,NULL,NULL,1,1,'Universal Template'),
	(2,NULL,NULL,2,2,'Worker Template'),
	(3,NULL,NULL,3,3,'Proficiency Tempate');

/*!40000 ALTER TABLE `AssetType` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table CalEvent
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CalEvent`;

CREATE TABLE `CalEvent` (
  `calEventId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `assetCalendarId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`calEventId`),
  KEY `FKF77A178CF9963071` (`assetCalendarId`),
  CONSTRAINT `FKF77A178CF9963071` FOREIGN KEY (`assetCalendarId`) REFERENCES `AssetCalendar` (`assetCalendarId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table contact
# ------------------------------------------------------------

DROP TABLE IF EXISTS `contact`;

CREATE TABLE `contact` (
  `contactId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `fname` varchar(50) NOT NULL,
  `jobTitle` varchar(50) DEFAULT NULL,
  `lname` varchar(50) NOT NULL,
  `middle` varchar(50) DEFAULT NULL,
  `suffix` varchar(25) DEFAULT NULL,
  `rolodexCardId` bigint(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`contactId`),
  KEY `FK38B72420341750EB` (`rolodexCardId`),
  CONSTRAINT `FK38B72420341750EB` FOREIGN KEY (`rolodexCardId`) REFERENCES `Organization` (`orgId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
INSERT INTO `contact` (`contactId`,`archiveDate`,`lastChange`,`fname`,`jobTitle`,`lname`,`middle`,`suffix`,`rolodexCardId`,`email`,`phone`)
VALUES
	(1,NULL,NULL,'Mike','Sgt','Kent Titcomb',NULL,NULL,NULL,'kent@springhillair.com','841-2818'),
	(2,NULL,NULL,'Mark','Sgt','Vince Paully',NULL,NULL,NULL,'vince@springhillair.com','917-9962');

/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Context
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Context`;

CREATE TABLE `Context` (
  `contextID` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `taskTemplateID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`contextID`),
  UNIQUE KEY `contextID` (`contextID`),
  KEY `FK9BEFCD8FB74849E4` (`taskTemplateID`),
  CONSTRAINT `FK9BEFCD8FB74849E4` FOREIGN KEY (`taskTemplateID`) REFERENCES `TaskTemplate` (`taskTemplateId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table email
# ------------------------------------------------------------

DROP TABLE IF EXISTS `email`;

CREATE TABLE `email` (
  `emailId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `addrType` varchar(255) NOT NULL,
  `address` varchar(50) NOT NULL,
  `contactId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`emailId`),
  KEY `FK5C24B9C3ECAEA65` (`contactId`),
  CONSTRAINT `FK5C24B9C3ECAEA65` FOREIGN KEY (`contactId`) REFERENCES `contact` (`contactId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

LOCK TABLES `email` WRITE;
/*!40000 ALTER TABLE `email` DISABLE KEYS */;
INSERT INTO `email` (`emailId`,`archiveDate`,`lastChange`,`addrType`,`address`,`contactId`)
VALUES
	(1,NULL,NULL,'','castro@30300.navy.mil',1);

/*!40000 ALTER TABLE `email` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Job
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Job`;

CREATE TABLE `Job` (
  `jobId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `jobCat` varchar(255) DEFAULT NULL,
  `combleteBy` date DEFAULT NULL,
  `completedDate` date DEFAULT NULL,
  `createdBy` varchar(50) DEFAULT NULL,
  `createdDate` date DEFAULT NULL,
  `dis` int(11) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  `dispatchedDate` date DEFAULT NULL,
  `earliestStart` date DEFAULT NULL,
  `eom` varchar(255) DEFAULT NULL,
  `ero` varchar(255) DEFAULT NULL,
  `estTime` int(11) DEFAULT NULL,
  `jobType` varchar(255) DEFAULT NULL,
  `latestStart` date DEFAULT NULL,
  `note` longtext,
  `priority` varchar(255) DEFAULT NULL,
  `risd` date DEFAULT NULL,
  `scheduledDate` date DEFAULT NULL,
  `startedDate` date DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `sticky` bit(1) DEFAULT NULL,
  `totatEstTime` double DEFAULT NULL,
  `orgId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`jobId`),
  KEY `FK1239D90BBF968` (`orgId`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

LOCK TABLES `Job` WRITE;
/*!40000 ALTER TABLE `Job` DISABLE KEYS */;
INSERT INTO `Job` (`jobId`,`archiveDate`,`lastChange`,`jobCat`,`combleteBy`,`completedDate`,`createdBy`,`createdDate`,`dis`,`description`,`dispatchedDate`,`earliestStart`,`eom`,`ero`,`estTime`,`jobType`,`latestStart`,`note`,`priority`,`risd`,`scheduledDate`,`startedDate`,`status`,`sticky`,`totatEstTime`,`orgId`)
VALUES
	(1,NULL,NULL,'CAT10','2011-09-28',NULL,NULL,'2011-05-20',7,NULL,NULL,NULL,'E2','0BB43',3,'JT2',NULL,'','P8','2011-05-20',NULL,NULL,'JS1',0,3,1),
	(3,NULL,NULL,'CAT6','2011-09-28',NULL,NULL,'2011-05-27',0,NULL,NULL,NULL,'E2','0BB75',40,'JT2',NULL,NULL,'P6','2011-05-27',NULL,NULL,'JS6',0,4,1),
	(4,NULL,NULL,'CAT6','2011-09-28',NULL,NULL,'2011-05-24',3,NULL,NULL,NULL,'E2','0BB58',3,'JT1',NULL,NULL,'P6','2011-05-23',NULL,NULL,'JS6',0,8,1),
	(5,NULL,NULL,'CAT6','2011-09-28',NULL,NULL,'2011-05-27',0,NULL,NULL,NULL,'E2','0BB59',1,'JT2',NULL,NULL,'P13','2011-05-27',NULL,NULL,'JS6',0,1,1);

/*!40000 ALTER TABLE `Job` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table JobAction
# ------------------------------------------------------------

DROP TABLE IF EXISTS `JobAction`;

CREATE TABLE `JobAction` (
  `jobActionId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `fieldCondition` varchar(25) DEFAULT NULL,
  `modifier` varchar(25) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `note` varchar(125) DEFAULT NULL,
  `seq` varchar(5) DEFAULT NULL,
  `verb` varchar(50) DEFAULT NULL,
  `actionId` bigint(20) DEFAULT NULL,
  `jobTaskId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`jobActionId`),
  KEY `FK9B21A43342E511E6` (`jobTaskId`),
  KEY `FK9B21A433654EBEED` (`actionId`),
  CONSTRAINT `FK9B21A43342E511E6` FOREIGN KEY (`jobTaskId`) REFERENCES `JobTask` (`jobTaskId`),
  CONSTRAINT `FK9B21A433654EBEED` FOREIGN KEY (`actionId`) REFERENCES `Action` (`actionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table JobTask
# ------------------------------------------------------------

DROP TABLE IF EXISTS `JobTask`;

CREATE TABLE `JobTask` (
  `jobTaskId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `actualTime` int(11) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  `estTime` int(11) DEFAULT NULL,
  `taskCode` varchar(255) DEFAULT NULL,
  `jobId` bigint(20) DEFAULT NULL,
  `taskInstanceId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`jobTaskId`),
  KEY `FKD83B6C2A26BC9A8` (`taskInstanceId`),
  KEY `FKD83B6C21165187C` (`jobId`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

LOCK TABLES `JobTask` WRITE;
/*!40000 ALTER TABLE `JobTask` DISABLE KEYS */;
INSERT INTO `JobTask` (`jobTaskId`,`archiveDate`,`lastChange`,`actualTime`,`description`,`estTime`,`taskCode`,`jobId`,`taskInstanceId`)
VALUES
	(1,NULL,NULL,0,'100hr Inspection',7,'100hr',1,1),
	(2,NULL,NULL,0,'Landing light relamp',1,'LL Relamp',1,2),
	(3,NULL,NULL,0,'Oleo Strut service',1,'Oleo',1,3),
	(4,NULL,NULL,0,'Engine overhaul',40,'Major Engine',3,4),
	(5,NULL,NULL,0,'Main gear tire replacement',3,'Main Tires',4,5),
	(6,NULL,NULL,0,'Inspect fuel injection servo plug per AD',1,'2008-06-51',5,6),
	(7,NULL,NULL,0,'Inspect fuel injection servo plug per AD',2,'2008-06-51',12,7);

/*!40000 ALTER TABLE `JobTask` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table loginactivity
# ------------------------------------------------------------

DROP TABLE IF EXISTS `loginactivity`;

CREATE TABLE `loginactivity` (
  `loginActivityId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `date` date DEFAULT NULL,
  `loginTime` datetime NOT NULL,
  `logoutTime` datetime NOT NULL,
  `userID` bigint(20) NOT NULL,
  PRIMARY KEY (`loginActivityId`),
  KEY `FK62246978CA8F2AF7` (`userID`),
  CONSTRAINT `FK62246978CA8F2AF7` FOREIGN KEY (`userID`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table NonAssetResource
# ------------------------------------------------------------

DROP TABLE IF EXISTS `NonAssetResource`;

CREATE TABLE `NonAssetResource` (
  `nonAssetResourceId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `unitRefId` bigint(20) DEFAULT NULL,
  `jobId` bigint(20) DEFAULT NULL,
  `taskId` bigint(20) DEFAULT NULL,
  `taskTemplateId` bigint(20) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `stocknum` varchar(255) DEFAULT NULL,
  `currentLoc` varchar(255) DEFAULT NULL,
  `recDateStr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nonAssetResourceId`),
  KEY `FK2431A171B74849E4` (`taskTemplateId`),
  KEY `FK2431A171B0D5174B` (`taskId`),
  KEY `FK2431A1711165187C` (`jobId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

LOCK TABLES `NonAssetResource` WRITE;
/*!40000 ALTER TABLE `NonAssetResource` DISABLE KEYS */;
INSERT INTO `NonAssetResource` (`nonAssetResourceId`,`archiveDate`,`lastChange`,`description`,`quantity`,`tag`,`type`,`unitRefId`,`jobId`,`taskId`,`taskTemplateId`,`location`,`status`,`stocknum`,`currentLoc`,`recDateStr`)
VALUES
	(1,NULL,NULL,'Aeroshell 15W-50',7,'2540-01-385-9031','NA1',1,1,1,1,'Shop','Received 1275','2540-01-385-9031','Shop','2011-10-02'),
	(2,NULL,NULL,'Zeus fasteners, 50pk',1,'458022','NA1',0,1,1,1,'In transit','Shipped 1275','2540-01-300-8745','In transit','2011-10-03'),
	(3,NULL,NULL,'100W Sealed Beam',1,'LL-172-009','NA1',0,1,2,2,'Shop','Canceled 1275','4750-01-341-4912','Shop',NULL),
	(4,NULL,NULL,'Oleo rebuild kit (nose, 172)',1,'OLEO-KIT-4459','NA1',0,1,3,3,'Shop','Not ordered','2540-01-184-5503','Shop',NULL),
	(5,NULL,'2011-06-01 00:00:00','Lycoming O-320 rebuilt',1,'ORT9947','NA1',0,3,4,4,'Shop','Received 1277','ORT9947','Shop','2011-10-04'),
	(6,NULL,'2011-06-01 00:00:00','Michelin Aviator 6.00-6 Tire',2,'071-317-0','NA2',0,4,5,5,'Site','Received 1272','071-317-0','Site','2011-09-29'),
	(7,NULL,'2011-06-01 00:00:00','15/600-6 Aero Classic Leakguard Tube',2,'TU 15/600-6 ACL TU','NA2',0,4,5,5,'Site','Received 1272','TU 15/600-6 ACL TU','Site','2011-09-29');

/*!40000 ALTER TABLE `NonAssetResource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Organization
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Organization`;

CREATE TABLE `Organization` (
  `orgId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `orgType` varchar(2) NOT NULL,
  PRIMARY KEY (`orgId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

LOCK TABLES `Organization` WRITE;
/*!40000 ALTER TABLE `Organization` DISABLE KEYS */;
INSERT INTO `Organization` (`orgId`,`archiveDate`,`lastChange`,`name`,`orgType`)
VALUES
	(1,NULL,NULL,'Spring Hill Aviation','O1');

/*!40000 ALTER TABLE `Organization` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table phone
# ------------------------------------------------------------

DROP TABLE IF EXISTS `phone`;

CREATE TABLE `phone` (
  `phoneId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `ext` varchar(10) DEFAULT NULL,
  `number` varchar(10) DEFAULT NULL,
  `phoneType` varchar(255) NOT NULL,
  `contactId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`phoneId`),
  KEY `FK65B3D6E3ECAEA65` (`contactId`),
  CONSTRAINT `FK65B3D6E3ECAEA65` FOREIGN KEY (`contactId`) REFERENCES `contact` (`contactId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

LOCK TABLES `phone` WRITE;
/*!40000 ALTER TABLE `phone` DISABLE KEYS */;
INSERT INTO `phone` (`phoneId`,`archiveDate`,`lastChange`,`ext`,`number`,`phoneType`,`contactId`)
VALUES
	(1,NULL,NULL,NULL,'841-2818','',1);

/*!40000 ALTER TABLE `phone` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `roleId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `description` varchar(125) DEFAULT NULL,
  `name` varchar(25) NOT NULL,
  PRIMARY KEY (`roleId`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `name_2` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table Task
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Task`;

CREATE TABLE `Task` (
  `taskId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  `estTime` int(11) DEFAULT NULL,
  `expiryDays` int(11) DEFAULT NULL,
  `ExpiryType` varchar(255) NOT NULL,
  `freqDays` int(11) DEFAULT NULL,
  `freqMonths` int(11) DEFAULT NULL,
  `freqType` varchar(255) NOT NULL,
  `isActive` bit(1) DEFAULT NULL,
  `isStandAlone` bit(1) DEFAULT NULL,
  `lastPlannedDate` datetime DEFAULT NULL,
  `lastServicedDate` datetime DEFAULT NULL,
  `priority` varchar(255) NOT NULL,
  `runHoursThresh` double DEFAULT NULL,
  `runHoursThreshInc` double DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `taskCode` varchar(255) DEFAULT NULL,
  `taskType` varchar(255) NOT NULL,
  `taskTemplateId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`taskId`),
  KEY `FK27A9A5B74849E4` (`taskTemplateId`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

LOCK TABLES `Task` WRITE;
/*!40000 ALTER TABLE `Task` DISABLE KEYS */;
INSERT INTO `Task` (`taskId`,`archiveDate`,`lastChange`,`description`,`estTime`,`expiryDays`,`ExpiryType`,`freqDays`,`freqMonths`,`freqType`,`isActive`,`isStandAlone`,`lastPlannedDate`,`lastServicedDate`,`priority`,`runHoursThresh`,`runHoursThreshInc`,`startDate`,`taskCode`,`taskType`,`taskTemplateId`)
VALUES
	(1,NULL,NULL,'100hr Inspection',3,0,'ET5',0,0,'FT1',1,1,NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','100hr','TT1',1),
	(2,NULL,NULL,'Landing light relamp',3,0,'ET5',0,0,'FT1',1,1,NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','LL Relamp','TT1',2),
	(3,NULL,NULL,'Oleo Strut service',4,0,'ET5',0,0,'FT1',1,1,NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','Oleo','TT1',3),
	(4,NULL,NULL,'Engine overhaul',5,0,'ET5',0,0,'FT1',1,1,NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','Major Engine','TT1',4),
	(5,NULL,NULL,'Main gear tire replacement',4,0,'ET5',0,0,'FT1',1,1,NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','Main Tires','TT2',5),
	(6,NULL,NULL,'Inspect fuel injection servo per AD',2,0,'ET5',0,0,'FT1',1,1,NULL,NULL,'P6',NULL,NULL,'2011-05-01 00:00:00','2008-06-51','TT1',6);

/*!40000 ALTER TABLE `Task` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Task_Asset
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Task_Asset`;

CREATE TABLE `Task_Asset` (
  `taskId` bigint(20) NOT NULL,
  `assetId` bigint(20) NOT NULL,
  `maintainedAssets_assetId` bigint(20) DEFAULT NULL,
  `maintTasks_taskId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`taskId`,`assetId`),
  KEY `FKC24F77B68FE980AA` (`taskId`),
  KEY `FKC24F77B6BB541DF6` (`assetId`),
  KEY `FKC24F77B6656DDE77` (`maintTasks_taskId`),
  KEY `FKC24F77B6A3EBB883` (`maintainedAssets_assetId`),
  KEY `FKC24F77B6B0D5174B` (`taskId`),
  KEY `FKC24F77B69A688755` (`assetId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Task_Asset` WRITE;
/*!40000 ALTER TABLE `Task_Asset` DISABLE KEYS */;
INSERT INTO `Task_Asset` (`taskId`,`assetId`,`maintainedAssets_assetId`,`maintTasks_taskId`)
VALUES
	(1,1,NULL,NULL),
	(1,2,NULL,NULL),
	(1,3,NULL,NULL),
	(1,4,NULL,NULL),
	(2,1,NULL,NULL),
	(2,2,NULL,NULL),
	(2,3,NULL,NULL),
	(2,4,NULL,NULL),
	(3,1,NULL,NULL),
	(3,2,NULL,NULL),
	(3,3,NULL,NULL),
	(3,4,NULL,NULL),
	(4,1,NULL,NULL),
	(4,2,NULL,NULL),
	(4,3,NULL,NULL),
	(4,4,NULL,NULL),
	(5,1,NULL,NULL),
	(5,2,NULL,NULL),
	(5,3,NULL,NULL),
	(5,4,NULL,NULL),
	(6,1,NULL,NULL),
	(6,2,NULL,NULL),
	(6,3,NULL,NULL),
	(6,4,NULL,NULL),
	(6,5,NULL,NULL);

/*!40000 ALTER TABLE `Task_Asset` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table TaskInstance
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TaskInstance`;

CREATE TABLE `TaskInstance` (
  `taskInstanceId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `estTime` int(11) NOT NULL,
  `isStandAloneTask` bit(1) NOT NULL,
  `taskId` bigint(20) DEFAULT NULL,
  `taskTemplateId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`taskInstanceId`),
  KEY `FKCB7B2D5AB74849E4` (`taskTemplateId`),
  KEY `FKCB7B2D5AB0D5174B` (`taskId`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

LOCK TABLES `TaskInstance` WRITE;
/*!40000 ALTER TABLE `TaskInstance` DISABLE KEYS */;
INSERT INTO `TaskInstance` (`taskInstanceId`,`archiveDate`,`lastChange`,`estTime`,`isStandAloneTask`,`taskId`,`taskTemplateId`)
VALUES
	(1,NULL,NULL,3,1,1,NULL),
	(2,NULL,NULL,3,1,2,NULL),
	(3,NULL,NULL,4,1,3,NULL),
	(4,NULL,NULL,5,1,4,NULL),
	(5,NULL,NULL,4,1,5,NULL),
	(6,NULL,NULL,2,1,6,NULL),
	(7,NULL,NULL,2,1,7,NULL);

/*!40000 ALTER TABLE `TaskInstance` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table TaskTemplate
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TaskTemplate`;

CREATE TABLE `TaskTemplate` (
  `taskTemplateId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `estHours` int(11) DEFAULT NULL,
  `estMin` int(11) DEFAULT NULL,
  `estTime` int(11) DEFAULT NULL,
  `expNumDays` int(11) DEFAULT NULL,
  `ExpiryType` varchar(255) NOT NULL,
  `freqDays` int(11) DEFAULT NULL,
  `freqMonths` int(11) DEFAULT NULL,
  `freqType` varchar(255) NOT NULL,
  `priority` varchar(255) NOT NULL,
  `runHoursThresh` double DEFAULT NULL,
  `runHoursThreshInc` double DEFAULT NULL,
  `taskCode` varchar(255) DEFAULT NULL,
  `taskDescription` varchar(45) DEFAULT NULL,
  `taskType` varchar(255) NOT NULL,
  `assetTemplateId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`taskTemplateId`),
  KEY `FK5B9F653FA9585884` (`assetTemplateId`),
  CONSTRAINT `FK5B9F653FA9585884` FOREIGN KEY (`assetTemplateId`) REFERENCES `AssetTemplate` (`assetTemplateId`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

LOCK TABLES `TaskTemplate` WRITE;
/*!40000 ALTER TABLE `TaskTemplate` DISABLE KEYS */;
INSERT INTO `TaskTemplate` (`taskTemplateId`,`archiveDate`,`lastChange`,`estHours`,`estMin`,`estTime`,`expNumDays`,`ExpiryType`,`freqDays`,`freqMonths`,`freqType`,`priority`,`runHoursThresh`,`runHoursThreshInc`,`taskCode`,`taskDescription`,`taskType`,`assetTemplateId`)
VALUES
	(1,NULL,NULL,0,0,3,0,'ET5',0,0,'FT1','P6',NULL,NULL,'A17','100hr Inspection','TT1',1),
	(2,NULL,NULL,0,0,3,0,'ET5',0,0,'FT1','P6',NULL,NULL,'B17','Landing light relamp','TT1',1),
	(3,NULL,NULL,0,0,4,0,'ET5',0,0,'FT1','P6',NULL,NULL,'C22','Main gear tire replacement','TT1',1),
	(4,NULL,NULL,0,0,5,0,'ET5',0,0,'FT1','P6',NULL,NULL,'D34','Engine overhaul','TT1',1),
	(5,NULL,NULL,0,0,4,0,'ET5',0,0,'FT1','P6',NULL,NULL,'E34','Oleo Strut service','TT1',1),
	(6,NULL,NULL,0,0,3,0,'ET5',0,0,'FT1','P6',NULL,NULL,'F52','VOR Ground Check','TT2',1),
	(7,NULL,NULL,0,0,2,0,'ET5',0,0,'FT1','P6',NULL,NULL,'2008-06-51','Inspect fuel injection servo per AD','TT1',1);

/*!40000 ALTER TABLE `TaskTemplate` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table TenantRequest
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TenantRequest`;

CREATE TABLE `TenantRequest` (
  `tenantRequestId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `isUrgent` bit(1) DEFAULT NULL,
  `jobDescription` longtext,
  `jobstatus` varchar(255) NOT NULL,
  `locationComment` varchar(50) DEFAULT NULL,
  `note` longtext,
  `requestType` varchar(255) NOT NULL,
  `contactId` bigint(20) DEFAULT NULL,
  `jobId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tenantRequestId`),
  KEY `FK42249E653ECAEA65` (`contactId`),
  KEY `FK42249E651165187C` (`jobId`),
  CONSTRAINT `FK42249E651165187C` FOREIGN KEY (`jobId`) REFERENCES `Job` (`jobId`),
  CONSTRAINT `FK42249E653ECAEA65` FOREIGN KEY (`contactId`) REFERENCES `contact` (`contactId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `userId` bigint(20) NOT NULL AUTO_INCREMENT,
  `archiveDate` datetime DEFAULT NULL,
  `lastChange` datetime DEFAULT NULL,
  `email` varchar(45) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `fname` varchar(25) NOT NULL,
  `lname` varchar(50) NOT NULL,
  `password` varchar(25) NOT NULL,
  `uid` varchar(50) NOT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `uid` (`uid`),
  UNIQUE KEY `uid_2` (`uid`),
  UNIQUE KEY `email_2` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`userId`,`archiveDate`,`lastChange`,`email`,`enabled`,`fname`,`lname`,`password`,`uid`)
VALUES
	(1,NULL,NULL,'admin@admin.min',1,'admin','admin','admin','admin');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `FK143BF46A4E742300` (`role_id`),
  KEY `FK143BF46AF39EE6E0` (`user_id`),
  CONSTRAINT `FK143BF46A4E742300` FOREIGN KEY (`role_id`) REFERENCES `role` (`roleId`),
  CONSTRAINT `FK143BF46AF39EE6E0` FOREIGN KEY (`user_id`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;






/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
