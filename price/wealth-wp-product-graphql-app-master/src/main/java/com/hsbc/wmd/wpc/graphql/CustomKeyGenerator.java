package com.dummy.wmd.wpc.graphql;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class CustomKeyGenerator implements KeyGenerator {

    public Object generate(Object target, Method method, Object... params) {
        return String.format("%s.%s(%s)", target.getClass().getSimpleName(), method.getName(), StringUtils.arrayToDelimitedString(params, ","));
    }
}
