package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wmd.wpc.graphql.Configuration;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.utils.FilterUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Projections.include;
@Slf4j
@Component
public class PbProductByFilterQueryFetcher  implements DataFetcher<List<Document>> {
    private static final CollectionName collectionName = CollectionName.pb_product;
    private MongoCollection<Document> collection;
    private static final ObjectMapper mapper = new ObjectMapper();

    public PbProductByFilterQueryFetcher(MongoDatabase mongoDatabase){
        this.collection = mongoDatabase.getCollection(collectionName.toString());
    }

    @Override
    public List<Document> get(DataFetchingEnvironment environment) throws JsonProcessingException {
        Map<String, Object> filterMap = environment.getArgument("filter");
        BsonDocument revisedFilterBson = new FilterUtils(collectionName).toBsonDocument(filterMap);

        Map<String, Object> sortMap = environment.getArgument("sort");
        BsonDocument sortBson = BsonDocument.parse("{}");
        if(null != sortMap) sortBson = BsonDocument.parse(mapper.writeValueAsString(sortMap));

        // projection is default on for better performance, but can turn it off when requesting calculated field
        Boolean projection = environment.getArgument("projection");
        Integer skip = environment.getArgument("skip");
        Integer limit = environment.getArgument("limit");
        if(null == limit) limit = Configuration.getDefaultLimit();

        Bson projections = null;
        if(Boolean.TRUE.equals(projection)) {
            List<String> fieldNames = getSelectedFields(environment);
            projections = include(fieldNames);
        }

        return collection
                .find(revisedFilterBson)
                .sort(sortBson)
                .skip(skip)
                .limit(limit)   // 0 means no limit
                .projection(projections)    // return only the fields requested
                .into(new LinkedList<>());
    }

    private List<String> getSelectedFields(DataFetchingEnvironment environment){
        List<SelectedField> selectedFields = environment.getSelectionSet().getFields();
        List<String> fieldNames = new LinkedList<>();
        selectedFields.forEach(field -> fieldNames.add(field.getQualifiedName().replace("/", ".")));
        return fieldNames;
    }
}
