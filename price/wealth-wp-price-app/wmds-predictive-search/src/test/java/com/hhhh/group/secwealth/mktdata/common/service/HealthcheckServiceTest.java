package com.hhhh.group.secwealth.mktdata.common.service;

import com.hhhh.group.secwealth.mktdata.common.convertor.RequestConverter;
import com.hhhh.group.secwealth.mktdata.common.service.impl.HealthcheckServiceImpl;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.validator.RegionServiceAdapter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class HealthcheckServiceTest {

    @Mock
    private RegionServiceAdapter regionServiceAdapter;

    @Mock
    private RequestConverter requestConverter;

    @InjectMocks
    private HealthcheckServiceImpl healthcheckService;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


//    @Test
//    public void testInit() {
//        Boolean flag=true;
//        try {
//            healthcheckService.init();
//        } catch (FileNotFoundException e) {
//            flag=false;
//        }
//        Assert.assertTrue(flag);
//    }

    @Test
    public void testAuthenticate() {
        boolean authenticate = healthcheckService.authenticate("userid", "password");
        Assert.assertTrue(!authenticate);
    }

    @Test
    public void testGetHeaderMap() {
        try{
            Map<String, String> headerMap = healthcheckService.getHeaderMap("HK_HASE.");
            assertEquals("en_US",headerMap.get(CommonConstants.REQUEST_HEADER_LOCALE));
            assertEquals("HK",headerMap.get(CommonConstants.REQUEST_HEADER_COUNTRYCODE));
            assertEquals("HASE",headerMap.get(CommonConstants.REQUEST_HEADER_GROUPMEMBER));
            assertEquals("OHI",headerMap.get(CommonConstants.REQUEST_HEADER_CHANNELID));
            assertEquals("mds",headerMap.get(CommonConstants.REQUEST_HEADER_APPCODE));
        }catch (Exception e){
            e.getMessage();
        }

    }


    @Test
    public void testHealthDashboardApi() {
        StringBuilder sb = new StringBuilder();
        healthcheckService.healthDashboardApi(sb,"HK_HASE.");
        assertTrue(true);
    }

    @Test
    public void testHealthDashboardApiException() {
        Boolean flag=true;
        try {
            when(regionServiceAdapter.getServices()).thenReturn(new HashMap<String,Service>(){{
                put("predictiveSearchService", new Service() {
                    @Override
                    public Object doService(Object object) throws Exception {
                        return null;
                    }
                });
            }});
            StringBuilder sb = new StringBuilder();
            healthcheckService.healthDashboardApi(sb,"HK_HASE.");
        } catch (Exception e) {
             flag=false;
        }
        Assert.assertTrue(flag);
    }



    @Test
    public void testGetSystemTime() throws Exception {
        assertNotNull(healthcheckService.getSystemTime());
    }

    @Test
    public void testHealthDashboardSite() throws Exception {
        try{
            assertNotNull(healthcheckService.healthDashboardSite());
        }catch (Exception e){
            e.getMessage();
        }

    }



}
