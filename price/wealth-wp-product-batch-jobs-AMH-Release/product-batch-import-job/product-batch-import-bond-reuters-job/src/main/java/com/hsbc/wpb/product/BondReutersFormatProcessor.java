package com.dummy.wpb.product;


import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductFormatService;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.*;

public class BondReutersFormatProcessor implements ItemProcessor<BondReutersStreamItem, BondReutersStreamItem> {

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Autowired
    private SystemUpdateConfigHolder systemUpdateConfigHolder;

    @Autowired
    ProductFormatService productFormatService;

    @Override
    public BondReutersStreamItem process(BondReutersStreamItem productStreamItem) {
        convertUpdatedProd(productStreamItem);
        Document origProd = productStreamItem.getOriginalProduct();
        Document updatedProd = productStreamItem.getUpdatedProduct();

        formatFromOrigProd(origProd, updatedProd);
        formatFromTermsheet(origProd, updatedProd);
        formatCouponDate(origProd, updatedProd);

        return productStreamItem;
    }

    private void convertUpdatedProd(ProductStreamItem streamItem) {
        List<String> updateAttrs = systemUpdateConfigHolder.getUpdateAttrs(systemCde, CollectionName.product.name());
        Document importProduct = streamItem.getImportProduct();
        Document updatedProduct = JsonUtil.deepCopy(streamItem.getOriginalProduct());
        productFormatService.formatByUpdateAttrs(importProduct, updatedProduct, updateAttrs);
        streamItem.setUpdatedProduct(updatedProduct);
    }

    private void formatCouponDate(Document origProd, Document updatedProd) {
        Map<String, Object> updatedDebtInstm = updatedProd.get(Field.debtInstm, new LinkedHashMap<>());
        Map<String, Object> origDebtInstm = origProd.get(Field.debtInstm, new LinkedHashMap<>());

        String pymtCoupnNextDt = (String) updatedDebtInstm.get(Field.pymtCoupnNextDt);
        if (StringUtils.isNotBlank(pymtCoupnNextDt)) {
            String histPymtCoupnNextDt = (String) origDebtInstm.get(Field.pymtCoupnNextDt);

            if (histPymtCoupnNextDt != null) {
                if (Date.valueOf(pymtCoupnNextDt).after(Date.valueOf(histPymtCoupnNextDt))) {
                    updatedDebtInstm.put(Field.coupnPrevRate, origDebtInstm.get(Field.coupnPrevRate));
                    updatedDebtInstm.put(Field.coupnCurrStartDt, origDebtInstm.get(Field.pymtCoupnNextDt));
                    updatedDebtInstm.put(Field.coupnPrevStartDt, origDebtInstm.get(Field.coupnCurrStartDt));
                } else {
                    updatedDebtInstm.put(Field.coupnCurrStartDt, origDebtInstm.get(Field.coupnCurrStartDt));
                    updatedDebtInstm.put(Field.coupnPrevStartDt, origDebtInstm.get(Field.coupnPrevStartDt));
                    updatedDebtInstm.put(Field.pymtCoupnNextDt, origDebtInstm.get(Field.pymtCoupnNextDt));
                    updatedDebtInstm.put(Field.coupnPrevRate, origDebtInstm.get(Field.coupnPrevRate));
                    updatedDebtInstm.put(Field.coupnAnnlRate, origDebtInstm.get(Field.coupnAnnlRate));
                    updatedDebtInstm.put(Field.coupnAnnlText, origDebtInstm.get(Field.coupnAnnlText));
                }
            }
        } else {
            updatedDebtInstm.put(Field.pymtCoupnNextDt, origDebtInstm.get(Field.pymtCoupnNextDt));
        }

        if (ObjectUtils.isEmpty(updatedDebtInstm.get(Field.pdcyCoupnPymtCde))) {
            updatedDebtInstm.put(Field.pdcyCoupnPymtCde, origDebtInstm.get(Field.pdcyCoupnPymtCde));
        }

        Number coupnAnnlRate = (Number) updatedDebtInstm.get(Field.coupnAnnlRate);
        if (null == coupnAnnlRate || StringUtils.equals(coupnAnnlRate.toString(), "0")) {
            updatedDebtInstm.put(Field.coupnAnnlRate, origDebtInstm.get(Field.coupnAnnlRate));
        }

        if (ObjectUtils.isEmpty(updatedDebtInstm.get(Field.coupnAnnlText))) {
            updatedDebtInstm.put(Field.coupnAnnlText, origDebtInstm.get(Field.coupnAnnlText));
        }
    }

    private void formatFromTermsheet(Document origProd, Document updatedProd) {
        //change product status code P-->A
        if (JsonPathUtils.readValue(origProd, Field.prodStatCde).equals(BatchConstants.PENDING)) {
            JsonPathUtils.setValue(updatedProd, Field.prodStatCde, BatchConstants.ACTIVE);
            JsonPathUtils.setValue(updatedProd, Field.prodStatUpdtDtTm, OffsetDateTime.now());
        } else {
            JsonPathUtils.setValue(updatedProd, Field.prodStatCde, JsonPathUtils.readValue(origProd, Field.prodStatCde));
        }

        JsonPathUtils.setValue(updatedProd, Field.prodId, JsonPathUtils.readValue(origProd, Field.prodId));

    }

    private void formatFromOrigProd(Document origProd, Document updatedProd) {
        Map<String, Object> origDebtInstm = origProd.get(Field.debtInstm, new HashMap<>());
        Map<String, Object> updatedDebtInstm = updatedProd.get(Field.debtInstm, new HashMap<>());

        JsonPathUtils.copyValue(origDebtInstm, updatedDebtInstm, Field.isrBondName, false);
        JsonPathUtils.copyValue(origDebtInstm, updatedDebtInstm, Field.prodBodLotQtyCnt, false);
        JsonPathUtils.copyValue(origDebtInstm, updatedDebtInstm, Field.grntrName, false);
        JsonPathUtils.setValueIfAbsent(updatedDebtInstm, Field.prodIssDt, JsonPathUtils.readValue(origProd, Field.debtInstm + "." + Field.prodIssDt));
    }
}
