package com.hhhh.group.secwealth.mktdata.common.svc.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;


class ResponseBuilderTest {

    Response initResponse(){
        Response response = new Response();
        response.setResponseCode("000");
        response.setReasonCode("0");
        response.setText("Success");
        return response;
    }

    @Test
    void responses() {
        Response response = initResponse();
        ResponseBuilder responseBuilder = new ResponseBuilder();
        responseBuilder.response(response);
        Assertions.assertNotNull(response);
    }

    @Test
    void build() {
        ResponseBuilder responseBuilder = new ResponseBuilder();
        responseBuilder.httpStatus(HttpStatus.GATEWAY_TIMEOUT);
        responseBuilder.build();
        Response response = initResponse();
        responseBuilder.response(response);
        responseBuilder.httpStatus(HttpStatus.GATEWAY_TIMEOUT);
        responseBuilder.build();

        Assertions.assertNotNull(response);

    }
    @Test
    void build1() {
        ResponseBuilder responseBuilder = new ResponseBuilder();
        responseBuilder.httpStatus(HttpStatus.GATEWAY_TIMEOUT);
        Response response = initResponse();
        List<Response> responses = new ArrayList<>();
        responses.add(response);
        responseBuilder.responses(responses);
        responseBuilder.build();
        Assertions.assertNotNull(responses);
    }


}