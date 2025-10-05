package com.dummy.wmd.wpc.graphql;

import com.dummy.wmd.wpc.graphql.service.MetadataService;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetcher;
import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.util.Lists;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

public class GraphQLProviderTest {

    GraphQLProvider graphQLProvider = new GraphQLProvider();

    ProductMetadataHelper productMetadata = Mockito.mock(ProductMetadataHelper.class);

    GraphQLDataFetchers graphQLDataFetchers = Mockito.mock(GraphQLDataFetchers.class);

    DataFetcher<Object> emptyDataFetcher = environment -> null;

    MongoDatabase mongoDatabase = Mockito.mock(MongoDatabase.class);

    MetadataService metadataService = Mockito.mock(MetadataService.class);

    productInstrumentation productInstrumentation = Mockito.mock(productInstrumentation.class);

    @Before
    public void before() {
        Mockito.when(productMetadata.getProductInputSchema()).thenReturn(this.getClass().getResource("/product-input-schema.graphqls"));
        Mockito.when(productMetadata.getProductOutputSchema()).thenReturn(this.getClass().getResource("/product-output-schema.graphqls"));
        ReflectionTestUtils.setField(graphQLProvider, "productMetadata", productMetadata);
        ReflectionTestUtils.setField(graphQLProvider, "graphQLDataFetchers", graphQLDataFetchers);
        ReflectionTestUtils.setField(graphQLProvider, "mongoDatabase", mongoDatabase);
        ReflectionTestUtils.setField(graphQLProvider, "metadataService", metadataService);
        ReflectionTestUtils.setField(graphQLProvider, "productInstrumentation", productInstrumentation);
        Mockito.doReturn(emptyDataFetcher).when(graphQLDataFetchers).getProductByIdDataFetcher();
        Mockito.doReturn(emptyDataFetcher).when(graphQLDataFetchers).getPbProductByIdDataFetcher();
        Mockito.doReturn(emptyDataFetcher).when(graphQLDataFetchers).getProductMetaDataFetcher();
        Mockito.doReturn(emptyDataFetcher).when(graphQLDataFetchers).getProdRelnFetcher();
        Mockito.doReturn(emptyDataFetcher).when(graphQLDataFetchers).getCalculatedFieldFetcher(any(Document.class));

        for (Field field : GraphQLProvider.class.getDeclaredFields()) {
            if (ArrayUtils.contains(field.getType().getInterfaces(), DataFetcher.class)) {
                ReflectionTestUtils.setField(graphQLProvider, field.getName(), Mockito.mock(field.getType()));
            }
        }
    }

    @Test
    public void testGraphQLProvider() {
        Document rootFieldMedata = new Document()
                .append("jsonPath", "allowTradProdInd")
                .append("parent", "[ROOT]");

        Document underAltIdFieldMedata = new Document()
                .append("jsonPath", "altId[*].prodId")
                .append("parent", "altId[*]");

        Document altIdMedata = new Document()
                .append("jsonPath", "altId[*]")
                .append("graphQLType", "[AltIdType]");

        Mockito.doReturn(Lists.newArrayList(rootFieldMedata, underAltIdFieldMedata))
                .when(metadataService)
                .getMetadataList(argThat(argument -> argument.toString().contains("calculatedBy.rootObjectPath")));

        Mockito.doReturn(Lists.newArrayList(altIdMedata))
                .when(metadataService)
                .getMetadataList(argThat(argument -> argument.toString().contains("altId[*]")));

        ReflectionTestUtils.invokeMethod(graphQLProvider, "init");

        Assertions.assertNotNull(GraphQLProvider.getGraphQLSchema().getType("ProductType"));
    }
}
