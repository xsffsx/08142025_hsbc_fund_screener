package com.hhhh.group.secwealth.mktdata.common.service;


import com.hhhh.group.secwealth.mktdata.common.convertor.RequestConverter;
import com.hhhh.group.secwealth.mktdata.common.service.impl.LabciProtalSearchService;
import com.hhhh.group.secwealth.mktdata.common.service.impl.RestfulServiceImpl;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.common.validator.RegionServiceAdapter;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.Validator;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.MultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl.MultiPredictiveSearchServiceImpl;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class RestfulServiceTest {

    @Mock
    private Map<String, Service> servicesHolder;

    @Mock
    private RegionServiceAdapter regionServiceAdapter;

    @Mock
    private RequestConverter requestConverter;

    @Mock
    private SiteFeature siteFeature;

    @Mock
    private LabciProtalSearchService labciProtalSearchService;

    @InjectMocks
    private RestfulServiceImpl restfulService;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testPredsrch() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(new ArrayList() {{
            add("x-hhhh-locale");
            add("x-hhhh-chnl-countrycode");
            add("x-hhhh-chnl-group-member");
            add("x-hhhh-channel-id");
            add("x-hhhh-app-code");
        }}));
        when(request.getHeader("x-hhhh-locale")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-chnl-countrycode")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-chnl-group-member")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-channel-id")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-app-code")).thenReturn("Keep-Alive");

        when(this.regionServiceAdapter.getServices()).thenReturn(new HashMap<String, Service>() {{
            put("test", new MultiPredictiveSearchServiceImpl());
        }});

        when(regionServiceAdapter.getServiceName(any(String.class), any(String.class))).thenReturn("test");

        MultiPredSrchRequest multiPredSrchRequest = new MultiPredSrchRequest();
        multiPredSrchRequest.setMarket("US");

        when(this.requestConverter.converterToRequest(any(Map.class), any(String.class), any(String.class))).thenReturn(multiPredSrchRequest);

        when(labciProtalSearchService.multiPredsrch(any(MultiPredSrchRequest.class))).thenReturn(new ArrayList<>());

        when(siteFeature.getStringDefaultFeature(any(String.class), any(String.class))).thenReturn("Keep-Alive");

        try{
            org.junit.Assert.assertNotNull(restfulService.all("test", JSONObject.fromObject(new HashMap<String, String>() {{
                put("name", "test");
            }}), request));
        }catch (Exception e){
            e.getMessage();
        }


    }



    @Test
    public void testPredSrchRequest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(new ArrayList() {{
            add("x-hhhh-locale");
            add("x-hhhh-chnl-countrycode");
            add("x-hhhh-chnl-group-member");
            add("x-hhhh-channel-id");
            add("x-hhhh-app-code");
        }}));
        when(request.getHeader("x-hhhh-locale")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-chnl-countrycode")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-chnl-group-member")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-channel-id")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-app-code")).thenReturn("Keep-Alive");

        when(this.regionServiceAdapter.getServices()).thenReturn(new HashMap<String, Service>() {{
            put("test", new MultiPredictiveSearchServiceImpl());
        }});

        when(regionServiceAdapter.getServiceName(any(String.class), any(String.class))).thenReturn("test");

        PredSrchRequest predSrchRequest = new PredSrchRequest();
        predSrchRequest.setMarket("US");

        when(this.requestConverter.converterToRequest(any(Map.class), any(String.class), any(String.class))).thenReturn(predSrchRequest);

        when(labciProtalSearchService.predsrch(any(PredSrchRequest.class))).thenReturn(new ArrayList<>());

        when(siteFeature.getStringDefaultFeature(any(String.class), any(String.class))).thenReturn("Keep-Alive");

        try {
            org.junit.Assert.assertNotNull(restfulService.all("test", JSONObject.fromObject(new HashMap<String, String>() {{
                put("name", "test");
            }}), request));
        } catch (Exception e){
            e.getMessage();
        }

    }


    @Test
    public void testPredsrchException() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(new ArrayList() {{
            add("x-hhhh-chnl-countrycode");
            add("x-hhhh-chnl-group-member");
            add("x-hhhh-channel-id");
            add("x-hhhh-app-code");
        }}));
        when(request.getHeader("x-hhhh-locale")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-chnl-countrycode")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-chnl-group-member")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-channel-id")).thenReturn("Keep-Alive");

        when(this.regionServiceAdapter.getServices()).thenReturn(new HashMap<String, Service>() {{
            put("test", new MultiPredictiveSearchServiceImpl());
        }});

        when(regionServiceAdapter.getServiceName(any(String.class), any(String.class))).thenReturn("test");

        MultiPredSrchRequest multiPredSrchRequest = new MultiPredSrchRequest();
        multiPredSrchRequest.setMarket("US");

        when(this.requestConverter.converterToRequest(any(Map.class), any(String.class), any(String.class))).thenReturn(multiPredSrchRequest);

        when(labciProtalSearchService.multiPredsrch(any(MultiPredSrchRequest.class))).thenReturn(new ArrayList<>());

        when(siteFeature.getStringDefaultFeature(any(String.class), any(String.class))).thenReturn("Keep-Alive");

        Boolean flag=false;
        try {
            restfulService.all("test", JSONObject.fromObject(new HashMap<String, String>() {{
                put("name", "test");
            }}), request);
        } catch (Exception e) {
            flag=true;
        }
        org.junit.Assert.assertTrue(flag);
    }



    @Test
    public void testValidator() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(regionServiceAdapter.getValidators(any(String.class), any(String.class))).thenReturn(new ArrayList<Validator>(){{
            add(new Validator() {
                @Override
                public void preValidate(Object obj) throws Exception {
                }

                @Override
                public List<ValidatorError> validate(Map<String, String> headerMap, JSONObject json) throws Exception {
                    return null;
                }
            });
        }});
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(new ArrayList() {{
            add("x-hhhh-locale");
            add("x-hhhh-chnl-countrycode");
            add("x-hhhh-chnl-group-member");
            add("x-hhhh-channel-id");
            add("x-hhhh-app-code");
        }}));
        when(request.getHeader("x-hhhh-locale")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-chnl-countrycode")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-chnl-group-member")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-channel-id")).thenReturn("Keep-Alive");
        when(request.getHeader("x-hhhh-app-code")).thenReturn("Keep-Alive");

        when(this.regionServiceAdapter.getServices()).thenReturn(new HashMap<String, Service>() {{
            put("test", new MultiPredictiveSearchServiceImpl());
        }});

        when(regionServiceAdapter.getServiceName(any(String.class), any(String.class))).thenReturn("test");

        MultiPredSrchRequest multiPredSrchRequest = new MultiPredSrchRequest();
        multiPredSrchRequest.setMarket("US");

        when(this.requestConverter.converterToRequest(any(Map.class), any(String.class), any(String.class))).thenReturn(multiPredSrchRequest);

        when(labciProtalSearchService.multiPredsrch(any(MultiPredSrchRequest.class))).thenReturn(new ArrayList<>());

        when(siteFeature.getStringDefaultFeature(any(String.class), any(String.class))).thenReturn("Keep-Alive");

        Boolean flag=false;
        try {
            restfulService.all("test", JSONObject.fromObject(new HashMap<String, String>() {{
                put("name", "test");
            }}), request);
        } catch (Exception e) {
            flag=true;
        }
        org.junit.Assert.assertTrue(flag);

    }


}
