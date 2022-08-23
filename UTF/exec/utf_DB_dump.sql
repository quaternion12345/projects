-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: i7a701.p.ssafy.io    Database: utf8
-- ------------------------------------------------------
-- Server version	8.0.30

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
-- Table structure for table `emotion`
--

DROP TABLE IF EXISTS `emotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emotion` (
  `emotion_id` bigint NOT NULL AUTO_INCREMENT,
  `emotion_filtered` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `score` double DEFAULT NULL,
  `emotion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`emotion_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `focus`
--

DROP TABLE IF EXISTS `focus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `focus` (
  `focus_id` bigint NOT NULL AUTO_INCREMENT,
  `is_focus` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`focus_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lecture`
--

DROP TABLE IF EXISTS `lecture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecture` (
  `lecture_id` int NOT NULL AUTO_INCREMENT,
  `lecture_room_id` bigint DEFAULT NULL,
  `video_url` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `chat_record` json DEFAULT NULL,
  PRIMARY KEY (`lecture_id`),
  KEY `lecture-lecture_room-fk_idx` (`lecture_room_id`),
  CONSTRAINT `lecture-lecture_room-fk` FOREIGN KEY (`lecture_room_id`) REFERENCES `lecture_room` (`lecture_room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lecture_room`
--

DROP TABLE IF EXISTS `lecture_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecture_room` (
  `lecture_room_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(50) NOT NULL,
  `subject` int NOT NULL,
  `lecture_room_code` varchar(12) NOT NULL,
  `is_active` tinyint DEFAULT NULL,
  PRIMARY KEY (`lecture_room_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `lecture_room_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recent_lecture`
--

DROP TABLE IF EXISTS `recent_lecture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recent_lecture` (
  `recent_lecture_id` int NOT NULL AUTO_INCREMENT,
  `lecture_room_id` int DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`recent_lecture_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `statistics`
--

DROP TABLE IF EXISTS `statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statistics` (
  `statistics_id` int NOT NULL AUTO_INCREMENT,
  `lecture_id` int DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `not_understand` json DEFAULT NULL,
  `neutral` json DEFAULT NULL,
  `understand` json DEFAULT NULL,
  `not_focus` json DEFAULT NULL,
  `focus` json DEFAULT NULL,
  `understand_statistics_id` bigint DEFAULT NULL,
  `lecture_room_id` int DEFAULT NULL,
  `video_id` int NOT NULL,
  PRIMARY KEY (`statistics_id`),
  KEY `FK73anya2yroptdtwunna15iu65` (`lecture_id`),
  CONSTRAINT `FK73anya2yroptdtwunna15iu65` FOREIGN KEY (`lecture_id`) REFERENCES `lecture` (`lecture_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `social_login_type` int DEFAULT NULL,
  `social_id` varchar(255) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-08-19  9:53:10
