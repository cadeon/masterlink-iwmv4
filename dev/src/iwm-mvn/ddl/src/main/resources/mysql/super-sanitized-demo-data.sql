/*
 * Demo Data Dump. Expects existing (but empty, aside from structure) db.
 */

use iwmDemo;


# Dump of table Organization
# ------------------------------------------------------------

LOCK TABLES `Organization` WRITE;
/*!40000 ALTER TABLE `Organization` DISABLE KEYS */;

INSERT INTO `Organization` (`orgId`, `archiveDate`, `lastChange`, `name`, `orgType`, `actCode`)
VALUES
	(1,NULL,NULL,'H','O1','20000'),
	(2,NULL,NULL,'A','O1','20000'),
	(3,NULL,NULL,'J','O1','20000'),
	(4,NULL,NULL,'K','O1','20000');

/*!40000 ALTER TABLE `Organization` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table AssetType
# ------------------------------------------------------------

LOCK TABLES `AssetType` WRITE;
/*!40000 ALTER TABLE `AssetType` DISABLE KEYS */;

INSERT INTO `AssetType` (`assetTypeId`, `archiveDate`, `lastChange`, `classId`, `code`, `description`)
VALUES
	(1,NULL,NULL,1,1,'Universal Template'),
	(2,NULL,NULL,2,2,'Worker Template'),
	(3,NULL,NULL,3,3,'Proficiency Template');

/*!40000 ALTER TABLE `AssetType` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table AssetTemplate
# ------------------------------------------------------------

LOCK TABLES `AssetTemplate` WRITE;
/*!40000 ALTER TABLE `AssetTemplate` DISABLE KEYS */;

INSERT INTO `AssetTemplate` (`assetTemplateId`, `archiveDate`, `lastChange`, `assetKind`, `name`, `plan`, `assetTypeID`, `parentId`)
VALUES
	(1, NULL, NULL, 'PA', 'Universal Template', NULL, 1, NULL),
	(2, NULL, NULL, 'WO', 'Worker Template', NULL, 2, NULL),
	(5, NULL, NULL, 'PR', '230SL maintenance course', NULL, 3, NULL),
	(6, NULL, NULL, 'PR', '380SL maintenance course', NULL, 3, NULL),
	(7, NULL, NULL, 'PR', '380SLC maintenance', NULL, 3, NULL),
	(8, NULL, NULL, 'PR', '500SL/SL500 maintenance', NULL, 3, NULL),
	(9, NULL, NULL, 'PR', 'SL55 AMG maintenance', NULL, 3, NULL),
	(10, NULL, NULL, 'PR', 'SLK230 maintenance', NULL, 3, NULL),
	(11, NULL, NULL, 'PR', 'CLK500 maintenance course', NULL, 3, NULL),
	(12, NULL, NULL, 'PR', '280SE Conv3.5 maintenance', NULL, 3, NULL);


/*!40000 ALTER TABLE `AssetTemplate` ENABLE KEYS */;
UNLOCK TABLES;

# Dump of table contact
# ------------------------------------------------------------

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;

INSERT INTO `contact` (`contactId`, `archiveDate`, `lastChange`, `email`, `fname`, `jobTitle`, `lname`, `middle`, `phone`, `suffix`, `orgId`)
VALUES
	(1,NULL,NULL,'first1.last1@usmc.mil','Mike','Sgt','Castro',NULL,'123-456-7890',NULL,4),
	(2,NULL,NULL,'first2.last2@usmc.mil','Mark','Sgt','Jones',NULL,'123-456-7891',NULL,2),
	(3,NULL,NULL,'first3.last3@usmc.mil','Levi','Sgt','Wong',NULL,'123-456-7892',NULL,3),
	(4,NULL,NULL,'first4.last4@usmc.mil','Jake','Sgt','DeLongue',NULL,'123-456-7893',NULL,4),
	(5,NULL,NULL,'first5.last5@usmc.mil','Lauren','Sgt','Estaban',NULL,'123-456-7894',NULL,2),
	(6,NULL,NULL,'first6.last6@usmc.mil','Lucy','Sgt','Mitchell',NULL,'123-456-7895',NULL,3),
	(7,NULL,NULL,'first7.last7@usmc.mil','Daniel','Sgt','Blumenfeld',NULL,'123-456-7896',NULL,4),
	(8,NULL,NULL,'first8.last8@usmc.mil','Mitch','Sgt','Jones',NULL,'123-456-7897',NULL,2),
	(9,NULL,NULL,'first9.last9@usmc.mil','Zachary','Sgt','Walken',NULL,'123-456-7898',NULL,3),
	(10,NULL,NULL,'first0.last0@usmc.mil','Jim','Sgt','Dandy',NULL,'123-456-7899',NULL,4),
	(11,NULL,NULL,'firsta.lasta@usmc.mil',NULL,NULL,'Jones',NULL,'123-456-7891',NULL,2),
	(12,NULL,NULL,'firstb.lastb@usmc.mil',NULL,NULL,'Castro',NULL,'123-456-7890',NULL,4),
	(13,NULL,NULL,'firstc.lastc@usmc.mil',NULL,NULL,'Jones',NULL,'123-456-7891',NULL,2),
	(14,NULL,NULL,'firstd.lastd@usmc.mil',NULL,NULL,'Wong',NULL,'123-456-7892',NULL,3),
	(15,NULL,NULL,'firste.laste@usmc.mil',NULL,NULL,'Castro',NULL,'123-456-7890',NULL,4),
	(16,NULL,NULL,'firstf.lastf@usmc.mil',NULL,NULL,'Jones',NULL,'123-456-7891',NULL,2),
	(17,NULL,NULL,'firstg.lastg@usmc.mil',NULL,NULL,'Wong',NULL,'123-456-7892',NULL,3),
	(18,NULL,NULL,'firsth.lasth@usmc.mil',NULL,NULL,'Winter',NULL,'123-456-7893',NULL,3);

/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `Asset` WRITE;
/*!40000 ALTER TABLE `Asset` DISABLE KEYS */;

