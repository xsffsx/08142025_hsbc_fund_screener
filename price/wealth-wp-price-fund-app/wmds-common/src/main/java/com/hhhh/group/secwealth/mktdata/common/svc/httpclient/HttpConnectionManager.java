/*
 */
package com.hhhh.group.secwealth.mktdata.common.svc.httpclient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


import com.hhhh.group.secwealth.mktdata.common.service.ToggleService;
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
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import org.apache.commons.lang.StringUtils;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * This class extends RequestHandler,enquire data from vendor by httpclient
 * software. The request method type(get or post) defines in external
 * properties,and we implement our retry handler(myHttpRetryHandler).
 */
public class HttpConnectionManager {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ToggleService toggleService;

    private static String CONTENT_TYPE = "Content-Type";

    private static String PDF_TYPE = "application/pdf";

    private static String COOKIE = "Cookie";

    private DefaultHttpClient httpClient;

    private RequestMethod sendMethodClass;

    /**
     * The max total connections
     */
    private int maxTotalConn = 800;
    /**
     * The max connection for route
     */
    private int maxRouteConn = 400;
    /**
     * Connection timeout
     */
    private int connectTimeout = 10000;
    /**
     * Read timeout
     */
    private int readTimeout = 10000;

    private String proxyHost = "intpxy6.hk.hhhh";

    private int proxyPort = 8080;

    private boolean needProxy = true;

    private int retryCount = 1;

    private String sendMethod = "post";

    private boolean enableGzip = true;

    private int buffer = 10240; // 10K by default

    public final static String HTTP = "http";

    public final static String HTTPS = "https";

    public final static String GZIP = "gzip";

    private String isMarketdataAdaptorEnable = "N";

    private String marketdataAdaptorUrl = "DUMMY";

	@Value("${enable.mstar.retry:N}")
	private String isEnabledMstarRetry;

