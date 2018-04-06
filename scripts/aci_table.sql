-- MySQL dump 10.13  Distrib 5.7.19, for osx10.12 (x86_64)
--
-- Host: localhost    Database: collinson
-- ------------------------------------------------------
-- Server version	5.7.19

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
-- Table structure for table `aci_user`
--

DROP TABLE IF EXISTS `aci_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aci_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `version` bigint(20) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `user_type` varchar(255) NOT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `modifier_id` bigint(20) DEFAULT NULL,
  `branch_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `verify` char(1) DEFAULT NULL,
  `introducer_id` bigint(20) DEFAULT NULL,
  `expires_in` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_username` (`username`),
  KEY `FKrlj77ymttuppjd3f7xvuk7x1q` (`creator_id`),
  KEY `FK4h3955rsrfhasipafokaro9ff` (`modifier_id`),
  KEY `FK9yy0ya980j002yvtxi9r7kv6b` (`branch_id`),
  KEY `FKn82ha3ccdebhokx3a8fgdqeyy` (`role_id`),
  KEY `FKiqlul966n736s0uvexrlhdxcv` (`introducer_id`),
  CONSTRAINT `FK4h3955rsrfhasipafokaro9ff` FOREIGN KEY (`modifier_id`) REFERENCES `aci_user` (`id`),
  CONSTRAINT `FK9yy0ya980j002yvtxi9r7kv6b` FOREIGN KEY (`branch_id`) REFERENCES `aci_branch` (`id`),
  CONSTRAINT `FKiqlul966n736s0uvexrlhdxcv` FOREIGN KEY (`introducer_id`) REFERENCES `aci_user` (`id`),
  CONSTRAINT `FKn82ha3ccdebhokx3a8fgdqeyy` FOREIGN KEY (`role_id`) REFERENCES `aci_role` (`id`),
  CONSTRAINT `FKrlj77ymttuppjd3f7xvuk7x1q` FOREIGN KEY (`creator_id`) REFERENCES `aci_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10084 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aci_role`
--

DROP TABLE IF EXISTS `aci_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aci_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `version` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `modifier_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgiycbu32u3ohnrmlp1xpl7bh4` (`creator_id`),
  KEY `FKoowdh5engkxou7i5796ivmkrg` (`modifier_id`),
  CONSTRAINT `FKgiycbu32u3ohnrmlp1xpl7bh4` FOREIGN KEY (`creator_id`) REFERENCES `aci_user` (`id`),
  CONSTRAINT `FKoowdh5engkxou7i5796ivmkrg` FOREIGN KEY (`modifier_id`) REFERENCES `aci_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aci_branch`
--

DROP TABLE IF EXISTS `aci_branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aci_branch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `version` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `modifier_id` bigint(20) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1gxi5vl5ufs2l8swye7g26n16` (`creator_id`),
  KEY `FK4mavupar7e5wr8jeuda2d49up` (`modifier_id`),
  KEY `FKqi6b3wpl77vybu6nwkh4jylib` (`parent_id`),
  CONSTRAINT `FK1gxi5vl5ufs2l8swye7g26n16` FOREIGN KEY (`creator_id`) REFERENCES `aci_user` (`id`),
  CONSTRAINT `FK4mavupar7e5wr8jeuda2d49up` FOREIGN KEY (`modifier_id`) REFERENCES `aci_user` (`id`),
  CONSTRAINT `FKqi6b3wpl77vybu6nwkh4jylib` FOREIGN KEY (`parent_id`) REFERENCES `aci_branch` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aci_permission`
--

DROP TABLE IF EXISTS `aci_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aci_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `version` bigint(20) DEFAULT NULL,
  `auth_key` varchar(255) DEFAULT NULL,
  `auth_uris` varchar(255) DEFAULT NULL,
  `display` varchar(255) DEFAULT NULL,
  `entity` varchar(255) DEFAULT NULL,
  `http_method` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `menu_url` varchar(255) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `modifier_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh9eedsmuc7u7f3wm3betx9822` (`creator_id`),
  KEY `FKcq45bqtlseybf27r7bdgo0k85` (`modifier_id`),
  CONSTRAINT `FKcq45bqtlseybf27r7bdgo0k85` FOREIGN KEY (`modifier_id`) REFERENCES `aci_user` (`id`),
  CONSTRAINT `FKh9eedsmuc7u7f3wm3betx9822` FOREIGN KEY (`creator_id`) REFERENCES `aci_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aci_rule`
--

DROP TABLE IF EXISTS `aci_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aci_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `version` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `params` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `modifier_id` bigint(20) DEFAULT NULL,
  `enable` char(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt563w3p6atvxp82y7mvd6usa1` (`creator_id`),
  KEY `FKp7rj1lrxysuwi3p1gxl73roti` (`modifier_id`),
  CONSTRAINT `FKp7rj1lrxysuwi3p1gxl73roti` FOREIGN KEY (`modifier_id`) REFERENCES `aci_user` (`id`),
  CONSTRAINT `FKt563w3p6atvxp82y7mvd6usa1` FOREIGN KEY (`creator_id`) REFERENCES `aci_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aci_role_permission`
--

DROP TABLE IF EXISTS `aci_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aci_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `version` bigint(20) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `modifier_id` bigint(20) DEFAULT NULL,
  `permission_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKagm093b5ownnb6cyft7c3kurj` (`creator_id`),
  KEY `FKi8iiyuadjhp8w70ynins1rgue` (`modifier_id`),
  KEY `FKf8yllw1ecvwqy3ehyxawqa1qp` (`permission_id`),
  KEY `FKa6jx8n8xkesmjmv6jqug6bg68` (`role_id`),
  CONSTRAINT `FKa6jx8n8xkesmjmv6jqug6bg68` FOREIGN KEY (`role_id`) REFERENCES `aci_role` (`id`),
  CONSTRAINT `FKagm093b5ownnb6cyft7c3kurj` FOREIGN KEY (`creator_id`) REFERENCES `aci_user` (`id`),
  CONSTRAINT `FKf8yllw1ecvwqy3ehyxawqa1qp` FOREIGN KEY (`permission_id`) REFERENCES `aci_permission` (`id`),
  CONSTRAINT `FKi8iiyuadjhp8w70ynins1rgue` FOREIGN KEY (`modifier_id`) REFERENCES `aci_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30329 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aci_role_permission_rule`
--

DROP TABLE IF EXISTS `aci_role_permission_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aci_role_permission_rule` (
  `role_permission_id` bigint(20) NOT NULL,
  `rule_id` bigint(20) NOT NULL,
  KEY `FKci40w9ke39k3pd4w2tkmt4l7d` (`rule_id`),
  KEY `FKhuxsojjj3aaynewjntl6seqvp` (`role_permission_id`),
  CONSTRAINT `FKci40w9ke39k3pd4w2tkmt4l7d` FOREIGN KEY (`rule_id`) REFERENCES `aci_rule` (`id`),
  CONSTRAINT `FKhuxsojjj3aaynewjntl6seqvp` FOREIGN KEY (`role_permission_id`) REFERENCES `aci_role_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-06 15:59:58
