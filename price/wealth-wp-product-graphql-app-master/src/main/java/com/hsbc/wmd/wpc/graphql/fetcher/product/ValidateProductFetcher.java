package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.dummy.wmd.wpc.graphql.validator.Error;
import com.dummy.wmd.wpc.graphql.validator.ProductValidator;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ValidateProductFetcher implements DataFetcher<List<Error>> {
    @Autowired
    private ProductValidator productValidator;

    @Override
    public List<Error> get(DataFetchingEnvironment environment) {
        // all validation has been handled by the GraphQL framework and extended validations
        Map<String, Object> product = environment.getArgument("product");

        return productValidator.validate(product);
    }
}