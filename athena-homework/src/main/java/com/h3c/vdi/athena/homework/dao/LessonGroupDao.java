package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.dto.LessonGroupDTO;
import com.h3c.vdi.athena.homework.entity.ClassEntity;
import com.h3c.vdi.athena.homework.entity.LessonGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

/**
 * Created by w14014 on 2018/3/8.
 * 课程组数据层
 */
public interface LessonGroupDao extends JpaRepository<LessonGroup, Long>, JpaSpecificationExecutor<ClassEntity> {


    List<LessonGroup> findByUserIdAndDeleted(Long userId, String deleted);

    LessonGroup findByIdAndDeleted(Long groupId, String deleted);

    List<LessonGroup> findByNameAndDeleted(String name, String deleted);

    List<LessonGroup> findByIdNotAndNameAndDeleted(Long groupId, String name, String deleted);

    @Query(value = "select ID from tbl_lesson_group where USER_ID = ?1", nativeQuery = true)
    List<Long> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = "update tbl_lesson_group set IS_DELETED = 'y' where ID in ?1", nativeQuery = true)
    void deleteGroupByIds(List<Long> groupIds);

    List<LessonGroup> findByDeleted(String isDeletedN);

    @Query(value = "select g.* \n" +
            "from tbl_lesson_group g,tbl_user_group ug\n" +
            "where g.ID=ug.GROUP_ID and ug.USER_ID=?1 and ug.IS_DELETED=?2\n", nativeQuery = true)
    List<LessonGroup> findByStudentId(Long stuId, String deleted);

    @Query(value = "select * \n" +
            "from tbl_lesson_group\n" +
            "where IS_DELETED=?2 and ID not in (\n" +
            "select GROUP_ID \n" +
            "from tbl_user_group\n" +
            "where USER_ID=?1\n" +
            "and IS_DELETED=?2\n" +
            ")", nativeQuery = true)
    List<LessonGroup> findNotInGroupsByUserId(Long id, String isDeletedN);

}
