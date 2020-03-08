package com.h3c.vdi.athena.homework.service.usergroupregisrar;

import com.h3c.vdi.athena.homework.dto.UserGroupRegistrarDTO;
import com.h3c.vdi.athena.homework.entity.UserGroupRegistrar;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckEvent;

import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/16
 */
public interface UserGroupRegistrarService {

    /**
     * 添加加入课程组申请信息
     * @param userId 用户id
     * @param groupId 课程组id
     */
    void addUserGroupRegistrar(Long userId, Long groupId);

    /**
     * 返回所有当前老师拥有的课程组的注册申请
     * @return 申请
     */
    List<UserGroupRegistrarDTO> queryAllUserGroupRegistrars();

    List<UserGroupRegistrarDTO> queryAllUnhandledUserGroupRegistrars();

    /**
     * 处理注册申请
     * @param userGroupRegistrarIds 资源
     * @param checkEvent 动作
     * @return 结果
     */
    boolean check(List<Long> userGroupRegistrarIds, CheckEvent checkEvent);

    /**
     * 根据ID查找对应的申请
     * @param userGroupRegistrarId 主键id
     * @return 申请
     */
    UserGroupRegistrar getById(Long userGroupRegistrarId);

    /**
     * 修改
     * @param userGroupRegistrars
     */
    void modifyUserGroupRegistrar(List<UserGroupRegistrar> userGroupRegistrars);

}
