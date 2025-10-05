
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;

import org.apache.commons.lang.StringUtils;
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
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.MorningStarUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.AMCBAdministratorCompanies;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.AMCBProviderCompanyCountryHeadquarters;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.AMCBSubAdvisorCompanies;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.AdministratorCompany;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.Api;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.ClosedToInvestor;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.CountryHeadquarters;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.FBPrimaryProspectusBenchmarks;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.FM2Managers;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.FSCBIFundServDetails;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.FeeSchedule;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.FundServDetail;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.ICMultilingualInvestmentStrategies;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.Manager;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.MultilingualStrategy;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.PIClosedToInvestors;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.PrimaryProspectusBenchmark;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.ProspectusCustodianFee;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.ProspectusPrimaryIndex;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.QuoteSummaryData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.SubAdvisorCompany;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.newapi.DataSet;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.newapi.DataSet.QuickTake.Fee;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.newapi.DataSet.QuickTake.FundInfo;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.newapi.DataSet.QuickTake.FundManager;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.newapi.DataSet.QuickTake.PortfolioSummary;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundQuoteSummaryRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundQuoteSummaryResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.FundCalendarYearReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.FundCumulativeReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.ManagemenInfo;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Prospectus;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.CalendarYearTotalReturns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.CumulativeTotalReturns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.FeesAndExpenses;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.HoldingDetails;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.InvestmentStrategy;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.MgmtAndContactInfo;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.MorningstarRatings;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.Profile;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.ToNewInvestors;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.YieldAndCredit;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;

@Service("quoteSummaryServiceExecutor")
public class QuoteSummaryServiceExecutor extends AbstractMstarService {

    private String FUND_SRV_CDE_TYPE = "M";
    private final String TEL_PHONE_NO = ".telephoneNo";
    private final String LOCAL_PHONE_NO = "fscbiLocalPhone";
    private final String SITE_FEATURE_MSTAR_MSTARNEWAPI = ".mstar.mstarnewapi";
    private final String SITE_FEATURE_MSTAR_MSTARNEWAPI_SITETYPE = ".mstar.mstarnewapi.sitetype";

    private Map<String, String> FUND_SRV_CODE_MAP = new HashMap<String, String>();

    @Resource(name = "multiLanguageForInvestmentStrategyMap")
    private Map<String, Map<String, String>> multiLanguageForInvestmentStrategyMap;

    @Resource(name = "bestFitIndexCalendarYearMap")
    private Map<String, String> bestFitIndexCalendarYearMap;

    @Resource(name = "fieldNeeddataPointNewapiMap")
    private Map<String, String> fieldNeeddataPointNewapiMap;

    @Value("#{systemConfig['mstar.conn.url.quotesummary']}")
    private String url;

    @Value("#{systemConfig['mstar.conn.url.mstarnewapi']}")
    private String urlApi;

    @Value("#{systemConfig['mstar.conn.url.mstarnewapi.glcm']}")
    private String urlApiGlcm;

    @Value("#{systemConfig['quotesummary.dataClass']}")
    private String dataClass;

    @Value("#{systemConfig['quotesummary.dataSetClass']}")
    private String dataSetClass;

    private JAXBContext dataClassJC;

    private JAXBContext dataSetClassJC;

