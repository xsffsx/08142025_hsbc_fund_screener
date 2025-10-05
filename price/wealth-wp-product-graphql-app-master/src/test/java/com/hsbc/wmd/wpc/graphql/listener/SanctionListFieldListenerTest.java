package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.productConfig;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.UnderlyingConfig;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.util.Lists;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.dummy.wmd.wpc.graphql.constant.ProdTypeCde.WARRANT;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class SanctionListFieldListenerTest {

    MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);

    productConfig productConfig = Mockito.mock(productConfig.class);

    SanctionListFieldListener listener;

    @Before
    public void before() {
        // Initialize the supportTypes and interestJsonPaths with sample data
        UnderlyingConfig eli = new UnderlyingConfig();
        eli.setProdTypeCde("ELI");
        eli.setAllowTypes(Arrays.asList("ELI","SEC"));
        eli.setPath("eqtyLinkInvst.undlStock");
        when(productConfig.getUnderlying()).thenReturn(Collections.singletonList(
                eli)
        );

        listener = new SanctionListFieldListener(mongoTemplate, productConfig);
    }

    @Test
    public void testBeforeUpdate_givenSuccessResult() {
        Document product = Document.parse(CommonUtils.readResource("/data/eli-test.json"));

        List<String> sanctionBuyList = Lists.newArrayList("CAP", "UKP");
        List<String> sanctionSellList = Lists.newArrayList("CAP", "SGP");
        Document result = new Document(Field.sanctionBuyList, sanctionBuyList).append(Field.sanctionSellList, sanctionSellList);
        AggregationResults<Document> aggregationResults = new AggregationResults(Collections.singletonList(result), result);
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), eq(Document.class))).thenReturn(aggregationResults);
        listener.beforeUpdate(product, Collections.emptyList());
        Assertions.assertEquals(sanctionBuyList, product.get(Field.sanctionBuyList));
        Assertions.assertEquals(sanctionSellList, product.get(Field.sanctionSellList));
    }

    @Test
    public void testCalculate_givenUtProduct() {
        Document product = new Document(Field.prodTypeCde, "UT");
        listener.beforeUpdate(product, Collections.emptyList());
        Assertions.assertNull(product.get(Field.sanctionSellList));
    }

    @Test
    public void testCalculate_givenEmptyUndlProduct_wrts() {
        UnderlyingConfig wrts = new UnderlyingConfig();
        wrts.setProdTypeCde(WARRANT);
        wrts.setAllowTypes(Collections.singletonList("SEC"));
        wrts.setPath("stockInstm.undlStock");
        when(productConfig.getUnderlying()).thenReturn(Collections.singletonList(
                wrts)
        );
        listener = new SanctionListFieldListener(mongoTemplate, productConfig);
        Document product = Document.parse(CommonUtils.readResource("/data/undl-test.json"));
        JsonPath.parse(product).set("stockInstm.undlStock",Collections.singletonList(new Object()));
        listener.beforeUpdate(product, Collections.emptyList());
        Assertions.assertEquals(Collections.singletonList("UKP"), product.get(Field.sanctionSellList));
    }

    @Test
    public void testCalculate_givenEmptyUndlProduct() {
        Document product = Document.parse(CommonUtils.readResource("/data/eli-test.json"));
        JsonPath.parse(product).delete("eqtyLinkInvst.undlStock");
        listener.beforeUpdate(product, Collections.emptyList());
        Assertions.assertNull(product.get(Field.sanctionSellList));
    }

    @Test
    public void testCalculate_givenEmptyResult() {
        listener.interestJsonPaths();
        Document product = Document.parse(CommonUtils.readResource("/data/eli-test.json"));
        AggregationResults<Document> aggregationResults = new AggregationResults(Collections.emptyList(), new Document());
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), eq(Document.class))).thenReturn(aggregationResults);
        listener.beforeInsert(product);
        Assertions.assertNull(product.get(Field.sanctionSellList));
    }
}
