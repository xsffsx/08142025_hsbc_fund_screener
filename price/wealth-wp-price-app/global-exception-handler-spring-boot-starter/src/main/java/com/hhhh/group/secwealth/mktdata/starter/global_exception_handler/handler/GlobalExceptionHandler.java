/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception.ExTraceCodeGenerator;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponse;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.util.ResponseUtils;

import lombok.Setter;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Setter
    private ExResponseComponent exResponseComponent;

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleThrowable(final Throwable e) {
        final String traceCode = ExTraceCodeGenerator.generate();
        final String exCode = e.getMessage();
        GlobalExceptionHandler.logger.error("TraceCode: " + traceCode + ", ExceptionCode: " + exCode, e);
        final ExResponseEntity exResponse = this.exResponseComponent.getExResponse(exCode);
        final ExResponse response = new ExResponse(exResponse, traceCode, e.getClass().getName(), exCode);
        return ResponseUtils.failure(HttpStatus.valueOf(Integer.parseInt(exResponse.getStatus())), response);
    }

}
