package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductFormatService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.ProductKeyUtils;
import com.dummy.wpb.product.utils.JsonPathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static com.dummy.wpb.product.constant.BatchImportAction.ADD;
import static com.dummy.wpb.product.constant.BatchImportAction.UPDATE;

@Slf4j
public class UtTrstInstmFormatProcessor implements ItemProcessor<ProductStreamItem, ProductStreamItem> {

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Autowired
    ProductFormatService productFormatService;

    @Autowired
    ProductService productService;

    @Value("#{'${batch.create.allow}'.split(',')}")
    private List<String> createAllowCde;

    @Override
    public ProductStreamItem process(ProductStreamItem streamItem) throws Exception {
        BatchImportAction actionCode = streamItem.getActionCode();

        if (ADD == actionCode) {
            if (!createAllowCde.contains(systemCde)) {
                log.warn("Can not find origin product {}", ProductKeyUtils.buildProdKeyInfo(streamItem.getImportProduct()));
                return null;
            }

            addProd(streamItem);
        } else if (UPDATE == actionCode) {
            updateProd(streamItem);
        }

        return streamItem;
    }

    private void addProd(ProductStreamItem streamItem) {
        Document importProduct = streamItem.getImportProduct();
        JsonPathUtils.deleteValue(importProduct, "utTrstInstm.scribCtoffNextDtTm");
        JsonPathUtils.deleteValue(importProduct, "utTrstInstm.rdmCtoffNextDtTm");
    }

    private void updateProd(ProductStreamItem streamItem) {
        Document importProduct = streamItem.getImportProduct();
        Document originalProduct = streamItem.getOriginalProduct();

        if (MORNING_STAR.equals(systemCde)) {
            String priShareClassInd = JsonPathUtils.readValue(originalProduct, "utTrstInstm.priShareClassInd", INDICATOR_NO);
            if (INDICATOR_NO.equals(priShareClassInd)) {
                importProduct.put(Field.topSellProdInd, INDICATOR_NO);
            }
            if (INDICATOR_NO.equals(importProduct.getString(Field.topSellProdInd))) {
                importProduct.put(Field.prodTopSellRankNum, null);
            }

            // refer wpc config: PROD_ALT_CLASS_CDE.BYPASS
            importProduct.getList(Field.altId, Document.class, Collections.emptyList()).removeIf(alt -> StringUtils.equalsAny(alt.getString(Field.prodCdeAltClassCde), "S", "I"));
        }

        // set up utTrstInstm.prodTrmtDt when update prodStatCde to T
        if (TERMINATED.equals(importProduct.getString(Field.prodStatCde))) {
            String prodTrmtDt = JsonPathUtils.readValue(originalProduct, "utTrstInstm.prodTrmtDt", LocalDate.now().toString());
            JsonPathUtils.setValue(importProduct, "utTrstInstm.prodTrmtDt", prodTrmtDt);
        }

        formatChanlAttr(importProduct, originalProduct);
    }

    private void formatChanlAttr(Document importProduct, Document originalProduct) {
        List<Document> importChanlAttrList = importProduct.getList(Field.chanlAttr, Document.class);
        if (CollectionUtils.isEmpty(importChanlAttrList)) {
            return;
        }

        String prodStatCde = importProduct.getString(Field.prodStatCde);
        boolean disableBuyInd = StringUtils.equalsAny(prodStatCde, DELISTED, CLOSED_FROM_SUBSCRIPTION, TERMINATED, EXPIRED);

        List<Map<String, Object>> chanlAttrs = (List<Map<String, Object>>) originalProduct.get(Field.chanlAttr);
        Map<String, Map<String, Object>> historyChanlAttrs = chanlAttrs
                .stream()
                .filter(item -> SI_I.equals(item.get(Field.chanlCde)))
                .collect(Collectors.toMap(item -> (String) item.get(Field.fieldCde), Function.identity()));

        importChanlAttrList.stream().filter(item -> SI_I.equals(item.get(Field.chanlCde))).forEach(importChanlAttr -> {
            String fieldCde = importChanlAttr.getString(Field.fieldCde);
            if (ALLOW_BUY_PROD_IND.equals(fieldCde) && disableBuyInd) {
                importChanlAttr.put(Field.fieldCharValueText, INDICATOR_NO);
                return;
            }

            Map<String, Object> historyChanlAttr = historyChanlAttrs.get(fieldCde);
            if (null != historyChanlAttr) {
                importChanlAttr.put(Field.fieldCharValueText, historyChanlAttr.get(Field.fieldCharValueText));
            }
        });
    }
}
