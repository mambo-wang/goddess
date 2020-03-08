package com.h3c.vdi.athena.webapp.service;

import com.h3c.vdi.athena.webapp.config.feign.FeignMultipartSupportConfig;
import com.h3c.vdi.athena.webapp.dto.ConfigDTO;
import com.h3c.vdi.athena.webapp.dto.DiskDTO;
import com.h3c.vdi.athena.webapp.dto.PreDataDTO;
import com.h3c.vdi.athena.webapp.dto.SkinDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/27
 */
@FeignClient(value = "athena-gateway", configuration = FeignMultipartSupportConfig.class)
public interface ConfigFeignService {

    @GetMapping(value = "/config/config/mount/paths")
    PreDataDTO queryDisks();

    /**
     * 查询子分区
     * @param path
     * @return
     */
    @Deprecated
    @GetMapping(value = "/config/config/mount/paths/more")
    List<DiskDTO> getMore(@RequestParam(value = "path")String path);

    @GetMapping(value = "/config/config")
    ConfigDTO queryConfigs();

    @GetMapping(value = "/config/config/skin/{username}")
    SkinDTO querySkinDTO(@PathVariable(value = "username")String username);

    @PutMapping(value = "/config/config/skin",consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateSkinConfig(@RequestBody SkinDTO skinDTO);

    @DeleteMapping(value = "/config/config/skin/{username}")
    void deleteSkinConfig(@PathVariable(value = "username")String username);

    @PutMapping(value = "/config/config", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateConfig(ConfigDTO configDTO);

    @PostMapping(value = "/config/config/nextcloud")
    void createNextCloud();

    @PutMapping(value = "/config/config/quota/{quota}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void modifyQuota(@PathVariable(value = "quota") String quota);

    @PutMapping(value = "/config/config/host/{host}")
    void modifyHostIP(@PathVariable(value = "host") String host);
}
