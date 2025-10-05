package com.dummy.wpb.product.configuration;

import com.dummy.wpb.product.service.GraphQLService;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceConfigurationTest {

    @Test
    public void testBean() {
        GraphQLService graphQLService = Mockito.mock(GraphQLService.class);
        ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
        ReflectionTestUtils.setField(serviceConfiguration, "graphQLService", graphQLService);
        Assert.assertNotNull(serviceConfiguration.amendmentService());
        Assert.assertNotNull(serviceConfiguration.finDocCollectionsService());
        Assert.assertNotNull(serviceConfiguration.legacyConfig());
    }
}