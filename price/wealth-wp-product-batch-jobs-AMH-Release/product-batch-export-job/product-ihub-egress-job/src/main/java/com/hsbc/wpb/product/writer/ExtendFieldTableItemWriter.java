package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.config.loader.DBConfigLoader;
import com.dummy.wpb.product.config.loader.ExportConfigLoader;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.UserDefinedFieldConfig;
import com.dummy.wpb.product.model.UserDefinedFieldConfig.FieldDataType;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.model.UserDefinedFieldConfig.FieldDataType.*;

public class ExtendFieldTableItemWriter extends TableFileItemWriter {
    private final List<String> headers;

    private final Map<UserDefinedFieldConfig, String[]> configPathsMap;

    public ExtendFieldTableItemWriter(String tableName, String outputPath) {
        super(tableName, outputPath);

        headers = ExportConfigLoader.getProductTableFields(tableName);
        List<UserDefinedFieldConfig> configList = DBConfigLoader.getUserDefinedFieldConfig(tableName);
        configPathsMap = configList.stream().collect(Collectors.toMap(
                config -> config,
                config -> config.getJsonPath().split("\\."),
                (k1, k2) -> k2,
                LinkedHashMap::new
        ));
    }

    @Override
    protected List<String> aggregate(Document product) {
        List<String> lines = new LinkedList<>();
        for (Map.Entry<UserDefinedFieldConfig, String[]> entry : configPathsMap.entrySet()) {

            UserDefinedFieldConfig config = entry.getKey();

            Object valueObj = readValue(product, entry.getValue());

            if (Objects.isNull(valueObj)) {
                continue;
            }

            int seq = 1;
            if (valueObj instanceof List) {
                List<Object> valueList = (List<Object>) valueObj;
                for (Object value : valueList) {
                    lines.add(StringUtils.join(parseValueToLine(config, value, seq++, product), CSV_DELIMITER));
                }
            } else {
                lines.add(StringUtils.join(parseValueToLine(config, valueObj, seq, product), CSV_DELIMITER));
            }
        }

        return lines;
    }

    /**
     * Better performance than {@link com.dummy.wpb.product.utils.JsonPathUtils#readValue(Map, String)}  }
     */
    private Object readValue(Document product, String[] paths) {
        Object value = product;
        for (String path : paths) {
            if (!(value instanceof Document)) {
                return value;
            }
            value = ((Document) value).get(path);
        }
        return value;
    }

    private List<String> parseValueToLine(UserDefinedFieldConfig config, Object value, Integer seq, Document product) {
        List<String> result = new LinkedList<>();

        FieldDataType dataType = config.getFieldDataTypeText();
        for (String header : headers) {
            switch (header) {
                case "PROD_ID":
                    result.add(convertValueToStr(product.get(Field._id)));
                    break;
                case "FIELD_TYPE_CDE":
                    result.add(convertValueToStr(config.getFieldTypeCde()));
                    break;
                case "FIELD_CDE":
                    result.add(convertValueToStr(config.getFieldCde()));
                    break;
                case "FIELD_SEQ_NUM":
                    result.add(convertValueToStr(seq));
                    break;
                case "CTRY_REC_CDE":
                    result.add(convertValueToStr(product.getString(Field.ctryRecCde)));
                    break;
                case "GRP_MEMBR_REC_CDE":
                    result.add(convertValueToStr(product.getString(Field.grpMembrRecCde)));
                    break;
                case "FIELD_DATA_TYPE_TEXT":
                    result.add(convertValueToStr(dataType));
                    break;
                case "FIELD_CHAR_VALUE_TEXT":
                    result.add(convertValueByType(value, dataType, CHAR));
                    break;
                case "FIELD_STRNG_VALUE_TEXT":
                    result.add(convertValueByType(value, dataType, STRING));
                    break;
                case "FIELD_INTG_VALUE_NUM":
                    result.add(convertValueByType(value, dataType, INTEGER));
                    break;
                case "FIELD_DCML_VALUE_NUM":
                    result.add(convertValueByType(value, dataType, DECIMAL));
                    break;
                case "FIELD_DT_VALUE_DT":
                    result.add(convertValueByType(value, dataType, DATE));
                    break;
                case "FIELD_TS_VALUE_DT_TM":
                    result.add(convertValueByType(value, dataType, TIMESTAMP));
                    break;
                case "FIELD_VALUE_TEXT":
                    result.add(convertValueToStr(value));
                    break;
                case "REC_CREAT_DT_TM":
                    result.add(convertValueToStr(product.get(Field.recCreatDtTm), true));
                    break;
                case "REC_UPDT_DT_TM":
                    result.add(convertValueToStr(product.get(Field.recUpdtDtTm), true));
                    break;
                default:
                    result.add(StringUtils.EMPTY);
            }
        }
        return result;
    }

    private String convertValueByType(Object value, FieldDataType actualType, FieldDataType expectType) {
        return actualType == expectType ? convertValueToStr(value, expectType == TIMESTAMP) : StringUtils.EMPTY;
    }

    @Override
    protected List<String> getHeaders() {
        return headers;
    }
}
