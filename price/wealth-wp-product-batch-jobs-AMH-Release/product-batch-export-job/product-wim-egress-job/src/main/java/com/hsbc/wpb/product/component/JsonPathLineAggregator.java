package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.TableInfo;
import com.dummy.wpb.product.utils.JsonPathUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.item.file.transform.LineAggregator;

import java.text.SimpleDateFormat;
import java.util.*;

public class JsonPathLineAggregator implements LineAggregator<Document> {
    private final TableInfo tableInfo;
    private static final String CSV_DELIMITER = "|";

    public JsonPathLineAggregator(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    @Override
    public String aggregate(Document document) {
        return doAggregate(document, tableInfo);
    }

    private String doAggregate(Document document, TableInfo tableInfo) {
        List<String> tableStringList = new LinkedList<>();

        Map<String, Object> calculatedFields = new HashMap<>();
        calculatedFields.put(Field.prodId, document.get(Field.prodId));
        calculatedFields.put(Field.ctryRecCde, document.get(Field.ctryRecCde));
        calculatedFields.put(Field.grpMembrRecCde, document.get(Field.grpMembrRecCde));

        Object tableObj = JsonPathUtils.readValue(document, tableInfo.getParent());
        if (tableObj instanceof List) {
            List<Map<String, Object>> tableDocList = (List) tableObj;
            for (Map<String, Object> tableDoc : tableDocList) {
                tableStringList.add(readTableAsString(tableDoc, tableInfo, calculatedFields));
            }
        } else if (tableObj instanceof Map) {
            Map<String, Object> tableDoc = (Map) tableObj;
            tableStringList.add(readTableAsString(tableDoc, tableInfo, calculatedFields));
        }
        return StringUtils.join(tableStringList, System.lineSeparator());
    }

    private String readTableAsString(Map<String, Object> tableDoc, TableInfo tableInfo, Map<String, Object> calculatedFields) {
        List<String> attrValueList = new LinkedList<>();
        for (String attributePath : tableInfo.getFieldMapping().values()) {
            Object objValue = JsonPathUtils.readValue(tableDoc, attributePath, calculatedFields.get(attributePath));
            // Can it judge whether it is a date directly through the field name?
            String attrValue = convertObjToStr(objValue, attributePath.endsWith("Dt"));
            attrValueList.add(attrValue);
        }
        return StringUtils.join(attrValueList, CSV_DELIMITER);
    }

    private String convertObjToStr(Object object, boolean isDate) {
        String result;
        if (object instanceof Date) {
            if (isDate) {
                result = new SimpleDateFormat("yyyy-MM-dd").format(object);
            } else {
                result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(object);
            }
        } else if (object instanceof List) {
            result = StringUtils.join((List<Object>) object, ",");
        } else {
            result = Objects.toString(object, StringUtils.EMPTY);
        }

        if (StringUtils.isNotBlank(result) && !(object instanceof Number)) {
            //Avoid line breaks caused by commas in text
            result = String.format("\"%s\"", result);
        }

        return result;
    }
}
