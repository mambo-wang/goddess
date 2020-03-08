package com.h3c.vdi.athena.configserver.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
@Data
public class DiskDTO implements Serializable {
    private static final long serialVersionUID = -1110967554839870250L;

    private String path;

    private String size;

    List<DiskDTO> subDisks;

}
