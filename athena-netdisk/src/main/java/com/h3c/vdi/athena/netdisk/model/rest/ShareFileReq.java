package com.h3c.vdi.athena.netdisk.model.rest;

import lombok.Data;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
@Data
public class ShareFileReq {

    int shareType;

    String shareWith;

    int permissions;

    String path;

}
