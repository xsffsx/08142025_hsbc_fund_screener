package com.dummy.wpb.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.graphql.*;
import com.dummy.wpb.product.service.impl.DefaultProductService;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.argThat;

public class ProductServiceTest {

    GraphQLService graphQLService = Mockito.mock(GraphQLService.class);

    DefaultProductService productService = new DefaultProductService(graphQLService);

    @Before
    public void setUp() throws Exception {
        List<Map<String, String>> productInputSchemas = Stream.of(Field.prodId, Field.prodName, Field.prodAltPrimNum, Field.prodStatCde,
                        Field.ctryRecCde, Field.grpMembrRecCde, Field.prodTypeCde, "altId[*].ctryRecCde", "utTrstInstm.fundRtainMinAmt")
                .map(name -> Collections.singletonMap("name", name))
                .collect(Collectors.toList());

        ArgumentMatcher<GraphQLRequest> schemaMatchers = request -> request.getDataPath().contains("graphQLTypeSchema");
        Mockito.when(graphQLService.executeRequest(argThat(schemaMatchers), Mockito.<TypeReference<List<Map<String, String>>>>any())).thenReturn(productInputSchemas);

        productService.init();
    }

    @Test
    public void testProductByFilters() throws Exception {
        productService.productByFilters(Collections.emptyMap());
        ArgumentCaptor<GraphQLRequest> graphQLRequestCaptor = ArgumentCaptor.forClass(GraphQLRequest.class);
        Mockito.verify(graphQLService, Mockito.times(2)).executeRequest(graphQLRequestCaptor.capture(), Mockito.<TypeReference<List<Document>>>any());
        GraphQLRequest request = graphQLRequestCaptor.getValue();
        Assert.assertTrue(request.getQuery().contains("utTrstInstm {fundRtainMinAmt"));
    }

    @Test
    public void testProductByFilters_withIgnoreFields() throws Exception {
        productService.productByFilters(Collections.emptyMap(), Collections.singletonList("utTrstInstm.fundRtainMinAmt"));
        ArgumentCaptor<GraphQLRequest> graphQLRequestCaptor = ArgumentCaptor.forClass(GraphQLRequest.class);
        Mockito.verify(graphQLService, Mockito.times(2)).executeRequest(graphQLRequestCaptor.capture(), Mockito.<TypeReference<List<Document>>>any());
        GraphQLRequest request = graphQLRequestCaptor.getValue();
        Assert.assertFalse(request.getQuery().contains("utTrstInstm {fundRtainMinAmt"));
    }

    @Test
    public void testBatchCreateProduct() throws Exception {
        Document product1 = new Document();
        product1.put(Field.prodId, 111111);
        product1.put(Field.prodAltPrimNum, "aaaaaa");

        Document product2 = new Document();
        product2.put(Field.prodId, 22222222);
        product2.put(Field.prodAltPrimNum, "bbbbbb");

        List<Map<String, ?>> createdProducts = Arrays.asList(product1, product2);

        ProductBatchCreateResult createResult = new ProductBatchCreateResult();
        createResult.setCreatedProducts(Arrays.asList(product1, product2));
        createResult.setInvalidProducts(Collections.emptyList());

        ArgumentMatcher<GraphQLRequest> matcher = request -> request.getDataPath().contains("productBatchCreate");
        Mockito.when(graphQLService.executeRequest(argThat(matcher), Mockito.<Class<ProductBatchCreateResult>>any())).thenReturn(createResult);

        productService.batchCreateProduct(createdProducts);

        ArgumentCaptor<GraphQLRequest> graphQLRequestCaptor = ArgumentCaptor.forClass(GraphQLRequest.class);
        Mockito.verify(graphQLService, Mockito.times(1)).executeRequest(graphQLRequestCaptor.capture(), Mockito.<Class<ProductBatchCreateResult>>any());
        Map<String, Object> variables = graphQLRequestCaptor.getValue().getVariables();
        Assert.assertEquals(variables.get("products"), createdProducts);
    }

    @Test
    public void testBatchUpdateProduct() throws Exception {
        Document product = new Document();
        product.put(Field.prodId, 111111);
        product.put(Field.prodAltPrimNum, "aaaaaa");
        product.put(Field.prodName, "testtest");

        ProductBatchUpdateInput updateInput = new ProductBatchUpdateInput();
        Map<String, Object> filter = Collections.singletonMap(Field._id, 123456);
        updateInput.setFilter(filter);
        Operation op = new Operation();
        op.setOp("put");
        op.setPath(Field.prodName);
        op.setValue("test");

        List<Operation> ops = Collections.singletonList(op);
        updateInput.setOperations(ops);

        ProductBatchUpdateResult updateResult = new ProductBatchUpdateResult();
        updateResult.setUpdatedProducts(Collections.singletonList(product));
        updateResult.setInvalidProducts(Collections.emptyList());
        ArgumentMatcher<GraphQLRequest> matcher = request -> request.getDataPath().contains("productBatchUpdate");
        Mockito.when(graphQLService.executeRequest(argThat(matcher), Mockito.<Class<ProductBatchUpdateResult>>any())).thenReturn(updateResult);

        productService.batchUpdateProduct(updateInput);

        ArgumentCaptor<GraphQLRequest> graphQLRequestCaptor = ArgumentCaptor.forClass(GraphQLRequest.class);
        Mockito.verify(graphQLService, Mockito.times(1)).executeRequest(graphQLRequestCaptor.capture(), Mockito.<Class<ProductBatchCreateResult>>any());
        Map<String, Object> variables = graphQLRequestCaptor.getValue().getVariables();
        Assert.assertEquals(variables.get("filter"), filter);

        ProductBatchUpdateByIdInput updateByIdInputInput = new ProductBatchUpdateByIdInput();
        updateByIdInputInput.setProdId(123456L);
        updateByIdInputInput.setOperations(ops);
        List<ProductBatchUpdateByIdInput> updateParams = Collections.singletonList(updateByIdInputInput);
        productService.batchUpdateProductById(updateParams);
        Mockito.verify(graphQLService, Mockito.times(2)).executeRequest(graphQLRequestCaptor.capture(), Mockito.<Class<ProductBatchCreateResult>>any());
        variables = graphQLRequestCaptor.getValue().getVariables();
        Assert.assertEquals(variables.get("updateParams"), updateParams);

    }
}
