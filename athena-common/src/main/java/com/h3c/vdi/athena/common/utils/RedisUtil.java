package com.h3c.vdi.athena.common.utils;

import com.h3c.vdi.athena.common.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author w14014
 * @date 2018/9/22
 */
@Slf4j
public class RedisUtil {

    public static final String REDIS_KEY_SSHCONFIG = "docker_host_ssh_config";

    public static final String REDIS_KEY_HOST = "docker_host_ip";

    private RedisTemplate<String, Object> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /************************************************ common ********************************************************
     * 指定缓存失效时间
     * @param key 键
     * @param time time 秒
     * @return
     */
    public boolean expire(String key, long time){
        try{
            if(time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e){
            log.error("set expire fail, key:{}, value:{}", key, time);
            return false;
        }
    }

    public boolean persist(String key){
        try {
            redisTemplate.persist(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断key是否存在
     * @param key key键
     * @return
     */
    public boolean hasKey(String key){

        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("-------------redis had key fail:{}", e.getMessage());
            return false;
        }
    }

    /**
     * 批量删除缓存
     * @param key key
     */
    public void del(String... key){
        if(Objects.nonNull(key) && key.length >0){
            if(key.length == 1){
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /******************************************** string ****************************************
     * 获取缓存
     * @param key key键
     * @return 值
     */
    public Object get(String key){
        return key == null?null:redisTemplate.opsForValue().get(key);
    }

    /**
     * 存入缓存
     * @param key key
     * @param value value
     * @return 是否成功
     */
    public boolean set(String key, Object value){

        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis insert fail, key:{},value{}, cause:{}", key, value, e.getMessage());
            return false;
        }
    }

    /**
     * 存入缓存并设置超时时间
     * @param key key
     * @param value value
     * @param time s时间
     * @return 成功失败
     */
    public boolean set(String key, Object value, long time){
        try{
            if(time > 0){
                redisTemplate.opsForValue().set(key, value, time);
            } else {
                set(key, value);
            }
            return true;
        }catch (Exception e){
            log.error("redis insert fail, key:{},value{}, cause:{}", key, value, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key key
     * @param delta 递增因子，增加几
     * @return 递增之后的值
     */
    public long incr(String key, long delta){
        if(delta < 0){
            throw new AppException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key key
     * @param delta 递减因子，减少几
     * @return 递减之后的值
     */
    public long decr(String key, long delta){
        if(delta < 0){
            throw new AppException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * ********************************************* Map **********************************************************
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item){
        return redisTemplate.opsForHash().get(key, item);
    }



}
