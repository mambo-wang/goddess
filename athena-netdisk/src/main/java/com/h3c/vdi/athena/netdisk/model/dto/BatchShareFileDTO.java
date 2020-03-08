package com.h3c.vdi.athena.netdisk.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
@Data
public class BatchShareFileDTO implements Serializable{

    private static final long serialVersionUID = -3563389203833356789L;
    List<String> shareWith;

    List<String> path;
}
