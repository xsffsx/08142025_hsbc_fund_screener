
package com.hhhh.group.secwealth.mktdata.fund.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.*;
import com.hhhh.group.secwealth.mktdata.common.util.*;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.AdvanceChartHelp;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.ResponseData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.AdvanceChartNavData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.HistoryDetail;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.Security;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.AdvanceChartGrowthData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.CumulativeReturnSeries;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.AdvanceChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.AdvanceChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart.Result;
import com.hhhh.group.secwealth.mktdata.fund.dao.AdvanceChartDao;
import com.hhhh.group.secwealth.mktdata.fund.util.EhcacheUtil;
import com.hhhh.group.secwealth.mktdata.fund.util.ThreadPoolExecutorUtil;
import net.sf.json.JSONArray;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Service("advanceChartJobCacheService")
public class AdvanceChartJobCacheServiceImpl extends AbstractMstarService {


    private final static String STARTDATE = "startDate";


    private final static String ENDDATE = "endDate";

    private final static String IDTYPE = "idType";

    private final static String CURRENCYID = "currencyId";

    private final static String FORWARDFILL = "forwardfill";

    private final static String IDTYPEVALUE = "Morningstar";

    private final static String ID = "id";

    private final static String FREQUENCY = "frequency";

    private final static int INDEX_0 = 0;

    private final static int INDEX_2 = 2;

    private final static int INDEX_4 = 4;

    private final static int FORWARD_WEEK = 2;

    private final static String DATATYPE_NAVPRICE = "navPrice";

    private final static String DATATYPE_CUMULATIVERETURN = "cumulativeReturn";

    private final static String DATATYPE_INDEXPRICE = "indexPrice";

    private final static String DATATYPE_INDEXRETURN = "indexReturn";

    private final static String MONTYLY = "monthly";

    private final static String ADVANCECHART_CORE_CACHE = "advanceChartCoreCache";

    private final static String CALLGROWTHANDNAVPRICE = "callGrowthAndNavPrice";

    private final static String CALLGROWTH = "callGrowth";

    private final static String CALLNAVPRICE = "callNavPrice";


    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    @Value("#{systemConfig['request.header.countryCode']}")
    private String countryCode;

    @Value("#{systemConfig['request.header.groupMember']}")
    private String groupMember;

    @Value("#{systemConfig['request.header.locale']}")
    private String locale;

    @Autowired
    @Qualifier("advanceChartDao")
    private AdvanceChartDao advanceChartDao;

    @Value("#{systemConfig['mstar.conn.url.advanceChartNav']}")
    private String navUrl;

    @Value("#{systemConfig['mstar.conn.url.advanceChartGrowth']}")
    private String growthUrl;

    @Value("#{systemConfig['mstar.conn.url.advanceChartToken']}")
    private String mstarToken;

    @Value("#{systemConfig['advanceChart.navDataClass']}")
    private String navDataClass;

    @Value("#{systemConfig['advanceChart.growthDataClass']}")
    private String growthDataClass;

    public EhcacheUtil ehcacheUtil = EhcacheUtil.getInstance("/spring/common/applicationContext-common-ehcache.xml");

    private JAXBContext navDataClassJC;

    private JAXBContext growthDataClassJC;

    @PostConstruct
    public void init() throws Exception {
        this.navDataClassJC = JAXBContext.newInstance(Class.forName(this.navDataClass));
        this.growthDataClassJC = JAXBContext.newInstance(Class.forName(this.growthDataClass));
    }

