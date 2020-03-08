package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * Created by z13339 on 2018/3/2.
 */
public interface ClassEntityDao extends JpaRepository<ClassEntity, Long>, JpaSpecificationExecutor<ClassEntity> {

    List<ClassEntity> findByNameAndDeleted(String name, String deleted);

    List<ClassEntity> findByCodeAndDeleted(String code, String deleted);

    List<ClassEntity> findByCollegeIdAndDeleted(Long id, String deleted);

    ClassEntity findByIdAndDeleted(Long id, String deleted);

    @Query(value = "select ID from tbl_class where COLLEGE_ID=?1 AND IS_DELETED='n'",nativeQuery = true)
    List<Long> findClassIdsByCollegeId(Long collegeId);

    @Query(value = "select ID from tbl_class where MONITOR_ID=?1 AND IS_DELETED='n'",nativeQuery = true)
    Long queryIdByMonitorId(Long monitorId);

    @Query(value = "select * from tbl_class where MONITOR_ID=?1 AND IS_DELETED='n'",nativeQuery = true)
    ClassEntity queryByMonitorId(Long monitorId);

    @Query(value = "select * from tbl_class where CODE=?1 AND IS_DELETED='n'",nativeQuery = true)
    ClassEntity queryByCode(String code);

    @Query(value = "select * from tbl_class where IS_DELETED='n'",nativeQuery = true)
    List<ClassEntity> findAll();

    @Query(value = "select INVITE_CODE from tbl_class where IS_DELETED='n' AND INVITE_CODE <> 'NULL'",nativeQuery = true)
    List<Integer> queryInviteCodes();

    @Query(value = "select * from tbl_class where CREATE_TIME >= ?1 and CREATE_TIME <= ?2 and IS_DELETED = 'n'",nativeQuery = true)
    List<ClassEntity> findClassesByStartTimeAndEndTime(String startTime,String endTime);
}
