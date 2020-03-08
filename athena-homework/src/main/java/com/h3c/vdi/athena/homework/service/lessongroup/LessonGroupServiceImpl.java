package com.h3c.vdi.athena.homework.service.lessongroup;

import com.h3c.vdi.athena.common.constant.BasicConstant;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.common.utils.StringManager;
import com.h3c.vdi.athena.homework.dao.LessonGroupDao;
import com.h3c.vdi.athena.homework.dao.UserGroupRegistrarDao;
import com.h3c.vdi.athena.homework.dao.UserGroupRelationDao;
import com.h3c.vdi.athena.homework.dto.LessonGroupDTO;
import com.h3c.vdi.athena.homework.dto.RoleDTO;
import com.h3c.vdi.athena.homework.dto.RoleType;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.entity.LessonGroup;
import com.h3c.vdi.athena.homework.entity.UserGroupRelation;
import com.h3c.vdi.athena.homework.exception.ErrorCodes;
import com.h3c.vdi.athena.homework.feign.keystone.UserFeignService;
import com.h3c.vdi.athena.homework.feign.netdisk.NetDiskFeignService;
import com.h3c.vdi.athena.homework.service.homework.HomeworkService;
import com.h3c.vdi.athena.homework.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by w14014 on 2018/3/8.
 */
@Service(value = "lessonGroupService")
public class LessonGroupServiceImpl implements LessonGroupService {

    @Resource
    private LessonGroupDao lessonGroupDao;

    @Resource
    private UserGroupRelationDao userGroupRelationDao;

    @Resource
    private UserService userService;

    @Resource
    private UserFeignService userFeignService;

    @Resource
    private NetDiskFeignService netDiskFeignService;

    @Resource
    private UserGroupRegistrarDao userGroupRegistrarDao;

    @Resource
    private HomeworkService homeworkService;

    private StringManager sm = StringManager.getManager("LessonGroup");

    @Override
    public List<LessonGroup> queryGroupByUsername(String username) {
        UserDTO byUsername = this.userFeignService.findByUsername(username);
        return queryGroupByUserId(byUsername.getId());
    }

    @Override
    public List<LessonGroup> queryGroupByUserId(Long userId) {
        return lessonGroupDao.findByUserIdAndDeleted(userId, BasicConstant.IS_DELETED_N);
    }

    @Override
    public List<LessonGroupDTO> queryGroupByCurrentLoginUserWithUsers() {

        List<LessonGroup> lessonGroups = queryAllGroups();
        List<LessonGroupDTO> allStus = lessonGroups.stream()
                .map(this::convertGroupToDTOWithoutOwner)
                .map(dto -> {
                    dto.setUsers(this.queryUserByGroup(dto.getId()));
                    return dto;
                })
                .collect(Collectors.toList());

        LessonGroupDTO stuNotInGroup = new LessonGroupDTO();
        stuNotInGroup.setName(sm.getString("stu.out.group"));
        //查询不在这个set中的学生用户
        List<BigInteger> userIds = userGroupRelationDao.findUserIdsNotDeleted();
        List<Long> userLongIds = userIds.stream().map(BigInteger::longValue).collect(Collectors.toList());
        List<UserDTO> dtos = userFeignService.queryUsersNotInGroup(userLongIds.toArray(new Long[userLongIds.size()]));
        stuNotInGroup.setUsers(dtos);
        allStus.add(stuNotInGroup);

        LessonGroupDTO teacherGroup = new LessonGroupDTO();
        teacherGroup.setName(sm.getString("tea.all"));
        //查询所有老师用户
        teacherGroup.setUsers(userFeignService.findAllDTOs(RoleType.TEACHER.getValue()));
        allStus.add(teacherGroup);

        return allStus;
    }

    @Override
    public List<LessonGroup> queryGroupByCurrentLoginUser() {
        UserDTO currentLoginUser = this.userService.currentLoginUser();
        String roleName = userService.getRoleName(currentLoginUser);
        //如果是老师，查出来他创建的课程组
        if(StringUtils.equals(roleName, RoleType.TEACHER.getName()) ||
                StringUtils.equals(roleName, RoleType.ADMIN.getName())){
            return queryGroupByUserId(currentLoginUser.getId());
        }else {
            return lessonGroupDao.findByStudentId(currentLoginUser.getId(), BasicConstant.IS_DELETED_N);
        }
    }

    @Override
    public List<LessonGroup> queryNotInGroupsByCurrentUser() {

        UserDTO currentLoginUser = this.userService.currentLoginUser();
        return lessonGroupDao.findNotInGroupsByUserId(currentLoginUser.getId(), BasicConstant.IS_DELETED_N);
    }

