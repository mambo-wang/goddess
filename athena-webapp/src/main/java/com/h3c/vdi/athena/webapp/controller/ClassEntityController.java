package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.webapp.dto.ClassEntityDTO;
import com.h3c.vdi.athena.webapp.service.HomeworkFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by z13339 on 2018/3/3.
 */
@Api(value = "班级controller", tags = {"班级相关操作"})
@RestController
public class ClassEntityController {
    private Logger logger = LoggerFactory.getLogger(ClassEntityController.class);

    @Resource
    HomeworkFeignService homeworkFeignService;

    @ApiOperation(value = "查找所有班级",notes = "查找所有班级")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/classEntity" , method = RequestMethod.GET)
    public List<ClassEntityDTO> findAllClasses() {
        return homeworkFeignService.findAllClasses();
    }

    @ApiOperation(value = "根据collegeId查找所有班级",notes = "根据collegeId查找所有班级")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/classEntity/college/{collegeId}" , method = RequestMethod.GET)
    public List<ClassEntityDTO> findAllClasses(@ApiParam(value = "学院ID")@PathVariable(name = "collegeId") Long collegeId) {
        return homeworkFeignService.findAllClassesByCollegeId(collegeId);
    }

    @ApiOperation(value = "添加班级",notes = "添加班级")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/classEntity" , method = RequestMethod.POST)
    public Integer addClassEntity(@RequestBody ClassEntityDTO classEntityDTO) {
        return homeworkFeignService.addClassEntity(classEntityDTO);
    }


    @ApiOperation(value = "修改班级",notes = "修改班级")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/classEntity" , method = RequestMethod.PUT)
    public void updateClassEntity(@RequestBody ClassEntityDTO classEntityDTO) {
        homeworkFeignService.updateClassEntity(classEntityDTO);
    }

    @ApiOperation(value = "重置班级的邀请码",notes = "重置班级的邀请码")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/resetInviteCode/{classId}",method = RequestMethod.PUT)
    public Integer resetInviteCode(@ApiParam(value = "等待重置邀请码的班级")@PathVariable(name = "classId") Long classId){
        return homeworkFeignService.resetInviteCode(classId);
    }

    @ApiOperation(value = "删除班级",notes = "删除班级")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = {"/homework/classEntity/{idList}"}, method = RequestMethod.DELETE)
    public void deleteClassEntityBatch(@PathVariable(name = "idList") ArrayList<Long> idList) {
        CommonDTO commonDTO = new CommonDTO();
        commonDTO.setData(idList);
        homeworkFeignService.deleteClassEntityBatch(commonDTO);
    }



}
