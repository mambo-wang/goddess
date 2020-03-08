package com.h3c.vdi.athena.common.utils;

import com.alibaba.fastjson.JSON;
import com.h3c.vdi.athena.common.config.SSHConfig;

/**
 *
 * @author w14014
 * @date 2018/11/6
 */
public class JsonUtils {

    /**
     * 对象转换为JSON字符串
     * @param t 对象
     * @param <T> 泛型
     * @return json
     */
    public static <T> String toJson(T t){
        return JSON.toJSONString(t);
    }

    /**
     * Json转换为对象
     * @param json json
     * @param clazz 对象的类型
     * @param <T> 泛型
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> clazz){
        return JSON.parseObject(json, clazz);
    }

    public static SSHConfig parseSSHConfig(Object json) {
        return fromJson((String) json, SSHConfig.class);
    }

}
