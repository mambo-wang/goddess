package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.webapp.dto.LessonGroupDTO;
import com.h3c.vdi.athena.webapp.dto.UserDTO;
import com.h3c.vdi.athena.webapp.dto.UserGroupRegistrarDTO;
import com.h3c.vdi.athena.webapp.enums.CheckEvent;
import com.h3c.vdi.athena.webapp.service.HomeworkFeignService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/3/12
 */
@Api(value = "课程组Controller", tags = "课程组相关操作")
@Slf4j
@RestController
@RequestMapping(value = "/homework")
public class LessonGroupController {

    @Resource
    private HomeworkFeignService homeworkFeignService;

    /**
     * 查询所有课程组
     * @return
     */
    @Deprecated
    @ApiOperation(value = "查询所有课程组",notes = "查询所有课程组信息")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/groups")
    public List<LessonGroupDTO> queryAllLessonGroups(){
        return homeworkFeignService.queryAllLessonGroups();
    }

    /**
     * 根据id查找
     * @param id 主键id
     * @return 课程组DTO
     */
    @ApiOperation(value = "根据id查找课程组", notes = "根据url的id来获取对应课程组的详细信息")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/groups/{id}")
    public LessonGroupDTO getGroupById(@ApiParam(value = "课程组id")@PathVariable Long id){
        return homeworkFeignService.getGroupById(id);
    }

