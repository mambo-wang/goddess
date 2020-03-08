package com.h3c.vdi.athena.homework.stateMachine.listener;

import com.h3c.vdi.athena.homework.dao.ClassEntityDao;
import com.h3c.vdi.athena.homework.dao.RegistrarDao;
import com.h3c.vdi.athena.homework.dto.HandleUserDTO;
import com.h3c.vdi.athena.homework.entity.Registrar;
import com.h3c.vdi.athena.homework.service.user.UserService;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckEvent;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by w16051 on 2018/4/2.
 * 处理学生注册的业务逻辑，将班长处理结果加工后存储到数据库中
 */
public class CheckRegistrarStateChangeListener implements PersistStateChangeListener{

    @Resource
    UserService userService;
    @Resource
    ClassEntityDao classEntityDao;
    @Resource
    RegistrarDao registrarDao;

    @Override
    public void onPersist(State<CheckStatus, CheckEvent> state, Message<CheckEvent> message, Transition<CheckStatus, CheckEvent> transition, StateMachine<CheckStatus, CheckEvent> stateMachine) {
        if(Objects.nonNull(message)){
            HandleUserDTO handleUserDTO = message.getHeaders().get("handleUserDTO",HandleUserDTO.class);
            Long registrarId=handleUserDTO.getId();
            Registrar registrar=registrarDao.findOne(registrarId);
            CheckStatus checkStatus=state.getId();
            //如果通过则调用将学生和班级关联的方法
            if(Objects.equals(checkStatus,CheckStatus.PASSED)){
                userService.addStudentToClass(registrar);
            }
            registrar.setCheckStatus(checkStatus);
            registrar.setComments(handleUserDTO.getComments());
            registrar.setHandleTime(System.currentTimeMillis());
            registrarDao.save(registrar);
        }
    }


}