INSERT INTO `Asset` (`assetId`, `archiveDate`, `lastChange`, `assetKind`, `createdDate`, `hasCustomData`, `hasCustomTask`, `isActive`, `isCustom`, `isStickyResource`, `name`, `runHours`, `startDate`, `tag`, `assetTemplateId`, `assetTypeId`, `orgId`, `parentId`, `taskId`, `contactId`)
VALUES
	(1,NULL,NULL,'PA',NULL,b'0',b'0',b'1',b'0',b'0',NULL,0,NULL,'12345C',1,1,2,NULL,NULL,1),
	(2,NULL,NULL,'PA',NULL,b'0',b'0',b'1',b'0',b'0',NULL,0,NULL,'12345A',1,1,3,NULL,NULL,2),
	(3,NULL,NULL,'PA',NULL,b'0',b'0',b'1',b'0',b'0',NULL,0,NULL,'12345B',1,1,4,NULL,NULL,3),
	(4,NULL,NULL,'PA',NULL,b'0',b'0',b'1',b'0',b'0',NULL,0,NULL,'12345X',1,1,2,NULL,NULL,4),
	(5,NULL,NULL,'PA',NULL,b'0',b'0',b'1',b'0',b'0',NULL,0,NULL,'12345J',1,1,3,NULL,NULL,5),
	(6,NULL,NULL,'PA',NULL,b'0',b'0',b'1',b'0',b'0',NULL,0,NULL,'12345S',1,1,4,NULL,NULL,6),
	(7,NULL,NULL,'PA',NULL,b'0',b'0',b'1',b'0',b'0',NULL,0,NULL,'12345M',1,1,2,NULL,NULL,7),
	(8,NULL,NULL,'PA',NULL,b'0',b'0',b'1',b'0',b'0',NULL,0,NULL,'12345Q',1,1,3,NULL,NULL,8),
	(9,NULL,NULL,'PA',NULL,b'0',b'0',b'1',b'0',b'0',NULL,0,NULL,'12345H',1,1,4,NULL,NULL,9),
	(10,NULL,NULL,'PA',NULL,b'0',b'0',b'1',b'0',b'0',NULL,0,NULL,'12345L',1,1,2,NULL,NULL,10),
	(22,NULL,NULL,'WO',NULL,b'0',b'0',b'1',b'0',b'0','Franco',0,NULL,NULL,2,2,1,NULL,NULL,NULL),
	(23,NULL,NULL,'WO',NULL,b'0',b'0',b'1',b'0',b'0','Jones',0,NULL,NULL,2,2,1,NULL,NULL,NULL),
	(24,NULL,NULL,'WO',NULL,b'0',b'0',b'1',b'0',b'0','Rivers',0,NULL,NULL,2,2,1,NULL,NULL,NULL),
	(25,NULL,NULL,'WO',NULL,b'0',b'0',b'1',b'0',b'0','Smith',0,NULL,NULL,2,2,1,NULL,NULL,NULL),
	(26,NULL,NULL,'WO',NULL,b'0',b'0',b'1',b'0',b'0','Abbot',0,NULL,NULL,2,2,1,NULL,NULL,NULL),
	(27,NULL,NULL,'WO',NULL,b'0',b'0',b'1',b'0',b'0','Wong',0,NULL,NULL,2,2,1,NULL,NULL,NULL),
	(28,NULL,NULL,'WO',NULL,b'0',b'0',b'1',b'0',b'0','Gomez',0,NULL,NULL,2,2,1,NULL,NULL,NULL),
	(29,NULL,NULL,'WO',NULL,b'0',b'0',b'1',b'0',b'0','Carter',0,NULL,NULL,2,2,1,NULL,NULL,NULL),
	(30,NULL,NULL,'WO',NULL,b'0',b'0',b'1',b'0',b'0','Hopper',0,NULL,NULL,2,2,1,NULL,NULL,NULL),
	(31,NULL,NULL,'WO',NULL,b'0',b'0',b'1',b'0',b'0','Fillion',0,NULL,NULL,2,2,1,NULL,NULL,NULL),
	(32,NULL,NULL,'PR',NULL,b'0',b'0',b'1',b'0',b'0','230SL maintenance course',0,NULL,'230SL maintenance course',5,3,1,22,NULL,NULL),
	(33,NULL,NULL,'PR',NULL,b'0',b'0',b'1',b'0',b'0','380SL maintenance course',0,NULL,'380SL maintenance course',6,3,1,23,NULL,NULL),
	(34,NULL,NULL,'PR',NULL,b'0',b'0',b'1',b'0',b'0','380SLC maintenance',0,NULL,'380SLC maintenance',7,3,1,24,NULL,NULL),
	(35,NULL,NULL,'PR',NULL,b'0',b'0',b'1',b'0',b'0','500SL/SL500 maintenance',0,NULL,'500SL/SL500 maintenance',8,3,1,25,NULL,NULL),
	(36,NULL,NULL,'PR',NULL,b'0',b'0',b'1',b'0',b'0','SL55 AMG maintenance',0,NULL,'SL55 AMG maintenance',9,3,1,26,NULL,NULL),
	(37,NULL,NULL,'PR',NULL,b'0',b'0',b'1',b'0',b'0','SLK230 maintenance',0,NULL,'SLK230 maintenance',10,3,1,27,NULL,NULL),
	(38,NULL,NULL,'PR',NULL,b'0',b'0',b'1',b'0',b'0','230SL maintenance course',0,NULL,'230SL maintenance course',5,3,1,28,NULL,NULL),
	(39,NULL,NULL,'PR',NULL,b'0',b'0',b'1',b'0',b'0','230SL maintenance course',0,NULL,'230SL maintenance course',5,3,1,29,NULL,NULL),
	(40,NULL,NULL,'PR',NULL,b'0',b'0',b'1',b'0',b'0','380SL maintenance course',0,NULL,'380SL maintenance course',6,3,1,30,NULL,NULL),
	(41,NULL,NULL,'PR',NULL,b'0',b'0',b'1',b'0',b'0','500SL/SL500 maintenance',0,NULL,'500SL/SL500 maintenance',8,3,1,28,NULL,NULL),
	(42,NULL,NULL,'PR',NULL,b'0',b'0',b'1',b'0',b'0','380SL maintenance course',0,NULL,'380SL maintenance course',6,3,1,31,NULL,NULL);

/*!40000 ALTER TABLE `Asset` ENABLE KEYS */;
UNLOCK TABLES;

# Dump of table AssetAttributeTemplate
# ------------------------------------------------------------

LOCK TABLES `AssetAttributeTemplate` WRITE;
/*!40000 ALTER TABLE `AssetAttributeTemplate` DISABLE KEYS */;

INSERT INTO `AssetAttributeTemplate` (`assetAttributeTemplateId`, `archiveDate`, `lastChange`, `name`, `value`, `assetTemplateId`)
VALUES
	(1, NULL, NULL, 'TAM', NULL, 1),
	(2, NULL, NULL, 'ID', NULL, 1),
	(3, NULL, NULL, 'OWNER', NULL, 1),
	(4, NULL, NULL, 'NSN', NULL, 1),
	(5, NULL, NULL, 'MODEL', NULL, 1),
	(6, NULL, NULL, 'RANK', NULL, 2),
	(7, NULL, NULL, 'EMAIL', NULL, 2),
	(8, NULL, NULL, 'PHONE', NULL, 2);


/*!40000 ALTER TABLE `AssetAttributeTemplate` ENABLE KEYS */;
UNLOCK TABLES;

# Dump of table AssetAttribute
# ------------------------------------------------------------

LOCK TABLES `AssetAttribute` WRITE;
/*!40000 ALTER TABLE `AssetAttribute` DISABLE KEYS */;

INSERT INTO `AssetAttribute` (`assetAttributeId`, `archiveDate`, `lastChange`, `name`, `value`, `assetAttributeTemplateId`, `assetId`)
VALUES
	(1, NULL, NULL, 'TAM', 'DXXXC', 1, 1),
	(2, NULL, NULL, 'ID', 'IXXXXC', 2, 1),
	(5, NULL, NULL, 'MODEL', '380SL', 5, 1),
	(6, NULL, NULL, 'TAM', 'DXXXA', 1, 2),
	(7, NULL, NULL, 'ID', '1XXXXA', 2, 2),
	(8, NULL, NULL, 'MODEL', 'SLK230', 5, 2),
	(9, NULL, NULL, 'TAM', 'DXXXB', 1, 3),
	(10, NULL, NULL, 'ID', '1XXXXB', 2, 3),
	(11, NULL, NULL, 'MODEL', '230SL', 5, 3),
	(12, NULL, NULL, 'TAM', 'DXXXX', 1, 4),
	(13, NULL, NULL, 'ID', '1XXXXX', 2, 4),
	(14, NULL, NULL, 'MODEL', '380SLC', 5, 4),
	(15, NULL, NULL, 'TAM', 'DXXXJ', 1, 5),
	(16, NULL, NULL, 'ID', '1XXXXJ', 2, 5),
	(17, NULL, NULL, 'MODEL', '500SL/SL500', 5, 5),
	(18, NULL, NULL, 'TAM', 'DXXXS', 1, 6),
	(19, NULL, NULL, 'ID', '1XXXXS', 2, 6),
	(20, NULL, NULL, 'MODEL', 'SL55 AMG', 5, 6),
	(22, NULL, NULL, 'TAM', 'DXXXC', 1, 7),
	(23, NULL, NULL, 'ID', '1XXXXC', 2, 7),
	(24, NULL, NULL, 'MODEL', '380SL', 5, 7),
	(25, NULL, NULL, 'TAM', 'DXXXB', 1, 8),
	(26, NULL, NULL, 'ID', '1XXXXB', 2, 8),
	(27, NULL, NULL, 'MODEL', '230SL', 5, 8),
	(28, NULL, NULL, 'TAM', 'DXXXH', 1, 9),
	(29, NULL, NULL, 'ID', '1XXXXH', 2, 9),
	(30, NULL, NULL, 'MODEL', 'CLK500', 5, 9),
	(31, NULL, NULL, 'TAM', 'DXXXL', 1, 10),
	(32, NULL, NULL, 'ID', '1XXXXL', 2, 10),
	(33, NULL, NULL, 'MODEL', '280SE Conv3.5', 5, 10),
	(34, NULL, NULL, 'RANK', 'Sgt', 6, 22),
	(35, NULL, NULL, 'RANK', 'Sgt', 6, 23),
	(36, NULL, NULL, 'RANK', 'Cpl', 6, 24),
	(37, NULL, NULL, 'RANK', 'LCpl', 6, 25),
	(38, NULL, NULL, 'RANK', 'Sgt', 6, 26),
	(39, NULL, NULL, 'RANK', 'Sgt', 6, 27),
	(40, NULL, NULL, 'RANK', 'Sgt', 6, 28),
	(41, NULL, NULL, 'RANK', 'LCpl', 6, 29),
	(42, NULL, NULL, 'RANK', 'Sgt', 6, 30),
	(43, NULL, NULL, 'RANK', 'LCpl', 6, 31),
	(44, NULL, NULL, 'EMAIL', '', 7, 22),
	(45, NULL, NULL, 'PHONE', '', 8, 22),
	(46, NULL, NULL, 'EMAIL', '', 7, 23),
	(47, NULL, NULL, 'PHONE', '', 8, 23),
	(48, NULL, NULL, 'EMAIL', '', 7, 24),
	(49, NULL, NULL, 'PHONE', '', 8, 24),
	(50, NULL, NULL, 'EMAIL', '', 7, 25),
	(51, NULL, NULL, 'PHONE', '', 8, 25),
	(52, NULL, NULL, 'EMAIL', '', 7, 26),
	(53, NULL, NULL, 'PHONE', '', 8, 26),
	(54, NULL, NULL, 'EMAIL', '', 7, 27),
	(55, NULL, NULL, 'PHONE', '', 8, 27),
	(56, NULL, NULL, 'EMAIL', '', 7, 28),
	(57, NULL, NULL, 'PHONE', '', 8, 28),
	(58, NULL, NULL, 'EMAIL', '', 7, 29),
	(59, NULL, NULL, 'PHONE', '', 8, 29),
	(60, NULL, NULL, 'EMAIL', '', 7, 30),
	(61, NULL, NULL, 'PHONE', '', 8, 30),
	(62, NULL, NULL, 'EMAIL', '', 7, 31),
	(63, NULL, NULL, 'PHONE', '', 8, 31);


