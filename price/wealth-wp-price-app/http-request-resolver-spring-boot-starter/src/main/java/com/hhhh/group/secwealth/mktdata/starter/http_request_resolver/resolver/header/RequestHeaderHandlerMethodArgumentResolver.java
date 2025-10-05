/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header;

import java.lang.reflect.Field;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestHeaderHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(final MethodParameter methodParameter, final ModelAndViewContainer modelAndViewContainer,
        final NativeWebRequest nativeWebRequest, final WebDataBinderFactory webDataBinderFactory) throws Exception {
        Class<?> clazz = methodParameter.getParameterType();
        Object header = clazz.newInstance();
        Field[] fields = header.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Property.class)) {
                Property property = field.getAnnotation(Property.class);
                String headerName = property.value();
                String[] headers = nativeWebRequest.getHeaderValues(headerName);
                if (null != headers && headers.length > 0) {
                    String headerValue = headers[0];
                    field.setAccessible(true);
                    field.set(header, headerValue);
                }
            }
        }
        return header;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestHeader.class);
    }

}
