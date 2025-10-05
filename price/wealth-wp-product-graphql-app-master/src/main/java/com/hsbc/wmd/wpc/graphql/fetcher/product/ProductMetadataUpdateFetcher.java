package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.MutationError;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("java:S3740")
@Slf4j
public class ProductMetadataUpdateFetcher implements DataFetcher<DataFetcherResult> {
    private MongoCollection<Document> collection;

    public ProductMetadataUpdateFetcher(MongoDatabase mongoDatabase){
        collection = mongoDatabase.getCollection(CollectionName.metadata.toString());
    }

    @Override
    public DataFetcherResult get(DataFetchingEnvironment environment) throws Exception {
        String id = environment.getArgument("id");
        Map<String, Object> metadata = environment.getArgument("metadata");

        /*
         validate input product metadata
         basic checking like field type, nullable already handled by the GraphQL framework,
         we need to do cross field checking, object array unique key here, etc.
         */

        String message = null;
        Document updated = null;
        try {
            collection.updateOne(Filters.eq(Field._id, id), updateBsonList(metadata));
            updated = collection.find(Filters.eq(Field._id, id)).first();
        } catch (MongoException e){
            message = e.getMessage();
        }

        DataFetcherResult.Builder builder = DataFetcherResult.newResult().data(updated);
        if(null != message) {
            MutationError error = new MutationError(message);
            builder.error(error);
        }

        return builder.build();
    }

    private List<Bson> updateBsonList(Map<String, Object> metadata) {
        List<Bson> list = new LinkedList<>();
        metadata.forEach((k,v) -> list.add(Updates.set(k, v)));
        return list;
    }
}