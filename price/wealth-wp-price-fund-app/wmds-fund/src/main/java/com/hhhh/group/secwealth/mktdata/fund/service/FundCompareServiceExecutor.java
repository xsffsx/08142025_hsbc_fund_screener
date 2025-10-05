
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundcompare.Api;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundcompare.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundcompare.FSCBIFundServDetails;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundcompare.FundCompareData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundcompare.FundServDetail;
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
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareProduct.Summary;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareRisk;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareRisk.YearRisk;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;


@Service("fundCompareServiceExecutor")
public class FundCompareServiceExecutor extends AbstractMstarService {

    private final String ASSETS_UNDER_MANAGEMENT = ".assetsUnderManagement";
    private final String ASSETS_UNDER_MANAGEMENT_CURRENCY = ".assetsUnderManagementCurrencyCode";

    @Value("#{systemConfig['mstar.conn.url.fundcompare']}")
    private String url;

    @Value("#{systemConfig['fundcompare.dataClass']}")
    private String dataClass;

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    @Autowired
    @Qualifier("siteFeature")
    private SiteFeature siteFeature;

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
        FundCompareRequest request = (FundCompareRequest) object;
        String site = request.getSiteKey();

        // Invoke service
        Object[] arr = sendFundCompareRequest(request);
        if (null == arr || arr.length == 0) {
            LogUtil.errorBeanToJson(FundCompareServiceExecutor.class,
                "Can not get record from morningstar, No record found for FundCompare|request=", request);
            return null;
        }
        FundCompareData fundCompareData = (FundCompareData) arr[0];

        // Prepare response
        FundCompareResponse response = new FundCompareResponse();
        if (null != fundCompareData) {
            Map<String, Data> mstarResponseMap = new HashMap<String, Data>();
            List<Data> datas = fundCompareData.getData();
            if (null != datas && datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    Data d = datas.get(i);
                    if (null != d) {
                        mstarResponseMap.put(d.getId(), d);
                    }
                }
            }

