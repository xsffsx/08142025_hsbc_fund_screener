package com.dummy.wpb.product.processor;

import com.dummy.wpb.product.model.ProductPriceStreamItem;
import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.helper.DateHelper;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.ProductPriceHistory;
import com.dummy.wpb.product.service.ProductFormatService;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.dummy.wpb.product.constant.BatchConstants.*;

@Order(1)
public class PriceHistoryProcessor implements ItemProcessor<ProductStreamItem, ProductPriceStreamItem> {

    @Autowired
    private SystemUpdateConfigHolder systemUpdateConfigHolder;

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Value("#{'${batch.reuters.price.history.count}'}")
    private Integer reutersPriceHistoryCount;

    @Autowired
    ProductFormatService productFormatService;

    private String[] historyUpdateAttrs = new String[]{};
    private String[] productUpdateAttrs = new String[]{};


    @PostConstruct
    private void init() {
        historyUpdateAttrs = systemUpdateConfigHolder.getUpdateAttrs(systemCde, CollectionName.prod_prc_hist.name()).toArray(new String[0]);
        productUpdateAttrs = systemUpdateConfigHolder.getUpdateAttrs(systemCde, CollectionName.product.name()).toArray(new String[0]);
    }

    @Override
    public ProductPriceStreamItem process(ProductStreamItem streamItem) throws Exception {
        ProductPriceStreamItem priceStreamItem = (ProductPriceStreamItem) streamItem;
        priceStreamItem.setUpdatedProduct(JsonUtil.deepCopy(priceStreamItem.getOriginalProduct()));
        Document importProduct = priceStreamItem.getImportProduct();

        setProductPriceField(priceStreamItem);

        String prodTypeCde = importProduct.getString(Field.prodTypeCde);
        if (EQUITY_LINKED_INVESTMENT.equals(prodTypeCde)) {
            processEliProductPrice(priceStreamItem);
        } else {
            calculateUpdatedHistory(priceStreamItem);
            setPriceHistSrceRdyStatCde(priceStreamItem);
        }

        productFormatService.formatByUpdateAttrs(importProduct, priceStreamItem.getUpdatedProduct(), Arrays.asList(productUpdateAttrs));
        return priceStreamItem;
    }


    private void processEliProductPrice(ProductPriceStreamItem priceStreamItem) {
        Document importProduct = priceStreamItem.getImportProduct();
        List<Document> importPriceHistoryList = priceStreamItem.getImportPriceHistory();
        Object xmlPrcEffDt = importPriceHistoryList.get(0).get(Field.prcEffDt);
        if (null != xmlPrcEffDt) {
            importProduct.put(Field.prcEffDt, xmlPrcEffDt);
        } else {
            xmlPrcEffDt = DateHelper.formatDate2String(new Date(), DateHelper.DEFAULT_DATE_FORMAT);
        }
        JsonPathUtils.setValue(importProduct, "eqtyLinkInvst.eliPrcEffDt", xmlPrcEffDt);
    }

    private void calculateUpdatedHistory(ProductPriceStreamItem priceStreamItem) throws IOException {
        List<ProductPriceHistory> updatedPriceHistoryList = new ArrayList<>();
        priceStreamItem.setUpdatedPriceHistory(updatedPriceHistoryList);

        if (historyUpdateAttrs.length == 0) {// that means not need to update price history
            return;
        }

        List<Document> importPriceHistoryList = priceStreamItem.getImportPriceHistory();
        List<ProductPriceHistory> originalPriceHistoryList = priceStreamItem.getOriginalPriceHistory();

        for (Document importHistory : importPriceHistoryList) {
            importHistory.put(Field.prodId, priceStreamItem.getProdId());
            importHistory.putIfAbsent(Field.prcInpDt, LocalDate.now());
            ProductPriceHistory updatedHistory = JsonUtil.convertType(importHistory, ProductPriceHistory.class);
            originalPriceHistoryList
                    .stream()
                    .filter(item -> areSameHistory(item, updatedHistory))
                    .findFirst()
                    .ifPresent(originalHistory -> BeanUtils.copyProperties(originalHistory, updatedHistory, historyUpdateAttrs));
            updatedHistory.setRecUpdtDtTm(LocalDateTime.now());
            updatedHistory.setProdId(priceStreamItem.getProdId());
            if (!originalPriceHistoryList.contains(updatedHistory)) {
                updatedPriceHistoryList.add(updatedHistory);
            }
        }
    }

