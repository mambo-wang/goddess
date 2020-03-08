package com.h3c.vdi.athena.netdisk.service.user;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.DefaultUserDetails;
import com.h3c.vdi.athena.common.utils.Utils;
import com.h3c.vdi.athena.netdisk.config.rest.RestConnection;
import com.h3c.vdi.athena.netdisk.exception.ErrorCodes;
import com.h3c.vdi.athena.netdisk.mappers.GroupUserMapper;
import com.h3c.vdi.athena.netdisk.model.dto.*;
import com.h3c.vdi.athena.netdisk.model.rest.CreateUserReq;
import com.h3c.vdi.athena.netdisk.model.rest.GroupInfo;
import com.h3c.vdi.athena.netdisk.model.rest.ModifyUserReq;
import com.h3c.vdi.athena.netdisk.model.xml.Quota;
import com.h3c.vdi.athena.netdisk.model.xml.UserResponseData;
import com.h3c.vdi.athena.netdisk.model.xml.Users;
import com.h3c.vdi.athena.netdisk.service.appconfig.AppConfigMgr;
import com.h3c.vdi.athena.netdisk.service.disk.NetDiskMgr;
import com.h3c.vdi.athena.netdisk.utils.ModelConverter;
import com.h3c.vdi.athena.netdisk.utils.NextCloudUrls;
import com.h3c.vdi.athena.netdisk.model.xml.OcsBaseData;
import com.h3c.vdi.athena.netdisk.utils.Constant;
import com.h3c.vdi.athena.netdisk.utils.EditUserKey;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
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
 * @date 2018/9/22
 */
@Slf4j
@Service("ncUserMgr")
public class NCUserMgrImpl implements NCUserMgr{


    @Autowired
    private RestConnection restConnection;

    @Resource
    private GroupUserMapper groupUserMapper;

    @Override
    public List<String> queryUsersByGroup(String groupId) {
        return groupUserMapper.getUIdsByGId(groupId);
    }

    private List<String> queryUsers() {
        Users users = restConnection.getWithDefault(NextCloudUrls.User.USER, new ParameterizedTypeReference<Users>() {}).getBody();
        return users.getData().getUsers().getElement();
    }

    @Override
    public void createUser(CreateUserReq createUserReq) {
        try {
            HttpEntity<CreateUserReq> httpEntity = new HttpEntity<>(createUserReq);
            OcsBaseData ocsBase = restConnection.postWithDefault(NextCloudUrls.User.USER, httpEntity, OcsBaseData.class).getBody();
            ocsBase.checkResult();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("user create fail");
            throw new AppException(e.getMessage());
        }
    }

