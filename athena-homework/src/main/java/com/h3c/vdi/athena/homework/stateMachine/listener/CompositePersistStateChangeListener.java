package com.h3c.vdi.athena.homework.stateMachine.listener;

import com.h3c.vdi.athena.homework.stateMachine.enums.CheckEvent;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.AbstractCompositeListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.Iterator;

/**
 * Created by w16051 on 2018/4/2.
 * 聚合操作，将所有加入的监听器聚合起来
 * 该类和业务解耦
 */
public class CompositePersistStateChangeListener extends AbstractCompositeListener<PersistStateChangeListener> implements PersistStateChangeListener{

    @Override
    public void onPersist(State<CheckStatus, CheckEvent> state, Message<CheckEvent> message, Transition<CheckStatus, CheckEvent> transition, StateMachine<CheckStatus, CheckEvent> stateMachine) {
        //3
        Iterator<PersistStateChangeListener> iterator=getListeners().reverse();
        //遍历所有加入的监听器，依次执行监听动作
        for (;iterator.hasNext();){
            PersistStateChangeListener listener = iterator.next();
            listener.onPersist(state,message,transition,stateMachine);
        }
    }
}
