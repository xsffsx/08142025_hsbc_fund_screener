package com.dummy.wpb.product.util;

import com.dummy.wpb.product.model.ProductKey;
import com.dummy.wpb.product.utils.CommonUtils;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Arrays;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProductKeyUtilsTest {

    static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    @Before
    public void setUp() {
        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
        legacyConfigMockedStatic.when(() -> LegacyConfig.getList(anyString())).thenReturn(Collections.emptyList());
        ReflectionTestUtils.setField(ProductKeyUtils.class, "ignoreStatFilterList", Arrays.asList("D", "P"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByProdCdeList", Collections.singletonList("W"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByUniqueList", Collections.singletonList(""));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByPrimaryList", Arrays.asList("P", "T"));
        ReflectionTestUtils.setField(ProductKeyUtils.class, "searchByAllList", Collections.singletonList("M"));

    }

    @After
    public void after() {
        legacyConfigMockedStatic.close();
    }

    @Test
    public void testProductKeyFilters() {
        ProductKey productKey1 = new ProductKey();
        productKey1.setProdCdeAltClassCde("W");
        productKey1.setProdAltNum("10000086");
        ProductKey productKey2 = new ProductKey();
        productKey2.setProdCdeAltClassCde("");
        productKey2.setProdAltNum("10000086");
        ProductKey productKey3 = new ProductKey();
        productKey3.setProdCdeAltClassCde("P");
        productKey3.setProdAltNum("U10000086");
        productKey3.setProdTypeCde("UT");
        ProductKey productKey4 = new ProductKey();
        productKey4.setProdCdeAltClassCde("M");
        productKey4.setProdAltNum("U10000086");
        productKey4.setProdTypeCde("UT");
        productKey4.setCtryProdTradeCde("HK");
        productKey4.setCcyProdCde("HKD");
        ProductKey productKey5 = new ProductKey();
        productKey5.setProdCdeAltClassCde("S");
        productKey5.setProdAltNum("U10000086");
        productKey5.setProdTypeCde("UT");
        productKey5.setCtryProdTradeCde("HK");
        productKey5.setCcyProdCde("HKD");
        Assert.assertNotNull(ProductKeyUtils.productKeyFilters(Arrays.asList(productKey1, productKey2, productKey3,
                productKey4, productKey5)));
    }

    @Test
    public void testAreEqual() {
        ProductKey productKey1 = new ProductKey();
        productKey1.setProdCdeAltClassCde("W");
        productKey1.setProdAltNum("10000086");
        ProductKey productKey2 = new ProductKey();
        productKey2.setProdCdeAltClassCde("");
        productKey2.setProdAltNum("10000086");
        Assert.assertFalse(ProductKeyUtils.areEqual(productKey1, productKey2));
        ProductKey productKey3 = new ProductKey();
        productKey3.setProdCdeAltClassCde("P");
        productKey3.setProdAltNum("U10000086");
        productKey3.setProdTypeCde("UT");
        ProductKey productKey4 = new ProductKey();
        productKey4.setProdCdeAltClassCde("P");
        productKey4.setProdAltNum("U10000086");
        productKey4.setProdTypeCde("UT");
        Assert.assertTrue(ProductKeyUtils.areEqual(productKey3, productKey4));
        ProductKey productKey5 = new ProductKey();
        productKey5.setProdCdeAltClassCde("M");
        productKey5.setProdAltNum("U10000086");
        productKey5.setProdTypeCde("UT");
        productKey5.setCtryProdTradeCde("HK");
        productKey5.setCcyProdCde("HKD");
        ProductKey productKey6 = new ProductKey();
        productKey6.setProdCdeAltClassCde("M");
        productKey6.setProdAltNum("U10000086");
        productKey6.setProdTypeCde("UT");
        productKey6.setCtryProdTradeCde("HK");
        productKey6.setCcyProdCde("HKD");
        Assert.assertTrue(ProductKeyUtils.areEqual(productKey5, productKey6));
        ProductKey productKey7 = new ProductKey();
        productKey7.setProdCdeAltClassCde("S");
        productKey7.setProdAltNum("U10000086");
        productKey7.setProdTypeCde("UT");
        productKey7.setCtryProdTradeCde("HK");
        ProductKey productKey8 = new ProductKey();
        productKey8.setProdCdeAltClassCde("S");
        productKey8.setProdAltNum("U10000086");
        productKey8.setProdTypeCde("UT");
        productKey8.setCtryProdTradeCde("HK");
        Assert.assertTrue(ProductKeyUtils.areEqual(productKey7, productKey8));
    }

    @Test
    public void testBuildProdKeyInfo() {
        String json = CommonUtils.readResource("/test/UT-50108709.json");
        Document product = Document.parse(json);
        Assert.assertNotNull(ProductKeyUtils.buildProdKeyInfo(product));
    }
}