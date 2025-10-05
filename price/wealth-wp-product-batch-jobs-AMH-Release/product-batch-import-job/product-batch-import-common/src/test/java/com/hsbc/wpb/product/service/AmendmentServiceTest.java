package com.dummy.wpb.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.graphql.Amendment;
import com.mongodb.client.model.Filters;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

import static org.mockito.Mockito.mock;


@RunWith(SpringJUnit4ClassRunner.class)
public class AmendmentServiceTest {

    private GraphQLService graphQLService = mock(GraphQLService.class);

    private AmendmentService amendmentService = new AmendmentService(graphQLService);

    @Test
    public void testAmendmentByFilters() throws Exception {
        Mockito.when(graphQLService.executeRequest(mock(GraphQLRequest.class), mock(TypeReference.class))).thenReturn(null);
        Assert.assertNull(amendmentService.amendmentByFilters(Filters.eq("prodTypeCde", "UT")));
    }

    @Test
    public void testAmendmentCreate_returnNull() throws Exception {
        Mockito.when(graphQLService.executeRequest(Mockito.any(GraphQLRequest.class), Mockito.eq(Amendment.class))).thenReturn(null);
        Assert.assertNull(amendmentService.amendmentCreate("add", "product", new HashMap<>()));
    }

    @Test
    public void testAmendmentCreate_returnNotNull() throws Exception {
        Mockito.when(graphQLService.executeRequest(Mockito.any(GraphQLRequest.class), Mockito.eq(Amendment.class))).thenReturn(new Amendment());
        Assert.assertNotNull(amendmentService.amendmentCreate("add", "product", new HashMap<>()));
    }

}