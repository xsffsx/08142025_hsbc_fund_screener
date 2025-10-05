package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.convertor.ConvertorUtil;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quoteperformance.Api;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quoteperformance.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quoteperformance.QuotePerformanceData;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.QuotePerformanceRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.QuotePerformanceResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteperformance.MultiTimeChartData;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteperformance.PerformOverMutiTimeList;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteperformance.QuotePerformance;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteperformance.QuotePerformance.PerformanceOverMultipleTimeHorizons;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteperformance.QuotePerformance.RiskAndReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteperformance.QuotePerformance.RiskMeasures;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteperformance.RiskMeasuresList;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;

@Service("quotePerformanceService")
public class QuotePerformanceServiceImpl extends AbstractMstarService {

    private static String performOverMutiTimeConvertorKey = "performOverMutiTimeConvertor_MSTAR";

    private String currencyAlignKey;

    @Value("#{systemConfig['mstar.conn.url.quoteperformance']}")
    private String url;

    @Value("#{systemConfig['quoteperformance.dataClass']}")
    private String dataClass;

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    public void setCurrencyAlignKey(final String currencyAlignKey) {
        this.currencyAlignKey = currencyAlignKey;
    }

    @Override
    protected String getCurrencyAlignKey() {
        return this.currencyAlignKey;
    }

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

        // Prepare request
        QuotePerformanceRequest request = (QuotePerformanceRequest) object;

        // Invoke service
        QuotePerformanceData quotePerformanceData = (QuotePerformanceData) sendRequest(request);

        // Prepase response
        QuotePerformanceResponse response = new QuotePerformanceResponse();
        QuotePerformance performance = new QuotePerformance();

        // Init service object
        PerformanceOverMultipleTimeHorizons performanceOverMultipleTimeHorizons = performance.new PerformanceOverMultipleTimeHorizons();
        List<PerformOverMutiTimeList> performanceItem = new ArrayList<PerformOverMutiTimeList>();
        RiskAndReturn riskAndReturn = performance.new RiskAndReturn();
        RiskMeasures riskMeasures = performance.new RiskMeasures();
        List<RiskMeasuresList> riskItem = new ArrayList<RiskMeasuresList>();
        List<MultiTimeChartData> multiTimeChartDataList = new ArrayList<MultiTimeChartData>();

        // Set value
        // Mandatory in response
        PerformOverMutiTimeList performOverMutiTimeItemFund = new PerformOverMutiTimeList();

        String productType = request.getProductType();
        performOverMutiTimeItemFund.setItemName(StringUtil.toStringAndCheckNull(ConvertorUtil.doConvert(
            QuotePerformanceServiceImpl.performOverMutiTimeConvertorKey, productType)));

        PerformOverMutiTimeList performOverMutiTimeItemCate = new PerformOverMutiTimeList();
        performOverMutiTimeItemCate.setItemName(MstarConstants.CATEGORY);
        PerformOverMutiTimeList performOverMutiTimeItemQuar = new PerformOverMutiTimeList();
        performOverMutiTimeItemQuar.setItemName(MstarConstants.QUARTILE);
        PerformOverMutiTimeList performOverMutiTimeItemTotal = new PerformOverMutiTimeList();
        performOverMutiTimeItemTotal.setItemName(MstarConstants.TOTAL);
        RiskMeasuresList riskMeasuresItemBeta = new RiskMeasuresList();
        riskMeasuresItemBeta.setItemName(MstarConstants.BETA);
        RiskMeasuresList riskMeasuresItemStand = new RiskMeasuresList();
        riskMeasuresItemStand.setItemName(MstarConstants.STANDARD_DEVIATION);
        RiskMeasuresList riskMeasuresItemAlpha = new RiskMeasuresList();
        riskMeasuresItemAlpha.setItemName(MstarConstants.ALPHA);
        RiskMeasuresList riskMeasuresItemSharpe = new RiskMeasuresList();
        riskMeasuresItemSharpe.setItemName(MstarConstants.SHARPE_RATIO);
        RiskMeasuresList riskMeasuresItemRS = new RiskMeasuresList();
        riskMeasuresItemRS.setItemName(MstarConstants.R_SQUARED);