    private final static String[] cacheRequest = new String[] {
      
        "{\"productKeys\":[{\"market\":\"HK\",\"productType\":\"UT\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50001\"}],\"timeZone\":\"Asia/Hong_Kong\",\"period\":\"5Y\",\"currency\":\"HKD\",\"dataType\":[\"cumulativeReturn\",\"navPrice\"],\"frequency\":\"daily\",\"navForwardFill\":true}",
        "{\"productKeys\":[{\"market\":\"HK\",\"productType\":\"UT\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50001\"}],\"timeZone\":\"Asia/Hong_Kong\",\"period\":\"3Y\",\"currency\":\"HKD\",\"dataType\":[\"cumulativeReturn\",\"navPrice\"],\"frequency\":\"daily\",\"navForwardFill\":true}",
        "{\"productKeys\":[{\"market\":\"HK\",\"productType\":\"UT\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50001\"}],\"timeZone\":\"Asia/Hong_Kong\",\"period\":\"1Y\",\"currency\":\"HKD\",\"dataType\":[\"cumulativeReturn\",\"navPrice\"],\"frequency\":\"daily\",\"navForwardFill\":true}",
        "{\"productKeys\":[{\"market\":\"HK\",\"productType\":\"UT\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50001\"}],\"timeZone\":\"Asia/Hong_Kong\",\"period\":\"6M\",\"currency\":\"HKD\",\"dataType\":[\"cumulativeReturn\",\"navPrice\"],\"frequency\":\"daily\",\"navForwardFill\":true}",
        "{\"productKeys\":[{\"market\":\"HK\",\"productType\":\"UT\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50001\"}],\"timeZone\":\"Asia/Hong_Kong\",\"period\":\"3M\",\"currency\":\"HKD\",\"dataType\":[\"cumulativeReturn\",\"navPrice\"],\"frequency\":\"daily\",\"navForwardFill\":true}",
        "{\"productKeys\":[{\"market\":\"HK\",\"productType\":\"UT\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50011\"}],\"timeZone\":\"Asia/Hong_Kong\",\"period\":\"5Y\",\"currency\":\"HKD\",\"dataType\":[\"cumulativeReturn\",\"navPrice\"],\"frequency\":\"daily\",\"navForwardFill\":true}",
        "{\"productKeys\":[{\"market\":\"HK\",\"productType\":\"UT\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50011\"}],\"timeZone\":\"Asia/Hong_Kong\",\"period\":\"3Y\",\"currency\":\"HKD\",\"dataType\":[\"cumulativeReturn\",\"navPrice\"],\"frequency\":\"daily\",\"navForwardFill\":true}",
        "{\"productKeys\":[{\"market\":\"HK\",\"productType\":\"UT\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50011\"}],\"timeZone\":\"Asia/Hong_Kong\",\"period\":\"1Y\",\"currency\":\"HKD\",\"dataType\":[\"cumulativeReturn\",\"navPrice\"],\"frequency\":\"daily\",\"navForwardFill\":true}",
        "{\"productKeys\":[{\"market\":\"HK\",\"productType\":\"UT\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50011\"}],\"timeZone\":\"Asia/Hong_Kong\",\"period\":\"6M\",\"currency\":\"HKD\",\"dataType\":[\"cumulativeReturn\",\"navPrice\"],\"frequency\":\"daily\",\"navForwardFill\":true}",
        "{\"productKeys\":[{\"market\":\"HK\",\"productType\":\"UT\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50011\"}],\"timeZone\":\"Asia/Hong_Kong\",\"period\":\"3M\",\"currency\":\"HKD\",\"dataType\":[\"cumulativeReturn\",\"navPrice\"],\"frequency\":\"daily\",\"navForwardFill\":true}",
        "{\"productKeys\":[{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50002\",\"market\":\"HK\",\"productType\":\"UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50003\",\"market\":\"HK\",\"productType\":\"UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50004\",\"market\":\"HK\",\"productType\":\"UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50005\",\"market\":\"HK\",\"productType\":\"UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50006\",\"market\":\"HK\",\"productType\":\"UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50007\",\"market\":\"HK\",\"productType\":\"UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50008\",\"market\":\"HK\",\"productType\":\"UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50009\",\"market\":\"HK\",\"productType\":\"UT\"},{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"U50010\",\"market\":\"HK\",\"productType\":\"UT\"}],\"timeZone\":\"Asia/Hong_Kong\",\"period\":\"1Y\",\"currency\":\"HKD\",\"dataType\":[\"indexPrice\",\"indexReturn\"],\"frequency\":\"daily\",\"navForwardFill\":true}"};

    


    public void cacheData() throws Exception {
        LogUtil.debug(AdvanceChartJobCacheServiceImpl.class, "Enter into the AdvanceChartJobCacheServiceImpl");
        String requestString =
            "{\"productKeys\":[{\"market\":\"marketValue\",\"productType\":\"productTypeValue\",\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"prodAltNumValue\"}],\"timeZone\":\"Asia/Hong_Kong\",\"period\":\"periodValue\",\"currency\":\"HKD\",\"dataType\":[\"indexReturn\",\"indexPrice\"],\"frequency\":\"daily\",\"navForwardFill\":true}";
        List<UtProdInstm> utProdInstmList = advanceChartDao.getEnableCacheProdInstmList();
        String[] periods = {"1Y", "3Y", "5Y", "3M", "6M"};
        List<ProductKey> productKeyList = new ArrayList<>();
        for (UtProdInstm utProdInstm : utProdInstmList) {
            ProductKey productKey = new ProductKey();
            productKey.setMarket(utProdInstm.getMarket());
            productKey.setProdAltNum(utProdInstm.getSymbol());
            productKey.setProdCdeAltClassCde("M");
            productKey.setProductType(utProdInstm.getProductType());
            productKeyList.add(productKey);
            String reqString = requestString.replaceAll("marketValue", utProdInstm.getMarket())
                .replaceAll("productTypeValue", utProdInstm.getProductType()).replace("prodAltNumValue", utProdInstm.getSymbol());
            for (String period : periods) {
                String Str = reqString.replaceAll("periodValue", period);
                collectionRequestCallMstar(Str);
            }
        }
        // collection the all index code to ProductKeys
//        if (null != productKeyList && productKeyList.size() < 20) {
//            JSONObject jsonobject = (JSONObject) JSONObject.fromObject(requestString);
//            jsonobject.put("productKeys", productKeyList);
//            jsonobject.put("period", "1Y");
//            collectionRequestCallMstar(jsonobject.toString());
//        }
        if (null != cacheRequest && cacheRequest.length > 0) {
            for (String url : cacheRequest) {
                collectionRequestCallMstar(url);
            }
        }
    }

