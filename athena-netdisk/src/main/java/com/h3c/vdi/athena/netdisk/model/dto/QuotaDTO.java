package com.h3c.vdi.athena.netdisk.model.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Created by w14014 on 2018/9/25.
 */
@Data
@Builder
public class QuotaDTO {

    private String free;

    private String used;

    private String total;

    private String relative;
}
