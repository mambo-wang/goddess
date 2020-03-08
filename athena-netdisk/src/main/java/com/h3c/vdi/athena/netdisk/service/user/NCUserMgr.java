package com.h3c.vdi.athena.netdisk.service.user;

import com.h3c.vdi.athena.netdisk.model.dto.*;
import com.h3c.vdi.athena.netdisk.model.rest.CreateUserReq;
import com.h3c.vdi.athena.netdisk.model.rest.GroupInfo;
import com.h3c.vdi.athena.netdisk.model.rest.ModifyUserReq;
import com.h3c.vdi.athena.netdisk.model.xml.UserResponseData;
import com.h3c.vdi.athena.netdisk.model.xml.Users;

import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/22
 */
public interface NCUserMgr {

    /**
     * 查询所有云盘用户列表
     * @return 用户名列表
     */
    List<String> queryUsersByGroup(String groupId);

    /**
     * 创建云盘用户
     * @param createUserReq 创建云盘用户
     */
    void createUser(CreateUserReq createUserReq);

    /**
     * 模糊查询用户
     * @return 用户
     */
    List<SearchResultDTO> searchUsersByName(String name);

    /**
     * 启用用户
     * @param userId 用户名
     */
    void enableUser(String userId);

    /**
     * 禁用用户
     * @param userId 用户名
     */
    void disableUser(String userId);

    /**
     * @param userId: userId 用户名
     */
    void deleteUser(String userId);

    /**
     * 批量删除用户
     * @param userIds 用户名
     */
    void deleteUser(String[] userIds);

    /**
     * 修改用户
     * @param userId 用户名
     * @param request 请求参数
     */
    void modifyUser(String userId,ModifyUserReq request);

    /**
     * 添加用户到分组
     * @param userId 用户名
     * @param groupInfo 用户组信息
     */
    void addUserToGroup(String userId, GroupInfo groupInfo);

    /**
     * 批量添加用户到分组
     * @param groupId 分组id
     * @param userIds 用户id
     */
    void addUserToGroup(String groupId, String[] userIds);

    /**
     * 从分组中移除用户
     * @param userId 用户信息
     * @param groupInfo 分组信息
     */
    void removeUserFromGroup(String userId, GroupInfo groupInfo);

    /**
     * 从分组中批量移除用户
     * @param groupId 分组信息
     * @param userIds
     */
    void removeUserFromGroup(String groupId, String[] userIds);

    /**
     * 查询用户
     * @param userId 用户id
     * @return 用户信息
     */
    UserResponseData queryUserByUserId(String userId);

    /**
     * 根据用户名查询用户加入的分组
     * @param userId 用户名
     * @return 分组名称
     */
    String queryGroupsByUserId(String userId);

    /**
     * 查询当前用户网盘用量
     * @return 网盘用量
     */
    QuotaDTO queryQuota();

    /**
     * 修改所有用户的配额
     * @param quota 配额大小，单位:GB
     */
    void modifyQuota(String quota);
}
