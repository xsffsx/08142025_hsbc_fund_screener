package com.hhhh.group.secwealth.mktdata.common.Service;


import com.hhhh.group.secwealth.mktdata.common.service.impl.HealthcheckServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.Silent.class)
public class HealthcheckServiceTest {

    @InjectMocks
    HealthcheckServiceImpl healthcheckService;

    @Test
    public void getSystemTime()throws Exception{
        healthcheckService.getSystemTime();
        Assert.assertTrue(true);
    }

    @Test
    public void authenticate()throws Exception{
        healthcheckService.authenticate("12345","12345");
        Assert.assertTrue(true);
    }


}
