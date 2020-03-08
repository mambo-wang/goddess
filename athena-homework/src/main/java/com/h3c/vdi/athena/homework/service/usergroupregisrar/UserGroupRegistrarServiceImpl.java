package com.h3c.vdi.athena.homework.service.usergroupregisrar;

import com.h3c.vdi.athena.homework.dao.UserGroupRegistrarDao;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.dto.UserGroupRegistrarDTO;
import com.h3c.vdi.athena.homework.entity.LessonGroup;
import com.h3c.vdi.athena.homework.entity.UserGroupRegistrar;
import com.h3c.vdi.athena.homework.service.lessongroup.LessonGroupService;
import com.h3c.vdi.athena.homework.service.user.UserService;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckEvent;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import com.h3c.vdi.athena.homework.stateMachine.handler.PersistStateMachineHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author w14014
 * @date 2018/10/16
 */
@Slf4j
@Service(value = "userGroupRegistrarService")
public class UserGroupRegistrarServiceImpl implements UserGroupRegistrarService {

    @Resource
    private UserGroupRegistrarDao userGroupRegistrarDao;

    @Resource
    private UserService userService;

    @Resource
    private PersistStateMachineHandler persistStateMachineHandler;

    @Resource
    private LessonGroupService lessonGroupService;

    @Override
    public void addUserGroupRegistrar(Long userId, Long groupId) {
        UserGroupRegistrar userGroupRegistrar = new UserGroupRegistrar();
        userGroupRegistrar.setUserId(userId);
        userGroupRegistrar.setGroupId(groupId);
        userGroupRegistrar.setSubmitTime(System.currentTimeMillis());
        userGroupRegistrar.setCheckStatus(CheckStatus.UNCHECKED);
        userGroupRegistrarDao.save(userGroupRegistrar);
    }

    @Override
    public List<UserGroupRegistrarDTO> queryAllUnhandledUserGroupRegistrars() {
        List<LessonGroup> lessonGroups = lessonGroupService.queryGroupByCurrentLoginUser();
        return lessonGroups.stream()
                .map(lessonGroup -> queryAllUnhandledUserGroupRegistrars(lessonGroup.getId()))
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(UserGroupRegistrarDTO::getGroupName))
                .collect(Collectors.toList());
    }

    public List<UserGroupRegistrarDTO> queryAllUnhandledUserGroupRegistrars(Long groupId) {

        List<UserGroupRegistrar> userGroupRegistrars = userGroupRegistrarDao.queryByGroupIdAndCheckStatus(groupId, CheckStatus.UNCHECKED.toString());

        return userGroupRegistrars.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<UserGroupRegistrarDTO> queryAllUserGroupRegistrars() {

        List<LessonGroup> lessonGroups = lessonGroupService.queryGroupByCurrentLoginUser();
        return lessonGroups.stream()
                .map(lessonGroup -> queryAllUserGroupRegistrars(lessonGroup.getId()))
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(UserGroupRegistrarDTO::getGroupName))
                .collect(Collectors.toList());
    }



    private UserGroupRegistrarDTO convert(UserGroupRegistrar userGroupRegistrar){

        UserGroupRegistrarDTO userGroupRegistrarDTO = new UserGroupRegistrarDTO();
        userGroupRegistrarDTO.setCheckStatus(userGroupRegistrar.getCheckStatus());
        userGroupRegistrarDTO.setId(userGroupRegistrar.getId());
        userGroupRegistrarDTO.setSubmitTime(userGroupRegistrar.getSubmitTime());
        userGroupRegistrarDTO.setGroupId(userGroupRegistrar.getGroupId());

        LessonGroup lessonGroup = lessonGroupService.findGroupById(userGroupRegistrar.getGroupId());
        userGroupRegistrarDTO.setGroupName(lessonGroup.getName());

        UserDTO userDTO = userService.queryUserById(userGroupRegistrar.getUserId());
        userGroupRegistrarDTO.setUser(userDTO);
        return userGroupRegistrarDTO;

    }

    public List<UserGroupRegistrarDTO> queryAllUserGroupRegistrars(Long groupId) {

        List<UserGroupRegistrar> userGroupRegistrars = userGroupRegistrarDao.queryByGroupId(groupId);
        return userGroupRegistrars.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public boolean check(List<Long> userGroupRegistrarIds, CheckEvent checkEvent) {

        return persistStateMachineHandler.handleEventWithState(
                MessageBuilder.withPayload(checkEvent)
                .setHeader("registrarIds", userGroupRegistrarIds)
                .build(),
                CheckStatus.UNCHECKED
        );
    }

    @Override
    public UserGroupRegistrar getById(Long userGroupRegistrarId) {
        return userGroupRegistrarDao.findOne(userGroupRegistrarId);
    }

    @Override
    public void modifyUserGroupRegistrar(List<UserGroupRegistrar> userGroupRegistrars) {
        this.userGroupRegistrarDao.save(userGroupRegistrars);
    }
}
