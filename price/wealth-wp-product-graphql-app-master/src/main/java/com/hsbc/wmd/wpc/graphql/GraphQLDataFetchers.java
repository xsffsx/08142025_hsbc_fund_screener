package com.dummy.wmd.wpc.graphql;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.*;

@SuppressWarnings("java:S3740")
@Component
@Slf4j
public class GraphQLDataFetchers {
    @Autowired
    private MongoDatabase mongoDatabase;

    public DataFetcher getProductByIdDataFetcher() {
        MongoCollection<Document> collection = mongoDatabase.getCollection(CollectionName.product.toString());
        return dataFetchingEnvironment -> {
            Object prodId = dataFetchingEnvironment.getArgument(Field.prodId);
            return collection.find(eq(Field.prodId, prodId)).first();
        };
    }
    public DataFetcher getPbProductByIdDataFetcher() {
        MongoCollection<Document> collection = mongoDatabase.getCollection(CollectionName.pb_product.toString());
        return dataFetchingEnvironment -> {
            Object prodId = dataFetchingEnvironment.getArgument("smartIdentifier");
            return collection.find(eq("smartIdentifier", prodId)).first();
        };
    }

    public DataFetcher getProductMetaDataFetcher() {
        MongoCollection<Document> collection = mongoDatabase.getCollection(CollectionName.metadata.toString());
        return dataFetchingEnvironment -> {
            String prodType = dataFetchingEnvironment.getArgument("prodType");
            String entity = dataFetchingEnvironment.getArgument("entity");

            List<Bson> filters = new ArrayList<>();
            filters.add(nin("table", "PROD_PRC_HIST"));
            filters.add(ne("graphQLType", "ProductType"));
            if(null != prodType){
                filters.add(eq("prodType", prodType));
            }
            if(null != entity){
                filters.add(eq("entity", entity));
            }

            List<Map<String, Object>> list = new LinkedList<>();
            collection.find(and(filters)).into(list);
            return list;
        };
    }

    public DataFetcher getProdRelnFetcher() {
        MongoCollection<Document> collection = mongoDatabase.getCollection(CollectionName.product.toString());
        return dataFetchingEnvironment -> {
            Map<String, String> prodReln = dataFetchingEnvironment.getSource();
            Object prodId = prodReln.get("prodIdRel");
            return collection.find(eq(Field.prodId, prodId)).first();
        };
    }

    public DataFetcher getCalculatedFieldFetcher(Document metadata) {
        return dataFetchingEnvironment -> {
            DataLoader<DataFetchingEnvironment, Object> dataLoader = dataFetchingEnvironment.getDataLoader(metadata.getString("jsonPath"));
            return null == dataLoader ? null : dataLoader.load(dataFetchingEnvironment);
        };
    }
}
