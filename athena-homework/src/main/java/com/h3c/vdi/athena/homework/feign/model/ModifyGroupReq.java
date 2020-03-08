package com.h3c.vdi.athena.homework.feign.model;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author w14014
 * @date 2018/9/28
 */
@Builder
public class ModifyGroupReq {

    private String oldGroupId;

    private String newGroupId;
}
