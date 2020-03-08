package com.h3c.vdi.athena.configserver.config.ssh;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author w14014
 * @date 2018/9/18
 */
@AllArgsConstructor
@NoArgsConstructor
@Component
@PropertySource("classpath:conf/ssh.properties")
public class SSHProperties {

    @Getter
    @Value("${ssh.username}")
    private String username;

    @Getter
    @Value("${ssh.password}")
    private String password;

    @Deprecated
    private String host;

    @Getter
    @Value("${ssh.port}")
    private int port;
}