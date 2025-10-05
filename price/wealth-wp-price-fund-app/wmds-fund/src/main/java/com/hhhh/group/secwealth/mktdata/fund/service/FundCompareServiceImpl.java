
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.service.AbstractService;
import com.hhhh.group.secwealth.mktdata.common.service.ServiceExecutor;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundCompareRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundCompareResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareProduct;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareProduct.Header;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareProduct.Performance;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareProduct.Performance.AnnualizedReturns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareProduct.Performance.CalendarReturns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareProduct.Profile;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareProduct.PurchaseInfo;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareProduct.Rating;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareRisk;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareRisk.YearRisk;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundCompareDao;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;


@Service("fundCompareService")
public class FundCompareServiceImpl extends AbstractService {

    @Autowired
    @Qualifier("fundCompareServiceExecutor")
    private ServiceExecutor fundCompareServiceExecutor;

    @Autowired
    @Qualifier("localeMappingUtil")
    protected LocaleMappingUtil localeMappingUtil;

    @Autowired
    @Qualifier("fundCompareDao")
    private FundCompareDao fundCompareDao;

    @Resource(name = "tradableCurrencyProdUsingMap")
    private List<String> tradableCurrencyProdUsingMap;

    @Override
    public Object execute(final Object object) throws Exception {
        FundCompareRequest request = (FundCompareRequest) object;
        FundCompareResponse fundCompareResponse = (FundCompareResponse) this.fundCompareServiceExecutor.execute(request);
        List<UtProdInstm> utProdInstmList = this.fundCompareDao.searchForCompare(request);
        int index =
            this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + request.getLocale());

