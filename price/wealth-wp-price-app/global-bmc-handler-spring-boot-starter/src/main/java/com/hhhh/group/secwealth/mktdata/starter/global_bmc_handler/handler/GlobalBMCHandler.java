/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.constant.Constant;

import lombok.Setter;

@ControllerAdvice
public class GlobalBMCHandler implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(GlobalBMCHandler.class);

    @Setter
    private String interceptResponse;

    @Setter
    private String interceptKey;

    @Setter
    private BMCComponent component;

    @Override
    public Object beforeBodyWrite(final Object body, final MethodParameter returnType, final MediaType selectedContentType,
        final Class<? extends HttpMessageConverter<?>> selectedConverterType, final ServerHttpRequest request,
        final ServerHttpResponse response) {
        if (body != null) {
            if (!StringUtils.isEmpty(this.interceptResponse)) {
                if (Arrays.asList(this.interceptResponse.split(Constant.SYMBOL_COMMA)).contains(body.getClass().getName())) {
                    if (!StringUtils.isEmpty(this.interceptKey)) {
                        final String[] interceptKeys = this.interceptKey.split(Constant.SYMBOL_COMMA);
                        final String exClassName = getField(body, interceptKeys[0]);
                        final String exCode = getField(body, interceptKeys[1]);
                        final String traceCode = getField(body, interceptKeys[2]);
                        try {
                            final Class<?> clazz = Class.forName(exClassName);
                            final Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
                            constructor.setAccessible(true);
                            final Throwable e = (Throwable) constructor.newInstance(exCode);
                            this.component.doBMC(e, traceCode);
                        } catch (Exception e) {
                            GlobalBMCHandler.logger.error("Write BMC encounter error", e);
                        }
                    } else {
                        GlobalBMCHandler.logger
                            .error("Please check your configuration, \"global.bmc.handler.intercept-key\" should not be empty");
                    }
                }
            } else {
                GlobalBMCHandler.logger
                    .error("Please check your configuration, \"global.bmc.handler.intercept-response\" should not be empty");
            }
        }
        return body;
    }

    private String getField(final Object body, final String key) {
        String result = "";
        try {
            final Field field = body.getClass().getDeclaredField(key);
            field.setAccessible(true);
            result = String.valueOf(field.get(body));
        } catch (Exception e) {
            GlobalBMCHandler.logger.error("Get " + key + " encounter error", e);
        }
        return result;
    }

    @Override
    public boolean supports(final MethodParameter returnType, final Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

}
