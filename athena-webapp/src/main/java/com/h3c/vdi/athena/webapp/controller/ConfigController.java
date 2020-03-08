package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.webapp.dto.ConfigDTO;
import com.h3c.vdi.athena.webapp.dto.DiskDTO;
import com.h3c.vdi.athena.webapp.dto.PreDataDTO;
import com.h3c.vdi.athena.webapp.dto.SkinDTO;
import com.h3c.vdi.athena.webapp.service.ConfigFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 *
 * @author w14014
 * @date 2018/9/27
 */
@Api(value = "配置中心controller", tags = {"配置中心相关操作"})
@Slf4j
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    private ConfigFeignService configFeignService;

    @ApiOperation(value = "获取所有可用分区列表")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/mount/paths")
    public PreDataDTO queryDisks(){
        return this.configFeignService.queryDisks();
    }

    @ApiOperation(value = "查询配置")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping
    public ConfigDTO queryConfigs(){
        return this.configFeignService.queryConfigs();
    }

    @ApiOperation(value = "更新配置信息",notes = "挂载")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @PutMapping
    public void updateConfigs(@ApiParam(value = "配置信息")@RequestBody ConfigDTO configDTO){

        configFeignService.updateConfig(configDTO);
    }

    @ApiOperation(value = "部署nextcloud容器",notes = "部署容器")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @PostMapping(value = "/nextcloud")
    public void deployNextCloud(){
        configFeignService.createNextCloud();
    }

    @ApiOperation(value = "查询皮肤设置",notes = "查询皮肤设置")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/skin/{username}")
    public SkinDTO querySkinDTO(@ApiParam(value = "用户名")@PathVariable(value = "username")String username){
        return configFeignService.querySkinDTO(username);
    }

    @ApiOperation(value = "修改皮肤设置",notes = "修改皮肤设置")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping(value = "/skin")
    public void updateSkinConfig(@ApiParam(value = "皮肤设置")@RequestBody SkinDTO skinDTO){
        configFeignService.updateSkinConfig(skinDTO);
    }

    @Deprecated
    @ApiOperation(value = "删除皮肤设置",notes = "删除皮肤设置")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @DeleteMapping(value = "/skin/{username}")
    public void deleteSkinConfig(@ApiParam(value = "用户名")@PathVariable(value = "username")String username){
        configFeignService.deleteSkinConfig(username);
    }

    @ApiOperation(value = "修改个人网盘配额",notes = "修改个人网盘配额")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @PutMapping(value = "/quota/{quota:.+}")
    public void modifyQuota(@ApiParam(value = "配额，数字，例如：5.5")@PathVariable(value = "quota")String quota){
        this.configFeignService.modifyQuota(quota);
    }

    @ApiOperation(value = "修改服务器IP地址",notes = "修改服务器IP地址")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @PutMapping(value = "/host/{host:.+}")
    public void modifyHostIP(@ApiParam(value = "服务器IP地址，例如：192.168.3.224")@PathVariable(value = "host")String host){
        this.configFeignService.modifyHostIP(host);
    }
}
