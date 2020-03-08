package com.h3c.vdi.athena.webapp.config.encoder;

import lombok.Getter;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by w14014 on 2018/10/12.
 */
public class MultipartFileResource extends InputStreamResource {

    @Getter
    private String filename;

    private long size;

    public MultipartFileResource(String filename, long size, InputStream inputStream) {
        super(inputStream);
        this.size = size;
        this.filename = filename;
    }

    @Override
    public InputStream getInputStream() throws IOException, IllegalStateException{
        return super.getInputStream();
    }

    @Override
    public long contentLength(){
        return size;
    }






}
