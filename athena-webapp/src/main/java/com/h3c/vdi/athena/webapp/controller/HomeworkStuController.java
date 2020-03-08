package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.webapp.dto.HomeworkDTO;
import com.h3c.vdi.athena.webapp.dto.HomeworkSubDTO;
import com.h3c.vdi.athena.webapp.service.HomeworkFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by w16051 on 2018/4/17.
 */
@Api(value = "学生作业controller", tags = {"学生作业相关操作"})
@RestController
@RequestMapping(value = "/homework/my/homeworks")
public class HomeworkStuController {
    @Resource
    HomeworkFeignService homeworkFeignService;

    /**
     * 查询当前登录学生的所有作业
     * @return
     */
    @ApiOperation(value = "查询当前登录学生的所有作业",notes = "查询当前登录学生的所有作业")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping
    public List<HomeworkDTO> queryAllHomeworkSubDTOs(){
        return homeworkFeignService.queryAllHomeworkSubDTOs();
    }


    @ApiOperation(value = "查看作业详情",notes = "查看作业详情，包含作业题目和已提交的内容")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/{homeworkId}")
    public HomeworkSubDTO queryHomeworkSubDTOByHomeworkId(@PathVariable Long homeworkId){
        return homeworkFeignService.queryHomeworkSubDTOByHomeworkId(homeworkId);
    }


    @ApiOperation(value = "学生提交作业",notes = "学生提交作业")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PostMapping
    public void submitHomework(@RequestBody HomeworkSubDTO homeworkSubDTO){
        homeworkFeignService.submitHomework(homeworkSubDTO);
    }


    @ApiOperation(value = "学生修改作业",notes = "学生修改作业，老师批改之前都能修改")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping
    public void updateHomeworkSubmission(@RequestBody HomeworkSubDTO homeworkSubDTO){
        homeworkFeignService.updateHomeworkSubmission(homeworkSubDTO);
    }
}
