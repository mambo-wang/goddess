package com.h3c.vdi.athena.netdisk.config.rest;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.Objects;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
@Slf4j
@Component
public class RestConnection {

    @Resource
    private NextCloudConfig nextCloudConfig;

    @Resource
    private RedisUtil redisUtil;

    public void createRestClient(){
        RestClientCache.INSTANCE.shutdown();
        NextCloudConfig defaultConfig = new NextCloudConfig();
        defaultConfig.setUsername(nextCloudConfig.getUsername());
        defaultConfig.setPassword(nextCloudConfig.getPassword());
        String host = (String) redisUtil.get(RedisUtil.REDIS_KEY_HOST);
        defaultConfig.setHost(host);
        defaultConfig.setProtocol(nextCloudConfig.getProtocol());
        defaultConfig.setPort(nextCloudConfig.getPort());
        RestClientCache.INSTANCE.putDefault(new NCRestClient(defaultConfig));
    }

    public void createRestClient(String username, String password){
        RestClientCache.INSTANCE.shutdown();
        NextCloudConfig customConfig = new NextCloudConfig();
        customConfig.setUsername(username);
        customConfig.setPassword(password);
        String host = (String) redisUtil.get(RedisUtil.REDIS_KEY_HOST);
        customConfig.setHost(host);
        customConfig.setProtocol(nextCloudConfig.getProtocol());
        customConfig.setPort(nextCloudConfig.getPort());
        RestClientCache.INSTANCE.putCustom(new NCRestClient(customConfig));
    }

    private RestTemplate findRestTemplate(Long restClientId){
        NCRestClient ncRestClient = RestClientCache.INSTANCE.get(restClientId);
        if(Objects.isNull(ncRestClient) || Objects.isNull(ncRestClient.getRestTemplate())){
            if(Objects.equals(RestClientCache.DEFAULT, restClientId)){
                createRestClient();
                return findRestTemplate(restClientId);
            }
            log.error("the RestTemplate not exists, rest client id is {}", restClientId);
            throw new AppException("Rest客户端出错！");
        }
        return ncRestClient.getRestTemplate();
    }

    private NextCloudConfig findNextCloudConfig(Long restClientId){
        NCRestClient ncRestClient = RestClientCache.INSTANCE.get(restClientId);
        if(Objects.isNull(ncRestClient) || Objects.isNull(ncRestClient.getNextCloudConfig())){
            if(Objects.equals(RestClientCache.DEFAULT, restClientId)){
                createRestClient();
                return findNextCloudConfig(restClientId);
            }
            log.error("the NextCloudConfig not exists, rest client id is {}", restClientId);
            throw new AppException("Rest客户端出错！");
        }
        return ncRestClient.getNextCloudConfig();
    }

    /**
     * post
     * @param url
     * @param entity
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> post(String url, HttpEntity entity, Class<T> responseType, Long restClientId) {
        return findRestTemplate(restClientId).postForEntity(findNextCloudConfig(restClientId).accessUrl(url), createHttpEntity(entity, restClientId), responseType);
    }

    public <T> ResponseEntity<T> postWithDefault(String url, HttpEntity entity, Class<T> responseType) {
        return post(url, entity, responseType, RestClientCache.DEFAULT);
    }

    public <T> ResponseEntity<T> postWithCustom(String url, HttpEntity entity, Class<T> responseType) {
        return post(url, entity, responseType, RestClientCache.CUSTOM);
    }

    /**
     * post
     * @param url
     * @param entity
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> put(String url, HttpEntity entity, ParameterizedTypeReference<T> responseType, Long restClientId) {
        return findRestTemplate(restClientId).exchange(findNextCloudConfig(restClientId).accessUrl(url), HttpMethod.PUT, createHttpEntity(entity, restClientId), responseType);
    }

    public <T> ResponseEntity<T> putWithDefault(String url, HttpEntity entity, ParameterizedTypeReference<T> responseType) {
        return put(url, entity, responseType, RestClientCache.DEFAULT);
    }

    public <T> ResponseEntity<T> putWithCustom(String url, HttpEntity entity, ParameterizedTypeReference<T> responseType) {
        return put(url, entity, responseType, RestClientCache.CUSTOM);
    }

    /**
     * get
     * @param url
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType, Long restClientId) {
        return findRestTemplate(restClientId).exchange(findNextCloudConfig(restClientId).accessUrl(url), HttpMethod.GET, createHttpEntity(null, restClientId), responseType);
    }

    public <T> ResponseEntity<T> getWithDefault(String url, ParameterizedTypeReference<T> responseType) {
        return get(url, responseType, RestClientCache.DEFAULT);
    }

    public <T> ResponseEntity<T> getWithCustom(String url, ParameterizedTypeReference<T> responseType) {
        return get(url, responseType, RestClientCache.CUSTOM);
    }


    public <T> ResponseEntity<T> delete(String url, ParameterizedTypeReference<T> responseType, Long restClientId) {
        return findRestTemplate(restClientId).exchange(findNextCloudConfig(restClientId).accessUrl(url), HttpMethod.DELETE, createHttpEntity(null, restClientId), responseType);
    }
    public <T> ResponseEntity<T> deleteWithDefault(String url, ParameterizedTypeReference<T> responseType) {
        return delete(url, responseType, RestClientCache.DEFAULT);
    }

    public <T> ResponseEntity<T> deleteWithCustom(String url, ParameterizedTypeReference<T> responseType) {
        return delete(url, responseType, RestClientCache.CUSTOM);
    }

    public <T> ResponseEntity<T> delete(String url, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Long restClientId) {
        return findRestTemplate(restClientId).exchange(findNextCloudConfig(restClientId).accessUrl(url), HttpMethod.DELETE, createHttpEntity(requestEntity, restClientId), responseType);
    }

    public <T> ResponseEntity<T> deleteWithDefault(String url, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {

        return delete(url, requestEntity, responseType, RestClientCache.DEFAULT);
    }

    public <T> ResponseEntity<T> deleteWithCustom(String url, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {

        return delete(url, requestEntity, responseType, RestClientCache.CUSTOM);
    }

    /**
     * 通用header构建
     * @return
     */
    public HttpHeaders commonHeader(Long restClientId) {
        return createHeader(MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE, restClientId);
    }

    public HttpHeaders createHeader(String contentType, String accept, Long restClientId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        headers.add("OCS-APIRequest", "true");
        headers.add(HttpHeaders.AUTHORIZATION, authStr(restClientId));
        return headers;
    }

    /**
     * 产生带token的请求头
     * @param entity
     * @return
     */
    private HttpEntity createHttpEntity(HttpEntity entity, Long restClientId) {
        HttpHeaders headers = commonHeader(restClientId);
        if (entity == null) {
            return new HttpEntity(headers);
        }
        return new HttpEntity(entity.getBody(), headers);
    }

    private String authStr(Long restClientId) {
        NextCloudConfig nextCloudConfig = findNextCloudConfig(restClientId);
        String plainCredentials = nextCloudConfig.getUsername() + ":" + findNextCloudConfig(restClientId).getPassword();
        String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());
        log.info("----------request header auth username:{}, password:{}", nextCloudConfig.getUsername(), nextCloudConfig.getPassword());
        return "Basic " + base64Credentials;
    }


}
