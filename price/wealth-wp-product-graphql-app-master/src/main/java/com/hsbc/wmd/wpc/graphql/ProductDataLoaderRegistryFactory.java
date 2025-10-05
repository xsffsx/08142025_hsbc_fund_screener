package com.dummy.wmd.wpc.graphql;

import com.dummy.wmd.wpc.graphql.fetcher.CalculatedFieldDataLoader;
import com.dummy.wmd.wpc.graphql.fetcher.ProdUndlInstmBatchLoader;
import com.dummy.wmd.wpc.graphql.fetcher.referencedata.ReferenceDataInUseBatchLoader;
import com.dummy.wmd.wpc.graphql.service.MetadataService;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class productDataLoaderRegistryFactory {

    @Autowired
    private CalculatedFieldDataLoader calculatedFieldDataLoader;

    @Autowired
    private ProdUndlInstmBatchLoader prodUndlInstmBatchLoader;

    @Autowired
    private ReferenceDataInUseBatchLoader referenceDataInUseBatchLoader;

    @Autowired
    private MetadataService metadataService;

    private static final List<String> calculatedFieldJsonPaths = new ArrayList<>();

    @PostConstruct
    private void init() {
        List<Document> calculatedMetadatas = metadataService.getMetadataList(Filters.exists("calculatedBy.rootObjectPath"));
        calculatedMetadatas.stream()
                .filter(metadata -> metadata.containsKey("jsonPath"))
                .map(metadata -> metadata.getString("jsonPath"))
                .forEach(calculatedFieldJsonPaths::add);
    }

    public DataLoaderRegistry create() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        for (String jsonPath : calculatedFieldJsonPaths) {
            dataLoaderRegistry.register(jsonPath, DataLoader.newDataLoader(calculatedFieldDataLoader));
        }
        dataLoaderRegistry.register("prodUndlInstm", DataLoader.newDataLoader(prodUndlInstmBatchLoader));
        dataLoaderRegistry.register("isInUseByProduct", DataLoader.newDataLoader(referenceDataInUseBatchLoader));
        return dataLoaderRegistry;
    }
}
