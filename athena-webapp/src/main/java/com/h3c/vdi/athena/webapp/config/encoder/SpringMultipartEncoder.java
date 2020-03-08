package com.h3c.vdi.athena.webapp.config.encoder;


import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by w14014 on 2018/10/12.
 */
public class SpringMultipartEncoder implements Encoder {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final Class MULTIPART_ARRAY_CLAZZ = MultipartFile[].class;
    private static final String FILES__KEY = "multipartFiles";
    private final List<HttpMessageConverter<?>> converters = new RestTemplate().getMessageConverters();
    private final HttpHeaders multipartHeaders = new HttpHeaders();
    private final HttpHeaders jsonHeaders = new HttpHeaders();

    public SpringMultipartEncoder(){
        multipartHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void encode(Object o, Type type, RequestTemplate requestTemplate) throws EncodeException {
        if(isFormRequest(type)){
            encodeMultipartFormRequest(o, requestTemplate);
        } else {
            encodeRequest(o, jsonHeaders, requestTemplate);
        }
    }

    private void encodeMultipartFormRequest(Object object, RequestTemplate template){
        if(Objects.isNull(object)){
            throw new EncodeException("Cannot encode request with null form");
        }
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        if(isMultipartFile(object)){
            MultipartFile multipartFile = (MultipartFile) object;
            map.add(multipartFile.getName(), encodeMultipartFile(multipartFile));
        } else if(isMultipartFileArray(object)){
            encodeMultipartFiles(map, FILES__KEY, Arrays.asList((MultipartFile[])object));
        } else {
            map.add("", encodeJsonObject(object));
        }
        encodeRequest(map, multipartHeaders, template);

    }

    private boolean isMultipartFile(Object o){
        return o instanceof MultipartFile;
    }

    private boolean isMultipartFileArray(Object o){
        return o != null && o.getClass().isArray() && MultipartFile.class.isAssignableFrom(o.getClass().getComponentType());
    }

    private HttpEntity<?> encodeMultipartFile(MultipartFile file){
        HttpHeaders filePartHeaders = new HttpHeaders();
        filePartHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            Resource multipartFileResource = new MultipartFileResource(file.getOriginalFilename(), file.getSize(), file.getInputStream());
            return new HttpEntity<>(multipartFileResource, filePartHeaders);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EncodeException("cannot encode request.", e);
        }
    }

    private void encodeMultipartFiles(LinkedMultiValueMap<String, Object> map, String name, List<? extends MultipartFile> files){

        HttpHeaders filePartHeaders = new HttpHeaders();
        filePartHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            for (MultipartFile file: files){
                Resource multipartFileResource = new MultipartFileResource(file.getOriginalFilename(), file.getSize(), file.getInputStream());
                map.add(name, new HttpEntity<>(multipartFileResource, filePartHeaders));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new EncodeException("cannot encode request.", e);
        }
    }

    private HttpEntity<?> encodeJsonObject(Object o){
        HttpHeaders jsonPartHeaders = new HttpHeaders();
        jsonPartHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(o, jsonPartHeaders);
    }

    private void encodeRequest(Object value, HttpHeaders requestHeaders, RequestTemplate template) throws EncodeException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpOutputMessage dummyRequest = new HttpOutputMessageImpl(outputStream, requestHeaders);
        try{
            Class<?> requestType = value.getClass();
            MediaType requestContentType = requestHeaders.getContentType();
            for (HttpMessageConverter<?> messageConverter : converters) {
                if(messageConverter.canWrite(requestType, requestContentType)){
                    ((HttpMessageConverter<Object>)messageConverter).write(value, requestContentType, dummyRequest);
                    break;
                }
            }
        } catch (IOException ex){
            throw new EncodeException("cannot encode request.", ex);
        }

        HttpHeaders httpHeaders = dummyRequest.getHeaders();
        if(httpHeaders != null){
            for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()){
                template.header(entry.getKey(), entry.getValue());
            }
        }

        template.body(outputStream.toByteArray(), DEFAULT_CHARSET);

    }

    private static boolean isFormRequest(Type type){
        return MAP_STRING_WILDCARD.equals(type) || MULTIPART_ARRAY_CLAZZ.equals(type);
    }




}
