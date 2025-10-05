package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.productConfig;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.ProdTypeCde;
import com.dummy.wmd.wpc.graphql.model.UnderlyingConfig;
import com.dummy.wmd.wpc.graphql.service.ProductService;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ProdUndlCdeValidatorTest {
    private ProductService productService = Mockito.mock(ProductService.class);
    private productConfig productConfig = Mockito.mock(productConfig.class);

    private ProdUndlCdeValidator prodUndlCdeValidator;
    private Document product;

    @Before
    public void setup() {
        product = Document.parse(CommonUtils.readResource("/data/undl-test.json"));
        UnderlyingConfig wrts = new UnderlyingConfig();
        wrts.setProdTypeCde("WRTS");
        wrts.setPath("stockInstm.undlStock");
        wrts.setAllowTypes(Collections.singletonList("SEC"));
        when(productConfig.getUnderlying()).thenReturn(Collections.singletonList(wrts));
        prodUndlCdeValidator = new ProdUndlCdeValidator(productService, productConfig);
    }

    @Test
    public void testValidateStockInstm_StockInstmNull() {
        List<Error> errors = prodUndlCdeValidator.validate(null, product);
        assertEquals("instmUndlCde@notFound", errors.get(0).getCode());
    }

    @Test
    public void testValidateStockInstm_StockInstm_notAllowType() {
        List<Document> mockProducts = createMockProducts("00066", ProdTypeCde.WARRANT);
        Mockito.when(productService.getProductsByFilter(Mockito.any())).thenReturn(mockProducts);
        List<Error> errors = prodUndlCdeValidator.validate(null, product);
        assertEquals("instmUndlCde@notAllowType", errors.get(0).getCode());
    }


    @Test
    public void testValidateStockInstm_StockInstm_success() {
        List<Document> mockProducts = createMockProducts("00066", ProdTypeCde.SECURITY);
        Mockito.when(productService.getProductsByFilter(Mockito.any())).thenReturn(mockProducts);
        List<Error> errors = prodUndlCdeValidator.validate(null, product);
        assertEquals(0, errors.size());
    }

    @Test
    public void test_err_path() {
        UnderlyingConfig wrts = new UnderlyingConfig();
        wrts.setPath("err.undlStock");
        wrts.setProdTypeCde(ProdTypeCde.WARRANT);
        when(productConfig.getUnderlying()).thenReturn(Collections.singletonList(wrts));
        prodUndlCdeValidator = new ProdUndlCdeValidator(productService, productConfig);
        List<Error> errors = prodUndlCdeValidator.validate(null, product);
        assertEquals(0, errors.size());
    }
    @Test
    public void test_noTypecde() {
        UnderlyingConfig wrts = new UnderlyingConfig();
        when(productConfig.getUnderlying()).thenReturn(Collections.singletonList(wrts));
        prodUndlCdeValidator = new ProdUndlCdeValidator(productService, productConfig);
        prodUndlCdeValidator.interestJsonPaths();
        List<Error> errors = prodUndlCdeValidator.validate(null, product);
        assertEquals(0, errors.size());
    }

    private List<Document> createMockProducts(String prodAltPrimNum, String prodTypeCde) {
        List<Document> mockProducts = new ArrayList<>();
        Document mockProduct = new Document();
        mockProduct.put(Field.prodAltPrimNum, prodAltPrimNum);
        mockProduct.put(Field._id,50005711);
        mockProduct.put(Field.prodTypeCde, prodTypeCde);
        mockProducts.add(mockProduct);
        return mockProducts;
    }
}