package com.dummy.wpb.product.listener;

import com.dummy.wpb.product.configuration.SystemDefaultValuesHolder;
import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductFormatService;
import com.dummy.wpb.product.service.impl.DefaultProductFormatService;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonPathUtils;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static com.dummy.wpb.product.constant.BatchConstants.INDICATOR_YES;
import static org.mockito.ArgumentMatchers.any;

public class ProductFormatProcessorListenerTest {

    ProductFormatProcessorListener formatListener;

    SystemUpdateConfigHolder updateConfigHolder = Mockito.mock(SystemUpdateConfigHolder.class);

    SystemDefaultValuesHolder defaultValuesHolder = Mockito.mock(SystemDefaultValuesHolder.class);

    ProductFormatService productFormatService = Mockito.mock(DefaultProductFormatService.class);

    Document product;

    @Before
    public void setUp() throws Exception {
        formatListener = new ProductFormatProcessorListener();
        product = Document.parse(CommonUtils.readResource("/test/UT-50108709.json"));
        ReflectionTestUtils.setField(formatListener, "updateConfigHolder", updateConfigHolder);
        ReflectionTestUtils.setField(formatListener, "defaultValuesHolder", defaultValuesHolder);
        ReflectionTestUtils.setField(formatListener, "productFormatService", productFormatService);

    }

    @Test
    public void testCreateProduct_givenRBTBOND() {
        ReflectionTestUtils.setField(formatListener, "systemCde", "RBT");
        ProductStreamItem streamItem = new ProductStreamItem();
        streamItem.putParam("fileName", "HK_HBAP_RBT_BOND_20221226081122.xml");
        Document importProduct = new Document();
        importProduct.put(Field.prodName, "abc");
        importProduct.put(Field.prodSubtpCde, "CD");
        importProduct.put(Field.prodStatCde, "T");
        importProduct.put(Field.prodTypeCde, "BOND");
        importProduct.put(Field.prodAltPrimNum, "BOND001");
        streamItem.setActionCode(BatchImportAction.ADD);
        streamItem.setImportProduct(importProduct);
        Mockito.when(productFormatService.initProduct(any())).thenReturn(importProduct);

        ReflectionTestUtils.setField(formatListener, "createEligAllowCde", Collections.singletonList("RBT"));
        Mockito.when(defaultValuesHolder.getDefaultValues(any(), any())).thenReturn(
                new Document().append("prodLocCde", "MSC")
                        .append("buyRestrictChannel", "SRBPI")
                        .append("tradeElig.ageAllowTrdMinNum", 18)
                        .append("restrCustCtry[0].restrCtryTypeCde", "R")
                        .append("restrCustCtry[0].ctryIsoCde", "US"));

        formatListener.putDefaultValueCondition("buyRestrictChannel", (product) -> Objects.equals("CD", product.get(Field.prodSubtpCde)));

        formatListener.beforeProcess(streamItem);
        formatListener.afterProcess(streamItem, streamItem);

        Document updatedProduct = streamItem.getUpdatedProduct();
        Assert.assertNotNull(updatedProduct.get("tradeElig"));
        Assert.assertNotNull(updatedProduct.get("restrCustCtry"));
        Assert.assertNotNull(updatedProduct.get("buyRestrictChannel"));
        Assert.assertEquals("MSC", updatedProduct.get("prodLocCde"));
    }


    @Test
    public void testCreateProduct_givenGSOPSSEC() {
        ReflectionTestUtils.setField(formatListener, "systemCde", "AMHGSOPS.AS");
        ProductStreamItem streamItem = new ProductStreamItem();
        streamItem.putParam("fileName", "HK_HBAP_AMHGSOPS.AS_SEC~CN_20221226081122.xml");
        Document importProduct = new Document();
        importProduct.put(Field.prodName, "abc");
        importProduct.put(Field.prodShrtName, "abc");
        importProduct.put(Field.prodSubtpCde, "AB");
        importProduct.put(Field.prodStatCde, "T");
        importProduct.put(Field.prodTypeCde, "SEC");
        importProduct.put(Field.prodAltPrimNum, "SEC001");
        streamItem.setActionCode(BatchImportAction.ADD);
        streamItem.setImportProduct(importProduct);
        Mockito.when(productFormatService.initProduct(any())).thenReturn(importProduct);
        formatListener.putDefaultValueCondition("buyRestrictChannel", (product) -> Objects.equals("CD", product.get(Field.prodSubtpCde)));

        ReflectionTestUtils.setField(formatListener, "createEligAllowCde", Collections.emptyList());
        Mockito.when(defaultValuesHolder.getDefaultValues(any(), any())).thenReturn(
                new Document().append("prodLocCde", "MSC")
                        .append("buyRestrictChannel", "SRBPI")
                        .append("stockInstm.suptAtmcTrdInd", "N"));


        formatListener.beforeProcess(streamItem);
        formatListener.afterProcess(streamItem, streamItem);

        Document updatedProduct = streamItem.getUpdatedProduct();
        Assert.assertNull(updatedProduct.get("tradeElig"));
        Assert.assertEquals("N", JsonPathUtils.readValue(updatedProduct, "stockInstm.suptAtmcTrdInd"));
    }

