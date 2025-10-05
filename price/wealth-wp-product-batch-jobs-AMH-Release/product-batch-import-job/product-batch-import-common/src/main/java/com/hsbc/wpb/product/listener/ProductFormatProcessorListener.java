package com.dummy.wpb.product.listener;

import com.dummy.wpb.product.configuration.SystemDefaultValuesHolder;
import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductFormatService;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import lombok.SneakyThrows;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static com.dummy.wpb.product.constant.BatchImportAction.ADD;
import static com.dummy.wpb.product.constant.BatchImportAction.UPDATE;

/**
 * This class is used of do some common formatting when importing product xml files. <br>
 * If we want to use it, we mush register this class as spring bean with {@link org.springframework.batch.core.scope.StepScope}
 */
public class ProductFormatProcessorListener implements ItemProcessListener<ProductStreamItem, ProductStreamItem> {

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Value("#{'${batch.create-custelig.allow:}'.split(',')}")
    private List<String> createEligAllowCde;

    @Autowired
    private ProductFormatService productFormatService;

    @Autowired(required = false)
    private SystemUpdateConfigHolder updateConfigHolder;

    @Autowired(required = false)
    private SystemDefaultValuesHolder defaultValuesHolder;

    @Override
    @SneakyThrows
    public void beforeProcess(ProductStreamItem streamItem) {
        if (ADD == streamItem.getActionCode()) {
            beforeCreate(streamItem);
        }
        if (UPDATE == streamItem.getActionCode()) {
            beforeUpdate(streamItem);
        }

    }

    private void beforeCreate(ProductStreamItem streamItem) {
        // system-default-values must be configured to make to this class know this job can crate product
        if (Objects.isNull(defaultValuesHolder)) {
            return;
        }

        streamItem.setImportProduct(productFormatService.initProduct(streamItem.getImportProduct()));

        Document importProduct = streamItem.getImportProduct();

        if (StringUtils.containsAny(importProduct.getString(Field.prodStatCde), "E", "T", "D")
                && StringUtils.containsAny(importProduct.getString(Field.prodTypeCde), "UT", "BOND", "SEC", "SID", "INS")) {
            importProduct.put("allowBuyProdInd", "N");
            importProduct.put("allowSellMipProdInd", "N");
            importProduct.put("allowSellProdInd", "N");
            importProduct.put("allowSwInProdInd", "N");
            importProduct.put("allowSwOutProdInd", "N");
        }

        Map<String, Object> defaultValues = defaultValuesHolder.getDefaultValues(systemCde, getTypeCde(streamItem));
        defaultValues.forEach((jsonPath, value) -> {
            if (StringUtils.startsWithAny(jsonPath, "tradeElig.", "restrCustCtry")) {
                return;
            }

            if (!defaultValueCondition.containsKey(jsonPath)) {
                JsonPathUtils.setValueIfAbsent(importProduct, jsonPath, value);
            }
        });

        if (StringUtils.isBlank(importProduct.getString(Field.prodShrtName))) {
            importProduct.put(Field.prodShrtName, StringUtils.substring(importProduct.getString(Field.prodName), 0, 30));
        }
    }

    private void beforeUpdate(ProductStreamItem streamItem) {
        Document importProduct = streamItem.getImportProduct();
        Document originalProduct = streamItem.getOriginalProduct();

        //Update AsetVoltlClass in case manOverAssClaInd=Y
        String manOverAssClaInd = importProduct.getString(Field.manOverAssClaInd);
        if (StringUtils.equals(BatchConstants.INDICATOR_YES, manOverAssClaInd)) {
            importProduct.put(Field.asetVoltlClass, originalProduct.getString(Field.manOverAssClaInd));
        }

        if (Objects.isNull(importProduct.get(Field.prodSubtpCde))) {
            importProduct.put(Field.prodSubtpCde, originalProduct.getString(Field.prodSubtpCde));
        }

        streamItem.setUpdatedProduct(JsonUtil.deepCopy(streamItem.getOriginalProduct()));
    }

    @SneakyThrows
    @Override
    public void afterProcess(ProductStreamItem streamItem, ProductStreamItem result) {
        if (Objects.isNull(result)) return;

        String typeCde = getTypeCde(streamItem);

        BatchImportAction actionCode = streamItem.getActionCode();

        Document importProduct = streamItem.getImportProduct();

        createCustElig(importProduct, typeCde);

        if (actionCode == UPDATE && Objects.nonNull(updateConfigHolder)) {
            List<String> updateAttrs = updateConfigHolder.getUpdateAttrs(systemCde, typeCde, "product");
            productFormatService.formatByUpdateAttrs(importProduct, streamItem.getUpdatedProduct(), updateAttrs);
            if (null != importProduct.get(Field.tradeElig)) {
                streamItem.getUpdatedProduct().put(Field.tradeElig, importProduct.get(Field.tradeElig));
            }
        }

        if (actionCode == ADD) {
            Map<String, Object> defaultValues = defaultValuesHolder.getDefaultValues(systemCde, getTypeCde(streamItem));
            defaultValues.forEach((jsonPath, value) -> {
                Predicate<Document> condition = defaultValueCondition.get(jsonPath);
                if (null != condition && condition.test(importProduct)) {
                    JsonPathUtils.setValueIfAbsent(importProduct, jsonPath, value);
                }
            });
            streamItem.setUpdatedProduct(JsonUtil.deepCopy(streamItem.getImportProduct()));
        }

    }

    private String getTypeCde(ProductStreamItem streamItem) {
        String fileName = MapUtils.getString(streamItem.getParams(), "fileName");
        Assert.state(StringUtils.isNotBlank(fileName), "Please set fileName in params of ProductStreamItem.");
        Assert.state(fileName.split("_").length >= 4, "Please make sure the pattern of imported file name is correct.");
        return fileName.split("_")[3];
    }

    private void createCustElig(Document product, String typeCde) {
        String allowKey = systemCde;
        if (typeCde.contains(SEPARATOR_OF_PROD_TYPE_AND_MARKET)) {
            allowKey = String.format("%s.%s", systemCde, typeCde);
        }

        if (!createEligAllowCde.contains(allowKey)) {
            return;
        }

        String prodTypeCde = product.getString(Field.prodTypeCde);
        Map<String, Object> defaultValues = defaultValuesHolder.getDefaultValues(systemCde, typeCde);

        /*
         * Below code segment only valid for 3 case
         * case 1: prodTypeCde = "SID" and undlAset1 = "EQ"
         * case 2: prodTypeCde = "ELI"
         * case 3: prodTypeCde = "BOND"
         * */
        boolean createCustCtry = StringUtils.equalsAny(prodTypeCde, EQUITY_LINKED_INVESTMENT, BOND_CD)
                || (StringUtils.equalsAny(prodTypeCde, STRUCTURE_INVESTMENT_DEPOSIT)
                && StringUtils.equals("EQ", JsonPathUtils.readValue(product, "undlAset[0].asetUndlCde")));

        defaultValues.forEach((path, value) -> {
            if (path.startsWith("tradeElig.")) {
                JsonPathUtils.setValueIfAbsent(product, path, value);
            }
            if (path.startsWith("restrCustCtry") && createCustCtry) {
                JsonPathUtils.setValue(product, path, value);
            }
        });
    }

    @Override
    public void onProcessError(ProductStreamItem streamItem, Exception e) {
        // do nothing
    }

    Map<String, Predicate<Document>> defaultValueCondition = new ConcurrentHashMap<>();

    public void putDefaultValueCondition(String key, Predicate<Document> defaultValuesCondition) {
        this.defaultValueCondition.put(key, defaultValuesCondition);
    }
}