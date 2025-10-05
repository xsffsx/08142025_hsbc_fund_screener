package com.hhhh.group.secwealth.mktdata.quote.service;

import com.google.gson.reflect.TypeToken;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.JsonUtil2;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.quote.beans.request.QuoteDetailRequest;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.QuoteDetailResponse;
import com.hhhh.group.secwealth.mktdata.quote.dao.FundQuoteDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class QuoteDetailServiceImplTest {

    @InjectMocks
    QuoteDetailServiceImpl quoteDetailService;

    @Mock
    InternalProductKeyUtil internalProductKeyUtil;

    @Mock
    FundQuoteDao fundQuoteDao;

    String zone = "Asia/Hong_Kong";

    String mockResponseString = "{\"externalKey\":\"externalKey\",\"prodCdeAltClassCde\":\"prodCdeAltClassCde\",\"searchObject\":{\"countryTradableCode\":\"countryTradableCode\",\"productType\":\"productType\",\"productName\":\"productName\"}}";

    @Before
    public void init()  throws Exception{
        String key = "countryCode.locale";
        LocaleMappingUtil util = new LocaleMappingUtil();
        Map<String, String> localeMapping = new HashMap<>();
        localeMapping.put(key, "1");
        ReflectionTestUtils.setField(util, "localeMapping", localeMapping);
        ReflectionTestUtils.setField(quoteDetailService, "localeMappingUtil", util);

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
        QuoteDetailRequest request = new QuoteDetailRequest();
        request.addHeader(CommonConstants.REQUEST_HEADER_COUNTRYCODE, countryCode);
        request.addHeader(CommonConstants.REQUEST_HEADER_GROUPMEMBER, groupMember);
        request.addHeader(CommonConstants.REQUEST_HEADER_LOCALE, locale);
        request.setProdAltNum(prodAltNum);
        request.setMarket(countryTradableCode);
        request.setProductType(productType);
        request.setDelay(delay);
        request.setEntityTimezone(zone);

        List<UtProdInstm> utProdInstmList = new ArrayList<>();
        UtProdInstm instm = new UtProdInstm();
        instm.setSymbol(prodAltNum);
        instm.setProductType(productType);
        instm.setMarket(countryTradableCode);
        utProdInstmList.add(instm);
        when(fundQuoteDao.search(ArgumentMatchers.<List<InternalProductKey>>any(), anyMap())).thenReturn(utProdInstmList);
        QuoteDetailResponse response  = (QuoteDetailResponse)quoteDetailService.execute(request);

        Assert.assertEquals(response.getPriceQuote().getMarket(), countryTradableCode);
        Assert.assertEquals(response.getPriceQuote().getProductType(), productType);
    }
}
