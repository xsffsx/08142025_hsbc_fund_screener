/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name		INVST_PROD_FIELD_CONST
 *
 * Creation Date	Apr 12, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Apr 12, 2006
 *    CMM/PPCR No.		
 *    Programmer		cbdhkg0010
 *    Description
 * 
 */
package com.dummy.wpc.datadaptor.constant;


public class INVST_PROD_FIELD_CONST {

    public class FIELD_NAME_CONST{
        public final static String currencyCode = "Base Currency";
        public final static String currencyTradeableCode = "Tradable Currency";
        public final static String periodProductCode = "Tenor Type";
        public final static String periodProductNumber = "Tenor Number";
        public final static String tenorInDaysNumber = "Tenor in Days"; 
        public final static String launchDate = "Launch Date";
        public final static String maturityDate = "Maturity Date";
        public final static String marketInvestmentCode = "Investment Market";
        public final static String regionInvestmentCode = "Investment Region";
        public final static String industrySectorCode = "Industry Sector";
        public final static String sectorCode = "Sector";
        public final static String allowBuyIndicator = "Allow Buy Indicator";
        public final static String allowSellUnitIndicator = "Allow Sell (in Unit) Indicator";
        public final static String allowBuyUnitIndicator = "Allow Buy (in Unit) Indicator";
        public final static String allowSellAmountIndicator = "Allow Sell (in Amount) Indicator";
        public final static String allowBuyAmountIndicator = "Allow Buy (in Amount) Indicator";
        public final static String allowSellIndicator = "Allow Sell Indicator";
        public final static String allowMonthlyInvestmentProgramIndicator = "Allow MIP Indicator";
        public final static String allowMonthlyInvestmentProgramUnitIndicator = "Allow MIP (in Unit) Indicator";
        public final static String allowMonthlyInvestmentProgramAmountIndicator = "Allow MIP (in Amount) Indicator";
        public final static String allowSwitchInIndicator = "Allow Switch In Indicator";
        public final static String allowSwitchInUnitIndicator = "Allow Switch In (in Unit) Indicator";
        public final static String allowSwitchInAmountIndicator = "Allow Switch In (in Amount) Indicator";
        public final static String allowSwitchOutIndicator = "Allow Switch Out Indicator";
        public final static String allowSwitchOutUnitIndicator = "Allow Switch Out (in Unit) Indicator";
        public final static String allowSwitchOutAmountIndicator = "Allow Switch Out (in Amount) Indicator";
        public final static String incomeIndicator = "Investment Objective : Income Indicator";
        public final static String capitalProtectionIndicator = "Investment Objective : Capital Protection Indicator";
        public final static String yieldEnhancementIndicator = "Investment Objective : Yield Enhancement Indicator";
        public final static String growthIndicator = "Investment Objective : Growth Indicator";
        public final static String averagePeriodReturn = "Average Period Return";
        public final static String averageVolatility = "Average Volatility";    
        public final static String statCde = "Status Code";
        public final static String unitPriceCde = "Price Divisor Number";
        public final static String addInfo = "Additional Information";
        public final static String addPllInfo = "Additional Pll Information";
        public final static String addSllInfo = "Additional Sll Information";
    }
    
    public final static StringFieldInfo currencyCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.currencyCode, 3, null);
    public final static StringFieldInfo currencyTradeableCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.currencyTradeableCode, 3, null);
    public final static StringFieldInfo periodProductCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.tenorInDaysNumber, 15, null); 
    public final static RateFieldInfo periodProductNumber = new RateFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.periodProductNumber, 5, 0);
    public final static RateFieldInfo tenorInDaysNumber = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.tenorInDaysNumber, 5, 0);
    public final static FieldInfo launchDate = new FieldInfo(FIELD_NAME_CONST.launchDate);
    public final static FieldInfo maturityDate = new FieldInfo(FIELD_NAME_CONST.maturityDate);
    public final static StringFieldInfo marketInvestmentCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.marketInvestmentCode, 15, null);
    public final static StringFieldInfo regionInvestmentCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.regionInvestmentCode, 15, null);
    public final static StringFieldInfo industrySectorCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.industrySectorCode, 15, null);
    public final static StringFieldInfo sectorCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.sectorCode, 10, null);
    public final static StringFieldInfo allowBuyIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowBuyIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowSellUnitIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowSellUnitIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowBuyUnitIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowBuyUnitIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowSellAmountIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowSellAmountIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowBuyAmountIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowBuyAmountIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowSellIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowSellUnitIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowMonthlyInvestmentProgramIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowMonthlyInvestmentProgramIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowMonthlyInvestmentProgramUnitIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowMonthlyInvestmentProgramUnitIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowMonthlyInvestmentProgramAmountIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowMonthlyInvestmentProgramAmountIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowSwitchInIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowSwitchInIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowSwitchInUnitIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowSwitchInUnitIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowSwitchInAmountIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowSwitchInAmountIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowSwitchOutIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowSwitchOutIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowSwitchOutUnitIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowSwitchOutUnitIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo allowSwitchOutAmountIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.allowSwitchOutAmountIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo incomeIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.incomeIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo capitalProtectionIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.capitalProtectionIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo yieldEnhancementIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.yieldEnhancementIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo growthIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.growthIndicator, 1, new String[]{"Y", "N"});
    public final static RateFieldInfo averagePeriodReturn = new RateFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.averagePeriodReturn, 13, 5);
    public final static RateFieldInfo averageVolatility = new RateFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.averageVolatility, 13, 5);
    public final static StringFieldInfo statCde = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.statCde, 1, null);
    public final static RateFieldInfo unitPriceCde = new RateFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.unitPriceCde, 100, 0);
    public final static StringFieldInfo addInfo = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.addInfo, 1000, null);
    public final static StringFieldInfo addPllInfo = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.addPllInfo, 3000, null);
    public final static StringFieldInfo addSllInfo = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.addSllInfo, 3000, null);
}
