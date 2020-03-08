package com.h3c.vdi.athena.common.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * VDI ThreadFactory自定义实现类
 */
public class DaemonThreadFactory implements ThreadFactory {

    private String threadName = "VdiDaemonThread";

    public DaemonThreadFactory() {
    }

    public DaemonThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        if (this.threadName != null) {
            thread.setName(this.threadName + "-" + thread.getId());
        }
        return thread;
    }
}