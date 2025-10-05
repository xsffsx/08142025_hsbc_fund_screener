package com.dummy.wmd.wpc.graphql.fetcher.dashboard;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.GroupItem;
import com.dummy.wmd.wpc.graphql.service.ProductService;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import com.mongodb.client.model.Filters;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Component
public class GroupByProductStatusFetcher implements DataFetcher<List<GroupItem>> {
    private ProductService productService;
    private static Map<String, String> statusMap = new HashMap<>();


    private static Map<String, List<GroupItem>> groupItemCache = new ConcurrentHashMap<>();
    private static final Map<String, CompletableFuture<Void>> executingFuture = new ConcurrentHashMap<>(); // thread safe

    public GroupByProductStatusFetcher(ProductService productService, ReferenceDataService refdataService) {
        this.productService = productService;

        // construct reference data code to name mapping for product status code
        Bson filter = Filters.eq(Field.cdvTypeCde, "PRDSTUS");
        refdataService.getReferDataByFilter(filter).forEach(doc -> statusMap.put((String) doc.get(Field.cdvCde), (String) doc.get("cdvDesc")));
    }

    @Override
    public List<GroupItem> get(DataFetchingEnvironment environment) {
        String prodTypeCode = environment.getArgument("prodTypeCode");
        Map<String, Object> filterMap = (Map<String, Object>) environment.getVariables().get("filter");

        String prodStatusKey = getProdStatusKey(prodTypeCode, filterMap);

        executingFuture.computeIfAbsent(prodStatusKey, key -> CompletableFuture.runAsync(() -> {
            List<GroupItem> result = productService.groupByProductStatus(prodTypeCode, filterMap);
            groupItemCache.put(key, result);
            executingFuture.remove(key);
        }));

        if (!groupItemCache.containsKey(prodStatusKey)) {
            //make sure the result is not empty
            executingFuture.get(prodStatusKey).join();
        }

        List<GroupItem> groupByProductStatus = groupItemCache.get(prodStatusKey);
        // status code to name
        groupByProductStatus.stream().forEach(item -> {
            String code = item.getCode();
            String name = statusMap.get(code);
            item.setName(name == null ? code : name);
        });

        return groupByProductStatus;
    }

    private String getProdStatusKey(String prodTypeCode, Map<String, Object> filterMap) {
        String ctryRecCde = (String) filterMap.getOrDefault("ctryRecCde", "");
        String grpMembrRecCde = (String) filterMap.getOrDefault("grpMembrRecCde", "");
        if (StringUtils.isEmpty(prodTypeCode)) {
            prodTypeCode = "ALL";
        }
        return String.format("%s-%s-%s", ctryRecCde, grpMembrRecCde, prodTypeCode);
    }
}