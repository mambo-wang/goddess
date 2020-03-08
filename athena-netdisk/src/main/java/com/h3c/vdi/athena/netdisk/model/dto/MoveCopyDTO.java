package com.h3c.vdi.athena.netdisk.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/18
 */
@Data
public class MoveCopyDTO implements Serializable{

    private static final long serialVersionUID = 5365912064533694587L;
    List<String> srcPath;

    String destPath;

}
