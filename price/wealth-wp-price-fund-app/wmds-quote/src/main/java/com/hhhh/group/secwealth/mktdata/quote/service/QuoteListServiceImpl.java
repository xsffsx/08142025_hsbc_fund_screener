/*
 */
package com.hhhh.group.secwealth.mktdata.quote.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.common.service.AbstractService;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.util.*;
import com.hhhh.group.secwealth.mktdata.quote.beans.request.ProductKey;
import com.hhhh.group.secwealth.mktdata.quote.beans.request.QuoteListRequest;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.QuoteListResponse;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.common.*;
import com.hhhh.group.secwealth.mktdata.quote.dao.FundQuoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("quoteListService")
public class QuoteListServiceImpl extends AbstractService {

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    @Autowired
    @Qualifier("fundQuoteDao")
    private FundQuoteDao fundQuoteDao;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hhhh.group.secwealth.mktdata.common.service.AbstractService#execute
     * (java.util.Map, java.lang.Object)
     */
    @Override
    public Object execute(final Object object) throws Exception {
        long startTime = System.currentTimeMillis();
        QuoteListRequest request = (QuoteListRequest) object;
        String countryCode = request.getCountryCode();
        String groupMember = request.getGroupMember();
        String countryTradableCode = request.getMarket();
        String locale = request.getLocale();
        Map<String, String> headers = request.getHeaders();

        QuoteListResponse response = new QuoteListResponse();
        List<ProductKey> productKeys = request.getProductKeys();
        List<PriceQuote> priceQuotes = new ArrayList<PriceQuote>();
        List<InternalProductKey> ipkList = new ArrayList<InternalProductKey>();
        if (ListUtil.isValid(productKeys)) {
            for (int i = 0; i < productKeys.size(); i++) {
                ProductKey productKey = productKeys.get(i);
                String prodCdeAltClassCde = productKey.getProdCdeAltClassCde();
                String productType = productKey.getProductType();
                String prodAltNum = productKey.getProdAltNum();

                SearchProduct searchProduct = this.internalProductKeyUtil.getProductBySearchWithAltClassCde(prodCdeAltClassCde,
                    countryCode, groupMember, prodAltNum, countryTradableCode, productType, locale);
                if (null != searchProduct && null != searchProduct.getSearchObject()) {
                    SearchableObject searchObject = searchProduct.getSearchObject();
                    PriceQuote quote = new PriceQuote();
                    quote.setMarket(searchObject.getCountryTradableCode());
                    quote.setProductType(searchObject.getProductType());
                    quote.setSymbol(prodAltNum);
                    // /quote.setRic(searchProduct.getExternalKey());
                    quote.setCompanyName(searchObject.getProductName());
                    quote.setDelay(request.getDelay());
                    quote.setProdAltNumSegs(searchObject.getProdAltNumSeg());
                    // quote.setCurrency(searchObject.getProductCcy());

                    InternalProductKey ipk = new InternalProductKey();
                    ipk.setCountryTradableCode(searchObject.getCountryTradableCode());
                    ipk.setProdAltNum(prodAltNum);
                    ipk.setProductType(productType);
                    ipkList.add(ipk);

                    priceQuotes.add(quote);
                } else {
                    LogUtil.errorBeanToJson(QuoteListServiceImpl.class, "No record found for QuoteList|productKeys=", productKey);
                }
            }
            // get data from DB and merge data
            if (priceQuotes.size() > 0 && ListUtil.isValid(ipkList)) {
                long startTimeDB = System.currentTimeMillis();
                List<UtProdInstm> utProdInstmList = this.fundQuoteDao.search(ipkList, headers);
                if (ListUtil.isValid(utProdInstmList)) {
                    long endTimeDB = System.currentTimeMillis();
//                    LogUtil.debug(QuoteListServiceImpl.class, "DB Spend Time : " + Long.toString(endTimeDB - startTimeDB));
                    int index = this.localeMappingUtil
                        .getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + request.getLocale());
                    for (PriceQuote quote : priceQuotes) {
                        for (UtProdInstm utProdInstm : utProdInstmList) {
                            if (quote.getSymbol().equalsIgnoreCase(utProdInstm.getSymbol())
                                && quote.getProductType().equalsIgnoreCase(utProdInstm.getProductType())
                                && quote.getMarket().equalsIgnoreCase(utProdInstm.getMarket())) {

                                quote.setCurrency(utProdInstm.getClosingPrcCcy());
                                quote.setPriceQuote(utProdInstm.getDayEndNAV());
                                quote.setChangeAmount(utProdInstm.getChangeAmountNAV());
                                quote.setChangePercent(utProdInstm.getMarketPrice());
                                quote.setDividendYield(utProdInstm.getYield1Yr());
                                quote.setDistributionFrequency(utProdInstm.getDistributionFrequency());
                                Character topPerformersInd = utProdInstm.getTopPerformersInd();
                                quote.setTopPerformersIndicator(topPerformersInd == null ? null : topPerformersInd.toString());
                                quote.setTopSellProdIndex(utProdInstm.getTopSellProdIndex());
                                quote.setCategoryLevel1Code(utProdInstm.getCategoryLevel1Code());
                                if (0 == index) {
                                    quote.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name1());
                                } else if (1 == index) {
                                    quote.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name2());
                                } else if (2 == index) {
                                    quote.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name3());
                                } else {
                                    quote.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name1());
                                }
                                if (null != utProdInstm.getAsOfDate()) {
                                    quote.setExchangeUpdatedTime(DateUtil.getSimpleDateFormat(utProdInstm.getAsOfDate(),
                                        DateConstants.DateFormat_yyyyMMdd_withHyphen));
                                }
                                quote.setRiskLvlCde(utProdInstm.getRiskLvlCde());
                                quote.setLaunchDate(DateUtil.getSimpleDateFormat(utProdInstm.getProdLaunchDate(),
                                    DateConstants.DateFormat_yyyyMMdd_withHyphen));
                                quote.setBidPrice(utProdInstm.getBidclosingPrc());
                                quote.setBidPriceDate(DateUtil.getSimpleDateFormat(utProdInstm.getBidPriceDate(),
                                    DateConstants.DateFormat_yyyyMMdd_withHyphen));
                                quote.setAskPrice(utProdInstm.getAskclosingPrc());
                                quote.setCalendarReturns(this.getCalendarReturns(utProdInstm));
                                quote.setCumulativeReturns(this.getCumulativeReturns(utProdInstm));
                                break;
                            }
                        }
                    }
                } else {
                    LogUtil.errorBeanToJson(QuoteDetailServiceImpl.class,
                        "Can not get record from DB, No record found for QuoteList|ipkList=", ipkList);
                }
            }

            response.setPriceQuotes(priceQuotes);
            response.setEntityTimezone(request.getEntityTimezone());
            String timeZoneStr = request.getEntityTimezone();
            if (StringUtil.isInvalid(timeZoneStr)) {
                timeZoneStr = "";
            }
            TimeZone timeZone = TimeZone.getTimeZone(timeZoneStr);
            String newDateStr = DateUtil.newDate2String(timeZone, DateConstants.DateFormat_yyyyMMddHHmmss);
            String iso8601Time = DateUtil.stringToISO8601DateString(newDateStr, DateConstants.DateFormat_yyyyMMddHHmmss,
                DateConstants.DateFormat_yyyyMMddHHmmssSSSZ_ISO8601, timeZone);
            response.setEntityUpdatedTime(iso8601Time);
        }
        long endTime = System.currentTimeMillis();
//        LogUtil.debug(QuoteListServiceImpl.class, "Function Spend Time : " + Long.toString(endTime - startTime));
        return response;
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
