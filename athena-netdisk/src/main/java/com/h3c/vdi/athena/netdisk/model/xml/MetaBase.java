package com.h3c.vdi.athena.netdisk.model.xml;

import lombok.Data;

import java.io.Serializable;

/**
 * @author z15722
 * @date 2018/8/28 14:26
 */
@Data
public class MetaBase implements Serializable {
    private String status;
    private String statuscode;
    private String message;
    private String totalitems;
    private String itemsperpage;
}
