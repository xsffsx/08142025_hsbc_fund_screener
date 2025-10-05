package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

@Slf4j
@Service
public class PriceCompareService {

    @Autowired
    private ProductService productService;

    private static final String REPORT_TYPE_STALE = "STL";
    
    private static final String PRICE_TYPE_BID = "BID";

    private static final String PRICE_TYPE_OFFR = "OFFR";

    private static final String PRICE_TYPE_NAV = "NAV";

    private static final String PRICE_TYPE_MKT = "MKT";

    /**
     * Handle price compare result update from WPS. Only updates stale price records (rptProdPrcTypeCde = STL). 
     * If any of price fields has been changed, then for each stale price record:  
     * (1) Reset counter to 0 
     * (2) Update product price amount to the latest value
     * @implNote https://wpb-confluence.systems.uk.dummy/display/WWS/PROD_PRC_CMPAR+update+logic
     * @param newProd
     * @return 
     */
    public Document updateStlPrcCmparFromWps(Document newProd) {
        Date startTime = new Date();
        log.debug("Update stale price compare records started");
        Long prodId = newProd.getLong(Field.prodId);
        if (null != prodId) {
            Document oldProd = productService.findFirst(eq(Field.prodId, prodId));
            if (null != oldProd) {
                LocalDate oldPrcEffDt = DocumentUtils.getLocalDate(oldProd, Field.prcEffDt);
                LocalDate newPrcEffDt = DocumentUtils.getLocalDate(newProd, Field.prcEffDt);
                List<Map<String, Object>> prcCmparList = (List<Map<String, Object>>) oldProd.get(Field.prcCmpar);
                if (checkPrcEffDt(oldPrcEffDt, newPrcEffDt) && !CollectionUtils.isEmpty(prcCmparList)) {
                    checkPriceUpdate(newProd, oldProd, prcCmparList);
                }
            }
        }
        Date endTime = new Date();
        log.debug("Update stale price compare records completed, time = " + (endTime.getTime() - startTime.getTime()) + "ms");
        return newProd;
    }

    private void checkPriceUpdate(Document newProd, Document oldProd, List<Map<String, Object>> prcCmparList) {
        boolean priceUpdated = false;
        // check price changes for any of below 4 price fields
        if (checkPriceChange(oldProd, newProd, Field.prodBidPrcAmt)) {
            priceUpdated = true;
            resetCounter(prcCmparList, REPORT_TYPE_STALE, PRICE_TYPE_BID);
        }
        if (checkPriceChange(oldProd, newProd, Field.prodOffrPrcAmt)) {
            priceUpdated = true;
            resetCounter(prcCmparList, REPORT_TYPE_STALE, PRICE_TYPE_OFFR);
        }
        if (checkPriceChange(oldProd, newProd, Field.prodNavPrcAmt)) {
            priceUpdated = true;
            resetCounter(prcCmparList, REPORT_TYPE_STALE, PRICE_TYPE_NAV);
        }
        if (checkPriceChange(oldProd, newProd, Field.prodMktPrcAmt)) {
            priceUpdated = true;
            resetCounter(prcCmparList, REPORT_TYPE_STALE, PRICE_TYPE_MKT);
            updateProdMktPricePrevAmt(oldProd, newProd);
        }
        if (priceUpdated) {
            updatePriceFields(newProd, prcCmparList, REPORT_TYPE_STALE);
            newProd.put(Field.prcCmpar, prcCmparList);
        }
    }

    /**
     * Check price effective date
     * @param oldPrcEffDt
     * @param newPrcEffDt 
     * @return <code>true</code> if newPrcEffDt >= oldPrcEffDt, or either date is null
     */
    private boolean checkPrcEffDt(LocalDate oldPrcEffDt, LocalDate newPrcEffDt) {
        boolean b = false;
        if (null == oldPrcEffDt
                || null == newPrcEffDt
                || newPrcEffDt.compareTo(oldPrcEffDt) >= 0) {
            b = true;
        }
        return b;
    }

    /**
     * Check price change
     * @param oldProd 
     * @param newProd 
     * @param priceField prodBidPrcAmt, prodNavPrcAmt, prodOffrPrcAmt, prodMktPrcAmt
     * @return <code>true</code> if oldPrice != newPrice; 
     * <code>false</code> if oldPrice = newPrice or both price values are null
     */
    private boolean checkPriceChange(Document oldProd, Document newProd, String priceField) {
        BigDecimal oldPrice = DocumentUtils.getBigDecimal(oldProd, priceField);
        BigDecimal newPrice = DocumentUtils.getBigDecimal(newProd, priceField);
        boolean b;
        if (null == oldPrice && null == newPrice) {
            b = false;
        } else if (null != oldPrice && null != newPrice) {
            b = !oldPrice.equals(newPrice);
        } else {
            b = true;
        }
        return b;
    }

    /**
     * Reset price checking counter if price value is changed
     * @param prcCmparList price compare list
     * @param rptTypeCde report type code
     * @param prodPrcTypeCde price type code
     */
    private void resetCounter(List<Map<String, Object>> prcCmparList, String rptTypeCde, String prodPrcTypeCde) {
        prcCmparList.forEach(prcCmpar -> {
            if (rptTypeCde.equalsIgnoreCase((String) prcCmpar.get(Field.rptProdPrcTypeCde))
                   && prodPrcTypeCde.equalsIgnoreCase((String) prcCmpar.get(Field.prodPrcTypeCde))) {
               prcCmpar.put(Field.prcChkngIntrnCnt, 0);
            }
        });
    }

    /**
     * Update price & price effective date in price compare
     * @param newProd
     * @param prcCmparList
     * @param rptTypeCde
     * @return
     */
    private void updatePriceFields(Document newProd, List<Map<String, Object>> prcCmparList, String rptTypeCde) {
        prcCmparList.forEach(prcCmpar -> {
            if (rptTypeCde.equalsIgnoreCase((String) prcCmpar.get(Field.rptProdPrcTypeCde))) {
                prcCmpar.put(Field.prcEffDt, DocumentUtils.getLocalDate(newProd, Field.prcEffDt));
                prcCmpar.put(Field.prodBidPrcAmt, newProd.getDouble(Field.prodBidPrcAmt));
                prcCmpar.put(Field.prodOffrPrcAmt, newProd.getDouble(Field.prodOffrPrcAmt));
                prcCmpar.put(Field.prodNavPrcAmt, newProd.getDouble(Field.prodNavPrcAmt));
                prcCmpar.put(Field.prodMktPrcAmt, newProd.getDouble(Field.prodMktPrcAmt));
                prcCmpar.put(Field.recUpdtDtTm, new Date());
            }
        });
    }

    /**
     * Update Product Market Price Previous Amount <br/>
     * If prodMktPricePrevAmt input value != DB value, trust input value <br/>
     * Else set prodMktPricePrevAmt = previous prodMktPrcAmt; <br/>
     * @param oldProd
     * @param newProd
     */
    private void updateProdMktPricePrevAmt(Document oldProd, Document newProd) {
        if (!checkPriceChange(oldProd, newProd, Field.prodMktPricePrevAmt)) {
            newProd.put(Field.prodMktPricePrevAmt, oldProd.get(Field.prodMktPrcAmt));
        }
    }
}
