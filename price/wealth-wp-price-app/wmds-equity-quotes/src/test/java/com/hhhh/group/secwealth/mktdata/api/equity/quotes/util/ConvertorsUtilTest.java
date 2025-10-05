package com.hhhh.group.secwealth.mktdata.api.equity.quotes.util;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ServiceProductKey;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ConvertorsUtilTest {

    private List<ProductKey> productKeys;

    private Map<String, List<String>> symbolsMap;

    private List<PredSrchResponse> predSrchResponses;
    @Before
    public void init(){
        productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProdAltNum("IBM");
        productKey.setProdCdeAltClassCde("M");
        productKey.setProductType("SEC");
        productKey.setExchange("SHS");
        productKeys.add(0,productKey);
        ProductKey productKey2 = new ProductKey();
        productKey2.setProdAltNum("AAPL");
        productKey2.setProdCdeAltClassCde("M");
        productKey2.setProductType("ALL");
        productKey.setExchange("HKG");
        productKeys.add(1,productKey2);
        symbolsMap = new HashMap<>();
        List<String> stringList = new ArrayList<>();
        stringList.add("IBM");
        symbolsMap.put("SEC",stringList);
        predSrchResponses = new ArrayList<>();
        PredSrchResponse predSrchResponse = new PredSrchResponse();
        predSrchResponse = new PredSrchResponse();
        predSrchResponse.setProductType("SEC");
        predSrchResponse.setCountryTradableCode("CN");
        predSrchResponse.setProductName("hhhh HOLDINGS PLC");
        predSrchResponse.setProductShortName("CSI 300 Index");
        predSrchResponse.setSymbol("IBM");
        predSrchResponse.setProductCode("2");
        predSrchResponse.setMarket("HK");
        predSrchResponse.setExchange("HKG");
        List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
        ProdAltNumSeg prodAltNumSeg1 = new ProdAltNumSeg();
        prodAltNumSeg1.setProdAltNum("CSI300");
        prodAltNumSeg1.setProdCdeAltClassCde("M");
        ProdAltNumSeg prodAltNumSeg2 = new ProdAltNumSeg();
        prodAltNumSeg2.setProdAltNum(".CSI300");
        prodAltNumSeg2.setProdCdeAltClassCde("T");
        ProdAltNumSeg prodAltNumSeg3 = new ProdAltNumSeg();
        prodAltNumSeg3.setProdAltNum("2");
        prodAltNumSeg3.setProdCdeAltClassCde("W");
        prodAltNumSegs.add(prodAltNumSeg1);
        prodAltNumSegs.add(prodAltNumSeg2);
        prodAltNumSegs.add(prodAltNumSeg3);
        predSrchResponse.setProdAltNumSegs(prodAltNumSegs);
        predSrchResponses.add(predSrchResponse);
    }

    @Test
    public void testConstructServiceProductKeys(){
        List<String> symbols = new ArrayList<>();
        symbols.add("IBM");
        List<ServiceProductKey> serviceProductKeyList = ConvertorsUtil.constructServiceProductKeys(symbols,predSrchResponses,"M");
        for (ServiceProductKey serviceProductKey: serviceProductKeyList){
            assertEquals("HKG",serviceProductKey.getExchange());
        }
    }
    @Test
    public void testErrConstructServiceProductKeys(){
        try {
            assertNull(ConvertorsUtil.constructServiceProductKeys(null,predSrchResponses,"M"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void testConstructServiceProductKeysList(){
        List<ServiceProductKey> serviceProductKeyList = ConvertorsUtil.constructServiceProductKeys(productKeys,symbolsMap,predSrchResponses,"M");
        for (ServiceProductKey serviceProductKey : serviceProductKeyList){
            assertEquals("CSI300",serviceProductKey.getProdAltNum());
        }
    }
    @Test
    public void testErrConstructServiceProductKeysList(){
        try {
            assertNull(ConvertorsUtil.constructServiceProductKeys(productKeys,null,predSrchResponses,"M"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void testGetPredsrchSymbol(){
        assertNotNull(ConvertorsUtil.getPredsrchSymbol(productKeys,symbolsMap,predSrchResponses,"M"));
    }

    @Test
    public void testErrGetPredsrchSymbol(){
        try {
            assertNull(ConvertorsUtil.getPredsrchSymbol(productKeys,null,predSrchResponses,"M"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
