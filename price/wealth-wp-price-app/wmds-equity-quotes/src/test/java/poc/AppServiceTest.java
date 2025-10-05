package poc;

import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppProperties.class, LabciProperties.class}, initializers=
        ConfigFileApplicationContextInitializer.class)
@EnableConfigurationProperties
@SpringBootTest
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class AppServiceTest {

    @Autowired
    AppProperties appProperties;

    @Autowired
    LabciProperties labciProperties;

    String testValue;

    @Test
    public void test() {
        System.out.println(appProperties.getTest());

        System.out.println(labciProperties.getService());
    }
}
