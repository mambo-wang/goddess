package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.webapp.service.HomeworkFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by w16051 on 2018/10/16.
 */
@Api(value = "数据归档controller", tags = {"数据归档相关操作"})
@RestController
@RequestMapping(value = "/homework/archive")
public class ArchiveController {

    @Resource
    HomeworkFeignService homeworkFeignService;

    @ApiOperation(value = "执行归档操作",notes = "根据入学年份进行归档")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping(value = "/{year}")
    public void archive(@PathVariable String year){
        homeworkFeignService.archive(year);
    }

    @ApiOperation(value = "查询所有入学年份",notes = "查询所有未删除未删除的学生的入学年份")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "years")
    public List<String> getYears(){
        return homeworkFeignService.getYears();
    }
}
