######################################################
################homework updates######################
######################################################
delimiter //
use athena-homework //
drop procedure if exists upgradeAthena //
create procedure upgradeAthena()
begin
CREATE TABLE IF NOT EXISTS `tbl_clas_teststst` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `CLASS_NO` varchar(50) NOT NULL COMMENT '班级代码',
  `CLASS_NAME` varchar(50) NOT NULL COMMENT '班级名称',
  `DATA_SOURCE` INT(11) NOT NULL DEFAULT '0' COMMENT '数据来源,0是正方，1本地，2云学堂',
  `DELETED` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0：正常数据  1：已删除',
  PRIMARY KEY (`ID`)
) COMMENT='班级';

end;
//
call upgradeAthena() //
drop procedure if exists upgradeAthena //

##########################################################################################################################################
######## keystone updates ##################################################################################################################
##########################################################################################################################################

create database if not exists homeworksys_demo //
USE `homeworksys_demo` //

drop procedure if exists upgradeAthenaBase //

create procedure upgradeAthenaBase()
begin


CREATE TABLE IF NOT EXISTS `tbl_classroom_teststst` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `NAME` varchar(256) NOT NULL COMMENT '教室名称',
  `ROOM_NUMBER` varchar(256) NOT NULL COMMENT '教室编号',
  `TERMINAL_AMOUNT` int(11) DEFAULT NULL COMMENT '座位数',
  `DESCRIPTION` varchar(1024) NOT NULL DEFAULT '' COMMENT '备注信息',
  `DATA_SOURCE` int(11) NOT NULL DEFAULT '0' COMMENT '数据来源,0是正方，1本地，2云学堂',
  `DELETED` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0：正常数据  1：已删除',
  PRIMARY KEY (`ID`)
) COMMENT='教室';

end;
//
call upgradeAthenaBase() //
drop procedure if exists upgradeAthenaBase //

