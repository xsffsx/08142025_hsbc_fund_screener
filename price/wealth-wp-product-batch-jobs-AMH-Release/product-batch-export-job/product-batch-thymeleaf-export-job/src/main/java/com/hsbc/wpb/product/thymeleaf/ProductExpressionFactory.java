package com.dummy.wpb.product.thymeleaf;

import com.dummy.wpb.product.thymeleaf.expression.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class productExpressionFactory implements IExpressionObjectFactory {
    @Autowired
    private ProductUserDefinedMapping udfMapping;
    @Autowired
    private TimeZones timeZones;
    @Autowired
    private RefDatas refDatas;
    @Autowired
    private Products products;

    private static final String USER_DEFINED_FIELD_MAPPING_EXPRESSION_OBJECT_NAME = "udfMapping";
    private static final String DOUBLES_EXPRESSION_OBJECT_NAME = "doubles";
    private static final String TIME_ZONE_EXPRESSION_OBJECT_NAME = "timeZones";
    private static final String STRINGS_EXPRESSION_OBJECT_NAME = "csvs";
    private static final String REF_DATAS_EXPRESSION_OBJECT_NAME = "refDatas";
    private static final String PRODUCTS_EXPRESSION_OBJECT_NAME = "products";

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return new HashSet<>(Arrays.asList(
                USER_DEFINED_FIELD_MAPPING_EXPRESSION_OBJECT_NAME,
                DOUBLES_EXPRESSION_OBJECT_NAME,
                TIME_ZONE_EXPRESSION_OBJECT_NAME,
                STRINGS_EXPRESSION_OBJECT_NAME,
                REF_DATAS_EXPRESSION_OBJECT_NAME,
                PRODUCTS_EXPRESSION_OBJECT_NAME));
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        if (USER_DEFINED_FIELD_MAPPING_EXPRESSION_OBJECT_NAME.equals(expressionObjectName)) {
            return udfMapping;
        }

        if (DOUBLES_EXPRESSION_OBJECT_NAME.equals(expressionObjectName)) {
            return new Doubles();
        }

        if (TIME_ZONE_EXPRESSION_OBJECT_NAME.equals(expressionObjectName)) {
            return timeZones;
        }
        if (STRINGS_EXPRESSION_OBJECT_NAME.equals(expressionObjectName)) {
            return new Csvs();
        }
        if (REF_DATAS_EXPRESSION_OBJECT_NAME.equals(expressionObjectName)) {
            return refDatas;
        }
        if (PRODUCTS_EXPRESSION_OBJECT_NAME.equals(expressionObjectName)) {
            return products;
        }
        if ("csvs".equals(expressionObjectName)){
            return new Csvs();
        }

        return null;
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return true;
    }
}
