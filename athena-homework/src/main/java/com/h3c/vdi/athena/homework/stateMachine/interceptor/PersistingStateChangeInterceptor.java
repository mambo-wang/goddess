package com.h3c.vdi.athena.homework.stateMachine.interceptor;

import com.h3c.vdi.athena.homework.stateMachine.enums.CheckEvent;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import com.h3c.vdi.athena.homework.stateMachine.listener.PersistStateChangeListener;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;

/**
 * Created by w16051 on 2018/4/2.
 * 拦截器，将我们的监听器应用到状态迁移函数之前。
 * 该类和业务解耦
 */
public class PersistingStateChangeInterceptor extends StateMachineInterceptorAdapter<CheckStatus, CheckEvent> {


    PersistStateChangeListener listener;

    public PersistingStateChangeInterceptor(PersistStateChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 在状态改变前触发监听操作
     * @param state
     * @param message
     * @param transition
     * @param stateMachine
     */
    @Override
    public void preStateChange(State<CheckStatus, CheckEvent> state, Message<CheckEvent> message, Transition<CheckStatus, CheckEvent> transition, StateMachine<CheckStatus, CheckEvent> stateMachine) {
        //2,5
        listener.onPersist(state, message, transition, stateMachine);
    }
}
