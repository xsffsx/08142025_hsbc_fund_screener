package com.hhhh.group.secwealth.mktdata.quote.service;

import com.google.gson.reflect.TypeToken;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.common.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.common.properties.PredSrchProperties;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.JsonUtil2;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.quote.beans.request.ProductKey;
import com.hhhh.group.secwealth.mktdata.quote.beans.request.QuoteListRequest;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.QuoteListResponse;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.common.PriceQuote;
import com.hhhh.group.secwealth.mktdata.quote.dao.FundQuoteDao;
import org.apache.http.NameValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.Silent.class)
public class QuoteListServiceImplTest {

    @InjectMocks
    QuoteListServiceImpl quoteListService;
    //@InjectMocks
    @Mock
    InternalProductKeyUtil internalProductKeyUtil;

    @Mock
    FundQuoteDao fundQuoteDao;
    @Mock
    PredSrchProperties predSrchProps;
    /*@Mock
    HttpClientHelper httpClientHelper;*/

    String mockResponseString = "{\"externalKey\":\"externalKey\",\"prodCdeAltClassCde\":\"prodCdeAltClassCde\",\"searchObject\":{\"countryTradableCode\":\"countryTradableCode\",\"productType\":\"productType\",\"productName\":\"productName\"}}";

    @Before
    public void init()  throws Exception{
        String key = "countryCode.locale";
        LocaleMappingUtil util = new LocaleMappingUtil();
        Map<String, String> localeMapping = new HashMap<>();
        localeMapping.put(key, "1");
        ReflectionTestUtils.setField(util, "localeMapping", localeMapping);
        ReflectionTestUtils.setField(quoteListService, "localeMappingUtil", util);
        when(predSrchProps.getPredSrchBodyPrefix()).thenReturn("");
        when(predSrchProps.getInternalUrl()).thenReturn("");
        //when(this.localeMappingUtil.getNameByIndex(ArgumentMatchers.<String>any())).thenReturn(1);
        SearchProduct searchProduct = JsonUtil2.fromJson(mockResponseString, new TypeToken<SearchProduct>() {
        }.getType());
        when(internalProductKeyUtil.getProductBySearchWithAltClassCde(ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any()
        ,ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any()))
                .thenReturn(searchProduct);

    }

    @Test
    public void executeTest() throws Exception {
        String countryCode = "countryCode";
        String groupMember = "groupMember";
        String countryTradableCode = "countryTradableCode";
        String locale = "locale";
        boolean delay = true;
        String prodCdeAltClassCde = "prodCdeAltClassCde";
        String productType = "productType";
        String prodAltNum = "prodAltNum";
        QuoteListRequest request = new QuoteListRequest();

        request.addHeader(CommonConstants.REQUEST_HEADER_COUNTRYCODE, countryCode);
        request.addHeader(CommonConstants.REQUEST_HEADER_GROUPMEMBER, groupMember);
        request.addHeader(CommonConstants.REQUEST_HEADER_LOCALE, locale);
        request.setMarket(countryTradableCode);
        request.setDelay(delay);
        List<ProductKey> productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProdCdeAltClassCde(prodCdeAltClassCde);
        productKey.setProductType(productType);
        productKey.setProdAltNum(prodAltNum);
        productKeys.add(productKey);
        request.setProductKeys(productKeys);

        List<UtProdInstm> utProdInstmList = new ArrayList<>();
        UtProdInstm instm = new UtProdInstm();
        instm.setSymbol(prodAltNum);
        instm.setProductType(productType);
        instm.setMarket(countryTradableCode);
        utProdInstmList.add(instm);
        when(fundQuoteDao.search(ArgumentMatchers.<List<InternalProductKey>>any(), anyMap())).thenReturn(utProdInstmList);

        QuoteListResponse response = (QuoteListResponse)quoteListService.execute(request);
        Assert.assertNotNull(response.getPriceQuotes());

    }
}
