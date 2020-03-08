package com.h3c.vdi.athena.netdisk.config.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.Jaxb2CollectionHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriTemplateHandler;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author z15722
 * @date 2018/8/28 14:46
 */
@Slf4j
public class NCRestClient implements Closeable {

    /** 登录客户端 */
    private CloseableHttpClient client = null;
    /** rest服务 */
    private RestTemplate restTemplate = null;

    private NextCloudConfig nextCloudConfig = null;

    public NCRestClient(NextCloudConfig nextCloudConfig) {
        this(nextCloudConfig, 100, 100);
        this.nextCloudConfig = nextCloudConfig;
    }

    private NCRestClient(NextCloudConfig nextCloudConfig, int maxTotalConnections, final int perRouteConnections) {
        this(nextCloudConfig.getProtocol(), nextCloudConfig.getHost(), nextCloudConfig.getPort(), nextCloudConfig.getUsername(),
                nextCloudConfig.getPassword(), maxTotalConnections, perRouteConnections);
    }

    private NCRestClient(String protocol, String host, int port, String username, String password, int maxTotalConnections, final int perRouteConnections) {

        //Client Pool
        PoolingHttpClientConnectionManager ccm = initConnectionPool(host, port, maxTotalConnections, perRouteConnections);

        // 认证信息
        CredentialsProvider credentialsProvider = initCredentialsProvider(host, port, username, password);

        // Create global request configuration
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.BASIC))
                .setProxyPreferredAuthSchemes(Collections.singletonList(AuthSchemes.BASIC))
                .setSocketTimeout(120000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();

        client = HttpClients.custom()
                .setConnectionManager(ccm)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();


        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(client));
        restTemplate.getMessageConverters().add(new Jaxb2CollectionHttpMessageConverter());
        restTemplate.getMessageConverters().add(new Jaxb2RootElementHttpMessageConverter());
        restTemplate.setUriTemplateHandler(initUriTemplateHandler(protocol, host, port));
        // restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        restTemplate.getInterceptors().add(new AcceptHeaderInterceptor());

    }

    private CredentialsProvider initCredentialsProvider(String host, int port, String username, String password) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        if (username != null && password != null) {
            credentialsProvider.setCredentials(
                    new AuthScope(host, port, null),
                    new UsernamePasswordCredentials(username, password));
        }
        return credentialsProvider;
    }

    private PoolingHttpClientConnectionManager initConnectionPool(String host, int port, int maxTotalConnections, int perRouteConnections) {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(initSSLContext(), NoopHostnameVerifier.INSTANCE))
                .build();

        PoolingHttpClientConnectionManager ccm = new PoolingHttpClientConnectionManager(registry);

        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
//                .setSoTimeout(60000)// 读取数据超时
                .setSoReuseAddress(true)// 允许地址复用
                .setRcvBufSize(60000)// socket缓冲
                .setSndBufSize(60000)
                .build();
        ccm.setDefaultSocketConfig(socketConfig);
        ccm.setSocketConfig(new HttpHost(host, port), socketConfig);
        ccm.setMaxTotal(maxTotalConnections);
        ccm.setDefaultMaxPerRoute(perRouteConnections);
        ccm.setMaxPerRoute(new HttpRoute(new HttpHost(host, port)), perRouteConnections);
        return ccm;
    }

    private SSLContext initSSLContext() {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, null);
            return sc;
        } catch (Exception e) {
            log.warn(null, e);
            throw new RuntimeException("Can not init SSLContext");
        }
    }

    private UriTemplateHandler initUriTemplateHandler(String protocol, String host, int port) {
        DefaultUriTemplateHandler handler = new DefaultUriTemplateHandler();
        handler.setBaseUrl(protocol + "://" + host + ":" + port);
        return handler;
    }

    /**
     * 获取当前HttpClient对应的RestTemplate
     *
     * @return 当前HttpClient对应的RestTemplate
     */
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public NextCloudConfig getNextCloudConfig(){
        return nextCloudConfig;
    }

    /**
     * 退出时关闭。
     */
    @Override
    public void close() {
        try {
            this.client.close();
        } catch (IOException e) {
            log.warn("client close error", e);
        }
    }

    /** 信任任何内容的 TrustManager。 */
    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        /**
         * 检查客户端信任证书
         *
         * @param arg0 证书
         * @param arg1 类型
         * @throws java.security.cert.CertificateException 证书异常
         */
        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1)
                throws java.security.cert.CertificateException {

        }

        /**
         * 检查服务器信任证书
         *
         * @param arg0 证书
         * @param arg1 类型
         * @throws java.security.cert.CertificateException 证书异常
         */
        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1)
                throws java.security.cert.CertificateException {

        }
    }

    /**
     * 自定义REST Template Request Accept Header类型
     */
    private static class AcceptHeaderInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().setAccept(acceptHeaders());
            return execution.execute(request, body);
        }

        private List<MediaType> acceptHeaders() {
            List<MediaType> types = new ArrayList<>();
            types.add(new MediaType("application", "xml", Charset.forName("UTF-8")));
            types.add(new MediaType("application", "json", Charset.forName("UTF-8")));
            return types;
        }
    }

}
