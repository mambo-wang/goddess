-- --------------------------------------------------------
-- 主机:                           192.168.0.225
-- 服务器版本:                        5.7.21 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 athena-config 的数据库结构
CREATE DATABASE IF NOT EXISTS `athena-config` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `athena-config`;


-- 导出  表 athena-config.tbl_mount_info 结构
CREATE TABLE IF NOT EXISTS `tbl_mount_info` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FILE_SYSTEM` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `MOUNT_POINT` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `TYPE` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `OPTIONS` varchar(128) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'defaults',
  `DUMP` int(2) NOT NULL DEFAULT '0',
  `PASS` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 数据导出被取消选择。


-- 导出  表 athena-config.tbl_parameter 结构
CREATE TABLE IF NOT EXISTS `tbl_parameter` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `TYPE` varchar(256) COLLATE utf8_unicode_ci NOT NULL COMMENT '类型',
  `NAME` varchar(256) COLLATE utf8_unicode_ci NOT NULL COMMENT '名称',
  `VALUE` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '值',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='参数配置表';

-- 数据导出被取消选择。


-- 导出  表 athena-config.tbl_skin_config 结构
CREATE TABLE IF NOT EXISTS `tbl_skin_config` (
  `USERNAME` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `SKIN_NUMBER` int(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
