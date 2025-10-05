/*
 */
package com.hhhh.group.secwealth.mktdata.quote.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.common.ProdCdeAltClassCdeEnum;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.service.AbstractService;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.common.util.*;
import com.hhhh.group.secwealth.mktdata.quote.beans.request.QuoteDetailRequest;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.QuoteDetailResponse;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.common.*;
import com.hhhh.group.secwealth.mktdata.quote.dao.FundQuoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * The Class QuoteDetailServiceImpl.
 */
@Service("quoteDetailService")
public class QuoteDetailServiceImpl extends AbstractService {

    @Autowired
    @Qualifier("fundQuoteDao")
    private FundQuoteDao fundQuoteDao;

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    @Override
    public Object execute(final Object object) throws Exception {
        QuoteDetailRequest request = (QuoteDetailRequest) object;

        String prodCdeAltClassCde = request.getProdCdeAltClassCde();
        String countryCode = request.getCountryCode();
        String groupMember = request.getGroupMember();
        String prodAltNum = request.getProdAltNum();
        String countryTradableCode = request.getMarket();
        String productType = request.getProductType();
        String locale = request.getLocale();
        Map<String, String> headers = request.getHeaders();

        SearchProduct searchProduct = this.internalProductKeyUtil.getProductBySearchWithAltClassCde(prodCdeAltClassCde, countryCode,
            groupMember, prodAltNum, countryTradableCode, productType, locale);
        int index =
            this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + request.getLocale());

        QuoteDetailResponse response = new QuoteDetailResponse();
        if (null != searchProduct && null != searchProduct.getSearchObject()) {
            SearchableObject searchObject = searchProduct.getSearchObject();
            QuoteData priceQuote = new QuoteData();
            priceQuote.setMarket(searchObject.getCountryTradableCode());
            priceQuote.setProductType(searchObject.getProductType());
            // priceQuote.setRic(searchObject.getKey());
            priceQuote.setSymbol(searchObject.getSymbol());
            priceQuote.setCompanyName(searchObject.getProductName());
            priceQuote.setProdShoreLocCde(searchObject.getProdShoreLocCde());
            priceQuote.setAllowSellMipProdInd(searchObject.getAllowSellMipProdInd());
            priceQuote.setProductSubTypeCode(searchObject.getProductSubType());
            // priceQuote.setCurrency(searchObject.getProductCcy());
            priceQuote.setQuoteIndicator(request.getDelay());
            // priceQuote.setAllowBuy(searchObject.getAllowBuy());
            // priceQuote.setAllowSell(searchObject.getAllowSell());

            List<InternalProductKey> ipkList = new ArrayList<InternalProductKey>();
            InternalProductKey ipk = new InternalProductKey();
            ipk.setCountryTradableCode(countryTradableCode);
            ipk.setProdAltNum(prodAltNum);
            ipk.setProductType(productType);
            ipkList.add(ipk);

            List<UtProdInstm> utProdInstmList = this.fundQuoteDao.search(ipkList, headers);
            Map<Integer, List<String>> utProdChanl = this.fundQuoteDao.searchChanlFund(request.getChannelRestrictCode());
            if (ListUtil.isValid(utProdInstmList) && utProdInstmList.get(0) != null) {
                UtProdInstm utProdInstm = utProdInstmList.get(0);
                priceQuote.setPiFundInd(utProdInstm.getPiFundInd());
                priceQuote.setDeAuthFundInd(utProdInstm.getDeAuthFundInd());
                priceQuote.setCurrency(utProdInstm.getClosingPrcCcy());
                priceQuote.setPriceQuote(utProdInstm.getDayEndNAV());
                priceQuote.setChangeAmount(utProdInstm.getChangeAmountNAV());
                priceQuote.setChangePercent(utProdInstm.getMarketPrice());
                priceQuote.setRiskLvlCde(utProdInstm.getRiskLvlCde());
                priceQuote.setTradableCurrency(utProdInstm.getCcyProdTradeCde());
                if (null != utProdInstm.getAsOfDate()) {
                    priceQuote.setExchangeUpdatedTime(
                        DateUtil.getSimpleDateFormat(utProdInstm.getAsOfDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
                }
                priceQuote.setDistributionFrequency(utProdInstm.getDistributionFrequency());
                priceQuote.setDividendYield(utProdInstm.getYield1Yr());
                Character topPerformersInd = utProdInstm.getTopPerformersInd();
                priceQuote.setTopPerformersIndicator(topPerformersInd == null ? null : topPerformersInd.toString());
                priceQuote.setTopSellProdIndex(utProdInstm.getTopSellProdIndex());
                priceQuote.setCategoryLevel1Code(utProdInstm.getCategoryLevel1Code());
                if (0 == index) {
                    priceQuote.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name1());
                } else if (1 == index) {
                    priceQuote.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name2());
                } else if (2 == index) {
                    priceQuote.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name3());
                } else {
                    priceQuote.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name1());
                }
                priceQuote.setLaunchDate(
                    DateUtil.getSimpleDateFormat(utProdInstm.getProdLaunchDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
                priceQuote.setCalendarReturns(this.getCalendarReturns(utProdInstm));
                priceQuote.setCumulativeReturns(this.getCumulativeReturns(utProdInstm));

                if (!utProdChanl.isEmpty() && utProdChanl.containsKey(utProdInstm.getUtProdInstmId().getProductId())) {
                    List<String> indicate = utProdChanl.get(utProdInstm.getUtProdInstmId().getProductId());
                    if (StringUtil.isValid(indicate.get(0)) && StringUtil.isValid(indicate.get(1))) {
                        priceQuote.setAllowBuy(indicate.get(0));
                        priceQuote.setAllowSell(indicate.get(1));
                    } else {
                        priceQuote.setAllowBuy(utProdInstm.getAllowBuyProdInd());
                        priceQuote.setAllowSell(utProdInstm.getAllowSellProdInd());
                    }
                } else {
                    priceQuote.setAllowBuy(utProdInstm.getAllowBuyProdInd());
                    priceQuote.setAllowSell(utProdInstm.getAllowSellProdInd());
                }
                priceQuote.setBidPrice(utProdInstm.getBidclosingPrc());
                priceQuote.setBidPriceDate(
                    DateUtil.getSimpleDateFormat(utProdInstm.getBidPriceDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
                priceQuote.setAskPrice(utProdInstm.getAskclosingPrc());
                response.setPriceQuote(priceQuote);
            } else {
                LogUtil.warn(QuoteDetailServiceImpl.class,
                    "Can not get record from DB, No record found for QuoteDetail|ipkList=", JacksonUtil.beanToJson(ipkList));
            }

            String timeZoneStr = request.getEntityTimezone();
            if (StringUtil.isInvalid(timeZoneStr)) {
                timeZoneStr = "";
            }
            TimeZone timeZone = TimeZone.getTimeZone(timeZoneStr);
            String newDateStr = DateUtil.newDate2String(timeZone, DateConstants.DateFormat_yyyyMMddHHmmss);
            String iso8601Time = DateUtil.stringToISO8601DateString(newDateStr, DateConstants.DateFormat_yyyyMMddHHmmss,
                DateConstants.DateFormat_yyyyMMddHHmmssSSSZ_ISO8601, timeZone);
            response.setEntityUpdatedTime(iso8601Time);
            // add ProdAltNumSegList And PerformanceId
            addProdAltNumSegListAndPerformanceId(searchObject, response);
        } else {
            LogUtil.error(QuoteDetailServiceImpl.class, "No record found for QuoteDetail|symbol=" + request.getProdAltNum());
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND);
        }
        return response;
    }

