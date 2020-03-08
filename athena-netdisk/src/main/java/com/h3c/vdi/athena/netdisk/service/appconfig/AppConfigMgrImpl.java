package com.h3c.vdi.athena.netdisk.service.appconfig;

import com.h3c.vdi.athena.common.utils.RedisUtil;
import com.h3c.vdi.athena.netdisk.mappers.AppConfigMapper;
import com.h3c.vdi.athena.netdisk.service.disk.NetDiskMgr;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/27
 */
@Service("appConfigMgr")
public class AppConfigMgrImpl implements AppConfigMgr {

    private static final String PASSWORD_LENGTH = "1";
    private static final String COMMON_PASSWORD = "0";
    private static final String NO = "no";

    @Resource
    private AppConfigMapper appConfigMapper;

    private void modifyPasswordLength() {
        String length = appConfigMapper.getPasswordLength();
        if(!StringUtils.equals(length, PASSWORD_LENGTH)){
            appConfigMapper.modifyPasswordLength();
        }
    }

    private void enforceNonCommonPassword() {
        String common = appConfigMapper.selectNonCommonPassword();
        if(!StringUtils.equals(common, COMMON_PASSWORD)){
            appConfigMapper.enforceNonCommonPassword();
        }
    }

    private void updateNotification() {
        String notification = appConfigMapper.getNotification();
        if(!StringUtils.equals(notification, NO)){
            appConfigMapper.updateNotification();
        }
    }

    private void surveyClient() {
        List<String> survey = appConfigMapper.getSurveyClient();
        boolean b = survey.stream().anyMatch(s -> !StringUtils.equals(s, NO));
        if(b){
            appConfigMapper.surveyClient();
        }

    }

    @Override
    public void initAppConfigIfNotInit() {
        this.modifyPasswordLength();
        this.enforceNonCommonPassword();
        this.surveyClient();
        this.updateNotification();
    }
}
