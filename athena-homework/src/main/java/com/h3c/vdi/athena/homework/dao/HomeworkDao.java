package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by w16051 on 2018/3/21.
 */
public interface HomeworkDao extends JpaRepository<Homework,Long> {

    @Query(value = "select * from tbl_homework where USER_ID=?1 AND IS_DELETED='n'", nativeQuery = true)
    List<Homework> queryHomeworkByUserId(Long userID);

    @Query(value = "select * from tbl_homework where GROUP_ID=?1 AND IS_DELETED='n'", nativeQuery = true)
    List<Homework> queryHomeworkByGroupId(Long groupID);

    @Override
    @Query(value = "select * from tbl_homework where ID=?1 AND IS_DELETED='n'", nativeQuery = true)
    Homework findOne(Long id);

    @Query(value = "select * from tbl_homework where GROUP_ID=?1 AND IS_DELETED='n' AND CREATE_TIME > ?2 AND CREATE_TIME<?3", nativeQuery = true)
    List<Homework> queryHomework(Long groupId, Long startDate, Long endDate);

    Homework queryByNameAndDeleted(String name, String isDeleted);

    @Query(value = "select * from tbl_homework where NAME=?1 AND GROUP_ID=?2 AND USER_ID=?3 and IS_DELETED='n'",nativeQuery = true)
    Homework queryByNameAndGroupIdAndUserId(String name,Long groupId,Long userId);

    @Query(value = "select * from tbl_homework where GROUP_ID=?1 AND IS_DELETED='n'",nativeQuery = true)
    List<Homework> queryByGroupId(Long groupId);

    /***************************************************数据统计使用***************************************************/
    @Query(value = "select * from tbl_homework where GROUP_ID=?1 AND IS_DELETED <> 'y'",nativeQuery = true)
    List<Homework> queryByGroupIdAndDeleted(Long groupId);
}
