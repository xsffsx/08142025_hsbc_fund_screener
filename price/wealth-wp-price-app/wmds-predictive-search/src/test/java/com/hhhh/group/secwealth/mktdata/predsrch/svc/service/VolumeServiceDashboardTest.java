package com.hhhh.group.secwealth.mktdata.predsrch.svc.service;


import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl.VolumeServiceDashboard;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.VolumeServiceConfig;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class VolumeServiceDashboardTest {

    @Mock
    private VolumeService volumeService;

    @Mock
    private VolumeServiceConfig volumeServiceConfig;


    @InjectMocks
    private VolumeServiceDashboard volumeServiceDashboard;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("site","site");
        Boolean flag=false;
        try {
            volumeServiceDashboard.execute(jsonObject);
        } catch (Exception e) {
            flag=true;
        }
       org.junit.Assert.assertTrue(flag);
    }

}