    private FastDateFormat formatter = FastDateFormat.getInstance(DateConstants.DateFormat_yyyyMMdd_withHyphen);

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    @Autowired
    @Qualifier("siteFeature")
    private SiteFeature siteFeature;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    @PostConstruct
    public void init() throws Exception {
        try {
            this.dataClassJC = JAXBContext.newInstance(Class.forName(this.dataClass));
            this.dataSetClassJC = JAXBContext.newInstance(Class.forName(this.dataSetClass));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Object execute(final Object object) throws Exception {

        FundQuoteSummaryRequest request = (FundQuoteSummaryRequest) object;
        String site = request.getSiteKey();
        // Invoke Service
        QuoteSummaryData quoteSumData = (QuoteSummaryData) sendRequest(request);

        // Set Response
        FundQuoteSummaryResponse resp = new FundQuoteSummaryResponse();
        Summary summary = new Summary();
        List<ProspectusPrimaryIndex> prospectusPrimaryIndexs = null;

        String prodSubType = null;
        SearchableObject searchObject = this.getSearchProduct(request).getSearchObject();
        if (null != searchObject) {
            prodSubType = searchObject.getProductSubType();
        }

        String prodType = request.getProductType();

        boolean bestFitIndexCalendarYearMap = bestFitIndexCalendarYearMap(site, prodType, prodSubType);
        boolean needGetDatapointNewApiSpecail = needGetDatapointNewApiSpecail(site, prodType, prodSubType);
        LogUtil.debug(QuoteSummaryServiceExecutor.class, "bestFitIndexCalendarYearMap is " + bestFitIndexCalendarYearMap
            + ", noNeedGetDatapointNewApiSpecail is " + needGetDatapointNewApiSpecail);

        if (null != quoteSumData) {

            // Init service object
            Profile fundProfile = summary.new Profile();
            HoldingDetails holdingDetails = summary.new HoldingDetails();
            ToNewInvestors toNewInvestors = summary.new ToNewInvestors();
            FeesAndExpenses feesAndExpenses = summary.new FeesAndExpenses();
            MorningstarRatings morningstarRatings = summary.new MorningstarRatings();
            MgmtAndContactInfo mgmtAndContactInfo = summary.new MgmtAndContactInfo();
            InvestmentStrategy investmentStrategy = summary.new InvestmentStrategy();
            YieldAndCredit yieldAndCredit = summary.new YieldAndCredit();

            // set the value
            Data data = quoteSumData.getData();
            if (null != data) {
                Api api = data.getApi();
                if (null != api) {
                    summary.setBid(BigDecimalUtil.fromStringAndCheckNull(api.getTSDayEndBidPrice()));
                    summary.setWeekRangeHigh(BigDecimalUtil.fromStringAndCheckNull(api.getTSNAV52WkHigh()));
                    summary.setWeekRangeLow(BigDecimalUtil.fromStringAndCheckNull(api.getTSNAV52WkLow()));
                    summary.setOffer(BigDecimalUtil.fromStringAndCheckNull(api.getTSDayEndOfferPrice()));

                    fundProfile.setBestFitIndex(api.getMptpiIndexName());
                    fundProfile.setBestFitIndexCode(api.getMPTBFIndexID());
                    fundProfile.setCategoryCode(api.getDPCategoryCode());
                    fundProfile.setCategoryName(api.getDPCategoryName());
                    fundProfile.setDayEndBidOfferPricesDate(api.getTsDayEndBidOfferPricesDate());
                    fundProfile.setDayEndNAVDate(api.getTsDayEndNAVDate());

                    if (null != searchObject) {
                        summary.setProdAltNumSegs(searchObject.getProdAltNumSeg());

                        fundProfile.setCurrency(searchObject.getProductCcy());
                        fundProfile.setChannelRestrictList(searchObject.getResChannelCde());
                    }

                    // Get family from MDS B/E, but not MorningStar
                    AMCBAdministratorCompanies aMCBAdministratorCompanies = api.getAMCBAdministratorCompanies();
                    if (null != aMCBAdministratorCompanies) {
                        AdministratorCompany administratorCompany = aMCBAdministratorCompanies.getAdministratorCompany();
                        if (null != administratorCompany) {
                            fundProfile.setAdvisor(administratorCompany.getCompanyName());
                        }
                    }

                    AMCBSubAdvisorCompanies aMCBSubAdvisorCompanies = api.getAMCBSubAdvisorCompanies();
                    if (null != aMCBSubAdvisorCompanies) {
                        SubAdvisorCompany subAdvisorCompany = aMCBSubAdvisorCompanies.getSubAdvisorCompany();
                        if (null != subAdvisorCompany) {
                            fundProfile.setSubAdvisor(subAdvisorCompany.getCompanyName());
                        }
                    }

                    // fundProfile.setInceptionDate(api.getKDInceptionDate());
                    fundProfile.setInvestmentObjectiveAndStrategy(
                        this.getInvestmentStrategyBySiteLocale(api, request.getSiteKey(), request.getLocale()));
                    holdingDetails.setTotalNetAssets(api.getNAShareClassNetAssets());

                    String currencyId = MorningStarUtil.trimCurrency(StringUtil.toStringAndCheckNull(api.getFscbiCurrencyId()));
                    holdingDetails.setTotalNetAssetsCurrencyCode(currencyId);

                    holdingDetails.setAnnualPortfolioTurnover(BigDecimalUtil.fromStringAndCheckNull(api.getPSTurnoverRatio()));
                    // holdingDetails.setEquityStyle(api.getPSEquityStylebox());
                    holdingDetails.setFixedIncomeStyle(api.getPSFixedIncomeStylebox());
                    holdingDetails.setSharesOutstanding(BigDecimalUtil.fromStringAndCheckNull(api.getDMPSharesOutstanding()));
                    holdingDetails.setNetAssetValue(BigDecimalUtil.fromStringAndCheckNull(api.getDPDayEndNAV()));
                    // always return USD
                    // holdingDetails.setNetAssetValueCurrencyCode(QuoteSummaryServiceExecutor.USD);
                    holdingDetails.setPremiumDiscountToNAV(api.getDPPremiumDiscount());
                    holdingDetails.setPriceEarningsRatio(BigDecimalUtil.fromStringAndCheckNull(api.getPSPriceToEarnings()));
                    holdingDetails.setDividendYield(BigDecimalUtil.fromStringAndCheckNull(api.getYLDYield1Yr()));
                    holdingDetails.setDividendPerShare(api.getDPDividend());
                    holdingDetails.setDividendPerShareCurrencyCode(api.getDPCurrency());
                    holdingDetails.setExDividendDate(api.getDPDividendDate());
                    // For ETF only
                    holdingDetails.setTaxAdjustedRating(api.getTARRatingOverall());
                    holdingDetails.setBeta(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACBeta3Yr()));

                    // Get minInitInvst, minSubqInvst from MDS B/E, but not
                    // MorningStar
                    String purchaseCurrencyId = api.getPIPurchaseCurrencyId();
                    if (null != purchaseCurrencyId && purchaseCurrencyId.length() > 3) {
                        purchaseCurrencyId =
                            purchaseCurrencyId.substring(purchaseCurrencyId.length() - 3, purchaseCurrencyId.length());
                    }
                    toNewInvestors.setMinInitRRSPInvst(BigDecimalUtil.fromStringAndCheckNull(api.getPIMinimumIRA()));
                    // PurchaseCurrencyId - Fund Purchase Details
                    toNewInvestors.setMinInitRRSPInvstCurrencyCode(purchaseCurrencyId);
                    toNewInvestors.setMinSubqRRSPInvst(BigDecimalUtil.fromStringAndCheckNull(api.getPISubsequentIRA()));
                    // PurchaseCurrencyId - Fund Purchase Details
                    toNewInvestors.setMinSubqRRSPInvstCurrencyCode(purchaseCurrencyId);
                    toNewInvestors.setPurchaseCurId(purchaseCurrencyId);
                    toNewInvestors.setMinInitUnit(api.getPIMinimumInitialInvestmentUnit());
                    toNewInvestors.setMinSubqUnit(api.getPIMinimumSubsequentUnit());
                    PIClosedToInvestors piClosedToInvestors = api.getPIClosedToInvestors();
                    if (null != piClosedToInvestors) {
                        ClosedToInvestor closedToInvestor = piClosedToInvestors.getClosedToInvestor();
                        if (null != closedToInvestor) {
                            String date = closedToInvestor.getClosedToNewDate();
                            if (null != date) {
                                toNewInvestors.setIndicator(false);
                            } else {
                                toNewInvestors.setIndicator(true);
                            }
                        } else {
                            toNewInvestors.setIndicator(true);
                        }
                    } else {
                        toNewInvestors.setIndicator(true);
                    }

                    feesAndExpenses.setMaximumInitialSalesFees(BigDecimalUtil.fromStringAndCheckNull(api.getLSMaximumFrontLoad()));
                    feesAndExpenses.setMaximumDeferredSalesFees(BigDecimalUtil.fromStringAndCheckNull(api.getLSMaximumDeferLoad()));
                    feesAndExpenses.setActualManagementFee(BigDecimalUtil.fromStringAndCheckNull(api.getPFActualManagementFee()));
                    feesAndExpenses.setActualFrontLoad(BigDecimalUtil.fromStringAndCheckNull(api.getAtActualFrontLoad()));

                    ProspectusCustodianFee ProspectusCustodianFee = api.getProspectusCustodianFee();
                    if (null != ProspectusCustodianFee) {
                        if (ListUtil.isValid(ProspectusCustodianFee.getFeeSchedule())) {
                            FeeSchedule feeSchedule = ProspectusCustodianFee.getFeeSchedule().get(0);
                            String prospectusCustodianUnit = feeSchedule.getUnit();
                            String prospectusCustodianFeeStr = feeSchedule.getValue();
                            feesAndExpenses.setProspectusCustodianUnit(prospectusCustodianUnit);
                            feesAndExpenses
                                .setProspectusCustodianFee(BigDecimalUtil.fromStringAndCheckNull(prospectusCustodianFeeStr));
                        }
                    }
                    feesAndExpenses.setActualMER(BigDecimalUtil.fromStringAndCheckNull(api.getARFMER()));
                    // defect#38441
                    FSCBIFundServDetails fundServDetails = api.getFSCBIFundServDetails();
                    if (fundServDetails != null) {
                        List<FundServDetail> fundServDetailList = fundServDetails.getFundServDetail();
                        if (ListUtil.isValid(fundServDetailList)) {
                            String fundSrvCde = this.FUND_SRV_CODE_MAP.get(data.getId());
                            if (StringUtil.isValid(fundSrvCde)) {
                                for (FundServDetail detail : fundServDetailList) {
                                    if (fundSrvCde.equals(detail.getFundServ())) {
                                        feesAndExpenses.setLoadType(detail.getClassType());
                                    }
                                }
                            }
                        }
                    }

                    morningstarRatings.setMorningstarRatingOverall(api.getMRRatingOverall());
                    morningstarRatings.setMorningstarRating3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getMRRating3Year()));
                    morningstarRatings.setMorningstarRating5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getMRRating5Year()));
                    morningstarRatings.setMorningstarRating10Yr(BigDecimalUtil.fromStringAndCheckNull(api.getMRRating10Year()));
                    morningstarRatings.setMorningstarTaxAdjustedRatingOverall(api.getTARRatingOverall());
                    morningstarRatings
                        .setMorningstarTaxAdjustedRating3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTARRating3Yr()));
                    morningstarRatings
                        .setMorningstarTaxAdjustedRating5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTARRating5Yr()));
                    morningstarRatings
                        .setMorningstarTaxAdjustedRating10Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTARRating10Yr()));

                    morningstarRatings.setLastUpdatedDate(api.getMRRatingDate());

                    if (null != api.getAMCBAdministratorCompanies()) {
                        mgmtAndContactInfo
                            .setCompanyName(api.getAMCBAdministratorCompanies().getAdministratorCompany().getCompanyName());
                    }
                    if (null != api.getAMCBDistributorCompanies()) {
                        mgmtAndContactInfo
                            .setAddress(api.getAMCBDistributorCompanies().getDistributorCompany().getCompanyAddress());
                        mgmtAndContactInfo.setCity(api.getAMCBDistributorCompanies().getDistributorCompany().getCompanyCity());
                        mgmtAndContactInfo
                            .setProvince(api.getAMCBDistributorCompanies().getDistributorCompany().getCompanyProvince());
                        mgmtAndContactInfo
                            .setPostalCode(api.getAMCBDistributorCompanies().getDistributorCompany().getCompanyPostalCode());
                    }
                    // API change: get telephone and fax no from
                    // AMCB-ProviderCompanyCountryHeadquarters
                    AMCBProviderCompanyCountryHeadquarters aMCBProviderCompanyCountryHeadquarters =
                        api.getAMCBProviderCompanyCountryHeadquarters();
                    if (null != aMCBProviderCompanyCountryHeadquarters) {
                        CountryHeadquarters countryHeadquarters = aMCBProviderCompanyCountryHeadquarters.getCountryHeadquarters();
                        if (null != countryHeadquarters) {
                            if ("MRF".equalsIgnoreCase(prodSubType)) {
                                mgmtAndContactInfo.setAddress(countryHeadquarters.getCompanyAddress());
                                mgmtAndContactInfo.setCity(countryHeadquarters.getCompanyCity());
                                mgmtAndContactInfo.setProvince(countryHeadquarters.getCompanyProvince());
                                mgmtAndContactInfo.setPostalCode(countryHeadquarters.getCompanyPostalCode());
                                mgmtAndContactInfo.setCountry(countryHeadquarters.getCountryName());
                            }
                        }

                        Field fields;
                        String telephoneNo = this.siteFeature.getStringDefaultFeature(site, this.TEL_PHONE_NO);
                        if (StringUtil.isValid(telephoneNo)) {
                            if (this.LOCAL_PHONE_NO.equals(telephoneNo)) {
                                fields = api.getClass().getDeclaredField(telephoneNo);
                                fields.setAccessible(true);
                                mgmtAndContactInfo.setTelephoneNo(StringUtil.toStringAndCheckNull(fields.get(api)));
                            } else {
                                fields = countryHeadquarters.getClass().getDeclaredField(telephoneNo);
                                fields.setAccessible(true);
                                mgmtAndContactInfo.setTelephoneNo(StringUtil.toStringAndCheckNull(fields.get(countryHeadquarters)));
                            }
                        }
                        mgmtAndContactInfo
                            .setFaxNo(api.getAMCBProviderCompanyCountryHeadquarters().getCountryHeadquarters().getCompanyFax());
                    }

                    mgmtAndContactInfo.setWebsite(api.getFSCBIProviderCompanyWebsite());
                    List<ManagemenInfo> mgmtInfo = new ArrayList<ManagemenInfo>();
                    FM2Managers fMManagers = api.getFM2Managers();
                    if (null != fMManagers) {
                        List<Manager> managerList = fMManagers.getManager();
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
                    mgmtAndContactInfo.setMgmtInfos(mgmtInfo);
                } else {
                    return resp;
                }

                investmentStrategy.setAssetAllocBondNet(BigDecimalUtil.fromStringAndCheckNull(api.getAabrpAssetAllocBondNet()));
                investmentStrategy.setPreferredStockNet(BigDecimalUtil.fromStringAndCheckNull(api.getAabrpPreferredStockNet()));
                investmentStrategy.setAssetAllocCashNet(BigDecimalUtil.fromStringAndCheckNull(api.getAabrpAssetAllocCashNet()));
                investmentStrategy.setOtherNet(BigDecimalUtil.fromStringAndCheckNull(api.getAabrpOtherNet()));
                investmentStrategy.setAssetAllocEquityNet(BigDecimalUtil.fromStringAndCheckNull(api.getAabrpAssetAllocEquityNet()));

                yieldAndCredit.setCreditQualA(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualA()));
                yieldAndCredit.setCreditQualAA(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualAA()));
                yieldAndCredit.setCreditQualAAA(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualAAA()));
                yieldAndCredit.setCreditQualB(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualB()));
                yieldAndCredit.setCreditQualBB(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualBB()));
                yieldAndCredit.setCreditQualBBB(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualBBB()));
                yieldAndCredit.setCreditQualBelowB(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualBelowB()));
                yieldAndCredit.setCreditQualNotRated(BigDecimalUtil.fromStringAndCheckNull(api.getCqbrpCreditQualNotRated()));
                yieldAndCredit.setCreditQualDate(api.getCqbrpCreditQualDate());

                prospectusPrimaryIndexs = data.getProspectusPrimaryIndexs();
                summary.setCalendarYearTotalReturns(this.getCalendarYearTotalReturns(summary, api, prospectusPrimaryIndexs,
                    bestFitIndexCalendarYearMap, fundProfile));
                summary.setCumulativeTotalReturns(this.getCumulativeTotalReturns(summary, api, prospectusPrimaryIndexs));
                summary.setProfile(fundProfile);
                summary.setHoldingDetails(holdingDetails);
                summary.setToNewInvestors(toNewInvestors);
                summary.setFeesAndExpenses(feesAndExpenses);
                summary.setMorningstarRatings(morningstarRatings);
                summary.setMgmtAndContactInfo(mgmtAndContactInfo);
                summary.setInvestmentStrategy(investmentStrategy);
                summary.setYieldAndCredit(yieldAndCredit);
            } else {
                return resp;
            }
        }


        if (this.siteFeature.getBooleanFeature(site, this.SITE_FEATURE_MSTAR_MSTARNEWAPI, false)) {
            DataSet.QuickTake dataset = null;

            if (needGetDatapointNewApiSpecail) {
                DataSet quoteDataSet = (DataSet) sendRequest2NewApi(request);
                if (null != quoteDataSet) {
                    dataset = quoteDataSet.getQuickTake();
                    if (null != dataset) {
                        int index = this.localeMappingUtil
                            .getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + request.getLocale());
                        // only show Locale = zh_CN
                        if (1 == index) {
                            LogUtil.debug(QuoteSummaryServiceExecutor.class, "setManagemenInfo for chinese");
                            MgmtAndContactInfo mgmtAndContactInfo = summary.getMgmtAndContactInfo();
                            if (null == mgmtAndContactInfo) {
                                summary.setMgmtAndContactInfo(summary.new MgmtAndContactInfo());
                            }
                            FundInfo fundInfo = dataset.getFundInfo();
                            if (null != fundInfo) {
                                Profile profile = summary.getProfile();
                                if (null == profile) {
                                    summary.setProfile(summary.new Profile());
                                }
                                summary.getProfile().setCategoryName(fundInfo.getCategory());
                                summary.getMgmtAndContactInfo().setCompanyName(fundInfo.getFundCompanyNameLocal());
                                summary.getMgmtAndContactInfo().setAddress(fundInfo.getFundCompanyAddressLine1Local());
                                summary.getMgmtAndContactInfo().setTelephoneNo(fundInfo.getFundCompanyTel());
                            }

                            List<ManagemenInfo> managerList = new ArrayList<ManagemenInfo>();
                            ManagemenInfo managerInfo = new ManagemenInfo();
                            FundManager fundManager = dataset.getFundManager();
                            if (null != fundManager) {
                                managerInfo.setManagerName(fundManager.getManagerName());
                                String startDate = fundManager.getStartDate();
                                if (StringUtil.isValid(startDate) && startDate.contains("T")) {
                                    startDate = startDate.substring(0, startDate.indexOf("T"));
                                }
                                managerInfo.setStartDate(startDate);
                            }
                            managerList.add(managerInfo);
                            summary.getMgmtAndContactInfo().setMgmtInfos(managerList);
                        }

                        Fee fee = dataset.getFee();
                        if (null != fee) {
                            LogUtil.debug(QuoteSummaryServiceExecutor.class, "setActualManagementFee and setActualMER");
                            FeesAndExpenses feesAndExpenses = summary.getFeesAndExpenses();
                            if (null == feesAndExpenses) {
                                summary.setFeesAndExpenses(summary.new FeesAndExpenses());
                            }
                            summary.getFeesAndExpenses().setActualManagementFee(new BigDecimal(fee.getActualManagementFee()));
                            summary.getFeesAndExpenses().setActualMER(new BigDecimal(fee.getCustodialFee()));
                        }

                        // "equityStyle" -- "StyleBox" Just for Local UT,GLCM
                        PortfolioSummary portfolioSummary = dataset.getPortfolioSummary();
                        if (null != portfolioSummary) {
                            LogUtil.debug(QuoteSummaryServiceExecutor.class, "setEquityStyle");
                            HoldingDetails holdingDetails = summary.getHoldingDetails();
                            if (null == holdingDetails) {
                                summary.setHoldingDetails(summary.new HoldingDetails());
                            }
                            summary.getHoldingDetails().setEquityStyle(portfolioSummary.getStyleBox());
                        }
                    }
                } else {
                    String msg = "QuoteSummaryServiceImpl|Invalid response for the symbol=" + request.getProdAltNum();
                    LogUtil.error(QuoteSummaryServiceExecutor.class, msg);
                }
            }
        }
        resp.setSummary(summary);

        return resp;
    }

    private CalendarYearTotalReturns getCalendarYearTotalReturns(final Summary summary, final Api api,
        final List<ProspectusPrimaryIndex> prospectusPrimaryIndexs, final boolean bestFitIndexCalendarYearMap,
        final Profile fundProfile) throws Exception {
        CalendarYearTotalReturns calendarYearTotalReturns = summary.new CalendarYearTotalReturns();
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
        fundCalendarYearReturn1.setFundCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRYear1()));
        fundCalendarYearReturn1
            .setBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRBestFitIndexYear1()));
        fundCalendarYearReturn1.setCategoryCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRCategoryYear1()));
        this.setStubYearEndReturn(fundCalendarYearReturn1, stubYear, stubYearEndReturn, cal);

        FundCalendarYearReturn fundCalendarYearReturn2 = new FundCalendarYearReturn();
        cal.add(Calendar.YEAR, -1);
        fundCalendarYearReturn2
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn2.setFundCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRYear2()));
        fundCalendarYearReturn2
            .setBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRBestFitIndexYear2()));
        fundCalendarYearReturn2.setCategoryCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRCategoryYear2()));
        this.setStubYearEndReturn(fundCalendarYearReturn2, stubYear, stubYearEndReturn, cal);

        FundCalendarYearReturn fundCalendarYearReturn3 = new FundCalendarYearReturn();
        cal.add(Calendar.YEAR, -1);
        fundCalendarYearReturn3
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn3.setFundCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRYear3()));
        fundCalendarYearReturn3
            .setBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRBestFitIndexYear3()));
        fundCalendarYearReturn3.setCategoryCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRCategoryYear3()));
        this.setStubYearEndReturn(fundCalendarYearReturn3, stubYear, stubYearEndReturn, cal);

        FundCalendarYearReturn fundCalendarYearReturn4 = new FundCalendarYearReturn();
        cal.add(Calendar.YEAR, -1);
        fundCalendarYearReturn4
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn4.setFundCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRYear4()));
        fundCalendarYearReturn4
            .setBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRBestFitIndexYear4()));
        fundCalendarYearReturn4.setCategoryCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRCategoryYear4()));
        this.setStubYearEndReturn(fundCalendarYearReturn4, stubYear, stubYearEndReturn, cal);

        FundCalendarYearReturn fundCalendarYearReturn5 = new FundCalendarYearReturn();
        cal.add(Calendar.YEAR, -1);
        fundCalendarYearReturn5
            .setYear(BigDecimalUtil.fromStringAndCheckNull(StringUtil.toStringAndCheckNull(cal.get(Calendar.YEAR))));
        fundCalendarYearReturn5.setFundCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRYear5()));
        fundCalendarYearReturn5
            .setBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRBestFitIndexYear5()));
        fundCalendarYearReturn5.setCategoryCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRCategoryYear5()));
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

        if (bestFitIndexCalendarYearMap) {
            LogUtil.debug(QuoteSummaryServiceExecutor.class, "Set CYRPrimaryIndexYear to BestFitIndexCalendarYearReturn");
            fundProfile.setBestFitIndexCode(api.getMPTPIIndexID());
            fundCalendarYearReturn1
                .setBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRPrimaryIndexYear1()));
            fundCalendarYearReturn2
                .setBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRPrimaryIndexYear2()));
            fundCalendarYearReturn3
                .setBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRPrimaryIndexYear3()));
            fundCalendarYearReturn4
                .setBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRPrimaryIndexYear4()));
            fundCalendarYearReturn5
                .setBestFitIndexCalendarYearReturn(BigDecimalUtil.fromStringAndCheckNull(api.getCYRPrimaryIndexYear5()));
        }

        calendarItem.add(fundCalendarYearReturn1);
        calendarItem.add(fundCalendarYearReturn2);
        calendarItem.add(fundCalendarYearReturn3);
        calendarItem.add(fundCalendarYearReturn4);
        calendarItem.add(fundCalendarYearReturn5);

        calendarYearTotalReturns.setItems(calendarItem);
        calendarYearTotalReturns.setBestFitIndex(api.getMptpiIndexName());
        calendarYearTotalReturns.setLastUpdatedDate(api.getTtrMonthEndDate());
        calendarYearTotalReturns.setDailyLastUpdatedDate(api.getDpDayEndDate());

        return calendarYearTotalReturns;
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

    private CumulativeTotalReturns getCumulativeTotalReturns(final Summary summary, final Api api,
        final List<ProspectusPrimaryIndex> prospectusPrimaryIndexs) throws Exception {
        CumulativeTotalReturns cumulativeTotalReturns = summary.new CumulativeTotalReturns();
        List<FundCumulativeReturn> cumulativeItem = new ArrayList<FundCumulativeReturn>();

        FundCumulativeReturn fundCumulativeReturnYTD = new FundCumulativeReturn();
        fundCumulativeReturnYTD.setPeriod("YTD");
        fundCumulativeReturnYTD.setTotalReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturnYTD()));
        fundCumulativeReturnYTD.setTotalDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpReturnYTD()));
        fundCumulativeReturnYTD.setBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTTRBestFitIndexReturnYTD()));
        fundCumulativeReturnYTD.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTTRCategoryReturnYTD()));
        fundCumulativeReturnYTD.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturnYTD()));
        fundCumulativeReturnYTD.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturnYTD()));

        FundCumulativeReturn fundCumulativeReturn1M = new FundCumulativeReturn();
        fundCumulativeReturn1M.setPeriod("1M");
        fundCumulativeReturn1M.setTotalReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrReturn1Mth()));
        fundCumulativeReturn1M.setTotalDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpReturn1Mth()));
        fundCumulativeReturn1M.setBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn1Mth()));
        fundCumulativeReturn1M.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn1Mth()));
        fundCumulativeReturn1M.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn1Mth()));
        fundCumulativeReturn1M.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn1Mth()));

        FundCumulativeReturn fundCumulativeReturn3M = new FundCumulativeReturn();
        fundCumulativeReturn3M.setPeriod("3M");
        fundCumulativeReturn3M.setTotalReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrReturn3Mth()));
        fundCumulativeReturn3M.setTotalDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpReturn3Mth()));
        fundCumulativeReturn3M.setBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn3Mth()));
        fundCumulativeReturn3M.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn3Mth()));
        fundCumulativeReturn3M.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn3Mth()));
        fundCumulativeReturn3M.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn3Mth()));

        FundCumulativeReturn fundCumulativeReturn6M = new FundCumulativeReturn();
        fundCumulativeReturn6M.setPeriod("6M");
        fundCumulativeReturn6M.setTotalReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrReturn6Mth()));
        fundCumulativeReturn6M.setTotalDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpReturn6Mth()));
        fundCumulativeReturn6M.setBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn6Mth()));
        fundCumulativeReturn6M.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn6Mth()));
        fundCumulativeReturn6M.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn6Mth()));
        fundCumulativeReturn6M.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn6Mth()));

        FundCumulativeReturn fundCumulativeReturn1Y = new FundCumulativeReturn();
        fundCumulativeReturn1Y.setPeriod("1Y");
        fundCumulativeReturn1Y.setTotalReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrReturn1Yr()));
        fundCumulativeReturn1Y.setTotalDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpReturn1Yr()));
        fundCumulativeReturn1Y.setBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn1Yr()));
        fundCumulativeReturn1Y.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn1Yr()));
        fundCumulativeReturn1Y.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn1Yr()));
        fundCumulativeReturn1Y.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn1Yr()));

        FundCumulativeReturn fundCumulativeReturn3Y = new FundCumulativeReturn();
        fundCumulativeReturn3Y.setPeriod("3Y");
        fundCumulativeReturn3Y.setTotalReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrReturn3Yr()));
        fundCumulativeReturn3Y.setTotalDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpReturn3Yr()));
        fundCumulativeReturn3Y.setBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn3Yr()));
        fundCumulativeReturn3Y.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn3Yr()));
        fundCumulativeReturn3Y.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn3Yr()));
        fundCumulativeReturn3Y.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn3Yr()));

        FundCumulativeReturn fundCumulativeReturn5Y = new FundCumulativeReturn();
        fundCumulativeReturn5Y.setPeriod("5Y");
        fundCumulativeReturn5Y.setTotalReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrReturn5Yr()));
        fundCumulativeReturn5Y.setTotalDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpReturn5Yr()));
        fundCumulativeReturn5Y.setBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn5Yr()));
        fundCumulativeReturn5Y.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn5Yr()));
        fundCumulativeReturn5Y.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn5Yr()));
        fundCumulativeReturn5Y.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn5Yr()));

        FundCumulativeReturn fundCumulativeReturn10Y = new FundCumulativeReturn();
        fundCumulativeReturn10Y.setPeriod("10Y");
        fundCumulativeReturn10Y.setTotalReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrReturn10Yr()));
        fundCumulativeReturn10Y.setTotalDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpReturn10Yr()));
        fundCumulativeReturn10Y.setBestFitIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrBestFitIndexReturn10Yr()));
        fundCumulativeReturn10Y.setCategoryReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrCategoryReturn10Yr()));
        fundCumulativeReturn10Y.setCategoryDailyReturn(BigDecimalUtil.fromStringAndCheckNull(api.getDpCategoryReturn10Yr()));
        fundCumulativeReturn10Y.setPrimaryIndexReturn(BigDecimalUtil.fromStringAndCheckNull(api.getTtrPrimaryIndexReturn10Yr()));

        // ProspectusPrimaryIndexReturn
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

        cumulativeItem.add(fundCumulativeReturnYTD);
        cumulativeItem.add(fundCumulativeReturn1M);
        cumulativeItem.add(fundCumulativeReturn3M);
        cumulativeItem.add(fundCumulativeReturn6M);
        cumulativeItem.add(fundCumulativeReturn1Y);
        cumulativeItem.add(fundCumulativeReturn3Y);
        cumulativeItem.add(fundCumulativeReturn5Y);
        cumulativeItem.add(fundCumulativeReturn10Y);

        cumulativeTotalReturns.setItems(cumulativeItem);
        cumulativeTotalReturns.setBestFitIndex(api.getMptpiIndexName());

        cumulativeTotalReturns.setLastUpdatedDate(api.getTtrMonthEndDate());
        cumulativeTotalReturns.setDailyLastUpdatedDate(api.getDpDayEndDate());

        return cumulativeTotalReturns;
    }

    private boolean bestFitIndexCalendarYearMap(final String site, final String prodType, final String prodSubType) {
        String strCoundition = this.bestFitIndexCalendarYearMap.get(site);
        boolean flag = false;
        if (StringUtil.isValid(strCoundition) && StringUtil.isValid(prodType) && StringUtil.isValid(prodSubType)) {
            if (strCoundition.contains(prodType) && strCoundition.contains(prodSubType)) {
                flag = true;
            } else if (CommonConstants.ALL.equals(strCoundition)) {
                flag = true;
            }
        }
        return flag;
    }

    private boolean needGetDatapointNewApiSpecail(final String site, final String prodType, final String prodSubType) {
        String strCoundition = this.fieldNeeddataPointNewapiMap.get(site);
        boolean flag = false;
        if (StringUtil.isValid(strCoundition) && StringUtil.isValid(prodType) && StringUtil.isValid(prodSubType)) {
            if (strCoundition.contains(prodType) && strCoundition.contains(prodSubType)) {
                flag = true;
            } else if (CommonConstants.ALL.equals(strCoundition)) {
                flag = true;
            }
        }
        return flag;
    }


    private String getInvestmentStrategyBySiteLocale(final Api api, final String site, final String locale) {
        // set default value as response
        String result = api.getICInvestmentStrategy();
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
        ICMultilingualInvestmentStrategies multiInvestStrategies = api.getICMultilingualInvestmentStrategies();
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

    protected Object sendRequest(final FundQuoteSummaryRequest request) throws Exception {
        SearchProduct searchProduct = this.getSearchProduct(request);
        if (StringUtil.isInvalid(searchProduct.getExternalKey())) {
            LogUtil.errorBeanToJson(QuoteSummaryServiceExecutor.class, "No record found for Mstar|ProdAltNum=", searchProduct);
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND,
                "No record found for " + request.getProdAltNum());
        } else {
            this.FUND_SRV_CODE_MAP.clear();
            this.saveFundSrvCdeForLoadType(searchProduct);
        }
        String id = searchProduct.getExternalKey();
        return super.sendRequest(MstarConstants.MSTARID, id, this.url, this.dataClassJC.createUnmarshaller());
    }


    protected Object sendRequest2NewApi(final FundQuoteSummaryRequest request) throws Exception {
        String site = request.getSiteKey();
        String prodAltNum = request.getProdAltNum();
        SearchProduct searchProduct = this.getSearchProduct(request);
        this.FUND_SRV_CODE_MAP.clear();
        this.saveFundSrvCdeForLoadType(searchProduct);
        String siteType = this.siteFeature.getStringFeature(site, this.SITE_FEATURE_MSTAR_MSTARNEWAPI_SITETYPE);
        if (StringUtil.isValid(site) && CommonConstants.ENTITY_CN_GLCM.equalsIgnoreCase(site)) {
            return super.sendRequest2NewApi(MstarConstants.MSTARID, prodAltNum, this.urlApiGlcm,
                this.dataSetClassJC.createUnmarshaller(), siteType);
        } else {
            return super.sendRequest2NewApi(MstarConstants.MSTARID, prodAltNum, this.urlApi,
                this.dataSetClassJC.createUnmarshaller(), siteType);
        }
    }

    public SearchProduct getSearchProduct(final FundQuoteSummaryRequest request) throws Exception {
        String altClassCde = request.getProdCdeAltClassCde();
        String countryCode = request.getCountryCode();
        String groupMember = request.getGroupMember();
        String prodAltNum = request.getProdAltNum();
        String countryTradableCode = request.getMarket();
        String productType = request.getProductType();
        String locale = request.getLocale();
        SearchProduct searchProduct = this.internalProductKeyUtil.getProductBySearchWithAltClassCde(altClassCde, countryCode,
            groupMember, prodAltNum, countryTradableCode, productType, locale);
        if (null == searchProduct) {
            LogUtil.error(QuoteSummaryServiceExecutor.class, "No record found for Mstar|ProdAltNum=" + prodAltNum);
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND, "No record found for " + prodAltNum);
        }
        return searchProduct;
    }


    private void saveFundSrvCdeForLoadType(final SearchProduct searchProduct) {
        if (searchProduct != null) {
            String keyProdAltNum = searchProduct.getExternalKey();
            SearchableObject searchableProduct = searchProduct.getSearchObject();
            if (searchableProduct != null) {
                List<ProdAltNumSeg> prodAltNumSegList = searchableProduct.getProdAltNumSeg();
                if (ListUtil.isValid(prodAltNumSegList) && (!StringUtils.isBlank(keyProdAltNum))) {
                    for (ProdAltNumSeg prodAltNumSeq : prodAltNumSegList) {
                        if (this.FUND_SRV_CDE_TYPE.equals(prodAltNumSeq.getProdCdeAltClassCde())) {
                            this.FUND_SRV_CODE_MAP.put(keyProdAltNum, prodAltNumSeq.getProdAltNum());
                            break;
                        }
                    }
                }
            }
        }
    }
}
