package com.dummy.wpb.product.service;

import com.dummy.wpb.product.constant.DataType;
import com.dummy.wpb.product.model.ExcelColumnInfo;
import com.dummy.wpb.product.utils.JsonPathUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.joda.time.format.DateTimeFormat;

import java.math.BigDecimal;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ExcelParseService {

    public Document parseExcelObject(List<ExcelColumnInfo> excelColumnInfoList) {
        Document document = new Document();
        for (ExcelColumnInfo columnInfo : excelColumnInfoList) {
            if (!needSkip(columnInfo)) {
                setProductField(document, columnInfo);
            }
        }
        afterSetProductField(document);
        return document;
    }


    protected boolean needSkip(ExcelColumnInfo columnInfo) {
        return StringUtils.isBlank(columnInfo.getJsonPath());
    }

    protected void setProductField(Document doc, ExcelColumnInfo columnInfo) {
        Object objValue = covertValueType(columnInfo);
        String jsonPath = columnInfo.getJsonPath();
        //If objValue is null, make sure not to add an empty object to the product
        if (JsonPathUtils.readValue(doc, jsonPath) != objValue) {
            JsonPathUtils.setValue(doc, jsonPath, objValue);
        }
    }

    protected Object covertValueType(ExcelColumnInfo columnInfo) {
        String value = columnInfo.getValue();
        if (StringUtils.isBlank(columnInfo.getValue())) {
            return null;
        }

        Object result;

        String dataType = columnInfo.getDataType();
        String dateFormat = columnInfo.getDateFormat();

        switch (dataType) {
            case DataType.CONSTANT:
            case DataType.STRING:
                result = value;
                break;
            case DataType.LONG:
                result = Long.parseLong(value);
                break;
            case DataType.FLOAT:
                result = Double.valueOf(value);
                break;
            case DataType.DATE:
                result = covertDateValue(value, dateFormat, "yyyy-MM-dd");
                break;
            case DataType.LOCAL_TIME:
                result = covertDateValue(value, dateFormat, "HH:mm:ss");
                break;
            case DataType.DATE_TIME:
            case DataType.LOCAL_DATE_TIME:
                result = covertDateValue(value, dateFormat, "yyyy-MM-dd'T'HH:mm:ss");
                break;
            case DataType.STRING_ARRAY:
                result = covertStringArrayValue(value);
                break;
            case DataType.BIGDECIMAL:
                result = convertBigDecimalValue(value);
                break;
            default:
                result = convertOtherValue(columnInfo);
        }
        return result;
    }

    protected BigDecimal convertBigDecimalValue(String value) {
        return new BigDecimal(StringUtils.replace(value, ",", EMPTY));
    }

    private Object covertStringArrayValue(String value) {
        if (StringUtils.isBlank(value)) {
            return Collections.emptyList();
        }

        String arrySpilt = String.format("%s%s", " ", "\n");
        return Arrays.asList(value.split(arrySpilt));
    }

    protected String covertDateValue(String value, String dateFormat, String toFormat) {
        return DateTimeFormat.forPattern(dateFormat).parseDateTime(value).toString(toFormat);
    }

    protected Object convertOtherValue(ExcelColumnInfo columnInfo) {
        return columnInfo.getValue();
    }

    protected void afterSetProductField(Document document) {
        //
    }
}
