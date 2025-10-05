package com.dummy.wmd.wpc.graphql;

import com.dummy.wmd.wpc.graphql.fetcher.CalculatedFieldDataLoader;
import com.dummy.wmd.wpc.graphql.fetcher.ProdUndlInstmBatchLoader;
import com.dummy.wmd.wpc.graphql.service.MetadataService;
import org.assertj.core.util.Lists;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.argThat;

public class productDataLoaderRegistryFactoryTest {

    productDataLoaderRegistryFactory productDataLoaderRegistryFactory = new productDataLoaderRegistryFactory();

    CalculatedFieldDataLoader calculatedFieldDataLoader = Mockito.mock(CalculatedFieldDataLoader.class);
    MetadataService metadataService = Mockito.mock(MetadataService.class);

    ProdUndlInstmBatchLoader prodUndlInstmBatchLoader = Mockito.mock(ProdUndlInstmBatchLoader.class);


    @Before
    public void before() {
        ReflectionTestUtils.setField(productDataLoaderRegistryFactory, "calculatedFieldDataLoader", calculatedFieldDataLoader);
        ReflectionTestUtils.setField(productDataLoaderRegistryFactory, "prodUndlInstmBatchLoader", prodUndlInstmBatchLoader);
        ReflectionTestUtils.setField(productDataLoaderRegistryFactory, "metadataService", metadataService);
    }

    @Test
    public void testInstrumentExecutionInput_givenCalculatedFields() {
        Document rootFieldMedata = new Document()
                .append("jsonPath", "allowTradProdInd")
                .append("parent", "[ROOT]");

        Document underAltIdFieldMedata = new Document()
                .append("jsonPath", "altId[*].prodId")
                .append("parent", "altId[*]");

        Mockito.doReturn(Lists.newArrayList(rootFieldMedata, underAltIdFieldMedata))
                .when(metadataService)
                .getMetadataList(argThat(argument -> argument.toString().contains("calculatedBy.rootObjectPath")));

        ReflectionTestUtils.invokeMethod(productDataLoaderRegistryFactory, "init");
        Assertions.assertEquals(4, productDataLoaderRegistryFactory.create().getDataLoaders().size());
    }

}
