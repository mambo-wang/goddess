package com.h3c.vdi.athena.common.config;

import lombok.*;

/**
 *
 * @author w14014
 * @date 2018/9/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SSHConfig {

    private String username;

    private String password;

    private String host;

    private int port;
}