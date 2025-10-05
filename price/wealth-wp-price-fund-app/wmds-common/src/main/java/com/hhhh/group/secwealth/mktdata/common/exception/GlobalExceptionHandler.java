package com.hhhh.group.secwealth.mktdata.common.exception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hhhh.group.secwealth.mktdata.common.dao.impl.CustomerContextHolder;
import com.hhhh.group.secwealth.mktdata.common.svc.response.Response;
import com.hhhh.group.secwealth.mktdata.common.svc.response.ResponseUtils;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    @Qualifier("errResponseAdapter")
    private ErrResponseAdapter errResponseAdapter;

    @ExceptionHandler(ValidatorException.class)
    public ResponseEntity<?> handleValidatorException(final ValidatorException ex, final HttpServletRequest request) {
        ResponseEntity<?> responseEntity = null;
        String traceCode = ex.getTraceCode();
        String errKey = ex.getExceptionCode();
        LogUtil.error(
            GlobalExceptionHandler.class,
            "ValidatorException TraceCode: " + traceCode + " ExceptionCode: " + ex.getExceptionCode() + " Message: "
                + ex.getMessage(), ex);
        ErrResponse errResponse = this.getErrResponse(errKey);
        if (ErrTypeConstants.INPUT_PARAMETER_INVALID.equals(errKey)) {
            StringBuffer bf = new StringBuffer();
            List<ValidatorError> errors = ex.getErrMessages();
            for (ValidatorError err : errors) {
                ErrResponse errRps = this.getErrResponse(err.getReasonCode());
                bf.append(CommonConstants.SEPARATOR).append(errRps.getReasonCode());
                LogUtil.error(GlobalExceptionHandler.class, "ValidatorException ExceptionCode: " + errKey + " ,TraceCode: "
                    + traceCode + " ,Messgae: " + err.toString());
            }
            responseEntity = ResponseUtils.failWithText(errResponse.getReasonCode(), traceCode,
                StringUtils.substring(bf.toString(), 1));
        } else {
            responseEntity = ResponseUtils.failWithText(errResponse.getReasonCode(), traceCode, errResponse.getText());
        }
        // Remove HeaderMap from ThreadLocal
        CustomerContextHolder.remove();
        return responseEntity;
    }

    @ExceptionHandler(VendorException.class)
    public ResponseEntity<?> handleVendorException(final VendorException ex, final HttpServletRequest request) {
        String traceCode = ex.getTraceCode();
        LogUtil.error(GlobalExceptionHandler.class, "VendorException TraceCode: " + traceCode + " Message: " + ex.getMessage(), ex);
        // Remove HeaderMap from ThreadLocal
        CustomerContextHolder.remove();
        return ResponseUtils.failWithText(ex.getErrResponse(), traceCode);
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<?> handleCommonException(final CommonException ex, final HttpServletRequest request) {
        String traceCode = ex.getTraceCode();
        LogUtil.error(GlobalExceptionHandler.class,
            "CommonException TraceCode: " + traceCode + " Message: " + ex.getExceptionCode() + " Message: " + ex.getMessage(), ex);
        ErrResponse errResponse = this.getErrResponse(ex.getExceptionCode());
        // Remove HeaderMap from ThreadLocal
        CustomerContextHolder.remove();
        return ResponseUtils.failWithText(errResponse, traceCode);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(final ApplicationException ex, final HttpServletRequest request) {
        String traceCode = ex.getTraceCode();
        LogUtil.error(GlobalExceptionHandler.class,
            "ApplicationException TraceCode: " + traceCode + " Message: " + ex.getMessage(), ex);
        Collection<CommonException> errors = ex.getErrors();
        List<Response> responses = new ArrayList<Response>();
        if (errors != null && errors.size() > 0) {
            for (CommonException e : errors) {
                responses.add(new Response(this.getErrResponse(e.getExceptionCode()), traceCode));
            }
        }
        // Remove HeaderMap from ThreadLocal
        CustomerContextHolder.remove();
        return ResponseUtils.failWithValues(responses);
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<?> handleSystemException(final SystemException ex, final HttpServletRequest request) {
        String traceCode = ex.getTraceCode();
        LogUtil
            .error(
                GlobalExceptionHandler.class,
                "SystemException TraceCode: " + traceCode + " ExceptionCode: " + ex.getExceptionCode() + " Message: "
                    + ex.getMessage(), ex);
        ErrResponse errResponse = this.getErrResponse(ex.getExceptionCode());
        // Remove HeaderMap from ThreadLocal
        CustomerContextHolder.remove();
        return ResponseUtils.failWithText(errResponse, traceCode);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleThrowable(final Throwable ex, final HttpServletRequest request) {
        String traceCode = BaseException.getErrTraceCode();
        LogUtil.error(GlobalExceptionHandler.class, "Throwable TraceCode: " + traceCode + ", Message: " + ex.getMessage(), ex);
        ErrResponse errResponse = this.getErrResponse(ErrTypeConstants.UNDEFINED);
        // Remove HeaderMap from ThreadLocal
        CustomerContextHolder.remove();
        return ResponseUtils.failWithText(errResponse, traceCode);
    }

    private ErrResponse getErrResponse(final String errKey) {
        return this.errResponseAdapter.getErrResponseObject(errKey);
    }
}