    public void collectionRequestCallMstar(String requestString) throws Exception {
        AdvanceChartRequest request = new ObjectMapper().readValue(requestString, AdvanceChartRequest.class);
        sortArray(request);
        long requestKey = new SimpleKey(request).hashCode();
        LogUtil.info(AdvanceChartJobCacheServiceImpl.class, "the coreCacheReqeust String is " + requestString + " , "
            + " Simple Key == " + new SimpleKey(request).hashCode());
        calculateTradeTime(request);
        sendAdvanceChartRequest(request, String.valueOf(requestKey));
    }

    public void sortArray(AdvanceChartRequest resquest) {
        Arrays.sort(resquest.getDataType());
        Collections.sort(resquest.getProductKeys(), new Comparator<ProductKey>() {
            @Override
            public int compare(ProductKey o1, ProductKey o2) {
                if ((o1.getProdAltNum()).compareToIgnoreCase(o2.getProdAltNum()) < 0) {
                    return -1;
                }
                return 1;
            }
        });
    }


    public boolean isIndexCode(final AdvanceChartRequest request) {
        List<String> dataTypeList = new ArrayList<>(Arrays.asList(request.getDataType()));
        if ("M".equals(request.getProductKeys().get(0).getProdCdeAltClassCde())
            && (dataTypeList.contains(DATATYPE_INDEXPRICE) || dataTypeList.contains(DATATYPE_INDEXRETURN))) {
            return true;
        }
        return false;
    }

