package com.h3c.vdi.athena.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginRespDTO {
    private String token;
    private UserDTO user;
}
