package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.entity.UserClassRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by w16051 on 2018/3/5.
 */
public interface UserClassRelationDao extends JpaRepository<UserClassRelation,Long>,JpaSpecificationExecutor<UserClassRelation> {

    @Query(value = "select USER_ID from tbl_user_class where CLASS_ID=?1 AND IS_DELETED='n'",nativeQuery = true)
    List<BigInteger> findUserIdsByClassId(Long classId);

    @Query(value = "select CLASS_ID from tbl_user_class where USER_ID=?1 AND IS_DELETED='n'",nativeQuery = true)
    Long findClassIdByUserId(Long userId);

    @Query(value = "select * from tbl_user_class where USER_ID=?1 AND IS_DELETED='n'",nativeQuery = true)
    UserClassRelation findByUserId(Long userId);

    @Query(value = "select * from tbl_user_class where CLASS_ID=?1 AND IS_DELETED='n'",nativeQuery = true)
    List<UserClassRelation> findByClassId(Long classId);
}
