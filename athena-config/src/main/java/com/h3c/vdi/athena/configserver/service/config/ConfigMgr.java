package com.h3c.vdi.athena.configserver.service.config;

import com.h3c.vdi.athena.configserver.model.dto.ConfigDTO;
import com.h3c.vdi.athena.configserver.model.dto.DiskDTO;
import com.h3c.vdi.athena.configserver.model.dto.PreDataDTO;
import com.h3c.vdi.athena.configserver.model.dto.SkinDTO;
import com.h3c.vdi.athena.configserver.model.entity.MountInfo;

import java.util.List;
import java.util.Set;

/**
 *
 * @author w14014
 * @date 2018/9/18
 */
public interface ConfigMgr {

    /**
     * 查询磁盘分区
     * @return 磁盘飞去
     */
    PreDataDTO queryDisks();

    /**
     * 暂时不用
     * @return
     */
    @Deprecated
    List<MountInfo> queryMountInfos();

    /**
     * 修改配置
     * @param configDTO 挂载参数配置
     */
    void updateConfig(ConfigDTO configDTO);

    /**
     * 挂载逻辑卷
     */
    void mountLv();

    /**
     * 部署nextcloud
     */
    void deployNextCloud();


}
