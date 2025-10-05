/*
 * ***************************************************************
 * Copyright. dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 * 
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system or translated in any human or computer language
 * in any way or for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 * 
 * Class Name MPF_FIELD_CONST
 * 
 * Creation Date Sep 8, 2006
 * 
 * Amendment History (In chronological sequence):
 * 
 * Amendment Date Sep 8, 2006 CMM/PPCR No. Programmer 35021438 Description
 */
package com.dummy.wpc.datadaptor.constant;


public class MPF_FIELD_CONST {
    public class FIELD_NAME_CONST {
        public final static String productMpfCode = "MPF Product Code";
        public final static String fundMpfCode = "MPF Fund Code";
        public final static String sequenceNumber = "Sequence Number";
        public final static String priceEffectiveDate = "MPF Price Date";
        public final static String currencyPriceCode = "Currency Daily Price Code";
        public final static String offerPriceAmount = "Daily Offer Price";
        public final static String bidPriceAmount = "Daily Bid Price";
        public final static String performanceCumulativePercent = "Cumulative Performance";
        public final static String performance1MonthsPercent = "1 Month Cumulative Performance";
        public final static String performance6MonthsPercent = "6 Month Cumulative Performance";
        public final static String performance1YearPercent = "1 Year Cumulative Performance";
        public final static String performance3YearsPercent = "3 Years Cumulative Performance";
        public final static String performance5YearsPercent = "5 Years Cumulative Performance";
        public final static String performanceSinceLaunchPercent = "Since Launch Cumulative Performance";
        public final static String performanceEffectiveDate = "Since Launch Performance Date";
        public final static String performanceUpdateDate = "Performance Date";
    }

    public final static StringFieldInfo productMpfCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR,
        FIELD_NAME_CONST.productMpfCode, 16, null);
    public final static StringFieldInfo fundMpfCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR,
        FIELD_NAME_CONST.fundMpfCode, 10, null);
    public final static FieldInfo sequenceNumber = new FieldInfo(FIELD_NAME_CONST.sequenceNumber);
    public final static FieldInfo priceEffectiveDate = new FieldInfo(FIELD_NAME_CONST.priceEffectiveDate);
    public final static StringFieldInfo currencyPriceCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR,
        FIELD_NAME_CONST.currencyPriceCode, 3, null);
    public final static AmountFieldInfo offerPriceAmount = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR,
        FIELD_NAME_CONST.offerPriceAmount, false, 9, 5);
    public final static AmountFieldInfo bidPriceAmount = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR,
        FIELD_NAME_CONST.bidPriceAmount, false, 9, 5);
    public final static RateFieldInfo performanceCumulativePercent = new RateFieldInfo(RateFieldInfo.THROW_ERROR,
        FIELD_NAME_CONST.performanceCumulativePercent, 7, 2);
    public final static RateFieldInfo performance1MonthsPercent = new RateFieldInfo(RateFieldInfo.THROW_ERROR,
        FIELD_NAME_CONST.performance1MonthsPercent, 7, 2);
    public final static RateFieldInfo performance6MonthsPercent = new RateFieldInfo(RateFieldInfo.THROW_ERROR,
        FIELD_NAME_CONST.performance6MonthsPercent, 7, 2);
    public final static RateFieldInfo performance1YearPercent = new RateFieldInfo(RateFieldInfo.THROW_ERROR,
        FIELD_NAME_CONST.performance1YearPercent, 7, 2);
    public final static RateFieldInfo performance3YearsPercent = new RateFieldInfo(RateFieldInfo.THROW_ERROR,
        FIELD_NAME_CONST.performance3YearsPercent, 7, 2);
    public final static RateFieldInfo performance5YearsPercent = new RateFieldInfo(RateFieldInfo.THROW_ERROR,
        FIELD_NAME_CONST.performance5YearsPercent, 7, 2);
    public final static RateFieldInfo performanceSinceLaunchPercent = new RateFieldInfo(RateFieldInfo.THROW_ERROR,
        FIELD_NAME_CONST.performanceSinceLaunchPercent, 7, 2);
    public final static FieldInfo performanceEffectiveDate = new FieldInfo(FIELD_NAME_CONST.performanceEffectiveDate);
    public final static FieldInfo performanceUpdateDate = new FieldInfo(FIELD_NAME_CONST.performanceUpdateDate);
}
