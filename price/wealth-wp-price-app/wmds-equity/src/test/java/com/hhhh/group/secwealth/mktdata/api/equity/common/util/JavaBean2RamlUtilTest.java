package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JavaBean2RamlUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(JavaBean2RamlUtilTest.class);

    public static final String JAVA_LANG_STRING = "java.lang.String";
    public static final String JAVA_LANG_BOOLEAN = "java.lang.Boolean";
    public static final String JAVA_UTIL_LIST = "java.util.List";
    public static final String JAVA_LANG_NUMBER = "java.lang.Number";
    public static final String JAVA_LANG_INTEGER = "java.lang.Integer";
    public static final String JAVA_INT = "int";
    public static final String JAVA_MATH_BIGDECIMAL = "java.math.BigDecimal";
    public static final String JAVA_LANG_LONG = "java.lang.Long";
    public static final String JAVA_LONG = "long";

    public static final String TYPE_OBJECT = "\"type\": \"object\",";
    public static final String TYPE_ARRAY = "\"type\": \"array\",";
    public static final String TYPE_STRING = "\"type\": \"string\"";
    public static final String TYPE_BOOLEAN = "\"type\": \"boolean\"";
    public static final String TYPE_NUMBER = "\"type\": \"number\"";
    public static final String TYPE_INTEGER = "\"type\": \"integer\"";
    public static final String TYPE_INT = "\"type\": \"int\"";
    public static final String TYPE_BIGDECIMAL = "\"type\": \"bigDecimal\"";
    public static final String TYPE_LONG = "\"type\": \"long\"";

    public static final String ANNOTATION_NOTEMPTY = "NotEmpty";
    public static final String ANNOTATION_NOTNULL = "NotNull";

    public static final String RAML_SCHEMA = "\"$schema\": \"http://json-schema.org/draft-04/schema#\",";
    public static final String START_SYMBOL_DEFAULT = "{";
    public static final String END_SYMBOL_DEFAULT = "}";
    public static final String START_SYMBOL_ARRAY = "[";
    public static final String END_SYMBOL_ARRAY = "]";
    public static final String DOUHAO_SEPARATOR = ",";
    public static final String MAOHAO_SEPARATOR = ":";
    public static final String DOUBLE_QUOTES_SEPARATOR = "\"";

    public static final String NODE_PROPERTIES = "\"properties\": {";
    public static final String NODE_ITEMS = "\"items\": [";
    public static final String NODE_REQUIRED = "\"required\": [";
    public static String[] classNames = null;

    static {
        JavaBean2RamlUtilTest.classNames = new String[] {"com.hhhh.group.secwealth.mktdata.wmds_equity.bean.request.QuotesRequest",
            "com.hhhh.group.secwealth.mktdata.wmds_equity.bean.response.QuotesResponse",
            "com.hhhh.group.secwealth.mktdata.wmds_equity.bean.request.SameSectorRequest",
            "com.hhhh.group.secwealth.mktdata.wmds_equity.bean.response.SameSectorResponse",
            "com.hhhh.group.secwealth.mktdata.wmds_equity.bean.request.TopTenMoversRequest",
            "com.hhhh.group.secwealth.mktdata.wmds_equity.bean.response.TopTenMoversResponse"};
    }

    @Test
    public void javaBeans2Raml() {
        for (String className : JavaBean2RamlUtilTest.classNames) {
            javaBean2Raml(className);
        }
        Assert.assertNotNull(JavaBean2RamlUtilTest.classNames);
    }

    public void javaBean2Raml(final String className) {

        try {
            StringBuilder sb = new StringBuilder();
            // step1
            appendNodeSchema(sb);
            // step2
            appendNodePropertiesAndRequired(sb, className);
            // step3
            sb.append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT);

            JavaBean2RamlUtilTest.logger.debug("className is : " + className);
            JavaBean2RamlUtilTest.logger.debug("Generate raml string is : " + sb.toString());
        } catch (Exception e) {
            JavaBean2RamlUtilTest.logger.error("java bean 2 raml fail with error: " + e);
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
            JavaBean2RamlUtilTest.logger.error("error: " + stackTraceElement);
            JavaBean2RamlUtilTest.logger.error("error name: " + stackTraceElement.getFileName());
            JavaBean2RamlUtilTest.logger.error("error line number: " + stackTraceElement.getLineNumber());
            JavaBean2RamlUtilTest.logger.error("error method name: " + stackTraceElement.getMethodName());
        }
    }

    public void appendNodeSchema(final StringBuilder sb) {
        sb.append(JavaBean2RamlUtilTest.START_SYMBOL_DEFAULT).append(JavaBean2RamlUtilTest.RAML_SCHEMA)
            .append(JavaBean2RamlUtilTest.TYPE_OBJECT);
    }

    public void appendNodePropertiesAndRequired(final StringBuilder sb, final String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        Field[] fields = clazz.getDeclaredFields();
        sb.append(JavaBean2RamlUtilTest.NODE_PROPERTIES);
        // append properties
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String typeName = fields[i].getGenericType().getTypeName();
            if (fieldName.contains("this")) {
                break;
            }

            boolean isLastOne = false;
            if (i == fields.length - 1) {
                isLastOne = true;
            }
            appendFieldNode(sb, fieldName, typeName, isLastOne);
        }
        sb.append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT).append(JavaBean2RamlUtilTest.DOUHAO_SEPARATOR);

        // append required
        sb.append(JavaBean2RamlUtilTest.NODE_REQUIRED);
        List<String> requiredFieldNames = new ArrayList<String>();
        for (int j = 0; j < fields.length; j++) {
            String fieldName = fields[j].getName();
            String typeName = fields[j].getGenericType().getTypeName();
            if (fieldName.contains("this")) {
                break;
            }

            // according annotation check if required
            Annotation[] annotations = fields[j].getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.toString().contains(JavaBean2RamlUtilTest.ANNOTATION_NOTEMPTY)
                    || annotation.toString().contains(JavaBean2RamlUtilTest.ANNOTATION_NOTNULL)) {
                    if (!typeName.contains(JavaBean2RamlUtilTest.JAVA_UTIL_LIST)) {
                        if (!requiredFieldNames.contains(fieldName)) {
                            requiredFieldNames.add(fieldName);
                        }
                    }
                }
            }
        }
        if (requiredFieldNames.size() > 0) {
            for (int z = 0; z < requiredFieldNames.size(); z++) {
                if (z == requiredFieldNames.size() - 1) {
                    sb.append(JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR + requiredFieldNames.get(z)
                        + JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR);
                } else {
                    sb.append(JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR + requiredFieldNames.get(z)
                        + JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR).append(JavaBean2RamlUtilTest.DOUHAO_SEPARATOR);
                }
            }
        }
        sb.append(JavaBean2RamlUtilTest.END_SYMBOL_ARRAY);
    }

    public void appendFieldNode(final StringBuilder sb, final String fieldName, final String typeName, final boolean isLastOne)
        throws ClassNotFoundException {

        if (typeName.contains(JavaBean2RamlUtilTest.JAVA_LANG_STRING)) {
            sb.append(JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR + fieldName + JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR)
                .append(JavaBean2RamlUtilTest.MAOHAO_SEPARATOR).append(JavaBean2RamlUtilTest.START_SYMBOL_DEFAULT)
                .append(JavaBean2RamlUtilTest.TYPE_STRING).append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT);
        } else if (typeName.contains(JavaBean2RamlUtilTest.JAVA_LANG_BOOLEAN)) {
            sb.append(JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR + fieldName + JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR)
                .append(JavaBean2RamlUtilTest.MAOHAO_SEPARATOR).append(JavaBean2RamlUtilTest.START_SYMBOL_DEFAULT)
                .append(JavaBean2RamlUtilTest.TYPE_BOOLEAN).append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT);
        } else if (typeName.contains(JavaBean2RamlUtilTest.JAVA_UTIL_LIST)) {
            // array start
            sb.append(JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR + fieldName + JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR)
                .append(JavaBean2RamlUtilTest.MAOHAO_SEPARATOR).append(JavaBean2RamlUtilTest.START_SYMBOL_DEFAULT)
                .append(JavaBean2RamlUtilTest.TYPE_ARRAY).append(JavaBean2RamlUtilTest.NODE_ITEMS)
                .append(JavaBean2RamlUtilTest.START_SYMBOL_DEFAULT).append(JavaBean2RamlUtilTest.TYPE_OBJECT);
            // properties start
            String className = typeName.substring(typeName.indexOf("<") + 1, typeName.indexOf(">"));
            appendNodePropertiesAndRequired(sb, className);
            // properties end
            sb.append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT).append(JavaBean2RamlUtilTest.END_SYMBOL_ARRAY)
                .append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT);
            // array end
        } else if (typeName.contains(JavaBean2RamlUtilTest.JAVA_LANG_NUMBER)) {
            sb.append(JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR + fieldName + JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR)
                .append(JavaBean2RamlUtilTest.MAOHAO_SEPARATOR).append(JavaBean2RamlUtilTest.START_SYMBOL_DEFAULT)
                .append(JavaBean2RamlUtilTest.TYPE_NUMBER).append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT);
        } else if (typeName.contains(JavaBean2RamlUtilTest.JAVA_MATH_BIGDECIMAL)) {
            sb.append(JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR + fieldName + JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR)
                .append(JavaBean2RamlUtilTest.MAOHAO_SEPARATOR).append(JavaBean2RamlUtilTest.START_SYMBOL_DEFAULT)
                .append(JavaBean2RamlUtilTest.TYPE_BIGDECIMAL).append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT);
        } else if (typeName.contains(JavaBean2RamlUtilTest.JAVA_LANG_INTEGER)) {
            sb.append(JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR + fieldName + JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR)
                .append(JavaBean2RamlUtilTest.MAOHAO_SEPARATOR).append(JavaBean2RamlUtilTest.START_SYMBOL_DEFAULT)
                .append(JavaBean2RamlUtilTest.TYPE_INTEGER).append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT);
        } else if (typeName.contains(JavaBean2RamlUtilTest.JAVA_INT)) {
            sb.append(JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR + fieldName + JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR)
                .append(JavaBean2RamlUtilTest.MAOHAO_SEPARATOR).append(JavaBean2RamlUtilTest.START_SYMBOL_DEFAULT)
                .append(JavaBean2RamlUtilTest.TYPE_INT).append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT);
        } else if (typeName.contains(JavaBean2RamlUtilTest.JAVA_LANG_LONG)) {
            sb.append(JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR + fieldName + JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR)
                .append(JavaBean2RamlUtilTest.MAOHAO_SEPARATOR).append(JavaBean2RamlUtilTest.START_SYMBOL_DEFAULT)
                .append(JavaBean2RamlUtilTest.TYPE_LONG).append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT);
        } else if (typeName.contains(JavaBean2RamlUtilTest.JAVA_LONG)) {
            sb.append(JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR + fieldName + JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR)
                .append(JavaBean2RamlUtilTest.MAOHAO_SEPARATOR).append(JavaBean2RamlUtilTest.START_SYMBOL_DEFAULT)
                .append(JavaBean2RamlUtilTest.TYPE_LONG).append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT);
        } else {
            // self define class eg: private Pagination pagination
            sb.append(JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR + fieldName + JavaBean2RamlUtilTest.DOUBLE_QUOTES_SEPARATOR)
                .append(JavaBean2RamlUtilTest.MAOHAO_SEPARATOR).append(JavaBean2RamlUtilTest.START_SYMBOL_DEFAULT)
                .append(JavaBean2RamlUtilTest.TYPE_OBJECT);
            String className = typeName;
            appendNodePropertiesAndRequired(sb, className);
            sb.append(JavaBean2RamlUtilTest.END_SYMBOL_DEFAULT);
        }
        if (!isLastOne) {
            sb.append(JavaBean2RamlUtilTest.DOUHAO_SEPARATOR);
        }
    }
}
