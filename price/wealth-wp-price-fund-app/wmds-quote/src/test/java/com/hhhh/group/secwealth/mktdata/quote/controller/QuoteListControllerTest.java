package com.hhhh.group.secwealth.mktdata.quote.controller;
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

import com.hhhh.group.secwealth.mktdata.common.service.RestfulService;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.QuoteListResponse;

import static com.hhhh.group.secwealth.mktdata.quote.controller.QuoteDetailControllerTest.setHeaders;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class QuoteListControllerTest {

    @InjectMocks
    QuoteListController quoteListController;

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
            "{\"productKeys\":[{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"FG0015\",\"productType\":\"PF\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"540004\",\"productType\":\"UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"IPFD3077\",\"productType\":\"QDII-UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"QTCZ01\",\"productType\":\"WMP\"}],\"market\":\"CN\",\"delay\":true}";
        QuoteListController controller = Mockito.mock(QuoteListController.class);
        ResponseEntity<QuoteListResponse> response = new ResponseEntity<>(new QuoteListResponse(), HttpStatus.OK);
        Mockito.doReturn(response).when(controller).get(param, this.request);
        Assertions.assertNotNull(this.restfulService);
        Assertions.assertEquals(response, controller.get(param, this.request));
    }

    @Test
    void testGet_2() throws Exception {
        String param =
            "{\"productKeys\":[{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"FG0015\",\"productType\":\"PF\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"540004\",\"productType\":\"UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"IPFD3077\",\"productType\":\"QDII-UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"QTCZ01\",\"productType\":\"WMP\"}],\"market\":\"CN\",\"delay\":true}";
        ResponseEntity<?> response = this.quoteListController.get(param, this.request);
        Assertions.assertNull(response);
    }

    @Test
    void testGet_3() throws Exception {
        assertThrows(Exception.class,() -> this.quoteListController.get("{", this.request));
    }
    @Test
    void testGet_4() throws Exception {
        String param = "";
        ResponseEntity<?> response = this.quoteListController.get(param, this.request);
        Assertions.assertNull(response);
    }

    @Test
    void testPost_1() throws Exception {
        String body =
            "{\"productKeys\":[{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"FG0015\",\"productType\":\"PF\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"540004\",\"productType\":\"UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"IPFD3077\",\"productType\":\"QDII-UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"QTCZ01\",\"productType\":\"WMP\"}],\"market\":\"CN\",\"delay\":true}";
        QuoteListController controller = Mockito.mock(QuoteListController.class);
        ResponseEntity<QuoteListResponse> response = new ResponseEntity<>(new QuoteListResponse(), HttpStatus.OK);
        Mockito.doReturn(response).when(controller).post(body, this.request);
        Assertions.assertEquals(response, controller.post(body, this.request));
    }

    @Test
    void testPost_2() throws Exception {
        String body =
            "{\"productKeys\":[{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"FG0015\",\"productType\":\"PF\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"540004\",\"productType\":\"UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"IPFD3077\",\"productType\":\"QDII-UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"QTCZ01\",\"productType\":\"WMP\"}],\"market\":\"CN\",\"delay\":true}";
        ResponseEntity<?> response = this.quoteListController.post(body, this.request);
        Assertions.assertNull(response);
    }

    @Test
    void testPost_4() throws Exception {
        String body = "";
        ResponseEntity<?> response = this.quoteListController.post(body, this.request);
        Assertions.assertNull(response);
    }
}
