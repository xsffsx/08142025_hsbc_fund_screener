package com.dummy.wpb.product.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.jayway.jsonpath.JsonPath;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;

public class DefaultProductFormatServiceTest {
    private Document product;

    private GraphQLService graphQLService = mock(GraphQLService.class);
    private DefaultProductFormatService productFormatService = new DefaultProductFormatService(graphQLService);

    @Before
    public void setUp() throws Exception {
        product = Document.parse(CommonUtils.readResource("/test/UT-50108709.json"));

        Map<String, String> lastDealDt = new ImmutableMap.Builder<String, String>()
                .put("fieldCde", "lastDealDt")
                .put("jsonPath", "utTrstInstm.lastDealDt")
                .put("fieldDataTypeText", "Date")
                .build();
        Map<String, String> knockInDate = new ImmutableMap.Builder<String, String>()
                .put("fieldCde", "knockInDate")
                .put("jsonPath", "eqtyLinkInvst.knockInDate")
                .put("fieldDataTypeText", "Timestamp")
                .build();
        Mockito.when(graphQLService.executeRequest(argThat(argument -> argument != null && argument.getQuery().contains("configurationByFilter")), any(TypeReference.class)))
                .thenReturn(Collections.singletonMap("ext", Arrays.asList(lastDealDt, knockInDate)));


        Mockito.when(graphQLService.executeRequest(argThat(argument -> argument != null && argument.getQuery().contains("graphQLTypeSchema")), any(TypeReference.class)))
                .thenReturn(Arrays.asList(
                        Collections.singletonMap("name", "prodName"),
                        Collections.singletonMap("name", "prodStatCde"),
                        Collections.singletonMap("name", "utTrstInstm.lastDealDt"),
                        Collections.singletonMap("name", "eqtyLinkInvst.knockInDate")
                ));

        productFormatService.init();
    }

    @Test
    public void testInitProductInputFields() {
        Document sourceProd = new Document();
        sourceProd.put("prodName", "sample product");
        sourceProd.put("prodStatCde", "D");
        sourceProd.put("nonexistent", "");

        LocalDateTime knockInDate = LocalDateTime.now();
        sourceProd.put("ext", Collections.singletonList(new Document("fieldCde", "knockInDate").append("fieldValue", knockInDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))));
        Document importedProd = productFormatService.initProduct(sourceProd);

        DateTimeFormatter defaultDateTimeFormat = (DateTimeFormatter) ReflectionTestUtils.getField(DefaultProductFormatService.class, "defaultDateTimeFormat");
        Assertions.assertFalse(importedProd.containsKey("nonexistent"));
        Assertions.assertEquals(defaultDateTimeFormat.format(knockInDate), defaultDateTimeFormat.format(JsonPathUtils.readValue(importedProd, "eqtyLinkInvst.knockInDate")));
    }

    @Test
    public void testFormatByUpdateAttrs() {
        Document importedProduct = new Document();
        importedProduct.put(Field.prodTypeCde, "SEC");
        List<Document> altIds = new ArrayList<>();

        Document yCode = new Document();
        yCode.put(Field.prodCdeAltClassCde, "Y");
        yCode.put(Field.prodAltNum, "Y-001");

        Document qCode = new Document();
        qCode.put(Field.prodCdeAltClassCde, "Q");
        qCode.put(Field.prodAltNum, "Q-001");

        altIds.add(yCode);
        altIds.add(qCode);

        String pCodeStr = JsonPath.read(product, "altId[?(@.prodCdeAltClassCde=='P')].prodAltNum").toString();

        importedProduct.put(Field.altId, altIds);
        productFormatService.formatByUpdateAttrs(importedProduct, product, Arrays.asList("altId", "prodTypeCde"));
        Assert.assertEquals(importedProduct.getString(Field.prodTypeCde), product.getString(Field.prodTypeCde));

        Assert.assertEquals("[\"Q-001\"]", JsonPath.read(product, "altId[?(@.prodCdeAltClassCde=='Q')].prodAltNum").toString());
        Assert.assertEquals(pCodeStr, JsonPath.read(product, "altId[?(@.prodCdeAltClassCde=='P')].prodAltNum").toString());
    }
}
