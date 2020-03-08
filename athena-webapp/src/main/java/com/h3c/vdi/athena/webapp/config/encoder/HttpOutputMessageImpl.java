package com.h3c.vdi.athena.webapp.config.encoder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author w14014
 * @date 2018/10/12
 */
@NoArgsConstructor
@AllArgsConstructor
public class HttpOutputMessageImpl implements HttpOutputMessage{

    private OutputStream body;
    private HttpHeaders headers;

    @Override
    public OutputStream getBody() throws IOException {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
