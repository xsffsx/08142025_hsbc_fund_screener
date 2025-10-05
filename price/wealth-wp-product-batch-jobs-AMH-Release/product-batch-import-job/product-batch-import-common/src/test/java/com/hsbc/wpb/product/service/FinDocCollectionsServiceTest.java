package com.dummy.wpb.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.mongodb.client.model.Filters;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
public class FinDocCollectionsServiceTest {

    private GraphQLService graphQLService = mock(GraphQLService.class);

    private FinDocCollectionsService finDocCollectionsService = new FinDocCollectionsService(graphQLService);

    @Test
    public void testFinDocUpldByFilters() throws Exception {
        Mockito.when(graphQLService.executeRequest(mock(GraphQLRequest.class), mock(TypeReference.class)))
                .thenReturn(null);
        Assert.assertNull(finDocCollectionsService.finDocUpldByFilters(Filters.eq("rsrcItemIdFinDoc", 135168)));
    }

    @Test
    public void testProdTypeFinDocByFilters() throws Exception {
        Mockito.when(graphQLService.executeRequest(mock(GraphQLRequest.class), mock(TypeReference.class)))
                .thenReturn(null);
        Assert.assertNull(finDocCollectionsService.prodTypeFinDocByFilters(Filters.eq("docFinTypeCde", "USERDOC-5")));
    }

}