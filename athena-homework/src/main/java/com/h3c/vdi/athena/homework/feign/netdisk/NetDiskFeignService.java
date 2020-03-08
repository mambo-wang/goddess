package com.h3c.vdi.athena.homework.feign.netdisk;

import com.h3c.vdi.athena.homework.dto.RoleType;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.entity.LessonGroup;
import com.h3c.vdi.athena.homework.feign.model.GroupInfo;
import com.h3c.vdi.athena.homework.feign.model.ModifyGroupReq;
import com.h3c.vdi.athena.homework.service.lessongroup.LessonGroupService;
import com.h3c.vdi.athena.homework.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author w14014
 * @date 2018/9/27
 */
@Slf4j
@Component
public class NetDiskFeignService {

    @Resource
    private NetDiskFeignClient netDiskFeignClient;

    @Resource
    private LessonGroupService lessonGroupService;

    @Resource
    private UserService userService;

    public void createGroup(String groupId){
        GroupInfo groupInfo = GroupInfo.builder().groupid(groupId).build();
        netDiskFeignClient.createGroup(groupInfo);
    }

    public void removeGroup(String groupId){
        netDiskFeignClient.deleteGroup(groupId);
    }

    public void removeGroup(String[] groupIds){
        netDiskFeignClient.deleteGroup(groupIds);
    }

    public void modifyGroup(String oldGroupId, String newGroupId){
        ModifyGroupReq modifyGroupReq = ModifyGroupReq.builder().oldGroupId(oldGroupId).newGroupId(newGroupId).build();
        netDiskFeignClient.modifyGroup(modifyGroupReq);
    }

    public void addUserToGroup(Long groupId, List<Long> userIds){
        LessonGroup lessonGroup = lessonGroupService.findGroupById(groupId);
        String gid = lessonGroup.getName();
        String[] uIdArray = this.convert(userIds);
        this.netDiskFeignClient.addUsersToGroup(gid, uIdArray);

    }

    public void removeUserFromGroup(Long groupId, List<Long> userIds){
        LessonGroup lessonGroup = lessonGroupService.findGroupById(groupId);
        String gid = lessonGroup.getName();
        String[] uIdArray = this.convert(userIds);
        this.netDiskFeignClient.removeUsersFromGroup(gid, uIdArray);
    }

    private String[] convert(List<Long> userIds){
        List<UserDTO> allUsers = userService.findAllDTOs(RoleType.STUDENT.getValue());

        List<String> uIds =userIds.stream()
                .map(u -> queryUsername(allUsers, u))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        String[] uIdArray = uIds.toArray(new String[uIds.size()]);
        return uIdArray;
    }

    private Optional<String> queryUsername(List<UserDTO> allUsers, Long userId){
        return allUsers.stream()
                .filter(userDTO -> Objects.equals(userDTO.getId(), userId))
                .findFirst()
                .map(UserDTO::getUsername);
    }



}