        List<ProductKey> productKeys = request.getProductKeys();
        Map<String, String> currencyProdUsingKeyMap = new HashMap<String, String>();
        for (ProductKey productKey : productKeys) {
            String currencyProdUsingKey =
                StringUtil.combineWithUnderline(request.getCountryCode(), request.getGroupMember(), productKey.getProductType());
            currencyProdUsingKeyMap.put(productKey.getProdAltNum(), currencyProdUsingKey);
        }
        migrateResponseFromeDB(fundCompareResponse, utProdInstmList, index, currencyProdUsingKeyMap, productKeys,
            request.getSiteKey());
        return fundCompareResponse;
    }


    private void migrateResponseFromeDB(final FundCompareResponse fundCompareResponse, final List<UtProdInstm> utProdInstmList,
        final Integer index, final Map<String, String> currencyProdUsingKeyMap, final List<ProductKey> ProductKeys,
        final String site) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        if (null != fundCompareResponse && ListUtil.isValid(utProdInstmList)) {
            List<FundCompareProduct> products = fundCompareResponse.getProducts();
            List<SearchProduct> list = (List<SearchProduct>) fundCompareResponse.getSearchProductList();
            Set<String> mstarPerformanceId = new HashSet<String>();
            Set<String> dbPerformanceId = new HashSet<String>();
            List<FundCompareProduct> notReturnProducts = new ArrayList<FundCompareProduct>();
            if (ListUtil.isValid(products)) {
                for (FundCompareProduct fundCompareProduct : products) {
                    mstarPerformanceId.add(fundCompareProduct.getProdAltNumXCode());
                    Header header = fundCompareProduct.getHeader();
                    Profile profile = fundCompareProduct.getProfile();
                    Rating rating = fundCompareProduct.getRating();
                    List<FundCompareRisk> risk = fundCompareProduct.getRisk();
                    Performance performance = fundCompareProduct.getPerformance();
                    AnnualizedReturns annualizedReturns = performance.getAnnualizedReturns();
                    CalendarReturns calendarReturns = performance.getCalendarReturns();
                    PurchaseInfo purchaseInfo = fundCompareProduct.getPurchaseInfo();
                    FundCompareProduct.Summary summary = fundCompareProduct.getSummary();
                    for (SearchProduct searchProduct : list) {
                        // For SearchProduct -> O:externalKey; For SearchObject
                        // -> M:symbol, T:key
                        SearchableObject searchObject = searchProduct.getSearchObject();
                        if (null != searchObject) {
                            String prodAltNumMCode = searchObject.getSymbol();
                            if (fundCompareProduct.getProdAltNumXCode().equals(searchProduct.getExternalKey())
                                && fundCompareProduct.getSymbol().equals(prodAltNumMCode)) {
                                for (UtProdInstm utProdInstm : utProdInstmList) {
                                    if (null != utProdInstm) {
                                        dbPerformanceId.add(utProdInstm.getPerformanceId());
                                        if (prodAltNumMCode.equals(utProdInstm.getSymbol())) {
                                            header.setProdAltNum(utProdInstm.getSymbol());
                                            header.setCurrency(utProdInstm.getClosingPrcCcy());
                                            String currencyProdUsingKey = currencyProdUsingKeyMap.get(prodAltNumMCode);
                                            if (null != profile) {
                                                profile.setInceptionDate(DateUtil.getSimpleDateFormat(
                                                    utProdInstm.getInceptionDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
                                                profile.setTurnoverRatio(utProdInstm.getTurnoverRatio());
                                                profile.setStdDev3Yr(utProdInstm.getStdDev3Yr());
                                                profile.setEquityStylebox(
                                                    StringUtil.toStringAndCheckNull(utProdInstm.getEquityStyle()));
                                                if (0 == index) {
                                                    profile.sethhhhCategoryName(utProdInstm.getCategoryName1());
                                                } else if (1 == index) {
                                                    profile.sethhhhCategoryName(utProdInstm.getCategoryName2());
                                                } else if (2 == index) {
                                                    profile.sethhhhCategoryName(utProdInstm.getCategoryName3());
                                                } else {
                                                    profile.sethhhhCategoryName(utProdInstm.getCategoryName1());
                                                }
                                                profile.sethhhhCategoryCode(utProdInstm.getCategoryCode());
                                                profile.setDistributionYield(utProdInstm.getYield1Yr());
                                                profile.setDistributionFrequency(utProdInstm.getDistributionFrequency());
                                                profile.setPiFundInd(utProdInstm.getPiFundInd());
                                                profile.setDeAuthFundInd(utProdInstm.getDeAuthFundInd());
                                                profile.setExpenseRatio(utProdInstm.getExpenseRatio());
                                                profile.setInitialCharge(utProdInstm.getChrgInitSalesPct());
                                                profile.setAnnualManagementFee(utProdInstm.getAnnMgmtFeePct());
                                                profile.setEsgInd(utProdInstm.getEsgInd());
                                                profile.setGbaAcctTrdb(utProdInstm.getGbaAcctTrdb());

                                            }

                                            if (null != annualizedReturns) {
                                                annualizedReturns.setReturn1Mth(utProdInstm.getReturn1mth());
                                                annualizedReturns.setReturn3Mth(utProdInstm.getReturn3mth());
                                                annualizedReturns.setReturn6Mth(utProdInstm.getReturn6mth());
                                                annualizedReturns.setReturn1Yr(utProdInstm.getReturn1yr());
                                                annualizedReturns.setReturn3Yr(utProdInstm.getReturn3yr());
                                                annualizedReturns.setReturn5Yr(utProdInstm.getReturn5yr());
                                                annualizedReturns.setReturn10Yr(utProdInstm.getReturn10yr());
                                                annualizedReturns.setReturnSinceInception(utProdInstm.getReturnSinceInception());
                                                performance.setAnnualizedReturns(annualizedReturns);
                                            }

                                            if (null != calendarReturns) {
                                                calendarReturns.setReturnYTD(utProdInstm.getReturnYTD());
                                                calendarReturns.setYear1(utProdInstm.getYear1());
                                                calendarReturns.setYear2(utProdInstm.getYear2());
                                                calendarReturns.setYear3(utProdInstm.getYear3());
                                                calendarReturns.setYear4(utProdInstm.getYear4());
                                                calendarReturns.setYear5(utProdInstm.getYear5());
                                                performance.setCalendarReturns(calendarReturns);
                                            }

                                            if (null != rating) {
                                                Integer ratingOverall = utProdInstm.getRatingOverall();
                                                if (null != ratingOverall) {
                                                    rating.setMorningstarRating(ratingOverall.toString());
                                                }
                                                if (MstarConstants.PRODUCTTYPE_UT.equals(ProductKeys.get(0).getProductType())) {
                                                    rating.setTaxAdjustedRating(
                                                        StringUtil.toStringAndCheckNull(utProdInstm.getTaxAdjustedRating()));
                                                }
                                                rating.setAverageCreditQualityName(utProdInstm.getAverageCreditQualityName());
                                                rating.setAverageCreditQuality(
                                                    creditRatingMapping(utProdInstm.getAverageCreditQuality()));
                                                rating.setAverageCreditQualityDate(
                                                    DateUtil.getSimpleDateFormat(utProdInstm.getHoldingAllocationPortfolioDate(),
                                                        DateConstants.DateFormat_yyyyMMdd_withHyphen));
                                                rating.setRank1Yr(utProdInstm.getRank1Yr());
                                                rating.setRank3Yr(utProdInstm.getRank3Yr());
                                                rating.setRank5Yr(utProdInstm.getRank5Yr());
                                                rating.setRank10Yr(utProdInstm.getRank10Yr());
                                            }

                                            if (ListUtil.isValid(risk)) {
                                                // Year1 Risk
                                                FundCompareRisk fundCompareYear1Risk = risk.get(0);
                                                YearRisk year1Risk = fundCompareYear1Risk.getYearRisk();
                                                year1Risk.setYear(MstarConstants.ONE);
                                                year1Risk.setBeta(utProdInstm.getBeta1());
                                                year1Risk.setStdDev(utProdInstm.getStdDev1());
                                                year1Risk.setAlpha(utProdInstm.getAlpha1());
                                                year1Risk.setSharpeRatio(utProdInstm.getSharpeRatio1());
                                                year1Risk.setrSquared(utProdInstm.getrSquared1());
                                                fundCompareYear1Risk.setYearRisk(year1Risk);

                                                // Year3 Risk
                                                FundCompareRisk fundCompareYear3Risk = risk.get(1);
                                                YearRisk year3Risk = fundCompareYear3Risk.getYearRisk();
                                                year3Risk.setYear(MstarConstants.THREE);
                                                year3Risk.setBeta(utProdInstm.getBeta3());
                                                year3Risk.setStdDev(utProdInstm.getStdDev3Yr());
                                                year3Risk.setAlpha(utProdInstm.getAlpha3());
                                                year3Risk.setSharpeRatio(utProdInstm.getSharpeRatio3());
                                                year3Risk.setrSquared(utProdInstm.getrSquared3());
                                                fundCompareYear3Risk.setYearRisk(year3Risk);

                                                // Year5 Risk
                                                FundCompareRisk fundCompareYear5Risk = risk.get(2);
                                                YearRisk year5Risk = fundCompareYear5Risk.getYearRisk();
                                                year5Risk.setYear(MstarConstants.FIVE);
                                                year5Risk.setBeta(utProdInstm.getBeta5());
                                                year5Risk.setStdDev(utProdInstm.getStdDev5());
                                                year5Risk.setAlpha(utProdInstm.getAlpha5());
                                                year5Risk.setSharpeRatio(utProdInstm.getSharpeRatio5());
                                                year5Risk.setrSquared(utProdInstm.getrSquared5());
                                                fundCompareYear5Risk.setYearRisk(year5Risk);

                                                // Year10 Risk
                                                FundCompareRisk fundCompareYear10Risk = risk.get(3);
                                                YearRisk year10Risk = fundCompareYear10Risk.getYearRisk();
                                                year10Risk.setYear(MstarConstants.TEN);
                                                year10Risk.setBeta(utProdInstm.getBeta10());
                                                year10Risk.setStdDev(utProdInstm.getStdDev10());
                                                year10Risk.setAlpha(utProdInstm.getAlpha10());
                                                year10Risk.setSharpeRatio(utProdInstm.getSharpeRatio10());
                                                year10Risk.setrSquared(utProdInstm.getrSquared10());
                                                fundCompareYear10Risk.setYearRisk(year10Risk);
                                            }

                                            if (null != purchaseInfo) {
                                                purchaseInfo.setMinimumInitial(utProdInstm.getMinInitInvst());
                                                // PurchaseCurrencyId -
                                                // Fund Purchase Details;
                                                purchaseInfo.setMinimumInitialCurrencyCode(utProdInstm.getPurchaseCcy());
                                                // PurchaseCurrencyId -
                                                // Fund Purchase Details;
                                                purchaseInfo.setMinimumSubsequent(utProdInstm.getMinSubqInvst());
                                                purchaseInfo.setMinimumSubsequentCurrencyCode(utProdInstm.getPurchaseCcy());

                                                purchaseInfo.setMinInitInvst(utProdInstm.getMinInitInvst());
                                                // PurchaseCurrencyId -
                                                // Fund Purchase Details;
                                                purchaseInfo.setMinInitInvstCurrencyCode(utProdInstm.getPurchaseCcy());
                                                purchaseInfo.setMinSubqInvst(utProdInstm.getMinSubqInvst());
                                                // PurchaseCurrencyId -
                                                // Fund Purchase Details;
                                                purchaseInfo.setMinSubqInvstCurrencyCode(utProdInstm.getPurchaseCcy());

                                                purchaseInfo.sethhhhMinInitInvst(utProdInstm.gethhhhMinInitInvst());
                                                purchaseInfo.sethhhhMinInitInvstCurrencyCode(
                                                    this.getTradableCurrencyCode(utProdInstm, currencyProdUsingKey));
                                                purchaseInfo.sethhhhMinSubqInvst(utProdInstm.gethhhhMinSubqInvst());
                                                purchaseInfo.sethhhhMinSubqInvstCurrencyCode(
                                                    this.getTradableCurrencyCode(utProdInstm, currencyProdUsingKey));

                                            }
                                            if (null != summary) {
                                                summary.setDayEndNAV(utProdInstm.getDayEndNAV());
                                                if (null != utProdInstm.getAsOfDate()) {
                                                    summary.setExchangeUpdatedTime(DateUtil.getSimpleDateFormat(
                                                        utProdInstm.getAsOfDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
                                                }
                                                // summary.setTotalNetAsset(utProdInstm.getTotalNetAsset());
                                                summary.setTotalNetAssetCurrencyCode(utProdInstm.getCurrency());
                                                Integer ratingOverall = utProdInstm.getRatingOverall();
                                                if (null != ratingOverall) {
                                                    summary.setRatingOverall(ratingOverall.toString());
                                                }
                                                summary.setMer(utProdInstm.getMer());
                                                summary.setYield1Yr(utProdInstm.getYield1Yr());
                                                summary.setActualManagementFee(utProdInstm.getActualManagementFee());
                                                summary.setDayEndNAVCurrencyCode(utProdInstm.getCurrency());
                                                summary.setChangeAmountNAV(utProdInstm.getChangeAmountNAV());
                                                summary.setChangePercentageNAV(utProdInstm.getMarketPrice());
                                                summary.setRiskLvlCde(utProdInstm.getRiskLvlCde());
                                                summary.setAnnualReportOngoingCharge(utProdInstm.getKiidOngoingCharge());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // if DB data not match with Mstar, return product list that
                // exists in Mstar and DB
                for (String mstarPerfId : mstarPerformanceId) {
                    if (!dbPerformanceId.contains(mstarPerfId)) {
                        for (FundCompareProduct fundCompareProduct : products) {
                            if (fundCompareProduct.getProdAltNumXCode().equals(mstarPerfId)) {
                                notReturnProducts.add(fundCompareProduct);
                            }
                        }
                    }
                }
            }
            products.removeAll(notReturnProducts);
        }
    }

    private String creditRatingMapping(final Integer quality) {
        String rating = null;
        if (null != quality) {
            int qualityNum = quality.intValue();
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

    private String getTradableCurrencyCode(final UtProdInstm utProdInstm, final String usingProductKey) {
        if (this.tradableCurrencyProdUsingMap.contains(usingProductKey)) {
            return utProdInstm.getCcyProdTradeCde();
        }
        return utProdInstm.getCcyInvstCde();
    }

}