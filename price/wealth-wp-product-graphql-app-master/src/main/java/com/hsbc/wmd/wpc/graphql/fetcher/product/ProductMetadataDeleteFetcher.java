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

@SuppressWarnings("java:S3740")
@Slf4j
public class ProductMetadataDeleteFetcher implements DataFetcher<DataFetcherResult> {
    private MongoCollection<Document> collection;

    public ProductMetadataDeleteFetcher(MongoDatabase mongoDatabase){
        collection = mongoDatabase.getCollection(CollectionName.metadata.toString());
    }

    @Override
    public DataFetcherResult get(DataFetchingEnvironment environment) throws Exception {
        String id = environment.getArgument("id");

        String message = null;
        Document deleted = null;
        try {
            deleted = collection.find(Filters.eq(Field._id, id)).first();
            collection.deleteOne(Filters.eq(Field._id, id));
        } catch (MongoException e){
            message = e.getMessage();
        }

        DataFetcherResult.Builder builder = DataFetcherResult.newResult().data(deleted);
        if(null != message) {
            MutationError error = new MutationError(message);
            builder.error(error);
        }

        return builder.build();
    }
}