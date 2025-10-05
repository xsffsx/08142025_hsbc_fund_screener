package com.hhhh.group.secwealth.mktdata.api.equity.quotes.util;

import static org.junit.Assert.assertNotNull;

import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Test;

import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;

import com.google.gson.JsonObject;

public class TrisPropsUtilTest {

    @Test
    public void testInOrderStrProps(){
        String key = "SITE|PRODUCT|EXCHANGE";
        String field = "id";
        Map<String, Map<String, String[]>> trisFieldMapper = new HashMap<>();
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER, trisFieldMapper);
        Map<String, String[]> mapper = new HashMap<>();
        trisFieldMapper.put(key, mapper);
        mapper.put(field, new String[] {"A"});
        mapper.put(field, new String[] {"A", "B"});
        mapper.put(field, new String[] {"A", "B", "C"});
        String httpResult = "{\"id\":\"326416\",\"headline\":\"TENCENT Becomes One of Top 10 Listed Companies Worldwide\",\"content\":\"Bloomberg News reported that with a market cap of US$279 billion, TENCENT (00700.HK) beat Wells Fargo to become one of the top ten listed companies in the world in terms of market value.<Br><Br>Of the top ten companies, six are IT companies and the top four are all IT companies. Apple Inc. is the company with the highest market value in the world, followed by Google's parent company Alphabet (2nd), Microsoft (3rd), Amazon (4th) and Facebook (6th).\",\"isTableFormat\":false,\"err_code\":\"2\",\"err_desc\":\"MDSEQTY50402\"}";
        JsonObject respJsonObj = JsonUtil.getAsJsonObject(httpResult);
        assertNotNull(TrisPropsUtil.inOrderStrProps("id",key,respJsonObj));
    }

    @Test
    public void testInOrderStrProps2(){
        try {
            String httpResult = "{\"id\":\"326416\",\"headline\":\"TENCENT Becomes One of Top 10 Listed Companies Worldwide\",\"content\":\"Bloomberg News reported that with a market cap of US$279 billion, TENCENT (00700.HK) beat Wells Fargo to become one of the top ten listed companies in the world in terms of market value.<Br><Br>Of the top ten companies, six are IT companies and the top four are all IT companies. Apple Inc. is the company with the highest market value in the world, followed by Google's parent company Alphabet (2nd), Microsoft (3rd), Amazon (4th) and Facebook (6th).\",\"isTableFormat\":false,\"err_code\":\"2\",\"err_desc\":\"MDSEQTY50402\"}";
            JsonObject respJsonObj = JsonUtil.getAsJsonObject(httpResult);
            assertNotNull(TrisPropsUtil.inOrderStrProps("id","headline",respJsonObj));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testInOrderStrProps3(){
        try {
            String key = "SITE|PRODUCT|EXCHANGE";
            String field = "field";
            Map<String, Map<String, String[]>> trisFieldMapper = new HashMap<>();
            ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER, trisFieldMapper);
            Map<String, String[]> mapper = new HashMap<>();
            trisFieldMapper.put(key, mapper);
            mapper.put(field, new String[] {"A"});
            mapper.put(field, new String[] {"A", "B"});
            mapper.put(field, new String[] {"A", "B", "C"});
            String httpResult = "{\"id\":\"326416\",\"headline\":\"TENCENT Becomes One of Top 10 Listed Companies Worldwide\",\"content\":\"Bloomberg News reported that with a market cap of US$279 billion, TENCENT (00700.HK) beat Wells Fargo to become one of the top ten listed companies in the world in terms of market value.<Br><Br>Of the top ten companies, six are IT companies and the top four are all IT companies. Apple Inc. is the company with the highest market value in the world, followed by Google's parent company Alphabet (2nd), Microsoft (3rd), Amazon (4th) and Facebook (6th).\",\"isTableFormat\":false,\"err_code\":\"2\",\"err_desc\":\"MDSEQTY50402\"}";
            JsonObject respJsonObj = JsonUtil.getAsJsonObject(httpResult);
            assertNotNull(TrisPropsUtil.inOrderStrProps("id",field,respJsonObj));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void testGrowthRateProps(){
        String key = "SITE|PRODUCT|EXCHANGE";
        String field = "id";
        Map<String, Map<String, String[]>> trisFieldMapper = new HashMap<>();
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER, trisFieldMapper);
        Map<String, String[]> mapper = new HashMap<>();
        trisFieldMapper.put(key, mapper);
        mapper.put(field, new String[] {"A"});
        mapper.put(field, new String[] {"A", "B"});
        mapper.put(field, new String[] {"A", "B", "C"});
        String httpResult = "{\"id\":\"326416\",\"A\":\"1000\",\"B\":\"\",\"C\":\"2000\",\"headline\":\"TENCENT Becomes One of Top 10 Listed Companies Worldwide\",\"content\":\"Bloomberg News reported that with a market cap of US$279 billion, TENCENT (00700.HK) beat Wells Fargo to become one of the top ten listed companies in the world in terms of market value.<Br><Br>Of the top ten companies, six are IT companies and the top four are all IT companies. Apple Inc. is the company with the highest market value in the world, followed by Google's parent company Alphabet (2nd), Microsoft (3rd), Amazon (4th) and Facebook (6th).\",\"isTableFormat\":false,\"err_code\":\"2\",\"err_desc\":\"MDSEQTY50402\"}";
        JsonObject respJsonObj = JsonUtil.getAsJsonObject(httpResult);
        MathContext context = MathContext.DECIMAL32;
        assertNotNull(TrisPropsUtil.growthRateProps("id",key,respJsonObj,context));
    }

    @Test
    public void testGrowthRateProps2(){
        String key = "SITE|PRODUCT|EXCHANGE";
        String field = "id";
        Map<String, Map<String, String[]>> trisFieldMapper = new HashMap<>();
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER, trisFieldMapper);
        Map<String, String[]> mapper = new HashMap<>();
        trisFieldMapper.put(key, mapper);
        mapper.put(field, new String[] {"A"});
        mapper.put(field, new String[] {"A", "B"});
        String httpResult = "{\"id\":\"326416\",\"A\":\"1000\",\"B\":\"2\",\"headline\":\"TENCENT Becomes One of Top 10 Listed Companies Worldwide\",\"content\":\"Bloomberg News reported that with a market cap of US$279 billion, TENCENT (00700.HK) beat Wells Fargo to become one of the top ten listed companies in the world in terms of market value.<Br><Br>Of the top ten companies, six are IT companies and the top four are all IT companies. Apple Inc. is the company with the highest market value in the world, followed by Google's parent company Alphabet (2nd), Microsoft (3rd), Amazon (4th) and Facebook (6th).\",\"isTableFormat\":false,\"err_code\":\"2\",\"err_desc\":\"MDSEQTY50402\"}";
        JsonObject respJsonObj = JsonUtil.getAsJsonObject(httpResult);
        MathContext context = MathContext.DECIMAL32;
        assertNotNull(TrisPropsUtil.growthRateProps("id",key,respJsonObj,context));
    }
    @Test
    public void testGrowthRateProps3(){
        String key = "SITE|PRODUCT|EXCHANGE";
        String field = "id";
        Map<String, Map<String, String[]>> trisFieldMapper = new HashMap<>();
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER, trisFieldMapper);
        Map<String, String[]> mapper = new HashMap<>();
        trisFieldMapper.put(key, mapper);
        mapper.put(field, new String[] {"A"});
        String httpResult = "{\"id\":\"326416\",\"A\":\"1000\",\"headline\":\"TENCENT Becomes One of Top 10 Listed Companies Worldwide\",\"content\":\"Bloomberg News reported that with a market cap of US$279 billion, TENCENT (00700.HK) beat Wells Fargo to become one of the top ten listed companies in the world in terms of market value.<Br><Br>Of the top ten companies, six are IT companies and the top four are all IT companies. Apple Inc. is the company with the highest market value in the world, followed by Google's parent company Alphabet (2nd), Microsoft (3rd), Amazon (4th) and Facebook (6th).\",\"isTableFormat\":false,\"err_code\":\"2\",\"err_desc\":\"MDSEQTY50402\"}";
        JsonObject respJsonObj = JsonUtil.getAsJsonObject(httpResult);
        MathContext context = MathContext.DECIMAL32;
        assertNotNull(TrisPropsUtil.growthRateProps("id",key,respJsonObj,context));
    }

    @Test
    public void testDateProps(){
        String httpResult = "{\"date\":\"2021-09-06\",\"time\":\"07:51:22.0Z\",\"headline\":\"TENCENT Becomes One of Top 10 Listed Companies Worldwide\",\"content\":\"Bloomberg News reported that with a market cap of US$279 billion, TENCENT (00700.HK) beat Wells Fargo to become one of the top ten listed companies in the world in terms of market value.<Br><Br>Of the top ten companies, six are IT companies and the top four are all IT companies. Apple Inc. is the company with the highest market value in the world, followed by Google's parent company Alphabet (2nd), Microsoft (3rd), Amazon (4th) and Facebook (6th).\",\"isTableFormat\":false,\"err_code\":\"2\",\"err_desc\":\"MDSEQTY50402\"}";
        JsonObject respJsonObj = JsonUtil.getAsJsonObject(httpResult);
        Map<String, Map<String, String[]>> resultMap = new HashMap<>();
        Map<String, String[]> map = new HashMap<>();
        map.put("dateTime",new String[]{"date","time"});
        resultMap.put("HK_HASE|SEC|SHEC",map);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER, resultMap);
        assertNotNull(TrisPropsUtil.dateProps("dateTime","HK_HASE|SEC|SHEC",respJsonObj, TimeZone.getTimeZone("GMT")));
    }


    /*@Test
    public void testInOrderNumberProps(){
        try {
        String key = "SITE|PRODUCT|EXCHANGE";
        String field = "id";
        Map<String, Map<String, String[]>> trisFieldMapper = new HashMap<>();
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER, trisFieldMapper);
        Map<String, String[]> mapper = new HashMap<>();
        trisFieldMapper.put(key, mapper);
        mapper.put(field, new String[] {"A"});
        mapper.put(field, new String[] {"A", "B"});
        mapper.put(field, new String[] {"A", "B", "C"});
        String httpResult = "{\"id\":\"326416\",\"headline\":\"TENCENT Becomes One of Top 10 Listed Companies Worldwide\",\"content\":\"Bloomberg News reported that with a market cap of US$279 billion, TENCENT (00700.HK) beat Wells Fargo to become one of the top ten listed companies in the world in terms of market value.<Br><Br>Of the top ten companies, six are IT companies and the top four are all IT companies. Apple Inc. is the company with the highest market value in the world, followed by Google's parent company Alphabet (2nd), Microsoft (3rd), Amazon (4th) and Facebook (6th).\",\"isTableFormat\":false,\"err_code\":\"2\",\"err_desc\":\"MDSEQTY50402\"}";
        JsonObject respJsonObj = JsonUtil.getAsJsonObject(httpResult);
        assertNotNull(TrisPropsUtil.inOrderNumberProps("id",key,respJsonObj));
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}
