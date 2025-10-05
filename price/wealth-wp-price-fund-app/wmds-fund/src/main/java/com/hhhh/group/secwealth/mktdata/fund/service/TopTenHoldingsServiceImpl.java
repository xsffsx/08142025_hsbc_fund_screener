
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.util.CountryCodeConvertor;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.newapi.DataSet;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.toptenholdings.Api;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.toptenholdings.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.toptenholdings.HoldingDetail;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.toptenholdings.T10HHoldings;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.toptenholdings.TopTenHoldingsData;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.TopTenHoldingsRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.TopTenHoldingsResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.toptenholdings.TopTenHoldings;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.toptenholdings.TopTenHoldingsItem;
import com.hhhh.group.secwealth.mktdata.fund.util.MiscUtil;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;

@Service("topTenHoldingsService")
public class TopTenHoldingsServiceImpl extends AbstractMstarService {

    public final static String PRODCDEALTCLASSCDE = "M";

    private final String SITE_FEATURE_MSTAR_MSTARNEWAPI = ".mstar.mstarnewapi";
    private final String SITE_FEATURE_MSTAR_MSTARNEWAPI_SITETYPE = ".mstar.mstarnewapi.sitetype";

    @Resource(name = "topTenHoldingDataPointNewapiMap")
    private Map<String, String> topTenHoldingDataPointNewapiMap;

    public ConcurrentHashMap<String, String> countryCodeMap = CountryCodeConvertor.getInstance().getCountryCodeMap();

    @Value("#{systemConfig['mstar.conn.url.toptenholdings']}")
    private String url;

    @Value("#{systemConfig['mstar.conn.url.mstarnewapi']}")
    private String urlApi;

    @Value("#{systemConfig['mstar.conn.url.mstarnewapi.glcm']}")
    private String urlApiGlcm;

    @Value("#{systemConfig['toptenholdings.dataClass']}")
    private String dataClass;

    @Value("#{systemConfig['toptenholdings.dataSetClass']}")
    private String dataSetClass;

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    @Autowired
    @Qualifier("siteFeature")
    private SiteFeature siteFeature;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    private JAXBContext dataClassJC;

    private JAXBContext dataSetClassJC;

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
        // Prepare request
        TopTenHoldingsRequest request = (TopTenHoldingsRequest) object;

        SearchProduct searchproduct = this.getSearchProduct(request);
        SearchableObject searchObject = searchproduct.getSearchObject();

