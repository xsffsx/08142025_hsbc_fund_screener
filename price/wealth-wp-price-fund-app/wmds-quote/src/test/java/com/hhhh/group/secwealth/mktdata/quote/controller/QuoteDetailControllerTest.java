package com.hhhh.group.secwealth.mktdata.quote.controller;

import com.hhhh.group.secwealth.mktdata.common.service.RestfulService;
import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.QuoteDetailResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class QuoteDetailControllerTest {

    @InjectMocks
    QuoteDetailController quoteDetailController;

    @Mock
    MockHttpServletRequest request;

    @Mock
    RestfulService restfulService;

    @BeforeEach
    void setup() {
        this.request = new MockHttpServletRequest();
        setHeaders(this.request);
    }

    @Test
    void testGet_1() throws Exception {
        String param =
                "{\"market\":\"CN\",\"productType\":\"UT\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"540002\",\"delay\":\"true\",\"entityTimezone\":\"Asia/Hong_Kong\"}";
        QuoteDetailController controller = Mockito.mock(QuoteDetailController.class);
        ResponseEntity<QuoteDetailResponse> response = new ResponseEntity<>(new QuoteDetailResponse(), HttpStatus.OK);
        Mockito.doReturn(response).when(controller).get(param, this.request);
        Assertions.assertNotNull(restfulService);
        Assertions.assertEquals(response, controller.get(param, this.request));
    }

    @Test
    void testGet_2() throws Exception {
        String param =
                "{\"market\":\"CN\",\"productType\":\"UT\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"540002\",\"delay\":\"true\",\"entityTimezone\":\"Asia/Hong_Kong\"}";
        ResponseEntity<?> response = this.quoteDetailController.get(param, this.request);
        Assertions.assertNull(response);
    }

    @Test
    void testGet_4() throws Exception {
        String param = "";
        ResponseEntity<?> response = this.quoteDetailController.get(param, this.request);
        Assertions.assertNull(response);
    }

    @Test
    void testGet_3() {
        assertThrows(Exception.class,() -> this.quoteDetailController.get("{", this.request));
    }

    public static void setHeaders(final MockHttpServletRequest request) {
        request.addHeader(CommonConstants.REQUEST_HEADER_LOCALE, "zh_CN");
        request.addHeader(CommonConstants.REQUEST_HEADER_COUNTRYCODE, "CN");
        request.addHeader(CommonConstants.REQUEST_HEADER_CHANNELID, "OHI");
        request.addHeader(CommonConstants.REQUEST_HEADER_GROUPMEMBER, "hhhh");
        request.addHeader(CommonConstants.REQUEST_HEADER_APPCODE, "Wmds");
    }

    public static void setHeaders(final Request request) {
        request.addHeader(CommonConstants.REQUEST_HEADER_LOCALE, "zh_CN");
        request.addHeader(CommonConstants.REQUEST_HEADER_COUNTRYCODE, "CN");
        request.addHeader(CommonConstants.REQUEST_HEADER_CHANNELID, "OHI");
        request.addHeader(CommonConstants.REQUEST_HEADER_GROUPMEMBER, "hhhh");
        request.addHeader(CommonConstants.REQUEST_HEADER_APPCODE, "Wmds");
    }
}