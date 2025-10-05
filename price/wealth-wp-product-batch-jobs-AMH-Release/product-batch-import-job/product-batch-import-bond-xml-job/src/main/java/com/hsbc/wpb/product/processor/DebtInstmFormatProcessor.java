package com.dummy.wpb.product.processor;

import com.dummy.wpb.product.batch.ProductSubtpCdeHolder;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.utils.JsonPathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static com.dummy.wpb.product.constant.BatchImportAction.ADD;
import static com.dummy.wpb.product.constant.BatchImportAction.UPDATE;
import static com.dummy.wpb.product.utils.CommonUtils.isEmptyList;
import static org.apache.commons.lang3.StringUtils.EMPTY;


@Slf4j
public class DebtInstmFormatProcessor implements ItemProcessor<ProductStreamItem, ProductStreamItem> {

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    @Value("#{'${batch.format.allow}'.split(',')}")
    private List<String> formatSystemCodes;

    private static final String CTRY_BOND_ISSUE_CDE = "debtInstm.ctryBondIssueCde";
    private static final String MTUR_EXT_DT = "debtInstm.mturExtDt";

    @Override
    public ProductStreamItem process(ProductStreamItem streamItem) {
        if (shouldSkip(streamItem)) return null;

        BatchImportAction actionCode = streamItem.getActionCode();
        if (systemCde.equals("RBT")) {
            Document importProd = streamItem.getImportProduct();
            importProd.put(Field.prodSubtpCde, ProductSubtpCdeHolder.getProdSubtpCde(importProd.getString(Field.prodSubtpCde)));
        }

        format(streamItem);

        if (ADD == actionCode) {
            addProd(streamItem);
        } else if (UPDATE == actionCode){
            updateProd(streamItem);
        }

        return streamItem;
    }

    /**
     * Product Item Filter rules
     * - MDSBE only update the existed product
     * - RBT(MSS) can not process the product which algo indicator is N
     *
     * @param streamItem product item
     * @return true - skip, false - continue the process flow
     */
    private boolean shouldSkip(ProductStreamItem streamItem) {
        BatchImportAction actionCode = streamItem.getActionCode();
        String algoIndicator = ADD == actionCode ?
                JsonPathUtils.readValue(streamItem.getImportProduct(), "debtInstm.isAlgoInd") :
                JsonPathUtils.readValue(streamItem.getOriginalProduct(), "debtInstm.isAlgoInd");
        return (systemCde.equals("MDSBE") && ADD == actionCode) || (systemCde.equals("RBT") && INDICATOR_NO.equals(algoIndicator));
    }

    private void addProd(ProductStreamItem streamItem) {
        Document importProduct = streamItem.getImportProduct();

        JsonPathUtils.setValueIfAbsent(importProduct, "debtInstm.prodIssDt", LocalDate.now().toString());
        JsonPathUtils.setValueIfAbsent(importProduct, Field.prodLnchDt, LocalDate.now().toString());
        JsonPathUtils.setValueIfAbsent(importProduct, Field.prodMturDt, JsonPathUtils.readValue(importProduct, MTUR_EXT_DT));

        String ccyInvstCde = importProduct.getString(Field.ccyInvstCde);
        JsonPathUtils.setValueIfAbsent(importProduct, Field.ccyProdCde, ccyInvstCde);

        if (systemCde.equals(RBT)) {
            addFormatForRBT(importProduct);
        }

        String ctryBondIssueCde = JsonPathUtils.readValue(importProduct, CTRY_BOND_ISSUE_CDE);
        if (StringUtils.equalsAny(ctryBondIssueCde, "01", "02")) {
            JsonPathUtils.setValue(importProduct, CTRY_BOND_ISSUE_CDE, "OT");
        }
        if (StringUtils.startsWith(importProduct.getString(Field.prodAltPrimNum), "XS")) {
            importProduct.put(Field.prodLocCde, "EOC");
        }

        BigDecimal prodBodLotQtyCnt = JsonPathUtils.readValue(importProduct, "debtInstm.prodBodLotQtyCnt");
        if (null != prodBodLotQtyCnt) {
            JsonPathUtils.setValue(importProduct, "debtInstm.prodBodLotQtyCnt", prodBodLotQtyCnt.divide(new BigDecimal(1000000), 6, RoundingMode.DOWN));
        }

        if (StringUtils.isNotBlank(ccyInvstCde)) {
            importProduct.compute(Field.tradeCcy,
                    (key, tradeCcy) -> ObjectUtils.isEmpty(tradeCcy) ? Collections.singletonMap(Field.ccyProdTradeCde, ccyInvstCde) : tradeCcy);
        }

    }

