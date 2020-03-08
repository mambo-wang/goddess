package com.h3c.vdi.athena.homework.service.registrar;

import com.h3c.vdi.athena.homework.dto.HandleUserDTO;
import com.h3c.vdi.athena.homework.dto.RegistrarDTO;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.entity.Registrar;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckEvent;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by w16051 on 2018/3/8.
 */
public interface RegistrarService {

    /**
     * 添加注册用户信息，返回注册者保存后的ID
     * @param registrarDTO
     * @return
     */
    Registrar addRegistrar(RegistrarDTO registrarDTO);

    /**
     * 返回所有当前用户需要处理的注册申请
     * @return
     */
    List<RegistrarDTO> queryAllUnhandledRegistrars();

    /**
     * 返回所有当前用户为班长的注册申请
     * @return
     */
    List<RegistrarDTO> queryAllRegistrars();

    /**
     * 处理注册申请
     * @param handleUserDTO
     * @return
     */
    boolean check(HandleUserDTO handleUserDTO);
}
