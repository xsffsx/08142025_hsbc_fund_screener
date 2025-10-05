package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Test;

public class ProductTableInfoTests {

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(new ProductTableInfo());
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetParent_noArgs_returnString() {
        ProductTableInfo productTableInfo = new ProductTableInfo();
        String parent1 = productTableInfo.getParent();
        Assert.assertNull(parent1);
        productTableInfo.setToAttribute("attr");
        productTableInfo.setImportMethod("List");
        String parent2 = productTableInfo.getParent();
        Assert.assertNotNull(parent2);
        productTableInfo.setImportMethod("List1");
        String parent3 = productTableInfo.getParent();
        Assert.assertNotNull(parent3);
    }
}
