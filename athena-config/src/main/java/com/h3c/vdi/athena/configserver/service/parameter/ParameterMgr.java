package com.h3c.vdi.athena.configserver.service.parameter;

import com.h3c.vdi.athena.common.config.SSHConfig;
import com.h3c.vdi.athena.configserver.model.dto.ConfigDTO;
import com.jcraft.jsch.JSchException;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
public interface ParameterMgr {

    /**
     * 查询参数
     * @return 参数
     * @throws JSchException ssh异常
     */
    ConfigDTO queryParameters() throws JSchException;

    /**
     *
     * @return 查询挂载相关配置
     */
    ConfigDTO queryPathsOfParams();

    /**
     * 修改参数配置
     * @param configDTO 参数配置信息
     */
    void updateParameters(ConfigDTO configDTO);

    /**
     * 修改指定键值的参数
     * @param name 键
     * @param value 值
     */
    void updateParameters(String name, String value);

    /**
     * 修改网盘配额
     * @param quota 网盘配额
     */
    void modifyQuota(String quota);

    /**
     * 查询网盘配额配置
     * @return 网盘配额
     */
    ConfigDTO queryQuota();

    /**
     * 修改服务器ip配置
     * @param host 服务器ip
     */
    void modifyHost(String host);

    /**
     * 获取ssh配置
     * @return ssh相关配置
     */
    SSHConfig getSSHConfig();

    /**
     * 重新保存ssh配置和服务器ip配置到redis
     */
    void reStoreSSHConfig();
}
