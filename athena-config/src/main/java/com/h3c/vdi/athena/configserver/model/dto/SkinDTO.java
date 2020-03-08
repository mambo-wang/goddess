package com.h3c.vdi.athena.configserver.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
@Setter
@ToString
@Getter
public class SkinDTO implements Serializable{

    private static final long serialVersionUID = 8672242511386983889L;
    private String username;
    private Integer skinNumber;
}
