package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.webapp.dto.HomeworkDTO;
import com.h3c.vdi.athena.webapp.dto.HomeworkSubDTO;
import com.h3c.vdi.athena.webapp.dto.ShowHomeworkInfoDTO;
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
@Api(value = "老师作业controller", tags = {"老师作业相关操作"})
@RestController
@RequestMapping(value = "/homework/homeworks")
public class HomeworkController {
    @Resource
    HomeworkFeignService homeworkFeignService;

    @ApiOperation(value = "查询当前登录人发布的作业",notes = "查询当前登录人发布的作业")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping
    public List<HomeworkDTO> queryHomeworkDTOs()
    {
        return homeworkFeignService.queryHomeworkDTOs();
    }

    @ApiOperation(value = "查询某作业",notes = "根据作业id查询某作业")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/{id}")
    public HomeworkDTO queryHomeworkDTO(@PathVariable Long id)
    {
        return homeworkFeignService.queryHomeworkDTO(id);
    }

    @ApiOperation(value = "提交作业",notes = "提交作业")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PostMapping
    public void addHomework(@RequestBody HomeworkDTO homeworkDTO){
        homeworkFeignService.addHomework(homeworkDTO);
    }

    @ApiOperation(value = "修改作业",notes = "修改作业")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping
    public void updateHomework(@RequestBody HomeworkDTO homeworkDTO){
        homeworkFeignService.updateHomework(homeworkDTO);
    }


    @ApiOperation(value = "删除作业",notes = "删除作业，连带删除该作业下学生提交的作业和附件")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping(value = "/{id}")
    public void deleteHomework(@PathVariable Long id){
        homeworkFeignService.deleteHomework(id);
    }


    @ApiOperation(value = "查询某作业的所有学生作业提交的状况",notes = "查询某作业的所有学生作业提交的状况")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/{id}/submitted_works")
    public List<HomeworkSubDTO> queryAllHomeworkSubmissions(@PathVariable Long id){
        return homeworkFeignService.queryAllHomeworkSubmissions(id);
    }

    @ApiOperation(value = "修改是否展示",notes = "修改是否展示")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping(value = "/submitted_works/showed")
    public void changeShowStatus(@RequestBody ShowHomeworkInfoDTO showHomeworkInfoDTO){
        homeworkFeignService.changeShowStatus(showHomeworkInfoDTO);
    }


    @ApiOperation(value = "获取某学生提交作业详情给老师批改",notes = "获取某学生提交作业详情给老师批改")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/submitted_works/{id}")
    public HomeworkSubDTO getHomeworkSubDTO(@PathVariable Long id){
        return homeworkFeignService.getHomeworkSubDTO(id);
    }


    @ApiOperation(value = "给作业打分",notes = "输入提交作业的id、分数、老师点评")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping(value = "/submitted_works")
    public void setScore(@RequestBody HomeworkSubDTO homeworkSubDTO){
        homeworkFeignService.setScore(homeworkSubDTO);
    }


    @ApiOperation(value = "查询某教师作业被展示的学生作业",notes = "查询某教师作业被展示的学生作业")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/showed_homeworks/{homeworkId}")
    public List<HomeworkSubDTO> queryShowedHomeworkDTOs(@PathVariable Long homeworkId){
        return homeworkFeignService.queryShowedHomeworkDTOs(homeworkId);
    }


    @ApiOperation(value = "查询某课程组下面所有教师作业",notes = "查询某课程组下面所有教师作业")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/lessonGroup/{groupId}")
    public List<HomeworkDTO> queryHomeworkDTOsByLessonGroupId(@PathVariable Long groupId){
        return homeworkFeignService.queryHomeworkDTOsByLessonGroupId(groupId);
    }

    @ApiOperation(value = "根据课程组id查询该课程组下面被展示的作业",notes = "根据课程组id查询该课程组下面被展示的作业")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/showed_homeworks/lessonGroup/{id}")
    public List<HomeworkSubDTO> queryShowedHomeworkDTOsByGroupId(@PathVariable Long id){
        return homeworkFeignService.queryShowedHomeworkDTOsByGroupId(id);
    }
}
