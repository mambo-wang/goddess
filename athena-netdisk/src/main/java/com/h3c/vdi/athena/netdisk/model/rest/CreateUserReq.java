package com.h3c.vdi.athena.netdisk.model.rest;

import lombok.Data;

/**
 * @author z15722
 * @date 2018/8/28 19:52
 */
@Data
public class CreateUserReq {
    private String userid;
    private String password;
}
