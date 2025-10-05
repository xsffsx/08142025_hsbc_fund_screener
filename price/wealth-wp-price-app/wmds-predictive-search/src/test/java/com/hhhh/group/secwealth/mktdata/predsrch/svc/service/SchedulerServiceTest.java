package com.hhhh.group.secwealth.mktdata.predsrch.svc.service;


import com.hhhh.group.secwealth.mktdata.predsrch.svc.manager.PredictiveSearchManager;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl.SchedulerServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class SchedulerServiceTest {

    @Mock
    private PredictiveSearchManager predictiveSearchManager;


    @InjectMocks
    private SchedulerServiceImpl schedulerService;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void testRefreshData()  {
        Boolean flag=true;
        try {
            schedulerService.refreshData();
        } catch (Exception e) {
            flag=false;
        }
        Assert.assertTrue(flag);

    }

    @Test
    public void testRefreshDataEx()  {
        ReflectionTestUtils.setField(schedulerService,"predictiveSearchManager",null);
        Boolean flag=true;

        try {
            schedulerService.refreshData();
        } catch (Exception e) {
            flag=false;
        }
        Assert.assertTrue(flag);

    }
}