    @Override
    public List<LessonGroup> queryAllGroups() {
        return lessonGroupDao.findByDeleted(BasicConstant.IS_DELETED_N);
    }

    @Override
    public LessonGroup findGroupById(Long groupId) {
        LessonGroup byId = lessonGroupDao.findByIdAndDeleted(groupId, BasicConstant.IS_DELETED_N);
        if(Objects.isNull(byId)){
            throw new AppException(ErrorCodes.GROUP_NOT_FOUND);
        }
        return byId;
    }

    @Override
    public LessonGroupDTO findGroupDTOById(Long groupId) {
        LessonGroup lessonGroup = this.findGroupById(groupId);
        return this.convertGroupToDTO(lessonGroup);
    }

    /**
     * @param lessonGroupDTO 课程组DTO
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void addGroup(LessonGroupDTO lessonGroupDTO) {

        List<LessonGroup> byName = this.queryGroupByName(lessonGroupDTO.getName());
        if(CollectionUtils.isEmpty(byName)){
            LessonGroup toAdd = this.convertDTOToGroup(lessonGroupDTO);
            lessonGroupDao.save(toAdd);
            //添加nextcloud用户分组
//            netDiskFeignService.createGroup(lessonGroupDTO.getName());
        } else {
            throw new AppException(ErrorCodes.GROUP_EXISTS);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modifyGroup(LessonGroupDTO lessonGroupDTO) {

        List<LessonGroup> byName = lessonGroupDao.findByIdNotAndNameAndDeleted(lessonGroupDTO.getId(), lessonGroupDTO.getName(), BasicConstant.IS_DELETED_N);

        if(CollectionUtils.isEmpty(byName)){
            LessonGroup toModify = this.convertDTOToGroup(lessonGroupDTO);
            lessonGroupDao.save(toModify);
        } else {
            throw new AppException(ErrorCodes.GROUP_EXISTS);
        }
    }

    //TODO待测
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeGroup(List<Long> ids) {
        //删除nextcloud分组
//        List<String> groupNames = ids.stream()
//                .map(this::findGroupById)
//                .map(LessonGroup::getName)
//                .collect(Collectors.toList());
//        this.netDiskFeignService.removeGroup(groupNames.toArray(new String[groupNames.size()]));
        if(!CollectionUtils.isEmpty(ids)){
            this.lessonGroupDao.deleteGroupByIds(ids);
            this.userGroupRelationDao.deleteRelationByGroupIds(ids);
            this.homeworkService.deleteHomeworkByGroupId(ids);
        }
    }

    @Override
    public void addUsersToGroup(Long groupId, List<Long> userIds) {

        Set<Long> userIdsInGroup = userGroupRelationDao.findUserIdsByGroupId(groupId);

        List<UserGroupRelation> userGroupRelations = userIds.stream()
                .filter(userId -> !userIdsInGroup.contains(userId))
                .map(userId -> {
                    UserGroupRelation userGroupRelation = new UserGroupRelation();
                    userGroupRelation.setUserId(userId);
                    userGroupRelation.setGroupId(groupId);
                    userGroupRelation.setDeleted(BasicConstant.IS_DELETED_N);
                    return userGroupRelation;
                }).collect(Collectors.toList());

        userGroupRelationDao.save(userGroupRelations);
        //调用netdisk
//        this.netDiskFeignService.addUserToGroup(groupId, userIds);
    }

    @Override
    public void addUserToGroupRegistrars(List<Long> groupIds) {
        UserDTO userDTO = userService.currentLoginUser();
        Long userId = userDTO.getId();
        groupIds.forEach(groupId -> userGroupRegistrarDao.addUserGroupRegistrar(userId, groupId, System.currentTimeMillis()));
    }

    @Override
    public void removeUserOutGroup(Long groupId, List<Long> userIds) {
        userIds.forEach(userId -> removeUserOutGroup(groupId, userId));
        //调用netdisk
//        netDiskFeignService.removeUserFromGroup(groupId, userIds);
    }

    @Override
    public void removeUserOutGroup(Long groupId, Long userId) {
        userGroupRelationDao.deleteRelation(groupId, userId);
    }

    @Override
    public void removeUserOutGroup(Long[] groupIds) {
        UserDTO userDTO = userService.currentLoginUser();
        Long userId = userDTO.getId();
        Stream.of(groupIds).forEach(groupId -> removeUserOutGroup(groupId, userId));
        //调用netdisk
//        Stream.of(groupIds).forEach(groupId -> netDiskFeignService.removeUserFromGroup(groupId, Collections.singletonList(userId)));
    }

    @Override
    public List<UserDTO> queryUserByGroup(Long groupId) {
        Set<Long> userIds = userGroupRelationDao.findUserIdsByGroupId(groupId);
        if(CollectionUtils.isEmpty(userIds)){
            return Collections.emptyList();
        }
        List<UserDTO> userDTOS = userFeignService.queryUserByIds(userIds.toArray(new Long[userIds.size()]));
        return userDTOS;
    }

    @Override
    public List<UserDTO> queryUserNotDeletedByGroup(Long groupId) {
        List<Long> userIds = userGroupRelationDao.findUserIdsNotDeletedByGroupId(groupId);
        if(CollectionUtils.isEmpty(userIds)){
            return Collections.emptyList();
        }
        return userFeignService.queryUserByIds(userIds.toArray(new Long[userIds.size()]));
    }

    @Override
    public LessonGroupDTO convertGroupToDTO(LessonGroup lessonGroup, UserDTO... users){
        LessonGroupDTO lessonGroupDTO = new LessonGroupDTO();
        lessonGroupDTO.setId(lessonGroup.getId());
        lessonGroupDTO.setName(lessonGroup.getName());
        Set<Long> userIdsInGroup = userGroupRelationDao.findUserIdsByGroupId(lessonGroup.getId());
        lessonGroupDTO.setMemberLimit(userIdsInGroup.size());
        if(Objects.nonNull(users) && users.length > 0){
            lessonGroupDTO.setUser(users[0]);
        } else {
            lessonGroupDTO.setUser(userService.queryUserById(lessonGroup.getUserId()));
        }
        return lessonGroupDTO;
    }

    private LessonGroupDTO convertGroupToDTOWithoutOwner(LessonGroup lessonGroup){
        LessonGroupDTO lessonGroupDTO = new LessonGroupDTO();
        lessonGroupDTO.setId(lessonGroup.getId());
        lessonGroupDTO.setName(lessonGroup.getName());
        lessonGroupDTO.setMemberLimit(lessonGroup.getMemberLimit());
        return lessonGroupDTO;
    }

    private LessonGroup convertDTOToGroup(LessonGroupDTO lessonGroupDTO) {
        LessonGroup lessonGroup = new LessonGroup();
        lessonGroup.setDeleted(BasicConstant.IS_DELETED_N);
        lessonGroup.setId(lessonGroupDTO.getId());
        lessonGroup.setMemberLimit(lessonGroupDTO.getMemberLimit());
        lessonGroup.setName(lessonGroupDTO.getName());
        if(Objects.isNull(lessonGroupDTO.getUser())){
            UserDTO currentUser = userService.currentLoginUser();
            lessonGroup.setUserId(currentUser.getId());
        } else {
            lessonGroup.setUserId(lessonGroupDTO.getUser().getId());
        }
        return lessonGroup;
    }

    @Override
    public List<LessonGroup> queryGroupByName(String name) {
        return lessonGroupDao.findByNameAndDeleted(name, BasicConstant.IS_DELETED_N);
    }

    @Override
    public void removeUserOutGroup(Long userId) {
        this.userGroupRelationDao.deleteRelationByUserId(userId);
    }

    @Override
    public void removeGroupByUserId(Long userId) {
        List<Long> ids = this.lessonGroupDao.findByUserId(userId);
        this.removeGroup(ids);
    }

    @Override
    public String queryGroupsByUsername(String username) {

        UserDTO userDTO = userFeignService.findByUsername(username);

        List<String> roles = userDTO.getRoleList().stream().map(RoleDTO::getName).collect(Collectors.toList());
        String desc = null;
        if(roles.contains(RoleType.TEACHER.getName())){
            List<LessonGroup> lessonGroups = this.queryGroupByUserId(userDTO.getId());
            if(!CollectionUtils.isEmpty(lessonGroups)){
                try {
                    desc =  lessonGroups.stream()
                            .map(LessonGroup::getId)
                            .map(this::findGroupById)
                            .map(LessonGroup::getName)
                            .collect(Collectors.joining(","));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(roles.contains(RoleType.STUDENT.getName())){
            //学生
            List<BigInteger> ids = this.userGroupRelationDao.findGroupIdsByUserIdsNotDeleted(userDTO.getId());
            try {
                desc = ids.stream()
                        .map(BigInteger::longValue)
                        .map(this::findGroupById)
                        .map(LessonGroup::getName)
                        .collect(Collectors.joining(","));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return desc;
    }
}
