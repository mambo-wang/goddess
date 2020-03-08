package com.h3c.vdi.athena.netdisk.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author w14014
 * @date 2018/9/25
 */
@Data
@AllArgsConstructor
public class SearchResultDTO {

    private String name;

    /**0:个人用户，1：分组*/
    private Integer type;
}