    @Test
    public void testUpdateProduct_givenGSOPSWRTS() {
        ReflectionTestUtils.setField(formatListener, "createEligAllowCde", Collections.emptyList());
        ReflectionTestUtils.setField(formatListener, "systemCde", "AMHCUTAS");
        ProductStreamItem streamItem = new ProductStreamItem();
        streamItem.putParam("fileName", "HK_HBAP_AMHGSOPS.AS_SEC_20221226081122.xml");
        Document importProduct = Document.parse(CommonUtils.readResource("/test/UT-50108709.json"));
        importProduct.put(Field.prodName, "newName");
        importProduct.put(Field.prodShrtName, "newShrtName");
        importProduct.put(Field.prodTypeCde, "WRTS");
        importProduct.put(Field.manOverAssClaInd, INDICATOR_YES);
        streamItem.setActionCode(BatchImportAction.UPDATE);
        streamItem.setImportProduct(importProduct);
        streamItem.setOriginalProduct(new Document());
        Mockito.doCallRealMethod().when(productFormatService).formatByUpdateAttrs(any(), any(), any());

        Mockito.when(updateConfigHolder.getUpdateAttrs(any(), any(), any())).thenReturn(Arrays.asList("prodName", "prodShrtName"));
        formatListener.beforeProcess(streamItem);
        formatListener.afterProcess(streamItem, streamItem);

        Document updatedProduct = streamItem.getUpdatedProduct();

        Assert.assertEquals(importProduct.get("prodName"), updatedProduct.get("prodName"));
        Assert.assertEquals(importProduct.get("prodShrtName"), updatedProduct.get("prodShrtName"));
    }


    @Test
    public void testUpdateProduct_givenAMUTASUT() {
        ReflectionTestUtils.setField(formatListener, "createEligAllowCde", Collections.emptyList());
        ReflectionTestUtils.setField(formatListener, "systemCde", "AMHCUTAS");
        ProductStreamItem streamItem = new ProductStreamItem();
        streamItem.putParam("fileName", "HK_HBAP_AMHCUTAS_UT_20221226081122.xml");
        Document importProduct = Document.parse(CommonUtils.readResource("/test/UT-50108709.json"));
        importProduct.put(Field.prodName, "newName");
        importProduct.put(Field.prodTypeCde, "UT");
        importProduct.put(Field.prodSubtpCde, null);
        JsonPathUtils.setValue(importProduct, "utTrstInstm.invstIncrmMinAmt", 9999.1D);
        streamItem.setActionCode(BatchImportAction.UPDATE);
        streamItem.setImportProduct(importProduct);
        streamItem.setOriginalProduct(Document.parse(CommonUtils.readResource("/test/UT-50108709.json")));
        Mockito.doCallRealMethod().when(productFormatService).formatByUpdateAttrs(any(), any(), any());

        Mockito.when(updateConfigHolder.getUpdateAttrs(any(), any(), any())).thenReturn(Arrays.asList("prodName", "utTrstInstm.invstIncrmMinAmt"));
        formatListener.beforeProcess(streamItem);
        formatListener.afterProcess(streamItem, streamItem);

        Document updatedProduct = streamItem.getUpdatedProduct();

        Assert.assertEquals(importProduct.get("prodName"), updatedProduct.get("prodName"));
        Assert.assertEquals((Object) JsonPathUtils.readValue(importProduct, "utTrstInstm.invstIncrmMinAmt"), JsonPathUtils.readValue(updatedProduct, "utTrstInstm.invstIncrmMinAmt"));
    }
}
