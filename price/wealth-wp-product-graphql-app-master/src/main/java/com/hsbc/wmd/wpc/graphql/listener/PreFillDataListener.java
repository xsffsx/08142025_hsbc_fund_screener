package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class PreFillDataListener extends BaseChangeListener{
    private static int order = 1000;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void beforeInsert(Document doc) {
        if (isValidProdPrc(doc)) {
            doc.put(Field.prcInpDt, LocalDate.now());
		}
    }

    private boolean isValidProdPrc(Document doc) {
        return ObjectUtils.allNotNull(doc.get(Field.ccyProdMktPrcCde), doc.get(Field.prcEffDt))
                && ObjectUtils.anyNotNull(doc.get(Field.prodBidPrcAmt), doc.get(Field.prodOffrPrcAmt),
                doc.get(Field.prodMktPrcAmt),doc.get(Field.prodNavPrcAmt));
	}

    @Override
    public void beforeValidation(Map<String, Object> oldProd, Map<String, Object> newProd) {
        checkPrice(oldProd,  newProd);
        checkProductStatus(oldProd, newProd);
    }

    @Override
    public Collection<String> interestJsonPaths() {
        return allInterestJsonPaths;
    }

    /**
     * The daily product price table should add the price effective date checking,
     * 1) if Source file price effective date should >= price effective date in DB, then we will update the records in product price stable, price history table update will continue.   (08-Dec-21 source file >  06Dec-21 DB)
     * 2) if the source file price effective date < price effective date in DB, the daily price update in product table should be skipped, anyway price history table update will continue . ( 06-Dec-21 source file<  08Dec-21 DB)  (Need to add this logic in product)
     * 3) refresh product price update date time if any price data changes
     * @param oldProd
     * @param newProd
     */
    private void checkPrice(Map<String, Object> oldProd, Map<String, Object> newProd) {
        LocalDate oldPrcEffDt = DocumentUtils.getLocalDate(oldProd, Field.prcEffDt);
        LocalDate newPrcEffDt = DocumentUtils.getLocalDate(newProd, Field.prcEffDt);

        LocalDate oldPrcInpDt = DocumentUtils.getLocalDate(oldProd, Field.prcInpDt);
        LocalDate newPrcInpDt = DocumentUtils.getLocalDate(newProd, Field.prcInpDt);

        String oldCcyProdMktPrcCde = DocumentUtils.getString(oldProd, Field.ccyProdMktPrcCde);
        String newCcyProdMktPrcCde = DocumentUtils.getString(newProd, Field.ccyProdMktPrcCde);

        BigDecimal oldProdBidPrcAmt = DocumentUtils.getBigDecimal(oldProd, Field.prodBidPrcAmt);
        BigDecimal newProdBidPrcAmt = DocumentUtils.getBigDecimal(newProd, Field.prodBidPrcAmt);

        BigDecimal oldProdOffrPrcAmt = DocumentUtils.getBigDecimal(oldProd, Field.prodOffrPrcAmt);
        BigDecimal newProdOffrPrcAmt = DocumentUtils.getBigDecimal(newProd, Field.prodOffrPrcAmt);

        BigDecimal oldProdMktPrcAmt = DocumentUtils.getBigDecimal(oldProd, Field.prodMktPrcAmt);
        BigDecimal newProdMktPrcAmt = DocumentUtils.getBigDecimal(newProd, Field.prodMktPrcAmt);

        BigDecimal oldProdNavPrcAmt = DocumentUtils.getBigDecimal(oldProd, Field.prodNavPrcAmt);
        BigDecimal newProdNavPrcAmt = DocumentUtils.getBigDecimal(newProd, Field.prodNavPrcAmt);

        boolean isPriceUpdated = true;
        if(null != oldPrcEffDt && null != newPrcEffDt && newPrcEffDt.isBefore(oldPrcEffDt)) {
            // set the fields to old values to ignore them
            newProd.put(Field.prcEffDt, oldProd.get(Field.prcEffDt));
            newProd.put(Field.prcInpDt, oldProd.get(Field.prcInpDt));
            newProd.put(Field.ccyProdMktPrcCde, oldProd.get(Field.ccyProdMktPrcCde));
            newProd.put(Field.prodBidPrcAmt, oldProd.get(Field.prodBidPrcAmt));
            newProd.put(Field.prodOffrPrcAmt, oldProd.get(Field.prodOffrPrcAmt));
            newProd.put(Field.prodMktPrcAmt, oldProd.get(Field.prodMktPrcAmt));
            newProd.put(Field.prodNavPrcAmt, oldProd.get(Field.prodNavPrcAmt));
            newProd.put(Field.prodPrcUpdtDtTm, oldProd.get(Field.prodPrcUpdtDtTm));
            isPriceUpdated = false;
        } else if (checkEqualDate(oldPrcEffDt, newPrcEffDt)
                && checkEqualDate(oldPrcInpDt, newPrcInpDt)
                && StringUtils.equals(oldCcyProdMktPrcCde, newCcyProdMktPrcCde)
                && checkEqualPriceAmount(oldProdBidPrcAmt, newProdBidPrcAmt)
                && checkEqualPriceAmount(oldProdOffrPrcAmt, newProdOffrPrcAmt)
                && checkEqualPriceAmount(oldProdMktPrcAmt, newProdMktPrcAmt)
                && checkEqualPriceAmount(oldProdNavPrcAmt, newProdNavPrcAmt)) {
            isPriceUpdated = false;
        }
        // refresh product price update date time
        if (isPriceUpdated) {
            newProd.put(Field.prodPrcUpdtDtTm, new Date());
        }
    }

    /**
     * Check if 2 date values are equal
     * @param date1
     * @param date2
     * @return <code>true</code> if equal or both valuse are null
     */
    private boolean checkEqualDate(LocalDate date1, LocalDate date2) {
        boolean b;
        if (null == date1 && null == date2) {
            b = true;
        } else if (null != date1 && null != date2) {
            b = date1.isEqual(date2);
        } else {
            b = false;
        }
        return b;
    }

    /**
     * Check if 2 price amounts are equal
     * @param prcAmt1
     * @param prcAmt2
     * @return <code>true</code> if equal or both values are null
     */
    private boolean checkEqualPriceAmount(BigDecimal prcAmt1, BigDecimal prcAmt2) {
        boolean b;
        if (null == prcAmt1 && null == prcAmt2) {
            b = true;
        } else if (null != prcAmt1 && null != prcAmt2) {
            b = prcAmt1.equals(prcAmt2);
        } else {
            b = false;
        }
        return b;
    }

    /**
     * Check product status code,
     * refresh product status update date time if product status code changes
     * @param oldProd
     * @param newProd
     */
    private void checkProductStatus(Map<String, Object> oldProd, Map<String, Object> newProd) {
        if (oldProd == null) {
            newProd.put(Field.prodStatUpdtDtTm, new Date());
        } else {
            String oldProdStatCde = (String) oldProd.get(Field.prodStatCde);
            String newProdStatCde = (String) newProd.get(Field.prodStatCde);
            // refresh prodStatUpdtDtTm if product status has been changed
            if (!StringUtils.equals(oldProdStatCde, newProdStatCde)) {
                newProd.put(Field.prodStatUpdtDtTm, new Date());
            }
        }
    }
}
