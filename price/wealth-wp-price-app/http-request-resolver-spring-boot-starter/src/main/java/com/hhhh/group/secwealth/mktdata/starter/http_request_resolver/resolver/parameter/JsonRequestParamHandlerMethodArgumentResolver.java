/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.parameter;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;

public class JsonRequestParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String SEPARATOR = "\\|";

    @Setter
    private ObjectMapper objectMapper;

    @Setter
    private String defaultExCode;

    @Override
    public Object resolveArgument(final MethodParameter methodParameter, final ModelAndViewContainer modelAndViewContainer,
        final NativeWebRequest nativeWebRequest, final WebDataBinderFactory webDataBinderFactory) throws Exception {
        JsonRequestParam jsonRequestParam = methodParameter.getParameterAnnotation(JsonRequestParam.class);
        String value = jsonRequestParam.value();
        String[] valueArray = value.split(JsonRequestParamHandlerMethodArgumentResolver.SEPARATOR);
        Class<?> clazz = methodParameter.getParameterType();
        String param = null;
        for (String val : valueArray) {
            param = nativeWebRequest.getParameter(val);
            if (null != param && !"".equals(param)) {
                break;
            }
        }
        try {
            return this.objectMapper.readValue(param, clazz);
        } catch (Exception e) {
            throw new Exception(this.defaultExCode, e);
        }
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonRequestParam.class);
    }

}
