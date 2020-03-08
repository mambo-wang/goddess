package com.h3c.vdi.athena.homework.service.lessongroup;

import com.h3c.vdi.athena.homework.dto.LessonGroupDTO;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.entity.LessonGroup;

import java.util.List;

/**
 * Created by w14014 on 2018/3/8.
 *
 * 课程组业务层接口
 */
public interface LessonGroupService {


    /**
     * 根据老师的用户名查询课程组
     * @param username 用户名
     * @return 课程组
     */
    List<LessonGroup> queryGroupByUsername(String username);

    /**
     * 根据老师的用户名查询课程组
     * @param userId 用户id
     * @return 课程组
     */
    List<LessonGroup> queryGroupByUserId(Long userId);

    /**
     * 根据当前登陆用户查询课程组
     * @return 课程组
     */
    List<LessonGroup> queryGroupByCurrentLoginUser();

    List<LessonGroupDTO> queryGroupByCurrentLoginUserWithUsers();

    /**
     * 根据当前登陆学生查未加入的课程组
     * @return 课程组
     */
    List<LessonGroup> queryNotInGroupsByCurrentUser();

    /**
     * 根据当前登陆用户查询课程组
     * @return 课程组
     */
    List<LessonGroup> queryAllGroups();

    /**
     * 根据Id查询
     * @param groupId 课程组id
     * @return 课程组
     */
    LessonGroup findGroupById(Long groupId);

    /**
     * 根据Id查询
     * @param groupId 课程组id
     * @return 课程组DTO
     */
    LessonGroupDTO findGroupDTOById(Long groupId);

    /**
     * 添加课程组
     * @param lessonGroupDTO 课程组DTO
     */
    void addGroup(LessonGroupDTO lessonGroupDTO);

    /**
     * 修改课程组
     * @param lessonGroupDTO 课程组DTO
     */
    void modifyGroup(LessonGroupDTO lessonGroupDTO);

    /**
     * 删除课程组
     * @param ids 课程组ids
     */
    void removeGroup(List<Long> ids);

    /**
     * 添加单个学生进课程组
     * @param groupId 课程组id
     * @param userIds 用户id
     */
    void addUsersToGroup(Long groupId, List<Long> userIds);

    /**
     * 添加当前用户至多个组
     * @param groupIds 课程组ids
     */
    void addUserToGroupRegistrars(List<Long> groupIds);

    /**
     * 删除课程组中的多个学生
     * @param groupId 课程组id
     * @param userIds 学生ids
     */
    void removeUserOutGroup(Long groupId, List<Long> userIds);

    /**
     * 移除课程组中的某一个学生
     * @param groupId 课程组id
     * @param userId 用户id
     */
    void removeUserOutGroup(Long groupId, Long userId);

    void removeUserOutGroup(Long[] groupIds);

    /**
     * 根据课程组查询学生
     * @param groupId 课程组id
     * @return 学生列表
     */
    List<UserDTO> queryUserByGroup(Long groupId);

    /**
     * 根据课程组查询学生, 数据统计专用接口
     * @param groupId 课程组id
     * @return 学生列表
     */
    List<UserDTO> queryUserNotDeletedByGroup(Long groupId);

    /**
     * 将Group实体转换为DTO
     * @param lessonGroup 课程组
     * @param users 用户，可传可不传
     * @return 课程组DTO
     */
    LessonGroupDTO convertGroupToDTO(LessonGroup lessonGroup, UserDTO... users);

    List<LessonGroup> queryGroupByName(String name);

    void removeUserOutGroup(Long userId);

    void removeGroupByUserId(Long userId);

    String queryGroupsByUsername(String username);
}
