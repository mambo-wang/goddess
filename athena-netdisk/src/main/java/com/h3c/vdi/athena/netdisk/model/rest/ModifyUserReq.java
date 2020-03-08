package com.h3c.vdi.athena.netdisk.model.rest;

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
