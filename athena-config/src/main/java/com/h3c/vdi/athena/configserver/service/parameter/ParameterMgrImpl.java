package com.h3c.vdi.athena.configserver.service.parameter;

import com.h3c.vdi.athena.common.config.SSHConfig;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.utils.JsonUtils;
import com.h3c.vdi.athena.common.utils.RedisUtil;
import com.h3c.vdi.athena.common.utils.SSHExecutor;
import com.h3c.vdi.athena.configserver.config.ssh.SSHProperties;
import com.h3c.vdi.athena.configserver.feign.NetDiskFeignClient;
import com.h3c.vdi.athena.configserver.mapper.ParameterMapper;
import com.h3c.vdi.athena.configserver.model.dto.ConfigDTO;
import com.h3c.vdi.athena.configserver.model.entity.Parameter;
import com.jcraft.jsch.JSchException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
@Service("parameterMgr")
public class ParameterMgrImpl implements ParameterMgr {

    @Resource
    private SSHProperties sshProperties;

    @Resource
    private ParameterMapper parameterMapper;

    @Resource
    private NetDiskFeignClient netDiskFeignClient;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public ConfigDTO queryParameters() throws JSchException {

        List<Parameter> configs = queryParams();
        return configConverter(configs);
    }

    @Override
    public ConfigDTO queryPathsOfParams() {
        List<Parameter> configs = queryParams();
        return configPathsConverter(configs);
    }

    private ConfigDTO configPathsConverter(List<Parameter> parameters) {
        ConfigDTO configDTO = new ConfigDTO();

        for (Parameter parameter : parameters) {
            String name = parameter.getName();
            String value = parameter.getValue();

            if (Parameter.NAME_FILE_SYSTEM_PATH.equals(name)) {
                if(StringUtils.isNotEmpty(value)){
                    configDTO.setFileSystemPath(Arrays.asList(value.split(",")));
                }
            }
            if (Parameter.NAME_FILE_SYSTEM_TYPE.equals(name)){
                configDTO.setFileSystemType(value);
            }
            if(Parameter.NAME_FILE_SYSTEM_CAPACITY.equals(name)){
                if(StringUtils.isNotEmpty(value)){
                    configDTO.setCapacity(Integer.parseInt(value));
                }
            }
        }
        return configDTO;
    }

    private List<Parameter> queryParams() {
        return parameterMapper.selectAll();
    }


    @Override
    public void updateParameters(ConfigDTO configDTO) {
        List<Parameter> configs = queryParams();
        for (Parameter parameter: configs){
            String name = parameter.getName();
            if(Parameter.NAME_FILE_SYSTEM_PATH.equals(name)){
                parameter.setValue(String.join(",",configDTO.getFileSystemPath()));
            }
            if(Parameter.NAME_FILE_SYSTEM_TYPE.equals(name)){
                parameter.setValue(configDTO.getFileSystemType());
            }
            if(Parameter.NAME_FILE_SYSTEM_CAPACITY.equals(name)){
                parameter.setValue(configDTO.getCapacity().toString());
            }
            parameterMapper.update(parameter);
        }
    }

