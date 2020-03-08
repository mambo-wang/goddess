package com.h3c.vdi.athena.configserver.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
@Data
public class Skin implements Serializable{

    private static final long serialVersionUID = -4513942269058330718L;
    private String username;

    private Integer skinNumber;

}
