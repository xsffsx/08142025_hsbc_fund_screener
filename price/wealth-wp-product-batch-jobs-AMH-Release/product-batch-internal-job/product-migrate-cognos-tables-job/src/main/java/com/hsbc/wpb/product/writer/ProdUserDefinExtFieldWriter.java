package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.config.UserDefinedFieldConfig;
import com.dummy.wpb.product.constant.FieldDataType;
import com.dummy.wpb.product.loader.DBConfigLoader;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.jpo.ProdUserDefinExtFieldPo;
import com.dummy.wpb.product.repository.ProdUserDefinExtFieldRepository;
import com.dummy.wpb.product.service.BulkInsertService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProdUserDefinExtFieldWriter implements ProductMigrateWriter{

    @Autowired
    private ProdUserDefinExtFieldRepository prodUserDefinExtFieldRepository;

    @Autowired
    private BulkInsertService bulkInsertService;

    @Override
    public void write(List<? extends Document> productList) {
        List<ProdUserDefinExtFieldPo> prodUserDefinExtFieldPoList = new ArrayList<>();
        productList.forEach(product -> {
            prodUserDefinExtFieldRepository.deleteByProdId(product.getLong(Field.prodId));
            List<ProdUserDefinExtFieldPo> prodUserDefinExtFieldPos = handleUserDefField(product);
            prodUserDefinExtFieldPoList.addAll(prodUserDefinExtFieldPos);
        });
        bulkInsertService.batchInsert(prodUserDefinExtFieldPoList);
    }

    private ArrayList<ProdUserDefinExtFieldPo> handleUserDefField(Document product) {
        ArrayList<ProdUserDefinExtFieldPo> userDefinExtFieldPos = new ArrayList<>();
        List<UserDefinedFieldConfig> configList = DBConfigLoader.getUserDefinedFieldConfig("TB_PROD_USER_DEFIN_EXT_FIELD");
        Map<UserDefinedFieldConfig, String[]> configPathsMap = configList.stream().collect(Collectors.toMap(
                config -> config,
                config -> config.getJsonPath().split("\\."),
                (k1, k2) -> k2,
                LinkedHashMap::new
        ));
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
                    userDefinExtFieldPos.add(parseValueToPo(config, value, seq++, product));
                }
            } else {
                userDefinExtFieldPos.add(parseValueToPo(config, valueObj, seq, product));
            }
        }
        return userDefinExtFieldPos;
    }

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

    private ProdUserDefinExtFieldPo parseValueToPo(UserDefinedFieldConfig config, Object value, Integer seq, Document product) {
        ProdUserDefinExtFieldPo prodUserDefinExtFieldPo = new ProdUserDefinExtFieldPo();
        prodUserDefinExtFieldPo.setRowid(product.getLong(Field._id) + config.getFieldTypeCde() + config.getFieldCde() + seq);
        prodUserDefinExtFieldPo.setProdId(product.getLong(Field._id));
        prodUserDefinExtFieldPo.setFieldTypeCde(config.getFieldTypeCde());
        prodUserDefinExtFieldPo.setFieldCde(config.getFieldCde());
        prodUserDefinExtFieldPo.setFieldSeqNum(seq.longValue());
        prodUserDefinExtFieldPo.setCtryRecCde(product.getString(Field.ctryRecCde));
        prodUserDefinExtFieldPo.setGrpMembrRecCde(product.getString(Field.grpMembrRecCde));
        String dataType = config.getFieldDataTypeText();
        prodUserDefinExtFieldPo.setFieldDataTypeText(dataType);
        prodUserDefinExtFieldPo.setFieldCharValueText(dataType.equals(FieldDataType.CHAR) ? (String) convertValueByType(value, dataType, FieldDataType.CHAR) : null);
        prodUserDefinExtFieldPo.setFieldStringValueText(dataType.equals(FieldDataType.STRING) ? (String) convertValueByType(value, dataType, FieldDataType.STRING) : null);
        prodUserDefinExtFieldPo.setFieldIntgValueText(dataType.equals(FieldDataType.INTEGER) ? (Long) convertValueByType(value, dataType, FieldDataType.INTEGER) : null);
        prodUserDefinExtFieldPo.setFieldDcmlValueText(dataType.equals(FieldDataType.DECIMAL) ? (Double) convertValueByType(value, dataType, FieldDataType.DECIMAL) : null);
        prodUserDefinExtFieldPo.setFieldDtValueText(dataType.equals(FieldDataType.DATE) ? (Date) convertValueByType(value, dataType, FieldDataType.DATE) : null);
        prodUserDefinExtFieldPo.setFieldTsValueText(dataType.equals(FieldDataType.TIMESTAMP) ? (Date) convertValueByType(value, dataType, FieldDataType.TIMESTAMP) : null);
        prodUserDefinExtFieldPo.setRecCreatDtTm(product.getDate(Field.recCreatDtTm));
        prodUserDefinExtFieldPo.setRecUpdtDtTm(product.getDate(Field.recUpdtDtTm));
        return prodUserDefinExtFieldPo;
    }

    private Object convertValueByType(Object value, String actualType, String expectType) {
        if (FieldDataType.DECIMAL.equals(actualType)) {
            value = value != null ? ((Number)value).doubleValue() : null;
        }
        return Objects.equals(actualType, expectType) ? value : null;
    }
}
