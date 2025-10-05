package com.dummy.wmd.wpc.graphql.fetcher.amendment;

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
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AmendmentByFilterQueryFetcher implements DataFetcher<List<Document>> {
    private MongoCollection<Document> collection;
    private static ObjectMapper mapper = new ObjectMapper();

    public AmendmentByFilterQueryFetcher(MongoDatabase mongoDatabase){
        this.collection = mongoDatabase.getCollection(CollectionName.amendment.toString());
    }

    private static Integer defaultLimit = Configuration.getDefaultLimit();

    @Override
    public List<Document> get(DataFetchingEnvironment environment) throws JsonProcessingException {
        Map<String, Object> filterMap = environment.getArgument("filter");
        BsonDocument revisedFilterBson = new FilterUtils(CollectionName.amendment).toBsonDocument(filterMap);

        Map<String, Object> sortMap = environment.getArgument("sort");
        BsonDocument sortBson = BsonDocument.parse("{}");
        if(null != sortMap) sortBson = BsonDocument.parse(mapper.writeValueAsString(sortMap));

        Integer skip = environment.getArgument("skip");
        Integer limit = environment.getArgument("limit");
        if(null == limit) limit = defaultLimit;

        List<Document> list = collection
                .find(revisedFilterBson)
                .sort(sortBson)
                .skip(skip)
                .limit(limit)   // 0 means no limit
                .into(new LinkedList<>());

        list.forEach(amendment -> AmendmentUtils.coerceChangeDocument(amendment, environment.getGraphQLSchema()));
        return list;
    }
}