            if (arr.length == 2) {
                List<SearchProduct> products = (List<SearchProduct>) arr[1];
                response.setSearchProductList(products);
                List<FundCompareProduct> product = new ArrayList<FundCompareProduct>();
                for (SearchProduct prod : products) {
                    String symbol = null;
                    if (null != prod && null != prod.getSearchObject()) {
                        symbol = prod.getSearchObject().getSymbol();
                    }
                    String id = prod.getExternalKey();
                    // Init service object
                    FundCompareProduct fundCompareProduct = new FundCompareProduct();
                    Header header = fundCompareProduct.new Header();
                    Summary summary = fundCompareProduct.new Summary();
                    Profile profile = fundCompareProduct.new Profile();
                    Rating rating = fundCompareProduct.new Rating();
                    Performance performance = fundCompareProduct.new Performance();
                    AnnualizedReturns annualizedReturns = performance.new AnnualizedReturns();
                    performance.setAnnualizedReturns(annualizedReturns);
                    CalendarReturns calendarReturns = performance.new CalendarReturns();
                    performance.setCalendarReturns(calendarReturns);
                    List<FundCompareRisk> risk = new ArrayList<FundCompareRisk>();
                    PurchaseInfo purchaseInfo = fundCompareProduct.new PurchaseInfo();

                    Data data = mstarResponseMap.get(id);
                    if (null != data) {
                        Api api = data.getApi();
                        if (null != api) {
                            header.setName(api.getFSCBIFundName());
                            // String currencyID = api.getFscbiCurrencyId();
                            // // keep the last 3 char
                            // if (null != currencyID && currencyID.length() >
                            // 3) {
                            // currencyID =
                            // currencyID.substring(currencyID.length() - 3,
                            // currencyID.length());
                            // header.setCurrency(currencyID);
                            // }
                            // header.setProdAltNum(fundSrvCde);
                            summary.setCategoryName(api.getDPCategoryName());

                            Field fields;
                            String assetsUnderManagement = this.siteFeature.getStringFeature(site, this.ASSETS_UNDER_MANAGEMENT);
                            if (StringUtil.isValid(assetsUnderManagement)) {
                                fields = api.getClass().getDeclaredField(assetsUnderManagement);
                                fields.setAccessible(true);
                                summary.setAssetsUnderManagement(BigDecimalUtil.fromStringAndCheckNull(String.valueOf(fields
                                    .get(api))));
                            }
                            String assetsUnderManagementCurrencyCode = this.siteFeature.getStringFeature(site,
                                this.ASSETS_UNDER_MANAGEMENT_CURRENCY);
                            if (StringUtil.isValid(assetsUnderManagementCurrencyCode)) {
                                if (MstarConstants.CURRENCY_USD.equals(assetsUnderManagementCurrencyCode)) {
                                    summary.setAssetsUnderManagementCurrencyCode(MstarConstants.CURRENCY_USD);
                                } else {
                                    fields = api.getClass().getDeclaredField(assetsUnderManagementCurrencyCode);
                                    fields.setAccessible(true);
                                    summary.setAssetsUnderManagementCurrencyCode(StringUtil.toStringAndCheckNull(fields.get(api)));
                                }
                            }
                            summary.setTotalNetAsset(BigDecimalUtil.fromStringAndCheckNull(api.getNAShareClassNetAssets()));
                            // summary.setTotalNetAssetCurrencyCode(currencyID);
                            // summary.setRatingOverall(api.getMRRatingOverall());
                            // summary.setMer(BigDecimalUtil.fromStringAndCheckNull(api.getARFMER()));
                            // summary.setYield1Yr(BigDecimalUtil.fromStringAndCheckNull(api.getYLDYield1Yr()));
                            // summary.setActualManagementFee(BigDecimalUtil.fromStringAndCheckNull(api.getPfActualManagementFee()));

                            // profile.setInceptionDate(api.getKDInceptionDate());
                            // profile.setTurnoverRatio(BigDecimalUtil.fromStringAndCheckNull(api.getPSTurnoverRatio()));
                            // profile.setStdDev3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMStdDev3Yr()));
                            // profile.setEquityStylebox(api.getPSEquityStylebox());
                            // rating.setMorningstarRating(api.getMRRatingOverall());

                            if (CommonConstants.ENTITY_UK.equals(site)) {
                                rating.setRatingDate(api.getMrRatingDate());
                                annualizedReturns.setMonthEndDate(api.getTtrMonthEndDate());
                                summary.setEndDate(api.getDpDayEndDate());
                                summary.setDayEndDate(api.getDpDayEndDate());
                            }
                            // Mapping
                            // String creditQuality =
                            // api.getPSAverageCreditQuality();
                            // rating.setAverageCreditQualityName(null);
                            // rating.setAverageCreditQuality(creditRatingMapping(creditQuality));
                            // rating.setRank1Yr(api.getTtrrRank1YrQuartile());
                            // annualizedReturns.setReturn1Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn1Mth()));
                            // annualizedReturns.setReturn3Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn3Mth()));
                            // annualizedReturns.setReturn6Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn6Mth()));
                            // annualizedReturns.setReturn1Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn1Yr()));
                            // annualizedReturns.setReturn3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn3Yr()));
                            // annualizedReturns.setReturn5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn5Yr()));
                            // annualizedReturns.setReturn10Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn10Yr()));
                            // annualizedReturns.setReturnSinceInception(BigDecimalUtil.fromStringAndCheckNull(api
                            // .getTTRReturnSinceInception()));
                            // calendarReturns.setReturnYTD(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturnYTD()));
                            // calendarReturns.setYear1(BigDecimalUtil.fromStringAndCheckNull(api.getCYRYear1()));
                            // calendarReturns.setYear2(BigDecimalUtil.fromStringAndCheckNull(api.getCYRYear2()));
                            // calendarReturns.setYear3(BigDecimalUtil.fromStringAndCheckNull(api.getCYRYear3()));
                            // calendarReturns.setYear4(BigDecimalUtil.fromStringAndCheckNull(api.getCYRYear4()));
                            // calendarReturns.setYear5(BigDecimalUtil.fromStringAndCheckNull(api.getCYRYear5()));
                            // Year1 Risk
                            FundCompareRisk fundCompareYear1Risk = new FundCompareRisk();
                            YearRisk year1Risk = fundCompareYear1Risk.new YearRisk();
                            // year1Risk.setYear(MstarConstants.ONE);
                            // year1Risk.setBeta(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACBeta1Yr()));
                            // year1Risk.setStdDev(BigDecimalUtil.fromStringAndCheckNull(api.getRMStdDev1Yr()));
                            // year1Risk.setAlpha(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACAlpha1Yr()));
                            // year1Risk.setSharpeRatio(BigDecimalUtil.fromStringAndCheckNull(api.getRMSharpeRatio1Yr()));
                            // year1Risk.setrSquared(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACRsquared1Yr()));
                            if (CommonConstants.ENTITY_UK.equals(site)) {
                                year1Risk.setEndDate(api.getRmEndDate());
                            }
                            fundCompareYear1Risk.setYearRisk(year1Risk);
                            risk.add(fundCompareYear1Risk);
                            // Year3 Risk
                            FundCompareRisk fundCompareYear3Risk = new FundCompareRisk();
                            YearRisk year3Risk = fundCompareYear3Risk.new YearRisk();
                            // year3Risk.setYear(MstarConstants.THREE);
                            // year3Risk.setBeta(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACBeta3Yr()));
                            // year3Risk.setStdDev(BigDecimalUtil.fromStringAndCheckNull(api.getRMStdDev3Yr()));
                            // year3Risk.setAlpha(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACAlpha3Yr()));
                            // year3Risk.setSharpeRatio(BigDecimalUtil.fromStringAndCheckNull(api.getRMSharpeRatio3Yr()));
                            // year3Risk.setrSquared(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACRsquared3Yr()));
                            if (CommonConstants.ENTITY_UK.equals(site)) {
                                year3Risk.setEndDate(api.getRmEndDate());
                            }
                            fundCompareYear3Risk.setYearRisk(year3Risk);
                            risk.add(fundCompareYear3Risk);
                            // Year5 Risk
                            FundCompareRisk fundCompareYear5Risk = new FundCompareRisk();
                            YearRisk year5Risk = fundCompareYear5Risk.new YearRisk();
                            // year5Risk.setYear(MstarConstants.FIVE);
                            // year5Risk.setBeta(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACBeta5Yr()));
                            // year5Risk.setStdDev(BigDecimalUtil.fromStringAndCheckNull(api.getRMStdDev5Yr()));
                            // year5Risk.setAlpha(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACAlpha5Yr()));
                            // year5Risk.setSharpeRatio(BigDecimalUtil.fromStringAndCheckNull(api.getRMSharpeRatio5Yr()));
                            // year5Risk.setrSquared(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACRsquared5Yr()));
                            if (CommonConstants.ENTITY_UK.equals(site)) {
                                year5Risk.setEndDate(api.getRmEndDate());
                            }
                            fundCompareYear5Risk.setYearRisk(year5Risk);
                            risk.add(fundCompareYear5Risk);
                            // Year10 Risk
                            FundCompareRisk fundCompareYear10Risk = new FundCompareRisk();
                            YearRisk year10Risk = fundCompareYear10Risk.new YearRisk();
                            // year10Risk.setYear(MstarConstants.TEN);
                            // year10Risk.setBeta(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACBeta10Yr()));
                            // year10Risk.setStdDev(BigDecimalUtil.fromStringAndCheckNull(api.getRMStdDev10Yr()));
                            // year10Risk.setAlpha(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACAlpha10Yr()));
                            // year10Risk.setSharpeRatio(BigDecimalUtil.fromStringAndCheckNull(api.getRMSharpeRatio10Yr()));
                            // year10Risk.setrSquared(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACRsquared10Yr()));
                            if (CommonConstants.ENTITY_UK.equals(site)) {
                                year10Risk.setEndDate(api.getRmEndDate());
                            }
                            fundCompareYear10Risk.setYearRisk(year10Risk);
                            risk.add(fundCompareYear10Risk);

                            if (MstarConstants.PRODUCTTYPE_UT.equals(request.getProductKeys().get(0).getProductType())) {
                                // rating.setTaxAdjustedRating(api.getTARRatingOverall());
                                purchaseInfo.setMinimumIRA(BigDecimalUtil.fromStringAndCheckNull(api.getPIMinimumIRA()));
                                String purchaseCurrencyId = api.getPiPurchaseCurrencyId();
                                if (null != purchaseCurrencyId && purchaseCurrencyId.length() > 3) {
                                    purchaseCurrencyId = purchaseCurrencyId.substring(purchaseCurrencyId.length() - 3,
                                        purchaseCurrencyId.length());
                                }
                                purchaseInfo.setMinimumIRACurrencyCode(purchaseCurrencyId);
                                if ("1".equals(api.getATRRSP())) {
                                    purchaseInfo.setRrsp(true);
                                } else {
                                    purchaseInfo.setRrsp(false);
                                }

                                FSCBIFundServDetails fundServDetails = api.getFSCBIFundServDetails();
                                if (fundServDetails != null) {
                                    List<FundServDetail> fundServDetailList = fundServDetails.getFundServDetail();
                                    if (ListUtil.isValid(fundServDetailList)) {
                                        if ((symbol != null) && (!"".equals(symbol))) {
                                            for (FundServDetail detail : fundServDetailList) {
                                                if (symbol.equals(detail.getFundServ())) {
                                                    purchaseInfo.setLoadType(detail.getClassType());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (MstarConstants.PRODUCTTYPE_ETF.equals(request.getProductKeys().get(0).getProductType())) {
                                header.setProdAltNum(api.getFSCBITicker());
                                summary.setChangeAmountNAV(BigDecimalUtil.fromStringAndCheckNull(api.getDMPDayEndPrice()));
                                summary.setChangePercentageNAV(BigDecimalUtil.fromStringAndCheckNull(api.getDMPDayEndPrice()));
                            }
                        }
                        fundCompareProduct.setProdAltNumXCode(id);
                        fundCompareProduct.setSymbol(symbol);
                        fundCompareProduct.setHeader(header);
                        fundCompareProduct.setSummary(summary);
                        fundCompareProduct.setProfile(profile);
                        fundCompareProduct.setRating(rating);
                        fundCompareProduct.setPerformance(performance);
                        fundCompareProduct.setRisk(risk);
                        fundCompareProduct.setPurchaseInfo(purchaseInfo);

                        product.add(fundCompareProduct);
                    }
                }
                response.setProducts(product);
            }
        }
        return response;
    }


    protected Object[] sendFundCompareRequest(final FundCompareRequest request) throws Exception {
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
                        LogUtil.error(FundCompareServiceExecutor.class,
                            "No record found for Product|symbol=" + productKey.toString());
                        throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND, "No record found for "
                            + productKey.toString());
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
            LogUtil.error(FundCompareServiceExecutor.class,
                "sendFundCompareRequest is fail, because there is a invalid productKeys" + request.toString());
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND);
        }
    }

}
