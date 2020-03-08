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

-- 导出 athena-homework 的数据库结构
CREATE DATABASE IF NOT EXISTS `athena-homework` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `athena-homework`;


-- 导出  表 athena-homework.tbl_attachments 结构
CREATE TABLE IF NOT EXISTS `tbl_attachments` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `NAME` varchar(512) NOT NULL COMMENT '附件名称',
  `TYPE` int(11) DEFAULT NULL COMMENT '0：学生作业，1：教师布置作业，2：教师模板',
  `RELATED_ID` bigint(20) DEFAULT NULL COMMENT '关联表的ID，有学生作业表，教师作业表和教师模板表三种可能',
  `URL` varchar(1024) NOT NULL COMMENT '资源位置',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 athena-homework.tbl_class 结构
CREATE TABLE IF NOT EXISTS `tbl_class` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(256) NOT NULL COMMENT '班级名称',
  `CODE` varchar(256) NOT NULL COMMENT '班级编号',
  `CREATE_TIME` date NOT NULL COMMENT '入学时间',
  `COLLEGE_ID` bigint(20) NOT NULL COMMENT '所属学院ID',
  `MONITOR_ID` bigint(20) DEFAULT NULL COMMENT '班长id',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否删除',
  `INVITE_CODE` int(11) DEFAULT NULL COMMENT '班长邀请码',
  PRIMARY KEY (`ID`),
  KEY `FKet89aqwsw6ndny7s8rqx7w9dj` (`COLLEGE_ID`),
  CONSTRAINT `FKet89aqwsw6ndny7s8rqx7w9dj` FOREIGN KEY (`COLLEGE_ID`) REFERENCES `tbl_college` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='班级';

-- 数据导出被取消选择。


-- 导出  表 athena-homework.tbl_college 结构
CREATE TABLE IF NOT EXISTS `tbl_college` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `NAME` varchar(256) NOT NULL COMMENT '学院名称',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学院表';

-- 数据导出被取消选择。


-- 导出  表 athena-homework.tbl_comment 结构
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


-- 导出  表 athena-homework.tbl_homework 结构
CREATE TABLE IF NOT EXISTS `tbl_homework` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '创建教师的ID',
  `GROUP_ID` bigint(20) NOT NULL COMMENT '所属课程组的ID',
  `NAME` varchar(256) NOT NULL COMMENT '作业名称',
  `CONTENT` text COMMENT '作业内容',
  `CREATE_TIME` bigint(20) DEFAULT NULL COMMENT '作业下发时间',
  `MODIFIED_TIME` bigint(20) DEFAULT NULL COMMENT '作业修改时间',
  `DEADLINE` bigint(20) DEFAULT NULL COMMENT '作业提交截止时间',
  `IS_SAVED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否保存为模板，n：不保存，y：保存',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`),
  KEY `FK33tnhii8i3r2q4lilxbedkqii` (`GROUP_ID`),
  CONSTRAINT `FK33tnhii8i3r2q4lilxbedkqii` FOREIGN KEY (`GROUP_ID`) REFERENCES `tbl_lesson_group` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教师下发作业表';

-- 数据导出被取消选择。


-- 导出  表 athena-homework.tbl_homework_praise 结构
CREATE TABLE IF NOT EXISTS `tbl_homework_praise` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `HOMEWORK_SUB_ID` bigint(20) NOT NULL COMMENT '提交作业的ID',
  `USER_ID` bigint(20) NOT NULL COMMENT '参与互动的用户的ID',
  `CREATE_TIME` datetime NOT NULL COMMENT '点赞的时间',
  `MODIFIED_TIME` datetime DEFAULT NULL COMMENT '点赞修改时间',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='点赞';

-- 数据导出被取消选择。


