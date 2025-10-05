package com.hhhh.group.secwealth.mktdata.fund;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class TestCaseCommonUtil {
     public static Method findMethodWithAccessibility(Object target, String name, Class<?>... paramTypes) {
        Method method = ReflectionUtils.findMethod(target.getClass(), name, paramTypes);
        ReflectionUtils.makeAccessible(method);
        return method;
    }
}
