package com.dummy.wmd.wpc.graphql.fetcher.defaultconfig;


import com.dummy.wmd.wpc.graphql.constant.ActionCde;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.DefaultFieldConfigCreateService.DefaultConfigCreateServiceHolder;
import com.dummy.wmd.wpc.graphql.service.DefaultCustomerEligibilityCreateService;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class DefaultConfigCreateFetcherTest {
    private DefaultCustomerEligibilityCreateService defaultCustomerEligibilityCreateService;
    private DefaultFieldConfigCreateFetcher defaultFieldConfigCreateFetcher;
    private DataFetchingEnvironment environment;

    @Before
    public void before() {
        defaultCustomerEligibilityCreateService = Mockito.mock(DefaultCustomerEligibilityCreateService.class);
        DefaultConfigCreateServiceHolder defaultConfigCreateServiceHolder =
                new DefaultConfigCreateServiceHolder(defaultCustomerEligibilityCreateService);
        defaultFieldConfigCreateFetcher = new DefaultFieldConfigCreateFetcher(defaultConfigCreateServiceHolder);
        environment = Mockito.mock(DataFetchingEnvironment.class);
    }

    @Test
    public void testGet() throws Exception {
        when(environment.getArgument(Field.docType)).thenReturn(DocType.product_customer_eligibility.name());
        when(defaultCustomerEligibilityCreateService.create(anyMap(), any(DocType.class), eq(ActionCde.add))).thenReturn(new Document());
        Document document = defaultFieldConfigCreateFetcher.get(environment);
        Assert.assertNull(document);
    }
}