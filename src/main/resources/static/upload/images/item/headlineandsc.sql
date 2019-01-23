-- MySQL dump 10.13  Distrib 5.5.34, for Linux (x86_64)
--
-- Host: localhost    Database: o2odb
-- ------------------------------------------------------
-- Server version	5.5.34-log

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
-- Dumping data for table `tb_head_line`
--

LOCK TABLES `tb_head_line` WRITE;
/*!40000 ALTER TABLE `tb_head_line` DISABLE KEYS */;
INSERT INTO `tb_head_line` VALUES (11,'1','1','/upload/images/item/headtitle/2017061320315746624.jpg',1,1,'2017-06-13 20:31:57','2017-06-13 20:31:57'),(12,'2','2','/upload/images/item/headtitle/2017061320371786788.jpg',2,1,'2017-06-13 20:37:17','2017-06-13 20:37:17'),(14,'3','3','/upload/images/item/headtitle/2017061320393452772.jpg',3,1,'2017-06-13 20:39:34','2017-06-13 20:39:34'),(15,'4','4','/upload/images/item/headtitle/2017061320400198256.jpg',4,1,'2017-06-13 20:40:01','2017-06-13 20:40:01');
/*!40000 ALTER TABLE `tb_head_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `tb_shop_category`
--

LOCK TABLES `tb_shop_category` WRITE;
/*!40000 ALTER TABLE `tb_shop_category` DISABLE KEYS */;
INSERT INTO `tb_shop_category` VALUES (10,'二手市场','二手商品交易','/upload/images/item/shopcategory/2017061223272255687.png',100,'2017-06-04 20:10:58','2017-06-12 23:27:22',NULL),(11,'美容美发','美容美发','/upload/images/item/shopcategory/2017061223273314635.png',99,'2017-06-04 20:12:57','2017-06-12 23:27:33',NULL),(12,'美食饮品','美食饮品','/upload/images/item/shopcategory/2017061223274213433.png',98,'2017-06-04 20:15:21','2017-06-12 23:27:42',NULL),(13,'休闲娱乐','休闲娱乐','/upload/images/item/shopcategory/2017061223275121460.png',97,'2017-06-04 20:19:29','2017-06-12 23:27:51',NULL),(14,'旧车','旧车','/upload/images/item/shopcategory/2017060420315183203.png',80,'2017-06-04 20:31:51','2017-06-04 20:31:51',10),(15,'二手书籍','二手书籍','/upload/images/item/shopcategory/2017060420322333745.png',79,'2017-06-04 20:32:23','2017-06-04 20:32:23',10),(17,'护理','护理','/upload/images/item/shopcategory/2017060420372391702.png',76,'2017-06-04 20:37:23','2017-06-04 20:37:23',11),(18,'理发','理发','/upload/images/item/shopcategory/2017060420374775350.png',74,'2017-06-04 20:37:47','2017-06-04 20:37:47',11),(20,'大排档','大排档','/upload/images/item/shopcategory/2017060420460491494.png',59,'2017-06-04 20:46:04','2017-06-04 20:46:04',12),(22,'奶茶店','奶茶店','/upload/images/item/shopcategory/2017060420464594520.png',58,'2017-06-04 20:46:45','2017-06-04 20:46:45',12),(24,'密室逃生','密室逃生','/upload/images/item/shopcategory/2017060420500783376.png',56,'2017-06-04 20:50:07','2017-06-04 21:45:53',13),(25,'KTV','KTV','/upload/images/item/shopcategory/2017060420505834244.png',57,'2017-06-04 20:50:58','2017-06-04 20:51:14',13),(27,'培训教育','培训教育','/upload/images/item/shopcategory/2017061223280082147.png',96,'2017-06-04 21:51:36','2017-06-12 23:28:00',NULL),(28,'租赁市场','租赁市场','/upload/images/item/shopcategory/2017061223281361578.png',95,'2017-06-04 21:53:52','2017-06-12 23:28:13',NULL),(29,'程序设计','程序设计','/upload/images/item/shopcategory/2017060421593496807.png',50,'2017-06-04 21:59:34','2017-06-04 21:59:34',27),(30,'声乐舞蹈','声乐舞蹈','/upload/images/item/shopcategory/2017060421595843693.png',49,'2017-06-04 21:59:58','2017-06-04 21:59:58',27),(31,'演出道具','演出道具','/upload/images/item/shopcategory/2017060422114076152.png',45,'2017-06-04 22:11:40','2017-06-04 22:11:40',28),(32,'交通工具','交通工具','/upload/images/item/shopcategory/2017060422121144586.png',44,'2017-06-04 22:12:11','2017-06-04 22:12:11',28);
/*!40000 ALTER TABLE `tb_shop_category` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-24 21:40:41
