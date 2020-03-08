package com.h3c.vdi.athena.netdisk.model.rest;

import lombok.Data;

/**
 *
 * @author w14014
 * @date 2018/9/28
 */
@Data
public class ModifyGroupReq {

    private String oldGroupId;

    private String newGroupId;
}
