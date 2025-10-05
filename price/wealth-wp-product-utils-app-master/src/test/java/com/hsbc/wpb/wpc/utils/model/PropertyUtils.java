package com.dummy.wpb.wpc.utils.model;

import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;

public class PropertyUtils {

    public static void setProperty(Object object) throws ClassNotFoundException {
        String pathName = object.getClass().getCanonicalName();
        Class<?> aClass = Class.forName(pathName);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getModifiers() == 25) {
                continue;
            }
            field.setAccessible(true);
            String fd = field.getName();
            if (field.getType() == long.class) {
                ReflectionTestUtils.setField(object, fd, 123L);
            }else if (field.getType() == int.class) {
                ReflectionTestUtils.setField(object, fd, 123);
            }else if (field.getType() == boolean.class) {
                ReflectionTestUtils.setField(object, fd, true);
            }else {
                ReflectionTestUtils.setField(object, fd, null);
            }
        }
    }

}
