package com.h3c.vdi.athena.homework.stateMachine.listener;

import com.h3c.vdi.athena.homework.stateMachine.enums.CheckEvent;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

/**
 * Created by w16051 on 2018/4/2.
 * 状态机监听器
 */
public interface PersistStateChangeListener {

    void onPersist(State<CheckStatus,CheckEvent> state, Message<CheckEvent> message,
                   Transition<CheckStatus,CheckEvent> transition, StateMachine<CheckStatus,CheckEvent> stateMachine);
}
