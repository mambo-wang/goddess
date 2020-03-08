package com.h3c.vdi.athena.homework.config.stateMachine;

import com.h3c.vdi.athena.homework.stateMachine.enums.CheckEvent;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import com.h3c.vdi.athena.homework.stateMachine.handler.PersistStateMachineHandler;
import com.h3c.vdi.athena.homework.stateMachine.listener.CheckRegistrarStateChangeListener;
import com.h3c.vdi.athena.homework.stateMachine.listener.CheckUserGroupRegistrarStateChangeListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import javax.annotation.Resource;
import java.util.EnumSet;

/**
 *
 * @author w16051
 * @date 2018/4/2
 */

@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<CheckStatus,CheckEvent>{

    @Resource
    private StateMachine<CheckStatus,CheckEvent> stateMachine;

    /**
     * 配置状态机初始化，将所有状态置为 UNCHECKED
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<CheckStatus,CheckEvent> states) throws Exception{
        states.withStates()
                .initial(CheckStatus.UNCHECKED)
                .states(EnumSet.allOf(CheckStatus.class));
    }

    /**
     * 配置状态机状态迁移，例如从初始状态（source）到目标状态（target）需要由事件（event）触发
     * @param transitions
     * @throws Exception
     */
    @Override
    //项目启动时调用，1
    public void configure(StateMachineTransitionConfigurer<CheckStatus, CheckEvent> transitions) throws Exception {
        transitions.withExternal()
                .source(CheckStatus.UNCHECKED).target(CheckStatus.PASSED)
                .event(CheckEvent.PASS)
                .and()
                .withExternal()
                .source(CheckStatus.UNCHECKED).target(CheckStatus.UNPASSED)
                .event(CheckEvent.UNPASS);
    }

    /**
     * 1、初始化Handler
     * 2、加入我们自定义的监听器
     *
     * @return
     */
    @Bean
    public PersistStateMachineHandler persistStateMachineHandler() {
        //项目启动时调用，2 ,新建一个Handler并且将statemachine赋给它
        PersistStateMachineHandler persistStateMachineHandler = new PersistStateMachineHandler(stateMachine);
        //项目启动时调用，3，将CheckRegistrarStateChangeListener注册到CompositePersistStateChangeListener，
        //从而sendEvent时会触发该监听器并处理相关的业务逻辑
        persistStateMachineHandler.addPersistStateChangeListener(checkRegistrarStateChangeListener());
        persistStateMachineHandler.addPersistStateChangeListener(checkUserGroupRegistrarStateChangeListener());
        return persistStateMachineHandler;
    }

    @Bean
    public CheckRegistrarStateChangeListener checkRegistrarStateChangeListener(){
        return new CheckRegistrarStateChangeListener();
    }

    @Bean
    public CheckUserGroupRegistrarStateChangeListener checkUserGroupRegistrarStateChangeListener(){
        return new CheckUserGroupRegistrarStateChangeListener();
    }
}
