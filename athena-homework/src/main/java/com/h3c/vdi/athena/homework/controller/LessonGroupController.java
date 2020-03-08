package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.homework.dto.LessonGroupDTO;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.dto.UserGroupRegistrarDTO;
import com.h3c.vdi.athena.homework.entity.LessonGroup;
import com.h3c.vdi.athena.homework.service.lessongroup.LessonGroupService;
import com.h3c.vdi.athena.homework.service.usergroupregisrar.UserGroupRegistrarService;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckEvent;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author w14014
 * @date 2018/3/8
 */
@RestController
@RequestMapping(value = "/groups")
public class LessonGroupController {

    @Resource
    private LessonGroupService lessonGroupService;

    @Resource
    private UserGroupRegistrarService userGroupRegistrarService;

    @GetMapping(value = "/current_user")
    public List<LessonGroupDTO> queryGroupsByCurrentUser(){
        List<LessonGroup> byUser = lessonGroupService.queryGroupByCurrentLoginUser();
        return byUser.stream().map(lessonGroup -> this.lessonGroupService.convertGroupToDTO(lessonGroup)).collect(Collectors.toList());
    }

    @GetMapping(value = "/current_user/with_users")
    public List<LessonGroupDTO> queryGroupsByCurrentUserWithUsers(){
        List<LessonGroupDTO> byUser = lessonGroupService.queryGroupByCurrentLoginUserWithUsers();
        return byUser;
    }

    @GetMapping(value = "/username/{username}")
    public String queryGroupsByCurrentUserWithUsers(@PathVariable(name = "username")String username){
        String byUsername = lessonGroupService.queryGroupsByUsername(username);
        return byUsername;
    }

    @GetMapping
    public List<LessonGroupDTO> queryAllLessonGroups(){
        List<LessonGroup> all = lessonGroupService.queryAllGroups();
        return all.stream().map(this.lessonGroupService::convertGroupToDTO).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public LessonGroupDTO getGroupById(@PathVariable Long id){
        LessonGroup byId = lessonGroupService.findGroupById(id);
        return this.lessonGroupService.convertGroupToDTO(byId);
    }

    @PostMapping
    public List<LessonGroupDTO> addLessonGroup(@RequestBody LessonGroupDTO lessonGroupDTO){
        lessonGroupService.addGroup(lessonGroupDTO);
        return queryGroupsByCurrentUser();
    }

    @PutMapping
    public List<LessonGroupDTO> modifyLessonGroup(@RequestBody LessonGroupDTO lessonGroupDTO){
        lessonGroupService.modifyGroup(lessonGroupDTO);
        return queryGroupsByCurrentUser();
    }

    @DeleteMapping(value = "/{ids}")
    public List<LessonGroupDTO> removeLessonGroups(@PathVariable Long[] ids){
        lessonGroupService.removeGroup(Arrays.asList(ids));
        return queryGroupsByCurrentUser();
    }

    @DeleteMapping(value = "/users/{userId}")
    public void removeUserOutGroup(@PathVariable(value = "userId") Long userId){
        lessonGroupService.removeUserOutGroup(userId);
    }

    /**
     * 批量添加学生到课程组
     * @param lessonGroupId 课程组
     * @param userIds 用户id
     */
    @PostMapping(value = "/{lesson_group_id}/users/{user_ids}")
    public void addUserToGroup(@PathVariable(name = "lesson_group_id") Long lessonGroupId, @PathVariable(name = "user_ids")Long[] userIds){
        lessonGroupService.addUsersToGroup(lessonGroupId, Arrays.asList(userIds));
    }

    /**
     * 从课程组删除学生
     * @param lessonGroupId 课程组id
     * @param userIds 学生id
     */
    @DeleteMapping(value = "/{lesson_group_id}/users/{user_ids}")
    public void removeUserOutGroup(@PathVariable(name = "lesson_group_id")Long lessonGroupId, @PathVariable(name = "user_ids")Long[] userIds){
        lessonGroupService.removeUserOutGroup(lessonGroupId, Arrays.asList(userIds));
    }

    /**
     * 查询课程组内的所有学生
     * @param lessonGroupId 课程组id
     * @return 学生列表
     */
    @GetMapping(value = "/{lesson_group_id}/users")
    public List<UserDTO> queryUsersByGroup(@PathVariable(name = "lesson_group_id")Long lessonGroupId){
        return lessonGroupService.queryUserByGroup(lessonGroupId);
    }

    /**
     * 申请加入课程组
     * @param lessonGroupIds
     */
    @PostMapping("/my/{lesson_group_ids}")
    public void applyIntoGroup(@PathVariable(name = "lesson_group_ids") Long[] lessonGroupIds){
        lessonGroupService.addUserToGroupRegistrars(Arrays.asList(lessonGroupIds));
    }

    /**
     * 申请退出课程组
     * @param lessonGroupIds 课程组ids
     */
    @DeleteMapping(value = "/my/{lesson_group_ids}")
    public void quitOutGroup(@PathVariable(name = "lesson_group_ids") Long[] lessonGroupIds){
        lessonGroupService.removeUserOutGroup(lessonGroupIds);
    }

    /**
     * 查询未加入的课程组
     * @return 课程组
     */
    @GetMapping(value = "/my/not_in")
    public List<LessonGroupDTO> queryNotInGroupsByCurrentUser(){
        List<LessonGroup> lessonGroups = lessonGroupService.queryNotInGroupsByCurrentUser();
        return lessonGroups.stream().map(this.lessonGroupService::convertGroupToDTO).collect(Collectors.toList());
    }

    /**
     * 查询待处理的申请
     * @return 申请
     */
    @GetMapping(value = "/apply/unchecked")
    public List<UserGroupRegistrarDTO> queryUnhandled(){
        return userGroupRegistrarService.queryAllUnhandledUserGroupRegistrars();
    }

    /**
     * 查询所有申请
     * @return 申请
     */
    @GetMapping(value = "/apply")
    public List<UserGroupRegistrarDTO> queryAllApply(){
        return userGroupRegistrarService.queryAllUserGroupRegistrars();
    }
    /**
     * 处理注册申请
     * @param ids 申请id
     * @param event 处理意见
     */
    @PutMapping(value = "/apply/{ids}/check/{event}")
    public CommonDTO<Boolean> check(@PathVariable(value = "ids") Long[] ids, @PathVariable(value = "event") CheckEvent event) {
        Boolean b = userGroupRegistrarService.check(Stream.of(ids).collect(Collectors.toList()), event);
        CommonDTO commonDTO = new CommonDTO();
        commonDTO.setData(b);
        return commonDTO;
    }

}