        // Set value from api
        if (null != quotePerformanceData) {
            Data data = quotePerformanceData.getData();
            if (null != data) {
                Api api = data.getApi();
                if (null != api) {
                    // performanceOverMultipleTimeHorizons - FUND - Fund
                    performOverMutiTimeItemFund.setReturn1Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn1Mth()));
                    performOverMutiTimeItemFund.setReturn3Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn3Mth()));
                    performOverMutiTimeItemFund.setReturn6Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn6Mth()));
                    performOverMutiTimeItemFund.setReturn1Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn1Yr()));
                    performOverMutiTimeItemFund.setReturn3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn3Yr()));
                    performOverMutiTimeItemFund.setReturn5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRReturn5Yr()));
                    // performanceOverMultipleTimeHorizons - CAT - Category
                    performOverMutiTimeItemCate
                        .setReturn1Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTTRCategoryReturn1Mth()));
                    performOverMutiTimeItemCate
                        .setReturn3Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTTRCategoryReturn3Mth()));
                    performOverMutiTimeItemCate
                        .setReturn6Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTTRCategoryReturn6Mth()));
                    performOverMutiTimeItemCate.setReturn1Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRCategoryReturn1Yr()));
                    performOverMutiTimeItemCate.setReturn3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRCategoryReturn3Yr()));
                    performOverMutiTimeItemCate.setReturn5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRCategoryReturn5Yr()));
                    // performanceOverMultipleTimeHorizons - QTL - Quartile
                    performOverMutiTimeItemQuar.setReturn1Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTtrrRank1MthQuartile()));
                    performOverMutiTimeItemQuar.setReturn3Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTtrrRank3MthQuartile()));
                    performOverMutiTimeItemQuar.setReturn6Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTtrrRank6MthQuartile()));
                    performOverMutiTimeItemQuar.setReturn1Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTtrrRank1YrQuartile()));
                    performOverMutiTimeItemQuar.setReturn3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTtrrRank3YrQuartile()));
                    performOverMutiTimeItemQuar.setReturn5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTtrrRank5YrQuartile()));
                    // performanceOverMultipleTimeHorizons - TTL- Total of
                    // Funds
                    performOverMutiTimeItemTotal
                        .setReturn1Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRCategorySize1Mth()));
                    performOverMutiTimeItemTotal
                        .setReturn3Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRCategorySize3Mth()));
                    performOverMutiTimeItemTotal
                        .setReturn6Mth(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRCategorySize6Mth()));
                    performOverMutiTimeItemTotal.setReturn1Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRCategorySize1Yr()));
                    performOverMutiTimeItemTotal.setReturn3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRCategorySize3Yr()));
                    performOverMutiTimeItemTotal.setReturn5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRCategorySize5Yr()));

                    performanceOverMultipleTimeHorizons.setLastUpdatedDate(api.getTTRRMonthEndDate());
                    riskAndReturn.setRiskRating3Yr(api.getMRRisk3Year());
                    riskAndReturn.setReturnRating3Yr(api.getMRReturn3Year());

                    riskAndReturn.setLastUpdatedDate(api.getMRRatingDate());
                    //
                    // RiskMeasures - BETA - Beta
                    riskMeasuresItemBeta.setRiskMeasures1Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACBeta1Yr()));
                    riskMeasuresItemBeta.setRiskMeasures3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACBeta3Yr()));
                    riskMeasuresItemBeta.setRiskMeasures5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACBeta5Yr()));
                    riskMeasuresItemBeta.setRiskMeasures10Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACBeta10Yr()));
                    riskMeasuresItemBeta.setRiskMeasuresCategoryAvg1Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMBACCategoryBeta1Yr()));
                    riskMeasuresItemBeta.setRiskMeasuresCategoryAvg3Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMBACCategoryBeta3Yr()));
                    riskMeasuresItemBeta.setRiskMeasuresCategoryAvg5Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMBACCategoryBeta5Yr()));
                    riskMeasuresItemBeta.setRiskMeasuresCategoryAvg10Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMBACCategoryBeta10Yr()));
                    // RiskMeasures - STD_DVIAT - Standard Deviation
                    riskMeasuresItemStand.setRiskMeasures1Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMStdDev1Yr()));
                    riskMeasuresItemStand.setRiskMeasures3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMStdDev3Yr()));
                    riskMeasuresItemStand.setRiskMeasures5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMStdDev5Yr()));
                    riskMeasuresItemStand.setRiskMeasures10Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMStdDev10Yr()));
                    riskMeasuresItemStand.setRiskMeasuresCategoryAvg1Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMCategoryStdDev1Yr()));
                    riskMeasuresItemStand.setRiskMeasuresCategoryAvg3Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMCategoryStdDev3Yr()));
                    riskMeasuresItemStand.setRiskMeasuresCategoryAvg5Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMCategoryStdDev5Yr()));
                    riskMeasuresItemStand.setRiskMeasuresCategoryAvg10Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMCategoryStdDev10Yr()));
                    // RiskMeasures - ALPHA - Alpha
                    riskMeasuresItemAlpha.setRiskMeasures1Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACAlpha1Yr()));
                    riskMeasuresItemAlpha.setRiskMeasures3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACAlpha3Yr()));
                    riskMeasuresItemAlpha.setRiskMeasures5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACAlpha5Yr()));
                    riskMeasuresItemAlpha.setRiskMeasures10Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACAlpha10Yr()));
                    riskMeasuresItemAlpha.setRiskMeasuresCategoryAvg1Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMBACCategoryAlpha1Yr()));
                    riskMeasuresItemAlpha.setRiskMeasuresCategoryAvg3Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMBACCategoryAlpha3Yr()));
                    riskMeasuresItemAlpha.setRiskMeasuresCategoryAvg5Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMBACCategoryAlpha5Yr()));
                    riskMeasuresItemAlpha.setRiskMeasuresCategoryAvg10Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMBACCategoryAlpha10Yr()));
                    // RiskMeasures - SHRP_RATIO - Sharpe Ratio
                    riskMeasuresItemSharpe.setRiskMeasures1Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMSharpeRatio1Yr()));
                    riskMeasuresItemSharpe.setRiskMeasures3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMSharpeRatio3Yr()));
                    riskMeasuresItemSharpe.setRiskMeasures5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMSharpeRatio5Yr()));
                    riskMeasuresItemSharpe.setRiskMeasures10Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMSharpeRatio10Yr()));
                    riskMeasuresItemSharpe.setRiskMeasuresCategoryAvg1Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMCategorySharpeRatio1Yr()));
                    riskMeasuresItemSharpe.setRiskMeasuresCategoryAvg3Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMCategorySharpeRatio3Yr()));
                    riskMeasuresItemSharpe.setRiskMeasuresCategoryAvg5Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMCategorySharpeRatio5Yr()));
                    riskMeasuresItemSharpe.setRiskMeasuresCategoryAvg10Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMCategorySharpeRatio10Yr()));
                    // RiskMeasures - R_SQUARED - R-Squared
                    riskMeasuresItemRS.setRiskMeasures1Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACRsquared1Yr()));
                    riskMeasuresItemRS.setRiskMeasures3Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACRsquared3Yr()));
                    riskMeasuresItemRS.setRiskMeasures5Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACRsquared5Yr()));
                    riskMeasuresItemRS.setRiskMeasures10Yr(BigDecimalUtil.fromStringAndCheckNull(api.getRMBACRsquared10Yr()));
                    riskMeasuresItemRS.setRiskMeasuresCategoryAvg1Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMBACCategoryRsquared1Yr()));
                    riskMeasuresItemRS.setRiskMeasuresCategoryAvg3Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMBACCategoryRsquared3Yr()));
                    riskMeasuresItemRS.setRiskMeasuresCategoryAvg5Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMBACCategoryRsquared5Yr()));
                    riskMeasuresItemRS.setRiskMeasuresCategoryAvg10Yr(BigDecimalUtil.fromStringAndCheckNull(api
                        .getRMBACCategoryRsquared10Yr()));

                    riskMeasures.setLastUpdatedDate(api.getRmEndDate());

                    // Set chart data
                    MultiTimeChartData multiTimeChartData1M = new MultiTimeChartData();
                    multiTimeChartData1M.setMultipleTimePeriod("1M");
                    multiTimeChartData1M.setQ0(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank1MthQuartileBreakpoint1()));
                    multiTimeChartData1M.setQ25(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank1MthQuartileBreakpoint25()));
                    multiTimeChartData1M.setQ50(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank1MthQuartileBreakpoint50()));
                    multiTimeChartData1M.setQ75(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank1MthQuartileBreakpoint75()));
                    multiTimeChartData1M.setQ100(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank1MthQuartileBreakpoint99()));
                    MultiTimeChartData multiTimeChartData3M = new MultiTimeChartData();
                    multiTimeChartData3M.setMultipleTimePeriod("3M");
                    multiTimeChartData3M.setQ0(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank3MthQuartileBreakpoint1()));
                    multiTimeChartData3M.setQ25(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank3MthQuartileBreakpoint25()));
                    multiTimeChartData3M.setQ50(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank3MthQuartileBreakpoint50()));
                    multiTimeChartData3M.setQ75(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank3MthQuartileBreakpoint75()));
                    multiTimeChartData3M.setQ100(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank3MthQuartileBreakpoint99()));
                    MultiTimeChartData multiTimeChartData6M = new MultiTimeChartData();
                    multiTimeChartData6M.setMultipleTimePeriod("6M");
                    multiTimeChartData6M.setQ0(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank6MthQuartileBreakpoint1()));
                    multiTimeChartData6M.setQ25(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank6MthQuartileBreakpoint25()));
                    multiTimeChartData6M.setQ50(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank6MthQuartileBreakpoint50()));
                    multiTimeChartData6M.setQ75(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank6MthQuartileBreakpoint75()));
                    multiTimeChartData6M.setQ100(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank6MthQuartileBreakpoint99()));
                    MultiTimeChartData multiTimeChartData1Y = new MultiTimeChartData();
                    multiTimeChartData1Y.setMultipleTimePeriod("1Y");
                    multiTimeChartData1Y.setQ0(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank1YrQuartileBreakpoint1()));
                    multiTimeChartData1Y.setQ25(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank1YrQuartileBreakpoint25()));
                    multiTimeChartData1Y.setQ50(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank1YrQuartileBreakpoint50()));
                    multiTimeChartData1Y.setQ75(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank1YrQuartileBreakpoint75()));
                    multiTimeChartData1Y.setQ100(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank1YrQuartileBreakpoint99()));
                    MultiTimeChartData multiTimeChartData3Y = new MultiTimeChartData();
                    multiTimeChartData3Y.setMultipleTimePeriod("3Y");
                    multiTimeChartData3Y.setQ0(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank3YrQuartileBreakpoint1()));
                    multiTimeChartData3Y.setQ25(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank3YrQuartileBreakpoint25()));
                    multiTimeChartData3Y.setQ50(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank3YrQuartileBreakpoint50()));
                    multiTimeChartData3Y.setQ75(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank3YrQuartileBreakpoint75()));
                    multiTimeChartData3Y.setQ100(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank3YrQuartileBreakpoint99()));
                    MultiTimeChartData multiTimeChartData5Y = new MultiTimeChartData();
                    multiTimeChartData5Y.setMultipleTimePeriod("5Y");
                    multiTimeChartData5Y.setQ0(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank5YrQuartileBreakpoint1()));
                    multiTimeChartData5Y.setQ25(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank5YrQuartileBreakpoint25()));
                    multiTimeChartData5Y.setQ50(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank5YrQuartileBreakpoint50()));
                    multiTimeChartData5Y.setQ75(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank5YrQuartileBreakpoint75()));
                    multiTimeChartData5Y.setQ100(BigDecimalUtil.fromStringAndCheckNull(api.getTTRRRank5YrQuartileBreakpoint99()));
                    if (null != performOverMutiTimeItemFund) {
                        multiTimeChartData1M.setDiv(performOverMutiTimeItemFund.getReturn1Mth());
                        multiTimeChartData3M.setDiv(performOverMutiTimeItemFund.getReturn3Mth());
                        multiTimeChartData6M.setDiv(performOverMutiTimeItemFund.getReturn6Mth());
                        multiTimeChartData1Y.setDiv(performOverMutiTimeItemFund.getReturn1Yr());
                        multiTimeChartData3Y.setDiv(performOverMutiTimeItemFund.getReturn3Yr());
                        multiTimeChartData5Y.setDiv(performOverMutiTimeItemFund.getReturn5Yr());
                    }
                    multiTimeChartDataList.add(multiTimeChartData1M);
                    multiTimeChartDataList.add(multiTimeChartData3M);
                    multiTimeChartDataList.add(multiTimeChartData6M);
                    multiTimeChartDataList.add(multiTimeChartData1Y);
                    multiTimeChartDataList.add(multiTimeChartData3Y);
                    multiTimeChartDataList.add(multiTimeChartData5Y);
                }
                performanceItem.add(performOverMutiTimeItemFund);
                performanceItem.add(performOverMutiTimeItemCate);
                performanceItem.add(performOverMutiTimeItemQuar);
                performanceItem.add(performOverMutiTimeItemTotal);
                performanceOverMultipleTimeHorizons.setItems(performanceItem);
                riskMeasures.setItems(riskItem);
                riskItem.add(riskMeasuresItemBeta);
                riskItem.add(riskMeasuresItemStand);
                riskItem.add(riskMeasuresItemAlpha);
                riskItem.add(riskMeasuresItemSharpe);
                riskItem.add(riskMeasuresItemRS);
                riskMeasures.setItems(riskItem);
                performance.setPerformanceOverMultipleTimeHorizons(performanceOverMultipleTimeHorizons);
                performance.setRiskAndReturn(riskAndReturn);
                performance.setRiskMeasures(riskMeasures);
            }
        }
        response.setPerformance(performance);
        response.setMultiTimeChartDatas(multiTimeChartDataList);
        return response;
    }

    protected Object sendRequest(final QuotePerformanceRequest request) throws Exception {
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
            String msg = "No record found for Mstar|ProdAltNum=" + request.getProdAltNum();
            LogUtil.error(AbstractMstarService.class, msg);
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND, msg);
        }
        String id = searchProduct.getExternalKey();
        return super.sendRequest(MstarConstants.MSTARID, id, this.url, this.dataClassJC.createUnmarshaller());
    }

}
