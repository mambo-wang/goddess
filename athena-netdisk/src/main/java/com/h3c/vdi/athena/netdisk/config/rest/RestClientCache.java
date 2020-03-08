package com.h3c.vdi.athena.netdisk.config.rest;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
public enum  RestClientCache {

    /** 唯一实例 */
    INSTANCE;

    public static final Long DEFAULT = 1L;

    public static final Long CUSTOM = 2L;

    /**
     * 1L:使用admin账号授权，一直存在
     * 2L:使用普通用户账号授权，用于文件分享操作
     * */
    private ConcurrentMap<Long, NCRestClient> ncRestClients = new ConcurrentHashMap<>();

    public NCRestClient putDefault(NCRestClient ncRestClient){
        return ncRestClients.put(DEFAULT, ncRestClient);
    }

    public NCRestClient get(Long restClientId){
        return ncRestClients.get(restClientId);
    }

    public NCRestClient putCustom(NCRestClient ncRestClient){
        return ncRestClients.put(CUSTOM, ncRestClient);
    }

    public void shutdown(){
        if(Objects.nonNull(get(CUSTOM))){
            get(CUSTOM).close();
        }
    }
}
