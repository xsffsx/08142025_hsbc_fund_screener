package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.dummy.wmd.wpc.graphql.constant.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class PreFillDataListenerTest {

    @InjectMocks
    private PreFillDataListener preFillDataListener;

    private Document product = new Document();

    @Test
    public void testBeforeInsert_InvalidProductPrice() {
        product.put(Field.ccyProdMktPrcCde,"CNY" );
        product.put(Field.prodBidPrcAmt,95.0002);
        product.put(Field.prcEffDt,new Date());
        product.put(Field.prodOffrPrcAmt,null);
        product.put(Field.prodMktPrcAmt,null);
        product.put(Field.prodNavPrcAmt,null);
        preFillDataListener.beforeInsert(product);
        Assert.assertNotNull(product.get(Field.prcInpDt));
        product = new Document();
        product.put(Field.ccyProdMktPrcCde,null);
        product.put(Field.prodBidPrcAmt,null);
        product.put(Field.prcEffDt,null);
        preFillDataListener.beforeInsert(product);
        Assert.assertNull(product.get(Field.prcInpDt));
    }

    @Test
    public void testGetOrder_NoArgs_DoesNotThrow() {
        int order = preFillDataListener.getOrder();
        Assert.assertEquals(1000, order);
    }

    @Test
    public void testBeforeValidation_givenMapAndMap_DoesNotThrow() {
        try {
            Document doc1 = CommonUtils.readResourceAsDocument("/files/BOND-286859053.json");
            Document doc2 = CommonUtils.readResourceAsDocument("/files/BOND-286859053.json");
            Document doc3 = CommonUtils.readResourceAsDocument("/files/BOND-286859053.json");
            doc2.replace("prodBidPrcAmt", 95.0002);
            doc3.replace("prcEffDt", LocalDate.of(2023, 8, 10));
            preFillDataListener.beforeValidation(doc1, doc1);
            preFillDataListener.beforeValidation(doc1, doc2);
            preFillDataListener.beforeValidation(doc1, doc3);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCheckEqualDate_givenDate1AndDate2_returnsBoolean() {
        try {
            Method checkEqualDate = preFillDataListener.getClass().getDeclaredMethod("checkEqualDate", LocalDate.class, LocalDate.class);
            checkEqualDate.setAccessible(true);
            boolean isTrue1 = (boolean)checkEqualDate.invoke(preFillDataListener, null, null);
            Assert.assertTrue(isTrue1);
            LocalDate localDate = LocalDate.of(2023, 8, 10);
            boolean isTrue2 = (boolean)checkEqualDate.invoke(preFillDataListener, localDate, localDate);
            Assert.assertTrue(isTrue2);
            boolean isTrue3 = (boolean)checkEqualDate.invoke(preFillDataListener, localDate, null);
            Assert.assertFalse(isTrue3);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCheckEqualPriceAmount_givenPrcAmt1AndPrcAmt2_returnsBoolean() {
        try {
            Method checkEqualPriceAmount = preFillDataListener.getClass().getDeclaredMethod("checkEqualPriceAmount", BigDecimal.class, BigDecimal.class);
            checkEqualPriceAmount.setAccessible(true);
            boolean isTrue1 = (boolean)checkEqualPriceAmount.invoke(preFillDataListener, null, null);
            Assert.assertTrue(isTrue1);
            BigDecimal bigDecimal = new BigDecimal("123.4");
            boolean isTrue2 = (boolean)checkEqualPriceAmount.invoke(preFillDataListener, bigDecimal, bigDecimal);
            Assert.assertTrue(isTrue2);
            boolean isTrue3 = (boolean)checkEqualPriceAmount.invoke(preFillDataListener, bigDecimal, null);
            Assert.assertFalse(isTrue3);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCheckProductStatus_givenOldProdAndNewProd_DoesNotThrow() {
        try {
            Method checkProductStatus = preFillDataListener.getClass().getDeclaredMethod("checkProductStatus", Map.class, Map.class);
            checkProductStatus.setAccessible(true);
            Map<String, Object> oldProd = new HashMap<>();
            oldProd.put("prodStatCde", "A");
            Map<String, Object> newProd = new HashMap<>();
            newProd.put("prodStatCde", "B");
            checkProductStatus.invoke(preFillDataListener, null, newProd);
            checkProductStatus.invoke(preFillDataListener, oldProd, newProd);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