    private void setProductPriceField(ProductPriceStreamItem priceStreamItem) {
        Document maxPriceHistory = Collections.max(priceStreamItem.getImportPriceHistory(), (priceHistory1, priceHistory2) ->
                ObjectUtils.compare(priceHistory1.get(Field.prcEffDt, LocalDate.class), priceHistory2.get(Field.prcEffDt, LocalDate.class))
        );

        Document importProduct = priceStreamItem.getImportProduct();
        Document originalProduct = priceStreamItem.getOriginalProduct();

        boolean priceUpdated = ObjectUtils.compare(
                Objects.toString(originalProduct.get(Field.prcEffDt), null),
                Objects.toString(maxPriceHistory.get(Field.prcEffDt), null)) <= 0;

        Document targetPrice = priceUpdated ? maxPriceHistory : originalProduct;

        importProduct.put(Field.prcEffDt, String.valueOf(targetPrice.get(Field.prcEffDt)));
        importProduct.put(Field.ccyProdMktPrcCde, targetPrice.get(Field.ccyProdMktPrcCde));
        importProduct.put(Field.prodBidPrcAmt, targetPrice.get(Field.prodBidPrcAmt));
        importProduct.put(Field.prodOffrPrcAmt, targetPrice.get(Field.prodOffrPrcAmt));
        importProduct.put(Field.prodNavPrcAmt, targetPrice.get(Field.prodNavPrcAmt));
        importProduct.put(Field.prodMktPrcAmt, targetPrice.get(Field.prodMktPrcAmt));
        importProduct.put(Field.prcInpDt, Objects.toString(targetPrice.get(Field.prcInpDt), null));

        if (StringUtils.equalsAny( originalProduct.getString(Field.prodTypeCde),  SECURITY,  WARRANT  )) {
            if (priceUpdated) {
                Document stockPrice = maxPriceHistory.get("stockPrice", Document.class);
                priceStreamItem.getImportProduct().put(Field.stockInstm, stockPrice);
            } else {
                importProduct.put(Field.stockInstm, originalProduct.get(Field.stockInstm));
            }
        }

    }

    private void setPriceHistSrceRdyStatCde(ProductPriceStreamItem priceStreamItem) {
        Document importProduct = priceStreamItem.getImportProduct();
        Document originalProduct = priceStreamItem.getOriginalProduct();
        List<Document> importPriceHistoryList = priceStreamItem.getImportPriceHistory();

        if (importPriceHistoryList.size() <= 1 && REUTERS_PRICE_STATUS_POPULATED.equals(originalProduct.get(Field.priceHistSrceRdyStatCde))){
            importProduct.put(Field.priceHistSrceRdyStatCde, originalProduct.get(Field.priceHistSrceRdyStatCde));
            return;
        }

        LocalDate historyDate = LocalDate.now().plusYears(reutersPriceHistoryCount);
        boolean greaterThanHist = ObjectUtils.compare(
                Objects.toString(importProduct.get(Field.prcEffDt), null),
                Objects.toString(historyDate)) >= 0;
        boolean hasOneNav = importPriceHistoryList.stream().anyMatch(hist -> null != hist.get(Field.prodNavPrcAmt));
        if (hasOneNav && greaterThanHist) {
            importProduct.put(Field.priceHistSrceRdyStatCde, REUTERS_PRICE_STATUS_READY);
        } else {
            importProduct.put(Field.priceHistSrceRdyStatCde, originalProduct.get(Field.priceHistSrceRdyStatCde));
        }
    }

    private boolean areSameHistory(ProductPriceHistory productPriceHistory1, ProductPriceHistory productPriceHistory2) {
        return Objects.equals(productPriceHistory1.getProdId(), productPriceHistory2.getProdId())
                && Objects.equals(productPriceHistory1.getPdcyPrcCde(), productPriceHistory2.getPdcyPrcCde())
                && Objects.equals(productPriceHistory1.getPrcEffDt(), productPriceHistory2.getPrcEffDt());
    }

}
