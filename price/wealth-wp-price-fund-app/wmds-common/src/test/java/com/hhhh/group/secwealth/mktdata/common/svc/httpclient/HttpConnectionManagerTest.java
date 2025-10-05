package com.hhhh.group.secwealth.mktdata.common.svc.httpclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.service.ToggleService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class HttpConnectionManagerTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ToggleService toggleService;

    @Mock
    private DefaultHttpClient httpClient;

    @Mock
    private HttpUriRequest httpRequest;

    @Mock
    private CloseableHttpResponse httpResponse;

    @Mock
    private StatusLine statusLine;

    @Mock
    private HttpEntity httpEntity;

    @Mock
    private InputStream inputStream;

    @Mock
    private RequestMethod sendMethodClass;

    @InjectMocks
    private HttpConnectionManager httpConnectionManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void sendRequestInternal_returnsResponseFromAdaptor_whenAdaptorEnabled() throws Exception {
        when(toggleService.isMarketdataAdaptorEnable()).thenReturn(true);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(String.class)))
                .thenReturn(new ResponseEntity<>("adaptor response", HttpStatus.OK));

        String result = httpConnectionManager.sendRequestInternal("http://example.com", "params", httpRequest, "paramsWithoutEncode");

        assertEquals("adaptor response", result);
    }

    @Test
    public void sendRequestInternal_logsErrorAndRetries_whenAdaptorFailsAndRetryEnabled() throws Exception {
        when(toggleService.isMarketdataAdaptorEnable()).thenReturn(true);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(String.class)))
                .thenThrow(new RuntimeException("adaptor error"));
        when(toggleService.isMarketdataAdaptorFailRetryEnable()).thenReturn(true);
        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(inputStream);
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenAnswer(invocation -> {
            byte[] buffer = invocation.getArgument(0);
            buffer[0] = 'r';
            buffer[1] = 'e';
            buffer[2] = 's';
            buffer[3] = 'p';
            buffer[4] = 'o';
            buffer[5] = 'n';
            buffer[6] = 's';
            buffer[7] = 'e';
            return 8;
        }).thenReturn(-1);

        String result = httpConnectionManager.sendRequestInternal("http://example.com", "params", httpRequest, "paramsWithoutEncode");

        assertEquals("response", result);
        verify(toggleService).isMarketdataAdaptorFailRetryEnable();
    }


    @Test
    public void sendRequestInternal_logsErrorAndThrowsException_whenAdaptorFailsAndRetryDisabled() throws Exception {
        when(toggleService.isMarketdataAdaptorEnable()).thenReturn(true);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(String.class)))
        	.thenThrow(new RuntimeException("adaptor error"));
        when(toggleService.isMarketdataAdaptorFailRetryEnable()).thenReturn(false);

        assertThrows(SystemException.class, () -> {
            httpConnectionManager.sendRequestInternal("http://example.com", "params", httpRequest, "paramsWithoutEncode");
        });
        verify(toggleService).isMarketdataAdaptorFailRetryEnable();
    }

    @Test
    public void sendRequestInternal_logsErrorAndThrowsException_whenAdaptorFailsAndRetryDisabled_HttpError() throws Exception {
        when(toggleService.isMarketdataAdaptorEnable()).thenReturn(true);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(String.class)))
        	.thenReturn(new ResponseEntity<>("adaptor error", HttpStatus.BAD_REQUEST));
        when(toggleService.isMarketdataAdaptorFailRetryEnable()).thenReturn(false);

        assertThrows(SystemException.class, () -> {
            httpConnectionManager.sendRequestInternal("http://example.com", "params", httpRequest, "paramsWithoutEncode");
        });
    }

    @Test
    public void sendRequest_returnsResponseFromHttpClient_whenParamsAreNull() throws Exception {
        when(sendMethodClass.sendRequest(anyString())).thenReturn(httpRequest);
        when(httpClient.execute(httpRequest)).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        InputStream inputStream = new ByteArrayInputStream("response".getBytes());
        when(httpEntity.getContent()).thenReturn(inputStream);

        String result = httpConnectionManager.sendRequest("http://example.com", null, null);

        assertEquals("response", result);
    }

    @Test
    public void sendRequest_returnsResponseFromHttpClient_whenParamsAreNotNull() throws Exception {
        when(sendMethodClass.sendRequest(anyString(), anyString())).thenReturn(httpRequest);
        when(httpClient.execute(httpRequest)).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        InputStream inputStream = new ByteArrayInputStream("response".getBytes());
        when(httpEntity.getContent()).thenReturn(inputStream);

        String result = httpConnectionManager.sendRequest("http://example.com", "params", null);

        assertEquals("response", result);
    }

    @Test
    public void sendRequest_throwsException_whenHttpClientFails() throws Exception {
        when(sendMethodClass.sendRequest(anyString(), anyString())).thenReturn(httpRequest);
        when(httpClient.execute(httpRequest)).thenThrow(new IOException("http client error"));

        assertThrows(SystemException.class, () -> {
            httpConnectionManager.sendRequest("http://example.com", "params", null);
        });
    }
}