package com.h3c.vdi.athena.common.config;

import com.h3c.vdi.athena.common.utils.RedisUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author w14014
 * @date 2018/9/22
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Component
@PropertySource(value = "classpath:conf/redis.yml", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "redis")
@Configuration
@EnableCaching
public class RedisConfig extends JedisPoolConfig{

    @Value("${host}")
    private String host;

    @Value("${port}")
    private int port = 6379;

    @Value("${password}")
    private String password;

    @Value("${timeout}")
    private int timeout;

    @Value("${maxIdle}")
    private int maxIdle;

    @Value("${minIdle}")
    private int minIdle;

    @Value("${database}")
    private int database = 0;

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        //默认缓存过期时间:一个星期(秒)
        cacheManager.setDefaultExpiration(TimeUnit.DAYS.toSeconds(7));
        return cacheManager;
    }

    /**
     * redis模板，使用jdk序列化
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);

        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * redis连接池
     * @return
     */
    @Bean
    public JedisConnectionFactory redisConnectionFactory(){
        JedisConnectionFactory factory = new JedisConnectionFactory(this);
        factory.setHostName(host);
        factory.setPort(port);
        factory.setPassword(password);
        factory.setTimeout(timeout);
        factory.setDatabase(database);
        return factory;
    }

    @Bean
    public RedisUtil redisUtil(RedisTemplate<String, Object> redisTemplate){
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }



}
