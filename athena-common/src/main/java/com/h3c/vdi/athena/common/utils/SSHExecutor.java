package com.h3c.vdi.athena.common.utils;

import com.h3c.vdi.athena.common.config.SSHConfig;
import com.h3c.vdi.athena.common.exception.AppException;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author w14014
 * @date 2018/9/18
 */
@Slf4j
public class SSHExecutor implements Closeable{
    private static long INTERVAL = 100L;
    private static int SESSION_TIMEOUT = 30000;
    private static int CHANNEL_TIMEOUT = 3000;
    private static int BUFFER = 1024;
    private JSch jsch = null;
    private Session session = null;

    /**
     * 新建ssh连接
     * @param sshConfig ssh连接相关参数配置信息
     * @throws JSchException 异常
     */
    private SSHExecutor(SSHConfig sshConfig) throws JSchException {
        jsch =new JSch();
        session = jsch.getSession(sshConfig.getUsername(), sshConfig.getHost(), sshConfig.getPort());
        session.setPassword(sshConfig.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(SESSION_TIMEOUT);
    }

    /**
     * @return 获取实例
     * @throws JSchException 异常
     */
    public static SSHExecutor newInstance(SSHConfig sshConfig) throws JSchException {
        return new SSHExecutor(sshConfig);
    }

    /**
     * 执行某命令，返回结果
     * @param cmd shell 命令
     * @return 结果集
     * @throws IOException IO异常
     * @throws JSchException ssh异常
     * @throws InterruptedException 中断异常
     */
    public Set<String> exec(String cmd) throws IOException, JSchException, InterruptedException {
        ChannelExec channelExec = (ChannelExec)session.openChannel( "exec" );
        channelExec.setCommand(cmd);

        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        channelExec.setErrStream(errorStream);
        InputStream in = channelExec.getInputStream();
        channelExec.connect();

        int res;
        StringBuilder builder = new StringBuilder();
        byte[] tmp = new byte[ BUFFER ];
        while ( true ) {
            while ( in.available() > 0 ) {
                int i = in.read( tmp, 0, BUFFER );
                if ( i < 0 ) {
                    break;
                }
                builder.append(new String( tmp, 0, i ));
            }
            if ( channelExec.isClosed() ) {
                res = channelExec.getExitStatus();
                break;
            }
            TimeUnit.MILLISECONDS.sleep(100);
        }
        channelExec.disconnect();
        if(res != 0) {
            String errorInfo = errorStream.toString();
            log.error("the command {} execute failed, cause {}", cmd, errorInfo);
            throw new AppException(errorInfo);
        }
        String[] infoItemArray = builder.toString().split("\n");
        return Arrays.stream(infoItemArray).collect(Collectors.toSet());
    }

    private Session getSession(){
        return session;
    }

    @Override
    public void close(){
        getSession().disconnect();
    }

}

