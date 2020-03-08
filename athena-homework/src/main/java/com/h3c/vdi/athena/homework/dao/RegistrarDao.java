package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.entity.Registrar;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by w16051 on 2018/3/8.
 */
public interface RegistrarDao extends JpaRepository<Registrar,Long>{

    Registrar findByUsername(String username);

    @Query(value = "select * from tbl_registrar where CLASS_ID=?1",nativeQuery = true)
    List<Registrar> queryByClassId(Long classId);

    @Query(value = "select * from tbl_registrar where CLASS_ID=?1 and STATUS=?2",nativeQuery = true)
    List<Registrar> queryByClassIdAndCheckStatus(Long classId, String checkStatus);

    @Query(value = "select * from tbl_registrar where ID=?1 and STATUS=?2",nativeQuery = true)
    Registrar queryByIdAndCheckStatus(Long Id, String checkStatus);

    @Query(value = "select * from tbl_registrar where USER_ID=?1",nativeQuery = true)
    Registrar queryByUserId(Long userId);
}
