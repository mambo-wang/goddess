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

-- 导出 athena-keystone 的数据库结构
CREATE DATABASE IF NOT EXISTS `athena-keystone` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `athena-keystone`;


-- 导出  表 athena-keystone.tbl_attachments 结构
CREATE TABLE IF NOT EXISTS `tbl_attachments` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `NAME` varchar(512) NOT NULL COMMENT '附件名称',
  `TYPE` int(11) unsigned NOT NULL COMMENT '0：学生作业，1：教师布置作业，2：教师模板',
  `RELATED_ID` bigint(20) NOT NULL COMMENT '关联表的ID，有学生作业表，教师作业表和教师模板表三种可能',
  `URL` varchar(1024) NOT NULL COMMENT '资源位置',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 athena-keystone.tbl_comment 结构
CREATE TABLE IF NOT EXISTS `tbl_comment` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `TYPE` int(2) NOT NULL DEFAULT '1' COMMENT '1：学生作业，2：老师作业',
  `RELATION_ID` bigint(20) NOT NULL COMMENT '作业ID，有可能是老师作业，也有可能是学生作业',
  `CONTENT` varchar(1024) NOT NULL COMMENT '评论的内容',
  `CREATE_TIME` bigint(20) NOT NULL COMMENT '提交评论的时间',
  `USER_ID` bigint(20) NOT NULL COMMENT '用户主键ID',
  `FLOOR` int(10) NOT NULL COMMENT '楼层',
  `TARGET` varchar(256) DEFAULT NULL COMMENT '回复的对象，楼层加用户姓名，形如“x楼xxx”',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论';

-- 数据导出被取消选择。


-- 导出  表 athena-keystone.tbl_homework 结构
CREATE TABLE IF NOT EXISTS `tbl_homework` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '创建教师的ID',
  `GROUP_ID` bigint(20) NOT NULL COMMENT '所属课程组的ID',
  `NAME` varchar(256) NOT NULL COMMENT '作业名称',
  `CONTENT` text COMMENT '作业内容',
  `CREATE_TIME` date DEFAULT NULL COMMENT '作业下发时间',
  `MODIFIED_TIME` date DEFAULT NULL COMMENT '作业修改时间',
  `DEADLINE` date DEFAULT NULL COMMENT '作业提交截止时间',
  `IS_SAVED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否保存为模板，n：不保存，y：保存',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教师下发作业表';

-- 数据导出被取消选择。


-- 导出  表 athena-keystone.tbl_homework_praise 结构
CREATE TABLE IF NOT EXISTS `tbl_homework_praise` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `HOMEWORK_SUB_ID` bigint(20) NOT NULL COMMENT '提交作业的ID',
  `USER_ID` bigint(20) NOT NULL COMMENT '参与互动的用户的ID',
  `CREATE_TIME` date NOT NULL COMMENT '点赞的时间',
  `MODIFIED_TIME` date DEFAULT NULL COMMENT '点赞修改时间',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='点赞';

-- 数据导出被取消选择。


-- 导出  表 athena-keystone.tbl_homework_submit 结构
CREATE TABLE IF NOT EXISTS `tbl_homework_submit` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '提交作业的用户的ID',
  `HOMEWORK_ID` bigint(20) NOT NULL COMMENT '对应老师发布作业的ID',
  `SUBMIT_TIME` date NOT NULL COMMENT '作业最后一次提交时间',
  `ANSWER` text COMMENT '答题栏内容',
  `SCORE` int(11) DEFAULT NULL COMMENT '分数',
  `COMMENT` text COMMENT '教师点评',
  `STARRED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否被展示，n：不被展示，y：被展示',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提交作业表';

-- 数据导出被取消选择。


-- 导出  表 athena-keystone.tbl_homework_template 结构
CREATE TABLE IF NOT EXISTS `tbl_homework_template` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '创建教师的ID',
  `NAME` varchar(256) NOT NULL COMMENT '作业名称',
  `CONTENT` text COMMENT '作业内容',
  `CREATE_TIME` date NOT NULL COMMENT '模板创建时间',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='作业模板表';

-- 数据导出被取消选择。


-- 导出  表 athena-keystone.tbl_lesson_group 结构
CREATE TABLE IF NOT EXISTS `tbl_lesson_group` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建课程组的用户ID',
  `NAME` varchar(256) NOT NULL DEFAULT '0' COMMENT '课程组名称',
  `MEMBER_LIMIT` int(11) DEFAULT NULL COMMENT '课程组学生人数限制',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='课程组表';

-- 数据导出被取消选择。


-- 导出  表 athena-keystone.tbl_role 结构
CREATE TABLE IF NOT EXISTS `tbl_role` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(50) NOT NULL COMMENT '角色名称',
  `SYS` varchar(255) NOT NULL COMMENT '角色所属系统名称',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- 数据导出被取消选择。


-- 导出  表 athena-keystone.tbl_user 结构
CREATE TABLE IF NOT EXISTS `tbl_user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USERNAME` varchar(128) NOT NULL COMMENT '登录名',
  `PASSWORD` varchar(256) NOT NULL COMMENT '密码',
  `NAME` varchar(128) NOT NULL COMMENT '用户姓名',
  `PHOTO` varchar(256) DEFAULT NULL COMMENT '头像',
  `MOBILE_NO` varchar(128) DEFAULT NULL COMMENT '手机号',
  `EMAIL_ADDRESS` varchar(128) DEFAULT NULL COMMENT '邮箱地址',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT '状态：n：正常数据  y：已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- 数据导出被取消选择。


-- 导出  表 athena-keystone.tbl_user_group 结构
CREATE TABLE IF NOT EXISTS `tbl_user_group` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `GROUP_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '课程组ID',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生-课程组中间表';

-- 数据导出被取消选择。


-- 导出  表 athena-keystone.tbl_user_role 结构
CREATE TABLE IF NOT EXISTS `tbl_user_role` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户主键',
  `ROLE_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色主键',
  PRIMARY KEY (`ID`),
  KEY `FK6phlytlf1w3h9vutsu019xor5` (`ROLE_ID`),
  KEY `FKggc6wjqokl2vlw89y22a1j2oh` (`USER_ID`),
  CONSTRAINT `FK6phlytlf1w3h9vutsu019xor5` FOREIGN KEY (`ROLE_ID`) REFERENCES `tbl_role` (`ID`),
  CONSTRAINT `FKggc6wjqokl2vlw89y22a1j2oh` FOREIGN KEY (`USER_ID`) REFERENCES `tbl_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色中间表';

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