    /**
     * 修改课程组
     * @param lessonGroupDTO 课程组信息
     * @return 当前登录用户的课程组
     */
    @ApiOperation(value = "修改课程组", notes = "根据lessonGroupDTO修改课程组数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
                    defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    })
    @PutMapping(value = "/groups")
    public List<LessonGroupDTO> modifyLessonGroup(@ApiParam(value = "课程组相关信息")@RequestBody LessonGroupDTO lessonGroupDTO){
        return homeworkFeignService.modifyLessonGroup(lessonGroupDTO);
    }

    /**
     * 添加课程组
     * @param lessonGroupDTO 课程组信息
     * @return 所有课程组
     */
    @ApiOperation(value = "添加课程组", notes = "根据lessonGroupDTO创建课程组实体")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @PostMapping(value = "/groups")
    public List<LessonGroupDTO> addLessonGroup(@ApiParam(value = "课程组相关信息")@RequestBody LessonGroupDTO lessonGroupDTO){
        return homeworkFeignService.addLessonGroup(lessonGroupDTO);
    }

    /**
     * 删除课程组
     * @param ids 要删除的课程组
     * @return 课程组
     */
    @ApiOperation(value = "删除课程组",notes = "根据id删除课程组")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @DeleteMapping(value = "/groups/{ids}")
    public List<LessonGroupDTO> removeLessonGroups(@ApiParam(value = "课程组ids")@PathVariable Long[] ids){
        return homeworkFeignService.removeLessonGroups(ids);
    }

    /**
     * 批量添加学生到课程组
     * @param lessonGroupId 课程组
     * @param userIds 用户id
     */
    @ApiOperation(value = "批量添加学生到课程组", notes = "根据用户id和课程组id实现批量添加学生到课程组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lesson_group_id", value = "课程组id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "user_ids", value = "用户id数组", required = true, dataType = "ArrayList", paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
                    defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    })
    @PostMapping(value = "/groups/{lesson_group_id}/users/{user_ids}")
    public void addUserToGroup(@PathVariable(name = "lesson_group_id") Long lessonGroupId, @PathVariable(name = "user_ids")Long[] userIds){
        homeworkFeignService.addUserToGroup(lessonGroupId, userIds);
    }

    /**
     * 批量从某课程组移除学生
     * @param lessonGroupId 课程组id
     * @param userIds 用户id
     */
    @ApiOperation(value = "批量从某课程组移除学生", notes = "根据用户id和课程组id实现批量移除学生")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lesson_group_id", value = "课程组id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "user_ids", value = "用户id数组", required = true, dataType = "ArrayList", paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
                    defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    })
    @DeleteMapping(value = "/groups/{lesson_group_id}/users/{user_ids}")
    public void removeUserOutGroup(@PathVariable(name = "lesson_group_id")Long lessonGroupId, @PathVariable(name = "user_ids")Long[] userIds){
        homeworkFeignService.removeUserOutGroup(lessonGroupId, userIds);
    }

    /**
     * 根据课程组id查询学生
     * @param lessonGroupId 课程组id
     * @return 该课程组中的学生
     */
    @ApiOperation(value = "根据课程组id查询学生")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/groups/{lesson_group_id}/users")
    public List<UserDTO> queryUsersByGroup(@ApiParam(value = "课程组id")@PathVariable(name = "lesson_group_id")Long lessonGroupId){
        return homeworkFeignService.queryUsersByGroup(lessonGroupId);
    }

    /**
     * 根据当前登录用户（学生或老师）查询加入或拥有的课程组
     * @return 课程组
     */
    @ApiOperation(value = "查询当前登录用户已加入或者拥有的课程组")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/groups/my")
    public List<LessonGroupDTO> queryGroupsByCurrentUser(){
        return homeworkFeignService.queryGroupsByCurrentUser();
    }


    /**
     * 根据当前登录用户（学生或老师）查询加入或拥有的课程组
     * @return 课程组
     */
    @ApiOperation(value = "查询所有课程组及其用户", notes = "分享文件时使用")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/groups/with_users")
    public List<LessonGroupDTO> queryGroupsByCurrentUserWithUsers(){
        return homeworkFeignService.queryGroupsByCurrentUserWithUsers();
    }

    /**
     * 根据当前登录用户（学生或老师）查询加入或拥有的课程组
     * @return 课程组
     */
    @ApiOperation(value = "查询当前登录用户未加入的课程组")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/groups/my/not_in")
    public List<LessonGroupDTO> queryNotInGroupsByCurrentUser(){
        return homeworkFeignService.queryNotInGroupsByCurrentUser();
    }

    /**
     * 当前登录用户（学生）申请加入到某课程组
     * @param lessonGroupIds 课程组
     */
    @ApiOperation(value = "当前登录用户申请加入到某课程组")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @PostMapping(value = "/groups/my/{lesson_group_ids}")
    public void applyIntoGroup(@ApiParam(value = "课程组id数组")@PathVariable(name = "lesson_group_ids") Long[] lessonGroupIds){
        homeworkFeignService.applyIntoGroup(lessonGroupIds);
    }

    /**
     * 从某课程组移除当前登陆用户（学生）
     * @param lessonGroupIds 课程组
     */
    @ApiOperation(value = "从课程组中移除当前登陆用户")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @DeleteMapping(value = "/groups/my/{lesson_group_ids}")
    public void quitOutGroup(@ApiParam(value = "课程组id数组")@PathVariable(name = "lesson_group_ids") Long[] lessonGroupIds){
        homeworkFeignService.quitOutGroup(lessonGroupIds);
    }


    /**
     * 查询待处理的申请
     * @return 申请
     */
    @ApiOperation(value = "查询待处理的申请")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/groups/apply/unchecked")
    public List<UserGroupRegistrarDTO> queryUnhandled(){
        return homeworkFeignService.queryUnhandled();
    }

    /**
     * 查询所有申请
     * @return 申请
     */
    @ApiOperation(value = "查询所有申请")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/groups/apply")
    public List<UserGroupRegistrarDTO> queryAllApply(){
        return homeworkFeignService.queryAllApply();
    }
    /**
     * 处理注册申请
     * @param ids 申请id
     * @param event 处理意见
     */
    @ApiOperation(value = "处理注册申请")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @PutMapping(value = "/groups/apply/{ids}/check/{event}")
    public CommonDTO<Boolean> check(@ApiParam(value = "申请id数组")@PathVariable(value = "ids") Long[] ids,
                                    @ApiParam(value = "处理意见：PASS,UNPASS")@PathVariable(value = "event") CheckEvent event) {
        return homeworkFeignService.check(ids,event);
    }
}
