/*
 */
package com.dummy.wpc.datadaptor.mapper;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.MpfInstm;
import com.dummy.wpc.batch.xml.MpfPerfmSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.constant.Const;
import com.dummy.wpc.datadaptor.constant.MPF_FIELD_CONST;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
public class MpfCumPerfMapper extends AbstractFieldSetMapper {
    private static final int PERCENTAGE = 100;
    private static final String DATE_FORMAT = "yyyyMMdd";

    public Object mapLine(final FieldSet fieldSet) {
        String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(
            ConstantsPropertiesHelper.getValue(getJobCode(), "ctry_rec_cde"),
            ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde")));

        MpfInstm mpfPerf = new MpfInstm();
        ProdKeySeg prodKey = new ProdKeySeg();
        prodKey.setCtryRecCde(ConstantsPropertiesHelper.getValue(getJobCode(), "ctry_rec_cde"));
        prodKey.setGrpMembrRecCde(ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde"));
        prodKey.setProdTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_type_cde"));
        prodKey.setProdCdeAltClassCde("P");

        StringBuilder mpfProdCde = new StringBuilder();
        mpfProdCde = mpfProdCde.append(StringUtils.trimToEmpty(fieldSet.readString("PROD_CDE"))).append("-")
            .append(StringUtils.trimToEmpty(fieldSet.readString("FUND_CDE")));
        prodKey.setProdCde(mpfProdCde.toString());
        mpfPerf.setProdKeySeg(prodKey);

        MpfPerfmSeg mpfPerfmSeg = new MpfPerfmSeg();
        // TODO: PRFM_UPDT_DT
        mpfPerfmSeg.setPerfmEffDt(DateHelper.formatDate2String(
            DateHelper.parseToDate(fieldSet.readString("PRFM_EFF_DT"), MpfCumPerfMapper.DATE_FORMAT),
            DateHelper.DEFAULT_DATE_FORMAT));

        BigDecimal Perfm1moPct = convStringToBD(fieldSet.readString("PRFM_1MO_PCT"), fieldSet.readString("PRFM_1MO_SGN"),
            MpfCumPerfMapper.PERCENTAGE, MPF_FIELD_CONST.performance1MonthsPercent.getMinorUnit());
        BigDecimal Perfm6moPct = convStringToBD(fieldSet.readString("PRFM_6MO_PCT"), fieldSet.readString("PRFM_6MO_SGN"),
            MpfCumPerfMapper.PERCENTAGE, MPF_FIELD_CONST.performance6MonthsPercent.getMinorUnit());
        BigDecimal Perfm1yrPct = convStringToBD(fieldSet.readString("PRFM_1YR_PCT"), fieldSet.readString("PRFM_1YR_SGN"),
            MpfCumPerfMapper.PERCENTAGE, MPF_FIELD_CONST.performance1YearPercent.getMinorUnit());
        BigDecimal Perfm3yrPct = convStringToBD(fieldSet.readString("PRFM_3YR_PCT"), fieldSet.readString("PRFM_3YR_SGN"),
            MpfCumPerfMapper.PERCENTAGE, MPF_FIELD_CONST.performance3YearsPercent.getMinorUnit());
        BigDecimal Perfm5yrPct = convStringToBD(fieldSet.readString("PRFM_5YR_PCT"), fieldSet.readString("PRFM_5YR_SGN"),
            MpfCumPerfMapper.PERCENTAGE, MPF_FIELD_CONST.performance5YearsPercent.getMinorUnit());
        BigDecimal PerfmCumPct = convStringToBD(fieldSet.readString("PRFM_CUM_PCT"), fieldSet.readString("PRFM_CUM_SGN"),
            MpfCumPerfMapper.PERCENTAGE, MPF_FIELD_CONST.performanceCumulativePercent.getMinorUnit());


        mpfPerfmSeg.setPerfm1moPct(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(Perfm1moPct)));
        mpfPerfmSeg.setPerfm6moPct(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(Perfm6moPct)));
        mpfPerfmSeg.setPerfm1yrPct(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(Perfm1yrPct)));
        mpfPerfmSeg.setPerfm3yrPct(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(Perfm3yrPct)));
        mpfPerfmSeg.setPerfm5yrPct(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(Perfm5yrPct)));
        mpfPerfmSeg.setPerfmCumPct(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(PerfmCumPct)));
        mpfPerfmSeg.setPerfmSinceLnchPct(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(PerfmCumPct)));
        mpfPerfmSeg.setDisIndc(StringUtils.trimToEmpty(fieldSet.readString("DIS_INDC")));
        // mpfPerfmSeg.setPdcyPrcCde(fieldSet.readString("PRC_TYPE_CDE"));
        // mpfPerfmSeg.setPrcInpDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("PRC_DT"),DateHelper.DATE_SHORT_FORMAT),
        // DateHelper.DEFAULT_DATE_FORMAT));
        // mpfPerfmSeg.setCcyProdMktPrcCde(StringUtils.trimToEmpty(fieldSet.readString("CRNCY_PRC_CDE")));
        // mpfPerfmSeg.setProdBidPrcAmt(DecimalHelper.trimZero(fieldSet.readString("BID_PRC_AMT")));
        // mpfPerfmSeg.setProdOffrPrcAmt(DecimalHelper.trimZero(fieldSet.readString("OFFER_PRC_AMT")));
        // mpfPerfmSeg.setProdNavPrcAmt(DecimalHelper.trimZero(fieldSet.readString("NAV_PRC_AMT")));

        RecDtTmSeg recDtTmSeg = new RecDtTmSeg();
        String dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
        recDtTmSeg.setRecCreatDtTm(dateTime);
        recDtTmSeg.setRecUpdtDtTm(dateTime);
        recDtTmSeg.setTimeZone(GMTStr);
        mpfPerfmSeg.setRecDtTmSeg(recDtTmSeg);

        mpfPerf.addMpfPerfmSeg(mpfPerfmSeg);

        return mpfPerf;
    }

    public static BigDecimal convStringToBD(final String in, final String sgn, final int scale, final int decimal) {
        BigDecimal bd;
        if ((in == null) || (in.equals(""))) {
            return null;
        } else {
            try {
                if (scale > 0) {
                    String pattern = "#0.";
                    for (int i = 0; i < decimal; i++) {
                        pattern = pattern.concat("0");
                    }
                    DecimalFormat df = new DecimalFormat(pattern);
                    double d = Double.valueOf(in).doubleValue() / scale;
                    if (sgn.equals(Const.NEG_SGN)) {
                        bd = new BigDecimal(df.format(d)).negate();
                    } else {
                        bd = new BigDecimal(df.format(d));
                    }
                    return bd;
                } else {
                    return null;
                }
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
    }


}
