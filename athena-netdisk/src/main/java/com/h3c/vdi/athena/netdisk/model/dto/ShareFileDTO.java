package com.h3c.vdi.athena.netdisk.model.dto;

import lombok.Data;

/**
 * Created by w14014 on 2018/9/26.
 */
@Data
public class ShareFileDTO {

    Integer shareType;

    String shareWith;

    String path;
}
