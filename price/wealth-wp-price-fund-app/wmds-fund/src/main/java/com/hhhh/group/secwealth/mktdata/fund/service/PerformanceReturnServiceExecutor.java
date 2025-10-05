
package com.hhhh.group.secwealth.mktdata.fund.service;

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
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.performancereturn.Api;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.performancereturn.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.performancereturn.PerformanceReturnData;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.PerformanceReturnRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.PerformanceReturnResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.performancereturn.PerformanceReturn;
import com.hhhh.group.secwealth.mktdata.fund.util.MiscUtil;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;


@Service("performanceReturnServiceExecutor")
public class PerformanceReturnServiceExecutor extends AbstractMstarService {

    private static final String INDICATE_SYMBOL = "symbol";
    private static final String INDICATE_INDEX = "index";

    @Value("#{systemConfig['mstar.conn.url.performanceReturn']}")
    private String url;

    @Value("#{systemConfig['performanceReturn.dataClass']}")
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

        PerformanceReturnRequest request = (PerformanceReturnRequest) object;
        // Invoke Service
        Object[] arr = sendRequest(request);
        if (null == arr || arr.length == 0) {
            return null;
        }
        PerformanceReturnData performanceReturnData = (PerformanceReturnData) arr[0];

        // Prepare Response
        PerformanceReturnResponse response = new PerformanceReturnResponse();
        if (null != performanceReturnData) {
            Map<String, Data> mstarResponseMap = new HashMap<String, Data>();
            List<Data> datas = performanceReturnData.getData();
            if (null != datas && datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    Data data = datas.get(i);
                    if (null != data) {
                        mstarResponseMap.put(data.getId(), data);
                    }
                }
            }
            List<PerformanceReturn> products = new ArrayList<PerformanceReturn>();

            if (request.getIndicate().equals(PerformanceReturnServiceExecutor.INDICATE_SYMBOL)) {
                // set response when search by symbol
                products = setResponseWithSymbol(request.getCurrency(), arr, mstarResponseMap, products);
            } else if (request.getIndicate().equals(PerformanceReturnServiceExecutor.INDICATE_INDEX)) {
                // set response when search by index
                products = setResponseWithIndex(request.getCurrency(), performanceReturnData, products);
            }
            response.setProducts(products);
        }
        return response;
    }

    protected List<PerformanceReturn> setResponseWithSymbol(final String currency, final Object[] arr,
        final Map<String, Data> mstarResponseMap, final List<PerformanceReturn> products) throws Exception {

        if (arr.length == 2) {
            List<SearchProduct> searchProducts = (List<SearchProduct>) arr[1];
            for (SearchProduct product : searchProducts) {
                PerformanceReturn performanceReturn = new PerformanceReturn();
                Data data = mstarResponseMap.get(product.getExternalKey());
                if (null != data) {
                    Api api = data.getApi();
                    if (null != api && data.getId().equalsIgnoreCase(product.getExternalKey())) {
                        performanceReturn.setProduct(product.getSearchObject().getSymbol());
                        performanceReturn.setReturnYTD(MiscUtil.safeBigDecimalValue(api.getDpReturnYTD()));
                        performanceReturn.setReturn1Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear1()));
                        performanceReturn.setReturn2Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear2()));
                        performanceReturn.setReturn3Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear3()));
                        performanceReturn.setReturn4Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear4()));
                        performanceReturn.setReturn5Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear5()));
                        performanceReturn.setReturn6Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear6()));
                        performanceReturn.setReturn7Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear7()));
                        performanceReturn.setReturn8Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear8()));
                        performanceReturn.setReturn9Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear9()));
                        performanceReturn.setReturn10Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear10()));
                        performanceReturn.setLastUpdatedDate(api.getDpDayEndDate());
                        performanceReturn.setCurrency(currency);
                        products.add(performanceReturn);
                    }
                }
            }
        }
        return products;
    }

    protected List<PerformanceReturn> setResponseWithIndex(final String currency,
        final PerformanceReturnData performanceReturnData, final List<PerformanceReturn> products) throws Exception {

        PerformanceReturn performanceReturn = new PerformanceReturn();
        if (null != performanceReturnData) {
            List<Data> data = performanceReturnData.getData();
            if (null != data) {
                for (Data da : data) {
                    Api api = da.getApi();
                    if (null != api) {
                        performanceReturn.setProduct(da.getId());
                        performanceReturn.setReturnYTD(MiscUtil.safeBigDecimalValue(api.getDmpReturnYTD()));
                        performanceReturn.setReturn1Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear1()));
                        performanceReturn.setReturn2Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear2()));
                        performanceReturn.setReturn3Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear3()));
                        performanceReturn.setReturn4Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear4()));
                        performanceReturn.setReturn5Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear5()));
                        performanceReturn.setReturn6Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear6()));
                        performanceReturn.setReturn7Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear7()));
                        performanceReturn.setReturn8Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear8()));
                        performanceReturn.setReturn9Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear9()));
                        performanceReturn.setReturn10Yr(MiscUtil.safeBigDecimalValue(api.getCyrYear10()));
                        performanceReturn.setLastUpdatedDate(api.getDmpDayEndDate());
                        performanceReturn.setCurrency(currency);
                        products.add(performanceReturn);
                    }
                }
            }
        }
        return products;
    }

    
    protected Object[] sendRequest(final PerformanceReturnRequest request) throws Exception {
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
                    if (request.getIndicate().equals(PerformanceReturnServiceExecutor.INDICATE_SYMBOL)) {
                        searchProduct = this.internalProductKeyUtil.getProductBySearchWithAltClassCde(altClassCde, countryCode,
                            groupMember, prodAltNum, countryTradableCode, productType, locale);
                        if (null == searchProduct || null == searchProduct.getExternalKey()) {
                            LogUtil.error(PerformanceReturnServiceExecutor.class, "No record found for Product|symbol="
                                + productKey.toString());
                            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND, "No record found for "
                                + productKey.toString());
                        } else {
                            list.add(searchProduct);
                        }
                        productAltNum.append(searchProduct.getExternalKey());
                    } else if (request.getIndicate().equals(PerformanceReturnServiceExecutor.INDICATE_INDEX)) {
                        productAltNum.append(prodAltNum);
                    }
                    productAltNum.append(CommonConstants.SYMBOL_COMMA);
                }
            }
            productAltNum.delete(productAltNum.length() - 1, productAltNum.length());
            ids = productAltNum.toString();
            Object response = super.sendRequest(MstarConstants.MSTARID, MstarConstants.MSTAR_CURRENCY + request.getCurrency(), ids,
                this.url, this.dataClassJC.createUnmarshaller());

            Object[] arr = {response, list};
            return arr;
        } else {
            LogUtil.error(PerformanceReturnServiceExecutor.class,
                "sendPerformanceReturnRequest is fail, because there is a invalid productKeys" + request.toString());
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND);
        }
    }
}
