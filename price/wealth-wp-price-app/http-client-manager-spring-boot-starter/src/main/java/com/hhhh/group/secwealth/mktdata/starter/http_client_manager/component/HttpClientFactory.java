/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.bean.Connection;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.constant.Constant;

import lombok.Setter;

public class HttpClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientFactory.class);

    @Setter
    private String initDefaultExCode;

    @Setter
    private Connection connection;

    public CloseableHttpClient create() throws Exception {
        final HttpClientBuilder httpClientBuilder = HttpClients.custom();
        try {
            addHttpsTrust(httpClientBuilder);
            if (this.connection.getEnableGzip()) {
                disableDefaultCompression(httpClientBuilder);
            }
            httpClientBuilder.setRetryHandler(retryHandler());
            httpClientBuilder.setDefaultRequestConfig(requestConfig());
            httpClientBuilder.setKeepAliveStrategy(keepAliveStrategy());
            return httpClientBuilder.build();
        } catch (Exception e) {
            HttpClientFactory.logger.error("Init HttpClient encounter error", e);
            throw new Exception(this.initDefaultExCode, e);
        }
    }

    private void addHttpsTrust(final HttpClientBuilder httpClientBuilder) throws Exception {
        final SSLContext sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
        final X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(final X509Certificate[] xcs, final String string) {}

            public void checkServerTrusted(final X509Certificate[] xcs, final String string) {}

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        };
        sslContext.init(null, new TrustManager[] {trustManager}, new SecureRandom());

        final SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
            new String[] {"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
        final Registry<ConnectionSocketFactory> registry =
            RegistryBuilder.<ConnectionSocketFactory> create().register("https", sslConnectionSocketFactory)
                .register("http", PlainConnectionSocketFactory.getSocketFactory()).build();
        final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
            new PoolingHttpClientConnectionManager(registry);
        poolingHttpClientConnectionManager.setMaxTotal(this.connection.getMaxTotal());
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(this.connection.getMaxPerRoute());
        poolingHttpClientConnectionManager.setValidateAfterInactivity(100);
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
    }

    private void disableDefaultCompression(final HttpClientBuilder httpClientBuilder) {
        httpClientBuilder.disableContentCompression().addInterceptorFirst(new HttpRequestInterceptor() {
            @Override
            public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader(Constant.HTTP_REQUEST_HEADER_ACCEPTCODING)) {
                    request.addHeader(Constant.HTTP_REQUEST_HEADER_ACCEPTCODING, Constant.HTTP_COMPRESSION_GZIP);
                }
            }
        }).addInterceptorLast(new HttpResponseInterceptor() {
            @Override
            public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    final Header contentEncodingHeader = entity.getContentEncoding();
                    if (contentEncodingHeader != null) {
                        final HeaderElement[] headerElements = contentEncodingHeader.getElements();
                        for (final HeaderElement headerElement : headerElements) {
                            if (headerElement.getName().equalsIgnoreCase(Constant.HTTP_COMPRESSION_GZIP)) {
                                response.setEntity(new GzipDecompressingEntity(entity));
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    private HttpRequestRetryHandler retryHandler() {
        return new HttpRequestRetryHandler() {
            public boolean retryRequest(final IOException exception, final int executionCount, final HttpContext context) {
                if (executionCount >= HttpClientFactory.this.connection.getRetryCount()) {
                    return false;
                }
                final HttpClientContext clientContext = HttpClientContext.adapt(context);
                final HttpRequest request = clientContext.getRequest();
                return !(request instanceof HttpEntityEnclosingRequest);
            }
        };
    }

    private RequestConfig requestConfig() {
        final Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(this.connection.getConnectTimeout()).setSocketTimeout(this.connection.getSocketTimeout());
        if (this.connection.getEnableProxy()) {
            builder.setProxy(new HttpHost(this.connection.getProxyHostname(), this.connection.getProxyPort()));
        }
        return builder.build();
    }

    private ConnectionKeepAliveStrategy keepAliveStrategy() {
        return new DefaultConnectionKeepAliveStrategy() {

            private final static long KEEP_ALIVE_MILLISECONDS = 250 * 1000;

            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                long keepAlive = super.getKeepAliveDuration(response, context);
                if(keepAlive == -1 || keepAlive > KEEP_ALIVE_MILLISECONDS){
                    keepAlive = KEEP_ALIVE_MILLISECONDS;
                }
                return keepAlive;
            }
        };
    }
}
