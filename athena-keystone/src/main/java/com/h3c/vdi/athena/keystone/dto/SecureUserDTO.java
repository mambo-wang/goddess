package com.h3c.vdi.athena.keystone.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JemmyZhang on 2018/3/1
 * @author w14014
 */
@Getter
@Setter
public class SecureUserDTO implements Serializable{

    private static final long serialVersionUID = 2940968607097238413L;

    private String username;

    private String password;

    private List<String> roles;
}
