package com.h3c.vdi.athena.homework.dto;

import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author w14014
 * @date 2018/10/16
 */
@Data
public class UserGroupRegistrarDTO implements Serializable{

    private static final long serialVersionUID = 5384607388149177868L;

    private Long id;

    private UserDTO user;

    private Long groupId;

    private String groupName;

    private Long submitTime;

    private CheckStatus checkStatus;

}
