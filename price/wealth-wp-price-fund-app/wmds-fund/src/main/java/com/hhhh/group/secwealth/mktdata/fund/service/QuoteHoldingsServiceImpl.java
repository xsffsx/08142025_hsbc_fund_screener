package com.hhhh.group.secwealth.mktdata.fund.service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quoteholdings.Api;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quoteholdings.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quoteholdings.QuoteHoldingsData;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.QuoteHoldingsRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.QuoteHoldingsResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteholdings.Holdings;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteholdings.Holdings.Fundamentals;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteholdings.Holdings.Fundamentals.AllHoldings;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteholdings.Holdings.Fundamentals.FixedIncomeHoldings;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteholdings.Holdings.Fundamentals.StockHoldings;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;

@Service("quoteHoldingsService")
public class QuoteHoldingsServiceImpl extends AbstractMstarService {

    @Value("#{systemConfig['mstar.conn.url.quoteholdings']}")
    private String url;

    @Value("#{systemConfig['quoteholdings.dataClass']}")
    private String dataClass;

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    private JAXBContext dataClassJC;

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

        // Prepare Request
        QuoteHoldingsRequest request = (QuoteHoldingsRequest) object;

        // Invoke Service
        QuoteHoldingsData holdingsData = (QuoteHoldingsData) sendRequest(request);

        // Set Response
        QuoteHoldingsResponse response = new QuoteHoldingsResponse();
        Holdings holdings = new Holdings();
        response.setHoldings(holdings);
        Fundamentals fundamentals = holdings.new Fundamentals();
        holdings.setFundamentals(fundamentals);
        if (null != holdingsData) {
            Data data = holdingsData.getData();
            if (null != data) {
                Api api = data.getApi();
                if (null != api) {
                    AllHoldings allHoldings = fundamentals.new AllHoldings();
                    StockHoldings stockHoldings = fundamentals.new StockHoldings();
                    FixedIncomeHoldings fixedIncomeHoldings = fundamentals.new FixedIncomeHoldings();

                    allHoldings.setAssetsUnderManagement(BigDecimalUtil.fromStringAndCheckNull(api.getFNASurveyedFundNetAssets()));
                    allHoldings.setAssetsUnderManagementCurrencyCode("USD");
                    allHoldings.setAssetsUnderManagementCategoryAvg(BigDecimalUtil.fromStringAndCheckNull(api
                        .getFnaCategoryNormalizedFundNetAssets()));

                    allHoldings.setAssetsUnderManagementCategoryAvgCurrencyCode("USD");
                    allHoldings.setAnnualPortfolioTurnover(BigDecimalUtil.fromStringAndCheckNull(api.getPSTurnoverRatio()));
                    allHoldings.setAnnualPortfolioTurnoverCategoryAvg(BigDecimalUtil.fromStringAndCheckNull(api
                        .getPSCategoryTurnoverRatio()));
                    stockHoldings.setPriceEarnings(BigDecimalUtil.fromStringAndCheckNull(api.getPSPriceToEarnings()));
                    stockHoldings.setPriceEarningsCategoryAvg(BigDecimalUtil.fromStringAndCheckNull(api
                        .getPSCategoryPriceToEarnings()));
                    stockHoldings.setPriceBook(BigDecimalUtil.fromStringAndCheckNull(api.getPSPriceToBook()));
                    stockHoldings.setPriceBookCategoryAvg(BigDecimalUtil.fromStringAndCheckNull(api.getPSCategoryPriceToBook()));
                    stockHoldings.setReturnOnAssets(BigDecimalUtil.fromStringAndCheckNull(api.getPSROA()));
                    stockHoldings.setReturnOnAssetsCategoryAvg(BigDecimalUtil.fromStringAndCheckNull(api.getPSCategoryROA()));
                    stockHoldings.setReturnOnEquity(BigDecimalUtil.fromStringAndCheckNull(api.getPSROE()));
                    stockHoldings.setReturnOnEquityCategoryAvg(BigDecimalUtil.fromStringAndCheckNull(api.getPSCategoryROE()));
                    stockHoldings.setDividendYield(BigDecimalUtil.fromStringAndCheckNull(api.getYLDYield1Yr()));
                    stockHoldings.setDividendYieldCategoryAvg(null);
                    // Mapping
                    String creditQuality = api.getPSAverageCreditQuality();
                    fixedIncomeHoldings.setCreditRating(creditRatingMapping(creditQuality));
                    // Mapping
                    String cateCreditQuality = api.getPSCategoryAverageCreditQuality();
                    fixedIncomeHoldings.setCreditRatingCategoryAvg(creditRatingMapping(cateCreditQuality));
                    fixedIncomeHoldings.setEffectiveMaturity(BigDecimalUtil.fromStringAndCheckNull(api.getPSEffectiveMaturity()));
                    fixedIncomeHoldings.setEffectiveMaturityCategoryAvg(null);
                    fixedIncomeHoldings.setYieldToMaturity(BigDecimalUtil.fromStringAndCheckNull(api.getPSYieldToMaturity()));
                    fixedIncomeHoldings.setYieldToMaturityCategoryAvg(api.getPSCategoryYieldToMaturity());

                    fundamentals.setLastUpdatedDate(api.getPSRPPortfolioDate());

                    fundamentals.setAllHoldings(allHoldings);
                    fundamentals.setStockHoldings(stockHoldings);
                    fundamentals.setFixedIncomeHoldings(fixedIncomeHoldings);
                }
            }
        }
        response.setHoldings(holdings);
        return response;
    }

    public String creditRatingMapping(final String quality) {
        String rating = null;
        if (null != quality) {
            int qualityNum = Integer.parseInt(quality);
            if (qualityNum >= 1 && qualityNum <= 2) {
                rating = MstarConstants.RATING_AAA;
            }
            if (qualityNum >= 3 && qualityNum <= 5) {
                rating = MstarConstants.RATING_AA;
            }
            if (qualityNum >= 6 && qualityNum <= 8) {
                rating = MstarConstants.RATING_A;
            }
            if (qualityNum >= 9 && qualityNum <= 11) {
                rating = MstarConstants.RATING_BBB;
            }
            if (qualityNum >= 12 && qualityNum <= 14) {
                rating = MstarConstants.RATING_BB;
            }
            if (qualityNum >= 15 && qualityNum <= 17) {
                rating = MstarConstants.RATING_B;
            }
            if (qualityNum >= 18) {
                rating = MstarConstants.RATING_BELOW_B;
            }
        }
        return rating;
    }

    protected Object sendRequest(final QuoteHoldingsRequest request) throws Exception {
        String altClassCde = request.getProdCdeAltClassCde();
        String countryCode = request.getCountryCode();
        String groupMember = request.getGroupMember();
        String prodAltNum = request.getProdAltNum();
        String countryTradableCode = request.getMarket();
        String productType = request.getProductType();
        String locale = request.getLocale();

        SearchProduct searchProduct = this.internalProductKeyUtil.getProductBySearchWithAltClassCde(altClassCde, countryCode,
            groupMember, prodAltNum, countryTradableCode, productType, locale);
        if (null == searchProduct || StringUtil.isInvalid(searchProduct.getExternalKey())) {
            String msg = "No record found for Mstar|ProdAltNum=" + prodAltNum;
            LogUtil.error(AbstractMstarService.class, msg);
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND, msg);
        }
        String id = searchProduct.getExternalKey();
        return super.sendRequest(MstarConstants.MSTARID, id, this.url, this.dataClassJC.createUnmarshaller());
    }
}
