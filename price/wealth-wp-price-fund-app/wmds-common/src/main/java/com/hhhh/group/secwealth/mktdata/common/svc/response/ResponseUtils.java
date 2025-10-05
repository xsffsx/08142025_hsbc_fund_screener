package com.hhhh.group.secwealth.mktdata.common.svc.response;

import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hhhh.group.secwealth.mktdata.common.exception.ErrResponse;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ResponseCode;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;

public class ResponseUtils<T> {
    /*
     * new instance
     */
    public static <T> ResponseBuilder<T> newResponse() {
        return new ResponseBuilder<T>();
    }

    /*
     * success
     */
    public static <T> ResponseEntity<?> success() {
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.OK).build();
    }

    public static <T> ResponseEntity<?> successWithValue(final Response response) {
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.OK).response(response).build();
    }

    public static <T> ResponseEntity<?> successWithEmptyValue() {
        LogUtil.debug(ResponseUtils.class, "successWithEmptyValue, Response Result: {}");
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.OK).response(new Response()).build();
    }

    public static <T> ResponseEntity<?> successWithValues(final List<Response> responses) {
        try {
            LogUtil.infoBeanToJson(ResponseUtils.class, "successWithValues, Response Result", responses);
        } catch (Exception e) {
        }
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.OK).responses(responses).build();
    }

    public static <T> ResponseEntity<?> successWithObject(final Object object) {
        try {
            LogUtil.infoBeanToJson(ResponseUtils.class, "successWithObject, Response Result", object);
        } catch (Exception e) {
        }
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.OK).buildObject(object);
    }

    @SuppressWarnings("unchecked")
    public static <T> ResponseEntity<?> successWithEmptyValues() {
        LogUtil.debug(ResponseUtils.class, "successWithEmptyValues, Response Result: []");
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.OK).responses(ListUtils.EMPTY_LIST).build();
    }

    /*
     * fail
     */
    public static <T> ResponseEntity<?> fail() {
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.BAD_REQUEST).build();
    }

    public static <T> ResponseEntity<?> failWithText(final String reasonCode, final String traceCode, final String text) {
        Response response = new Response(ResponseCode.ERROR, reasonCode, traceCode, text);
        try {
            LogUtil.infoBeanToJson(ResponseUtils.class, "failWithText, Response Result", response);
        } catch (Exception e) {
        }
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.BAD_REQUEST).response(response).build();
    }

    public static <T> ResponseEntity<?> failWithText(final ErrResponse errResponse, final String traceCode) {
        Response response = new Response(errResponse, traceCode);
        try {
            LogUtil.infoBeanToJson(ResponseUtils.class, "failWithText, Response Result", response);
        } catch (Exception e) {
        }
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.BAD_REQUEST).response(response).build();
    }

    public static <T> ResponseEntity<?> failWithTexts(final String reasonCode, final String traceCode,
        final List<ValidatorError> texts) {
        Response response = new Response(ResponseCode.ERROR, reasonCode, traceCode, texts);
        try {
            LogUtil.infoBeanToJson(ResponseUtils.class, "failWithTexts, Response Result", response);
        } catch (Exception e) {
        }
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.BAD_REQUEST).response(response).build();
    }

    public static <T> ResponseEntity<?> failWithTexts(final ErrResponse errResponse, final String traceCode,
        final List<ValidatorError> texts) {
        Response response = new Response(errResponse, traceCode, texts);
        try {
            LogUtil.infoBeanToJson(ResponseUtils.class, "failWithTexts, Response Result", response);
        } catch (Exception e) {
        }
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.BAD_REQUEST).response(response).build();
    }

    public static <T> ResponseEntity<?> failWithValues(final List<Response> responses) {
        try {
            LogUtil.infoBeanToJson(ResponseUtils.class, "failWithValues, Response Result", responses);
        } catch (Exception e) {
        }
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.BAD_REQUEST).responses(responses).build();
    }

}
