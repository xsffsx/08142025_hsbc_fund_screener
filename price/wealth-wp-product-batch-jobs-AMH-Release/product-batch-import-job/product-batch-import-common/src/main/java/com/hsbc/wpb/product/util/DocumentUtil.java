package com.dummy.wpb.product.util;

import com.dummy.wpb.product.annotation.DocumentField;
import com.dummy.wpb.product.annotation.DocumentObject;
import com.dummy.wpb.product.utils.JsonPathUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
@SuppressWarnings("squid:S3011")
public class DocumentUtil {

    public static Document parseObject(Object object) {
        Document document = new Document();
        setDocValue(document, object);
        return document;
    }

    @SneakyThrows
    private static void setDocValue(Document doc, Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String parentPath = getParent(clazz);
        for (Field field : fields) {

            field.setAccessible(true);
            Object fieldValue = field.get(object);

            if (ObjectUtils.isEmpty(fieldValue) || isDocObj(field.getType())) {
                if (ObjectUtils.isNotEmpty(fieldValue)) {
                    setDocValue(doc, fieldValue);
                }
                continue;
            }

            if (isDocObjListField(field)) {
                List<Object> itemList = (List<Object>) fieldValue;
                List<Object> value = new ArrayList<>();
                String listPath = getParent(getListItemType(field));

                for (Object item : itemList) {
                    Document tempDoc = new Document();
                    setDocValue(tempDoc, item);
                    value.add(JsonPathUtils.readValue(tempDoc, listPath));
                }
                JsonPathUtils.setValue(doc, listPath, value);
            } else if (StringUtils.isNotBlank(parentPath)) {
                JsonPathUtils.setValue(doc, String.format("%s.%s", parentPath, getName(field)), fieldValue);
            }
        }
    }


    private static String getParent(Class<?> clazz) {
        DocumentObject annotation = clazz.getAnnotation(DocumentObject.class);
        if (Objects.isNull(annotation)) {
            return StringUtils.EMPTY;
        }
        return annotation.value();
    }

    private static String getName(Field field) {
        DocumentField annotation = field.getAnnotation(DocumentField.class);
        if (Objects.isNull(annotation)) {
            return field.getName();
        }
        return annotation.value();
    }

    private static boolean isDocObj(Class<?> clazz) {
        return Objects.nonNull(clazz.getAnnotation(DocumentObject.class));
    }

    private static boolean isDocObjListField(Field field) {
        return field.getType().equals(List.class) && isDocObj(getListItemType(field));
    }

    private static Class<?> getListItemType(Field field) {
        Type genericType = field.getGenericType();
        ParameterizedType pt = (ParameterizedType) genericType;
        return (Class<?>) pt.getActualTypeArguments()[0];
    }
}
