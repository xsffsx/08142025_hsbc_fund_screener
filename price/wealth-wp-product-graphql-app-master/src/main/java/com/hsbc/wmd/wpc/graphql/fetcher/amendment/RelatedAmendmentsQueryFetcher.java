package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Slf4j
public class RelatedAmendmentsQueryFetcher implements DataFetcher<List<Document>> {
    private final MongoCollection<Document> collAmendment;
    private DocType docType;
    private ObjectMapper mapper = new ObjectMapper();

    public RelatedAmendmentsQueryFetcher(MongoDatabase mongoDatabase, DocType docType){
        this.docType = docType;
        this.collAmendment = mongoDatabase.getCollection(CollectionName.amendment.toString());
    }

    @Override
    public List<Document> get(DataFetchingEnvironment environment) throws JsonProcessingException {
        Document source = environment.getSource();
        Boolean lastOnly = environment.getArgument("lastOnly");
        Map<String, Object> sortMap = environment.getArgument("sort");
        Object docId = source.get(Field._id);
        Bson sortBson = BsonDocument.parse("{}");
        if(null != sortMap) sortBson = BsonDocument.parse(mapper.writeValueAsString(sortMap));

        String docTypeStr = environment.getArgumentOrDefault("docType",docType.toString());
        Bson filter = and(eq(Field.docType, docTypeStr), eq(Field.docId, docId));

        Map<String,Object> filterMap = environment.getArgumentOrDefault("filter", Collections.emptyMap());
        if (!filterMap.isEmpty()){
            for (Map.Entry<String,Object> entry : filterMap.entrySet()){
                filter = and(filter, eq(entry.getKey(), entry.getValue()));
            }
        }

        Integer limit = 0;
        if(Boolean.TRUE.equals(lastOnly)) {
            sortBson = Sorts.descending(Field._id);
            limit = 1;
        }

        return collAmendment
                .find(filter)
                .sort(sortBson)
                .limit(limit)   // 0 means no limit
                .into(new LinkedList<>());
    }
}