package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.entity.UserGroupRegistrar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/3/8
 */
public interface UserGroupRegistrarDao extends JpaRepository<UserGroupRegistrar, Long>, JpaSpecificationExecutor<UserGroupRegistrar> {

    @Query(value = "select * from tbl_user_group_registrar where GROUP_ID=?1 and STATUS=?2", nativeQuery = true)
    List<UserGroupRegistrar> queryByGroupIdAndCheckStatus(Long groupId, String checkStatus);

    @Query(value = "select * from tbl_user_group_registrar where GROUP_ID=?1", nativeQuery = true)
    List<UserGroupRegistrar> queryByGroupId(Long groupId);

    @Transactional
    @Modifying
    @Query(value = "insert into tbl_user_group_registrar(USER_ID,GROUP_ID,SUBMIT_TIME) values (?1,?2,?3)", nativeQuery = true)
    void addUserGroupRegistrar(Long userId, Long groupId, long l);
}
