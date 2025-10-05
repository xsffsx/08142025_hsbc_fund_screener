package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.Operation;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.service.ConfigurationService;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.com.google.common.collect.Lists;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ProdCutoffTimeListenerTest {

    private MongoDatabase mongoDatabase = Mockito.mock(MongoDatabase.class);

    private ConfigurationService configurationService;

    private ProdCutoffTimeListener prodCutoffTimeListener;

    @Before
    public void setUp() {
        Document tableColumnsMapping = CommonUtils.readResourceAsDocument("/files/table-columns-mapping.json");

        MongoCollection<Document> colConfiguration = Mockito.mock(MongoCollection.class);
        FindIterable<Document> findIterable = Mockito.mock(FindIterable.class);
        Mockito.when(findIterable.into(any())).thenReturn(Lists.newArrayList(tableColumnsMapping));
        Mockito.when(colConfiguration.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(mongoDatabase.getCollection(CollectionName.configuration.name())).thenReturn(colConfiguration);

        configurationService = new ConfigurationService(mongoDatabase);
        prodCutoffTimeListener = new ProdCutoffTimeListener(configurationService);
    }

    @Test
    public void testBeforeInsert_givenDocumentAndOperations_returnSuccessResult() {
        Document product = new Document();
        int fullGroupSize = configurationService.getInterestedGroupFullMap()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet())
                .size();
        prodCutoffTimeListener.beforeInsert(product);
        Assert.assertEquals(fullGroupSize, product.getList(Field.fieldGroupCtoff, Map.class).size());
    }

    @Test
    public void testBeforeUpdate_givenDocumentAndOperations_returnSuccessResult() {
        Document product = CommonUtils.readResourceAsDocument("/files/BOND-286859053.json");

        List<Map<String, Object>> fieldGroupCtoffList = product.get(Field.fieldGroupCtoff, new ArrayList<>());

        fieldGroupCtoffList.removeIf(fieldGroupCtoff -> "product".equals(fieldGroupCtoff.get(Field.fieldGroupCde)));

        Date currentDate = new Date();
        prodCutoffTimeListener.beforeUpdate(product, Lists.newArrayList(
                new OperationInput(Operation.put, "$." + Field.prodBidPrcAmt, 1.456d),
                new OperationInput(Operation.put, "debtInstm.bondIndusGrp", "ABC")
        ));

        Map<String, List<String>> interestedGroupFullMap = configurationService.getInterestedGroupFullMap();
        Set<String> expectedGroups = new HashSet<>();
        expectedGroups.addAll(interestedGroupFullMap.get(Field.prodBidPrcAmt));
        expectedGroups.addAll(interestedGroupFullMap.get("debtInstm.bondIndusGrp"));

        for (String interestedGroup : expectedGroups) {
            Map<String, Object> fieldGroupCtoff = fieldGroupCtoffList.stream()
                    .filter(item -> interestedGroup.equals(item.get(Field.fieldGroupCde)))
                    .findFirst()
                    .orElse(null);
            Assert.assertNotNull(fieldGroupCtoff);
            Assert.assertTrue(currentDate.compareTo((Date) fieldGroupCtoff.get(Field.fieldGroupCtoffDtTm)) < 0);
        }
    }
}
