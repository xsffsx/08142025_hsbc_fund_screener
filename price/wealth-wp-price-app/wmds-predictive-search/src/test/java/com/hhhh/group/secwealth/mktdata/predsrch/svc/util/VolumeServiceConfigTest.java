package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

@RunWith(SpringRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class VolumeServiceConfigTest {

    @Mock
    @Qualifier("volumeServiceProperty")
    private Properties properties;


    @InjectMocks
    private VolumeServiceConfig volumeServiceConfig;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(properties);
        MockitoAnnotations.initMocks(volumeServiceConfig);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetValue(){
        Boolean flag=true;
        try {
            volumeServiceConfig.getValue("1","");
        } catch (Exception e) {
            flag=false;
        }
         org.junit.Assert.assertTrue(flag);
    }

    @Test
    public void testGetValueEx(){
        Boolean flag=false;
        try {
            ReflectionTestUtils.setField(volumeServiceConfig,"properties",null);
            volumeServiceConfig.getValue("1","");
        } catch (Exception e) {
            flag=true;
        }
         org.junit.Assert.assertTrue(flag);
    }

}
