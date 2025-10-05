package com.dummy.wpb.wpc.utils.constant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class OtherTypeTableEnumTests {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            OtherTypeTableEnum[] otherTypeTableEnums = OtherTypeTableEnum.values();
            String ignored = Arrays.toString(otherTypeTableEnums);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetCollectionName_givenTableName_returnsString() {
        String prod_type_fin_doc = OtherTypeTableEnum.getCollectionName("PROD_TYPE_FIN_DOC");
        Assert.assertNotNull(prod_type_fin_doc);
        String string = OtherTypeTableEnum.getCollectionName("PROD_TYPE_FIN_DO");
        Assert.assertNull(string);
    }
}
