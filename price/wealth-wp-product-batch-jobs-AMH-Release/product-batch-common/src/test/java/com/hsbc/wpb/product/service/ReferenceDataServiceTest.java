package com.dummy.wpb.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.graphql.*;
import com.dummy.wpb.product.service.impl.DefaultReferenceDataService;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.argThat;

public class ReferenceDataServiceTest {

    GraphQLService graphQLService = Mockito.mock(GraphQLService.class);

    ReferenceDataService referenceDataService = new DefaultReferenceDataService(graphQLService);

    List<ReferenceData> referenceDataList = null;

    @Before
    public void setUp() throws Exception {
        referenceDataList = JsonUtil.convertJson2Object(CommonUtils.readResource("/reference_data.json"), new TypeReference<List<ReferenceData>>() {
        });
    }

    @Test
    public void testReferenceDataByFilter() throws Exception {
        ArgumentMatcher<GraphQLRequest> schemaMatchers = request -> request.getDataPath().contains("referenceDataByFilter");
        Mockito.when(graphQLService.executeRequest(argThat(schemaMatchers), Mockito.<TypeReference<List<ReferenceData>>>any())).thenReturn(referenceDataList);
        referenceDataService.referenceDataByFilter(Collections.emptyMap());

        ArgumentCaptor<GraphQLRequest> graphQLRequestCaptor = ArgumentCaptor.forClass(GraphQLRequest.class);
        Mockito.verify(graphQLService, Mockito.times(1))
                .executeRequest(graphQLRequestCaptor.capture(), Mockito.<TypeReference<List<ReferenceData>>>any());
        GraphQLRequest request = graphQLRequestCaptor.getValue();
        Assert.assertTrue(request.getQuery().contains("referenceDataByFilter"));

    }

    @Test
    public void testCreateRefData() throws Exception {
        ReferenceDataBatchCreateResult createResult = new ReferenceDataBatchCreateResult();
        InvalidProductError error = new InvalidProductError();
        InvalidReferData invalidReferData = new InvalidReferData();
        error.setCode("duplicate");
        error.setJsonPath("cdvTypeCde");
        error.setMessage("cdvTypeCde is duplicate");
        invalidReferData.setErrors(Collections.singletonList(error));
        invalidReferData.setReferData(referenceDataList.get(0));
        createResult.setCreatedReferenceData(referenceDataList.subList(1, 2));
        createResult.setInvalidReferenceData(Collections.singletonList(invalidReferData));

        ArgumentMatcher<GraphQLRequest> matcher = request -> request.getDataPath().contains("referenceDataBatchCreate");
        Mockito.when(graphQLService.executeRequest(argThat(matcher), Mockito.<Class<ReferenceDataBatchCreateResult>>any())).thenReturn(createResult);

        referenceDataService.createRefData(referenceDataList);

        ArgumentCaptor<GraphQLRequest> graphQLRequestCaptor = ArgumentCaptor.forClass(GraphQLRequest.class);
        Mockito.verify(graphQLService, Mockito.times(1))
                .executeRequest(graphQLRequestCaptor.capture(), Mockito.<Class<ReferenceDataBatchCreateResult>>any());
        GraphQLRequest request = graphQLRequestCaptor.getValue();
        Map<String, Object> variables = request.getVariables();
        Assert.assertEquals(variables.get("refData"), referenceDataList);
    }
}
