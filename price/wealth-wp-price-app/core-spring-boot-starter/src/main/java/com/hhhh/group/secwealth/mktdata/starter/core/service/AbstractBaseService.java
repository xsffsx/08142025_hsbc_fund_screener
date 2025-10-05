/*
 */
package com.hhhh.group.secwealth.mktdata.starter.core.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import com.hhhh.group.secwealth.mktdata.starter.core.constant.Constant;
import com.hhhh.group.secwealth.mktdata.starter.validation.bean.InvalidResult;
import com.hhhh.group.secwealth.mktdata.starter.validation.service.ValidationService;

public abstract class AbstractBaseService<Q, P, H> implements Service<Q, P, H> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBaseService.class);

    @Autowired
    private ValidationService validationService;

    @Autowired
    @Qualifier("defaultInvalidResponseExCode")
    private String defaultInvalidResponseExCode;

    public final P doService(final Q request, final H header) throws Exception {
        validateRequest(request, header);
        final Object serviceRequest = convertRequest(request, header);
        final Object serviceResponse = execute(serviceRequest);
        final Object validServiceResponse = validateServiceResponse(serviceResponse);
        final P response = convertResponse(validServiceResponse);
        validateResponse(response);
        return response;
    }

    protected void validateRequest(final Q request, final H header) throws Exception {
        handleInvalidResult(this.validationService.doValidate(header), null);
        handleInvalidResult(this.validationService.doValidate(request), null);
    }

    protected abstract Object convertRequest(final Q request, final H header) throws Exception;

    protected abstract Object execute(Object serviceRequest) throws Exception;

    protected abstract Object validateServiceResponse(final Object serviceResponse) throws Exception;

    protected abstract P convertResponse(final Object validServiceResponse) throws Exception;

    protected void validateResponse(final P response) throws Exception {
        handleInvalidResult(this.validationService.doValidate(response), this.defaultInvalidResponseExCode);
    }

    private void handleInvalidResult(final List<InvalidResult> results, final String defaultExCode) throws Exception {
        if (results != null && !results.isEmpty()) {
            final InvalidResult result = results.get(0);
            final String annotationName = result.getAnnotationName();
            final String message = result.getMessage();
            AbstractBaseService.logger.error(annotationName + Constant.SYMBOL_COLON + message);
            if (!StringUtils.isEmpty(defaultExCode)) {
                throw new Exception(defaultExCode);
            }
            throw new Exception(annotationName);
        }
    }

}
