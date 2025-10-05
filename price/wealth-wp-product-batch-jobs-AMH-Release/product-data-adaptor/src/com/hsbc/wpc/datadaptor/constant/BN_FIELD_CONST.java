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
 * Class Name		BN_FIELD_CONST
 *
 * Creation Date	May 10, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	May 10, 2006
 *    CMM/PPCR No.		
 *    Programmer		cbdhkg0010
 *    Description
 * 
 */
package com.dummy.wpc.datadaptor.constant;


public class BN_FIELD_CONST {

    public class FIELD_NAME_CONST {
        public final static String issuerCode = "Issuer Code";
        public final static String issuerNumber = "Issuer Number";
        public final static String issueDate = "Issue Date";
        public final static String frequencyCouponCode = "Coupon Frequency Type";
        public final static String couponAnnualPercent = "Coupon Rate";
        public final static String couponAnnualText = "Coupon Rate in Text";
        public final static String couponAnnualExtendedText = "Extended Coupon Rate in Text";
        public final static String paymentNextCouponDate = "Next Coupon Payment Date";
        public final static String extendableCallableIndicator = "Extendable/Callable Indicator";
        public final static String interestIndicativeAccrueAmount = "Indicative Accrual Interest";
        public final static String investmentInitialMinimumAmount = "Minimal Initial Amount";
        public final static String investmentIncrementMinimumAmount = "Minimal Increment Amount";
        public final static String lotSizeNumber = "Lot Size number";
        public final static String maturityExtendedDate = "Extended Maturity date";
        public final static String bidPriceAmount = "Bid Price";
        public final static String offerPriceAmount = "Offer Price";
        public final static String bidYield = "Bid Yield";
        public final static String bidYieldText = "Bid Yield in Text";
        public final static String bidYieldToCallText = "Bid Yield to Call in Text";
        public final static String bidYieldToMaturityText = "Bid Yield to Maturity in Text";
        public final static String offerYieldText = "Offer Yield in Text";
        public final static String offerYieldPercent = "Offer Yield";
        public final static String offerYieldToCallText = "Offer Yield to Call in Text";
        public final static String offerYieldToMaturityText = "Offer Yield to Maturity in Text";
        public final static String overdraftSecurePercent = "Secure Overdraft Percent";
        public final static String subordinatedIndicator = "Subordinated Indicator";
        
//      New fields added for Online Bond Project
        public final static String investmentIncrementMinimumSellAmount = "Minimal Increment Sell Amount";
        public final static String investmentInitialMinimumSellAmount = "Minimal Initial Sell Amount";
        public final static String bondName = "Bond Name";
        public final static String bondType = "Bond Subtype Code";
        public final static String isinCode = "ISIN Code";
        public final static String bloombergID = "Bloomberg ID";
        public final static String guarantor = "Guarantor";
        public final static String issueCountry = "Issue Country";
		public final static String callDate = "Next callable date";
		public final static String couponType = "Coupon Type";
		public final static String previousCouponDate = "Previous Coupon date";
		public final static String capitalTier = "Capital Tier";
		public final static String interestRateBasis = "Interest Rate Basis";
		public final static String daysAccrued = "Days accrued";
		public final static String closingBidPrice = "Closing Bid Price";
		public final static String closingOfferPrice = "Closing Offer Price";
		public final static String closingBidYield = "Closing Bid Yield";
		public final static String closingOfferYield = "Closing Offer Yield";
		public final static String closingDate = "Closing Date";
		public final static String floatingRateIndex = "Floating Rate Index";
		public final static String floatingRateSpread = "Floating Rate Spread";
		public final static String previousCouponDate2 = "Previous Coupon Date 2";
		public final static String previousCouponRate = "Previous Coupon Rate";
		public final static String businessStartTime = "Business Start Time";
		public final static String businessEndTime = "Business End Time";
		public final static String remark = "Remark";
		public final static String remark2 = "Remark2";
		public final static String riskLevel = "Risk Level";
		public final static String cmplxProdInd = "Is Complex";
		public final static String bondStatCde = "Bond Status Code";
		public final static String TradeBidSz = "Trade Bid Size";
		public final static String TradeAskSz = "Trade Ask Size";
		public final static String BidYieldPct = "Bid Price Perent";
		public final static String CloseBidYield = "Closed Bid Yield";
		public final static String CloseOfferYield = "Closed Offer Yield";
		public final static String BankSellDscnMrgn = "Bank Sell Discant Margin";
		public final static String BankBuyDscnMrgn = "Bank Buy Discant Margin";
		public final static String PriceReceiveDateTime = "Price Receive Date Time";
		//public final static String 
    }
    public final static StringFieldInfo issuerCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.issuerCode, 100, null);
    public final static StringFieldInfo issuerNumber = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.issuerNumber, 40, null);
    public final static StringFieldInfo issueDate = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.issueDate, 10, null);
    public final static StringFieldInfo frequencyCouponCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.frequencyCouponCode, 1, null);
    public final static AmountFieldInfo couponAnnualPercent = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR, FIELD_NAME_CONST.couponAnnualPercent, false, 20, 6);
    public final static StringFieldInfo couponAnnualText = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.couponAnnualText, 100, null);
    public final static StringFieldInfo couponAnnualExtendedText = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.couponAnnualExtendedText, 100, null);
    public final static StringFieldInfo paymentNextCouponDate = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.paymentNextCouponDate, 10, null);
    public final static StringFieldInfo extendableCallableIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.extendableCallableIndicator, 1, null);
    public final static AmountFieldInfo interestIndicativeAccrueAmount = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR, FIELD_NAME_CONST.interestIndicativeAccrueAmount, false, 25, 10);
    public final static AmountFieldInfo investmentInitialMinimumAmount = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR, FIELD_NAME_CONST.investmentInitialMinimumAmount, false, 20, 4);
    public final static AmountFieldInfo investmentIncrementMinimumAmount = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR, FIELD_NAME_CONST.investmentIncrementMinimumAmount, false, 20, 4);
    public final static RateFieldInfo lotSizeNumber = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.lotSizeNumber, 20, 6);
    public final static StringFieldInfo maturityExtendedDate = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.maturityExtendedDate, 10, null);
    public final static AmountFieldInfo bidPriceAmount = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR, FIELD_NAME_CONST.bidPriceAmount, false, 20, 4);
    public final static AmountFieldInfo offerPriceAmount = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR, FIELD_NAME_CONST.offerPriceAmount, false, 20, 4);
    public final static RateFieldInfo bidYield = new RateFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.bidYield, 20, 4);
    public final static StringFieldInfo bidYieldText = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.bidYieldText, 100, null);
    public final static StringFieldInfo bidYieldToCallText = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.bidYieldToCallText, 100, null);
    public final static StringFieldInfo bidYieldToMaturityText = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.bidYieldToMaturityText, 100, null);
    public final static StringFieldInfo offerYieldText = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.offerYieldText, 100, null);
    public final static RateFieldInfo offerYieldPercent = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.offerYieldPercent, 20, 4);
    public final static StringFieldInfo offerYieldToCallText = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.offerYieldToCallText, 100, null);
    public final static StringFieldInfo offerYieldToMaturityText = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.offerYieldToMaturityText, 100, null);
    public final static RateFieldInfo overdraftSecurePercent = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.overdraftSecurePercent, 13, 0);
    public final static StringFieldInfo subordinatedIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.subordinatedIndicator, 1, null);
   
    //New fields added for Online Bond Project
    public final static AmountFieldInfo investmentIncrementMinimumSellAmount = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR, FIELD_NAME_CONST.investmentIncrementMinimumSellAmount, false, 20, 4);
    public final static AmountFieldInfo investmentInitialMinimumSellAmount = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR, FIELD_NAME_CONST.investmentInitialMinimumSellAmount, false, 20, 4);
    public final static StringFieldInfo bondName = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.bondName, 100, null);
    public final static StringFieldInfo bondType = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.bondType, 50, null);//TODO : 30
    public final static StringFieldInfo isinCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.isinCode, 12, null);
    public final static StringFieldInfo bloombergID = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.bloombergID, 30, null);
    public final static StringFieldInfo guarantor = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.guarantor, 100, null);
    public final static StringFieldInfo issueCountry = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.issueCountry, 30, null);
    public final static StringFieldInfo callDate = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.callDate, 10, null);
    public final static StringFieldInfo couponType = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.couponType, 20, null);
    public final static StringFieldInfo previousCouponDate = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.previousCouponDate, 10, null);
    public final static StringFieldInfo capitalTier = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.capitalTier, 100, null);
    public final static StringFieldInfo interestRateBasis = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.interestRateBasis, 100, null);
    public final static RateFieldInfo daysAccrued = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.daysAccrued, 20, 6);
    public final static AmountFieldInfo closingBidPriceAmount = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR, FIELD_NAME_CONST.closingBidPrice, false, 20, 4);
    public final static AmountFieldInfo closingOfferPriceAmount = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR, FIELD_NAME_CONST.closingOfferPrice, false, 20, 4);
    public final static AmountFieldInfo closingBidYield = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR, FIELD_NAME_CONST.closingBidYield, false, 20, 4);
    public final static AmountFieldInfo closingOfferYield = new AmountFieldInfo(AmountFieldInfo.THROW_ERROR, FIELD_NAME_CONST.closingOfferYield, false, 20, 4);
    public final static StringFieldInfo closingDate = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.closingDate, 10, null);
    public final static StringFieldInfo floatingRateIndex = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.floatingRateIndex, 50, null);
    public final static RateFieldInfo floatingRateSpread = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.floatingRateSpread, 20,4);
    public final static StringFieldInfo previousCouponDate2 = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.previousCouponDate2, 10, null);
    public final static RateFieldInfo previousCouponRate = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.previousCouponRate, 20, 6);   
    public final static StringFieldInfo businessStartTime = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.businessStartTime, 20, null);
    public final static StringFieldInfo businessEndTime = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.businessEndTime, 20, null);
    public final static StringFieldInfo remark = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.remark,500, null);
    public final static StringFieldInfo remark2 = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.remark2, 200, null);
    
    public final static StringFieldInfo riskLevel = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.riskLevel, 1, null);
    
    public final static StringFieldInfo bondStatCde = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.bondStatCde, 1, null);
    public final static StringFieldInfo cmplxProdInd = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.cmplxProdInd, 1, null);

    public final static RateFieldInfo TradeBidSz = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.TradeBidSz, 20,6);
    public final static RateFieldInfo TradeAskSz = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.TradeAskSz, 20,6);
    public final static RateFieldInfo BidYieldPct = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.BidYieldPct, 20,4);
    public final static RateFieldInfo CloseBidYield = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.CloseBidYield, 20,4);
    public final static RateFieldInfo CloseOfferYield = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.CloseOfferYield, 20,4);
    public final static RateFieldInfo BankSellDscnMrgn = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.BankSellDscnMrgn, 20,4);
    public final static RateFieldInfo BankBuyDscnMrgn = new RateFieldInfo(RateFieldInfo.THROW_ERROR, FIELD_NAME_CONST.BankBuyDscnMrgn, 20,4);
    public final static StringFieldInfo PriceReceiveDateTime = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.PriceReceiveDateTime, 30, null);
}