    public Set<String> validateProductKey(List<ProductKey> productKeys) {
        Set<String> numSet = new HashSet<>();
        for (ProductKey productKey : productKeys) {
            numSet.add(productKey.getProdCdeAltClassCde());
        }
        if (numSet.size() != 1) {
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
        return numSet;
    }

    @SuppressWarnings("uncheck")
    protected AdvanceChartResponse sendAdvanceChartRequest(final AdvanceChartRequest request, final String requestKey)
        throws Exception {
        Map<String, Result> resultMap = null;
        Map<String, String> currencyMap = null;
        int index = this.localeMappingUtil.getNameByIndex(countryCode + CommonConstants.SYMBOL_DOT + locale);
        String currency = request.getCurrency();
        List<ProductKey> productKeys = request.getProductKeys();
        Set<String> numSet = validateProductKey(request.getProductKeys());
        AdvanceChartResponse advanceChartResponse = new AdvanceChartResponse();
        String mastrIdString = "";
        if (ListUtil.isValid(productKeys)) {
            List<ProductKey> productKeyMcode = new ArrayList<>();
            List<ProductKey> productKeyZcode = new ArrayList<>();

            // get the Z code productKeys and M code productKeys
            if (numSet.contains("M")) {
                productKeyMcode.addAll(productKeys);
            } else if (numSet.contains("Z")) {
                productKeyZcode.addAll(productKeys);
            }
            resultMap = new HashMap<>();
            currencyMap = new HashMap<>();
            if (ListUtil.isValid(productKeyMcode)) {
                boolean indexCode = isIndexCode(request);
                List<UtProdInstm> utProdInstms = this.advanceChartDao.getUtProdInstmList(productKeyMcode, request.getHeaders());
                if (ListUtil.isValid(utProdInstms)) {
                    for (UtProdInstm utProdInstm : utProdInstms) {
                        String mastrId = "";
                        if (indexCode) {
                            mastrId = utProdInstm.getUndlIndexCde();
                        } else {
                            mastrId = utProdInstm.getmStarID();
                        }
                        mastrIdString =
                            new StringBuilder(mastrIdString).append(mastrId).append(CommonConstants.SYMBOL_VERTICAL).toString();
                        if (null != utProdInstm) {
                            Result result = new Result();
                            result.setCurrency(currency);
                            result.setFrequency(request.getFrequency());
                            resultMap.put(mastrId, result);
                            currencyMap.put(mastrId, utProdInstm.getMstarCcyCde());
                            SearchProduct searchProduct =
                                this.internalProductKeyUtil.getProductBySearchWithAltClassCde("M", countryCode, groupMember,
                                    utProdInstm.getSymbol(), utProdInstm.getMarket(), utProdInstm.getProductType(), locale);
                            if (null != searchProduct && null != searchProduct.getSearchObject()) {
                                List<ProdAltNumSeg> prodAltNumSegs = searchProduct.getSearchObject().getProdAltNumSeg();
                                result.setProdAltNumSegs(prodAltNumSegs);
                            } else {
                                LogUtil.error(AdvanceChartJobCacheServiceImpl.class,
                                    "No record found for symbol=" + utProdInstm.getSymbol() + " market=" + utProdInstm.getMarket());
                                throw new CommonException(ErrTypeConstants.WARNMSG_NORECORDFOUND);
                            }
                            if (indexCode) {
                                if (0 == index) {
                                    result.setIndexName(utProdInstm.getUndlIndexName());
                                } else if (1 == index) {
                                    result.setIndexName(utProdInstm.getUndlIndexPllName());
                                } else if (2 == index) {
                                    result.setIndexName(utProdInstm.getUndlIndexSllName());
                                } else {
                                    result.setIndexName(utProdInstm.getUndlIndexName());
                                }
                            } else {
                                if (0 == index) {
                                    result.setProductName(utProdInstm.getProdName());
                                } else if (1 == index) {
                                    result.setProductName(utProdInstm.getProdPllName());
                                } else if (2 == index) {
                                    result.setProductName(utProdInstm.getProdSllName());
                                } else {
                                    result.setProductName(utProdInstm.getProdName());
                                }
                            }
                        }
                    }

                } else {
                    LogUtil.error(AdvanceChartJobCacheServiceImpl.class,
                        "No record found in DB table v_ut_prod_instm,prodkeys =" + (JSONArray.fromObject(productKeys)).toString());
                    throw new CommonException(ErrTypeConstants.WARNMSG_NORECORDFOUND);
                }
            } else if (ListUtil.isValid(productKeyZcode)) {
                Result result = null;
                for (ProductKey productkey : productKeyZcode) {
                    result = new Result();
                    result.setCurrency(currency);
                    result.setFrequency(request.getFrequency());
                    resultMap.put(productkey.getProdAltNum(), result);
                    List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
                    ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
                    prodAltNumSeg.setProdCdeAltClassCde("Z");
                    prodAltNumSeg.setProdAltNum(productkey.getProdAltNum());
                    prodAltNumSegs.add(prodAltNumSeg);
                    result.setProdAltNumSegs(prodAltNumSegs);
                    mastrIdString = new StringBuilder(mastrIdString).append(productkey.getProdAltNum())
                        .append(CommonConstants.SYMBOL_VERTICAL).toString();
                }
            }
            if (mastrIdString.length() > 0) {
                mastrIdString = mastrIdString.substring(0, mastrIdString.length() - 1);
                getMastarInfo(request, advanceChartResponse, mastrIdString, resultMap, requestKey, currencyMap);
            } else {
                LogUtil.error(AdvanceChartJobCacheServiceImpl.class,
                    "the productKey exist issue = " + (JSONArray.fromObject(productKeys)).toString());
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
            }
        }
        return advanceChartResponse;
    }

    private Map<String, Boolean> checkDataType(final AdvanceChartRequest request) {
        Map<String, Boolean> dataTypeMap = new HashMap<String, Boolean>() {
            {
                put(CALLGROWTHANDNAVPRICE, Boolean.FALSE);
                put(CALLGROWTH, Boolean.FALSE);
                put(CALLNAVPRICE, Boolean.FALSE);
            }
        };
        String dataType[] = request.getDataType();
        if (null != dataType) {
            Set<String> dataTypelist = new HashSet<>(Arrays.asList(dataType));
            if (dataTypelist.contains(DATATYPE_NAVPRICE) && dataTypelist.contains(DATATYPE_CUMULATIVERETURN)) {
                dataTypeMap.put(CALLGROWTHANDNAVPRICE, Boolean.TRUE);
            } else if (dataTypelist.contains(DATATYPE_INDEXPRICE) && dataTypelist.contains(DATATYPE_INDEXRETURN)) {
                dataTypeMap.put(CALLGROWTH, Boolean.TRUE);
            } else if (dataTypelist.contains(DATATYPE_NAVPRICE) || dataTypelist.contains(DATATYPE_INDEXPRICE)) {
                dataTypeMap.put(CALLNAVPRICE, Boolean.TRUE);
            } else if (dataTypelist.contains(DATATYPE_CUMULATIVERETURN) || dataTypelist.contains(DATATYPE_INDEXRETURN)) {
                dataTypeMap.put(CALLGROWTH, Boolean.TRUE);
            } else {
                LogUtil.error(AdvanceChartJobCacheServiceImpl.class, ErrTypeConstants.SOME_INPUT_PARAMETER_INVALID);
                throw new CommonException(ErrTypeConstants.SOME_INPUT_PARAMETER_INVALID);
            }
        }
        return dataTypeMap;
    }

    private void getMastarInfo(final AdvanceChartRequest request, final AdvanceChartResponse response, final String mastrIdString,
        final Map<String, Result> resultMap, final String requestKey, final Map<String, String> currencyMap)
        throws InterruptedException, ExecutionException {
        String mastrIdArray[] = mastrIdString.split(CommonConstants.SYMBOL_SEPARATOR);
        String currency = "";
        for (Entry<String, String> map : currencyMap.entrySet()) {
            currency = map.getValue();
            break;
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(AdvanceChartJobCacheServiceImpl.STARTDATE, request.getStartDate()));
        params.add(new BasicNameValuePair(AdvanceChartJobCacheServiceImpl.ENDDATE, request.getEndDate()));
        params.add(new BasicNameValuePair(AdvanceChartJobCacheServiceImpl.CURRENCYID,
            StringUtil.isValid(currency) ? currency : request.getCurrency()));
        params.add(new BasicNameValuePair(AdvanceChartJobCacheServiceImpl.IDTYPE, AdvanceChartJobCacheServiceImpl.IDTYPEVALUE));
        params.add(new BasicNameValuePair(AdvanceChartJobCacheServiceImpl.ID, mastrIdArray[0]));
        params.add(new BasicNameValuePair(AdvanceChartJobCacheServiceImpl.FREQUENCY, request.getFrequency()));
        if (request.getNavForwardFill()) {
            params.add(new BasicNameValuePair(AdvanceChartJobCacheServiceImpl.FORWARDFILL, "true"));
        } else {
            params.add(new BasicNameValuePair(AdvanceChartJobCacheServiceImpl.FORWARDFILL, "false"));
        }
        String reqNavUrl = new StringBuilder(this.navUrl).append(CommonConstants.SYMBOL_SLASH).append(this.mstarToken).toString();
        String reqGrowthUrl =
            new StringBuilder(this.growthUrl).append(CommonConstants.SYMBOL_SLASH).append(this.mstarToken).toString();

        // create the AdvanceChartHelp List for calling Growth
        List<AdvanceChartHelp> advanceChartHelps = new ArrayList<>();
        if (null != mastrIdArray) {
            for (String str : mastrIdArray) {
                collectAdvanceChartHelpInfo(advanceChartHelps, str, this.growthDataClassJC, params, this.growthUrl, reqGrowthUrl,
                    currencyMap);
            }
        }
        Map<String, Boolean> dataTypeMap = checkDataType(request);
        List<ResponseData> responseDataList = null;
        AdvanceChartNavData advanceChartNavData = null;
        if (dataTypeMap.get(CALLGROWTHANDNAVPRICE)) {
            collectAdvanceChartHelpInfo(advanceChartHelps, mastrIdString, this.navDataClassJC, params, this.navUrl, reqNavUrl,
                null);
            responseDataList = MultithreadCallMstarApi(advanceChartHelps);
        } else if (dataTypeMap.get(CALLNAVPRICE)) {
            advanceChartNavData = getNavPriceData(request, request.getStartDate(), mastrIdString, params, reqNavUrl);
        } else if (dataTypeMap.get(CALLGROWTH)) {
            responseDataList = MultithreadCallMstarApi(advanceChartHelps);
        }

        AdvanceChartGrowthData advanceChartGrowthData = null;
        if (ListUtil.isValid(responseDataList)) {
            List<com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.Security> securitys = new ArrayList<>();
            for (ResponseData responseData : responseDataList) {
                if (responseData instanceof AdvanceChartGrowthData) {
                    AdvanceChartGrowthData futureResponse = (AdvanceChartGrowthData) responseData;
                    if (null != futureResponse && ListUtil.isValid(futureResponse.getSecurity())) {
                        securitys.addAll(futureResponse.getSecurity());
                    }
                } else if (responseData instanceof AdvanceChartNavData) {
                    advanceChartNavData = (AdvanceChartNavData) responseData;
                }
            }
            advanceChartGrowthData = new AdvanceChartGrowthData();
            advanceChartGrowthData.setSecurity(securitys);
        }

        Map<String, List<Data>> dataMap = mergeMstarResponse(advanceChartNavData, advanceChartGrowthData);
        mergeResponse(dataMap, response, request, resultMap, advanceChartNavData, advanceChartGrowthData, requestKey);
    }



    private List<AdvanceChartHelp> collectAdvanceChartHelpInfo(final List<AdvanceChartHelp> advanceChartHelps, final String id,
        final JAXBContext jaxbContext, final List<NameValuePair> params, final String mstarUrl, final String reqUrl,
        final Map<String, String> currencyMap) {
        AdvanceChartHelp advanceChartHelp = new AdvanceChartHelp();
        if (null != currencyMap && StringUtil.isValid(currencyMap.get(id))) {
            params.set(INDEX_2, new BasicNameValuePair(AdvanceChartJobCacheServiceImpl.CURRENCYID, currencyMap.get(id)));
        }
        advanceChartHelp.setId(id);
        advanceChartHelp.setjAXBContext(jaxbContext);
        advanceChartHelp.setParams(params);
        advanceChartHelp.setMstarUrl(mstarUrl);
        advanceChartHelp.setReqUrl(reqUrl);
        advanceChartHelps.add(advanceChartHelp);
        return advanceChartHelps;
    }


    private AdvanceChartNavData getNavPriceData(final AdvanceChartRequest request, String StartDate, final String mastrIdString,
        final List<NameValuePair> params, final String reqNavUrl) {
        if (request.getFrequency().equals(AdvanceChartJobCacheServiceImpl.MONTYLY)) {
            try {
                StartDate = calculateNavStartDate(request, StartDate);
            } catch (ParseException e) {
                LogUtil.error(AdvanceChartJobCacheServiceImpl.class, "StartDate is invalid, newStartDate: " + StartDate);
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
            }
        }
        params.set(AdvanceChartJobCacheServiceImpl.INDEX_0,
            new BasicNameValuePair(AdvanceChartJobCacheServiceImpl.STARTDATE, StartDate));
        params.set(AdvanceChartJobCacheServiceImpl.INDEX_4,
            new BasicNameValuePair(AdvanceChartJobCacheServiceImpl.ID, mastrIdString));
        String reqParams = URLEncodedUtils.format(params, CommonConstants.CODING_UTF8);
        // call nva url
        AdvanceChartNavData advanceChartNavData =
            (AdvanceChartNavData) callMstarRelationApi(reqNavUrl, this.navUrl, reqParams, this.navDataClassJC, AdaptorUtil.convertNameValuePairListToString(params));

        return advanceChartNavData;
    }


    private void mergeResponse(final Map<String, List<Data>> dataMap, final AdvanceChartResponse response,
        final AdvanceChartRequest request, final Map<String, Result> resultMap, final AdvanceChartNavData advanceChartNavData,
        final AdvanceChartGrowthData advanceChartGrowthData, final String requestKey) {
        List<Result> results = new ArrayList<>();
        if (null != dataMap && dataMap.size() > 0) {
            for (Map.Entry<String, List<Data>> entry : dataMap.entrySet()) {
                String id = entry.getKey();
                if (resultMap.containsKey(id)) {
                    Result result = resultMap.get(id);
                    result.setData(entry.getValue());
                    results.add(result);
                }
            }
        }
        response.setResult(results);
        // check if cache logic
        Map<String, Boolean> dataTypeMap = checkDataType(request);
        boolean cacheEnable = Boolean.FALSE;
        List<ProductKey> productKeys = request.getProductKeys();
        if ("Z".equals(productKeys.get(0).getProdCdeAltClassCde()) && null != response.getResult()
            && request.getProductKeys().size() == response.getResult().size()) {
            cacheEnable = true;
        } else if ("M".equals(productKeys.get(0).getProdCdeAltClassCde()) && null != response.getResult()
            && request.getProductKeys().size() == response.getResult().size()) {
            if (dataTypeMap.get(CALLGROWTHANDNAVPRICE)) {
                if (null != advanceChartNavData && null != advanceChartGrowthData
                    && ListUtil.isValid(advanceChartNavData.getSecurity()) && ListUtil.isValid(advanceChartGrowthData.getSecurity())
                    && advanceChartNavData.getSecurity().size() == advanceChartGrowthData.getSecurity().size())
                    if (ListUtil.isValid(advanceChartGrowthData.getSecurity().get(0).getCumulativeReturnSeries())
                        && ListUtil.isValid(advanceChartNavData.getSecurity().get(0).getHistoryDetail())) {
                        cacheEnable = true;
                    }
            } else if (dataTypeMap.get(CALLNAVPRICE)) {
                if (null != advanceChartNavData && ListUtil.isValid(advanceChartNavData.getSecurity())) {
                    cacheEnable = true;
                }
            } else if (dataTypeMap.get(CALLGROWTH)) {
                if (null != advanceChartGrowthData && ListUtil.isValid(advanceChartGrowthData.getSecurity())) {
                    cacheEnable = true;
                }
            }
        }
        LogUtil.info(AdvanceChartJobCacheServiceImpl.class, "enter the core cache from db cacheEnable " + cacheEnable);
        if (cacheEnable) {
            ObjectMapper jb = new ObjectMapper();
            try {
                ehcacheUtil.put(ADVANCECHART_CORE_CACHE, requestKey, jb.writeValueAsString(response), true, 24 * 60 * 60);
                LogUtil.info(AdvanceChartJobCacheServiceImpl.class,
                    " current memroy == " + ehcacheUtil.getMemory(ADVANCECHART_CORE_CACHE) + " , " + ADVANCECHART_CORE_CACHE
                        + " size == " + ehcacheUtil.getcacheSize(ehcacheUtil.get(ADVANCECHART_CORE_CACHE)));
            } catch (JsonProcessingException e) {

                throw new CommonException(ErrTypeConstants.UNDEFINED);
            }
        }
    }

    private Map<String, List<Data>> mergeMstarResponse(final AdvanceChartNavData advanceChartNavData,
        final AdvanceChartGrowthData advanceChartGrowthData) {
        Map<String, List<Data>> dataMap = new HashMap<>();
        if (null != advanceChartGrowthData) {
            if (null != advanceChartGrowthData.getSecurity()) {
                List<com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.Security> securityList =
                    advanceChartGrowthData.getSecurity();
                for (com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.Security securityGrowth : securityList) {
                    String growthId = securityGrowth.getId();
                    List<Data> dataList = new ArrayList<>();
                    for (CumulativeReturnSeries cumulativeReturnSeries : securityGrowth.getCumulativeReturnSeries()) {
                        for (com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.HistoryDetail growthHistoryDetail : cumulativeReturnSeries
                            .getHistoryDetail()) {
                            Data data = new Data();
                            data.setDate(growthHistoryDetail.getEndDate());
                            data.setCumulativeReturn(new BigDecimal(growthHistoryDetail.getValue()));
                            dataList.add(data);
                        }
                    }
                    dataMap.put(growthId, dataList);
                }
            }
        }
        if (null != advanceChartNavData) {
            if (advanceChartNavData.getSecurity() != null) {
                List<Security> securityList = advanceChartNavData.getSecurity();
                for (Security security : securityList) {
                    List<Data> merageData = new ArrayList<>();
                    if (dataMap.containsKey(security.getId())) {
                        List<Data> growthData = dataMap.get(security.getId());
                        for (int i = 0, length = security.getHistoryDetail().size(); i < length; i++) {
                            HistoryDetail historyDetail = security.getHistoryDetail().get(i);
                            for (Data data : growthData) {
                                if (data.getDate().equals(historyDetail.getEndDate())) {
                                    data.setNavPrice(new BigDecimal(historyDetail.getValue()));
                                    break;
                                }
                            }
                        }
                        dataMap.put(security.getId(), growthData);
                    } else if (ListUtil.isValid(security.getHistoryDetail())) {
                        for (int i = 0, length = security.getHistoryDetail().size(); i < length; i++) {
                            HistoryDetail historyDetail = security.getHistoryDetail().get(i);
                            Data data = new Data();
                            data.setDate(historyDetail.getEndDate());
                            data.setNavPrice(new BigDecimal(historyDetail.getValue()));
                            merageData.add(data);
                        }
                        dataMap.put(security.getId(), merageData);
                    }
                }
            }
        }
        return dataMap;
    }


    private ResponseData callMstarRelationApi(final String url, final String baseUrl, final String reqParams,
        final JAXBContext jaxbContext, final String paramsWithoutEncode) {
        ResponseData responseData = null;
        LogUtil.infoOutboundMsg(AdvanceChartJobCacheServiceImpl.class, VendorType.MSTAR, baseUrl, reqParams);
        try {
            String resp = this.connManager.sendRequest(url, reqParams,paramsWithoutEncode);

            // String resp =
            // "<TimeSeries><Security
            // Id=\"F00000Q2LQ\"><CumulativeReturnSeries><HistoryDetail><EndDate>2020-01-06</EndDate><Value>0</Value></HistoryDetail></CumulativeReturnSeries></Security></TimeSeries>";

            LogUtil.infoInboundMsg(AdvanceChartJobCacheServiceImpl.class, VendorType.MSTAR, resp);
            responseData = (ResponseData) jaxbContext.createUnmarshaller().unmarshal(new StringReader(resp));
        } catch (JAXBException e) {
            LogUtil.error(AdvanceChartJobCacheServiceImpl.class, "Mstar MstarUnmarshalFail", e);
            throw new CommonException(MstarExceptionConstants.UNMARSHAL_FAIL, e.getMessage());
        } catch (Exception e) {
            String message = e.getMessage();
            LogUtil.info(AdvanceChartJobCacheServiceImpl.class, "message info = ", message);
            if (null != message && message.toLowerCase().contains("timeout")) {
                LogUtil.error(AdvanceChartJobCacheServiceImpl.class, "Mstar timeout error", e);
                throw new CommonException(MstarExceptionConstants.TIMEOUT, e.getMessage());
            } else if (null != message && message.toLowerCase().contains("nodata")) {
                LogUtil.error(AdvanceChartJobCacheServiceImpl.class, "Mstar no data error", e);
                throw new CommonException(MstarExceptionConstants.NO_DATA, e.getMessage());
            } else {
                LogUtil.error(AdvanceChartJobCacheServiceImpl.class, "Mstar undefined error", e);
                throw new CommonException(MstarExceptionConstants.UNDEFINED, e.getMessage());
            }
        }
        if (null == responseData) {
            LogUtil.error(AdvanceChartJobCacheServiceImpl.class, "Mstar response is null");
            throw new CommonException(MstarExceptionConstants.UNDEFINED, "response is null");
        }
        return responseData;

    }


    private List<ResponseData> MultithreadCallMstarApi(final List<AdvanceChartHelp> advanceChartHelps)
        throws InterruptedException, ExecutionException {
        List<Future<ResponseData>> futureList = new ArrayList<>();
        List<ResponseData> list = new ArrayList<>();
        ThreadPoolExecutor pool = ThreadPoolExecutorUtil.getInstance();
        for (AdvanceChartHelp advanceChartHelp : advanceChartHelps) {
            Future<ResponseData> future = pool.submit(new Callable<ResponseData>() {
                private AdvanceChartHelp advanceChartHelp;

                public Callable<ResponseData> accept(final AdvanceChartHelp advanceChartHelp) {
                    this.advanceChartHelp = advanceChartHelp;
                    return this;
                }

                @Override
                public ResponseData call() throws ExecutionException {
                    List<NameValuePair> paramss = new ArrayList<>();
                    String id = advanceChartHelp.getId();
                    String reqUrl = advanceChartHelp.getReqUrl();
                    String mstarUrl = advanceChartHelp.getMstarUrl();
                    JAXBContext jaxbContext = advanceChartHelp.getjAXBContext();
                    paramss.addAll(advanceChartHelp.getParams());
                    paramss.set(AdvanceChartJobCacheServiceImpl.INDEX_4,
                        new BasicNameValuePair(AdvanceChartJobCacheServiceImpl.ID, id));
                    String reqParams = URLEncodedUtils.format(paramss, CommonConstants.CODING_UTF8);
                    return callMstarRelationApi(reqUrl, mstarUrl, reqParams, jaxbContext, AdaptorUtil.convertNameValuePairListToString(paramss));
                }
            }.accept(advanceChartHelp));
            futureList.add(future);
        }

        for (Future<ResponseData> future : futureList) {
            list.add(future.get());
        }

        return list;
    }



    private String calculateNavStartDate(final AdvanceChartRequest request, final String startDate) throws ParseException {
        String timeZone = request.getTimeZone();
        String oldFormatPattern = DateConstants.DateFormat_yyyyMMdd_withHyphen;
        String newFormatPattern = DateConstants.DateFormat_yyyyMMdd_withHyphen;
        TimeZone oldTimezone = TimeZone.getDefault();
        TimeZone newTimezone = TimeZone.getTimeZone(timeZone);
        SimpleDateFormat format = new SimpleDateFormat(oldFormatPattern);
        Date date = format.parse(startDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.WEDNESDAY, -AdvanceChartJobCacheServiceImpl.FORWARD_WEEK);
        String dateStr = format.format(cal.getTime());
        String newStartDate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone, newTimezone);
        return newStartDate;
    }



