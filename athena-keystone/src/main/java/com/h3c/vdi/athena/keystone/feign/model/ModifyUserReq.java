package com.h3c.vdi.athena.keystone.feign.model;

import lombok.Data;

/**
 * Created by y17381 on 2018/9/8.
 */
@Data
public class ModifyUserReq {

    private String email;
    private String quota;
    private String displayname;
    private String phone;
    private String address;
    private String website;
    private String password;

}
