package com.h3c.vdi.athena.netdisk.model.entity;


import lombok.Data;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */

@Deprecated
@Data
public class Share {

    private Long id;

    private Integer shareType;

    private String uIdOwner;

    private Long sTime;

    private String itemType;

    private String fileTarget;

    private String shareWith;
}
