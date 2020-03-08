package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.entity.HomeworkSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by w16051 on 2018/3/26.
 */
public interface HomeworkSubmissionDao extends JpaRepository<HomeworkSubmission,Long>{

    @Query(value = "select * from tbl_homework_submit where HOMEWORK_ID = ?1 and IS_DELETED = 'n'",nativeQuery = true)
    List<HomeworkSubmission> queryHomeworkSubmissionByHomeworkId(Long homeworkId);

    @Query(value = "select * from tbl_homework_submit where HOMEWORK_ID = ?1 and USER_ID = ?2 and IS_DELETED = 'n'",nativeQuery = true)
    HomeworkSubmission queryByHomeworkIdAndUserId(Long homeworkId, Long userId);

    @Query(value = "select * from tbl_homework_submit where USER_ID = ?1 and IS_DELETED = 'n'",nativeQuery = true)
    List<HomeworkSubmission> queryByUserId(Long userId);

    @Override
    @Query(value = "select * from tbl_homework_submit where ID=?1 and IS_DELETED = 'n'",nativeQuery = true)
    HomeworkSubmission getOne(Long id);

    @Query(value = "select * from tbl_homework_submit where HOMEWORK_ID=?1 and STARRED='y' and IS_DELETED='n'",nativeQuery = true)
    List<HomeworkSubmission> queryStarredHomeworkSubmissionByHomeworkId(Long homeworkId);

    /***********************************************数据统计使用*******************************************************/
    @Query(value = "select s.*\n" +
            "from tbl_homework_submit as s inner join tbl_homework as h \n" +
            "on s.HOMEWORK_ID = h.ID\n" +
            "and s.USER_ID=?4 \n" +
            "and h.GROUP_ID=?1\n" +
            "and s.SCORE is not null\n" +
            "and h.IS_DELETED <> 'y'\n" +
            "and s.IS_DELETED <> 'y'\n" +
            "and h.CREATE_TIME between ?2 and ?3", nativeQuery = true)
    List<HomeworkSubmission> queryByGroupIdAndStuIdAndTime(Long lessonGroupId, Long startDate, Long endDate, Long useId);

    @Query(value = "select s.*\n" +
            "from tbl_homework_submit as s inner join tbl_homework as h \n" +
            "on s.HOMEWORK_ID = h.ID\n" +
            "and s.USER_ID=?3 \n" +
            "and s.SCORE is not null\n" +
            "and s.IS_DELETED <> 'y'\n" +
            "and h.IS_DELETED <> 'y'\n" +
            "and h.GROUP_ID in (select g.ID from tbl_lesson_group g,tbl_user_group ug where g.ID=ug.GROUP_ID and ug.USER_ID=?3 and ug.IS_DELETED <> 'y')\n" +
            "and h.CREATE_TIME between ?1 and ?2", nativeQuery = true)
    List<HomeworkSubmission> queryByStuIdAndTime(Long startDate, Long endDate, Long useId);

    @Query(value = "select s.*\n" +
            "from tbl_homework_submit as s inner join tbl_homework as h \n" +
            "on s.HOMEWORK_ID = h.ID\n" +
            "and h.ID=?4\n" +
            "and h.GROUP_ID=?1\n" +
            "and s.SCORE is not null\n" +
            "and h.IS_DELETED <> 'y'\n" +
            "and s.IS_DELETED <> 'y'\n" +
            "and h.CREATE_TIME between ?2 and ?3", nativeQuery = true)
    List<HomeworkSubmission> queryByGroupIdAndHomeworkIdAndTime(Long lessonGroupId, Long startDate, Long endDate, Long homeworkId);

    @Query(value = "select s.*\n" +
            "from tbl_homework_submit as s inner join tbl_homework as h \n" +
            "on s.HOMEWORK_ID = h.ID\n" +
            "and h.GROUP_ID=?1\n" +
            "and s.SCORE is not null\n" +
            "and h.IS_DELETED <> 'y'\n" +
            "and s.IS_DELETED <> 'y'\n" +
            "and h.CREATE_TIME between ?2 and ?3", nativeQuery = true)
    List<HomeworkSubmission> queryByGroupIdAndTime(Long lessonGroupId, Long startDate, Long endDate);

    @Query(value = "select s.*\n" +
            "from tbl_homework_submit as s inner join tbl_homework as h \n" +
            "on s.HOMEWORK_ID = h.ID\n" +
            "and h.GROUP_ID=?1\n" +
            "and h.IS_DELETED <> 'y'\n" +
            "and s.IS_DELETED <> 'y'\n" +
            "and h.CREATE_TIME between ?2 and ?3", nativeQuery = true)
    List<HomeworkSubmission> queryAllByGroupIdAndTime(Long lessonGroupId, Long startDate, Long endDate);
    @Query(value = "select * from tbl_homework_submit where USER_ID IN ?1 and IS_DELETED = 'n'",nativeQuery = true)
    List<HomeworkSubmission> queryByUserIds(List<Long> userIds);
}
