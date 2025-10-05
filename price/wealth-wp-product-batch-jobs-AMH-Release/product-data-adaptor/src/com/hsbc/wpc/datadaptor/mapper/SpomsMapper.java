/*
 */
package com.dummy.wpc.datadaptor.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFErrorConstants;
import org.apache.poi.hssf.usermodel.HSSFRow;

import com.dummy.wpc.batch.xml.DebtInstm;
import com.dummy.wpc.batch.xml.DebtInstmSeg;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdAsetUndlSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdUserDefExtSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.YieldHistSeg;
import com.dummy.wpc.datadaptor.constant.BN_FIELD_CONST;
import com.dummy.wpc.datadaptor.constant.Const;
import com.dummy.wpc.datadaptor.constant.INVST_PROD_FIELD_CONST;
import com.dummy.wpc.datadaptor.constant.PROD_FIELD_CONST;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.ExcelHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

/**
 * <p>
 * <b> Insert description of the class's responsibility/role. </b>
 * </p>
 */
public class SpomsMapper implements IMapper {

    private static final Logger LOGGER = Logger.getLogger(SpomsMapper.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");

    private static final String ISSUER = "issuer";
    private static final String ISSUE_NO = "issueno.ordescriptionifany";
    private static final String RATING = "rating";
    private static final String CCY = "ccy";
    private static final String ISS_DT = "issuedate";
    private static final String MAT_DT = "maturity";
    private static final String FREQ_COUPN_CDE = "couponfreq";
    private static final String COUPN_ANNL_PCT = "coupon";
    private static final String PAY_NEXT_COUPN_DT = "nextcoupondate";
    private static final String INVST_INIT_MIN_AMT = "minsize";// bank sell/customer buy
    private static final String INVST_ICT_MIN_AMT = "incremental";// bank sell/customer buy
    private static final String INT_IND_ACCR_AMT = "accruedinterestper1million";
    private static final String BOND_CDE = "bondcode";
    private static final String EXCL_CLBL_IND = "extendable/callable/puttable?";// Amend by Sharon
    private static final String SUB_ORDNT_IND = "subordinated?";
    private static final String LOT_SIZE_NUM = "sizeinmillion";
    private static final String SEPARATOR = "/";
    private static final int NUMBER = 1;
    private static final int PERCENTAGE = 100;

    // New added fields for Online Bond Trading from SPOMS - HFI interface
    private static final String INVST_INIT_MIN_SELL_AMT = "sellminsize"; // bank buy/customer sell
    private static final String INVST_ICT_MIN_SELL_AMT = "sellincremental";// bank buy/customer sell
    private static final String BOND_NAME = "bondname";
    private static final String BOND_SUBTYPE = "bondtype";
    private static final String ISIN_CDE = "isincode";
    private static final String BLOOMBERG_ID = "bloombergid";
    private static final String GUARANTOR = "guarantor";
    private static final String ISSUE_COUNTRY = "issuecountry";
    private static final String CALL_DATE = "calldate";
    private static final String COUPON_TYPE = "coupontype";
    private static final String PREV_COUPON_DATE = "previouscoupondate";
    private static final String CAPTITAL_TIER = "capitaltier";
    private static final String INTEREST_RATE_BASIS = "interestratebasis";
    private static final String DAYS_ACCRUED = "daysaccrued";
    private static final String CLOSING_BID_PRICE = "closingbidprice";
    private static final String CLOSING_OFFER_PRICE = "closingofferprice";
    private static final String CLOSING_YIELD = "closingbidyield";
    private static final String CLOSING_OFFER_YIELD = "closingofferyield";
    private static final String CLOSING_DATE = "closingdate";
    private static final String FLOAT_RATE_INDEX = "floatingrateindex";
    private static final String FLOAT_RATE_SPREAD = "floatingratespread";
    private static final String PREV_COUPON_DATE_2 = "previouscoupondate2";
    private static final String PREV_COUPON_RATE = "previouscouponrate";
    private static final String BUS_START_TM = "businessstarttime";
    private static final String BUS_END_TM = "businessendtime";
    private static final String RMK = "remark";
    private static final String RMK2 = "remark2";
    private static final String RISK_LEVEL = "risklevel";
    private static final String IS_COMPLEX = "iscomplex";

    private String jobCode;

    public DebtInstm maprow(HSSFRow row, Map titleMap) throws Exception {

        String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(ConstantsPropertiesHelper.getValue(
            getJobCode(), "ctry_rec_cde"), ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde")));
        String emptyStr = ConstantsPropertiesHelper.getValue(getJobCode(), "empty_str");
        String currentDateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
        String currentDate = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT);
        HSSFCell cell = null;
        HSSFCell issuerCell = null;
        HSSFCell matDtCell = null;
        HSSFCell coupnCell = null;
        boolean error = false;

        //1.prepare data
        String prodCde = processToString(row, titleMap, error, BOND_CDE, "bond code");
        error = validate(error, prodCde, PROD_FIELD_CONST.productCode.getFieldLength(), "bond code", prodCde);
        if (error) {
            return null;
        }

        String crncyCde = processToString(row, titleMap, error, CCY, "currency code", prodCde);
        error = validate(error, crncyCde, INVST_PROD_FIELD_CONST.currencyCode.getFieldLength(), "currency code", prodCde);
        if (error) {
            return null;
        }

        matDtCell = ExcelHelper.getCell(row, titleMap, MAT_DT);
        Date matDt = processToDate(row, titleMap, error, MAT_DT, "maturity date", prodCde);

        issuerCell = ExcelHelper.getCell(row, titleMap, ISSUER);
        String issCde = processToString(row, titleMap, error, ISSUER, "issuer", prodCde);
        issCde = StringUtils.isNotBlank(issCde) ? issCde.toUpperCase() : null;
        error = validate(error, issCde, BN_FIELD_CONST.issuerCode.getFieldLength(), "issuer", prodCde);
        if (error) {
            return null;
        }

        String issNum = processToString(row, titleMap, error, ISSUE_NO, "issue no.", prodCde);
        issNum = StringUtils.isNotBlank(issNum) ? StringUtils.substringBefore(issNum, ".") : null;
        error = validate(error, issNum, BN_FIELD_CONST.issuerNumber.getFieldLength(), "issue no.", prodCde);
        if (error) {
            return null;
        }

        Date issDt = processToDate(row, titleMap, error, ISS_DT, "issue date", prodCde);

        String freqCoupnCde = processToString(row, titleMap, error, FREQ_COUPN_CDE, "frequency coupon code", prodCde);
        error = validate(error, freqCoupnCde, BN_FIELD_CONST.frequencyCouponCode.getFieldLength(), "frequency coupon code", prodCde);
        if (error) {
            return null;
        }

        cell = ExcelHelper.getCell(row, titleMap, COUPN_ANNL_PCT);
        coupnCell = cell;
        String coupnAnnlPctTxt = null;
        if ((cell != null) && !error) {
            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                // coupnAnnlPct = ExcelHelper.convDoubleToBD(cell.getNumericCellValue(), PERCENTAGE,
                // BN_FIELD_CONST.couponAnnualPercent.getMinorUnit());
                coupnAnnlPctTxt = ExcelHelper.convDoubleToString(cell.getNumericCellValue(), PERCENTAGE,
                    BN_FIELD_CONST.couponAnnualPercent.getMinorUnit());
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                // coupnAnnlPct = ExcelHelper.convStringToBD(cell.getStringCellValue(), NUMBER,
                // BN_FIELD_CONST.couponAnnualPercent.getMinorUnit());
                coupnAnnlPctTxt = ExcelHelper.formatNumericString(cell.getStringCellValue(), NUMBER,
                    BN_FIELD_CONST.couponAnnualPercent.getMinorUnit());
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
                processErrorCell(cell, "annual coupon percentage", prodCde);
            }
        }

        Date payNextCoupnDt = processToDate(row, titleMap, error, PAY_NEXT_COUPN_DT, "next coupon payment date", prodCde);

        String exblClblInd = processToString(row, titleMap, error, EXCL_CLBL_IND, "extendable/callable", prodCde);
        error = validate(error, exblClblInd, BN_FIELD_CONST.extendableCallableIndicator.getFieldLength(), "extendable/callable",
            prodCde);
        if (error) {
            return null;
        }

        String credRtng1Cde = null;
        String credRtng2Cde = null;
        String[] credRtngArr = null;
        String credRtng = processToString(row, titleMap, error, RATING, "credit ratings", prodCde);
        if ((credRtng != null) && !error) {
            credRtngArr = StringUtils.split(credRtng, SEPARATOR);
            if (credRtngArr.length == 2) {
                if (ExcelHelper.hasLowerCase(credRtngArr[0]) || ExcelHelper.hasNumeric(credRtngArr[0])) {
                    credRtng1Cde = credRtngArr[0];
                    credRtng2Cde = credRtngArr[1];
                    // credRtng3Cde = credRtngArr[2];
                } else if (ExcelHelper.hasLowerCase(credRtngArr[1]) || ExcelHelper.hasNumeric(credRtngArr[1])) {
                    credRtng1Cde = credRtngArr[1];
                    credRtng2Cde = credRtngArr[0];
                    // credRtng3Cde = credRtngArr[2];
                } else {
                    if (StringUtils.containsOnly(credRtngArr[0], "-")) {
                        credRtng1Cde = credRtngArr[0];
                        credRtng2Cde = credRtngArr[1];
                        // credRtng3Cde = credRtngArr[2];
                    } else {
                        credRtng1Cde = credRtngArr[1];
                        credRtng2Cde = credRtngArr[0];
                        // credRtng3Cde = credRtngArr[2];
                    }
                }
            }
        }

//        BigDecimal intIndAccrAmtBd = processToBigDecimal(row, titleMap, error, INT_IND_ACCR_AMT,
//            "interest indicative accrued amount", prodCde);
//        BigDecimal intIndAccrAmtBd = processToBigDecimalWithScale(row, titleMap, error, INT_IND_ACCR_AMT, NUMBER, NUMBER,
//            BN_FIELD_CONST.interestIndicativeAccrueAmount.getMinorUnit(), "interest indicative accrued amount", prodCde);
        //TODO:Pending confirmed by BA,now will temporarily trim by decimal place 6 to align with WPC Batch.
        BigDecimal intIndAccrAmtBd = processToBigDecimalWithScale(row, titleMap, error, INT_IND_ACCR_AMT, NUMBER, NUMBER,
            6, "interest indicative accrued amount", prodCde);
        String intIndAccrAmt = ExcelHelper.convBDToString(intIndAccrAmtBd);

        BigDecimal invstInitMinAmtBd = processToBigDecimal(row, titleMap, error, INVST_INIT_MIN_SELL_AMT,
            "investment initial minimum amount", prodCde);
        String invstInitMinAmt = ExcelHelper.convBDToString(invstInitMinAmtBd);

        BigDecimal invstIctMinAmtBd = processToBigDecimal(row, titleMap, error, INVST_ICT_MIN_SELL_AMT,
            "investment incremental minimum amount", prodCde);// bank buy
        String invstIctMinAmt = ExcelHelper.convBDToString(invstIctMinAmtBd);

        BigDecimal lotSizeNumBd = processToBigDecimalWithScale(row, titleMap, error, LOT_SIZE_NUM, NUMBER, NUMBER,
            BN_FIELD_CONST.lotSizeNumber.getMinorUnit(), "lot size number", prodCde);
        if (lotSizeNumBd != null && !error) {
            String integral = StringUtils.substringBefore(lotSizeNumBd.toString(), ".");
            if (integral.length() > BN_FIELD_CONST.lotSizeNumber.getLength()) {
                error = true;
                LOGGER.error("Length of lot size number exceeds predefined value (PROD_CDE: " + prodCde + ").");
            }
        }
        String lotSizeNum = ExcelHelper.convBDToString(lotSizeNumBd);

        String subOrdnt = processToString(row, titleMap, error, SUB_ORDNT_IND, "subordinated indicator", prodCde);
        error = validate(error, subOrdnt, BN_FIELD_CONST.subordinatedIndicator.getFieldLength(), "subordinated indicator", prodCde);
        if (error) {
            return null;
        }

        // New fields added for Online Bond Trading
        String bondName = processToString(row, titleMap, error, BOND_NAME, "bond name", prodCde);
        error = validate(error, bondName, BN_FIELD_CONST.bondName.getFieldLength(), "bond name", prodCde);
        if (error) {
            return null;
        }

        String bondType = processToString(row, titleMap, error, BOND_SUBTYPE, "bond subtype", prodCde);
        error = validate(error, bondType, BN_FIELD_CONST.bondType.getFieldLength(), "bond subtype", prodCde);
        if (error) {
            return null;
        }

        String isinCode = processToString(row, titleMap, error, ISIN_CDE, "ISIN Code", prodCde);
        error = validate(error, isinCode, BN_FIELD_CONST.isinCode.getFieldLength(), "ISIN Code", prodCde);
        if (error) {
            return null;
        }

        String bloombergID = processToString(row, titleMap, error, BLOOMBERG_ID, "Bloomberg ID", prodCde);
        error = validate(error, bloombergID, BN_FIELD_CONST.bloombergID.getFieldLength(), "Bloomberg ID", prodCde);
        if (error) {
            return null;
        }

        String guarantor = processToString(row, titleMap, error, GUARANTOR, "guarantor", prodCde);
        guarantor = StringUtils.isNotBlank(guarantor) ? guarantor.toUpperCase() : null;
        error = validate(error, guarantor, BN_FIELD_CONST.guarantor.getFieldLength(), "guarantor", prodCde);
        if (error) {
            return null;
        }

//        String issueCountry = processToString(row, titleMap, error, ISSUE_COUNTRY, "Issue Country", prodCde);
//        error = validate(error, issueCountry, BN_FIELD_CONST.issueCountry.getFieldLength(), "Issue Country", prodCde);
//        if (error) {
//            return null;
//        }
        //Set default value,instead of getting value from field "Issue Country".Because this field will be overrided by Reuter,
        //and its value is too large for column CTRY_BOND_ISSUE_CDE.e.g its value is HONG KONG,but CTRY_BOND_ISSUE_CDE is char(2),
        //so it need to map to HK.
        String issueCountry = ConstantsPropertiesHelper.getValue(getJobCode(), "country_bond_issue_name");

        Date callDate = processToDate(row, titleMap, error, CALL_DATE, "next callable date", prodCde);

        String couponType = processToString(row, titleMap, error, COUPON_TYPE, "coupon type", prodCde);
        error = validate(error, couponType, BN_FIELD_CONST.couponType.getFieldLength(), "coupon type", prodCde);
        if (error) {
            return null;
        }

        Date previousCouponDate = processToDate(row, titleMap, error, PREV_COUPON_DATE, "previous coupon date", prodCde);

        String capitalTier = processToString(row, titleMap, error, CAPTITAL_TIER, "capital tier", prodCde);
        error = validate(error, capitalTier, BN_FIELD_CONST.capitalTier.getFieldLength(), "capital tier", prodCde);
        if (error) {
            return null;
        }

        String interestRateBasis = processToString(row, titleMap, error, INTEREST_RATE_BASIS, "interest rate basis", prodCde);
        error = validate(error, interestRateBasis, BN_FIELD_CONST.interestRateBasis.getFieldLength(), "interest rate basis",
            prodCde);
        if (error) {
            return null;
        }

        BigDecimal daysAccruedBd = processToBigDecimalWithScale(row, titleMap, error, DAYS_ACCRUED, NUMBER, NUMBER,
            BN_FIELD_CONST.daysAccrued.getMinorUnit(), "days accrued percentage", prodCde);
        String daysAccrued = ExcelHelper.convBDToString(daysAccruedBd);

		BigDecimal closingBidPrcAmtBd = processToBigDecimal(row, titleMap,
				error, CLOSING_BID_PRICE, "closing bid price amount",
            prodCde);
        String closingBidPrcAmt = ExcelHelper.convBDToString(closingBidPrcAmtBd);

		BigDecimal closingOfferPrcAmtBd = processToBigDecimal(row, titleMap,
				error, CLOSING_OFFER_PRICE,
            "closing offer price amount", prodCde);
        String closingOfferPrcAmt = ExcelHelper.convBDToString(closingOfferPrcAmtBd);

		BigDecimal closingBidYieldBd = processToBigDecimalWithScale(row,
				titleMap, error, CLOSING_YIELD, PERCENTAGE, NUMBER,
            BN_FIELD_CONST.closingBidYield.getMinorUnit(), "closing bid yield", prodCde);
        String closingBidYield = ExcelHelper.convBDToString(closingBidYieldBd);

		BigDecimal closingOfferYieldBd = processToBigDecimalWithScale(row,
				titleMap, error, CLOSING_OFFER_YIELD, PERCENTAGE, NUMBER,
            BN_FIELD_CONST.closingOfferYield.getMinorUnit(), "closing offer yield", prodCde);
        String closingOfferYield = ExcelHelper.convBDToString(closingOfferYieldBd);

        Date closingDate = processToDate(row, titleMap, error, CLOSING_DATE, "extended closing date", prodCde);

        String floatingRateIndex = processToString(row, titleMap, error, FLOAT_RATE_INDEX, "floating rate index", prodCde);
        error = validate(error, floatingRateIndex, BN_FIELD_CONST.floatingRateIndex.getFieldLength(), "floating rate index",
            prodCde);
        if (error) {
            return null;
        }

        BigDecimal floatingRateSpreadBd = processToBigDecimalWithScale(row, titleMap, error, FLOAT_RATE_SPREAD, PERCENTAGE, NUMBER,
            BN_FIELD_CONST.floatingRateSpread.getMinorUnit(), "floating rate spread", prodCde);
        String floatingRateSpread = ExcelHelper.convBDToString(floatingRateSpreadBd);

        Date previousCouponDate2 = processToDate(row, titleMap, error, PREV_COUPON_DATE_2, "previous coupon date2", prodCde);

        BigDecimal previousCouponRateBd = processToBigDecimalWithScale(row, titleMap, error, PREV_COUPON_RATE, PERCENTAGE, NUMBER,
            BN_FIELD_CONST.previousCouponRate.getMinorUnit(), "previous coupon rate", prodCde);
        String previousCouponRate = ExcelHelper.convBDToString(previousCouponRateBd);

        String businessStartTime = processToString(row, titleMap, error, BUS_START_TM, "business start time", prodCde);
        error = validate(error, businessStartTime, BN_FIELD_CONST.businessStartTime.getFieldLength(), "business start time",
            prodCde);
        if (error) {
            return null;
        }

        String businessEndTime = processToString(row, titleMap, error, BUS_END_TM, "business end time", prodCde);
        error = validate(error, businessEndTime, BN_FIELD_CONST.businessEndTime.getFieldLength(), "business end time", prodCde);
        if (error) {
            return null;
        }

        String remark = processToString(row, titleMap, error, RMK, "remark", prodCde);
        error = validate(error, remark, BN_FIELD_CONST.remark.getFieldLength(), "remark", prodCde);
        if (error) {
            return null;
        }

        String remark2 = processToString(row, titleMap, error, RMK2, "remark2", prodCde);
        error = validate(error, remark2, BN_FIELD_CONST.remark.getFieldLength(), "remark2", prodCde);
        if (error) {
            return null;
        }

        String riskLevel = processToString(row, titleMap, error, RISK_LEVEL, "risk level", prodCde);
        riskLevel = StringUtils.equals(riskLevel, "0") ? null : riskLevel;
        error = validate(error, riskLevel, BN_FIELD_CONST.riskLevel.getFieldLength(), "risk level", prodCde);
        if (error) {
            return null;
        }

        String cmplxProdInd = processToString(row, titleMap, error, IS_COMPLEX, "Is Complex", prodCde);
        error = validate(error, cmplxProdInd, BN_FIELD_CONST.cmplxProdInd.getFieldLength(), "Is Complex", prodCde);
        if (error) {
            return null;
        }
        
        BigDecimal invstInitMinSellAmtBd = processToBigDecimal(row, titleMap, error, INVST_INIT_MIN_AMT,
            "investment initial minimum sell amount", prodCde);// bank sell
        String invstInitMinSellAmt = ExcelHelper.convBDToString(invstInitMinSellAmtBd);

        BigDecimal invstIctMinSellAmtBd = processToBigDecimal(row, titleMap, error, INVST_ICT_MIN_AMT,
            "investment incremental minimum sell amount", prodCde);// bank sell
        String invstIctMinSellAmt = ExcelHelper.convBDToString(invstIctMinSellAmtBd);

        //2.map to pojo
        DebtInstm bond = new DebtInstm();

        ProdKeySeg prodKey = new ProdKeySeg();
        prodKey.setCtryRecCde(ConstantsPropertiesHelper.getValue(getJobCode(), "ctry_rec_cde"));
        prodKey.setGrpMembrRecCde(ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde"));
        prodKey.setProdCde(prodCde);
        prodKey.setProdTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_type_cde"));
        bond.setProdKeySeg(prodKey);

        ProdAltNumSeg prodAltNumSegP = new ProdAltNumSeg();
        prodAltNumSegP.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_cde_alt_class_cde_p"));
        prodAltNumSegP.setProdAltNum(prodCde);
        bond.addProdAltNumSeg(prodAltNumSegP);
        
        ProdAltNumSeg prodAltNumSeg_M = new ProdAltNumSeg();
        prodAltNumSeg_M.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_cde_alt_class_cde_m"));
        prodAltNumSeg_M.setProdAltNum(prodCde);
        bond.addProdAltNumSeg(prodAltNumSeg_M);

        if (StringUtils.isNotBlank(isinCode)) {
            ProdAltNumSeg prodAltNumSeg_I = new ProdAltNumSeg();
            prodAltNumSeg_I.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_cde_alt_class_cde_i"));
            prodAltNumSeg_I.setProdAltNum(StringUtils.trimToEmpty(isinCode));
            bond.addProdAltNumSeg(prodAltNumSeg_I);
        }

        if (StringUtils.isNotBlank(bloombergID)) {
            ProdAltNumSeg prodAltNumSeg_Y = new ProdAltNumSeg();
            prodAltNumSeg_Y.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_cde_alt_class_cde_y"));
            prodAltNumSeg_Y.setProdAltNum(StringUtils.trimToEmpty(bloombergID));
            bond.addProdAltNumSeg(prodAltNumSeg_Y);
        }

        ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
        prodInfoSeg.setProdSubtpCde(StringUtils.trimToEmpty(bondType));

        // prodInfoSeg.setProdName(bondName);
        Object[] prodNameArr = new Object[5];
        prodNameArr[0] = issCde;
        prodNameArr[1] = issNum;
        prodNameArr[2] = DateHelper.formatDate2String(matDt, DateHelper.DATE_SHORT_FORMAT);
        prodNameArr[3] = crncyCde;
        if (ExcelHelper.isNumeric(coupnAnnlPctTxt)) {
            prodNameArr[4] = coupnAnnlPctTxt + "%";
        } else {
            prodNameArr[4] = coupnAnnlPctTxt;
        }
        String prodName = StringUtils.join(prodNameArr, " ");
        prodInfoSeg.setProdName(StringUtils.trimToEmpty(prodName));

        prodInfoSeg.setProdStatCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_stat_cde"));
        prodInfoSeg.setCcyProdCde(StringUtils.trimToEmpty(crncyCde));
        prodInfoSeg.setRiskLvlCde(riskLevel);
        prodInfoSeg.setProdLnchDt(currentDate);
        prodInfoSeg.setProdMturDt(DateHelper.formatDate2String(matDt, DateHelper.DEFAULT_DATE_FORMAT));
        prodInfoSeg.setAllowBuyProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), "allow_buy_product_indicator"));
        prodInfoSeg.setAllowSellProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), "allow_sell_product_indicator"));
        prodInfoSeg.setAllowBuyUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), "allow_buy_unit_product_indicator"));
        prodInfoSeg.setAllowBuyAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), "allow_buy_amount_product_indicator"));
        prodInfoSeg.setAllowSellUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), "allow_sell_unit_product_indicator"));
        prodInfoSeg.setAllowSellAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), "allow_sell_amount_product_indicator"));
        prodInfoSeg.setAllowSellMipProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            "allow_sell_monthly_investment_program_product_indicator"));
        prodInfoSeg.setAllowSellMipUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            "allow_sell_monthly_investment_program_unit_product_indicator"));
        prodInfoSeg.setAllowSellMipAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            "allow_sell_monthly_investment_program_amount_product_indicator"));
        prodInfoSeg.setAllowSwInProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), "allow_switch_in_product_indicator"));
        prodInfoSeg.setAllowSwInUtProdInd(ConstantsPropertiesHelper
            .getValue(getJobCode(), "allow_switch_in_unit_product_indicator"));
        prodInfoSeg.setAllowSwInAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            "allow_switch_in_amount_product_indicator"));
        prodInfoSeg.setAllowSwOutProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), "allow_switch_out_product_indicator"));
        prodInfoSeg.setAllowSwOutUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            "allow_switch_out_unit_product_indicator"));
        prodInfoSeg.setAllowSwOutAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            "allow_switch_out_amount_product_indicator"));
        prodInfoSeg.setIncmCharProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), "income_characteristic_product_indicator"));
        prodInfoSeg.setCptlGurntProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), "capital_guaranteed_product_indicator"));
        prodInfoSeg.setYieldEnhnProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), "yield_enhancement_product_indicator"));
        prodInfoSeg
            .setGrwthCharProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), "growth_characteristic_product_indicator"));
        prodInfoSeg.setPrtyProdSrchRsultNum(ConstantsPropertiesHelper.getValue(getJobCode(),
            "priority_product_search_result_number"));
        prodInfoSeg.setAvailMktInfoInd(ConstantsPropertiesHelper.getValue(getJobCode(), "available_market_information_indicator"));
        prodInfoSeg.setDmyProdSubtpRecInd(ConstantsPropertiesHelper
            .getValue(getJobCode(), "dummy_product_subtype_record_indicator"));
        prodInfoSeg.setDispComProdSrchInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            "display_common_product_search_indicator"));
        prodInfoSeg.setDivrNum(ConstantsPropertiesHelper.getValue(getJobCode(), "divisor_number"));
        prodInfoSeg.setMrkToMktInd(ConstantsPropertiesHelper.getValue(getJobCode(), "mark_to_market_indicator"));
        prodInfoSeg.setCtryProdTrade1Cde(ConstantsPropertiesHelper.getValue(getJobCode(), "country_product_tradable_code_1"));
        prodInfoSeg.setBusStartTm(businessStartTime);
        prodInfoSeg.setBusEndTm(businessEndTime);
        prodInfoSeg.setCcyInvstCde(StringUtils.trimToEmpty(crncyCde));
        prodInfoSeg.setInvstInitMinAmt(invstInitMinSellAmt);

        prodInfoSeg.setProdShrtName(emptyStr);
        prodInfoSeg.setQtyTypeCde(emptyStr);
        prodInfoSeg.setProdLocCde(emptyStr);
        prodInfoSeg.setSuptRcblCashProdInd(emptyStr);
        prodInfoSeg.setSuptRcblScripProdInd(emptyStr);
        prodInfoSeg.setDcmlPlaceTradeUnitNum(emptyStr);
        prodInfoSeg.setAumChrgProdInd(emptyStr);
        
        ProdAsetUndlSeg prodAsetUndlSeg = new ProdAsetUndlSeg();
        prodAsetUndlSeg.setAsetUndlCdeSeqNum(ConstantsPropertiesHelper.getValue(getJobCode(), "aset_undl_cde_seq_num_1"));
        prodAsetUndlSeg.setAsetUndlCde(ConstantsPropertiesHelper.getValue(getJobCode(), "aset_undl_cde"));
        prodInfoSeg.addProdAsetUndlSeg(prodAsetUndlSeg);
        
        List<ProdUserDefExtSeg> userExtFieldList = new ArrayList<ProdUserDefExtSeg>();
        String  cmplxProdIndVal = null;
        if(StringUtils.isNotBlank(cmplxProdInd) && "Y".equals(cmplxProdInd)){
        	cmplxProdIndVal = Const.PRODUCT_IND_COMPLEX;
        }else if(StringUtils.isNotBlank(cmplxProdInd) && "N".equals(cmplxProdInd)){
        	cmplxProdIndVal = Const.PRODUCT_IND_SIMPLE;
        }
		userExtFieldList.add(extractUserDefExtField(cmplxProdIndVal, "cmplxProdInd"));
		prodInfoSeg.setProdUserDefExtSeg(userExtFieldList.toArray(new ProdUserDefExtSeg[] {}));
        
        bond.setProdInfoSeg(prodInfoSeg);

        DebtInstmSeg bondSeg = new DebtInstmSeg();
        bondSeg.setIsrBndNme(issCde);
        bondSeg.setIssueNum(issNum);
        bondSeg.setProdIssDt(DateHelper.formatDate2String(issDt, DateHelper.DEFAULT_DATE_FORMAT));
        bondSeg.setPdcyCoupnPymtCd(freqCoupnCde);
        bondSeg.setCoupnAnnlRt(coupnAnnlPctTxt);
        bondSeg.setPymtCoupnNextDt(DateHelper.formatDate2String(payNextCoupnDt, DateHelper.DEFAULT_DATE_FORMAT));
        bondSeg.setFlexMatOptInd(exblClblInd);
        bondSeg.setIntIndAccrAmt(intIndAccrAmt);
        bondSeg.setInvstIncMinAmt(invstIctMinAmt);
        bondSeg.setProdBodLotQtyCnt(lotSizeNum);
        bondSeg.setSubDebtInd(subOrdnt);
        bondSeg.setCtryBondIssueCde(issueCountry);
        bondSeg.setGrntrName(guarantor);
        bondSeg.setCptlTierText(capitalTier);
        bondSeg.setCoupnType(couponType);
        bondSeg.setCoupnPrevRt(previousCouponRate);
        bondSeg.setIndexFltRtNme(floatingRateIndex);
        bondSeg.setBondFltSprdRt(floatingRateSpread);
        bondSeg.setCoupnCurrStartDt(DateHelper.formatDate2String(previousCouponDate, DateHelper.DEFAULT_DATE_FORMAT));
        bondSeg.setCoupnPrevStartDt(DateHelper.formatDate2String(previousCouponDate2, DateHelper.DEFAULT_DATE_FORMAT));
        bondSeg.setBondCallNextDt(DateHelper.formatDate2String(callDate, DateHelper.DEFAULT_DATE_FORMAT));
        bondSeg.setIntBassiCalcText(interestRateBasis);
        bondSeg.setIntAccrDayCnt(daysAccrued);
