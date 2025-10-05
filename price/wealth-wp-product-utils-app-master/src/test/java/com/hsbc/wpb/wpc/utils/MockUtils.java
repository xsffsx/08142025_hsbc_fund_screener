package com.dummy.wpb.wpc.utils;

import com.dummy.wpb.wpc.utils.controller.UtilsRestController;
import com.mongodb.client.MongoDatabase;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


public class MockUtils {

    public static <T, M> void mockPrivate(T t, String fieldName, M mock) throws NoSuchFieldException, IllegalAccessException {
        Field field = t.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(t, mock);
    }

    public static <T> void mock(String className, T t, LinkedHashMap<Class<?>,Object> paramList, String fieldName, Object field) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Class<?> mockServiceClass = Class.forName(className);
        /*Class<?>[] c = new Class[];
        for (int i =0 ; i < paramList.size(); i++) {
            c[i] = paramList.get(i).getClass();
        }

        T utilsRestController = (T) mockServiceClass.getDeclaredConstructor(c).newInstance(paramList.toArray());*/

        List<Class<?>> a = new LinkedList();
        List<Object> b = new LinkedList();
        paramList.forEach(( k,v) -> { a.add(k); b.add(v);});

        T utilsRestController = (T) mockServiceClass.getDeclaredConstructor(a.toArray(new Class[a.size()])).newInstance(b.toArray());

        Field adminLogServiceField = mockServiceClass.getDeclaredField(fieldName);
        adminLogServiceField.setAccessible(true);
        adminLogServiceField.set(utilsRestController, field);
    }

}
