package com.h3c.vdi.athena.homework.stateMachine.listener;

import com.h3c.vdi.athena.homework.dao.UserGroupRegistrarDao;
import com.h3c.vdi.athena.homework.entity.UserGroupRegistrar;
import com.h3c.vdi.athena.homework.service.lessongroup.LessonGroupService;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckEvent;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by w14014 on 2018/10/16
 * 处理学生申请加入课程组的业务逻辑
 * @author w14014
 */
@Slf4j
public class CheckUserGroupRegistrarStateChangeListener implements PersistStateChangeListener{

    @Resource
    private LessonGroupService lessonGroupService;

    @Resource
    private UserGroupRegistrarDao userGroupRegistrarDao;

    @Override
    public void onPersist(State<CheckStatus, CheckEvent> state, Message<CheckEvent> message, Transition<CheckStatus, CheckEvent> transition, StateMachine<CheckStatus, CheckEvent> stateMachine) {

        log.info("====================come in====================");
        if(Objects.nonNull(message)&&message.getHeaders().containsKey("registrarIds")){
            ArrayList registrarIds = message.getHeaders().get("registrarIds", ArrayList.class);

            Map<Long, List<Long>> userGroup = new HashMap<>();
            List<UserGroupRegistrar> userGroupRegistrars = new ArrayList<>();
            for(Object registrarId : registrarIds){
                Long id = (Long)registrarId;
                UserGroupRegistrar registrar=userGroupRegistrarDao.findOne(id);
                CheckStatus checkStatus=state.getId();
                if(Objects.equals(checkStatus,CheckStatus.PASSED)){
                    List<Long> users = userGroup.get(registrar.getGroupId());
                    if(CollectionUtils.isEmpty(users)){
                        users = new ArrayList<>();
                    }
                    users.add(registrar.getUserId());
                    userGroup.put(registrar.getGroupId(), users);
                }
                registrar.setCheckStatus(checkStatus);
                userGroupRegistrars.add(registrar);
            }

            log.info("==================registrars:{}", userGroupRegistrars);
            userGroupRegistrarDao.save(userGroupRegistrars);
            log.info("======================usergroups:{}", userGroup);
            userGroup.entrySet()
                    .forEach(longListEntry -> lessonGroupService.addUsersToGroup(longListEntry.getKey(), longListEntry.getValue()));

        }
    }


}
