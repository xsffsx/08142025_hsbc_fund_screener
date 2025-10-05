package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.utils.JsonPathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dummy.wpb.product.constant.BatchConstants.*;

@Component
@StepScope
@Slf4j
public class SecXmlFileProcessor implements ItemProcessor<ProductStreamItem, ProductStreamItem> {
    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Value("#{'${batch.format.allow:}'.split(',')}")
    private List<String> formatAllowCde;

    @Value("#{'${batch.format.not-allow:}'.split(',')}")
    private List<String> formatNotAllowCde;

    @Value("#{'${batch.altId.full-set:}'.split(',')}")
    private List<String> altIdFullSetCode;

    @Value("#{'${batch.altId.delta:}'.split(',')}")
    private List<String> altIdDeltaCode;

    @Value("#{'${batch.altId.reserved-codes:}'.split(',')}")
    private List<String> altIdReservedCode;

    @Override
    public ProductStreamItem process(ProductStreamItem streamItem) throws Exception {

        String fileName = MapUtils.getString(streamItem.getParams(), "fileName");
        Assert.state(StringUtils.isNotBlank(fileName), "Please set fileName in params of ProductStreamItem.");
        String typeCde = fileName.split("_")[3];

        if (streamItem.getActionCode() == BatchImportAction.UPDATE) {
            updateProcessor(streamItem, typeCde);
        } else if ((streamItem.getActionCode() == BatchImportAction.ADD)) {
            createProcessor(streamItem, typeCde);
        }

        return streamItem;
    }

    private void createProcessor(ProductStreamItem streamItem, String typeCde) {
        Document importProduct = streamItem.getImportProduct();
        JsonPathUtils.setValueIfAbsent(importProduct, "stockInstm.prodMktStatChngLaDt", LocalDate.now().toString());

        if (allow(typeCde, formatAllowCde, formatNotAllowCde)) {
            String prodStatCde = importProduct.getString(Field.prodStatCde);
            if (StringUtils.equals(prodStatCde, ACTIVE)) {
                importProduct.put(Field.allowBuyProdInd, "Y");
                importProduct.put(Field.allowSellProdInd, "Y");
                importProduct.put(Field.allowSellMipProdInd, "N");
                importProduct.put(Field.allowSwInProdInd, "N");
                importProduct.put(Field.allowSwOutProdInd, "N");
            } else if (StringUtils.equalsAny(prodStatCde, DELISTED, SUSPENDED)) {
                importProduct.put(Field.allowBuyProdInd, "N");
                importProduct.put(Field.allowSellProdInd, "N");
                importProduct.put(Field.allowSellMipProdInd, "N");
                importProduct.put(Field.allowSwInProdInd, "N");
                importProduct.put(Field.allowSwOutProdInd, "N");
            }
        }
    }

    private void updateProcessor(ProductStreamItem streamItem, String typeCde) {
        Document originalProd = streamItem.getOriginalProduct();
        Document importedProd = streamItem.getImportProduct();

        if (allow(typeCde, formatAllowCde, formatNotAllowCde)) {
            String prodStatCde = importedProd.getString(Field.prodStatCde);
            if (StringUtils.isBlank(prodStatCde) || StringUtils.equals(originalProd.getString(Field.prodStatCde), PENDING)) {
                importedProd.put(Field.prodStatCde, originalProd.get(Field.prodStatCde));
                importedProd.put(Field.allowBuyProdInd, originalProd.get(Field.allowBuyProdInd));
                importedProd.put(Field.allowSellProdInd, originalProd.get(Field.allowSellProdInd));
                importedProd.put(Field.allowSellMipProdInd, originalProd.get(Field.allowSellMipProdInd));
                importedProd.put(Field.allowSwInProdInd, originalProd.get(Field.allowSwInProdInd));
                importedProd.put(Field.allowSwOutProdInd, originalProd.get(Field.allowSwOutProdInd));
                return;
            }

            switch (prodStatCde) {
                case ACTIVE:
                    importedProd.put(Field.allowBuyProdInd, "Y");
                    importedProd.put(Field.allowSellProdInd, "Y");
                    importedProd.put(Field.allowSellMipProdInd, "N");
                    importedProd.put(Field.allowSwInProdInd, "N");
                    importedProd.put(Field.allowSwOutProdInd, "N");
                    break;
                case DELISTED:
                case SUSPENDED:
                    importedProd.put(Field.allowBuyProdInd, "N");
                    importedProd.put(Field.allowSellProdInd, "N");
                    importedProd.put(Field.allowSellMipProdInd, "N");
                    importedProd.put(Field.allowSwInProdInd, "N");
                    importedProd.put(Field.allowSwOutProdInd, "N");
                    break;
                default:
                    break;
            }
        }

        if (allow(typeCde, altIdFullSetCode, altIdDeltaCode)) {
            mergeReservedCodes(originalProd, importedProd);
            //cheat on ProductFormatService.formatByUpdateAttrs. Make the imported altId be full set list.
            List<Map<String,Object>> oldAltIds = streamItem.getUpdatedProduct().get(Field.altId, new ArrayList<>());
            for (int i = oldAltIds.size() - 1; i > 0; i--) {
                String path = String.format("altId[?(@.prodCdeAltClassCde=='%s')]", oldAltIds.get(i).get(Field.prodCdeAltClassCde));
                List<Map<String,Object>> newAltId = JsonPathUtils.readValue(importedProd, path);
                if (CollectionUtils.isEmpty(newAltId)) {
                    oldAltIds.remove(i);
                }
            }
        }
    }

    private boolean allow(String typeCde, List<String> allowCde, List<String> notAllowCde) {
        String systemProdTypeMarketCde = String.format("%s.%s", systemCde, typeCde);
        if (notAllowCde.stream().anyMatch(cde -> StringUtils.equalsIgnoreCase(cde, systemProdTypeMarketCde))) {
            return false;
        }

        if (allowCde.stream().anyMatch(cde -> StringUtils.equalsIgnoreCase(cde, systemProdTypeMarketCde))) {
            return true;
        }

        String prodTypeCde = StringUtils.substringBefore(typeCde, SEPARATOR_OF_PROD_TYPE_AND_MARKET);
        String systemProdTypeCde = String.format("%s.%s", systemCde, prodTypeCde);
        return allowCde.stream().anyMatch(cde -> StringUtils.equalsIgnoreCase(cde, systemProdTypeCde));
    }

    private void mergeReservedCodes(Document originalProd, Document importedProd) {
        List<Map<String, Object>> importedAltIdList = importedProd.get(Field.altId, new ArrayList<>());
        for (String prodCdeAltClassCde : altIdReservedCode) {
            String altClassCdeJsonPath = String.format("altId[?(@.prodCdeAltClassCde=='%s')]", prodCdeAltClassCde);
            List<Map<String, Object>> altId = JsonPathUtils.readValue(originalProd, altClassCdeJsonPath, new ArrayList<>());
            importedAltIdList.addAll(altId);
        }
    }

}