-- 导出  表 athena-homework.tbl_homework_submit 结构
CREATE TABLE IF NOT EXISTS `tbl_homework_submit` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '提交作业的用户的ID',
  `HOMEWORK_ID` bigint(20) NOT NULL COMMENT '对应老师发布作业的ID',
  `SUBMIT_TIME` bigint(20) NOT NULL COMMENT '作业最后一次提交时间',
  `SCORE_TIME` bigint(20) DEFAULT NULL COMMENT '批改时间',
  `ANSWER` text COMMENT '答题栏内容',
  `SCORE` int(11) DEFAULT NULL COMMENT '分数',
  `COMMENT` text COMMENT '教师点评',
  `STARRED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否被展示，n：不被展示，y：被展示',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  `modified_time` bigint(20) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `homework_sub_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKn4yuokmladbnwrn3m1bc2w3ae` (`HOMEWORK_ID`),
  CONSTRAINT `FKn4yuokmladbnwrn3m1bc2w3ae` FOREIGN KEY (`HOMEWORK_ID`) REFERENCES `tbl_homework` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提交作业表';

-- 数据导出被取消选择。


-- 导出  表 athena-homework.tbl_homework_template 结构
CREATE TABLE IF NOT EXISTS `tbl_homework_template` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '创建教师的ID',
  `NAME` varchar(256) NOT NULL COMMENT '作业名称',
  `CONTENT` text COMMENT '作业内容',
  `CREATE_TIME` date NOT NULL COMMENT '模板创建时间',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='作业模板表';

-- 数据导出被取消选择。


-- 导出  表 athena-homework.tbl_lesson_group 结构
CREATE TABLE IF NOT EXISTS `tbl_lesson_group` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建课程组的用户ID',
  `NAME` varchar(256) NOT NULL DEFAULT '0' COMMENT '课程组名称',
  `MEMBER_LIMIT` int(11) DEFAULT NULL COMMENT '课程组学生人数限制',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='课程组表';

-- 数据导出被取消选择。


-- 导出  表 athena-homework.tbl_registrar 结构
CREATE TABLE IF NOT EXISTS `tbl_registrar` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户的主键',
  `USERNAME` varchar(128) NOT NULL COMMENT '登录名',
  `PASSWORD` varchar(256) NOT NULL COMMENT '密码',
  `NAME` varchar(128) NOT NULL COMMENT '用户姓名',
  `EMAIL_ADDRESS` varchar(128) DEFAULT NULL COMMENT '邮箱地址',
  `CLASS_ID` bigint(20) NOT NULL COMMENT '班级主键',
  `SUBMIT_TIME` bigint(20) NOT NULL COMMENT '请求提交时间',
  `STATUS` varchar(50) NOT NULL DEFAULT 'UNCHECKED' COMMENT 'UNCHECKED:未处理，PASSED:通过，UNPASSED:拒绝，KICKED：踢出班级',
  `HANDLE_TIME` bigint(20) DEFAULT NULL COMMENT '班长最后一次处理的时间',
  `COMMENTS` varchar(128) DEFAULT NULL COMMENT '班长处理意见',
  `MOBILE_NO` varchar(128) DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='注册用户表';

-- 数据导出被取消选择。


-- 导出  表 athena-homework.tbl_user_class 结构
CREATE TABLE IF NOT EXISTS `tbl_user_class` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `USER_ID` bigint(20) NOT NULL COMMENT '学生主键ID',
  `CLASS_ID` bigint(20) NOT NULL COMMENT '班级主键ID',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生班级中间表';

-- 数据导出被取消选择。


-- 导出  表 athena-homework.tbl_user_group 结构
CREATE TABLE IF NOT EXISTS `tbl_user_group` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `GROUP_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '课程组ID',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生-课程组中间表';

-- 数据导出被取消选择。


-- 导出  表 athena-homework.tbl_user_group_registrar 结构
CREATE TABLE IF NOT EXISTS `tbl_user_group_registrar` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `USER_ID` bigint(20) NOT NULL COMMENT '学生用户ID',
  `GROUP_ID` bigint(20) NOT NULL COMMENT '课程组ID',
  `SUBMIT_TIME` bigint(20) NOT NULL COMMENT '请求提交时间',
  `STATUS` varchar(50) NOT NULL DEFAULT 'UNCHECKED' COMMENT 'UNCHECKED:未处理，PASSED:通过，UNPASSED:拒绝',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 athena-homework.test 结构
CREATE TABLE IF NOT EXISTS `test` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `col` bigint(20) DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
