package com.h3c.vdi.athena.netdisk.mappers;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
public interface AppConfigMapper {

    /** 修改密码最小允许长度为1 */
    @Update("update appconfig set configvalue='1' where appid='password_policy' and configkey='minLength';")
    void modifyPasswordLength();

    /** 修改密码最小允许长度为1 */
    @Select("select configvalue from appconfig where appid='password_policy' and configkey='minLength';")
    String getPasswordLength();

    /** 允许普通密码 */
    @Update("update appconfig set configvalue='0' where appid='password_policy' and configkey='enforceNonCommonPassword';")
    void enforceNonCommonPassword();

    @Select("select configvalue from appconfig where appid='password_policy' and configkey='enforceNonCommonPassword';")
    String selectNonCommonPassword();

    /** 禁止检查更新 */
    @Update("update appconfig set configvalue='no' where appid='updatenotification' and configkey='enabled';")
    void updateNotification();

    @Select("select configvalue from appconfig where appid='updatenotification' and configkey='enabled';")
    String getNotification();

    /** 禁止使用情况调查 */
    @Update("update appconfig set configvalue='no' \n" +
            "where appid='survey_client' and configkey in ('enabled','apps','database','encryption','files_sharing','php','server','stats');")
    void surveyClient();

    @Select("select configvalue from appconfig \n" +
            "where appid='survey_client' and configkey in ('enabled','apps','database','encryption','files_sharing','php','server','stats');")
    List<String> getSurveyClient();




}
