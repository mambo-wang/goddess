package com.h3c.vdi.athena.netdisk.config.rest;

import com.h3c.vdi.athena.common.utils.RedisUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 *
 * @author w14014
 * @date 2018/9/18
 */
@Data
@Component
@PropertySource(value = "classpath:conf/nextcloud.properties", ignoreResourceNotFound = true)
public class NextCloudConfig {

    @Value("${nextcloud.protocol}")
    private String protocol;

    private String host;

    private Integer port = 58082;

    @Value("${nextcloud.username}")
    private String username;

    @Value("${nextcloud.password}")
    private String password;

    public static RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil){
        NextCloudConfig.redisUtil = redisUtil;
    }

    public String accessUrl(String uri) {
        this.host = (String) redisUtil.get(RedisUtil.REDIS_KEY_HOST);
        String baseUrl = this.protocol + "://" + this.host + ":" +port;
        if(StringUtils.isNotEmpty(uri) && uri.startsWith("/")){
            uri = uri.substring(1);
        }
        //文件名中的空格需要替换为%20
        uri = org.springframework.util.StringUtils.replace(uri, " ", "%20");
        return baseUrl + "/" + uri;
    }

}
