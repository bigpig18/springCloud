-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- Server version:               8.0.15 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL 版本:                  10.1.0.5487
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for coupon_data
CREATE DATABASE IF NOT EXISTS `coupon_data` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `coupon_data`;

-- Dumping structure for table coupon_data.coupon
CREATE TABLE IF NOT EXISTS `coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `template_id` int(11) NOT NULL DEFAULT '0' COMMENT '关联优惠券模板的主键',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '领取用户',
  `coupon_code` varchar(64) NOT NULL DEFAULT '' COMMENT '优惠券码',
  `assign_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '领取时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '优惠券的状态',
  PRIMARY KEY (`id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='优惠券(用户领取的记录)';

-- Dumping data for table coupon_data.coupon: ~0 rows (approximately)
/*!40000 ALTER TABLE `coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupon` ENABLE KEYS */;

-- Dumping structure for table coupon_data.coupon_template
CREATE TABLE IF NOT EXISTS `coupon_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `available` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是可用状态; true: 可用, false: 不可用',
  `expired` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否过期; true: 是, false: 否',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '优惠券名称',
  `logo` varchar(256) NOT NULL DEFAULT '' COMMENT '优惠券 logo',
  `intro` varchar(256) NOT NULL DEFAULT '' COMMENT '优惠券描述',
  `category` varchar(64) NOT NULL DEFAULT '' COMMENT '优惠券分类',
  `product_line` int(11) NOT NULL DEFAULT '0' COMMENT '产品线',
  `coupon_count` int(11) NOT NULL DEFAULT '0' COMMENT '总数',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '创建时间',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建用户',
  `template_key` varchar(128) NOT NULL DEFAULT '' COMMENT '优惠券模板的编码',
  `target` int(11) NOT NULL DEFAULT '0' COMMENT '目标用户',
  `rule` varchar(1024) NOT NULL DEFAULT '' COMMENT '优惠券规则: TemplateRule 的 json 表示',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `idx_category` (`category`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='优惠券模板表';

-- Dumping data for table coupon_data.coupon_template: ~0 rows (approximately)
/*!40000 ALTER TABLE `coupon_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupon_template` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
