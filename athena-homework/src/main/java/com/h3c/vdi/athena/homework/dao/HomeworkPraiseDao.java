package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.entity.HomeworkPraise;
import com.h3c.vdi.athena.homework.entity.HomeworkSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by w16051 on 2018/3/26.
 */
public interface HomeworkPraiseDao extends JpaRepository<HomeworkPraise,Long>{
    HomeworkPraise findByHomeworkSubIdAndUserId(Long homeworkSubmitId,Long userId,String deleted);

    List<HomeworkPraise> findByHomeworkSubId(Long homeworkSubmitId,String deleted);

    @Query(value = "select * from tbl_homework_praise where USER_ID IN ?1 and IS_DELETED = 'n'",nativeQuery = true)
    List<HomeworkPraise> findByUserIds(List<Long> userIds);
}
