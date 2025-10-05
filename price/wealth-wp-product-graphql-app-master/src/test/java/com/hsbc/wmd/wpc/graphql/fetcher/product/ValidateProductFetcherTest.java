package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.dummy.wmd.wpc.graphql.validator.Error;
import com.dummy.wmd.wpc.graphql.validator.ProductValidator;
import graphql.schema.DataFetchingEnvironment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyMap;

@RunWith(MockitoJUnitRunner.class)
public class ValidateProductFetcherTest {

    @InjectMocks
    private ValidateProductFetcher validateProductFetcher;
    @Mock
    private ProductValidator productValidator;
    @Mock
    private DataFetchingEnvironment environment;

    @Test
    public void get() {
        Mockito.when(environment.getArgument("product")).thenReturn(new HashMap<>());
        Mockito.when(productValidator.validate(anyMap())).thenReturn(new ArrayList<>());
        List<Error> errorList = validateProductFetcher.get(environment);
        Assert.assertNotNull(errorList);
    }
}