        DataSet topTenHoldingsDataSet = null;
        TopTenHoldingsData topTenHoldingsData = null;
        int index =
            this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + request.getLocale());
        String site = request.getSiteKey();
        String prodType = request.getProductType();
        String prodSubType = searchObject.getProductSubType();

        // Invoke service
        if (this.siteFeature.getBooleanFeature(site, this.SITE_FEATURE_MSTAR_MSTARNEWAPI, false)
            && needGetDatapointNewApi(site, index, prodType, prodSubType)) {
            LogUtil.debug(TopTenHoldingsServiceImpl.class, "Call China morningstar data center API");
            // Local UT DataSet from new Api
            topTenHoldingsDataSet = (DataSet) sendRequestToNewApi(request);
        } else {
            topTenHoldingsData = (TopTenHoldingsData) sendRequest(request);
        }

        // Prepare response
        TopTenHoldingsResponse response = new TopTenHoldingsResponse();
        TopTenHoldings topTenHoldings = new TopTenHoldings();
        List<TopTenHoldingsItem> item = new ArrayList<TopTenHoldingsItem>();

        // Mandatory field in the response
        if (null != topTenHoldingsData) {
            Data data = topTenHoldingsData.getData();
            if (null != data) {
                Api api = data.getApi();
                if (null != api) {
                    T10HHoldings t10HHoldings = api.getT10HHoldings();
                    String currency = api.getPsPortfolioCurrencyId();
                    if (null != t10HHoldings) {
                        if (null != t10HHoldings.getHoldingDetail()) {
                            for (int i = 0; i < t10HHoldings.getHoldingDetail().size(); i++) {
                                // Init the service object
                                TopTenHoldingsItem topTenHoldingsItem = new TopTenHoldingsItem();
                                HoldingDetail holdingDetail = new HoldingDetail();
                                // Set the value
                                holdingDetail = t10HHoldings.getHoldingDetail().get(i);
                                topTenHoldingsItem.setProductType(holdingDetail.getHoldingType());
                                if (null != holdingDetail.getCountryId()) {
                                    topTenHoldingsItem.setMarket(this.countryCodeMap.get(holdingDetail.getCountryId()));
                                }
                                topTenHoldingsItem.setProdCdeAltClassCde(TopTenHoldingsServiceImpl.PRODCDEALTCLASSCDE);
                                topTenHoldingsItem.setSecurityName(holdingDetail.getName());
                                topTenHoldingsItem.setWeighting(MiscUtil.safeBigDecimalValue(holdingDetail.getWeighting()));
                                topTenHoldingsItem.setCurrency(currency);
                                topTenHoldingsItem.setMarketValue(MiscUtil.safeBigDecimalValue(holdingDetail.getMarketValue()));
                                // Fund & ETF case
                                if (MstarConstants.PRODUCTTYPE_UT.equals(request.getProductType())) {
                                    topTenHoldingsItem.setProdAltNum(null);
                                }
                                if (MstarConstants.PRODUCTTYPE_ETF.equals(request.getProductType())) {
                                    topTenHoldingsItem.setProdAltNum(holdingDetail.getTicker());
                                }
                                item.add(topTenHoldingsItem);
                            }
                        }
                    }
                    topTenHoldings.setLastUpdatedDate(api.getPSRPPortfolioDate());
                }
            }
        }

        // Local UT DataSet from new Api
        if (null != topTenHoldingsDataSet) {
            DataSet.QuickTake dataset = topTenHoldingsDataSet.getQuickTake();
            if (null != dataset) {
                List<DataSet.QuickTake.TopHolding> topHoldings = dataset.getTopHolding();
                if (null != topHoldings) {
                    for (int i = 0; i < topHoldings.size(); i++) {
                        TopTenHoldingsItem topTenHoldingsItem = new TopTenHoldingsItem();
                        DataSet.QuickTake.TopHolding topHolding = new DataSet.QuickTake.TopHolding();
                        topHolding = topHoldings.get(i);
                        // Set the value
                        topTenHoldingsItem.setMarket(searchObject.getMarket());
                        topTenHoldingsItem.setProductType(searchObject.getProductType());
                        topTenHoldingsItem.setProdCdeAltClassCde(TopTenHoldingsServiceImpl.PRODCDEALTCLASSCDE);
                        if (1 == index) {
                            topTenHoldingsItem.setSecurityName(topHolding.getHoldingName());
                        } else {
                            topTenHoldingsItem.setSecurityName(topHolding.getHoldingNameEn());
                        }
                        topTenHoldingsItem.setWeighting(MiscUtil.safeBigDecimalValue(topHolding.getNetAssetPercentage()));
                        topTenHoldingsItem.setCurrency("CNY");
                        topTenHoldingsItem.setMarketValue(MiscUtil.safeBigDecimalValue(topHolding.getMarketValue()));
                        // Fund & ETF case
                        if (MstarConstants.PRODUCTTYPE_UT.equals(request.getProductType())) {
                            topTenHoldingsItem.setProdAltNum(MiscUtil.safeString(topHolding.getFundCode()));
                        }
                        if (MstarConstants.PRODUCTTYPE_ETF.equals(request.getProductType())) {
                            topTenHoldingsItem.setProdAltNum(MiscUtil.safeString(topHolding.getFundCode()));
                        }
                        item.add(topTenHoldingsItem);
                    }
                }

                List<DataSet.QuickTake.BondHolding> bondHoldings = dataset.getBondHolding();
                if (null != bondHoldings) {
                    for (int j = 0; j < bondHoldings.size(); j++) {
                        TopTenHoldingsItem topTenHoldingsItem = new TopTenHoldingsItem();
                        DataSet.QuickTake.BondHolding bondHolding = new DataSet.QuickTake.BondHolding();
                        bondHolding = bondHoldings.get(j);
                        // Set the value
                        topTenHoldingsItem.setMarket(searchObject.getMarket());
                        topTenHoldingsItem.setProductType(searchObject.getProductType());
                        topTenHoldingsItem.setProdCdeAltClassCde(TopTenHoldingsServiceImpl.PRODCDEALTCLASSCDE);
                        if (1 == index) {
                            topTenHoldingsItem.setSecurityName(bondHolding.getHoldingName());
                        } else {
                            topTenHoldingsItem.setSecurityName(bondHolding.getHoldingNameEn());
                        }
                        topTenHoldingsItem.setWeighting(MiscUtil.safeBigDecimalValue(bondHolding.getNetAssetPercentage()));
                        topTenHoldingsItem.setCurrency("CNY");
                        topTenHoldingsItem.setMarketValue(MiscUtil.safeBigDecimalValue(bondHolding.getMarketValue()));
                        // Fund & ETF case
                        if (MstarConstants.PRODUCTTYPE_UT.equals(request.getProductType())) {
                            topTenHoldingsItem.setProdAltNum(MiscUtil.safeString(bondHolding.getFundCode()));
                        }
                        if (MstarConstants.PRODUCTTYPE_ETF.equals(request.getProductType())) {
                            topTenHoldingsItem.setProdAltNum(MiscUtil.safeString(bondHolding.getFundCode()));
                        }
                        item.add(topTenHoldingsItem);
                    }
                }

                sortItem(item);

                // just return ten records
                item = item.subList(0, item.size() > 10 ? 10 : item.size());

                DataSet.QuickTake.PortfolioSummary portfolioSummary = dataset.getPortfolioSummary();
                if (portfolioSummary != null) {
                    String lastUpdatedDate = portfolioSummary.getEffectiveDate();
                    if (StringUtil.isValid(lastUpdatedDate) && lastUpdatedDate.contains("T")) {
                        lastUpdatedDate = lastUpdatedDate.substring(0, lastUpdatedDate.indexOf("T"));
                    }
                    topTenHoldings.setLastUpdatedDate(lastUpdatedDate);
                }
            }
        }

        topTenHoldings.setItems(item);
        response.setTop10Holdings(topTenHoldings);

        return response;
    }

    protected Object sendRequest(final TopTenHoldingsRequest request) throws Exception {
        SearchProduct searchProduct = this.getSearchProduct(request);
        String id = searchProduct.getExternalKey();
        return super.sendRequest(MstarConstants.MSTARID, id, this.url, this.dataClassJC.createUnmarshaller());
    }

    
    protected Object sendRequestToNewApi(final TopTenHoldingsRequest request) throws Exception {
        String site = request.getSiteKey();
        String prodAltNum = request.getProdAltNum();
        String siteType = this.siteFeature.getStringFeature(site, this.SITE_FEATURE_MSTAR_MSTARNEWAPI_SITETYPE);

        if (StringUtil.isValid(site) && CommonConstants.ENTITY_CN_GLCM.equalsIgnoreCase(site)) {
            return super.sendRequest2NewApi(MstarConstants.MSTARID, prodAltNum, this.urlApiGlcm,
                this.dataSetClassJC.createUnmarshaller(), siteType);
        } else {
            return super.sendRequest2NewApi(MstarConstants.MSTARID, prodAltNum, this.urlApi,
                this.dataSetClassJC.createUnmarshaller(), siteType);
        }
    }

    public SearchProduct getSearchProduct(final TopTenHoldingsRequest request) throws Exception {
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
            LogUtil.error(AbstractMstarService.class, "No record found for Mstar|ProdAltNum={}", prodAltNum);
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND, "No record found for " + prodAltNum);
        }
        return searchProduct;
    }

    private boolean needGetDatapointNewApi(final String site, final int index, final String prodType, final String prodSubType)
        throws Exception {
        String strCoundition = this.topTenHoldingDataPointNewapiMap.get(site);
        boolean flag = false;
        if (StringUtil.isValid(strCoundition) && StringUtil.isValid(prodType) && StringUtil.isValid(prodSubType)) {
            if (strCoundition.contains(String.valueOf(index)) && strCoundition.contains(prodType)
                && strCoundition.contains(prodSubType)) {
                flag = true;
            } else if (CommonConstants.ALL.equals(strCoundition)) {
                flag = true;
            }
        }
        return flag;
    }

    private static void sortItem(final List<TopTenHoldingsItem> item) {

        Collections.sort(item, new Comparator<TopTenHoldingsItem>() {

            @Override
            public int compare(final TopTenHoldingsItem item1, final TopTenHoldingsItem item2) {

                if (item1 == null || item1.getWeighting() == null) {
                    return 1;
                } else if (item2 == null || item2.getWeighting() == null) {
                    return -1;
                } else {
                    return item2.getWeighting().compareTo(item1.getWeighting());
                }
            }
        });
    }

}