//        bondSeg.setInvstSoldLestAmt(invstInitMinSellAmt);
        bondSeg.setInvstIncrmSoldAmt(invstIctMinSellAmt);
        bondSeg.setProdClsBidPrcAmt(closingBidPrcAmt);
        bondSeg.setProdClsOffrPrcAmt(closingOfferPrcAmt);
        bondSeg.setBondCloseDt(DateHelper.formatDate2String(closingDate, DateHelper.DEFAULT_DATE_FORMAT));

        if (ExcelHelper.isExtRow(issuerCell, matDtCell)) {
            bondSeg.setMturExtDt(DateHelper.formatDate2String(matDt, DateHelper.DEFAULT_DATE_FORMAT));
        }
        if (ExcelHelper.isExtRow(issuerCell, coupnCell)) {
            bondSeg.setCoupnExtInstmRt(coupnAnnlPctTxt);// coupnAnnlPct
        }

//        CreditRtingSeg creditRtingSeg1 = new CreditRtingSeg();
//        creditRtingSeg1.setCreditRtingAgcyCde(ConstantsPropertiesHelper.getValue(getJobCode(), "credit_rating_agency_code_1"));
//        creditRtingSeg1.setCreditRtingCde(StringUtils.trimToEmpty(credRtng1Cde));
//        bondSeg.addCreditRtingSeg(creditRtingSeg1);
//
//        CreditRtingSeg creditRtingSeg2 = new CreditRtingSeg();
//        creditRtingSeg2.setCreditRtingAgcyCde(ConstantsPropertiesHelper.getValue(getJobCode(), "credit_rating_agency_code_2"));
//        creditRtingSeg2.setCreditRtingCde(StringUtils.trimToEmpty(credRtng2Cde));
//        bondSeg.addCreditRtingSeg(creditRtingSeg2);

        YieldHistSeg yieldHistSeg = new YieldHistSeg();
        yieldHistSeg.setYieldTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), "investment_yield_type_code"));
        // excel value may be null,but mandatory,so set empty for castor validation
        yieldHistSeg.setYieldDt(StringUtils.trimToEmpty(DateHelper.formatDate2String(closingDate, DateHelper.DEFAULT_DATE_FORMAT)));
        yieldHistSeg.setYieldEffDt(StringUtils.trimToEmpty(DateHelper
            .formatDate2String(closingDate, DateHelper.DEFAULT_DATE_FORMAT)));
        //yieldHistSeg.setYieldBidPct(closingBidYield);
        //yieldHistSeg.setYieldOfferPct(closingOfferYield);
        yieldHistSeg.setYieldBidClosePct(closingBidYield);
        yieldHistSeg.setYieldOfferClosePct(closingOfferYield);
        bondSeg.addYieldHistSeg(yieldHistSeg);
        bond.setDebtInstmSeg(bondSeg);

        RecDtTmSeg recDtTmSeg = new RecDtTmSeg();
        recDtTmSeg.setRecCreatDtTm(currentDateTime);
        recDtTmSeg.setRecUpdtDtTm(currentDateTime);
        recDtTmSeg.setProdStatUpdtDtTm(currentDateTime);
        recDtTmSeg.setTimeZone(GMTStr);
        bond.setRecDtTmSeg(recDtTmSeg);

        return bond;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    private BigDecimal processToBigDecimalWithScale(HSSFRow row, Map titleMap, boolean error, String columnName, int numericScale,
        int StringScale, int decimal, String fieldName, String prodCde) {
        BigDecimal result = null;
        HSSFCell cell = ExcelHelper.getCell(row, titleMap, columnName);
        if ((cell != null) && !error) {
            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                result = ExcelHelper.convDoubleToBD(cell.getNumericCellValue(), numericScale, decimal);
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                result = ExcelHelper.convStringToBD(cell.getStringCellValue(), StringScale, decimal);
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
                processErrorCell(cell, fieldName, prodCde);
            }
        }
        return result;
    }
    
    private BigDecimal processToBigDecimalWithScaleSupportNegativeValue(HSSFRow row, Map titleMap, boolean error, String columnName, int numericScale,
            int StringScale, int decimal, String fieldName, String prodCde) {
            BigDecimal result = null;
            HSSFCell cell = ExcelHelper.getCell(row, titleMap, columnName);
            if ((cell != null) && !error) {
                if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    result = ExcelHelper.convDoubleToBDSupportNegativeValue(cell.getNumericCellValue(), numericScale, decimal);
                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                    result = ExcelHelper.convStringToBD(cell.getStringCellValue(), StringScale, decimal);
                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
                    processErrorCell(cell, fieldName, prodCde);
                }
            }
            return result;
        }

    private BigDecimal processToBigDecimal(HSSFRow row, Map titleMap, boolean error, String columnName, String fieldName,
        String prodCde) {
        BigDecimal result = null;
        HSSFCell cell = ExcelHelper.getCell(row, titleMap, columnName);
        if ((cell != null) && !error) {
            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                result = ExcelHelper.convDoubleToBD(cell.getNumericCellValue());
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                result = ExcelHelper.convStringToBD(cell.getStringCellValue());
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
                processErrorCell(cell, fieldName, prodCde);
            }
        }
        return result;
    }
    
    private BigDecimal processToBigDecimalSupportNegativeValue(HSSFRow row, Map titleMap, boolean error, String columnName, String fieldName,
            String prodCde) {
            BigDecimal result = null;
            HSSFCell cell = ExcelHelper.getCell(row, titleMap, columnName);
            if ((cell != null) && !error) {
                if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    result = ExcelHelper.convDoubleToBDSupportNegativeValue(cell.getNumericCellValue());
                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                    result = ExcelHelper.convStringToBD(cell.getStringCellValue());
                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
                    processErrorCell(cell, fieldName, prodCde);
                }
            }
            return result;
        }

    private Date processToDate(HSSFRow row, Map titleMap, boolean error, String columnName, String fieldName, String prodCde)
        throws Exception {
        Date result = null;
        HSSFCell cell = ExcelHelper.getCell(row, titleMap, columnName);
        if ((cell != null) && !error) {
            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                result = ExcelHelper.processDate(cell.getDateCellValue(), dateFormat);
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                result = ExcelHelper.convStringToDate(cell.getStringCellValue(), dateFormat);
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
                processErrorCell(cell, fieldName, prodCde);
            }
        }
        return result;
    }

    private String processToString(HSSFRow row, Map titleMap, boolean error, String columnName, String fieldName) {
        String result = null;
        HSSFCell cell = ExcelHelper.getCell(row, titleMap, columnName);
        if ((cell != null) && !error) {
            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                result = ExcelHelper.convDoubleToString(cell.getNumericCellValue());
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                result = ExcelHelper.processString(cell.getStringCellValue());
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
                processErrorCell(cell, fieldName, result);
            }
        }
        return result;
    }

    private String processToString(HSSFRow row, Map titleMap, boolean error, String columnName, String fieldName, String prodCde) {
        String result = null;
        HSSFCell cell = ExcelHelper.getCell(row, titleMap, columnName);
        if ((cell != null) && !error) {
            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                result = ExcelHelper.convDoubleToString(cell.getNumericCellValue());
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                result = ExcelHelper.processString(cell.getStringCellValue());
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
                processErrorCell(cell, fieldName, prodCde);
            }
        }
        return result;
    }

    private boolean validate(boolean preValidate, String value, int maxLen, String fieldName, String prodCde) {
        boolean error = false;
        if (!preValidate && (value != null) && (value.length() > maxLen)) {
            error = true;
            LOGGER.error("Length of " + fieldName + " exceeds predefined value (PROD_CDE: " + prodCde + ").");
        }
        return error;
    }

    private void processErrorCell(HSSFCell cell, String fieldName, String prodCde) {
        if (cell.getErrorCellValue() == HSSFErrorConstants.ERROR_DIV_0) {
            LOGGER.error("Invalid " + fieldName + " found: #DIV/0! (PROD_CDE: " + prodCde + ").");
        } else if (cell.getErrorCellValue() == HSSFErrorConstants.ERROR_NA) {
            LOGGER.error("Invalid " + fieldName + " found: #N/A! (PROD_CDE: " + prodCde + ").");
        } else if (cell.getErrorCellValue() == HSSFErrorConstants.ERROR_NAME) {
            LOGGER.error("Invalid " + fieldName + " found: #NAME? (PROD_CDE: " + prodCde + ").");
        } else if (cell.getErrorCellValue() == HSSFErrorConstants.ERROR_NULL) {
            LOGGER.error("Invalid " + fieldName + " found: #NULL! (PROD_CDE: " + prodCde + ").");
        } else if (cell.getErrorCellValue() == HSSFErrorConstants.ERROR_NUM) {
            LOGGER.error("Invalid " + fieldName + " found: #NUM! (PROD_CDE: " + prodCde + ").");
        } else if (cell.getErrorCellValue() == HSSFErrorConstants.ERROR_REF) {
            LOGGER.error("Invalid " + fieldName + " found: #REF! (PROD_CDE: " + prodCde + ").");
        } else if (cell.getErrorCellValue() == HSSFErrorConstants.ERROR_VALUE) {
            LOGGER.error("Invalid " + fieldName + " found: #VALUE! (PROD_CDE: " + prodCde + ").");
        }
    }

    /**
   	 * @param ProdUserDefExtSeg fieldSet
   	 * @return
   	 */
   	private ProdUserDefExtSeg extractUserDefExtField(String fieldSet, String fieldCde) {
   		ProdUserDefExtSeg bondFundIndField = new ProdUserDefExtSeg();
   		bondFundIndField.setFieldCde(fieldCde);
   		bondFundIndField.setFieldValue(StringUtils.trimToEmpty(fieldSet));
   		return bondFundIndField;
   	}
    
}
