package com.h3c.vdi.athena.netdisk.service.group;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.netdisk.config.rest.RestConnection;
import com.h3c.vdi.athena.netdisk.exception.ErrorCodes;
import com.h3c.vdi.athena.netdisk.model.rest.GroupInfo;
import com.h3c.vdi.athena.netdisk.model.rest.ModifyGroupReq;
import com.h3c.vdi.athena.netdisk.model.xml.Groups;
import com.h3c.vdi.athena.netdisk.model.dto.SearchResultDTO;
import com.h3c.vdi.athena.netdisk.service.user.NCUserMgr;
import com.h3c.vdi.athena.netdisk.utils.NextCloudUrls;
import com.h3c.vdi.athena.netdisk.model.xml.OcsBaseData;
import com.h3c.vdi.athena.netdisk.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author w14014
 * @date 2018/9/25
 */
@Slf4j
@Service("ncGroupMgr")
public class NCGroupMgrImpl implements NCGroupMgr {

    @Resource
    private RestConnection restConnection;

    @Resource
    private NCUserMgr ncUserMgr;

    @Override
    public Groups queryGroups() {
        Groups groups = restConnection.getWithDefault(NextCloudUrls.Group.GROUP, new ParameterizedTypeReference<Groups>() {}).getBody();
        log.info("query groups successful");
        return groups;
    }

    @Override
    public void createGroup(GroupInfo groupInfo) {
        try {
            HttpEntity<GroupInfo> httpEntity = new HttpEntity<>(groupInfo);
            OcsBaseData ocsBaseData = restConnection.postWithDefault(NextCloudUrls.Group.GROUP, httpEntity, OcsBaseData.class).getBody();
            ocsBaseData.checkResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCodes.USER_GROUP_CREATE_FAIL);
        }
    }

    @Override
    public void deleteGroup(String groupid) {
        String url = String.format(NextCloudUrls.Group.GROUP_DELETE, groupid);
        OcsBaseData ocsBaseData = restConnection.deleteWithDefault(url, new ParameterizedTypeReference<OcsBaseData>() {}).getBody();
        ocsBaseData.checkResult();
    }

    @Override
    public void deleteGroup(String[] groupIds) {
        Stream.of(groupIds).forEach(this::deleteGroup);
    }

    @Override
    public List<SearchResultDTO> searchGroups(String name) {
        String url = String.format(NextCloudUrls.Group.GROUP_SEARCH, name);
        Groups groups = restConnection.getWithDefault(url, new ParameterizedTypeReference<Groups>() {}).getBody();
        if(Objects.isNull(groups.getData().getGroups().getElement())){
            return Collections.emptyList();
        }
        List<SearchResultDTO> result = groups.getData().getGroups().getElement().stream()
                .map(e -> new SearchResultDTO(e, Constant.SEARCH_TYPE_GROUP))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * @param modifyGroupReq
     */
    @Override
    public void modifyGroup(ModifyGroupReq modifyGroupReq){

        //查询组内用户
        List<String> users = this.ncUserMgr.queryUsersByGroup(modifyGroupReq.getOldGroupId());
        //删除分组
        this.deleteGroup(modifyGroupReq.getOldGroupId());
        //创建新分组
        GroupInfo groupInfo = new GroupInfo(modifyGroupReq.getNewGroupId());
        this.createGroup(groupInfo);
        //添加用户到新分组
        users.forEach(s -> this.ncUserMgr.addUserToGroup(s, groupInfo));
    }
}
