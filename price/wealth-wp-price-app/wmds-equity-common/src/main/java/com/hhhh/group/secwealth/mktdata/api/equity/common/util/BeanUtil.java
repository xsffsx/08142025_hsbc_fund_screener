/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;

@Deprecated
public final class BeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    private static final String METHOD_SET_PREFIX = "set";

    private BeanUtil() {}

    // move from stock project
    public static <T> T jsonToBean(final T t, final Map<String, String> fieldMapper, final JsonNode node) throws Exception {
        MethodAccess access = MethodAccess.get(t.getClass());
        for (Map.Entry<String, String> entry : fieldMapper.entrySet()) {
            String key = entry.getKey();
            if (!StringUtils.isEmpty(key) && key.contains(SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET)
                && key.contains(SymbolConstant.SYMBOL_RIGHT_CIRCLE_BRACKET)) {
                String methodName = getSetMethodName(key.substring(0, key.indexOf(SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET)));
                String fieldType = key.substring(key.indexOf(SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET) + 1,
                    key.indexOf(SymbolConstant.SYMBOL_RIGHT_CIRCLE_BRACKET));
                access.invoke(t, methodName, getValue(fieldType, node.findPath(entry.getValue()).asText()));
            } else {
                BeanUtil.logger.error("Please check your configuration \"response.mapper.field\", " + key + " is invalid");
                throw new Exception(ExCodeConstant.EX_CODE_ILLEGAL_CONFIGURATION);
            }
        }
        return t;
    }


    /**
     *
     * <p>
     * <b> Convert Json Object to Bean Object. Use key to get field mapping.
     * </b>
     * </p>
     *
     * @param key
     * @param t
     * @param jsonObj
     * @return
     */
    public static <T> T jsonToBean(final String key, final T t, final JsonObject jsonObj) {
        @SuppressWarnings("unchecked")
        Map<String, Map<String, String[]>> fieldMappers =
            (Map<String, Map<String, String[]>>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER);
        Map<String, String[]> fieldMapper = fieldMappers.get(key);
        return jsonToBeanWithoutType(t, fieldMapper, jsonObj);
    }

    /**
     *
     * <p>
     * <b> The fieldMapper paramerter should meet the following structure: Java
     * Bean field name => corresponding JsonObject field name. If a Java Bean
     * field has multiple Json Bean fields, have a simple logic, take values in
     * order until get a valid value. </b>
     * </p>
     *
     * @param t
     * @param fieldMapper
     * @param jsonObj
     * @return
     */
    public static <T> T jsonToBeanWithoutType(final T t, final Map<String, String[]> fieldMapper, final JsonObject jsonObj) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String beanFieldName = field.getName();
            String beanFieldType = field.getType().getName();
            if (fieldMapper.containsKey(beanFieldName)) {
                String[] jsonObjFieldNames = fieldMapper.get(beanFieldName);
                String jsonObjFieldValue = "";
                for (String jsonObjFieldName : jsonObjFieldNames) {
                    jsonObjFieldValue = JsonUtil.getAsString(jsonObj, jsonObjFieldName);
                    if (StringUtil.isValidResp(jsonObjFieldValue)) {
                        break;
                    }
                }
                if (String.class.getName().equals(beanFieldType)) {
                    setValue(t, field, jsonObjFieldValue);
                } else if (BigDecimal.class.getName().equals(beanFieldType)) {
                    setValue(t, field, BigDecimalUtil.fromString(jsonObjFieldValue));
                }
            }
        }
        return t;
    }

    /**
     *
     * <p>
     * <b> Use reflection to set values to attributes. </b>
     * </p>
     *
     * @param t
     * @param field
     * @param jsonValue
     */
    private static <T> void setValue(final T t, final Field field, final Object jsonValue) {
        try {
            field.set(t, jsonValue);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            BeanUtil.logger
                .error("Use reflection to set value encounter error, field is " + field.getName() + ", data is " + jsonValue);
            throw new ApplicationException(e);
        }
    }

    /**
     *
     * <p>
     * <b> The fieldMapper paramerter should meet the following structure: Java
     * Bean field name(Java Bean field type) => corresponding JsonObject field
     * name. According to the field correspondence between the Java Bean and
     * the JsonObject, take out the value of field in the JsonObject then put
     * it into the Java Bean after convert. </b>
     * </p>
     *
     * @param t
     * @param fieldMapper
     * @param jsonObj
     * @return
     */
    public static <T> T jsonToBean(final T t, final Map<String, String> fieldMapper, final JsonObject jsonObj) {
        MethodAccess access = MethodAccess.get(t.getClass());
        for (Map.Entry<String, String> entry : fieldMapper.entrySet()) {
            String fieldOfJavaBean = entry.getKey();
            String fieldOfJsonObj = entry.getValue();
            String methodName = getSetMethodName(getFieldName(fieldOfJavaBean));
            String fieldType = getFieldType(fieldOfJavaBean);
            access.invoke(t, methodName, getValue(fieldType, JsonUtil.getAsString(jsonObj, fieldOfJsonObj)));
        }
        return t;
    }

    /**
     *
     * <p>
     * <b> Intercept the string before the left circle bracket as the field
     * name. </b>
     * </p>
     *
     * @param str
     * @return
     */
    private static String getFieldName(final String str) {
        return str.substring(0, str.indexOf(SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET));
    }

    /**
     *
     * <p>
     * <b> Get the set method name by given field name. </b>
     * </p>
     *
     * @param fieldName
     * @return
     */
    private static String getSetMethodName(final String fieldName) {
        StringBuilder sb = new StringBuilder();
        sb.append(BeanUtil.METHOD_SET_PREFIX);
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        return sb.toString();
    }

    /**
     *
     * <p>
     * <b> Intercept the string between left circle bracket and right circle
     * bracket as the field type. </b>
     * </p>
     *
     * @param str
     * @return
     */
    private static String getFieldType(final String str) {
        return str.substring(str.indexOf(SymbolConstant.SYMBOL_LEFT_CIRCLE_BRACKET) + 1,
            str.indexOf(SymbolConstant.SYMBOL_RIGHT_CIRCLE_BRACKET));
    }

    /**
     *
     * <p>
     * <b> Casting from String to given field type. </b>
     * </p>
     *
     * @param fieldType
     * @param value
     * @return
     */
    private static Object getValue(final String fieldType, final String value) {
        Object result;
        switch (fieldType) {
        case Constant.DATA_TYPE_STRING:
            result = StringUtil.fromRespStr(value);
            break;
        case Constant.DATA_TYPE_BIGDECIMAL:
            result = BigDecimalUtil.fromString(value);
            break;
        default:
            result = null;
        }
        return result;
    }

}
