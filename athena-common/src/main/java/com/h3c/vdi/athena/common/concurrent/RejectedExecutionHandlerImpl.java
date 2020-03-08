package com.h3c.vdi.athena.common.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * VDI RejectedExecutionHandler自定义实现类。当任务无法执行时，触发此handler。
 */
public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

    protected Logger log = LoggerFactory.getLogger(RejectedExecutionHandlerImpl.class);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.warn("queue full, {} rejected, {}", String.valueOf(r), String.valueOf(executor));
    }
}

