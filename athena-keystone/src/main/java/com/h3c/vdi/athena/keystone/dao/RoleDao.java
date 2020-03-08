package com.h3c.vdi.athena.keystone.dao;

import com.h3c.vdi.athena.keystone.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by w16051 on 2018/10/31.
 */
public interface RoleDao extends JpaRepository<Role,Long>,JpaSpecificationExecutor<Role> {

}