    public void init() throws Exception {
        try {
            commonSetting();
            dataTransmitType();
            retryHandler();
            if (CommonConstants.HTTP_POST.equalsIgnoreCase(this.sendMethod)) {
                this.sendMethodClass = new HttpPostMethod();
            } else if (CommonConstants.HTTP_GET.equalsIgnoreCase(this.sendMethod)) {
                this.sendMethodClass = new HttpGetMethod();
            } else {
                LogUtil.error(HttpConnectionManager.class, "No send method defined, sendMethod: {}", this.sendMethod);
                throw new SystemException("No send method defined");
            }
        } catch (Exception e) {
            LogUtil.error(HttpConnectionManager.class, "Can't init ErrResponseAdapter|exception=" + e.getMessage(), e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        }
    }

    public String sendRequest(final String url, final String params,final String paramsWithoutEncode) throws Exception {

        HttpUriRequest httpRequest = null;
        if (params == null) {
            httpRequest = this.sendMethodClass.sendRequest(url);
        } else {
            httpRequest = this.sendMethodClass.sendRequest(url, params);
        }
         return sendRequestInternal(url,params,httpRequest,paramsWithoutEncode);
    }

    @SuppressWarnings("java:S3776")
    public String sendRequestInternal(final String url, final String params, final HttpUriRequest httpRequest, final String paramsWithoutEncode) throws Exception {
        long startTime = System.currentTimeMillis();
        String responseString = null;
        BufferedReader reader = null;

                // handle GET method only
        if (toggleService.isMarketdataAdaptorEnable() && StringUtils.isNotBlank(paramsWithoutEncode)) {
	        try  {
	        	String newUrl = setAdaptorUrl(url);

	        	newUrl = newUrl + "?" + paramsWithoutEncode;

	        	LogUtil.debug(HttpConnectionManager.class, "sending to adaptor: " + newUrl);
	        	ResponseEntity<String> restResponse = restTemplate.exchange(newUrl, HttpMethod.GET, null, String.class);
	        	if (restResponse.getStatusCode() == org.springframework.http.HttpStatus.OK) {
	        		LogUtil.debug(HttpConnectionManager.class, "adaptor return success");
	        		return restResponse.getBody();
	        	} else if (restResponse != null && restResponse.getBody() != null) {
	                LogUtil.error(HttpConnectionManager.class,
	                        "Can't retrieve data from adaptor,the statusCode:" + restResponse.getStatusCode()
	                            + ", the reason2:" + restResponse.getBody()
	                            + CommonConstants.SYMBOL_INTERROGATION + params);
	    		}
	        } catch (Exception e) {
	        	LogUtil.error(HttpConnectionManager.class, e.getMessage());
	        	if (toggleService.isMarketdataAdaptorFailRetryEnable())
	        		LogUtil.error(HttpConnectionManager.class, "adaptor error; retrying with direct Mstar call");
	        	else {
	        		LogUtil.error(HttpConnectionManager.class, "adaptor error; throw exception");
	        		throw new SystemException(e);
	        	}
	        }
        }

        try {
            HttpResponse response = this.httpClient.execute(httpRequest);
            long endTime = System.currentTimeMillis();
            LogUtil.info(HttpConnectionManager.class, "Response return used time=" + (endTime - startTime) + " ms, the url:" + url
                + CommonConstants.SYMBOL_INTERROGATION + params);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                StringBuffer buffer = new StringBuffer();
                HttpEntity en = response.getEntity();
                reader = new BufferedReader(new InputStreamReader(en.getContent(), CommonConstants.CODING_UTF8));
                char[] chars = new char[1000];
                int len;
                while ((len = reader.read(chars)) != -1) {
                    buffer.append(chars, 0, len);
                }
                responseString = buffer.toString();
            } else {
                if (null != response) {
                    StatusLine s = response.getStatusLine();
                    if (null != s) {
                        LogUtil.error(HttpConnectionManager.class,
                            "Can't retrieve data from vendor,the statusCode:" + response.getStatusLine().getStatusCode()
                                + ", the reason:" + response.getStatusLine().getReasonPhrase() + ", the url:" + url
                                + CommonConstants.SYMBOL_INTERROGATION + params);
                        throw new CommonException(ErrTypeConstants.VENDOR_NO_DATA);
                    }
                }
            }
            return responseString;

        } catch (Exception ex) {
            LogUtil.error(HttpConnectionManager.class, "the request url:" + url + CommonConstants.SYMBOL_INTERROGATION + params
                + " ,the response is :" + responseString + " , sendRequest error:" + ex.getMessage(), ex);
            if (ex.getClass().getSimpleName().toLowerCase().contains("timeout")) {
                LogUtil.error(HttpConnectionManager.class, "Timeout when retrieving data from vendor", ex);
                throw new CommonException(ErrTypeConstants.VENDOR_TIMEOUT);
            }
            LogUtil.error(HttpConnectionManager.class, "sendRequest error", ex);
            throw new SystemException(ex);
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
                httpRequest.abort();
            } catch (Exception e) {
                LogUtil.error(HttpConnectionManager.class,
                    "can not close java.io.BufferedReader, org.apache.http.client.methods.HttpUriRequest|exception="
                        + e.getMessage(),
                    e);
                throw new SystemException(e);
            }
        }
    }

    public byte[] sendRequestGetByte(final String url, final String params) throws Exception {
        HttpUriRequest httpRequest = this.sendMethodClass.sendRequest(url, params);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] responseByte = null;
        try {
            HttpResponse response = this.httpClient.execute(httpRequest);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity en = response.getEntity();
                int count = 0;
                InputStream is = en.getContent();
                byte[] buf = new byte[this.buffer];
                while ((count = is.read(buf)) != -1) {
                    baos.write(buf, 0, count);
                }
                responseByte = baos.toByteArray();
            } else {
                if (null != response) {
                    StatusLine s = response.getStatusLine();
                    if (null != s) {
                        LogUtil.error(HttpConnectionManager.class,
                            "Can't retrieve data from vendor,the statusCode:" + response.getStatusLine().getStatusCode()
                                + ", the reason:" + response.getStatusLine().getReasonPhrase() + ", the url:" + url
                                + CommonConstants.SYMBOL_INTERROGATION + params);
                        throw new CommonException(ErrTypeConstants.VENDOR_NO_DATA);
                    }
                }
            }
            return responseByte;
        } catch (Exception ex) {
            if (ex.getClass().getSimpleName().toLowerCase().contains("timeout")) {
                LogUtil.error(HttpConnectionManager.class,
                    "Timeout when retrieving data from vendor, the url:" + url + CommonConstants.SYMBOL_INTERROGATION + params, ex);
                throw new CommonException(ErrTypeConstants.VENDOR_TIMEOUT);
            }
            LogUtil.error(HttpConnectionManager.class,
                "sendRequestGetByte error, the url:" + url + CommonConstants.SYMBOL_INTERROGATION + params, ex);
            throw new SystemException(ex);
        } finally {
            try {
                httpRequest.abort();
            } catch (Exception e) {
                LogUtil.error(HttpConnectionManager.class,
                    "can not close org.apache.http.client.methods.HttpUriRequest|exception=" + e.getMessage(), e);
                throw new SystemException(e);
            }
        }
    }

    public void forwardRequest(final String url, final String params, final String cookieStr, final HttpServletResponse resp)
        throws Exception {
        HttpUriRequest httpRequest = this.sendMethodClass.sendRequest(url, params);
        ServletOutputStream outputStream = null;
        try {
            httpRequest.setHeader(HttpConnectionManager.COOKIE, cookieStr);
            HttpResponse response = this.httpClient.execute(httpRequest);
            Header[] headers = response.getAllHeaders();
            boolean noContentTypeHeader = true;
            if (null != headers && 0 != headers.length) {
                for (Header h : headers) {
                    String hn = h.getName();
                    resp.addHeader(hn, h.getValue());
                    if (HttpConnectionManager.CONTENT_TYPE.equals(hn)) {
                        noContentTypeHeader = false;
                    }
                }
            }
            if (noContentTypeHeader) {
                resp.addHeader(HttpConnectionManager.CONTENT_TYPE, HttpConnectionManager.PDF_TYPE);
            }

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity en = response.getEntity();
                int count = 0;
                InputStream is = en.getContent();
                outputStream = resp.getOutputStream();
                byte[] buf = new byte[this.buffer];
                while ((count = is.read(buf)) != -1) {
                    outputStream.write(buf, 0, count);
                }
                outputStream.close();
            } else {
                if (null != response) {
                    StatusLine s = response.getStatusLine();
                    if (null != s) {
                        LogUtil.error(HttpConnectionManager.class,
                            "Can't retrieve data from vendor,the statusCode:" + response.getStatusLine().getStatusCode()
                                + ", the reason:" + response.getStatusLine().getReasonPhrase() + ", the url:" + url
                                + CommonConstants.SYMBOL_INTERROGATION + params);
                        throw new CommonException(ErrTypeConstants.VENDOR_NO_DATA);
                    }
                }
            }
        } catch (Exception ex) {
            if (ex.getClass().getSimpleName().toLowerCase().contains("timeout")) {
                LogUtil.error(HttpConnectionManager.class,
                    "Timeout when retrieving data from vendor, the url:" + url + CommonConstants.SYMBOL_INTERROGATION + params, ex);
                throw new CommonException(ErrTypeConstants.VENDOR_TIMEOUT);
            }
            LogUtil.error(HttpConnectionManager.class,
                "forwardRequest error, the url:" + url + CommonConstants.SYMBOL_INTERROGATION + params, ex);
            throw new SystemException(ex);
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
                httpRequest.abort();
            } catch (Exception e) {
                LogUtil.error(HttpConnectionManager.class,
                    "can not close javax.servlet.ServletOutputStream, org.apache.http.client.methods.HttpUriRequest|exception="
                        + e.getMessage(),
                    e);
                throw new SystemException(e);
            }
        }
    }

    private void commonSetting() throws Exception {
        HttpParams httpParams = new BasicHttpParams();
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(HttpConnectionManager.HTTP, 80, PlainSocketFactory.getSocketFactory()));
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(final X509Certificate[] xcs, final String string) {}

            public void checkServerTrusted(final X509Certificate[] xcs, final String string) {}

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[] {tm}, null);
        SSLSocketFactory ssl = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        schemeRegistry.register(new Scheme(HttpConnectionManager.HTTPS, 443, ssl));

        ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(schemeRegistry);
        connectionManager.setMaxTotal(this.maxTotalConn);
        connectionManager.setDefaultMaxPerRoute(this.maxRouteConn);
        HttpConnectionParams.setConnectionTimeout(httpParams, this.connectTimeout);
        HttpConnectionParams.setSoTimeout(httpParams, this.readTimeout);

        if (this.needProxy) {
            HttpHost proxy = new HttpHost(this.proxyHost, this.proxyPort, HttpConnectionManager.HTTP);
            httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

        this.httpClient = new DefaultHttpClient(connectionManager, httpParams);
    }

    private void dataTransmitType() {
        if (this.enableGzip) {
            this.httpClient.addRequestInterceptor(new HttpRequestInterceptor() {

                public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
                    if (!request.containsHeader(CommonConstants.HTTP_REQUEST_HEADER_ACCEPTCODING)) {
                        request.addHeader(CommonConstants.HTTP_REQUEST_HEADER_ACCEPTCODING, HttpConnectionManager.GZIP);
                    }
                }

            });

            this.httpClient.addResponseInterceptor(new HttpResponseInterceptor() {

                public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        Header ceheader = entity.getContentEncoding();
                        if (ceheader != null) {
                            HeaderElement[] codecs = ceheader.getElements();
                            for (int i = 0; i < codecs.length; i++) {
                                if (codecs[i].getName().equalsIgnoreCase(HttpConnectionManager.GZIP)) {
                                    response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                                    return;
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private void retryHandler() {
        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(final IOException exception, final int executionCount, final HttpContext context) {
                if (executionCount >= HttpConnectionManager.this.retryCount) {
                    // stop to try if exceed max
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {
                    // stop to try if no response
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {
                    // stop to try SSL handshake exception
                    return false;
                }
                HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    return true;
                }
                return false;
            }
        };
        this.httpClient.setHttpRequestRetryHandler(myRetryHandler);
    }

    public DefaultHttpClient getHttpClient() {
        return this.httpClient;
    }

    public void setHttpClient(final DefaultHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public RequestMethod getSendMethodClass() {
        return this.sendMethodClass;
    }

    public void setSendMethodClass(final RequestMethod sendMethodClass) {
        this.sendMethodClass = sendMethodClass;
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public void setProxyHost(final String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

    public void setProxyPort(final int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public int getRetryCount() {
        return this.retryCount;
    }

    public void setRetryCount(final int retryCount) {
        this.retryCount = retryCount;
    }

    public String getSendMethod() {
        return this.sendMethod;
    }

    public void setSendMethod(final String sendMethod) {
        this.sendMethod = sendMethod;
    }

    public boolean isEnableGzip() {
        return this.enableGzip;
    }

    public void setEnableGzip(final boolean enableGzip) {
        this.enableGzip = enableGzip;
    }

    public int getMaxTotalConn() {
        return this.maxTotalConn;
    }

    public void setMaxTotalConn(final int maxTotalConn) {
        this.maxTotalConn = maxTotalConn;
    }

    public int getMaxRouteConn() {
        return this.maxRouteConn;
    }

    public void setMaxRouteConn(final int maxRouteConn) {
        this.maxRouteConn = maxRouteConn;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isNeedProxy() {
        return this.needProxy;
    }

    public void setNeedProxy(final boolean needProxy) {
        this.needProxy = needProxy;
    }

    private String setAdaptorUrl(String originalUrl) {
		String thirdPartyUri = originalUrl.substring(originalUrl.indexOf("https://") + 8);

		return marketdataAdaptorUrl + "/api/" + thirdPartyUri;
	}

    public String getIsMarketdataAdaptorEnable() {
		return isMarketdataAdaptorEnable;
	}

	public void setIsMarketdataAdaptorEnable(String isMarketdataAdaptorEnable) {
		this.isMarketdataAdaptorEnable = isMarketdataAdaptorEnable;
	}

	public String getMarketdataAdaptorUrl() {
		return marketdataAdaptorUrl;
	}

	public void setMarketdataAdaptorUrl(String marketdataAdaptorUrl) {
		this.marketdataAdaptorUrl = marketdataAdaptorUrl;
	}

}