/*!40000 ALTER TABLE `AssetAttribute` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table AssetNotAvail
# ------------------------------------------------------------


# Dump of table TaskTemplate
# ------------------------------------------------------------

LOCK TABLES `TaskTemplate` WRITE;
/*!40000 ALTER TABLE `TaskTemplate` DISABLE KEYS */;

INSERT INTO `TaskTemplate` (`taskTemplateId`, `archiveDate`, `lastChange`, `estHours`, `estMin`, `estTime`, `expNumDays`, `ExpiryType`, `freqDays`, `freqMonths`, `freqType`, `priority`, `runHoursThresh`, `runHoursThreshInc`, `taskCode`, `taskDescription`, `taskType`, `assetTemplateId`)
VALUES
	(1,NULL,NULL,0,0,100,0,'ET5',0,0,'FT1','P6',NULL,NULL,'X17','CM: Rplc power steering pump ','TT1',1),
	(2,NULL,NULL,0,0,90,0,'ET5',0,0,'FT1','P6',NULL,NULL,'X22','CM: Rplc. pwr steering hose','TT1',1),
	(3,NULL,NULL,0,0,15,0,'ET5',0,0,'FT1','P6',NULL,NULL,'X34','CM: Rplc. water spray nozzle','TT1',1),
	(4,NULL,NULL,0,0,160,0,'ET5',0,0,'FT1','P6',NULL,NULL,'X34','CM: Rplc rear axle','TT1',1),
	(5,NULL,NULL,0,0,240,0,'ET5',0,0,'FT1','P6',NULL,NULL,'X52','PM: Annual PMCS','TT2',1),
	(6,NULL,NULL,0,0,240,0,'ET5',0,0,'FT1','P6',NULL,NULL,'X16','CM: Rplc. transmission seals','TT1',1),
	(7,NULL,NULL,0,0,120,0,'ET5',0,0,'FT1','P6',NULL,NULL,'X17','CM: Rplc. water pump','TT1',1),
	(8,NULL,NULL,0,0,25,0,'ET5',0,0,'FT1','P6',NULL,NULL,'X11','CM: Rplc. water spray hose','TT1',1),
	(9,NULL,NULL,0,0,45,0,'ET5',0,0,'FT1','P6',NULL,NULL,'X31','CM: Rplc shocks','TT1',1),
	(10,NULL,NULL,0,0,45,0,'ET5',0,0,'FT1','P6',NULL,NULL,'X18','CM: Rplc water valve','TT1',1),
	(11,NULL,NULL,0,0,70,0,'ET5',0,0,'FT1','P6',NULL,NULL,'X19','CM: Rplc AC compressor','TT1',1),
	(12,NULL,NULL,0,0,240,0,'ET5',0,0,'FT7','P6',NULL,NULL,'X53','PM: Semi-Annual PMCS','TT2',1);

/*!40000 ALTER TABLE `TaskTemplate` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table Task
# ------------------------------------------------------------

LOCK TABLES `Task` WRITE;
/*!40000 ALTER TABLE `Task` DISABLE KEYS */;

