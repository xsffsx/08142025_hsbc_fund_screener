package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.calc.CalculationManager;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.DiffType;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.jayway.jsonpath.JsonPath;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class DiffFromAmendmentFetcherTest {

    DiffFromAmendmentFetcher diffFromAmendmentFetcher = new DiffFromAmendmentFetcher();

    Document productAmendment = CommonUtils.readResourceAsDocument("/files/amendment-product-50504681.json");
    Document finDocAmendment = CommonUtils.readResourceAsDocument("/files/amendment-fin_doc_hist.json");

    DataFetchingEnvironment environment;

    CalculationManager calculationManager = Mockito.mock(CalculationManager.class);

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(diffFromAmendmentFetcher, "calculationManager", calculationManager);
    }

    @Test
    public void testGetDiff_givenAmendForProduct_returnDiff() throws Exception {
        environment = new DataFetchingEnvironmentImpl.Builder()
                .source(productAmendment)
                .build();

        Mockito.doAnswer((Answer<Void>) invocation -> {
            Document product = invocation.getArgument(0);
            product.put("asetClassCde", product.getString(Field.mktInvstCde) + JsonPath.read(product, "$.undlAset[0].asetUndlCde"));
            return null;
        }).when(calculationManager).updateCalculatedFields(any());

        List<DiffType> diffs = diffFromAmendmentFetcher.get(environment);

        Assert.assertFalse(diffs.isEmpty());
        Assert.assertTrue(diffs.stream().anyMatch(diff -> "$.asetClassCde".equals(diff.getPath())));
    }

    @Test
    public void testGetDiff_givenNullDocBase_returnEmptyDiff() throws Exception {
        environment = new DataFetchingEnvironmentImpl.Builder()
                .source(finDocAmendment)
                .build();
        Assert.assertTrue(diffFromAmendmentFetcher.get(environment).isEmpty());
    }
}
