/*
 */
package com.dummy.wpc.datadaptor.mapper;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdPerf;
import com.dummy.wpc.batch.xml.ProdPerfmSeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.ProdPrcSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

/**
 * <p>
 * <b> This mapper will map fieldSet to java object which will be marshaled to XML. </b>
 * </p>
 */
public class Life400Mapper4MultiWriter extends AbstractFieldSetMapper {

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapLine(org
     * .springframework.batch.item.file.mapping.FieldSet)
     */
    public Object mapLine(final FieldSet fieldSet) {

        MultiWriterObj arr = new MultiWriterObj();

        ProdPerf prodPerf = getProdPerf(fieldSet);
        arr.addObj(prodPerf);

        ProdPrc prodPrc = getProdPrc(fieldSet);
        arr.addObj(prodPrc);

        return arr;
    }

    private ProdPrc getProdPrc(final FieldSet fieldSet) {

        ProdPrc prodPrc = new ProdPrc();
        prodPrc.setProdKeySeg(getProdKeySeg(fieldSet));

        ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
        prodPrcSeg.setPdcyPrcCde(ConstantsPropertiesHelper.getValue(getJobCode(), "pdcy_prc_cde"));
        prodPrcSeg.setPrcInpDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));

        String priceInfo = fieldSet.readString("PRICE_INFO");
        int ind = StringUtils.indexOf(priceInfo, "=");
        String productCode = priceInfo.substring(16, ind);
        String[] arrTmp = StringUtils.split(productCode, "_");
        String productMarketPriceCurrencyCode = arrTmp[1];
        prodPrcSeg.setCcyProdMktPrcCde(productMarketPriceCurrencyCode);
        // Get the latest price and price date of each record.
        String priceEffectiveDateTmp = priceInfo.substring(0, 8);
        String navPriceAmountTmp = priceInfo.substring(ind + 1);
        priceEffectiveDateTmp = DateHelper.formatDate2String(DateHelper.parseToDate(priceEffectiveDateTmp,
            DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT);
        navPriceAmountTmp = (StringUtils.isBlank(navPriceAmountTmp) || "N/A".equals(navPriceAmountTmp)) ? "0" : navPriceAmountTmp;
        String priceEffectiveDate = priceEffectiveDateTmp;
        String navPriceAmount = navPriceAmountTmp;
        for (int i = 1; i < 6; i++) {
            priceEffectiveDateTmp = fieldSet.readString("PRC_EFF_DT" + i);
            navPriceAmountTmp = fieldSet.readString("NAV_DLY_PRC_AMT" + i);
            if (StringUtils.isNotBlank(priceEffectiveDateTmp) && !"N/A".equals(priceEffectiveDateTmp)) {
                if (DateHelper.parseToDate(priceEffectiveDateTmp, DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH).compareTo(
                    DateHelper.parseToDate(priceEffectiveDate, DateHelper.DEFAULT_DATE_FORMAT)) > 0) {
                    priceEffectiveDate = DateHelper.formatDate2String(DateHelper.parseToDate(priceEffectiveDateTmp,
                        DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT);
                    navPriceAmount = (StringUtils.isBlank(navPriceAmountTmp) || "N/A".equals(navPriceAmountTmp)) ? "0"
                        : navPriceAmountTmp;
                }
            }
        }
        prodPrcSeg.setPrcEffDt(StringUtils.trimToEmpty(priceEffectiveDate));
        prodPrcSeg.setProdNavPrcAmt(DecimalHelper.trimZero(navPriceAmount));
        
        prodPrcSeg.setRecDtTmSeg(getRecDtTmSeg(fieldSet));

        prodPrc.addProdPrcSeg(prodPrcSeg);

        return prodPrc;
    }

    private ProdPerf getProdPerf(final FieldSet fieldSet) {

        ProdPerf prodPerf = new ProdPerf();
        prodPerf.setProdKeySeg(getProdKeySeg(fieldSet));

        ProdPerfmSeg prodPerfmSeg_PRFM = new ProdPerfmSeg();
        prodPerfmSeg_PRFM.setPerfmTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), "perfm_type_cde_p"));

        String priceInfo = fieldSet.readString("PRICE_INFO");
        String performanceCalculationDate = priceInfo.substring(8, 16);
        performanceCalculationDate = DateHelper.formatDate2String(DateHelper.parseToDate(performanceCalculationDate,
            DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT);
        prodPerfmSeg_PRFM.setPerfmCalcDt(StringUtils.trimToEmpty(performanceCalculationDate));

        prodPerfmSeg_PRFM.setPerfm6moPct(formatPerformancePercent(fieldSet.readString("PRFM_6MO_PCT")));
        prodPerfmSeg_PRFM.setPerfm1yrPct(formatPerformancePercent(fieldSet.readString("PRFM_1YR_PCT")));
        prodPerfmSeg_PRFM.setPerfm3yrPct(formatPerformancePercent(fieldSet.readString("PRFM_3YR_PCT")));
        prodPerfmSeg_PRFM.setPerfm5yrPct(formatPerformancePercent(fieldSet.readString("PRFM_5YR_PCT")));
        prodPerfmSeg_PRFM.setPerfmSinceLnchPct(formatPerformancePercent(fieldSet.readString("PRFM_SNC_LNCH_PCT")));
        prodPerfmSeg_PRFM.setRecDtTmSeg(getRecDtTmSeg(fieldSet));
        prodPerf.addProdPerfmSeg(prodPerfmSeg_PRFM);

        return prodPerf;
    }

    private ProdKeySeg getProdKeySeg(final FieldSet fieldSet) {

        ProdKeySeg prodKey = new ProdKeySeg();
        prodKey.setCtryRecCde(ConstantsPropertiesHelper.getValue(getJobCode(), "ctry_rec_cde"));
        prodKey.setGrpMembrRecCde(ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde"));
        String priceInfo = fieldSet.readString("PRICE_INFO");
        int ind = StringUtils.indexOf(priceInfo, "=");
        String productCode = priceInfo.substring(16, ind);
        prodKey.setProdCde(StringUtils.trimToEmpty(productCode));
        prodKey.setProdCdeAltClassCde("P");
        prodKey.setProdTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_type_cde"));

        return prodKey;
    }

    // RecDtTmSeg for ProdPrcSeg and ProdPerfmSeg use.
    private RecDtTmSeg getRecDtTmSeg(final FieldSet fieldSet) {

        String dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
        String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(ConstantsPropertiesHelper.getValue(
            getJobCode(), "ctry_rec_cde"), ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde")));
        RecDtTmSeg recDtTm = new RecDtTmSeg();
        recDtTm.setRecCreatDtTm(dateTime);
        recDtTm.setRecUpdtDtTm(dateTime);
        recDtTm.setTimeZone(GMTStr);

        return recDtTm;
    }
    
    //Convert performance percent to number.e.g. -0.40% to -0.40,8.49% to 8.49
    private String formatPerformancePercent(final String percent){
        if(StringUtils.isBlank(percent) || "N/A".equals(percent)){
            return "0";
        }
        String num = percent.substring(0,percent.length()-1);
        return new BigDecimal(num).stripTrailingZeros().toPlainString();
    }
}
