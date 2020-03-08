package com.h3c.vdi.athena.homework.service.registrar;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.homework.constant.CommonConst;
import com.h3c.vdi.athena.homework.dao.ClassEntityDao;
import com.h3c.vdi.athena.homework.dao.RegistrarDao;
import com.h3c.vdi.athena.homework.dto.HandleUserDTO;
import com.h3c.vdi.athena.homework.dto.RegistrarDTO;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.entity.Registrar;
import com.h3c.vdi.athena.homework.exception.ErrorCodes;
import com.h3c.vdi.athena.homework.service.user.UserService;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckEvent;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import com.h3c.vdi.athena.homework.stateMachine.handler.PersistStateMachineHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by w16051 on 2018/3/8.
 */
@Service
public class RegistrarServiceImpl implements RegistrarService{

    @Resource
    RegistrarDao registrarDao;

    @Resource
    UserService userService;

    @Resource
    ClassEntityDao classEntityDao;

    @Resource
    PersistStateMachineHandler persistStateMachineHandler;

    @Override
    public Registrar addRegistrar(RegistrarDTO registrarDTO)
    {
        Registrar registrar=new Registrar();
        registrar.setUserId(registrarDTO.getUserId());
        registrar.setName(registrarDTO.getName());
        registrar.setPassword(registrarDTO.getPassword());
        registrar.setUsername(registrarDTO.getUsername());
        registrar.setEmailAddress(registrarDTO.getEmailAddress());
        registrar.setClassId(registrarDTO.getClassId());
        registrar.setSubmitTime(System.currentTimeMillis());
        registrar.setCheckStatus(CheckStatus.UNCHECKED);
        Registrar registrarSaved=registrarDao.save(registrar);
        return registrarSaved;
    }

    @Override
    public List<RegistrarDTO> queryAllUnhandledRegistrars(){
        Long currentUserId=userService.currentLoginUser().getId();
        Long classId=classEntityDao.queryIdByMonitorId(currentUserId);
        if(Objects.isNull(classId)){
            return new ArrayList<>();
        }else{
            List<Registrar> registrars=registrarDao.queryByClassIdAndCheckStatus(classId,CheckStatus.UNCHECKED.toString());
            if(CollectionUtils.isEmpty(registrars))
                return new ArrayList<>();
            else
                return registrars.stream().map(this::convertRegistrarToRegistrarDTO).collect(Collectors.toList());
        }
    }

    private RegistrarDTO convertRegistrarToRegistrarDTO(Registrar registrar){
        RegistrarDTO registrarDTO=new RegistrarDTO();
        BeanUtils.copyProperties(registrar,registrarDTO);
        return registrarDTO;
    }

    @Override
    public List<RegistrarDTO> queryAllRegistrars(){
        Long currentUserId=userService.currentLoginUser().getId();
        Long classId=classEntityDao.queryIdByMonitorId(currentUserId);
        if(Objects.isNull(classId)){
            return new ArrayList<>();
        }else{
            List<Registrar> registrars=registrarDao.queryByClassId(classId);
            if(CollectionUtils.isEmpty(registrars))
                return new ArrayList<>();
            else
                return registrars.stream().map(this::convertRegistrarToRegistrarDTO).collect(Collectors.toList());
        }
    }

    @Override
    public boolean check(HandleUserDTO handleUserDTO){
        Long currentUserId=userService.currentLoginUser().getId();
        //判断该申请是否已经被处理，如果已经被处理则抛出异常提示
        Registrar registrar=registrarDao.queryByIdAndCheckStatus(handleUserDTO.getId(),CheckStatus.UNCHECKED.toString());
        if (Objects.isNull(registrar))
            throw new AppException(ErrorCodes.REGISTRAR_NOT_FOUND);
        Long monitorId=classEntityDao.findByIdAndDeleted(registrar.getClassId(), CommonConst.NOT_DELETED).getMonitorId();
        if(Objects.isNull(monitorId)||!Objects.equals(monitorId,currentUserId))
            throw new AppException(ErrorCodes.REGISTRAR_NOT_PERMITTED);
        return persistStateMachineHandler
                .handleEventWithState(MessageBuilder.withPayload(CheckEvent.valueOf(handleUserDTO.getEvent()))
                        .setHeader("handleUserDTO",handleUserDTO).build(),registrar.getCheckStatus());
    }
}
