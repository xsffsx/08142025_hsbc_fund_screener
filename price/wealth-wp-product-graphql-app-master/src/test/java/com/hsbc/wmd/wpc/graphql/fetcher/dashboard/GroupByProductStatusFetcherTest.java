package com.dummy.wmd.wpc.graphql.fetcher.dashboard;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.GroupItem;
import com.dummy.wmd.wpc.graphql.service.ProductService;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class GroupByProductStatusFetcherTest {

    private GroupByProductStatusFetcher groupByProductStatusFetcher;
    @Mock
    private ProductService productService;
    @Mock
    private ReferenceDataService refdataService;
    @Mock
    private DataFetchingEnvironment environment;

    @Before
    public void setUp() {
        List<Document> list = new ArrayList<>();
        Document document = new Document();
        document.put(Field.cdvCde, "cdvCde");
        document.put(Field.cdvDesc, "cdvDesc");
        list.add(document);
        Mockito.when(refdataService.getReferDataByFilter(any(Bson.class))).thenReturn(list);
        groupByProductStatusFetcher = new GroupByProductStatusFetcher(productService, refdataService);

    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsGroupItemList() {
        Mockito.when(environment.getArgument("prodTypeCode")).thenReturn(null);
        Map<String, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> filter = new HashMap<>();
        map.put("filter", filter);
        filter.put("ctryRecCde", "ctryRecCde");
        filter.put("grpMembrRecCde", "grpMembrRecCde");
        Mockito.when(environment.getVariables().get("filter")).thenReturn(map);
        List<GroupItem> groupItemList = groupByProductStatusFetcher.get(environment);
        Assert.assertNotNull(groupItemList);
    }
}
