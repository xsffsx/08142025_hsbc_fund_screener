package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wmd.wpc.graphql.Configuration;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.utils.FilterUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class ProductPriceHistoryQueryFetcher implements DataFetcher<List<Map<String, Object>>> {
    private MongoCollection<Document> collection;
    private static final ObjectMapper mapper = new ObjectMapper();

    private static Integer defaultLimit = Configuration.getDefaultLimit();

    public ProductPriceHistoryQueryFetcher(MongoDatabase mongoDatabase){
        this.collection = mongoDatabase.getCollection(CollectionName.prod_prc_hist.toString());
    }

    @Override
    public List<Map<String, Object>> get(DataFetchingEnvironment environment) throws JsonProcessingException {
        Document source = environment.getSource();
        Object prodId = source.get(Field.prodId);

        Map<String, Object> filterMap = environment.getArgument("filter");
        if(filterMap == null) {
            filterMap = new LinkedHashMap<>();
        }
        filterMap.put(Field.prodId, prodId);
        BsonDocument revisedFilterBson = new FilterUtils(CollectionName.prod_prc_hist).toBsonDocument(filterMap);

        Map<String, Object> sortMap = environment.getArgument("sort");
        BsonDocument sortBson = BsonDocument.parse("{}");
        if(null != sortMap) sortBson = BsonDocument.parse(mapper.writeValueAsString(sortMap));

        Integer skip = environment.getArgument("skip");
        Integer limit = environment.getArgument("limit");
        if(null == limit) limit = defaultLimit;

        return collection
                .find(revisedFilterBson)
                .sort(sortBson)
                .skip(skip)
                .limit(limit)   // 0 means no limit
                .into(new LinkedList<>());

    }
}