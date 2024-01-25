-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: multifitclub
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `annuncio`
--

DROP TABLE IF EXISTS `annuncio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `annuncio` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titolo` varchar(45) NOT NULL,
  `testo` text NOT NULL,
  `data` datetime NOT NULL,
  `utente` char(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `annuncio_utente_idx` (`utente`),
  CONSTRAINT `annuncio_utente` FOREIGN KEY (`utente`) REFERENCES `utente` (`cf`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `annuncio`
--

LOCK TABLES `annuncio` WRITE;
/*!40000 ALTER TABLE `annuncio` DISABLE KEYS */;
INSERT INTO `annuncio` VALUES (1,'Annuncio','bellissimo testo','2024-01-24 20:09:52','BNCMRA70A20H501B'),(9,'titolone','aiutoooo.','2024-01-24 23:16:02','BNCMRA70A20H501B'),(16,'nuovo ann','sa sa prova prova','2024-01-25 20:04:34','BNCMRA70A20H501B');
/*!40000 ALTER TABLE `annuncio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `corso`
--

DROP TABLE IF EXISTS `corso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `corso` (
  `nome` varchar(45) NOT NULL,
  `data_inizio` date NOT NULL,
  PRIMARY KEY (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `corso`
--

LOCK TABLES `corso` WRITE;
/*!40000 ALTER TABLE `corso` DISABLE KEYS */;
INSERT INTO `corso` VALUES ('judo','2023-12-01'),('karate','2024-01-22'),('yoga','2022-01-01');
/*!40000 ALTER TABLE `corso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `insegnato`
--

DROP TABLE IF EXISTS `insegnato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `insegnato` (
  `corso` varchar(45) NOT NULL,
  `utente` char(16) NOT NULL,
  PRIMARY KEY (`corso`,`utente`),
  KEY `insegnato_utente_idx` (`utente`),
  CONSTRAINT `insegnato_corso` FOREIGN KEY (`corso`) REFERENCES `corso` (`nome`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `insegnato_utente` FOREIGN KEY (`utente`) REFERENCES `utente` (`cf`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `insegnato`
--

LOCK TABLES `insegnato` WRITE;
/*!40000 ALTER TABLE `insegnato` DISABLE KEYS */;
INSERT INTO `insegnato` VALUES ('judo','MACMRA70A20H335L'),('yoga','MACMRA70A20H335L');
/*!40000 ALTER TABLE `insegnato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `iscrizione`
--

DROP TABLE IF EXISTS `iscrizione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `iscrizione` (
  `corso` varchar(45) NOT NULL,
  `utente` char(16) NOT NULL,
  PRIMARY KEY (`utente`,`corso`),
  KEY `iscrizione_corso_idx` (`corso`),
  CONSTRAINT `iscrizione_corso` FOREIGN KEY (`corso`) REFERENCES `corso` (`nome`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `iscrizione_utente` FOREIGN KEY (`utente`) REFERENCES `utente` (`cf`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `iscrizione`
--

LOCK TABLES `iscrizione` WRITE;
/*!40000 ALTER TABLE `iscrizione` DISABLE KEYS */;
INSERT INTO `iscrizione` VALUES ('judo','MRZMTA70A20H409H'),('yoga','MRZMTA70A20H409H'),('yoga','MRZMTT12J78F345K');
/*!40000 ALTER TABLE `iscrizione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lezione`
--

DROP TABLE IF EXISTS `lezione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lezione` (
  `giorno` varchar(45) NOT NULL,
  `ora` time NOT NULL,
  `sala` varchar(45) NOT NULL,
  `corso` varchar(45) DEFAULT NULL,
  `istruttore` char(16) DEFAULT NULL,
  PRIMARY KEY (`giorno`,`ora`,`sala`),
  KEY `lezione_corso_idx` (`corso`),
  KEY `lezione_utente_idx` (`istruttore`),
  CONSTRAINT `lezione_corso` FOREIGN KEY (`corso`) REFERENCES `corso` (`nome`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lezione_utente` FOREIGN KEY (`istruttore`) REFERENCES `utente` (`cf`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lezione`
--

LOCK TABLES `lezione` WRITE;
/*!40000 ALTER TABLE `lezione` DISABLE KEYS */;
INSERT INTO `lezione` VALUES ('lunedi','10:30:00','A','judo',NULL),('mercoledi','10:30:00','B','judo',NULL),('venerdi','14:00:00','C','yoga',NULL);
/*!40000 ALTER TABLE `lezione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente` (
  `cf` char(16) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(45) NOT NULL,
  `data_nascita` date NOT NULL,
  `ruolo` int NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`cf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES ('BNCMRA70A20H501B','Matteo','Bianchi','1970-01-20',2,'email@email.it','12345'),('MACMRA70A20H335L','Maria','Verdi','2001-05-17',1,'ist@email.it','ciaociao'),('MRZMTA70A20H409H','Giorgio','Morandi','1970-01-28',0,'gino@email.eu','ciao'),('MRZMTT12J78F345K','Eugenia','Para','2004-01-09',0,'eu@gmail.com','dafult1'),('RSSMTA70A20H409H','Luca','Rossi','2000-02-02',0,'ute@email.it','ciao');
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-25 20:07:12
