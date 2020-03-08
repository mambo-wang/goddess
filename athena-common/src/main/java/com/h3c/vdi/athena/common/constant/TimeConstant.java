package com.h3c.vdi.athena.common.constant;

import java.time.ZoneId;

/**
 * Created by JemmyZhang on 2018/2/12
 */
public interface TimeConstant {

    ZoneId BEIJING_ZONE = ZoneId.of("UTC+08:00");

    Long SECOND_IN_MILLS = 1000L;

    Long MINUTE_IN_MILLS = SECOND_IN_MILLS * 60;

    Long HOUR_IN_MILLIS = MINUTE_IN_MILLS * 60;

    Long DAY_IN_MILLS = HOUR_IN_MILLIS * 24;
}
