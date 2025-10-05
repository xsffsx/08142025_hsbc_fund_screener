package com.dummy.wmd.wpc.graphql.fetcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.FilterUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class DocumentCountByFilterQueryFetcher implements DataFetcher<Long> {
    private final MongoDatabase mongoDatabase;

    public DocumentCountByFilterQueryFetcher(MongoDatabase mongoDatabase){
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public Long get(DataFetchingEnvironment environment) throws JsonProcessingException {
        DocType docType = DocType.valueOf(environment.getArgument(Field.docType));
        Map<String, Object> filterMap = environment.getArgument("filter");
        CollectionName collectionName = getCollectionName(docType);

        BsonDocument revisedFilterBson = new FilterUtils(collectionName).toBsonDocument(filterMap);

        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName.toString());

        return collection.countDocuments(revisedFilterBson);
    }

    private CollectionName getCollectionName(DocType docType) {
        CollectionName collectionName = CollectionName.valueOf(docType.toString());
        if(null == collectionName) {
            throw new productErrorException(productErrors.NotSupportDocType, "Not support docType: " + docType);
        }
        return collectionName;
    }
}