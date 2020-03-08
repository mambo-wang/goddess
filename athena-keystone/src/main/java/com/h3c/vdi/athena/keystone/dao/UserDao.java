package com.h3c.vdi.athena.keystone.dao;

import com.h3c.vdi.athena.keystone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by JemmyZhang on 2018/2/13
 */
public interface UserDao extends JpaRepository<User,Long>,JpaSpecificationExecutor<User> {

    User findByUsernameAndDeleted(String username, String deleted);

    List<User> findByDeleted(String delete);

    @Query(value = "select a.* from tbl_user as a ,tbl_user_role as b,tbl_role as c\n" +
            "where a.ID=b.USER_ID and b.ROLE_ID=c.ID and c.NAME=?1 and a.IS_DELETED=?2", nativeQuery = true)
    List<User> findByRoleAndDeleted(String name,String delete);

    List<User> findByIdIn(List<Long> ids);

    @Query(value = "select a.* from tbl_user as a, tbl_user_role as b\n" +
            "where a.ID=b.USER_ID and b.ROLE_ID=3 and a.id not in ?1 and a.IS_DELETED='n'", nativeQuery = true)
    List<User> findStudentsNotInIds(List<Long> userIds);
}
