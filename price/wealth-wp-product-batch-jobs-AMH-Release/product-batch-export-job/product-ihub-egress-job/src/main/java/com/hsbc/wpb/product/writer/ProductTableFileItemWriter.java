package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.config.loader.DBConfigLoader;
import com.dummy.wpb.product.config.loader.ExportConfigLoader;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductMetadata;
import com.dummy.wpb.product.utils.JsonPathUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ProductTableFileItemWriter extends TableFileItemWriter {

    private final List<ProductMetadata> metadataList;

    private final String parentPath;

    public ProductTableFileItemWriter(String tableName, String outputPath) {
        super(tableName, outputPath);

        List<ProductMetadata> dbMetadataList = DBConfigLoader.getMetadata(tableName);
        List<String> tableFields = ExportConfigLoader.getProductTableFields(tableName);

        parentPath = dbMetadataList.get(0).getParent().equals("[ROOT]") ? "$" : dbMetadataList.get(0).getParent();
        metadataList = tableFields.stream()
                .map(fieldName -> dbMetadataList.stream()
                        .filter(m -> m.getFieldName().equals(fieldName))
                        .findFirst()
                        .orElse(createNoFoundMetadata(fieldName))
                )
                .collect(Collectors.toList());
    }

    private static final Pattern pattern = Pattern.compile("CTRY_PROD_TRADE_(\\d)_CDE");

    private ProductMetadata createNoFoundMetadata(String fieldName) {
        Matcher matcher = pattern.matcher(fieldName);
        if (matcher.find()) {
            String index = matcher.group(1);
            ProductMetadata ctryProdTradeCde = new ProductMetadata();
            ctryProdTradeCde.setFieldName(String.format("CTRY_PROD_TRADE_%s_CDE", index));
            ctryProdTradeCde.setAttrName(String.format("ctryProdTradeCde[%s]", Integer.parseInt(index) - 1));
            ctryProdTradeCde.setType("String");
            return ctryProdTradeCde;
        } else {
            return new ProductMetadata(fieldName);
        }

    }

    @Override
    protected List<String> aggregate(Document product) {
        Map<String, Object> calculatedFields = new HashMap<>();
        calculatedFields.put(Field.prodId, product.get(Field.prodId));
        calculatedFields.put(Field.ctryRecCde, product.get(Field.ctryRecCde));
        calculatedFields.put(Field.grpMembrRecCde, product.get(Field.grpMembrRecCde));

        Object tableObj = JsonPathUtils.readValue(product, parentPath);

        if (tableObj instanceof List) {
            List<Map<String, Object>> tableList = (List) tableObj;
            return tableList.stream()
                    .map(item -> this.doAggregate(item, calculatedFields))
                    .collect(Collectors.toList());
        } else if (tableObj instanceof Map) {
            Map<String, Object> tableDocument = (Map) tableObj;
            return Collections.singletonList(this.doAggregate(tableDocument, calculatedFields));
        }

        return Collections.emptyList();
    }

    private String doAggregate(Map<String, Object> tableDocument, Map<String, Object> calculatedFields) {
        List<String> attrValueList = new LinkedList<>();
        for (ProductMetadata metadata : metadataList) {
            String attrName = metadata.getAttrName();
            String fieldName = metadata.getFieldName();
            String value = null;

            if (fieldName.startsWith("PROD_ID")) {
                value = convertValueToStr(calculatedFields.get(Field.prodId));
            } else if (StringUtils.isBlank(attrName)) {
                value = convertValueToStr(EMPTY);
            } else if (attrName.startsWith("ctryProdTradeCde")) {
                value = convertValueToStr(JsonPathUtils.readValue(tableDocument, attrName));
            } else {
                String type = metadata.getType();
                Object attrValue = tableDocument.getOrDefault(attrName, calculatedFields.get(attrName));
                value = convertValueToStr(attrValue, type.equals("DateTime"));
            }

            if (value != null) {
                attrValueList.add(value);
            }
        }
        return StringUtils.join(attrValueList, CSV_DELIMITER);
    }

    @Override
    protected List<String> getHeaders() {
        return metadataList.stream().map(ProductMetadata::getFieldName).collect(Collectors.toList());
    }
}
