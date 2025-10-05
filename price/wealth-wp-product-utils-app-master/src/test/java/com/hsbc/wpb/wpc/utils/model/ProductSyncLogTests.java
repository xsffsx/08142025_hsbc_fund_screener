package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductSyncLogTests {

    @Mock
    private ProductEventGroup mockGroup;

    private ProductSyncLog productSyncLogUnderTest;

    @Before
    public void setUp() throws Exception {
        when(mockGroup.getEventStartTime()).thenReturn(new Date(System.currentTimeMillis()));
        when(mockGroup.getEventEndTime()).thenReturn(new Date(System.currentTimeMillis()));
        when(mockGroup.getEventCount()).thenReturn(1);
        when(mockGroup.getProdIdList()).thenReturn(Arrays.asList(1L));
        productSyncLogUnderTest = new ProductSyncLog(mockGroup);
    }

    @Test
    public void testSetProdIdList() {
        productSyncLogUnderTest.setProdIdList(Arrays.asList(0L));
        Assert.assertNotNull(mockGroup);
    }

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(productSyncLogUnderTest);
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

}
