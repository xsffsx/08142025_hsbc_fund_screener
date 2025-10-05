package com.dummy.wmd.wpc.graphql.fetcher.defaultconfig;

import com.dummy.wmd.wpc.graphql.constant.ActionCde;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.DefaultFieldConfigCreateService.DefaultConfigCreateServiceHolder;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class DefaultFieldConfigCreateFetcher implements DataFetcher<Document> {
    private DefaultConfigCreateServiceHolder defaultConfigCreateServiceHolder;

    @Override
    public Document get(DataFetchingEnvironment environment) throws Exception {
        Map<String, Object> filterMap = environment.getArgument("filter");
        DocType docType = DocType.valueOf(environment.getArgument(Field.docType));
        log.info("Create default config for docType: {}", docType);
        return defaultConfigCreateServiceHolder.getService(docType).create(filterMap, docType, ActionCde.add);
    }
}
