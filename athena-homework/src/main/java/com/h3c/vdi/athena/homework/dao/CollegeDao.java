package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *
 * @author z13339
 * @date 2018/3/2
 */
public interface CollegeDao extends JpaRepository<College, Long>, JpaSpecificationExecutor<College> {


    List<College> findByNameAndDeleted(String name, String deleted);

    College findByIdAndDeleted(Long id, String deleted);

    @Query(value = "select * from tbl_college where IS_DELETED = 'n'",nativeQuery = true)
    List<College> findAll();
}