INSERT INTO `Task` (`taskId`, `archiveDate`, `lastChange`, `description`, `estTime`, `expiryDays`, `ExpiryType`, `freqDays`, `freqMonths`, `freqType`, `isActive`, `isStandAlone`, `lastPlannedDate`, `lastServicedDate`, `priority`, `runHoursThresh`, `runHoursThreshInc`, `startDate`, `taskCode`, `taskType`, `taskTemplateId`)
VALUES
	(1, NULL, NULL, 'CM: Rplc power steering pump ', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 1),
	(2, NULL, NULL, 'CM: Rplc. pwr steering hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X22', 'TT1', 2),
	(3, NULL, NULL, 'CM: Rplc. water spray nozzle', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 3),
	(4, NULL, NULL, 'CM: Rplc rear axle', 6, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 4),
	(5, NULL, NULL, 'PM: Annual PMCS', 8, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X52', 'TT2', 5),
	(6, NULL, NULL, 'CM: Rplc. transmission seals', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X16', 'TT1', 6),
	(7, NULL, NULL, 'CM: Rplc. water pump', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 7),
	(8, NULL, NULL, 'CM: Rplc. water spray hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X11', 'TT1', 8),
	(9, NULL, NULL, 'CM: Rplc shocks', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X31', 'TT1', 9),
	(10, NULL, NULL, 'CM: Rplc water valve', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X18', 'TT1', 10),
	(11, NULL, NULL, 'CM: Rplc AC compressor', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X19', 'TT1', 11),
	(12, NULL, NULL, 'PM: Semi-Annual PMCS', 2, 0, 'ET5', 0, 0, 'FT7', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X53', 'TT2', 12),
	(13, NULL, NULL, 'CM: Rplc power steering pump', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 1),
	(14, NULL, NULL, 'CM: Rplc. pwr steering hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X22', 'TT1', 2),
	(15, NULL, NULL, 'CM: Rplc. water spray nozzle', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 3),
	(16, NULL, NULL, 'CM: Rplc rear axle', 6, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 4),
	(17, NULL, NULL, 'PM: Annual PMCS', 8, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X52', 'TT2', 5),
	(18, NULL, NULL, 'CM: Rplc. transmission seals', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X16', 'TT1', 6),
	(19, NULL, NULL, 'CM: Rplc. water pump', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 7),
	(20, NULL, NULL, 'CM: Rplc. water spray hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X11', 'TT1', 8),
	(21, NULL, NULL, 'CM: Rplc shocks', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X31', 'TT1', 9),
	(22, NULL, NULL, 'CM: Rplc water valve', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X18', 'TT1', 10),
	(23, NULL, NULL, 'CM: Rplc AC compressor', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X19', 'TT1', 11),
	(24, NULL, NULL, 'PM: Semi-Annual PMCS', 2, 0, 'ET5', 0, 0, 'FT7', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X53', 'TT2', 12),
	(28, NULL, NULL, 'CM: Rplc power steering pump ', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 1),
	(29, NULL, NULL, 'CM: Rplc. pwr steering hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X22', 'TT1', 2),
	(30, NULL, NULL, 'CM: Rplc. water spray nozzle', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 3),
	(31, NULL, NULL, 'CM: Rplc rear axle', 6, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 4),
	(32, NULL, NULL, 'PM: Annual PMCS', 8, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X52', 'TT2', 5),
	(33, NULL, NULL, 'CM: Rplc. transmission seals', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X16', 'TT1', 6),
	(34, NULL, NULL, 'CM: Rplc. water pump', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 7),
	(35, NULL, NULL, 'CM: Rplc. water spray hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X11', 'TT1', 8),
	(36, NULL, NULL, 'CM: Rplc shocks', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X31', 'TT1', 9),
	(37, NULL, NULL, 'CM: Rplc water valve', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X18', 'TT1', 10),
	(38, NULL, NULL, 'CM: Rplc AC compressor', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X19', 'TT1', 11),
	(39, NULL, NULL, 'PM: Semi-Annual PMCS', 2, 0, 'ET5', 0, 0, 'FT7', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X53', 'TT2', 12),
	(43, NULL, NULL, 'CM: Rplc power steering pump ', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 1),
	(44, NULL, NULL, 'CM: Rplc. pwr steering hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X22', 'TT1', 2),
	(45, NULL, NULL, 'CM: Rplc. water spray nozzle', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 3),
	(46, NULL, NULL, 'CM: Rplc rear axle', 6, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 4),
	(47, NULL, NULL, 'PM: Annual PMCS', 8, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X52', 'TT2', 5),
	(48, NULL, NULL, 'CM: Rplc. transmission seals', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X16', 'TT1', 6),
	(49, NULL, NULL, 'CM: Rplc. water pump', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 7),
	(50, NULL, NULL, 'CM: Rplc. water spray hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X11', 'TT1', 8),
	(51, NULL, NULL, 'CM: Rplc shocks', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X31', 'TT1', 9),
	(52, NULL, NULL, 'CM: Rplc water valve', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X18', 'TT1', 10),
	(53, NULL, NULL, 'CM: Rplc AC compressor', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X19', 'TT1', 11),
	(54, NULL, NULL, 'PM: Semi-Annual PMCS', 2, 0, 'ET5', 0, 0, 'FT7', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X53', 'TT2', 12),
	(58, NULL, NULL, 'CM: Rplc power steering pump ', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 1),
	(59, NULL, NULL, 'CM: Rplc. pwr steering hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X22', 'TT1', 2),
	(60, NULL, NULL, 'CM: Rplc. water spray nozzle', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 3),
	(61, NULL, NULL, 'CM: Rplc rear axle', 6, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 4),
	(62, NULL, NULL, 'PM: Annual PMCS', 8, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X52', 'TT2', 5),
	(63, NULL, NULL, 'CM: Rplc. transmission seals', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X16', 'TT1', 6),
	(64, NULL, NULL, 'CM: Rplc. water pump', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 7),
	(65, NULL, NULL, 'CM: Rplc. water spray hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X11', 'TT1', 8),
	(66, NULL, NULL, 'CM: Rplc shocks', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X31', 'TT1', 9),
	(67, NULL, NULL, 'CM: Rplc water valve', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X18', 'TT1', 10);

INSERT INTO `Task` (`taskId`, `archiveDate`, `lastChange`, `description`, `estTime`, `expiryDays`, `ExpiryType`, `freqDays`, `freqMonths`, `freqType`, `isActive`, `isStandAlone`, `lastPlannedDate`, `lastServicedDate`, `priority`, `runHoursThresh`, `runHoursThreshInc`, `startDate`, `taskCode`, `taskType`, `taskTemplateId`)
VALUES
	(68, NULL, NULL, 'CM: Rplc AC compressor', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X19', 'TT1', 11),
	(69, NULL, NULL, 'PM: Semi-Annual PMCS', 2, 0, 'ET5', 0, 0, 'FT7', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X53', 'TT2', 12),
	(73, NULL, NULL, 'CM: Rplc power steering pump ', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 1),
	(74, NULL, NULL, 'CM: Rplc. pwr steering hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X22', 'TT1', 2),
	(75, NULL, NULL, 'CM: Rplc. water spray nozzle', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 3),
	(76, NULL, NULL, 'CM: Rplc rear axle', 6, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 4),
	(77, NULL, NULL, 'PM: Annual PMCS', 8, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X52', 'TT2', 5),
	(78, NULL, NULL, 'CM: Rplc. transmission seals', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X16', 'TT1', 6),
	(79, NULL, NULL, 'CM: Rplc. water pump', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 7),
	(80, NULL, NULL, 'CM: Rplc. water spray hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X11', 'TT1', 8),
	(81, NULL, NULL, 'CM: Rplc shocks', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X31', 'TT1', 9),
	(82, NULL, NULL, 'CM: Rplc water valve', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X18', 'TT1', 10),
	(83, NULL, NULL, 'CM: Rplc AC compressor', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X19', 'TT1', 11),
	(84, NULL, NULL, 'PM: Semi-Annual PMCS', 2, 0, 'ET5', 0, 0, 'FT7', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X53', 'TT2', 12),
	(88, NULL, NULL, 'CM: Rplc power steering pump ', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 1),
	(89, NULL, NULL, 'CM: Rplc. pwr steering hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X22', 'TT1', 2),
	(90, NULL, NULL, 'CM: Rplc. water spray nozzle', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 3),
	(91, NULL, NULL, 'CM: Rplc rear axle', 6, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 4),
	(92, NULL, NULL, 'PM: Annual PMCS', 8, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X52', 'TT2', 5),
	(93, NULL, NULL, 'CM: Rplc. transmission seals', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X16', 'TT1', 6),
	(94, NULL, NULL, 'CM: Rplc. water pump', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 7),
	(95, NULL, NULL, 'CM: Rplc. water spray hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X11', 'TT1', 8),
	(96, NULL, NULL, 'CM: Rplc shocks', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X31', 'TT1', 9),
	(97, NULL, NULL, 'CM: Rplc water valve', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X18', 'TT1', 10),
	(98, NULL, NULL, 'CM: Rplc AC compressor', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X19', 'TT1', 11),
	(99, NULL, NULL, 'PM: Semi-Annual PMCS', 2, 0, 'ET5', 0, 0, 'FT7', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X53', 'TT2', 12),
	(103, NULL, NULL, 'CM: Rplc power steering pump ', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 1),
	(104, NULL, NULL, 'CM: Rplc. pwr steering hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X22', 'TT1', 2),
	(105, NULL, NULL, 'CM: Rplc. water spray nozzle', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 3),
	(106, NULL, NULL, 'CM: Rplc rear axle', 6, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 4),
	(107, NULL, NULL, 'PM: Annual PMCS', 8, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X52', 'TT2', 5),
	(108, NULL, NULL, 'CM: Rplc. transmission seals', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X16', 'TT1', 6),
	(109, NULL, NULL, 'CM: Rplc. water pump', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 7),
	(110, NULL, NULL, 'CM: Rplc. water spray hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X11', 'TT1', 8),
	(111, NULL, NULL, 'CM: Rplc shocks', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X31', 'TT1', 9),
	(112, NULL, NULL, 'CM: Rplc water valve', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X18', 'TT1', 10),
	(113, NULL, NULL, 'CM: Rplc AC compressor', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X19', 'TT1', 11),
	(114, NULL, NULL, 'PM: Semi-Annual PMCS', 2, 0, 'ET5', 0, 0, 'FT7', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X53', 'TT2', 12),
	(118, NULL, NULL, 'CM: Rplc power steering pump ', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 1),
	(119, NULL, NULL, 'CM: Rplc. pwr steering hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X22', 'TT1', 2),
	(120, NULL, NULL, 'CM: Rplc. water spray nozzle', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 3),
	(121, NULL, NULL, 'CM: Rplc rear axle', 6, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 4),
	(122, NULL, NULL, 'PM: Annual PMCS', 8, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X52', 'TT2', 5),
	(123, NULL, NULL, 'CM: Rplc. transmission seals', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X16', 'TT1', 6),
	(124, NULL, NULL, 'CM: Rplc. water pump', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 7),
	(125, NULL, NULL, 'CM: Rplc. water spray hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X11', 'TT1', 8),
	(126, NULL, NULL, 'CM: Rplc shocks', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X31', 'TT1', 9),
	(127, NULL, NULL, 'CM: Rplc water valve', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X18', 'TT1', 10),
	(128, NULL, NULL, 'CM: Rplc AC compressor', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X19', 'TT1', 11),
	(129, NULL, NULL, 'PM: Semi-Annual PMCS', 2, 0, 'ET5', 0, 0, 'FT7', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X53', 'TT2', 12),
	(133, NULL, NULL, 'CM: Rplc power steering pump ', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 1),
	(134, NULL, NULL, 'CM: Rplc. pwr steering hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X22', 'TT1', 2),
	(135, NULL, NULL, 'CM: Rplc. water spray nozzle', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 3),
	(136, NULL, NULL, 'CM: Rplc rear axle', 6, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X34', 'TT1', 4),
	(137, NULL, NULL, 'PM: Annual PMCS', 8, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X52', 'TT2', 5),
	(138, NULL, NULL, 'CM: Rplc. transmission seals', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X16', 'TT1', 6),
	(139, NULL, NULL, 'CM: Rplc. water pump', 2, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X17', 'TT1', 7),
	(140, NULL, NULL, 'CM: Rplc. water spray hose', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X11', 'TT1', 8);

INSERT INTO `Task` (`taskId`, `archiveDate`, `lastChange`, `description`, `estTime`, `expiryDays`, `ExpiryType`, `freqDays`, `freqMonths`, `freqType`, `isActive`, `isStandAlone`, `lastPlannedDate`, `lastServicedDate`, `priority`, `runHoursThresh`, `runHoursThreshInc`, `startDate`, `taskCode`, `taskType`, `taskTemplateId`)
VALUES
	(141, NULL, NULL, 'CM: Rplc shocks', 3, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X31', 'TT1', 9),
	(142, NULL, NULL, 'CM: Rplc water valve', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X18', 'TT1', 10),
	(143, NULL, NULL, 'CM: Rplc AC compressor', 1, 0, 'ET5', 0, 0, 'FT1', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X19', 'TT1', 11),
	(144, NULL, NULL, 'PM: Semi-Annual PMCS', 2, 0, 'ET5', 0, 0, 'FT7', 1, 1, NULL, NULL, 'P6', NULL, NULL, '2011-05-01 00:00:00', 'X53', 'TT2', 12);


/*!40000 ALTER TABLE `Task` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Task_Asset
# ------------------------------------------------------------

LOCK TABLES `Task_Asset` WRITE;
/*!40000 ALTER TABLE `Task_Asset` DISABLE KEYS */;

INSERT INTO `Task_Asset` (`assetId`, `taskId`)
VALUES
	(1, 1),
	(1, 2),
	(1, 3),
	(1, 4),
	(1, 5),
	(1, 6),
	(1, 7),
	(1, 8),
	(1, 9),
	(1, 10),
	(1, 11),
	(1, 12),
	(2, 13),
	(2, 14),
	(2, 15),
	(2, 16),
	(2, 17),
	(2, 18),
	(2, 19),
	(2, 20),
	(2, 21),
	(2, 22),
	(2, 23),
	(2, 24),
	(3, 28),
	(3, 29),
	(3, 30),
	(3, 31),
	(3, 32),
	(3, 33),
	(3, 34),
	(3, 35),
	(3, 36),
	(3, 37),
	(3, 38),
	(3, 39),
	(4, 43),
	(4, 44),
	(4, 45),
	(4, 46),
	(4, 47),
	(4, 48),
	(4, 49),
	(4, 50),
	(4, 51),
	(4, 52),
	(4, 53),
	(4, 54),
	(5, 58),
	(5, 59),
	(5, 60),
	(5, 61),
	(5, 62),
	(5, 63),
	(5, 64),
	(5, 65),
	(5, 66),
	(5, 67),
	(5, 68),
	(5, 69),
	(6, 73),
	(6, 74),
	(6, 75),
	(6, 76),
	(6, 77),
	(6, 78),
	(6, 79),
	(6, 80),
	(6, 81),
	(6, 82),
	(6, 83),
	(6, 84),
	(7, 88),
	(7, 89),
	(7, 90),
	(7, 91),
	(7, 92),
	(7, 93),
	(7, 94),
	(7, 95),
	(7, 96),
	(7, 97),
	(7, 98),
	(7, 99),
	(8, 103),
	(8, 104),
	(8, 105),
	(8, 106),
	(8, 107),
	(8, 108),
	(8, 109),
	(8, 110),
	(8, 111),
	(8, 112),
	(8, 113),
	(8, 114),
	(9, 118),
	(9, 119),
	(9, 120),
	(9, 121),
	(9, 122),
	(9, 123),
	(9, 124),
	(9, 125),
	(9, 126),
	(9, 127),
	(9, 128),
	(9, 129),
	(10, 133),
	(10, 134),
	(10, 135),
	(10, 136),
	(10, 137),
	(10, 138),
	(10, 139),
	(10, 140),
	(10, 141),
	(10, 142),
	(10, 143),
	(10, 144);


/*!40000 ALTER TABLE `Task_Asset` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table TaskInstance
# ------------------------------------------------------------

LOCK TABLES `TaskInstance` WRITE;
/*!40000 ALTER TABLE `TaskInstance` DISABLE KEYS */;

INSERT INTO `TaskInstance` (`taskInstanceId`, `archiveDate`, `lastChange`, `estTime`, `isStandAloneTask`, `taskId`, `taskTemplateId`)
VALUES
	(1, NULL, NULL, 2, 1, 1, NULL),
	(2, NULL, NULL, 2, 1, 2, NULL),
	(3, NULL, NULL, 2, 1, 3, NULL),
	(4, NULL, NULL, 8, 1, 31, NULL),
	(5, NULL, NULL, 6, 1, 47, NULL),
	(6, NULL, NULL, 8, 1, 63, NULL),
	(7, NULL, NULL, 0, 1, 79, NULL),
	(8, NULL, NULL, 0, 1, 80, NULL),
	(9, NULL, NULL, 3, 1, 96, NULL),
	(10, NULL, NULL, 0, 1, 112, NULL),


	(11, NULL, NULL, 0, 1, NULL, NULL),
	(12, NULL, NULL, 0, 1, NULL, NULL),
	(13, NULL, NULL, 0, 1, NULL, NULL),
	(14, NULL, NULL, 0, 1, NULL, NULL),
	(15, NULL, NULL, 0, 1, NULL, NULL),
	(16, NULL, NULL, 0, 1, NULL, NULL),
	(17, NULL, NULL, 0, 1, NULL, NULL),
	(18, NULL, NULL, 0, 1, NULL, NULL);
	
/*!40000 ALTER TABLE `TaskInstance` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Job
# ------------------------------------------------------------

LOCK TABLES `Job` WRITE;
/*!40000 ALTER TABLE `Job` DISABLE KEYS */;

INSERT INTO `Job` (`jobId`, `archiveDate`, `lastChange`, `jobCat`, `completedDate`, `createdBy`, `createdDate`, `dis`, `description`, `dispatchedDate`, 
`dueDate`, `earliestStart`, `eom`, `ero`, `estTime`, `jobType`, `latestStart`, `note`, `priority`, `risd`, `scheduledDate`, `startedDate`, `status`, 
`sticky`, `totatEstTime`, `assetId`, `orgId`, `taskInstanceId`,`contactId`)
VALUES
	(1, NULL, NULL, 'CAT10', NULL, NULL, '2011-08-14', 3, '380SL maintenance course', NULL, '2011-08-17', '2011-08-17', 'E2', '0BB43', 8, 'JT2', '2011-08-20', '', 'P6', '2011-08-14', NULL, NULL, 'JS6', 0, 180, 1, 1, 11,11),
	(3, NULL, NULL, 'CAT6', NULL, NULL, '2011-08-14', 3, '230SL maintenance course', NULL, '2011-08-17', '2011-08-17', 'E2', '0BB75', 6, 'JT2', '2011-08-20', NULL, 'P6', '2011-08-14', NULL, NULL, 'JS6', 0, 360, 3, 1, 12,12),
	(4, NULL, NULL, 'CAT6', NULL, NULL, '2011-08-14', 3, '380SLC maintenance course', NULL, '2011-08-17', '2011-08-17', 'E2', '0BB58', 8, 'JT1', '2011-08-20', NULL, 'P6', '2011-08-14', NULL, NULL, 'JS6', 0, 480, 4, 1, 13,13),
	(5, NULL, NULL, 'CAT6', NULL, NULL, '2011-08-14', 3, '500SL/SL500 maintenance', NULL, '2011-08-17', '2011-08-17', 'E2', '0BB59', 4, 'JT2', '2011-08-20', NULL, 'P13', '2011-08-14', NULL, NULL, 'JS6', 0, 0, 5, 1, 14,14),
	(6, NULL, NULL, 'CAT6', NULL, NULL, '2011-08-14', 3, 'SL55 AMG maintenance', NULL, '2011-08-17', '2011-08-17', 'E2', '0BB61', 2, 'JT2', '2011-08-20', NULL, 'P12', '2011-08-14', NULL, NULL, 'JS6', 0, 0, 6, 1, 15,15),
	(7, NULL, NULL, 'CAT6', NULL, NULL, '2011-08-14', 3, '380SL maintenance course', NULL, '2011-08-17', '2011-08-17', 'E2', '0BB65', 3, 'JT2', '2011-08-20', NULL, 'P13', '2011-08-14', NULL, NULL, 'JS6', 0, 180, 7, 1, 16,16),
	(8, NULL, NULL, 'CAT10', NULL, NULL, '2011-08-14', 3, '230SL maintenance course', NULL, '2011-08-17', '2011-08-17', 'E2', '0BB60', 2, 'JT2', '2011-08-20', '', 'P6', '2011-08-14', NULL, NULL, 'JS1', 0, 0, 8, 1, 17,17),
	(16, NULL, NULL, 'CAT5', NULL, NULL, '2011-08-14', 3, 'SLK230 maintenance course', NULL, '2011-08-17', '2011-08-17', 'E2', '0BB75', 0, NULL, '2011-08-20', '', 'P1', '2011-08-14', NULL, NULL, 'JS1', 0, NULL, 2, 1, 18,18);


/*!40000 ALTER TABLE `Job` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table JobAction
# ------------------------------------------------------------



# Dump of table JobStatus
# ------------------------------------------------------------



# Dump of table JobTask
# ------------------------------------------------------------

LOCK TABLES `JobTask` WRITE;
/*!40000 ALTER TABLE `JobTask` DISABLE KEYS */;

INSERT INTO `JobTask` (`jobTaskId`, `archiveDate`, `lastChange`, `actualTime`, `description`, `estTime`, `taskCode`, `jobId`, `taskInstanceId`)
VALUES
	(1, NULL, NULL, 0, 'CM: Rplc power steering pump ', 2, 'X17', 1, 1),
	(2, NULL, NULL, 0, 'CM: Rplc. pwr steering hose', 2, 'X22', 1, 2),
	(3, NULL, NULL, 0, 'CM: Rplc. water spray nozzle', 2, 'X34', 1, 3),
	(4, NULL, NULL, 0, 'CM: Rplc rear axle', 8, 'X34', 3, 4),
	(5, NULL, NULL, 0, 'PM: Annual PMCS', 6, 'X52', 4, 5),
	(6, NULL, NULL, 0, 'CM: Rplc. transmission seals', 8, 'X16', 5, 6),
	(7, NULL, NULL, 0, 'CM: Rplc. water pump', 2, 'X17', 6, 7),
	(8, NULL, NULL, 0, 'CM: Rplc. water spray hose', 0, 'X11', 6, 8),
	(9, NULL, NULL, 0, 'CM: Rplc shocks', 3, 'X31', 7, 9),
	(10, NULL, NULL, 0, 'CM: Rplc water valve', 1, 'X18', 8, 10);


/*!40000 ALTER TABLE `JobTask` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table AssetResource
# ------------------------------------------------------------

LOCK TABLES `AssetResource` WRITE;
/*!40000 ALTER TABLE `AssetResource` DISABLE KEYS */;

INSERT INTO `AssetResource` (`assetResourceId`, `archiveDate`, `lastChange`, `isManualAssign`, `quantity`, `assetTemplateId`, `jobId`, `stickyAssetId`, `taskId`, `taskTemplateId`)
VALUES
	(25, NULL, NULL, 0, 1, 5, 8, NULL, 112, NULL),
	(51, NULL, NULL, 0, 1, 6, 7, NULL, 96, NULL),
	(98, NULL, NULL, 0, 1, 9, 6, NULL, 79, NULL),
	(82, NULL, NULL, 0, 1, 8, 5, NULL, 63, NULL),
	(66, NULL, NULL, 0, 1, 7, 4, NULL, 47, NULL),
	(4, NULL, NULL, 0, 1, 5, 3, NULL, 31, NULL),
	(31, NULL, NULL, 0, 1, 6, 1, NULL, 1, NULL),
	(103, NULL, NULL, 0, 1, 9, NULL, NULL, 84, NULL),
	(107, NULL, NULL, 0, 1, 10, NULL, NULL, 13, NULL),
	(108, NULL, NULL, 0, 1, 10, NULL, NULL, 14, NULL),
	(109, NULL, NULL, 0, 1, 10, NULL, NULL, 15, NULL),
	(110, NULL, NULL, 0, 1, 10, NULL, NULL, 16, NULL),
	(111, NULL, NULL, 0, 1, 10, NULL, NULL, 17, NULL),
	(112, NULL, NULL, 0, 1, 10, NULL, NULL, 18, NULL),
	(113, NULL, NULL, 0, 1, 10, NULL, NULL, 19, NULL),
	(114, NULL, NULL, 0, 1, 10, NULL, NULL, 20, NULL),
	(115, NULL, NULL, 0, 1, 10, NULL, NULL, 21, NULL),
	(116, NULL, NULL, 0, 1, 10, NULL, NULL, 22, NULL),
	(84, NULL, NULL, 0, 1, 8, NULL, NULL, 65, NULL),
	(102, NULL, NULL, 0, 1, 9, NULL, NULL, 83, NULL),
	(85, NULL, NULL, 0, 1, 8, NULL, NULL, 66, NULL),
	(86, NULL, NULL, 0, 1, 8, NULL, NULL, 67, NULL),
	(87, NULL, NULL, 0, 1, 8, NULL, NULL, 68, NULL),
	(88, NULL, NULL, 0, 1, 8, NULL, NULL, 69, NULL),
	(92, NULL, NULL, 0, 1, 9, NULL, NULL, 73, NULL),
	(93, NULL, NULL, 0, 1, 9, NULL, NULL, 74, NULL),
	(94, NULL, NULL, 0, 1, 9, NULL, NULL, 75, NULL),
	(95, NULL, NULL, 0, 1, 9, NULL, NULL, 76, NULL),
	(96, NULL, NULL, 0, 1, 9, NULL, NULL, 77, NULL),
	(97, NULL, NULL, 0, 1, 9, NULL, NULL, 78, NULL),
	(62, NULL, NULL, 0, 1, 7, NULL, NULL, 43, NULL),
	(99, NULL, NULL, 0, 1, 9, NULL, NULL, 80, NULL),
	(100, NULL, NULL, 0, 1, 9, NULL, NULL, 81, NULL),
	(101, NULL, NULL, 0, 1, 9, NULL, NULL, 82, NULL),
	(117, NULL, NULL, 0, 1, 10, NULL, NULL, 23, NULL),
	(118, NULL, NULL, 0, 1, 10, NULL, NULL, 24, NULL),
	(141, NULL, NULL, 0, 1, 12, NULL, NULL, 137, NULL),
	(142, NULL, NULL, 0, 1, 12, NULL, NULL, 138, NULL),
	(143, NULL, NULL, 0, 1, 12, NULL, NULL, 139, NULL),
	(144, NULL, NULL, 0, 1, 12, NULL, NULL, 140, NULL),
	(145, NULL, NULL, 0, 1, 12, NULL, NULL, 141, NULL),
	(146, NULL, NULL, 0, 1, 12, NULL, NULL, 142, NULL),
	(147, NULL, NULL, 0, 1, 12, NULL, NULL, 143, NULL),
	(148, NULL, NULL, 0, 1, 12, NULL, NULL, 144, NULL),
	(149, NULL, NULL, 0, 1, 6, NULL, NULL, 1, NULL),
	(150, NULL, NULL, 0, 1, 5, NULL, NULL, 31, NULL),
	(151, NULL, NULL, 0, 1, 7, NULL, NULL, 47, NULL),
	(152, NULL, NULL, 0, 1, 8, NULL, NULL, 63, NULL),
	(153, NULL, NULL, 0, 1, 9, NULL, NULL, 79, NULL),
	(154, NULL, NULL, 0, 1, 6, NULL, NULL, 96, NULL),
	(140, NULL, NULL, 0, 1, 12, NULL, NULL, 136, NULL),
	(139, NULL, NULL, 0, 1, 12, NULL, NULL, 135, NULL),
	(122, NULL, NULL, 0, 1, 11, NULL, NULL, 118, NULL),
	(123, NULL, NULL, 0, 1, 11, NULL, NULL, 119, NULL),
	(124, NULL, NULL, 0, 1, 11, NULL, NULL, 120, NULL),
	(125, NULL, NULL, 0, 1, 11, NULL, NULL, 121, NULL),
	(126, NULL, NULL, 0, 1, 11, NULL, NULL, 122, NULL),
	(127, NULL, NULL, 0, 1, 11, NULL, NULL, 123, NULL),
	(128, NULL, NULL, 0, 1, 11, NULL, NULL, 124, NULL),
	(129, NULL, NULL, 0, 1, 11, NULL, NULL, 125, NULL),
	(130, NULL, NULL, 0, 1, 11, NULL, NULL, 126, NULL),
	(131, NULL, NULL, 0, 1, 11, NULL, NULL, 127, NULL),
	(132, NULL, NULL, 0, 1, 11, NULL, NULL, 128, NULL),
	(133, NULL, NULL, 0, 1, 11, NULL, NULL, 129, NULL),
	(137, NULL, NULL, 0, 1, 12, NULL, NULL, 133, NULL),
	(138, NULL, NULL, 0, 1, 12, NULL, NULL, 134, NULL),
	(155, NULL, NULL, 0, 1, 5, NULL, NULL, 112, NULL),
	(1, NULL, NULL, 0, 1, 5, NULL, NULL, 28, NULL),
	(21, NULL, NULL, 0, 1, 5, NULL, NULL, 108, NULL),
	(22, NULL, NULL, 0, 1, 5, NULL, NULL, 109, NULL),
	(23, NULL, NULL, 0, 1, 5, NULL, NULL, 110, NULL),
	(24, NULL, NULL, 0, 1, 5, NULL, NULL, 111, NULL),
	(26, NULL, NULL, 0, 1, 5, NULL, NULL, 113, NULL),
	(27, NULL, NULL, 0, 1, 5, NULL, NULL, 114, NULL),
	(32, NULL, NULL, 0, 1, 6, NULL, NULL, 2, NULL),
	(33, NULL, NULL, 0, 1, 6, NULL, NULL, 3, NULL),
	(34, NULL, NULL, 0, 1, 6, NULL, NULL, 4, NULL),
	(35, NULL, NULL, 0, 1, 6, NULL, NULL, 5, NULL),
	(36, NULL, NULL, 0, 1, 6, NULL, NULL, 6, NULL),
	(37, NULL, NULL, 0, 1, 6, NULL, NULL, 7, NULL),
	(38, NULL, NULL, 0, 1, 6, NULL, NULL, 8, NULL),
	(20, NULL, NULL, 0, 1, 5, NULL, NULL, 107, NULL),
	(19, NULL, NULL, 0, 1, 5, NULL, NULL, 106, NULL),
	(2, NULL, NULL, 0, 1, 5, NULL, NULL, 29, NULL),
	(3, NULL, NULL, 0, 1, 5, NULL, NULL, 30, NULL),
	(5, NULL, NULL, 0, 1, 5, NULL, NULL, 32, NULL),
	(6, NULL, NULL, 0, 1, 5, NULL, NULL, 33, NULL),
	(7, NULL, NULL, 0, 1, 5, NULL, NULL, 34, NULL),
	(8, NULL, NULL, 0, 1, 5, NULL, NULL, 35, NULL),
	(9, NULL, NULL, 0, 1, 5, NULL, NULL, 36, NULL),
	(10, NULL, NULL, 0, 1, 5, NULL, NULL, 37, NULL),
	(11, NULL, NULL, 0, 1, 5, NULL, NULL, 38, NULL),
	(12, NULL, NULL, 0, 1, 5, NULL, NULL, 39, NULL),
	(16, NULL, NULL, 0, 1, 5, NULL, NULL, 103, NULL),
	(17, NULL, NULL, 0, 1, 5, NULL, NULL, 104, NULL),
	(18, NULL, NULL, 0, 1, 5, NULL, NULL, 105, NULL),
	(39, NULL, NULL, 0, 1, 6, NULL, NULL, 9, NULL),
	(40, NULL, NULL, 0, 1, 6, NULL, NULL, 10, NULL),
	(65, NULL, NULL, 0, 1, 7, NULL, NULL, 46, NULL);

INSERT INTO `AssetResource` (`assetResourceId`, `archiveDate`, `lastChange`, `isManualAssign`, `quantity`, `assetTemplateId`, `jobId`, `stickyAssetId`, `taskId`, `taskTemplateId`)
VALUES
	(67, NULL, NULL, 0, 1, 7, NULL, NULL, 48, NULL),
	(68, NULL, NULL, 0, 1, 7, NULL, NULL, 49, NULL),
	(69, NULL, NULL, 0, 1, 7, NULL, NULL, 50, NULL),
	(70, NULL, NULL, 0, 1, 7, NULL, NULL, 51, NULL),
	(71, NULL, NULL, 0, 1, 7, NULL, NULL, 52, NULL),
	(72, NULL, NULL, 0, 1, 7, NULL, NULL, 53, NULL),
	(73, NULL, NULL, 0, 1, 7, NULL, NULL, 54, NULL),
	(77, NULL, NULL, 0, 1, 8, NULL, NULL, 58, NULL),
	(78, NULL, NULL, 0, 1, 8, NULL, NULL, 59, NULL),
	(79, NULL, NULL, 0, 1, 8, NULL, NULL, 60, NULL),
	(80, NULL, NULL, 0, 1, 8, NULL, NULL, 61, NULL),
	(81, NULL, NULL, 0, 1, 8, NULL, NULL, 62, NULL),
	(64, NULL, NULL, 0, 1, 7, NULL, NULL, 45, NULL),
	(63, NULL, NULL, 0, 1, 7, NULL, NULL, 44, NULL),
	(41, NULL, NULL, 0, 1, 6, NULL, NULL, 11, NULL),
	(42, NULL, NULL, 0, 1, 6, NULL, NULL, 12, NULL),
	(43, NULL, NULL, 0, 1, 6, NULL, NULL, 88, NULL),
	(44, NULL, NULL, 0, 1, 6, NULL, NULL, 89, NULL),
	(45, NULL, NULL, 0, 1, 6, NULL, NULL, 90, NULL),
	(46, NULL, NULL, 0, 1, 6, NULL, NULL, 91, NULL),
	(47, NULL, NULL, 0, 1, 6, NULL, NULL, 92, NULL),
	(48, NULL, NULL, 0, 1, 6, NULL, NULL, 93, NULL),
	(49, NULL, NULL, 0, 1, 6, NULL, NULL, 94, NULL),
	(50, NULL, NULL, 0, 1, 6, NULL, NULL, 95, NULL),
	(52, NULL, NULL, 0, 1, 6, NULL, NULL, 97, NULL),
	(53, NULL, NULL, 0, 1, 6, NULL, NULL, 98, NULL),
	(54, NULL, NULL, 0, 1, 6, NULL, NULL, 99, NULL),
	(83, NULL, NULL, 0, 1, 8, NULL, NULL, 64, NULL);



/*!40000 ALTER TABLE `AssetResource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table AssetTimeShare
# ------------------------------------------------------------

LOCK TABLES `AssetTimeShare` WRITE;
/*!40000 ALTER TABLE `AssetTimeShare` DISABLE KEYS */;

INSERT INTO `AssetTimeShare` (`assetTimeShareId`,`archiveDate`,`lastChange`,`asResource`,`isAssigned`,`endDate`,`startDate`,`assetId`,`jobId`)
VALUES 
(1,NULL,NULL,1,1,'2011-09-13 23:44:58','2011-09-13 15:44:58',23,1),
(2,NULL,NULL,1,1,'2011-09-13 23:44:58','2011-09-13 15:44:58',22,3),
(3,NULL,NULL,1,1,'2011-09-13 23:44:58','2011-09-13 15:44:58',24,4),
(4,NULL,NULL,1,1,'2011-09-13 19:44:58','2011-09-13 15:44:58',28,5),
(5,NULL,NULL,1,1,'2011-09-13 17:44:58','2011-09-13 15:44:58',26,6),
(6,NULL,NULL,1,1,'2011-09-13 18:44:58','2011-09-13 15:44:58',30,7);

/*!40000 ALTER TABLE `AssetTimeShare` ENABLE KEYS */;
UNLOCK TABLES;

# Dump of table loginactivity
# ------------------------------------------------------------



# Dump of table NonAssetResource
# ------------------------------------------------------------

LOCK TABLES `NonAssetResource` WRITE;
/*!40000 ALTER TABLE `NonAssetResource` DISABLE KEYS */;


INSERT INTO `NonAssetResource` (`nonAssetResourceId`, `archiveDate`, `lastChange`, `currentLoc`, `description`, `quantity`, `recDateStr`, `status`, `tag`, `type`, `unitRefId`, `jobId`, `taskId`, `taskTemplateId`)
VALUES
	(1, NULL, NULL, '20000 H', 'Power steering pump', 1, '1275', 'Received 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, 1, 1, 1),
	(2, NULL, NULL, '20000 H', 'Brackets', 1, '1276', 'Shipped 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, 1, 1, 1),
	(3, NULL, NULL, '20000 H', 'Power steering hose', 1, NULL, 'Canceled 1275', '4XXX-YY-YYY-ZZZZ', 'NA1', 0, 1, 2, 2),
	(4, NULL, NULL, '20000 H', 'Water spray nozzle', 1, NULL, 'Not ordered', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, 1, 3, 3),
	(5, NULL, '2011-06-01 00:00:00', '20000 H', 'Rear Axle', 1, '1277', 'Received 1277', '8XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 4, 4),
	(6, NULL, '2011-06-01 00:00:00', '20000 H', 'Engine oil 20w50', 15, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA2', 1, NULL, 5, 5),
	(7, NULL, '2011-06-01 00:00:00', '20000 H', 'M-ATV Annual PMCS Kit', 1, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA3', 0, NULL, 5, 5),
	(8, NULL, NULL, '20000 H', 'Power steering pump', 1, '1275', 'Received 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 13, NULL),
	(9, NULL, NULL, '20000 H', 'Brackets', 1, '1276', 'Shipped 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 13, NULL),
	(11, NULL, NULL, '20000 H', 'Power steering pump', 1, '1275', 'Received 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 28, NULL),
	(12, NULL, NULL, '20000 H', 'Brackets', 1, '1276', 'Shipped 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 28, NULL),
	(14, NULL, NULL, '20000 H', 'Power steering pump', 1, '1275', 'Received 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 43, NULL),
	(15, NULL, NULL, '20000 H', 'Brackets', 1, '1276', 'Shipped 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 43, NULL),
	(17, NULL, NULL, '20000 H', 'Power steering pump', 1, '1275', 'Received 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 58, NULL),
	(18, NULL, NULL, '20000 H', 'Brackets', 1, '1276', 'Shipped 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 58, NULL),
	(20, NULL, NULL, '20000 H', 'Power steering pump', 1, '2011-10-02', 'Received 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 73, NULL),
	(21, NULL, NULL, '20000 H', 'Brackets', 1, '1276', 'Shipped 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 73, NULL),
	(23, NULL, NULL, '20000 H', 'Power steering pump', 1, '2011-10-02', 'Received 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 88, NULL),
	(24, NULL, NULL, '20000 H', 'Brackets', 1, '1276', 'Shipped 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 88, NULL),
	(26, NULL, NULL, '20000 H', 'Power steering pump', 1, '1275', 'Received 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 103, NULL),
	(27, NULL, NULL, '20000 H', 'Brackets', 1, '1276', 'Shipped 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 103, NULL),
	(29, NULL, NULL, '20000 H', 'Power steering pump', 1, '1275', 'Received 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 118, NULL),
	(30, NULL, NULL, '20000 H', 'Brackets', 1, '1276', 'Shipped 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 118, NULL),
	(32, NULL, NULL, '20000 H', 'Power steering pump', 1, '1275', 'Received 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 133, NULL),
	(33, NULL, NULL, '20000 H', 'Brackets', 1, '1276', 'Shipped 1275', '2XXX-YY-ZZZ-XXXX', 'NA1', 0, NULL, 133, NULL),
	(35, NULL, NULL, '20000 H', 'Power steering hose', 1, NULL, 'Canceled 1275', '4XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 14, NULL),
	(36, NULL, NULL, '20000 H', 'Power steering hose', 1, NULL, 'Canceled 1275', '4XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 29, NULL),
	(37, NULL, NULL, '20000 H', 'Power steering hose', 1, NULL, 'Canceled 1275', '4XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 44, NULL),
	(38, NULL, NULL, '20000 H', 'Power steering hose', 1, NULL, 'Canceled 1275', '4XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 59, NULL),
	(39, NULL, NULL, '20000 H', 'Power steering hose', 1, NULL, 'Canceled 1275', '4XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 74, NULL),
	(40, NULL, NULL, '20000 H', 'Power steering hose', 1, NULL, 'Canceled 1275', '4XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 89, NULL),
	(41, NULL, NULL, '20000 H', 'Power steering hose', 1, NULL, 'Canceled 1275', '4XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 104, NULL),
	(42, NULL, NULL, '20000 H', 'Power steering hose', 1, NULL, 'Canceled 1275', '4XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 119, NULL),
	(43, NULL, NULL, '20000 H', 'Power steering hose', 1, NULL, 'Canceled 1275', '4XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 134, NULL),
	(44, NULL, NULL, '20000 H', 'Water spray nozzle', 1, NULL, 'Not ordered', '2XXX-01-184-5503', 'NA1', 0, NULL, 15, NULL),
	(45, NULL, NULL, '20000 H', 'Water spray nozzle', 1, NULL, 'Not ordered', '2XXX-01-184-5503', 'NA1', 0, NULL, 30, NULL),
	(46, NULL, NULL, '20000 H', 'Water spray nozzle', 1, NULL, 'Not ordered', '2XXX-01-184-5503', 'NA1', 0, NULL, 45, NULL),
	(47, NULL, NULL, '20000 H', 'Water spray nozzle', 1, NULL, 'Not ordered', '2XXX-01-184-5503', 'NA1', 0, NULL, 60, NULL),
	(48, NULL, NULL, '20000 H', 'Water spray nozzle', 1, NULL, 'Not ordered', '2XXX-01-184-5503', 'NA1', 0, NULL, 75, NULL),
	(49, NULL, NULL, '20000 H', 'Water spray nozzle', 1, NULL, 'Not ordered', '2XXX-01-184-5503', 'NA1', 0, NULL, 90, NULL),
	(50, NULL, NULL, '20000 H', 'Water spray nozzle', 1, NULL, 'Not ordered', '2XXX-01-184-5503', 'NA1', 0, NULL, 105, NULL),
	(51, NULL, NULL, '20000 H', 'Water spray nozzle', 1, NULL, 'Not ordered', '2XXX-01-184-5503', 'NA1', 0, NULL, 120, NULL),
	(52, NULL, NULL, '20000 H', 'Water spray nozzle', 1, NULL, 'Not ordered', '2XXX-01-184-5503', 'NA1', 0, NULL, 135, NULL),
	(53, NULL, NULL, '20000 H', 'Rear Axle', 1, '1277', 'Received 1277', '8XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 16, NULL),
	(54, NULL, NULL, '20000 H', 'Rear Axle', 1, '1277', 'Received 1277', '8XXX-YY-YYY-ZZZZ', 'NA1', 0, 3, 31, NULL),
	(55, NULL, NULL, '20000 H', 'Rear Axle', 1, '1277', 'Received 1277', '8XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 46, NULL),
	(56, NULL, NULL, '20000 H', 'Rear Axle', 1, '1277', 'Received 1277', '8XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 61, NULL),
	(57, NULL, NULL, '20000 H', 'Rear Axle', 1, '1277', 'Received 1277', '8XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 76, NULL),
	(58, NULL, NULL, '20000 H', 'Rear Axle', 1, '1277', 'Received 1277', '8XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 91, NULL),
	(59, NULL, NULL, '20000 H', 'Rear Axle', 1, '1277', 'Received 1277', '8XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 106, NULL),
	(60, NULL, NULL, '20000 H', 'Rear Axle', 1, '1277', 'Received 1277', '8XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 121, NULL),
	(61, NULL, NULL, '20000 H', 'Rear Axle', 1, '1277', 'Received 1277', '8XXX-YY-YYY-ZZZZ', 'NA1', 0, NULL, 136, NULL),
	(62, NULL, NULL, '20000 H', 'Engine oil 20w50', 15, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA2', 1, NULL, 17, NULL),
	(63, NULL, NULL, '20000 H', 'Annual PMCS Kit', 1, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA3', 0, NULL, 17, NULL),
	(65, NULL, NULL, '20000 H', 'Engine oil 20w50', 15, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA2', 1, NULL, 32, NULL),
	(66, NULL, NULL, '20000 H', 'Annual PMCS Kit', 1, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA3', 0, NULL, 32, NULL),
	(68, NULL, NULL, '20000 H', 'Engine oil 20w50', 15, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA2', 1, 4, 47, NULL),
	(69, NULL, NULL, '20000 H', 'Annual PMCS Kit', 1, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA3', 0, 4, 47, NULL),
	(71, NULL, NULL, '20000 H', 'Engine oil 20w50', 15, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA2', 1, NULL, 62, NULL),
	(72, NULL, NULL, '20000 H', 'Annual PMCS Kit', 1, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA3', 0, NULL, 62, NULL),
	(74, NULL, NULL, '20000 H', 'Engine oil 20w50', 15, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA2', 1, NULL, 77, NULL),
	(75, NULL, NULL, '20000 H', 'Annual PMCS Kit', 1, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA3', 0, NULL, 77, NULL),
	(77, NULL, NULL, '20000 H', 'Engine oil 20w50', 15, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA2', 1, NULL, 92, NULL);

INSERT INTO `NonAssetResource` (`nonAssetResourceId`, `archiveDate`, `lastChange`, `currentLoc`, `description`, `quantity`, `recDateStr`, `status`, `tag`, `type`, `unitRefId`, `jobId`, `taskId`, `taskTemplateId`)
VALUES
	(78, NULL, NULL, '20000 H', 'Annual PMCS Kit', 1, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA3', 0, NULL, 92, NULL),
	(80, NULL, NULL, '20000 H', 'Engine oil 20w50', 15, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA2', 1, NULL, 107, NULL),
	(81, NULL, NULL, '20000 H', 'Annual PMCS Kit', 1, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA3', 0, NULL, 107, NULL),
	(83, NULL, NULL, '20000 H', 'Engine oil 20w50', 15, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA2', 1, NULL, 122, NULL),
	(84, NULL, NULL, '20000 H', 'Annual PMCS Kit', 1, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA3', 0, NULL, 122, NULL),
	(86, NULL, NULL, '20000 H', 'Engine oil 20w50', 15, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA2', 1, NULL, 137, NULL),
	(87, NULL, NULL, '20000 H', 'Annual PMCS Kit', 1, '1272', 'Received 1272', '0XXX-XX-XXX-XXXX', 'NA3', 0, NULL, 137, NULL);
	
/*!40000 ALTER TABLE `NonAssetResource` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table role
# ------------------------------------------------------------

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;

INSERT INTO `role` (`roleId`, `archiveDate`, `lastChange`, `description`, `name`)
VALUES
	(1,NULL,NULL,'IWM Application Administrator','admin');

/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;





# Dump of table TenantRequest
# ------------------------------------------------------------



# Dump of table user
# ------------------------------------------------------------

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`userId`, `archiveDate`, `lastChange`, `email`, `enabled`, `fname`, `lname`, `password`, `uid`)
VALUES
	(1,NULL,NULL,'nadia@masterlink.com',b'1','Administrator','Administrator','admin','admin');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_role
# ------------------------------------------------------------

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;

INSERT INTO `user_role` (`user_id`, `role_id`)
VALUES
	(1,1);

/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;



/*
 * Update job dates
 * TODO: This should be done at job inser
 */

UPDATE `Job` 
SET createdDate= DATE_ADD( curdate(), INTERVAL -3 DAY), 
    dueDate= curdate(), 
    risd=DATE_ADD( curdate(), INTERVAL -3 DAY),
    earliestStart=curDate(),
    latestStart= DATE_ADD( curdate(), INTERVAL +3 DAY);
    
/*
 * An Attempt to avert the TaskInstance bomb.
 */
alter table TaskInstance auto_increment = 100;