    @Override
    public List<SearchResultDTO> searchUsersByName(String name) {
        String url = String.format(NextCloudUrls.User.USER_SEARCH, name);
        Users users = restConnection.getWithDefault(url, new ParameterizedTypeReference<Users>() {}).getBody();
        if(Objects.isNull(users.getData().getUsers().getElement())){
            return Collections.emptyList();
        }
        List<SearchResultDTO> result = users.getData().getUsers().getElement().stream()
                .map(e -> new SearchResultDTO(e, Constant.SEARCH_TYPE_USER))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public void enableUser(String userId) {
        String url = String.format(NextCloudUrls.User.USER_ENABLE, userId);
        OcsBaseData ocsBase = restConnection.putWithDefault(url, null, new ParameterizedTypeReference<OcsBaseData>() {}).getBody();
        ocsBase.checkResult();
    }

    @Override
    public void disableUser(String userId) {
        String url = String.format(NextCloudUrls.User.USER_DISABLE, userId);
        OcsBaseData ocsBase = restConnection.putWithDefault(url, null, new ParameterizedTypeReference<OcsBaseData>() {}).getBody();
        ocsBase.checkResult();
    }

    @Override
    public void deleteUser(String userId) {
        String url = String.format(NextCloudUrls.User.USER_DELETE, userId);
        OcsBaseData ocsBase = restConnection.deleteWithDefault(url, new ParameterizedTypeReference<OcsBaseData>() {}).getBody();
        ocsBase.checkResult();
    }

    @Override
    public void deleteUser(String[] userIds) {
        Stream.of(userIds).forEach(this::deleteUser);
    }

    @Override
    public void modifyUser(String userId,ModifyUserReq modifyUserReq) {

        if(StringUtils.isNotBlank(userId)){
            // quota不为空，修改云盘用户容量
            if (StringUtils.isNotBlank(modifyUserReq.getQuota())) {
                this.modifyUser(userId, EditUserKey.quota.getKey() , modifyUserReq.getQuota());
            }
            // displayName不为空，修改用户显示名称
            if (StringUtils.isNotBlank(modifyUserReq.getDisplayname())) {
                this.modifyUser(userId, EditUserKey.displayname.getKey(), modifyUserReq.getDisplayname());
            }

            // password不为空，修改用户密码
            if (StringUtils.isNotBlank(modifyUserReq.getPassword())) {
                this.modifyUser(userId, EditUserKey.password.getKey() , modifyUserReq.getPassword());
            }
            // email，邮箱
            if (StringUtils.isNotBlank(modifyUserReq.getEmail())) {
                this.modifyUser(userId, EditUserKey.email.getKey(), modifyUserReq.getEmail());
            }
        }
    }

    private void modifyUser(String userId, String key, String value) {
        String url = String.format(NextCloudUrls.User.USER_MODIFY, userId, key, value);
        OcsBaseData ocsBase = restConnection.putWithDefault(url, null, new ParameterizedTypeReference<OcsBaseData>() {}).getBody();
        ocsBase.checkResult();
    }

    @Override
    public void addUserToGroup(String userId, GroupInfo groupInfo) {
        String url = String.format(NextCloudUrls.User.USER_GROUP, userId);
        HttpEntity<GroupInfo> httpEntity = new HttpEntity<>(groupInfo);
        OcsBaseData ocsBaseData = restConnection.postWithDefault(url, httpEntity, OcsBaseData.class).getBody();
        ocsBaseData.checkResult();
    }

    @Override
    public void addUserToGroup(String groupId, String[] userIds) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupid(groupId);

        Stream.of(userIds).forEach(u -> addUserToGroup(u, groupInfo));
    }

    @Override
    public void removeUserFromGroup(String userId, GroupInfo groupInfo) {
        String url = String.format(NextCloudUrls.User.USER_GROUP, userId);
        HttpEntity<GroupInfo> httpEntity = new HttpEntity<>(groupInfo);
        OcsBaseData ocsBaseData = restConnection.deleteWithDefault(url, httpEntity, new ParameterizedTypeReference<OcsBaseData>() {}).getBody();
        ocsBaseData.checkResult();
    }

    @Override
    public void removeUserFromGroup(String groupId, String[] userIds) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupid(groupId);
        Stream.of(userIds).forEach(u -> removeUserFromGroup(u, groupInfo));
    }

    @Override
    public UserResponseData queryUserByUserId(String userId) {
        String url = String.format(NextCloudUrls.User.QUERY_USER_BY_ID, userId);
        UserResponseData userResponseData = restConnection.getWithDefault(url, new ParameterizedTypeReference<UserResponseData>() {}).getBody();
        return userResponseData;
    }

    @Override
    public String queryGroupsByUserId(String userId) {
        UserResponseData user = queryUserByUserId(userId);
        List<String> groups = user.getData().getGroups().getElement();
        String groupInfo = groups.stream().collect(Collectors.joining(","));
        log.info("{}'s group info is {}", userId, groupInfo);
        return groupInfo;
    }

    /**
     * 保留两位小数
     * @return
     */
    @Override
    public QuotaDTO queryQuota() {
        DefaultUserDetails userDetails = Utils.getLoginUser();
        UserResponseData data = queryUserByUserId(userDetails.getUsername());
        Quota quota = data.getData().getQuota();

        var free = ModelConverter.formatContentLength(quota.getFree());
        var used = ModelConverter.formatContentLength(quota.getUsed());
        var total = ModelConverter.formatContentLength(quota.getTotal());
        var relative = quota.getRelative() + "%";

        log.info("free {}, used{}, total{}, relative{}", free, used, total);
        return QuotaDTO.builder().free(free).used(used).total(total).relative(relative).build();
    }

    @Override
    public void modifyQuota(String quota) {

        List<String> users = this.queryUsers();
        users.forEach(userId -> {
            try {
                this.modifyUser(userId, EditUserKey.quota.getKey() , quota + " GB");
            }catch (Exception e){
                log.error("modify user error,userid:{}, error:{}", userId, e);
                throw new AppException(ErrorCodes.USER_MODIFY_QUOTA_FAIL);
            }
        });

    }

}
