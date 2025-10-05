package com.dummy.wmd.wpc.graphql.fetcher.assetvolt;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.AssetVolatilityClassService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class AssetVolatilityClassCorlUpdateFetcher implements DataFetcher<Document> {
    @Autowired private AssetVolatilityClassService service;

    public AssetVolatilityClassCorlUpdateFetcher(){
        // do nothing
    }

    @Override
    public Document get(DataFetchingEnvironment environment) throws Exception {
        Map<String, Object> doc = environment.getArgument(Field.doc);

        return service.saveAssetVolatilityClassCorl(new Document(doc));
    }
}
