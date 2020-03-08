package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.entity.UserGroupRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

/**
 *
 * @author w14014
 * @date 2018/3/8
 */
public interface UserGroupRelationDao extends JpaRepository<UserGroupRelation, Long>, JpaSpecificationExecutor<UserGroupRelation> {

    @Query(value = "select r.userId from com.h3c.vdi.athena.homework.entity.UserGroupRelation r where r.groupId=?1 and r.deleted='n'")
    Set<Long> findUserIdsByGroupId(Long groupId);

    @Query(value = "select r.userId from com.h3c.vdi.athena.homework.entity.UserGroupRelation r where r.groupId=?1 and r.deleted<>'y'")
    List<Long> findUserIdsNotDeletedByGroupId(Long groupId);

    @Transactional
    @Modifying
    @Query(value = "update tbl_user_group set IS_DELETED = 'y' where GROUP_ID in ?1", nativeQuery = true)
    void deleteRelationByGroupIds(List<Long> ids);

    @Transactional
    @Modifying
    @Query(value = "insert into tbl_user_group(GROUP_ID,USER_ID) values (?1,?2)", nativeQuery = true)
    void insertRelation(Long groupId, Long userId);

    @Transactional
    @Modifying
    @Query(value = "update tbl_user_group set IS_DELETED = 'y' where USER_ID=?2 and GROUP_ID=?1", nativeQuery = true)
    void deleteRelation(Long groupId, Long userId);

    @Transactional
    @Modifying
    @Query(value = "update tbl_user_group set IS_DELETED = 'y' where USER_ID=?1", nativeQuery = true)
    void deleteRelationByUserId(Long userId);

    @Query(value = "select USER_ID from tbl_user_group where IS_DELETED = 'n'", nativeQuery = true)
    List<BigInteger> findUserIdsNotDeleted();

    @Query(value = "select ID from tbl_user_group where USER_ID=?1 and IS_DELETED = 'n'", nativeQuery = true)
    List<BigInteger> findGroupIdsByUserIdsNotDeleted(Long userId);
}
