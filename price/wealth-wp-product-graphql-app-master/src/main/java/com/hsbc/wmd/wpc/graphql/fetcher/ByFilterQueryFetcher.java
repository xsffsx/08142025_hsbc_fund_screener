package com.dummy.wmd.wpc.graphql.fetcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wmd.wpc.graphql.Configuration;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.utils.FilterUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ByFilterQueryFetcher implements DataFetcher<List<Map<String, Object>>> {
    private final CollectionName collectionName;
    private MongoCollection<Document> collection;
    private ObjectMapper mapper = new ObjectMapper();

    public ByFilterQueryFetcher(MongoDatabase mongoDatabase, CollectionName collectionName){
        this.collectionName = collectionName;
        this.collection = mongoDatabase.getCollection(collectionName.name());
    }

    private Integer defaultLimit = Configuration.getDefaultLimit();

    @Override
    public List<Map<String, Object>> get(DataFetchingEnvironment environment) throws JsonProcessingException {
        Map<String, Object> filterMap = environment.getArgument("filter");
        BsonDocument revisedFilterBson = new FilterUtils(collectionName).toBsonDocument(filterMap);

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