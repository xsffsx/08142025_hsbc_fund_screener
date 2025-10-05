package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
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
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static com.dummy.wpb.product.constant.BatchImportAction.ADD;
import static com.dummy.wpb.product.constant.BatchImportAction.UPDATE;

@Slf4j
public class EliXmlFileProcessor implements ItemProcessor<ProductStreamItem, ProductStreamItem> {
    @Value("#{'${batch.create.not-allow}'.split(',')}")
    private List<String> notAllowCreateSystemCdes;

    @Value("#{'${batch.format.allow}'.split(',')}")
    private List<String> formatSystemCdes;

    @Value("#{'${batch.underlying.prodtype.code}'.split(',')}")
    private List<String> undelProdtypes;

    @Value("#{jobParameters['ctryRecCde']}")
    private String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    private static final Map<String, String[]> statCdeChanlAttrFieldsMap = new HashMap<>();

    @Autowired
    private ProductService productService;

    static {
        statCdeChanlAttrFieldsMap.put(ACTIVE, new String[]{"Y", "N", "N", "N", "N"});
        statCdeChanlAttrFieldsMap.put(CLOSED_FROM_SUBSCRIPTION, new String[]{"N", "Y", "N", "N", "N"});
        statCdeChanlAttrFieldsMap.put(EXPIRED, new String[]{"N", "N", "N", "N", "N"});
    }

    @Override
    public ProductStreamItem process(ProductStreamItem streamItem) throws Exception {
        if (!validate(streamItem)) {
            return null;
        }

        systemFormat(streamItem);

        return streamItem;
    }

    private boolean validate(ProductStreamItem streamItem) throws Exception {
        if (ADD == streamItem.getActionCode() && notAllowCreateSystemCdes.contains(systemCde)) {
            String prodKeyInfo = ProductKeyUtils.buildProdKeyInfo(streamItem.getImportProduct());
            log.error(String.format("Can not find origin product%s", prodKeyInfo));
            return false;
        }

        Document importProd = streamItem.getImportProduct();
        List<Document> undlStockList = JsonPathUtils.readValue(importProd, "eqtyLinkInvst.undlStock");

        if (CollectionUtils.isNotEmpty(undlStockList)) {
            Map<String, Document> instmUndlCdeMap = undlStockList.stream()
                    .collect(Collectors.toMap(undlStock -> undlStock.getString("instmUndlCde"), undlStock -> undlStock));
            Criteria criteria = new Criteria()
                    .and(Field.ctryRecCde).is(ctryRecCde)
                    .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                    .and(Field.prodAltPrimNum).in(instmUndlCdeMap.keySet())
                    .and(Field.prodTypeCde).in(undelProdtypes)
                    .and(Field.prodStatCde).ne(DELISTED);
            List<Document> undlProductList = productService.productByFilters(criteria.getCriteriaObject());
            Map<String, Object> undlProductMap = undlProductList.stream()
                    .collect(Collectors.toMap(prod -> prod.getString(Field.prodAltPrimNum), prod -> prod.get(Field.prodId)));
            for (Map.Entry<String, Document> entry : instmUndlCdeMap.entrySet()) {
                Document undlStock = entry.getValue();
                Object prodIdUndlInstm = undlProductMap.get(entry.getKey());
                undlStock.put("prodIdUndlInstm", prodIdUndlInstm);
                if (null == prodIdUndlInstm) {
                    log.error("Product{} can not find any underlying product type code: {}!", ProductKeyUtils.buildProdKeyInfo(importProd), entry.getKey());
                    return false;
                }
            }
        }
        return true;
    }

    private void systemFormat(ProductStreamItem streamItem) {
        if (!formatSystemCdes.contains(systemCde)) {
            return;
        }

        Document importProd = streamItem.getImportProduct();
        String cptlGurntProdInd = importProd.getString(Field.cptlGurntProdInd);

        if (UPDATE == streamItem.getActionCode()) {
            Document originalProd = streamItem.getOriginalProduct();

            if (StringUtils.isBlank(importProd.getString(Field.prodStatCde)) || StringUtils.equals(PENDING, originalProd.getString(Field.prodStatCde))) {
                importProd.put(Field.prodStatCde, originalProd.getString(Field.prodStatCde));
                String[] chanlAttrFields = new String[]{
                        originalProd.getString(Field.allowBuyProdInd),
                        originalProd.getString(Field.allowSellProdInd),
                        originalProd.getString(Field.allowSellMipProdInd),
                        originalProd.getString(Field.allowSwInProdInd),
                        originalProd.getString(Field.allowSwOutProdInd)};
                setChanlAttrFields(importProd, chanlAttrFields);
            }
            cptlGurntProdInd = originalProd.getString(Field.cptlGurntProdInd);
        }

        if (statCdeChanlAttrFieldsMap.containsKey(importProd.getString(Field.prodStatCde))) {
            setChanlAttrFields(importProd, statCdeChanlAttrFieldsMap.get(importProd.getString(Field.prodStatCde)));
        }

        String allowSellProdInd = importProd.getString(Field.allowSellProdInd);
        if (INDICATOR_YES.equals(allowSellProdInd) && INDICATOR_NO.equals(cptlGurntProdInd)) {
            importProd.put(Field.mrkToMktInd, INDICATOR_YES);
        } else {
            importProd.put(Field.mrkToMktInd, INDICATOR_NO);
        }
    }

    private void setChanlAttrFields(Document prod, String[] chanlAttrFields) {
        if (chanlAttrFields.length == 5) {
            prod.put(Field.allowBuyProdInd, chanlAttrFields[0]);
            prod.put(Field.allowSellProdInd, chanlAttrFields[1]);
            prod.put(Field.allowSellMipProdInd, chanlAttrFields[2]);
            prod.put(Field.allowSwInProdInd, chanlAttrFields[3]);
            prod.put(Field.allowSwOutProdInd, chanlAttrFields[4]);
        }
    }
}