    @Override
    public void updateParameters(String name, String value) {

        List<Parameter> configs = queryParams();
        for (Parameter parameter: configs){
            if(parameter.getName().equals(name)){
                parameter.setValue(value);
                parameterMapper.update(parameter);
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modifyQuota(String quota) {
        this.updateParameters(Parameter.NAME_NETDISK_QUOTA, quota);
        this.netDiskFeignClient.modifyQuota(quota);
    }

    @Override
    public ConfigDTO queryQuota() {
        ConfigDTO configDTO = new ConfigDTO();
        configDTO.setQuota(getValueByName(Parameter.NAME_NETDISK_QUOTA));
        return configDTO;
    }

    private String getValueByName(String configName){
        List<Parameter> configs = queryParams();
        String configValue = "";
        for (Parameter parameter:configs){
            String name = parameter.getName();
            String value = parameter.getValue();
            if(configName.equals(name)){
                configValue = value;
                break;
            }
        }
        return configValue;
    }

    /**
     * 修改宿主机ip并保存到redis里
     * @param host 主机ip
     */
    @Override
    public void modifyHost(String host) {
        this.updateParameters(Parameter.NAME_HOST_IP, host);
        redisUtil.set(RedisUtil.REDIS_KEY_HOST, host);
        redisUtil.persist(RedisUtil.REDIS_KEY_HOST);
        SSHConfig sshConfig = this.getSSHConfig();
        String configValue = JsonUtils.toJson(sshConfig);
        redisUtil.set(RedisUtil.REDIS_KEY_SSHCONFIG, configValue);
        redisUtil.persist(RedisUtil.REDIS_KEY_SSHCONFIG);
    }

    @Override
    public SSHConfig getSSHConfig() {

        String host;
        if(redisUtil.hasKey(RedisUtil.REDIS_KEY_HOST)){
            host = (String) redisUtil.get(RedisUtil.REDIS_KEY_HOST);
        } else {
            host = getValueByName(Parameter.NAME_HOST_IP);
            if(StringUtils.isEmpty(host)){
                throw new AppException("请先配置服务器IP地址");
            }
        }

        SSHConfig sshConfig = SSHConfig.builder()
                .host(host)
                .port(sshProperties.getPort())
                .username(sshProperties.getUsername())
                .password(sshProperties.getPassword())
                .build();

        return sshConfig;
    }

    @Override
    public void reStoreSSHConfig() {
        String host = getValueByName(Parameter.NAME_HOST_IP);
        redisUtil.set(RedisUtil.REDIS_KEY_HOST, host);
        redisUtil.persist(RedisUtil.REDIS_KEY_HOST);
        SSHConfig sshConfig = this.getSSHConfig();
        String configValue = JsonUtils.toJson(sshConfig);
        redisUtil.set(RedisUtil.REDIS_KEY_SSHCONFIG, configValue);
        redisUtil.persist(RedisUtil.REDIS_KEY_SSHCONFIG);
    }

    private ConfigDTO configConverter(List<Parameter> parameters) {
        ConfigDTO configDTO = new ConfigDTO();
        for (Parameter parameter : parameters) {
            String name = parameter.getName();
            String value = parameter.getValue();

            if (Parameter.NAME_FILE_SYSTEM_PATH.equals(name)) {
                if (StringUtils.isNotEmpty(value)) {
                    configDTO.setFileSystemPath(Arrays.asList(value.split(",")));
                }
                configDTO.setUse(this.getFileSystemUse(Parameter.MOUNT_FILESYSTEM));
            }
            if (Parameter.NAME_FILE_SYSTEM_TYPE.equals(name)) {
                configDTO.setFileSystemType(value);
            }
            if (Parameter.NAME_FILE_SYSTEM_CAPACITY.equals(name)) {
                if (StringUtils.isNotEmpty(value)) {
                    configDTO.setCapacity(Integer.parseInt(value));
                }
            }
            if (Parameter.NAME_NETDISK_QUOTA.equals(name)) {
                configDTO.setQuota(value);
            }

            if (Parameter.NAME_HOST_IP.equals(name)) {
                configDTO.setHost(value);
            }
        }
        return configDTO;
    }

    private String[] getFileSystemUse(String fileSystem) {

        try (SSHExecutor ssh = SSHExecutor.newInstance(getSSHConfig())) {
            if (StringUtils.isEmpty(fileSystem)) {
                return new String[]{"0", "0", "0", "0"};
            }
            Set<String> cmd1 = ssh.exec("df -h | grep " + fileSystem);
            cmd1.remove("");
            if (CollectionUtils.isEmpty(cmd1)) {
                throw new AppException("没有这个挂载点。");
            }
            String result = cmd1.stream().findFirst().get();
            String[] results = result.split("\\s+");
            return new String[]{results[1], results[2], results[3], results[4]};
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"0", "0", "0", "0"};
        }
    }
}
