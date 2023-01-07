-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: projectuasnmp
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.24-MariaDB

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
-- Table structure for table `meme_comments`
--

DROP TABLE IF EXISTS `meme_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meme_comments` (
  `idmeme_comments` int(11) NOT NULL AUTO_INCREMENT,
  `users_id` int(11) NOT NULL,
  `memes_id` int(11) NOT NULL,
  `content` varchar(45) NOT NULL,
  `publish_date` datetime NOT NULL,
  PRIMARY KEY (`idmeme_comments`),
  KEY `fk_meme_comments_users1_idx` (`users_id`),
  KEY `fk_meme_comments_memes1_idx` (`memes_id`),
  CONSTRAINT `fk_meme_comments_memes1` FOREIGN KEY (`memes_id`) REFERENCES `memes` (`idmemes`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_meme_comments_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`idusers`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meme_comments`
--

LOCK TABLES `meme_comments` WRITE;
/*!40000 ALTER TABLE `meme_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `meme_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `memes`
--

DROP TABLE IF EXISTS `memes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `memes` (
  `idmemes` int(11) NOT NULL AUTO_INCREMENT,
  `image_url` varchar(100) NOT NULL,
  `top_text` varchar(45) NOT NULL,
  `bottom_text` varchar(45) NOT NULL,
  `num_likes` int(11) NOT NULL,
  `users_id` int(11) NOT NULL,
  PRIMARY KEY (`idmemes`),
  KEY `fk_memes_users_idx` (`users_id`),
  CONSTRAINT `fk_memes_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`idusers`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `memes`
--

LOCK TABLES `memes` WRITE;
/*!40000 ALTER TABLE `memes` DISABLE KEYS */;
INSERT INTO `memes` VALUES (1,'https://a.pinatafarm.com/2000x1124/b177c50844/guy-confused.jpg/m/522x0','WHAT','WHAT??',100,1),(6,'https://pbs.twimg.com/profile_images/1311952801677758465/TDzHQHyV_400x400.jpg','SUCH','WOW',1000,2);
/*!40000 ALTER TABLE `memes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `memes_likes`
--

DROP TABLE IF EXISTS `memes_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `memes_likes` (
  `idmemes` int(11) NOT NULL,
  `idusers` int(11) NOT NULL,
  PRIMARY KEY (`idmemes`,`idusers`),
  KEY `fk_memes_has_users_users1_idx` (`idusers`),
  KEY `fk_memes_has_users_memes1_idx` (`idmemes`),
  CONSTRAINT `fk_memes_has_users_memes1` FOREIGN KEY (`idmemes`) REFERENCES `memes` (`idmemes`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_memes_has_users_users1` FOREIGN KEY (`idusers`) REFERENCES `users` (`idusers`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `memes_likes`
--

LOCK TABLES `memes_likes` WRITE;
/*!40000 ALTER TABLE `memes_likes` DISABLE KEYS */;
/*!40000 ALTER TABLE `memes_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `idusers` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `password` varchar(45) NOT NULL,
  `date_regist` datetime NOT NULL,
  `avatar_img` varchar(100) NOT NULL,
  `privacy_setting` tinyint(4) NOT NULL,
  PRIMARY KEY (`idusers`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'johndoe','John','Doe','1234','2022-12-29 12:20:09','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDr1i25jqVHRFv8eBDwljZqXTSBVO60GRsiQ&usqp=CAU',0),(2,'roronini','roro',NULL,'roni123','2022-12-29 12:24:21','',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-01-07 10:38:23
