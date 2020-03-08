package com.h3c.vdi.athena.homework.stateMachine.handler;

import com.h3c.vdi.athena.homework.stateMachine.enums.CheckEvent;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import com.h3c.vdi.athena.homework.stateMachine.interceptor.PersistingStateChangeInterceptor;
import com.h3c.vdi.athena.homework.stateMachine.listener.CompositePersistStateChangeListener;
import com.h3c.vdi.athena.homework.stateMachine.listener.PersistStateChangeListener;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.LifecycleObjectSupport;

import java.util.List;

/**
 * Created by w16051 on 2018/4/2.
 * 状态机处理器，主要职责：1、增加监听器，并通过监听函数加入我们自己的逻辑。2、增加拦截器，使我们的监听器能够被状态机所处理。
 * 该类和业务解耦
 */
public class PersistStateMachineHandler extends LifecycleObjectSupport {
    private StateMachine<CheckStatus,CheckEvent> stateMachine;

    private final CompositePersistStateChangeListener listeners = new CompositePersistStateChangeListener();
    private final PersistingStateChangeInterceptor interceptor = new PersistingStateChangeInterceptor(listeners);

    public PersistStateMachineHandler(StateMachine<CheckStatus,CheckEvent> stateMachine) {
        this.stateMachine=stateMachine;
    }

    //项目启动时调用，3，将CheckPersistStateChangeListener注册到CompositePersistStateChangeListener，
    public void addPersistStateChangeListener(PersistStateChangeListener listener) {
        listeners.register(listener);
    }

    /**
     * 状态处理逻辑。1、停止状态机，将状态置为我们需要的当前状态，重启。2、发送事件实现状态迁移。
     * @param event
     * @param status
     * @return
     */
    public boolean handleEventWithState(Message<CheckEvent> event, CheckStatus status){
        stateMachine.stop();
        List<StateMachineAccess<CheckStatus, CheckEvent>> stateMachineAccesses =    stateMachine.getStateMachineAccessor().withAllRegions();
        for(StateMachineAccess<CheckStatus,CheckEvent> item: stateMachineAccesses){
            item.resetStateMachine(new DefaultStateMachineContext<>(status,null,null,null));
        }
        stateMachine.start();
        //1
        return stateMachine.sendEvent(event);
    }

    /**
     * 将监听器加入到状态机的处理流程中
     * 项目启动时调用，4，将包含注册好listener的listeners的拦截器加入到状态机的处理流程中
     * @throws Exception
     */
    @Override
    protected void onInit() throws Exception {
        stateMachine.getStateMachineAccessor().doWithAllRegions(var -> var.addStateMachineInterceptor(interceptor));
    }
}
