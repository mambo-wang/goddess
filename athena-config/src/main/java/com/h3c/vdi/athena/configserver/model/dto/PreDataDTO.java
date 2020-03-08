package com.h3c.vdi.athena.configserver.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 *
 * @author w14014
 * @date 2018/10/19
 */
@Data
public class PreDataDTO implements Serializable{

    private static final long serialVersionUID = -2422785469354353L;
    /** 可选的文件系统类型 */
    List<String> fileSystemTypes;

    List<DiskDTO> disks;
}
