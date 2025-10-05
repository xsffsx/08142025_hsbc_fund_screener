package com.dummy.wmd.wpc.graphql.listener;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseChangeListenerTests {

    private BaseChangeListener baseChangeListener = new BondFieldsPreSetListener();

    @Test
    public void testGetOrder_NoArgs_returnsInt() {
        int num = baseChangeListener.getOrder();
        Assert.assertEquals(0, num);
    }

    @Test
    public void testBeforeInsert_givenDocument_DoesNotThrow() {
        try {
            baseChangeListener.beforeInsert(new Document());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testBeforeUpdate_givenDocumentAndList_DoesNotThrow() {
        try {
            baseChangeListener.beforeUpdate(new Document(), new ArrayList<>());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testAfterInsert_givenDocument_DoesNotThrow() {
        try {
            baseChangeListener.afterInsert(new Document());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testBeforeValidation_givenMapAndMap_DoesNotThrow() {
        try {
            baseChangeListener.beforeValidation(new HashMap<>(), new HashMap<>());
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