    private void addFormatForRBT(Document importProduct) {
        String isrBndNme = JsonPathUtils.readValue(importProduct, "debtInstm.isrBondName");
        LocalDate mturExtDt = JsonPathUtils.readValue(importProduct, MTUR_EXT_DT);
        String ccyProdCde = importProduct.getString(Field.ccyProdCde);
        String coupnAnnlText = formatDecimalPoint(JsonPathUtils.readValue(importProduct, "debtInstm.coupnAnnlText"));
        importProduct.put(Field.prodName, StringUtils.joinWith(" ", isrBndNme, Objects.toString(mturExtDt, EMPTY), ccyProdCde, coupnAnnlText));

        LocalDate prodMturDt = importProduct.get(Field.prodMturDt, LocalDate.class);
        if (dummy.equals(grpMembrRecCde) && prodMturDt != null) {
            long termRemainDayCnt = Math.max(0L, prodMturDt.toEpochDay() - LocalDate.now().toEpochDay());
            importProduct.put(Field.termRemainDayCnt, termRemainDayCnt);
        }

        if (StringUtils.equals(importProduct.getString(Field.prodSubtpCde), "CD")) {
            JsonPathUtils.setValue(importProduct, "debtInstm.bondIndusGrp", "Fin");
        }
    }

    private void format(ProductStreamItem streamItem) {
        if (!formatSystemCodes.contains(systemCde)) {
            return;
        }

        Document importProd = streamItem.getImportProduct();
        Document originalProd = streamItem.getOriginalProduct();
        BatchImportAction actionCode = streamItem.getActionCode();

        Object mturExtDt = JsonPathUtils.readValue(importProd, MTUR_EXT_DT);
        if (ADD == actionCode) {

            importProd.put(Field.prodStatCde, ACTIVE);
            importProd.put(Field.allowSellMipProdInd, INDICATOR_NO);
            importProd.put(Field.allowSwInProdInd, INDICATOR_NO);
            importProd.put(Field.allowSwOutProdInd, INDICATOR_NO);
            importProd.putIfAbsent(Field.prodMturDt, mturExtDt);

        } else if (UPDATE == actionCode) {

            if (StringUtils.equalsAny(originalProd.getString(Field.prodStatCde), EXPIRED, ACTIVE)) {
                importProd.put(Field.prodStatCde, ACTIVE);
                importProd.put(Field.allowSellMipProdInd, INDICATOR_NO);
                importProd.put(Field.allowSwInProdInd, INDICATOR_NO);
                importProd.put(Field.allowSwOutProdInd, INDICATOR_NO);
            } else {
                importProd.put(Field.prodStatCde, originalProd.getString(Field.prodStatCde));
                importProd.put(Field.allowSellMipProdInd, originalProd.getString(Field.allowSellMipProdInd));
                importProd.put(Field.allowSwInProdInd, originalProd.getString(Field.allowSwInProdInd));
                importProd.put(Field.allowSwOutProdInd, originalProd.getString(Field.allowSwOutProdInd));
            }

            if (null != mturExtDt) {
                importProd.put(Field.prodMturDt, mturExtDt);
            }

            if (RBT.equals(systemCde)
                    && HBAP.equals(grpMembrRecCde)
                    && !StringUtils.isBlank(JsonPathUtils.readValue(originalProd, "debtInstm.cmbOnlyInd"))) {
                List<Document> importExtendFields = (List<Document>) importProd.computeIfAbsent("ext", key -> new ArrayList<>());
                importExtendFields.add(new Document("fieldCde", "cmbOnlyInd").append("fieldValue", INDICATOR_NO));
            }
        }
        String ctryBondIssueCde = JsonPathUtils.readValue(importProd, CTRY_BOND_ISSUE_CDE);
        if (StringUtils.equalsAny(ctryBondIssueCde, "01", "02")) {
            JsonPathUtils.setValue(importProd, CTRY_BOND_ISSUE_CDE, "OT");
        }

    }

    private void updateProd(ProductStreamItem streamItem) {
        if (systemCde.equals("MDSBE")) updateYieldHistoryList(streamItem);
    }

    private void updateYieldHistoryList(ProductStreamItem streamItem) {
        Document importedBondInfo = streamItem.getImportProduct().get(Field.debtInstm, Document.class);
        if(isEmptyList((List<?>) importedBondInfo.get(Field.yieldHist))) return;

        Document imprtedYieldHist = importedBondInfo.getList(Field.yieldHist, Document.class).get(0);
        importedBondInfo.put(Field.yieldDt, imprtedYieldHist.get(Field.yieldDt));
        importedBondInfo.put(Field.yieldEffDt, imprtedYieldHist.get(Field.yieldEffDt));
        importedBondInfo.put(Field.yieldBidPct, imprtedYieldHist.get(Field.yieldBidPct));
        importedBondInfo.put(Field.yieldOfferPct, imprtedYieldHist.get(Field.yieldOfferPct));
    }

    private String formatDecimalPoint(String decimalValue) {
        if (StringUtils.isNotBlank(decimalValue)) {
            String number = decimalValue;
            if (number.contains(".")) {
                String integer = number.substring(0, number.indexOf("."));
                String decimal = number.substring(number.indexOf(".") + 1, number.length() - 1);
                if (decimal.length() > 3) {
                    decimal = decimal.substring(decimal.indexOf(".") + 1, decimal.indexOf(".") + 4);
                }
                number = integer + "." + decimal + "%";
            }
            return number;
        } else {
            return null;
        }
    }

}
