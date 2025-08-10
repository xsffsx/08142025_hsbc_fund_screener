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
-- Table structure for table `datasource`
--

DROP TABLE IF EXISTS `datasource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datasource` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'æ•°æ®æºåç§°',
  `type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'æ•°æ®æºç±»åž‹ï¼šmysql, postgresql',
  `host` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ä¸»æœºåœ°å€',
  `port` int NOT NULL COMMENT 'ç«¯å£å·',
  `database_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'æ•°æ®åº“åç§°',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ç”¨æˆ·å',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'å¯†ç ï¼ˆåŠ å¯†å­˜å‚¨ï¼‰',
  `connection_url` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'å®Œæ•´è¿žæŽ¥URL',
  `status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'active' COMMENT 'çŠ¶æ€ï¼šactive-å¯ç”¨ï¼Œinactive-ç¦ç”¨',
  `test_status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'unknown' COMMENT 'è¿žæŽ¥æµ‹è¯•çŠ¶æ€ï¼šsuccess-æˆåŠŸï¼Œfailed-å¤±è´¥ï¼Œunknown-æœªçŸ¥',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT 'æè¿°',
  `creator_id` bigint DEFAULT NULL COMMENT 'åˆ›å»ºè€…ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  PRIMARY KEY (`id`),
  KEY `idx_ds_name` (`name`),
  KEY `idx_ds_type` (`type`),
  KEY `idx_ds_status` (`status`),
  KEY `idx_ds_creator_id` (`creator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='æ•°æ®æºè¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datasource`
--
-- WHERE:  1

LOCK TABLES `datasource` WRITE;
/*!40000 ALTER TABLE `datasource` DISABLE KEYS */;
INSERT INTO `datasource` VALUES (1,'æœ¬åœ°MySQLæ•°æ®åº“','mysql','localhost',3306,'nl2sql','nl2sql_user','nl2sql_pass','jdbc:mysql://localhost:3306/nl2sql','active','success','æœ¬åœ°MySQLæ•°æ®åº“ï¼ŒåŒ…å«æ ¸å¿ƒä¸šåŠ¡æ•°æ®',2100246635,'2025-08-04 02:22:41','2025-08-04 02:22:41'),(2,'æ•°æ®ä»“åº“MySQL','mysql','localhost',3306,'data_warehouse','nl2sql_user','nl2sql_pass','jdbc:mysql://localhost:3306/data_warehouse','active','success','æ•°æ®ä»“åº“ï¼Œç”¨äºŽæ•°æ®åˆ†æžå’ŒæŠ¥è¡¨ç”Ÿæˆ',2100246635,'2025-08-04 02:22:41','2025-08-04 02:22:41'),(3,'æµ‹è¯•çŽ¯å¢ƒMySQL','mysql','192.168.1.102',3306,'test_db','test_user','encrypted_password_789','jdbc:mysql://192.168.1.102:3306/test_db','active','failed','æµ‹è¯•çŽ¯å¢ƒæ•°æ®åº“ï¼Œç”¨äºŽå¼€å‘æµ‹è¯•',2100246635,'2025-08-04 02:22:41','2025-08-04 02:22:41'),(4,'CDPå®¢æˆ·æ•°æ®å¹³å°','postgresql','192.168.1.103',5432,'customer_data','cdp_user','encrypted_password_abc','jdbc:postgresql://192.168.1.103:5432/customer_data','inactive','unknown','CDPå®¢æˆ·æ•°æ®å¹³å°ï¼ŒåŒ…å«å®¢æˆ·360åº¦æ•°æ®',2100246635,'2025-08-04 02:22:41','2025-08-04 02:22:41');
/*!40000 ALTER TABLE `datasource` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-04 14:05:59