    private void calculateTradeTime(final AdvanceChartRequest request) throws Exception {
        String period = request.getPeriod();
        // Set the date depends on the period
        String startdate = request.getStartDate();
        String enddate = request.getEndDate();
        String timeZone = request.getTimeZone();
        SimpleDateFormat df = new SimpleDateFormat(DateConstants.DateFormat_yyyyMMdd_withHyphen);
        Calendar cal = Calendar.getInstance();
        String oldFormatPattern = DateConstants.DateFormat_yyyyMMdd_withHyphen;
        String newFormatPattern = DateConstants.DateFormat_yyyyMMdd_withHyphen;
        TimeZone oldTimezone = TimeZone.getDefault();
        TimeZone newTimezone = TimeZone.getTimeZone(timeZone);
        if (StringUtil.isValid(startdate) && StringUtil.isValid(enddate)) {
            startdate = request.getStartDate();
            enddate = request.getEndDate();
        } else if (StringUtil.isValid(period) && StringUtil.isValid(timeZone)) {
            try {
                enddate = df.format(cal.getTime());
                if (period.equals("YTD")) {
                    cal.set(cal.get(Calendar.YEAR), 1 - 1, 1);
                    String dateStr = df.format(cal.getTime());
                    startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone, newTimezone);
                } else if (period.equals("MAX")) {
                    cal.set(1900, 0, 1);
                    String dateStr = df.format(cal.getTime());
                    startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone, newTimezone);

                } else {
                    String dateType = period.substring(period.length() - 1).toUpperCase();
                    int number = Integer.parseInt(period.substring(0, period.length() - 1));
                    if (CommonConstants.YEAR_PERIOD.equals(dateType)) {
                        cal.add(Calendar.YEAR, -number);
                        // cal.add(Calendar.MONTH);
                    } else if (CommonConstants.MONTH_PERIOD.equals(dateType)) {
                        cal.add(Calendar.MONTH, -number);
                    } else {
                        LogUtil.error(AdvanceChartJobCacheServiceImpl.class, "period is invalid, period: " + period);
                        throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
                    }
                    String dateStr = df.format(cal.getTime());
                    startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone, newTimezone);
                }
            } catch (Exception e) {
                LogUtil.error(AdvanceChartJobCacheServiceImpl.class,
                    "InputParameterInvalid, period: " + period + ", timeZone: " + timeZone, e);
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
            }
        } else {
            timeZone = request.getTimeZone();
            cal.add(Calendar.YEAR, -3);
            // cal.add(Calendar.MONTH, -1);
            String dateStr = df.format(cal.getTime());
            enddate = df.format(cal.getTime());
            startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone, newTimezone);
        }
        LogUtil.info(AdvanceChartJobCacheServiceImpl.class, "startdate: " + startdate + ", enddate" + enddate);
        request.setStartDate(startdate);
        request.setEndDate(enddate);
    }

    @Override
    public Object execute(Object object) throws Exception {

        return null;
    }

}
