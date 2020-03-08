package com.h3c.vdi.athena.netdisk.controller;

import com.h3c.vdi.athena.netdisk.model.dto.QuotaDTO;
import com.h3c.vdi.athena.netdisk.model.dto.SearchResultDTO;
import com.h3c.vdi.athena.netdisk.model.rest.CreateUserReq;
import com.h3c.vdi.athena.netdisk.model.rest.GroupInfo;
import com.h3c.vdi.athena.netdisk.model.rest.ModifyGroupReq;
import com.h3c.vdi.athena.netdisk.model.rest.ModifyUserReq;
import com.h3c.vdi.athena.netdisk.service.group.NCGroupMgr;
import com.h3c.vdi.athena.netdisk.service.user.NCUserMgr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/22
 *
 * 用户和用户分组相关接口，供keystone调用
 */
@Slf4j
@RestController
@RequestMapping(value = "/ncuser")
public class NCUserController {

    @Resource
    private NCUserMgr ncUserMgr;

    @Resource
    private NCGroupMgr ncGroupMgr;

    /**
     * 分享文件时查询要分享给谁
     * @param search
     * @return
     */
    @GetMapping
    public List<SearchResultDTO> queryUsersOrGroups(@RequestParam(name = "search")String search){
        List<SearchResultDTO> users = ncUserMgr.searchUsersByName(search);
        List<SearchResultDTO> groups = ncGroupMgr.searchGroups(search);
        users.addAll(groups);
        return users;
    }

    @PostMapping(value = "/users")
    public void createUser(@RequestBody CreateUserReq createUserReq){

        this.ncUserMgr.createUser(createUserReq);
    }

    @PutMapping("/users/{userId}")
    public void modifyUser(@PathVariable(value = "userId")String userId, @RequestBody ModifyUserReq modifyUserReq){
        this.ncUserMgr.modifyUser(userId, modifyUserReq);
    }

    @PutMapping("/users/quota/{quota:.+}")
    public void modifyQuota(@PathVariable(value = "quota")String quota){
        this.ncUserMgr.modifyQuota(quota);
    }

    @DeleteMapping("/users/{userId}")
    public void removeUser(@PathVariable(value = "userId")String userId){
        this.ncUserMgr.deleteUser(userId);
    }

    /**
     * 批量删除用户
     * @param userIds
     */
    @DeleteMapping(value = "/users/batch/{userIds}")
    public void removeUser(@PathVariable(name = "userIds") String[] userIds){
        this.ncUserMgr.deleteUser(userIds);
    }

    @PostMapping("/groups")
    public void createGroup(@RequestBody GroupInfo groupInfo){
        this.ncGroupMgr.createGroup(groupInfo);
    }

    @PostMapping("/groups/{groupId}")
    public void deleteGroup(@PathVariable(value = "groupId")String groupId){
        this.ncGroupMgr.deleteGroup(groupId);
    }

    @DeleteMapping(value = "/groups/batch/{groupIds}")
    public void deleteGroup(@PathVariable(value = "groupIds")String[] groupIds){
        this.ncGroupMgr.deleteGroup(groupIds);
    }

    @PutMapping("/groups")
    public void modifyGroup(@RequestBody ModifyGroupReq modifyGroupReq){

        this.ncGroupMgr.modifyGroup(modifyGroupReq);
    }

    @GetMapping("/quota")
    public QuotaDTO queryQuota(){
        return ncUserMgr.queryQuota();
    }

    @PostMapping("/groups/{groupId}/users/{userIds}")
    public void addUsersToGroup(@PathVariable(value = "groupId")String groupId, @PathVariable(value = "userIds")String[] userIds){
        this.ncUserMgr.addUserToGroup(groupId, userIds);
    }

    @DeleteMapping("/groups/{groupId}/users/{userIds}")
    public void removeUsersFromGroup(@PathVariable(value = "groupId")String groupId, @PathVariable(value = "userIds")String[] userIds){
        this.ncUserMgr.removeUserFromGroup(groupId, userIds);
    }

}
