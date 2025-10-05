package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class ProdUndlInstmBatchLoader implements BatchLoader<DataFetchingEnvironment, Document> {

    @Autowired
    private MongoDatabase mongoDatabase;

    @Override
    public CompletionStage<List<Document>> load(List<DataFetchingEnvironment> envs) {
        return CompletableFuture.supplyAsync(() -> {
            List<Long> ids = envs.stream().map(env -> {
                Document undlStock = env.getSource();
                return undlStock.getLong("prodIdUndlInstm");
            }).collect(Collectors.toList());
            MongoCollection<Document> collection = mongoDatabase.getCollection(CollectionName.product.toString());
            List<Document> documents = collection.find(Filters.in("prodId", ids)).into(new ArrayList<>());
            Map<Long, Document> documentMap = documents.stream()
                    .collect(Collectors.toMap(doc -> doc.getLong("prodId"), doc -> doc));
            return ids.stream().map(documentMap::get).collect(Collectors.toList());
        });
    }
}
