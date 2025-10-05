
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.Api;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.FBPrimaryProspectusBenchmarks;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.FM2Managers;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.FundListData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.ICMultilingualInvestmentStrategies;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.Manager;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.MultilingualStrategy;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.PrimaryProspectusBenchmark;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.ProspectusPrimaryIndex;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundListRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundListResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundCalendarYearReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundCumulativeReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Header;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.HoldingDetails;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.InvestmentStrategy;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.MgmtAndContactInfo;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.MorningstarRatings;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Performance;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Performance.CalendarYearTotalReturns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Performance.CumulativeTotalReturns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Profile;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Summary;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.YieldAndCredit;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.ManagemenInfo;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.Prospectus;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;


@Service("fundListServiceExecutor")
public class FundListServiceExecutor extends AbstractMstarService {

    @Value("#{systemConfig['mstar.conn.url.fundList']}")
    private String url;

    @Value("#{systemConfig['fundList.dataClass']}")
    private String dataClass;

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    @Resource(name = "multiLanguageForInvestmentStrategyMap")
    private Map<String, Map<String, String>> multiLanguageForInvestmentStrategyMap;

    private JAXBContext dataClassJC;

    private FastDateFormat formatter = FastDateFormat.getInstance(DateConstants.DateFormat_yyyyMMdd_withHyphen);

