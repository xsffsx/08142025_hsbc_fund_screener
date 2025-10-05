package com.dummy.wmd.wpc.graphql.fetcher.defaultconfig;

import com.dummy.wmd.wpc.graphql.config.DefaultFieldConfig;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.utils.ObjectMapperUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class DefaultFieldConfigFetcher implements DataFetcher<Document> {
    private DefaultFieldConfig defaultConfig;

    private static final Document EMPTY = new Document();

    @Override
    public Document get(DataFetchingEnvironment environment) throws Exception {
        Map<String, Object> filterMap = environment.getArgument("filter");
        DocType docType = DocType.valueOf(environment.getArgument(Field.docType));
        log.info("Query default config filter: {}, docType: {}", filterMap, docType);
        return findByFilter(filterMap, docType);
    }

    /*
     * currently only support BOND - product_customer_eligibility
     */
    private Document findByFilter(Map<String, Object> filterMap, DocType docType) {
        if (docType == DocType.product_customer_eligibility) {
            String prodTypeCde = filterMap.get(Field.prodTypeCde).toString();
            return toDocument(defaultConfig.custElig(prodTypeCde));
        }
        return EMPTY;
    }

    private Document toDocument(Object object) {
        return Objects.isNull(object) ? EMPTY : ObjectMapperUtils.convertValue(object, Document.class);
    }
}
