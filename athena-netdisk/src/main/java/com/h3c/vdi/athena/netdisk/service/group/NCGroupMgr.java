package com.h3c.vdi.athena.netdisk.service.group;

import com.h3c.vdi.athena.netdisk.model.rest.GroupInfo;
import com.h3c.vdi.athena.netdisk.model.rest.ModifyGroupReq;
import com.h3c.vdi.athena.netdisk.model.xml.Groups;
import com.h3c.vdi.athena.netdisk.model.dto.SearchResultDTO;

import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/25
 */
public interface NCGroupMgr {

    /**
     * 查询分组
     * @return 分组
     */
    Groups queryGroups();

    /**
     * 创建分组
     * @param groupInfo 分组信息
     */
    void createGroup(GroupInfo groupInfo);

    /**
     * 删除分组
     * @param groupId 分组id
     */
    void deleteGroup(String groupId);

    /**
     * 批量删除分组
     * @param groupIds 分组id
     */
    void deleteGroup(String[] groupIds);

    /**
     * 查询分组
     * @param name 分组id
     * @return 分组
     */
    List<SearchResultDTO> searchGroups(String name);

    /**
     * 修改分组
     * @param modifyGroupReq 修改分组
     */
    void modifyGroup(ModifyGroupReq modifyGroupReq);



}
