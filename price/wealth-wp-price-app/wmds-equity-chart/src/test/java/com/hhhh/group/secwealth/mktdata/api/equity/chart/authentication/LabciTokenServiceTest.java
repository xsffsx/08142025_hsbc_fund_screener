package com.hhhh.group.secwealth.mktdata.api.equity.chart.authentication;

import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncher;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.LabciToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"sit_hase_stma_eqty_unittest"})
public class LabciTokenServiceTest {

    @Mock
    LabciTokenService labciTokenService;

    @Test
    public void testInitLabciAuthenticationServiceMapper(){
        Boolean flag=true;
        try {
            labciTokenService.initLabciAuthenticationServiceMapper();
        }catch (Exception e){
            flag = false;
            e.printStackTrace();
        }
        assertTrue(flag);
    }

}
