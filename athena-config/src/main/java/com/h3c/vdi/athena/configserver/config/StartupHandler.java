package com.h3c.vdi.athena.configserver.config;

import com.h3c.vdi.athena.common.concurrent.VDIExecutorServices;
import com.h3c.vdi.athena.configserver.service.config.ConfigMgr;
import com.h3c.vdi.athena.configserver.service.parameter.ParameterMgr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
@Component
@Slf4j
public class StartupHandler {

    @Resource
    private ConfigMgr configMgr;

    @Resource
    private ParameterMgr parameterMgr;

    private final ExecutorService service = VDIExecutorServices.get().getMonitorService();

    @PostConstruct
    private void afterInit(){
        addStartupTarget(new MountLv());
    }

    private <T extends AbstractStartupHandler> void addStartupTarget(T handler) {
        service.submit(handler::run);
    }

    abstract class AbstractStartupHandler {

        protected abstract String getOperateMessage();

        protected abstract void add();

        public void run() {
            boolean flag = true;
            while (flag) {
                try {
                    add();
                    flag = false;
                    log.info("StartupHandler::{} success.", getOperateMessage());
                } catch (Exception ex) {
                    log.warn("StartupHandler::{} failed.", getOperateMessage(), ex.getMessage());
                    try {
                        TimeUnit.SECONDS.sleep(30);
                    } catch (InterruptedException e) {
                        log.warn("StartupHandler::Interrupted while sleep.", e);
                    }
                }
            }
        }
    }

    class MountLv extends AbstractStartupHandler {

        @Override
        protected String getOperateMessage() {
            return "mount lv";
        }

        @Override
        protected void add() {
            configMgr.mountLv();
            parameterMgr.reStoreSSHConfig();
        }
    }

}
