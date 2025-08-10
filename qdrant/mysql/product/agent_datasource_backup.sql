-- MySQL dump 10.13  Distrib 8.0.43, for Linux (aarch64)
--
-- Host: localhost    Database: nl2sql
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `agent_datasource`
--

DROP TABLE IF EXISTS `agent_datasource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agent_datasource` (
  `id` int NOT NULL AUTO_INCREMENT,
  `agent_id` int NOT NULL COMMENT 'æ™ºèƒ½ä½“ID',
  `datasource_id` int NOT NULL COMMENT 'æ•°æ®æºID',
  `is_active` tinyint DEFAULT '1' COMMENT 'æ˜¯å¦å¯ç”¨ï¼š0-ç¦ç”¨ï¼Œ1-å¯ç”¨',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_agent_datasource` (`agent_id`,`datasource_id`),
  KEY `idx_ads_agent_id` (`agent_id`),
  KEY `idx_ads_datasource_id` (`datasource_id`),
  KEY `idx_ads_is_active` (`is_active`),
  CONSTRAINT `agent_datasource_ibfk_1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE CASCADE,
  CONSTRAINT `agent_datasource_ibfk_2` FOREIGN KEY (`datasource_id`) REFERENCES `datasource` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='æ™ºèƒ½ä½“æ•°æ®æºå…³è”è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agent_datasource`
--
-- WHERE:  1

LOCK TABLES `agent_datasource` WRITE;
/*!40000 ALTER TABLE `agent_datasource` DISABLE KEYS */;
INSERT INTO `agent_datasource` VALUES (1,1,2,1,'2025-08-04 02:22:41','2025-08-04 02:22:41'),(2,2,1,1,'2025-08-04 02:22:41','2025-08-04 02:22:41'),(3,3,1,1,'2025-08-04 02:22:41','2025-08-04 02:22:41'),(4,4,1,1,'2025-08-04 02:22:41','2025-08-04 02:22:41');
/*!40000 ALTER TABLE `agent_datasource` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-04 14:06:00
