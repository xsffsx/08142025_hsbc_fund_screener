package com.dummy.wpb.product.component;

import com.dummy.wpb.product.model.ProductTo;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.utils.CommonUtils;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImportEliFinDocServiceTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ImportEliFinDocService importEliFinDocService;

    static Document product;

    @BeforeAll
    public static void setUp() {
        product = Document.parse(CommonUtils.readResource("/product-import.json"));
    }

    @Test
    void testQeryProductByPriNum() throws Exception {
        ProductTo productTo = new ProductTo();
        Mockito.when(productService.productByFilters(Mockito.any())).thenReturn(Collections.emptyList());
        assertNull(importEliFinDocService.queryProductByPriNum(productTo));

        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Collections.singletonList(product));
        assertNotNull(importEliFinDocService.queryProductByPriNum(productTo));
    }

    @Test
    void testUpdateToDB() throws Exception {
        ProductBatchUpdateResult updateResult = Mockito.mock(ProductBatchUpdateResult.class);
        Mockito.when(productService.batchUpdateProduct(Mockito.any())).thenReturn(updateResult);

        Mockito.doNothing().when(updateResult).logUpdateResult(Mockito.any());
        importEliFinDocService.updateToDB(product, "APPENDIX", "test");
        assertTrue(true);
    }
}