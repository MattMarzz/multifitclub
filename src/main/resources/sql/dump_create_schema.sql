CREATE DATABASE  IF NOT EXISTS `multifitclub` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `multifitclub`;
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
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `richiesta`
--

DROP TABLE IF EXISTS `richiesta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `richiesta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titolo` varchar(45) NOT NULL,
  `testo` varchar(45) DEFAULT NULL,
  `data` datetime DEFAULT NULL,
  `quando` datetime DEFAULT NULL,
  `status` int DEFAULT '0',
  `utente` char(16) DEFAULT NULL,
  `sala` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `richiesta_utente_idx` (`utente`),
  CONSTRAINT `richiesta_utente` FOREIGN KEY (`utente`) REFERENCES `utente` (`cf`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-14 13:08:39
