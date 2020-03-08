package com.h3c.vdi.athena.homework.entity;

import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author w14014
 * @date 2018/10/16
 */
@Data
@Entity
@Table(name = "tbl_user_group_registrar")
public class UserGroupRegistrar implements Serializable {
    private static final long serialVersionUID = -5607194197253393240L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "GROUP_ID")
    private Long groupId;

    @Column(name = "SUBMIT_TIME")
    private Long submitTime;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private CheckStatus checkStatus;


}
