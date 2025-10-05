package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.MutationError;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.Map;

@SuppressWarnings("java:S3740")
@Slf4j
public class ProductMetadataCreateFetcher implements DataFetcher<DataFetcherResult> {
    private MongoCollection<Document> collection;

    private static final String JSON_PATH = "jsonPath";

    public ProductMetadataCreateFetcher(MongoDatabase mongoDatabase){
        collection = mongoDatabase.getCollection("metadata");
    }

    @Override
    public DataFetcherResult get(DataFetchingEnvironment environment) throws Exception {
        Map<String, Object> metadata = environment.getArgument(CollectionName.metadata.toString());

        /*
         validate input product metadata
         basic checking like field type, nullable already handled by the GraphQL framework,
         we need to do cross field checking, object array unique key here, etc.
         */

        String message = null;
        Document inserted = null;
        try {
            metadata.put(Field._id, metadata.get(JSON_PATH));  // jsonPath should be unique in the collection
            collection.insertOne(new Document(metadata));
            inserted = collection.find(Filters.eq(JSON_PATH, metadata.get(JSON_PATH))).first();
        } catch (MongoException e){
            message = e.getMessage();
        }

        DataFetcherResult.Builder builder = DataFetcherResult.newResult().data(inserted);
        if(null != message) {
            MutationError error = new MutationError(message);
            builder.error(error);
        }

        return builder.build();
    }
}