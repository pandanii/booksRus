CREATE DATABASE  IF NOT EXISTS `movies&books` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `movies&books`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: movies&books
-- ------------------------------------------------------
-- Server version	8.0.0-dmr-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authors` (
  `address` varchar(40) NOT NULL,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`address`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors`
--

LOCK TABLES `authors` WRITE;
/*!40000 ALTER TABLE `authors` DISABLE KEYS */;
INSERT INTO `authors` VALUES ('812 cthulhu lane','H. P. Lovecraft '),('newtownsomewhere','fred');
/*!40000 ALTER TABLE `authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `books` (
  `title` varchar(30) NOT NULL,
  `ISBN` int(11) DEFAULT NULL,
  `subject_category` varchar(20) DEFAULT NULL,
  `address` varchar(40) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`title`),
  KEY `address` (`address`,`name`),
  CONSTRAINT `books_ibfk_1` FOREIGN KEY (`title`) REFERENCES `media` (`title`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `books_ibfk_2` FOREIGN KEY (`address`, `name`) REFERENCES `publishers` (`address`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES ('Necronomicon ',6660,'horror','underworld, 666 1000 street','necorocorp'),('WitchCraft and Wizardry',666,'zalgo','zalgo','zalgo'),('yoMomma',11,'horror','SOMEEEEWHEEERRROVERTHERAINBOW','wizardofOzz');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dvds`
--

DROP TABLE IF EXISTS `dvds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dvds` (
  `title` varchar(30) NOT NULL,
  `cast` varchar(20) DEFAULT NULL,
  `director` varchar(20) DEFAULT NULL,
  `genre` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`title`),
  CONSTRAINT `dvds_ibfk_1` FOREIGN KEY (`title`) REFERENCES `media` (`title`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dvds`
--

LOCK TABLES `dvds` WRITE;
/*!40000 ALTER TABLE `dvds` DISABLE KEYS */;
INSERT INTO `dvds` VALUES ('Bruno','Sacha Baron Cohen ','Sacha Baron Cohen ','comedy'),('catdog','somedudes','neil patrick harris','gayromance'),('catdog2','somedudes and stuff','neil patrick harris','gayromance'),('Doctor Strange ','Ben CucumberBatch','Scott Derrickson','SuperHero'),('Fifty Shades of Grey','Dakota Johnson','Sam Taylor-Johnson',' erotic drama '),('Harry Potter and the sore bone','Harry and Ron','neil patrick harris','horror'),('rip','rip','rip','rip'),('the ring','Gore','Gore Verbinski','horror'),('the ring 2','Gore','Gore Verbinski','horror'),('the ring 3','Gore','Gore Verbinski','horror');
/*!40000 ALTER TABLE `dvds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `keywordsearch`
--

DROP TABLE IF EXISTS `keywordsearch`;
/*!50001 DROP VIEW IF EXISTS `keywordsearch`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `keywordsearch` AS SELECT 
 1 AS `title`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `media`
--

DROP TABLE IF EXISTS `media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `media` (
  `title` varchar(30) NOT NULL,
  `price` int(11) NOT NULL,
  `copies_In_Stock` int(11) NOT NULL,
  `year` year(4) DEFAULT NULL,
  PRIMARY KEY (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media`
--

LOCK TABLES `media` WRITE;
/*!40000 ALTER TABLE `media` DISABLE KEYS */;
INSERT INTO `media` VALUES ('Bruno',1,3,2016),('catdog',100,5,1994),('catdog2',100,5,1995),('Doctor Strange ',1,3,2016),('Fifty Shades of Grey',1,3,2016),('Harry Potter and the sore bone',2,1,2016),('Necronomicon ',666,6,2017),('rip',6,6,2006),('the ring',1,111,2002),('the ring 2',2,222,2003),('the ring 3',3,333,2003),('WitchCraft and Wizardry',666,666,1996),('yoMomma',1,100,1969);
/*!40000 ALTER TABLE `media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publishers`
--

DROP TABLE IF EXISTS `publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `publishers` (
  `address` varchar(40) NOT NULL,
  `name` varchar(40) NOT NULL,
  `URL` varchar(40) DEFAULT NULL,
  `phone_number` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`address`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publishers`
--

LOCK TABLES `publishers` WRITE;
/*!40000 ALTER TABLE `publishers` DISABLE KEYS */;
INSERT INTO `publishers` VALUES ('SOMEEEEWHEEERRROVERTHERAINBOW','wizardofOzz','wizis.com','1111111111'),('underworld, 666 1000 street','necorocorp','necrocorp.com','6666666666'),('zalgo','zalgo','zalgo.zalgo','1234555555');
/*!40000 ALTER TABLE `publishers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase` (
  `userID` varchar(12) NOT NULL,
  `title` varchar(30) NOT NULL,
  `transactionID` int(11) NOT NULL,
  PRIMARY KEY (`userID`,`title`,`transactionID`),
  KEY `title` (`title`),
  KEY `transactionID` (`transactionID`),
  CONSTRAINT `purchase_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`),
  CONSTRAINT `purchase_ibfk_2` FOREIGN KEY (`title`) REFERENCES `media` (`title`),
  CONSTRAINT `purchase_ibfk_3` FOREIGN KEY (`transactionID`) REFERENCES `purchase_history` (`transactionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase`
--

LOCK TABLES `purchase` WRITE;
/*!40000 ALTER TABLE `purchase` DISABLE KEYS */;
INSERT INTO `purchase` VALUES ('Not_admin','WitchCraft and Wizardry',1),('Not_admin','yoMomma',2),('Not_admin','yoMomma',3),('Not_admin','yoMomma',4),('Not_admin','yoMomma',5),('Not_admin','yoMomma',6),('Not_admin','Necronomicon ',7),('Not_admin','Necronomicon ',8),('Not_admin','WitchCraft and Wizardry',9),('Not_admin','Necronomicon ',10),('Admin_1','WitchCraft and Wizardry',11),('Admin_1','Doctor Strange ',12),('Admin_1','the ring',13),('Admin_1','the ring 2',14),('Admin_1','the ring 3',15),('Admin_1','WitchCraft and Wizardry',16);
/*!40000 ALTER TABLE `purchase` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `beforeInsertOnPurchase` BEFORE INSERT ON `purchase` FOR EACH ROW BEGIN
	CASE(SELECT copies_In_Stock FROM media WHERE title = NEW.title)
		WHEN(0) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Current item is out of stock';
		ELSE
			UPDATE media
			SET    media.copies_In_Stock = media.copies_In_Stock - 1
			WHERE  media.title = NEW.title;
	END CASE;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `purchase_history`
--

DROP TABLE IF EXISTS `purchase_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_history` (
  `transactionID` int(11) NOT NULL,
  `date_of_purchase` date NOT NULL,
  `number_of_copies` int(11) NOT NULL,
  `total_cost` int(11) DEFAULT NULL,
  PRIMARY KEY (`transactionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_history`
--

LOCK TABLES `purchase_history` WRITE;
/*!40000 ALTER TABLE `purchase_history` DISABLE KEYS */;
INSERT INTO `purchase_history` VALUES (1,'2016-11-05',1,667),(2,'2016-11-05',1,3),(3,'2016-11-05',1,3),(4,'2016-11-07',1,666),(5,'2016-11-07',1,666),(6,'2016-11-07',1,666),(7,'2016-11-07',1,666),(8,'2016-11-07',1,666),(9,'2016-11-08',2,112312),(10,'2016-11-09',2,1111),(11,'2016-11-08',2,112312),(12,'2016-11-09',2,2),(13,'2016-11-09',2,222),(14,'2016-11-09',2,444),(15,'2016-11-07',2,666),(16,'2016-11-09',2,112312);
/*!40000 ALTER TABLE `purchase_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sequel_to`
--

DROP TABLE IF EXISTS `sequel_to`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequel_to` (
  `prequel_title` varchar(30) DEFAULT NULL,
  `sequel_title` varchar(30) DEFAULT NULL,
  KEY `prequel_title` (`prequel_title`),
  KEY `sequel_title` (`sequel_title`),
  CONSTRAINT `sequel_to_ibfk_1` FOREIGN KEY (`prequel_title`) REFERENCES `dvds` (`title`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sequel_to_ibfk_2` FOREIGN KEY (`sequel_title`) REFERENCES `dvds` (`title`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequel_to`
--

LOCK TABLES `sequel_to` WRITE;
/*!40000 ALTER TABLE `sequel_to` DISABLE KEYS */;
INSERT INTO `sequel_to` VALUES ('catdog','catdog2'),('the ring','the ring 2'),('the ring 2','the ring 3');
/*!40000 ALTER TABLE `sequel_to` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `userID` varchar(12) NOT NULL,
  `password` varchar(12) NOT NULL,
  `phone_number` varchar(10) DEFAULT NULL,
  `address` varchar(40) NOT NULL,
  `email` varchar(40) NOT NULL,
  `name` varchar(40) NOT NULL,
  `is_Admin` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

ALTER table `purchase`
	DROP FOREIGN KEY `purchase_ibfk_1`

ALTER table `purchase` ADD CONSTRAINT `purchase_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;
--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('Admin_1','12','5556421111','somewhere','admin_1@booksRus.com','Admin',1),('Not_admin','1','5556321121','somewhere1','not_admin_1@booksRus.com','Not_Admin',0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `written_by`
--

DROP TABLE IF EXISTS `written_by`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `written_by` (
  `title` varchar(30) NOT NULL,
  `address` varchar(40) NOT NULL,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`title`,`address`,`name`),
  KEY `address` (`address`,`name`),
  CONSTRAINT `written_by_ibfk_1` FOREIGN KEY (`title`) REFERENCES `books` (`title`),
  CONSTRAINT `written_by_ibfk_2` FOREIGN KEY (`address`, `name`) REFERENCES `authors` (`address`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `written_by`
--

LOCK TABLES `written_by` WRITE;
/*!40000 ALTER TABLE `written_by` DISABLE KEYS */;
INSERT INTO `written_by` VALUES ('Necronomicon ','812 cthulhu lane','H. P. Lovecraft '),('WitchCraft and Wizardry','newtownsomewhere','fred'),('yoMomma','newtownsomewhere','fred');
/*!40000 ALTER TABLE `written_by` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'movies&books'
--

--
-- Dumping routines for database 'movies&books'
--

--
-- Final view structure for view `keywordsearch`
--

/*!50001 DROP VIEW IF EXISTS `keywordsearch`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `keywordsearch` AS select distinct `media`.`title` AS `title` from (((`books` join `written_by`) join `authors`) join `media`) where ((`media`.`title` = `books`.`title`) and (`authors`.`name` = `written_by`.`name`) and (`authors`.`address` = `written_by`.`address`) and (`media`.`title` like '%fred%')) union select distinct `media`.`title` AS `title` from (((`books` join `written_by`) join `authors`) join `media`) where ((`media`.`title` = `books`.`title`) and (`authors`.`name` = `written_by`.`name`) and (`authors`.`address` = `written_by`.`address`) and (`authors`.`name` like '%fred%')) union select distinct `media`.`title` AS `title` from (`books` join `media`) where ((`media`.`title` = `books`.`title`) and (`books`.`name` like '%fred%')) union select distinct `media`.`title` AS `title` from (`books` join `media`) where ((`media`.`title` = `books`.`title`) and (`books`.`subject_category` like '%fred%')) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-01 22:07:36