    private void addProdAltNumSegListAndPerformanceId(final SearchableObject obj, final QuoteDetailResponse response) {
        if (null != obj && null != response) {
            response.setProdAltNumSegs(obj.getProdAltNumSeg());
            if (null != response.getPriceQuote()) {
                response.getPriceQuote()
                    .setPerformanceId(this.internalProductKeyUtil.getProductCodeValueByCodeType(ProdCdeAltClassCdeEnum.O, obj));
            }
        }
    }

    private CumulativeReturns getCumulativeReturns(final UtProdInstm utProdInstm) {
        CumulativeReturns returns = new CumulativeReturns();
        returns.setMonthEndDate(
            DateUtil.getSimpleDateFormat(utProdInstm.getMonthEndDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        returns
            .setDayEndDate(DateUtil.getSimpleDateFormat(utProdInstm.getAsOfDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        List<FundCumulativeReturn> items = new ArrayList<FundCumulativeReturn>();

        FundCumulativeReturn fundCumulativeReturnYTD = new FundCumulativeReturn();
        fundCumulativeReturnYTD.setPeriod("YTD");
        fundCumulativeReturnYTD.setTrailingTotalReturn(utProdInstm.getReturnYTD());
        fundCumulativeReturnYTD.setDailyPerformanceNAV(utProdInstm.getReturnytdDaily());
        items.add(fundCumulativeReturnYTD);

        FundCumulativeReturn fundCumulativeReturn1M = new FundCumulativeReturn();
        fundCumulativeReturn1M.setPeriod("1M");
        fundCumulativeReturn1M.setTrailingTotalReturn(utProdInstm.getReturn1mth());
        fundCumulativeReturn1M.setDailyPerformanceNAV(utProdInstm.getReturn1mthDaily());
        items.add(fundCumulativeReturn1M);

        FundCumulativeReturn fundCumulativeReturn3M = new FundCumulativeReturn();
        fundCumulativeReturn3M.setPeriod("3M");
        fundCumulativeReturn3M.setTrailingTotalReturn(utProdInstm.getReturn3mth());
        fundCumulativeReturn3M.setDailyPerformanceNAV(utProdInstm.getReturn3mthDaily());
        items.add(fundCumulativeReturn3M);

        FundCumulativeReturn fundCumulativeReturn6M = new FundCumulativeReturn();
        fundCumulativeReturn6M.setPeriod("6M");
        fundCumulativeReturn6M.setTrailingTotalReturn(utProdInstm.getReturn6mth());
        fundCumulativeReturn6M.setDailyPerformanceNAV(utProdInstm.getReturn6mthDaily());
        items.add(fundCumulativeReturn6M);

        FundCumulativeReturn fundCumulativeReturn1Y = new FundCumulativeReturn();
        fundCumulativeReturn1Y.setPeriod("1Y");
        fundCumulativeReturn1Y.setTrailingTotalReturn(utProdInstm.getReturn1yr());
        fundCumulativeReturn1Y.setDailyPerformanceNAV(utProdInstm.getReturn1yrDaily());
        items.add(fundCumulativeReturn1Y);

        FundCumulativeReturn fundCumulativeReturn3Y = new FundCumulativeReturn();
        fundCumulativeReturn3Y.setPeriod("3Y");
        fundCumulativeReturn3Y.setTrailingTotalReturn(utProdInstm.getReturn3yr());
        fundCumulativeReturn3Y.setDailyPerformanceNAV(utProdInstm.getReturn3yrDaily());
        items.add(fundCumulativeReturn3Y);

        FundCumulativeReturn fundCumulativeReturn5Y = new FundCumulativeReturn();
        fundCumulativeReturn5Y.setPeriod("5Y");
        fundCumulativeReturn5Y.setTrailingTotalReturn(utProdInstm.getReturn5yr());
        fundCumulativeReturn5Y.setDailyPerformanceNAV(utProdInstm.getReturn5yrDaily());
        items.add(fundCumulativeReturn5Y);

        FundCumulativeReturn fundCumulativeReturn10Y = new FundCumulativeReturn();
        fundCumulativeReturn10Y.setPeriod("10Y");
        fundCumulativeReturn10Y.setTrailingTotalReturn(utProdInstm.getReturn10yr());
        fundCumulativeReturn10Y.setDailyPerformanceNAV(utProdInstm.getReturn10yrDaily());
        items.add(fundCumulativeReturn10Y);

        returns.setItems(items);
        return returns;
    }

    private CalendarReturns getCalendarReturns(final UtProdInstm utProdInstm) {
        CalendarReturns returns = new CalendarReturns();
        returns.setMonthEndDate(
            DateUtil.getSimpleDateFormat(utProdInstm.getMonthEndDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        List<FundCalendarYearReturn> items = new ArrayList<FundCalendarYearReturn>();
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.YEAR, -1);
        FundCalendarYearReturn fundCalendarYearReturn1 = new FundCalendarYearReturn();
        fundCalendarYearReturn1
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn1.setAnnualCalendarYearReturn(utProdInstm.getYear1());
        items.add(fundCalendarYearReturn1);

        cal.add(Calendar.YEAR, -1);
        FundCalendarYearReturn fundCalendarYearReturn2 = new FundCalendarYearReturn();
        fundCalendarYearReturn2
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn2.setAnnualCalendarYearReturn(utProdInstm.getYear2());
        items.add(fundCalendarYearReturn2);

        cal.add(Calendar.YEAR, -1);
        FundCalendarYearReturn fundCalendarYearReturn3 = new FundCalendarYearReturn();
        fundCalendarYearReturn3
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn3.setAnnualCalendarYearReturn(utProdInstm.getYear3());
        items.add(fundCalendarYearReturn3);

        cal.add(Calendar.YEAR, -1);
        FundCalendarYearReturn fundCalendarYearReturn4 = new FundCalendarYearReturn();
        fundCalendarYearReturn4
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn4.setAnnualCalendarYearReturn(utProdInstm.getYear4());
        items.add(fundCalendarYearReturn4);

        cal.add(Calendar.YEAR, -1);
        FundCalendarYearReturn fundCalendarYearReturn5 = new FundCalendarYearReturn();
        fundCalendarYearReturn5
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn5.setAnnualCalendarYearReturn(utProdInstm.getYear5());
        items.add(fundCalendarYearReturn5);

        returns.setItems(items);
        return returns;
    }
}