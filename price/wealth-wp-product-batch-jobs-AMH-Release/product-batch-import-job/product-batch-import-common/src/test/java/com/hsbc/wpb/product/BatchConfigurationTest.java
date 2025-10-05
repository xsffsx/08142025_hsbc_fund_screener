package com.dummy.wpb.product;

import com.dummy.wpb.product.configuration.SystemDefaultValuesHolder;
import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import com.dummy.wpb.product.model.SystemDefaultValues;
import com.dummy.wpb.product.model.SystemUpdateConfig;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;


@RunWith(SpringRunner.class)
public class BatchConfigurationTest {

    @Test
    public void testSystemDefaultValuesHolder() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("system-default-values[0].systemCde", "RBT");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setEnvironment(env);
        context.register(BatchConfiguration.class);
        context.refresh();

        SystemDefaultValuesHolder systemDefaultValuesHolder = context.getBean(SystemDefaultValuesHolder.class);
        Assert.assertNotNull( systemDefaultValuesHolder);

        SystemDefaultValues systemDefaultValues = new SystemDefaultValues();
        systemDefaultValues.setSystemCde("AMHCUTAS");
        systemDefaultValues.setTypeCde("UT");
        systemDefaultValues.setDefaultValues(new HashMap<>());
        systemDefaultValuesHolder.setSystemDefaultValues(Arrays.asList(systemDefaultValues));
        Assert.assertNotNull(systemDefaultValuesHolder.getDefaultValues("AMHCUTAS", "UT~SEC"));
        Assert.assertNotNull(systemDefaultValuesHolder.getDefaultValues("AMHCUTAS"));
        Assert.assertTrue(systemDefaultValuesHolder.isSupport("AMHCUTAS"));
    }

    @Test
    public void tesSystemUpdateConfigHolder() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("system-update-config[0].systemCde", "AMHGSOPS.AS");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setEnvironment(env);
        context.register(BatchConfiguration.class);
        context.refresh();

        SystemUpdateConfigHolder systemUpdateConfigHolder = context.getBean(SystemUpdateConfigHolder.class);
        Assert.assertNotNull(systemUpdateConfigHolder);

        SystemUpdateConfig systemUpdateConfig = new SystemUpdateConfig();
        SystemUpdateConfig.UpdateConfig updateConfig = new SystemUpdateConfig.UpdateConfig();
        updateConfig.setCollection("product");
        updateConfig.setTypeCde("UT");
        updateConfig.setUpdateAttrs(Arrays.asList("prodTypeCde","prodName"));
        systemUpdateConfig.setSystemCde("AMHCUTAS");
        systemUpdateConfig.setConfig(Arrays.asList(updateConfig));
        systemUpdateConfigHolder.setSystemUpdateConfig(Arrays.asList(systemUpdateConfig));
        Assert.assertNotNull(systemUpdateConfigHolder.getUpdateAttrs("AMHCUTAS", "product"));
        Assert.assertNotNull(systemUpdateConfigHolder.getUpdateAttrs("AMHCUTAS", "prod"));
        Assert.assertNotNull(systemUpdateConfigHolder.getUpdateAttrs("MDS", "product"));
        Assert.assertNotNull(systemUpdateConfigHolder.getUpdateAttrs("AMHCUTAS", "UT~SEC", "product"));
        Assert.assertNotNull(systemUpdateConfigHolder.getUpdateAttrs("AMHCUTAS", "UT", "product"));
        Assert.assertNotNull(systemUpdateConfigHolder.getUpdateAttrs("MDS", "UT~SEC", "product"));
        Assert.assertTrue(systemUpdateConfigHolder.isSupport("AMHCUTAS"));
    }

}