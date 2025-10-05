package com.dummy.wmd.wpc.graphql.fetcher.referencedata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Slf4j
@Component
public class ReferenceDataParentQueryFetcher implements DataFetcher<Document> {
    private MongoCollection<Document> collection;

    public ReferenceDataParentQueryFetcher(MongoDatabase mongoDatabase){
        this.collection = mongoDatabase.getCollection(CollectionName.reference_data.toString());
    }

    @Override
    public Document get(DataFetchingEnvironment environment) throws JsonProcessingException {
        Document source = environment.getSource();

        Bson filter = and(
                eq(Field.ctryRecCde, source.get(Field.ctryRecCde)),
                eq(Field.grpMembrRecCde, source.get(Field.grpMembrRecCde)),
                eq(Field.cdvTypeCde, source.get(Field.cdvParntTypeCde)),
                eq(Field.cdvCde, source.get(Field.cdvParntCde)));

        return collection.find(filter).first();
    }
}