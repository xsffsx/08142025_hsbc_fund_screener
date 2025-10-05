/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;

@RunWith(MockitoJUnitRunner.class)
public class CacheDistributeServiceTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CacheDistributeService service = new CacheDistributeService();

    @Test
    public void whenRetrieveCacheRecordInvoked_withInvalidMockedHttpServletRequest_shouldThrowIllegalArgumentException() {
        List<String[]> items = new ArrayList<>(4);
        // X-hhhh-User-Id and X-hhhh-Session-Correlation-Id are mandatory
        items.add(new String[] {"", "", "", ""});
        items.add(new String[] {"userId", "", "", ""});
        items.add(new String[] {"", "correlationId", "", ""});
        // X-hhhh-Saml and X-hhhh-Saml3 at least one exists
        items.add(new String[] {"userId", "correlationId", "", ""});

        items.forEach(item -> {
            mockHttpServletRequest(item);
            try {
                this.service.retrieveCacheRecord(this.request);
                fail("No exception thrown.");
            } catch (Exception e) {
                assertTrue(IllegalArgumentException.class == e.getClass());
                assertEquals(
                    "X-hhhh-User-Id and X-hhhh-Session-Correlation-Id are mandatory. X-hhhh-Saml and X-hhhh-Saml3 and x-hhhh-e2e-trust-token at least one exists.",
                    e.getMessage());
            }
        });
    }

    @Test
    public void whenRetrieveCacheRecordInvoked_withValidMockedHttpServletRequest_shouldReturnMockedResponse()
        throws InterruptedException, ExecutionException {
        // Mock HttpServletRequest
        mockHttpServletRequest(new String[] {"userId", "correlationId", "", "saml3"});
        // Mock response
        CacheDistributeResponse response = new CacheDistributeResponse();
        response.setCacheExistingFlag("Y");
        // Prepare url
        String url =
            "https://cache-distributed-domain/cache/df14a046d5a1192e5c3158a78ebd02763ad98db4a7b368d47d1c193b72d74675~~~correlationId~~~marketData";
        // Prepare HttpEntity
        HttpHeaders headers = new HttpHeaders();
        headers.add(CacheDistributeEnum.X_hhhh_SAML3.getValue(), "saml3");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        Mockito.when(this.restTemplate.exchange(url, HttpMethod.GET, entity, CacheDistributeResponse.class))
            .thenReturn(new ResponseEntity<CacheDistributeResponse>(response, HttpStatus.OK));

        this.service.setAwsUri("https://cache-distributed-domain/cache/");
        Future<CacheDistributeResponse> future = this.service.retrieveCacheRecord(this.request);
        assertEquals(response, future.get());
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
