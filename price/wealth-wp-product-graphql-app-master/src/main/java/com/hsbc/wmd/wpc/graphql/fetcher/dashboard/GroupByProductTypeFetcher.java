package com.dummy.wmd.wpc.graphql.fetcher.dashboard;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.GroupItem;
import com.dummy.wmd.wpc.graphql.service.ProductService;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import com.mongodb.client.model.Filters;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Component
public class GroupByProductTypeFetcher implements DataFetcher<List<GroupItem>> {
    private ProductService productService;
    private static Map<String, String> prodTypeMap = new HashMap<>();

    private static final Map<String, List<GroupItem>> groupItemCache = new ConcurrentHashMap<>();
    private static final Map<String, CompletableFuture<Void>> executingFuture = new ConcurrentHashMap<>(); // thread safe

    public GroupByProductTypeFetcher(ProductService productService, ReferenceDataService refdataService) {
        this.productService = productService;
        // construct reference data code to name mapping for product type code
        Bson filter = Filters.eq(Field.cdvTypeCde, "PRODTYP");
        refdataService.getReferDataByFilter(filter).forEach(doc -> prodTypeMap.put((String) doc.get(Field.cdvCde), (String) doc.get("cdvDesc")));
    }

    @Override
    public List<GroupItem> get(DataFetchingEnvironment environment) {
        Map<String, Object> filterMap = (Map<String, Object>) environment.getVariables().get("filter");

        String prodTypeKey = getProdTypeKey(filterMap);

        executingFuture.computeIfAbsent(prodTypeKey, key -> CompletableFuture.runAsync(() -> {
            List<GroupItem> result = productService.groupByProductType(filterMap);
            groupItemCache.put(key, result);
            executingFuture.remove(key);
        }));

        if (!groupItemCache.containsKey(prodTypeKey)) {
            //make sure the result is not empty
            executingFuture.get(prodTypeKey).join();
        }

        List<GroupItem> groupByProductType = groupItemCache.get(prodTypeKey);
        // product type code to name
        groupByProductType.stream().forEach(item -> {
            String code = item.getCode();
            String name = prodTypeMap.get(code);
            item.setName(name == null ? code : name);
        });

        return groupByProductType;
    }

    private String getProdTypeKey(Map<String, Object> filterMap) {
        String ctryRecCde = (String) filterMap.getOrDefault("ctryRecCde", "");
        String grpMembrRecCde = (String) filterMap.getOrDefault("grpMembrRecCde", "");
        return String.format("%s-%s", ctryRecCde, grpMembrRecCde);
    }
}