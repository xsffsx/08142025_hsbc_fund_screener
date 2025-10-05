/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.configuration.CacheDistributeSpringTestConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CacheDistributeSpringTestConfiguration.class)
public class CacheDistributeServiceMockRestServiceServerTest {

    @Mock
    private HttpServletRequest request;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CacheDistributeService service;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void before() {
        this.mockServer = MockRestServiceServer.createServer(this.restTemplate);
        mockHttpServletRequest(new String[] {"userId", "correlationId", "", "saml3"});
    }

    @Test
    public void whenRetrieveCacheRecordInvoked_withValidMockedHttpServletRequest_shouldReturnMockedResponse()
        throws InterruptedException, ExecutionException {
        CacheDistributeResponse response = new CacheDistributeResponse();
        response.setCacheExistingFlag("Y");
        String body = "";
        try {
            body = this.mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            fail("Unexcepted Json format exception.");
        }

        this.mockServer
            .expect(ExpectedCount.once(),
                requestTo(URI.create(
                    "https://cache-distributed-domain/cache/df14a046d5a1192e5c3158a78ebd02763ad98db4a7b368d47d1c193b72d74675~~~correlationId~~~marketData")))
            .andExpect(header(HttpHeaders.ACCEPT, MediaType.toString(Arrays.asList(MediaType.APPLICATION_JSON))))
            .andExpect(header(CacheDistributeEnum.X_hhhh_SAML3.getValue(), "saml3")).andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(body));

        CompletableFuture<CacheDistributeResponse> future = this.service.retrieveCacheRecord(this.request);
        this.mockServer.verify();
        CacheDistributeResponse excepted = future.get();
        assertEquals(excepted, response);
    }

    @Test(expected = HttpClientErrorException.class)
    public void whenRetrieveCacheRecordInvoked_withValidMockedHttpServletRequest_butResourceNotFound_shouldThrowHttpClientError() {
        this.mockServer
            .expect(ExpectedCount.once(),
                requestTo(URI.create(
                    "https://cache-distributed-domain/cache/df14a046d5a1192e5c3158a78ebd02763ad98db4a7b368d47d1c193b72d74675~~~correlationId~~~marketData")))
            .andExpect(header(HttpHeaders.ACCEPT, MediaType.toString(Arrays.asList(MediaType.APPLICATION_JSON))))
            .andExpect(header(CacheDistributeEnum.X_hhhh_SAML3.getValue(), "saml3")).andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));
        this.service.retrieveCacheRecord(this.request);
    }

    @Test(expected = HttpServerErrorException.class)
    public void whenRetrieveCacheRecordInvoked_withValidMockedHttpServletRequest_butServiceUnavailable_shouldThrowHttpServerError() {
        this.mockServer
            .expect(ExpectedCount.once(),
                requestTo(URI.create(
                    "https://cache-distributed-domain/cache/df14a046d5a1192e5c3158a78ebd02763ad98db4a7b368d47d1c193b72d74675~~~correlationId~~~marketData")))
            .andExpect(header(HttpHeaders.ACCEPT, MediaType.toString(Arrays.asList(MediaType.APPLICATION_JSON))))
            .andExpect(header(CacheDistributeEnum.X_hhhh_SAML3.getValue(), "saml3")).andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));
        this.service.retrieveCacheRecord(this.request);
    }

    /**
     * <p>
     * <b> Default order is [X-hhhh-User-Id, X-hhhh-Session-Correlation-Id,
     * X-hhhh-Saml, X-hhhh-Saml3]. </b>
     * </p>
     *
     * @param args
     */
    private void mockHttpServletRequest(final String[] args) {
        assert args.length == 4;
        Mockito.when(this.request.getHeader(CacheDistributeEnum.X_hhhh_USER_ID.getValue())).thenReturn(args[0]);
        Mockito.when(this.request.getHeader(CacheDistributeEnum.X_hhhh_SESSION_CORRELATION_ID.getValue())).thenReturn(args[1]);
        Mockito.when(this.request.getHeader(CacheDistributeEnum.X_hhhh_SAML.getValue())).thenReturn(args[2]);
        Mockito.when(this.request.getHeader(CacheDistributeEnum.X_hhhh_SAML3.getValue())).thenReturn(args[3]);
    }

}
