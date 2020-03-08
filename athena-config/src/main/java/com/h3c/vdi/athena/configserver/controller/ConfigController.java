package com.h3c.vdi.athena.configserver.controller;

import com.h3c.vdi.athena.configserver.model.dto.ConfigDTO;
import com.h3c.vdi.athena.configserver.model.dto.PreDataDTO;
import com.h3c.vdi.athena.configserver.model.dto.SkinDTO;
import com.h3c.vdi.athena.configserver.model.entity.MountInfo;
import com.h3c.vdi.athena.configserver.service.config.ConfigMgr;
import com.h3c.vdi.athena.configserver.service.parameter.ParameterMgr;
import com.h3c.vdi.athena.configserver.service.skin.SkinMgr;
import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/18
 */
@Slf4j
@RestController
@RequestMapping(value = "/config")
public class ConfigController {

    @Resource
    private ConfigMgr configMgr;

    @Resource
    private SkinMgr skinMgr;

    @Resource
    private ParameterMgr parameterMgr;

    /**
     * 查询某个用户的皮肤编号
     * @param username 用户名
     * @return 皮肤设置
     */
    @GetMapping(value = "/skin/{username}")
    public SkinDTO querySkinDTO(@PathVariable(value = "username")String username){
        return skinMgr.querySkinConfig(username);
    }

    /**
     * 修改某个用户的皮肤编号
     * @param skinDTO 皮肤配置
     */
    @PutMapping(value = "/skin")
    public void updateSkinConfig(@RequestBody SkinDTO skinDTO){
        skinMgr.updateSkinConfig(skinDTO);
    }

    /**
     * 删除某个用户的皮肤设置，删除用户或者归档时使用
     * @param username 用户名
     */
    @DeleteMapping(value = "/skin/{username}")
    public void deleteSkinConfig(@PathVariable(value = "username")String username){
        skinMgr.deleteSkinConfig(username);
    }

    /**
     * 获取所有可用分区列表
     * @return 可用分区列表
     */
    @GetMapping(value = "/mount/paths")
    public PreDataDTO getPaths(){
        log.info("get mounted paths");
        return configMgr.queryDisks();
    }

    @PostMapping(value = "/nextcloud")
    public void createNextCloud(){
        configMgr.deployNextCloud();
    }

    /**
     * 获取配置
     * @return 配置
     * @throws JSchException ssh错误
     */
    @GetMapping
    public ConfigDTO queryConfigs() throws JSchException {
        return parameterMgr.queryParameters();
    }

    @PutMapping
    public void updateConfigs(@RequestBody ConfigDTO configDTO){
        configMgr.updateConfig(configDTO);
    }

    @GetMapping(value = "/mounts")
    public List<MountInfo> queryMountInfos(){
        return configMgr.queryMountInfos();
    }

    @PutMapping(value = "/host/{host:.+}")
    public void modifyHostIp(@PathVariable(value = "host")String host){
        this.parameterMgr.modifyHost(host);
    }

    @PutMapping(value = "/quota/{quota:.+}")
    public void modifyQuota(@PathVariable(value = "quota")String quota){
        this.parameterMgr.modifyQuota(quota);
    }

    @GetMapping(value = "/quota")
    public ConfigDTO queryQuota(){
        return this.parameterMgr.queryQuota();
    }
}
