package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.xml.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExportGoldXmlJobService {

    /**
     * Get Digital Asset Currency
     */
    public DigitalAssetCurrency getDigitalAssetCurrency(Document product) {
        DigitalAssetCurrency digtlAssetCcy = new DigitalAssetCurrency();
        //prodKeySeg
        digtlAssetCcy.setProdKeySeg(this.getProdKeySeg(product));
        //prodAltNumSeg
        digtlAssetCcy.setProdAltNumSeg(this.getProdAltNumSeg(product));
        //prodInfoSeg
        digtlAssetCcy.setProdInfoSeg((this.getProdInfoSeg(product)));
        //digtlAssetCcySeg
        digtlAssetCcy.setDigtlAssetCcySeg((this.getDigtlAssetCcySeg(product)));
        //recDtTmSeg
        digtlAssetCcy.setRecDtTmSeg(this.getRecDtTmSeg(product));

        return digtlAssetCcy;
    }

    /**
     * Get Product Key Segment
     */
    public ProductKeySegment getProdKeySeg(Document product) {
        ProductKeySegment prodKeySeg = new ProductKeySegment();
        prodKeySeg.setCtryRecCde(product.getString(Field.ctryRecCde));
        prodKeySeg.setGrpMembrRecCde(product.getString(Field.grpMembrRecCde));
        prodKeySeg.setProdTypeCde(product.getString(Field.prodTypeCde));
        prodKeySeg.setProdCde(product.getInteger(Field.prodId).toString());
        return prodKeySeg;
    }

    /**
     * Get Product Alternative Number Segment
     */
    public List<ProductAlternativeNumberSegment> getProdAltNumSeg(Document product) {
        List<ProductAlternativeNumberSegment> list = new ArrayList<>();
        List<Map<String, Object>> altIdList = (List<Map<String, Object>>) product.get(Field.altId);
        for (Map<String, Object> altId: altIdList) {
            ProductAlternativeNumberSegment prodAltNumSeg = new ProductAlternativeNumberSegment();
            prodAltNumSeg.setProdCdeAltClassCde((String) altId.get(Field.prodCdeAltClassCde));
            prodAltNumSeg.setProdAltNum((String) altId.get(Field.prodAltNum));
            list.add(prodAltNumSeg);
        }
        return list;
    }

    /**
     * Get Product Information Segment
     */
    public ProductInformationSegment getProdInfoSeg(Document product) {
        ProductInformationSegment prodInfoSeg = new ProductInformationSegment();
        prodInfoSeg.setProdSubtpCde(product.getString(Field.prodSubtpCde));
        prodInfoSeg.setProdName(product.getString(Field.prodName));
        prodInfoSeg.setProdShrtName(product.getString(Field.prodShrtName));
        prodInfoSeg.setProdStatCde(product.getString(Field.prodStatCde));
        prodInfoSeg.setCcyProdMktPrcCde(product.getString(Field.ccyProdMktPrcCde));
        prodInfoSeg.setCcyProdCde(product.getString(Field.ccyProdCde));
        prodInfoSeg.setAllowBuyProdInd(product.getString(Field.allowBuyProdInd));
        prodInfoSeg.setAllowSellProdInd(product.getString(Field.allowSellProdInd));
        prodInfoSeg.setAllowBuyUtProdInd(product.getString(Field.allowBuyUtProdInd));
        prodInfoSeg.setAllowBuyAmtProdInd(product.getString(Field.allowBuyAmtProdInd));
        prodInfoSeg.setAllowSellUtProdInd(product.getString(Field.allowSellUtProdInd));
        prodInfoSeg.setAllowSellAmtProdInd(product.getString(Field.allowSellAmtProdInd));
        prodInfoSeg.setAllowSellMipProdInd(product.getString(Field.allowSellMipProdInd));
        prodInfoSeg.setAllowSellMipUtProdInd(product.getString(Field.allowSellMipUtProdInd));
        prodInfoSeg.setAllowSellMipAmtProdInd(product.getString(Field.allowSellMipAmtProdInd));
        prodInfoSeg.setAllowSwInProdInd(product.getString(Field.allowSwInProdInd));
        prodInfoSeg.setAllowSwInUtProdInd(product.getString(Field.allowSwInUtProdInd));
        prodInfoSeg.setAllowSwInAmtProdInd(product.getString(Field.allowSwInAmtProdInd));
        prodInfoSeg.setAllowSwOutProdInd(product.getString(Field.allowSwOutProdInd));
        prodInfoSeg.setAllowSwOutUtProdInd(product.getString(Field.allowSwOutUtProdInd));
        prodInfoSeg.setAllowSwOutAmtProdInd(product.getString(Field.allowSwOutAmtProdInd));
        prodInfoSeg.setCptlGurntProdInd(product.getString(Field.cptlGurntProdInd));
        prodInfoSeg.setYieldEnhnProdInd(product.getString(Field.yieldEnhnProdInd));
        prodInfoSeg.setDmyProdSubtpRecInd(product.getString(Field.dmyProdSubtpRecInd));
        prodInfoSeg.setDispComProdSrchInd(product.getString(Field.dispComProdSrchInd));
        prodInfoSeg.setMrkToMktInd(product.getString(Field.mrkToMktInd));
        prodInfoSeg.setCcyInvstCde(product.getString(Field.ccyInvstCde));
        prodInfoSeg.setProdLocCde(product.getString(Field.prodLocCde));
        prodInfoSeg.setSuptRcblCashProdInd(product.getString(Field.suptRcblCashProdInd));
        prodInfoSeg.setSuptRcblScripProdInd(product.getString(Field.suptRcblScripProdInd));
        prodInfoSeg.setPldgLimitAssocAcctInd(product.getString(Field.pldgLimitAssocAcctInd));

        if (StringUtils.isNotBlank(product.getString(Field.prodLnchDt))) {
            prodInfoSeg.setProdLnchDt(LocalDate.parse(product.getString(Field.prodLnchDt)));
        }
        if (product.getInteger(Field.prtyProdSrchRsultNum) != null) {
            prodInfoSeg.setPrtyProdSrchRsultNum(product.getInteger(Field.prtyProdSrchRsultNum).longValue());
        }
        if (product.getDouble(Field.divrNum) != null) {
            prodInfoSeg.setDivrNum(BigDecimal.valueOf(product.getDouble(Field.divrNum)));
        }
        if (product.getInteger(Field.dcmlPlaceTradeUnitNum) != null) {
            prodInfoSeg.setDcmlPlaceTradeUnitNum(product.getInteger(Field.dcmlPlaceTradeUnitNum).longValue());
        }
        prodInfoSeg.setProdTradeCcySeg(this.getProdTradeCcySeg(product));
        return prodInfoSeg;
    }

    /**
     * Get Product Trade Currency Segment
     */
    public List<ProductTradeCcySegment> getProdTradeCcySeg(Document product) {
        List<ProductTradeCcySegment> list = new ArrayList<>();
        List<Map<String, Object>> tradeCcyList = (List<Map<String, Object>>) product.get(Field.tradeCcy);
        for (Map<String, Object> tradeCcy : tradeCcyList) {
            ProductTradeCcySegment prodTradeCcySeg = new ProductTradeCcySegment();
            prodTradeCcySeg.setCcyProdTradeCde((String) tradeCcy.get(Field.ccyProdTradeCde));
            list.add(prodTradeCcySeg);
        }
        
        return list;
    }

    /**
     * Get Digital Asset Currency Segment
     */
    public DigitalAssetCurrencySegment getDigtlAssetCcySeg(Document product) {
        DigitalAssetCurrencySegment digtlAssetCcySeg = new DigitalAssetCurrencySegment();
        List<MarketRateSegment> marketRateSegList = new ArrayList<>();
        Map<String, Object> digtlAssetCcy = (Map<String, Object>) product.get(Field.digtlAssetCcy);
        if (digtlAssetCcy != null) {
            digtlAssetCcySeg.setDigtlCcy((String) digtlAssetCcy.get(Field.digtlCcy));
            List<Map<String, Object>> marketRateList = (List<Map<String, Object>>) digtlAssetCcy.get(Field.marketRate);
            for (Map<String, Object> marketRate : marketRateList) {
                MarketRateSegment marketRateSeg = new MarketRateSegment();
                marketRateSeg.setCurrencyPair((String) marketRate.get(Field.currencyPair));
                marketRateSeg.setCurveType((String) marketRate.get(Field.curveType));
                marketRateSeg.setMidRate(BigDecimal.valueOf((Double) marketRate.get(Field.midRate)));
                marketRateSeg.setXpeTime(LocalDateTime.parse((String) marketRate.get(Field.xpeTime), DateTimeFormatter.ISO_DATE_TIME));
                marketRateSegList.add(marketRateSeg);
            }
            digtlAssetCcySeg.setMarketRateSeg(marketRateSegList);
        }
        return digtlAssetCcySeg;
    }

    /**
     * Get Record Date Time Segment
     */
    public RecordDateTimeSegment getRecDtTmSeg(Document product) {
        RecordDateTimeSegment recDtTmSeg = new RecordDateTimeSegment();
        recDtTmSeg.setRecCreatDtTm(LocalDateTime.parse(product.getString(Field.recCreatDtTm), DateTimeFormatter.ISO_DATE_TIME));
        recDtTmSeg.setRecUpdtDtTm(LocalDateTime.parse(product.getString(Field.recCreatDtTm), DateTimeFormatter.ISO_DATE_TIME));
        recDtTmSeg.setTimeZone("GMT+0000");
        return recDtTmSeg;
    }
}
