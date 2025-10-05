package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.ProductStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
public class ProductStatusUpdateProcessor implements ItemProcessor<Document,Document> {

    private static final String ELI_SUBTYPE_DCDC = "ELI_DCDC";

    @Override
    public Document process(Document product) {
        if (isProductExpired(product) && isNotTheSameValve(product)) {
            return product;
        }
        return null;
    }

    public boolean isProductExpired(Document product) {
        boolean isExpired = false;
        String prodTypeCde = product.getString(Field.prodTypeCde);
        switch (prodTypeCde) {
            case "BOND":
            case "SEC":
            case "WRTS":
            case "UT":
                isExpired = isCommonProductExpired(product);
                break;
            case "DPS":
                isExpired = isDpsProductExpired(product);
                break;
            case "ELI":
                isExpired = isEliProductExpired(product);
                break;
            case "SID":
                isExpired = isSidProductExpired(product);
                break;
            default:
                log.info("Product has been skipped (ctryRecCde: {}, grpMembrRecCde: {}, prodTypeCde: {}, prodAltPrimNum: {})",
                        product.getString(Field.ctryRecCde),
                        product.getString(Field.grpMembrRecCde),
                        prodTypeCde,
                        product.getString(Field.prodAltPrimNum));
                break;
        }
        return isExpired;
    }

    /**
     * BOND, SEC, WRTS, UT: Maturity Date < Current Date
     */
    private boolean isCommonProductExpired(Document product) {
        return isDateExpired(product.getString(Field.prodMturDt));
    }

    /**
     * DPS: Launch Date < Current Date
     */
    private boolean isDpsProductExpired(Document product) {
        return isDateExpired(product.getString(Field.prodLnchDt));
    }

    /**
     * ELI DCDC & product launch status = Y: Maturity Date < Current Date <br/>
     * ELI DCDC & product launch status = N: Trade Date < Current Date <br/>
     * ELI others: Settlement Date < Current Date <br/>
     */
    private boolean isEliProductExpired(Document product) {
        boolean isExpired = false;
        Map<String,Object> eqtyLinkInvst = product.get(Field.eqtyLinkInvst, Map.class);
        if (eqtyLinkInvst != null) {
            String prodSubtpCde = product.getString(Field.prodSubtpCde);
            if (StringUtils.containsIgnoreCase(prodSubtpCde, ELI_SUBTYPE_DCDC)) {
                if (BatchConstants.INDICATOR_YES.equals(eqtyLinkInvst.get(Field.lnchProdInd))
                        && StringUtils.isNotBlank(product.getString(Field.prodMturDt))) {
                    // ELI DCDC, product launch status = Y and Maturity Date < today
                    isExpired = isDateExpired(product.getString(Field.prodMturDt));
                } else if (BatchConstants.INDICATOR_NO.equals(eqtyLinkInvst.get(Field.lnchProdInd))
                        && eqtyLinkInvst.get(Field.trdDt) != null) {
                    // ELI DCDC, product launch status = N and Trade Date < today
                    isExpired = isDateExpired((String) eqtyLinkInvst.get(Field.trdDt));
                }
            } else if (StringUtils.isNotBlank(prodSubtpCde) && eqtyLinkInvst.get(Field.setlDt) != null) {
                // ELI others, Settlement Date < today
                isExpired = isDateExpired((String) eqtyLinkInvst.get(Field.setlDt));
            }
        }
        return isExpired;
    }

    /**
     * SID: Marketing Period End Date < Current Date
     */
    private boolean isSidProductExpired(Document product) {
        boolean isExpired = false;
        if (product.get(Field.strucInvstDepst) != null) {
            Map<String,Object> strucInvstDepst = product.get(Field.strucInvstDepst, Map.class);
            if (strucInvstDepst.get(Field.mktEndDt) != null) {
                isExpired = isDateExpired((String) strucInvstDepst.get(Field.mktEndDt));
            }
        }
        return isExpired;
    }

    /**
     * Check if the date is expired
     */
    private boolean isDateExpired(String dateStr) {
        boolean isExpired = false;
        if (StringUtils.isNotBlank(dateStr)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date currentDate = new Date();
                Date date = simpleDateFormat.parse(dateStr);
                // expired if check date < current date
                if (currentDate.after(date) && !DateUtils.isSameDay(currentDate, date)) {
                    isExpired = true;
                }
            } catch (ParseException e) {
                log.error("Failed to parse date {}", dateStr);
            }
        }
        return isExpired;
    }

    /**
     * compare the current values and expected values,if any value is not tha same, and update
     * @param product
     * @return
     */
    private boolean isNotTheSameValve(Document product){
        return !ProductStatus.E.name().equals(product.getString(Field.prodStatCde))
                || !BatchConstants.INDICATOR_NO.equals(product.getString(Field.allowBuyProdInd))
                || !BatchConstants.INDICATOR_NO.equals(product.getString(Field.allowSellProdInd))
                || !BatchConstants.INDICATOR_NO.equals(product.getString(Field.allowSwInProdInd))
                || !BatchConstants.INDICATOR_NO.equals(product.getString(Field.allowSwOutProdInd))
                || !BatchConstants.INDICATOR_NO.equals(product.getString(Field.allowSellMipProdInd));
    }
}