    @PostConstruct
    public void init() throws Exception {
        try {
            this.dataClassJC = JAXBContext.newInstance(Class.forName(this.dataClass));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Object execute(final Object object) throws Exception {
        FundListRequest request = (FundListRequest) object;

        // Invoke service
        Object[] arr = sendFundListRequest(request);
        if (null == arr || arr.length == 0) {
            return null;
        }
        FundListData fundListData = (FundListData) arr[0];

        // Prepare response
        FundListResponse response = new FundListResponse();
        if (null != fundListData) {
            Map<String, Data> mstarResponseMap = new HashMap<String, Data>();
            List<Data> datas = fundListData.getData();
            if (null != datas && datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    Data data = datas.get(i);
                    if (null != data) {
                        mstarResponseMap.put(data.getId(), data);
                    }
                }
            }

            if (arr.length == 2) {
                List<SearchProduct> products = (List<SearchProduct>) arr[1];
                response.setSearchProductList(products);
                List<FundListProduct> product = new ArrayList<FundListProduct>();
                for (SearchProduct prod : products) {
                    String symbol = null;
                    if (null != prod && null != prod.getSearchObject()) {
                        symbol = prod.getSearchObject().getSymbol();
                    }
                    String id = prod.getExternalKey();
                    FundListProduct fundListProduct = new FundListProduct();
                    Header header = fundListProduct.new Header();
                    Summary summary = fundListProduct.new Summary();
                    Profile profile = fundListProduct.new Profile();
                    Performance performance = fundListProduct.new Performance();
                    MorningstarRatings morningstarRatings = fundListProduct.new MorningstarRatings();
                    MgmtAndContactInfo mgmtAndContactInfo = fundListProduct.new MgmtAndContactInfo();
                    InvestmentStrategy investmentStrategy = fundListProduct.new InvestmentStrategy();
                    YieldAndCredit yieldAndCredit = fundListProduct.new YieldAndCredit();
                    HoldingDetails holdingDetails = fundListProduct.new HoldingDetails();

                    Data data = mstarResponseMap.get(id);
                    if (null != data) {
                        Api api = data.getApi();
                        if (null != api) {
                            // header.setName(api.getFSCBIFundName());
                            summary.setBid(BigDecimalUtil.fromStringAndCheckNull(api.getTsDayEndBidPrice()));
                            summary.setOffer(BigDecimalUtil.fromStringAndCheckNull(api.getTsDayEndOfferPrice()));
                            summary.setWeekRangeHigh(BigDecimalUtil.fromStringAndCheckNull(api.getTsnav52WkHigh()));
                            summary.setWeekRangeLow(BigDecimalUtil.fromStringAndCheckNull(api.getTsnav52WkLow()));

                            profile.setInvestmentObjectiveAndStrategy(
                                this.getInvestmentStrategyBySiteLocale(api, request.getSiteKey(), request.getLocale()));
                            profile.setDayEndBidOfferPricesDate(api.getTsDayEndBidOfferPricesDate());
                            profile.setDayEndNAVDate(api.getTsDayEndNAVDate());

                            holdingDetails.setDividendPerShare(api.getDpDividend());
                            holdingDetails.setExDividendDate(api.getDpDividendDate());
                        }
                        fundListProduct.setHeader(header);
                        fundListProduct.setSummary(summary);
                        fundListProduct.setProfile(profile);
                        fundListProduct.setPerformance(getPerformance(api, performance, data.getProspectusPrimaryIndexs()));
                        fundListProduct.setMorningstarRatings(getMorningstarRatings(api, morningstarRatings));
                        fundListProduct.setMgmtAndContactInfo(getMgmtAndContactInfo(api, mgmtAndContactInfo));
                        fundListProduct.setInvestmentStrategy(getInvestmentStrategy(api, investmentStrategy));
                        fundListProduct.setYieldAndCredit(getYieldAndCredit(api, yieldAndCredit));
                        fundListProduct.setHoldingDetails(holdingDetails);
                        fundListProduct.setProdAltNumXCode(id);
                        fundListProduct.setSymbol(symbol);

                        product.add(fundListProduct);
                    }
                }
                response.setProducts(product);
            }
        }
        return response;
    }


    protected Object[] sendFundListRequest(final FundListRequest request) throws Exception {
        List<ProductKey> productKeys = request.getProductKeys();
        String countryCode = request.getCountryCode();
        String groupMember = request.getGroupMember();
        String locale = request.getLocale();

        SearchProduct searchProduct = null;
        String ids = null;
        List<SearchProduct> list = new ArrayList<SearchProduct>();
        if (ListUtil.isValid(productKeys)) {
            StringBuilder productAltNum = new StringBuilder();
            for (ProductKey productKey : productKeys) {
                if (null != productKey) {
                    String altClassCde = productKey.getProdCdeAltClassCde();
                    String prodAltNum = productKey.getProdAltNum();
                    String countryTradableCode = productKey.getMarket();
                    String productType = productKey.getProductType();
                    searchProduct = this.internalProductKeyUtil.getProductBySearchWithAltClassCde(altClassCde, countryCode,
                        groupMember, prodAltNum, countryTradableCode, productType, locale);
                    if (null == searchProduct || null == searchProduct.getExternalKey()) {
                        LogUtil.error(FundListServiceExecutor.class, "No record found for Product|symbol=" + productKey.toString());
                        throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND,
                            "No record found for " + productKey.toString());
                    } else {
                        list.add(searchProduct);
                    }
                    productAltNum.append(searchProduct.getExternalKey());
                    productAltNum.append(CommonConstants.SYMBOL_COMMA);
                }
            }
            productAltNum.delete(productAltNum.length() - 1, productAltNum.length());
            ids = productAltNum.toString();
            Object response = super.sendRequest(MstarConstants.MSTARID, ids, this.url, this.dataClassJC.createUnmarshaller());
            Object[] arr = {response, list};
            return arr;
        } else {
            LogUtil.error(FundListServiceExecutor.class,
                "sendFundListRequest is fail, because there is a invalid productKeys" + request.toString());
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND);
        }
    }

    private Performance getPerformance(final Api api, final Performance performance,
        final List<ProspectusPrimaryIndex> prospectusPrimaryIndexs) throws Exception {
        // performance set Annualised returns
        CumulativeTotalReturns cumulativeTotalReturns = performance.new CumulativeTotalReturns();
        cumulativeTotalReturns.setItems(setCumulativeTotalReturns(api, prospectusPrimaryIndexs));
        cumulativeTotalReturns.setMptpiIndexId(api.getMptpiIndexId());
        cumulativeTotalReturns.setMptpiIndexName(api.getMptpiIndexName());
        cumulativeTotalReturns.setLastUpdatedDate(api.getTtrMonthEndDate());
        cumulativeTotalReturns.setDailyLastUpdatedDate(api.getDpDayEndDate());
        performance.setCumulativeTotalReturns(cumulativeTotalReturns);

        // performance set Calendar returns
        CalendarYearTotalReturns calendarYearTotalReturns = performance.new CalendarYearTotalReturns();
        calendarYearTotalReturns.setItems(setCalendarYearTotalReturns(api, prospectusPrimaryIndexs));
        calendarYearTotalReturns.setMptpiIndexId(api.getMptpiIndexId());
        calendarYearTotalReturns.setMptpiIndexName(api.getMptpiIndexName());
        calendarYearTotalReturns.setLastUpdatedDate(api.getTtrMonthEndDate());
        performance.setCalendarYearTotalReturns(calendarYearTotalReturns);
        return performance;
    }

    private List<FundCumulativeReturn> setCumulativeTotalReturns(final Api api,
        final List<ProspectusPrimaryIndex> prospectusPrimaryIndexs) throws Exception {
        List<FundCumulativeReturn> cumulativeItem = new ArrayList<FundCumulativeReturn>();

        FundCumulativeReturn fundCumulativeReturnYTD = new FundCumulativeReturn();
        fundCumulativeReturnYTD.setPeriod("YTD");
        if (null != api) {
            fundCumulativeReturnYTD
                .setTtrBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturnYTD()));
            fundCumulativeReturnYTD.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturnYTD()));
            fundCumulativeReturnYTD.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturnYTD()));
            fundCumulativeReturnYTD.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturnYTD()));
        }

        FundCumulativeReturn fundCumulativeReturn1M = new FundCumulativeReturn();
        fundCumulativeReturn1M.setPeriod("1M");
        if (null != api) {
            fundCumulativeReturn1M
                .setTtrBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn1Mth()));
            fundCumulativeReturn1M.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn1Mth()));
            fundCumulativeReturn1M.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn1Mth()));
            fundCumulativeReturn1M.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn1Mth()));
        }

        FundCumulativeReturn fundCumulativeReturn3M = new FundCumulativeReturn();
        fundCumulativeReturn3M.setPeriod("3M");
        if (null != api) {
            fundCumulativeReturn3M
                .setTtrBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn3Mth()));
            fundCumulativeReturn3M.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn3Mth()));
            fundCumulativeReturn3M.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn3Mth()));
            fundCumulativeReturn3M.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn3Mth()));
        }

        FundCumulativeReturn fundCumulativeReturn6M = new FundCumulativeReturn();
        fundCumulativeReturn6M.setPeriod("6M");
        if (null != api) {
            fundCumulativeReturn6M
                .setTtrBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn6Mth()));
            fundCumulativeReturn6M.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn6Mth()));
            fundCumulativeReturn6M.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn6Mth()));
            fundCumulativeReturn6M.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn6Mth()));
        }

        FundCumulativeReturn fundCumulativeReturn1Y = new FundCumulativeReturn();
        fundCumulativeReturn1Y.setPeriod("1Y");
        if (null != api) {
            fundCumulativeReturn1Y
                .setTtrBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn1Yr()));
            fundCumulativeReturn1Y.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn1Yr()));
            fundCumulativeReturn1Y.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn1Yr()));
            fundCumulativeReturn1Y.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn1Yr()));
        }

        FundCumulativeReturn fundCumulativeReturn3Y = new FundCumulativeReturn();
        fundCumulativeReturn3Y.setPeriod("3Y");
        if (null != api) {
            fundCumulativeReturn3Y
                .setTtrBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn3Yr()));
            fundCumulativeReturn3Y.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn3Yr()));
            fundCumulativeReturn3Y.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn3Yr()));
            fundCumulativeReturn3Y.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn3Yr()));
        }

        FundCumulativeReturn fundCumulativeReturn5Y = new FundCumulativeReturn();
        fundCumulativeReturn5Y.setPeriod("5Y");
        if (null != api) {
            fundCumulativeReturn5Y
                .setTtrBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn5Yr()));
            fundCumulativeReturn5Y.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn5Yr()));
            fundCumulativeReturn5Y.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn3Yr()));
            fundCumulativeReturn5Y.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn5Yr()));
        }

        FundCumulativeReturn fundCumulativeReturn10Y = new FundCumulativeReturn();
        fundCumulativeReturn10Y.setPeriod("10Y");
        if (null != api) {
            fundCumulativeReturn10Y
                .setTtrBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn10Yr()));
            fundCumulativeReturn10Y.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn10Yr()));
            fundCumulativeReturn10Y.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn10Yr()));
            fundCumulativeReturn10Y
                .setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn10Yr()));
        }

        // ProspectusPrimaryIndexReturn
        if (null != api) {
            FBPrimaryProspectusBenchmarks fbPrimaryProspectusBenchmarks = api.getFbPrimaryProspectusBenchmarks();
            if (fbPrimaryProspectusBenchmarks != null) {
                List<PrimaryProspectusBenchmark> primaryProspectusBenchmarks =
                    fbPrimaryProspectusBenchmarks.getPrimaryProspectusBenchmark();
                if (ListUtil.isValid(primaryProspectusBenchmarks)) {
                    List<Prospectus> prospectusYTD = new ArrayList<Prospectus>();
                    List<Prospectus> prospectus1Mth = new ArrayList<Prospectus>();
                    List<Prospectus> prospectus3Mth = new ArrayList<Prospectus>();
                    List<Prospectus> prospectus6Mth = new ArrayList<Prospectus>();
                    List<Prospectus> prospectus1Yr = new ArrayList<Prospectus>();
                    List<Prospectus> prospectus3Yr = new ArrayList<Prospectus>();
                    List<Prospectus> prospectus5Yr = new ArrayList<Prospectus>();
                    List<Prospectus> prospectus10Yr = new ArrayList<Prospectus>();

                    if (ListUtil.isValid(prospectusPrimaryIndexs)) {
                        for (PrimaryProspectusBenchmark benchmark : primaryProspectusBenchmarks) {
                            for (ProspectusPrimaryIndex prospectus : prospectusPrimaryIndexs) {
                                if (null != prospectus && null != benchmark && null != prospectus.getId()
                                    && prospectus.getId().equals(benchmark.getIndexId())) {
                                    prospectusYTD.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                        BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                        BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexReturnYTD())));
                                    prospectus1Mth.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                        BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                        BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexReturn1Mth())));
                                    prospectus3Mth.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                        BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                        BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexReturn3Mth())));
                                    prospectus6Mth.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                        BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                        BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexReturn6Mth())));
                                    prospectus1Yr.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                        BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                        BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexReturn1Yr())));
                                    prospectus3Yr.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                        BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                        BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexReturn3Yr())));
                                    prospectus5Yr.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                        BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                        BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexReturn5Yr())));
                                    prospectus10Yr.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                        BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                        BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexReturn10Yr())));
                                    break;
                                }
                            }
                        }
                    }
                    fundCumulativeReturnYTD.setProspectusPrimaryIndexReturns(prospectusYTD);
                    fundCumulativeReturn1M.setProspectusPrimaryIndexReturns(prospectus1Mth);
                    fundCumulativeReturn3M.setProspectusPrimaryIndexReturns(prospectus3Mth);
                    fundCumulativeReturn6M.setProspectusPrimaryIndexReturns(prospectus6Mth);
                    fundCumulativeReturn1Y.setProspectusPrimaryIndexReturns(prospectus1Yr);
                    fundCumulativeReturn3Y.setProspectusPrimaryIndexReturns(prospectus3Yr);
                    fundCumulativeReturn5Y.setProspectusPrimaryIndexReturns(prospectus5Yr);
                    fundCumulativeReturn10Y.setProspectusPrimaryIndexReturns(prospectus10Yr);
                }
            }
        }
        cumulativeItem.add(fundCumulativeReturnYTD);
        cumulativeItem.add(fundCumulativeReturn1M);
        cumulativeItem.add(fundCumulativeReturn3M);
        cumulativeItem.add(fundCumulativeReturn6M);
        cumulativeItem.add(fundCumulativeReturn1Y);
        cumulativeItem.add(fundCumulativeReturn3Y);
        cumulativeItem.add(fundCumulativeReturn5Y);
        cumulativeItem.add(fundCumulativeReturn10Y);
        return cumulativeItem;
    }

    private List<FundCalendarYearReturn> setCalendarYearTotalReturns(final Api api,
        final List<ProspectusPrimaryIndex> prospectusPrimaryIndexs) throws Exception {
        List<FundCalendarYearReturn> calendarItem = new ArrayList<FundCalendarYearReturn>();

        Integer stubYear = null;
        String stubYearEndReturnEndDate = api.getSrStubYearEndReturnEndDate();
        String stubYearEndReturn = api.getSrStubYearEndReturn();
        if (StringUtil.isValid(stubYearEndReturnEndDate)) {
            Date date = this.formatter.parse(stubYearEndReturnEndDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            stubYear = calendar.get(Calendar.YEAR);
        }

        Calendar cal = Calendar.getInstance();

        FundCalendarYearReturn fundCalendarYearReturn1 = new FundCalendarYearReturn();
        cal.add(Calendar.YEAR, -1);
        fundCalendarYearReturn1
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn1
            .setCyrBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrBestFitIndexYear1()));
        fundCalendarYearReturn1.setCategoryCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrCategoryYear1()));
        fundCalendarYearReturn1.setPrimaryIndexYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrPrimaryIndexYear1()));
        this.setStubYearEndReturn(fundCalendarYearReturn1, stubYear, stubYearEndReturn, cal);

        FundCalendarYearReturn fundCalendarYearReturn2 = new FundCalendarYearReturn();
        cal.add(Calendar.YEAR, -1);
        fundCalendarYearReturn2
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn2
            .setCyrBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrBestFitIndexYear2()));
        fundCalendarYearReturn2.setCategoryCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrCategoryYear2()));
        fundCalendarYearReturn2.setPrimaryIndexYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrPrimaryIndexYear2()));
        this.setStubYearEndReturn(fundCalendarYearReturn2, stubYear, stubYearEndReturn, cal);

        FundCalendarYearReturn fundCalendarYearReturn3 = new FundCalendarYearReturn();
        cal.add(Calendar.YEAR, -1);
        fundCalendarYearReturn3
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn3
            .setCyrBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrBestFitIndexYear3()));
        fundCalendarYearReturn3.setCategoryCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrCategoryYear3()));
        fundCalendarYearReturn3.setPrimaryIndexYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrPrimaryIndexYear3()));
        this.setStubYearEndReturn(fundCalendarYearReturn3, stubYear, stubYearEndReturn, cal);

        FundCalendarYearReturn fundCalendarYearReturn4 = new FundCalendarYearReturn();
        cal.add(Calendar.YEAR, -1);
        fundCalendarYearReturn4
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn4
            .setCyrBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrBestFitIndexYear4()));
        fundCalendarYearReturn4.setCategoryCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrCategoryYear4()));
        fundCalendarYearReturn4.setPrimaryIndexYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrPrimaryIndexYear4()));
        this.setStubYearEndReturn(fundCalendarYearReturn4, stubYear, stubYearEndReturn, cal);

        FundCalendarYearReturn fundCalendarYearReturn5 = new FundCalendarYearReturn();
        cal.add(Calendar.YEAR, -1);
        fundCalendarYearReturn5
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn5
            .setCyrBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrBestFitIndexYear5()));
        fundCalendarYearReturn5.setCategoryCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrCategoryYear5()));
        fundCalendarYearReturn5.setPrimaryIndexYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCyrPrimaryIndexYear5()));
        this.setStubYearEndReturn(fundCalendarYearReturn5, stubYear, stubYearEndReturn, cal);

        // ProspectusPrimaryIndexYearReturn
        FBPrimaryProspectusBenchmarks fbPrimaryProspectusBenchmarks = api.getFbPrimaryProspectusBenchmarks();
        if (fbPrimaryProspectusBenchmarks != null) {
            List<PrimaryProspectusBenchmark> primaryProspectusBenchmarks =
                fbPrimaryProspectusBenchmarks.getPrimaryProspectusBenchmark();
            if (ListUtil.isValid(primaryProspectusBenchmarks)) {
                List<Prospectus> prospectus1Yr = new ArrayList<Prospectus>();
                List<Prospectus> prospectus2Yr = new ArrayList<Prospectus>();
                List<Prospectus> prospectus3Yr = new ArrayList<Prospectus>();
                List<Prospectus> prospectus4Yr = new ArrayList<Prospectus>();
                List<Prospectus> prospectus5Yr = new ArrayList<Prospectus>();

                if (ListUtil.isValid(prospectusPrimaryIndexs)) {
                    for (PrimaryProspectusBenchmark benchmark : primaryProspectusBenchmarks) {
                        for (ProspectusPrimaryIndex prospectus : prospectusPrimaryIndexs) {
                            if (null != prospectus && null != benchmark && null != prospectus.getId()
                                && prospectus.getId().equals(benchmark.getIndexId())) {
                                prospectus1Yr.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                    BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                    BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexYear1())));
                                prospectus2Yr.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                    BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                    BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexYear2())));
                                prospectus3Yr.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                    BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                    BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexYear3())));
                                prospectus4Yr.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                    BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                    BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexYear4())));
                                prospectus5Yr.add(new Prospectus(benchmark.getIndexId(), benchmark.getIndexName(),
                                    BigDecimalUtil.fromStringAndCheckNull(benchmark.getWeighting()),
                                    BigDecimalUtil.fromStringAndCheckNull(prospectus.getProspectusPrimaryIndexYear5())));
                                break;
                            }
                        }
                    }
                }
                fundCalendarYearReturn1.setProspectusPrimaryIndexYearReturns(prospectus1Yr);
                fundCalendarYearReturn2.setProspectusPrimaryIndexYearReturns(prospectus2Yr);
                fundCalendarYearReturn3.setProspectusPrimaryIndexYearReturns(prospectus3Yr);
                fundCalendarYearReturn4.setProspectusPrimaryIndexYearReturns(prospectus4Yr);
                fundCalendarYearReturn5.setProspectusPrimaryIndexYearReturns(prospectus5Yr);
            }
        }

        calendarItem.add(fundCalendarYearReturn1);
        calendarItem.add(fundCalendarYearReturn2);
        calendarItem.add(fundCalendarYearReturn3);
        calendarItem.add(fundCalendarYearReturn4);
        calendarItem.add(fundCalendarYearReturn5);
        return calendarItem;
    }

    private void setStubYearEndReturn(final FundCalendarYearReturn fundCalendarYearReturn, final Integer stubYear,
        final String stubYearEndReturn, final Calendar cal) {
        if (null != stubYear && StringUtil.isValid(stubYearEndReturn)) {
            int year = cal.get(Calendar.YEAR);
            if (stubYear.intValue() == year) {
                fundCalendarYearReturn.setStubYearEndReturnIndicator('Y');
                fundCalendarYearReturn.setFundStubYearEndReturn(BigDecimalUtil.fromStringAndCheckNull(stubYearEndReturn));
            }
        }
    }

    private MorningstarRatings getMorningstarRatings(final Api api, final MorningstarRatings morningstarRatings) throws Exception {
        if (null != api) {
            morningstarRatings.setMorningstarRatingOverall(api.getMrRatingOverall());
            morningstarRatings.setMorningstarRating3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getMrRating3Year()));
            morningstarRatings.setMorningstarRating5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getMrRating5Year()));
            morningstarRatings.setMorningstarRating10Yr(BigDecimalUtil.fromStringAndCheckNull(api.getMrRating10Year()));
            morningstarRatings.setMorningstarTaxAdjustedRatingOverall(api.getTarRatingOverall());
            morningstarRatings.setMorningstarTaxAdjustedRating3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTarRating3Yr()));
            morningstarRatings.setMorningstarTaxAdjustedRating5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTarRating5Yr()));
            morningstarRatings.setMorningstarTaxAdjustedRating10Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTarRating10Yr()));
            morningstarRatings.setLastUpdatedDate(api.getMrRatingDate());
        }
        return morningstarRatings;
    }

    private MgmtAndContactInfo getMgmtAndContactInfo(final Api api, final MgmtAndContactInfo mgmtAndContactInfo) throws Exception {
        List<ManagemenInfo> mgmtInfo = new ArrayList<ManagemenInfo>();
        if (null != api) {
            FM2Managers fmManagers = api.getFM2Managers();
            if (null != fmManagers) {
                List<Manager> managerList = fmManagers.getManager();
                if (ListUtil.isValid(managerList)) {
                    for (Manager mgr : managerList) {
                        ManagemenInfo managemenInfo = new ManagemenInfo();
                        managemenInfo.setManagerName(mgr.getName());
                        // - M* will return the date as format
                        managemenInfo.setStartDate(mgr.getStartDate());
                        mgmtInfo.add(managemenInfo);
                    }
                }
            }
        }
        mgmtAndContactInfo.setMgmtInfos(mgmtInfo);
        return mgmtAndContactInfo;
    }

    private InvestmentStrategy getInvestmentStrategy(final Api api, final InvestmentStrategy investmentStrategy) throws Exception {
        if (null != api) {
            investmentStrategy.setAssetAllocBondNet(BigDecimalUtil.fromStringAndCheckNull(api.getAabrpAssetAllocBondNet()));
            investmentStrategy.setPreferredStockNet(BigDecimalUtil.fromStringAndCheckNull(api.getAabrpPreferredStockNet()));
            investmentStrategy.setAssetAllocCashNet(BigDecimalUtil.fromStringAndCheckNull(api.getAabrpAssetAllocCashNet()));
            investmentStrategy.setOtherNet(BigDecimalUtil.fromStringAndCheckNull(api.getAabrpOtherNet()));
            investmentStrategy.setAssetAllocEquityNet(BigDecimalUtil.fromStringAndCheckNull(api.getAabrpAssetAllocEquityNet()));
        }
        return investmentStrategy;
    }

    private YieldAndCredit getYieldAndCredit(final Api api, final YieldAndCredit yieldAndCredit) throws Exception {
        if (null != api) {
            yieldAndCredit.setCreditQualA(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualA()));
            yieldAndCredit.setCreditQualAA(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualAA()));
            yieldAndCredit.setCreditQualAAA(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualAAA()));
            yieldAndCredit.setCreditQualB(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualB()));
            yieldAndCredit.setCreditQualBB(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualBB()));
            yieldAndCredit.setCreditQualBBB(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualBBB()));
            yieldAndCredit.setCreditQualBelowB(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualBelowB()));
            yieldAndCredit.setCreditQualNotRated(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualNotRated()));
            yieldAndCredit.setCreditQualDate(api.getCqbrpCreditQualDate());
        }
        return yieldAndCredit;
    }


    private String getInvestmentStrategyBySiteLocale(final Api api, final String site, final String locale) {
        // set default value as response
        String result = api.getIcInvestmentStrategy();
        LogUtil.debug(QuoteSummaryServiceExecutor.class,
            "QuoteSummaryServiceExecutor.getInvestmentStrategyByLocal >>  Locale is " + locale + " ,site is " + site);

        if (this.multiLanguageForInvestmentStrategyMap != null) {
            // check if have special setting for locale
            Map<String, String> mappingVal = this.multiLanguageForInvestmentStrategyMap.keySet().contains(site)
                ? this.multiLanguageForInvestmentStrategyMap.get(site) : this.multiLanguageForInvestmentStrategyMap.get("DEFAULT");
            if (mappingVal != null && mappingVal.size() > 0) {
                String valueStr = mappingVal.get(locale);
                if (StringUtil.isValid(valueStr)) {
                    String[] values = valueStr.split(CommonConstants.SYMBOL_SEPARATOR);
                    if (values != null && values.length > 0) {
                        for (String val : values) {
                            String invest = this.getInvestmentStrategyByLocale(api, val, locale);
                            if (StringUtil.isValid(invest)) {
                                result = invest;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private String getInvestmentStrategyByLocale(final Api api, final String val, final String locale) {
        String result = null;
        // mapping pattern is [NarrativeLanguageId,NarrativeLanguageName]
        String[] mappings = val.split(",");
        ICMultilingualInvestmentStrategies multiInvestStrategies = api.getIcMultilingualInvestmentStrategies();
        if (multiInvestStrategies != null) {
            List<MultilingualStrategy> investStrategyList = multiInvestStrategies.getMultilingualStrategy();
            for (MultilingualStrategy investStrategy : investStrategyList) {
                if (mappings[0].equals(investStrategy.getNarrativeLanguageId())
                    && mappings[1].equals(investStrategy.getNarrativeLanguageName())) {
                    result = investStrategy.getInvestmentStrategy();
                    break;
                }
            }
        }
        return result;
    }
}
