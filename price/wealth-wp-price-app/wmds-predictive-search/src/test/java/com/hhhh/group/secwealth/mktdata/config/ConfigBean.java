package com.hhhh.group.secwealth.mktdata.config;


import com.hhhh.group.secwealth.mktdata.WmdsPredictiveSearchApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = WmdsPredictiveSearchApplication.class)
@WebAppConfiguration
@ActiveProfiles({"sit"})
public class ConfigBean {

}
