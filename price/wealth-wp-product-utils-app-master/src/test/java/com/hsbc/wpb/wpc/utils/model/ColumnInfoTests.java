package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ColumnInfoTests {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void propertyTest_DoesNotThrow() {
        try {
            PropertyUtils.setProperty(new ColumnInfo());
        } catch (ClassNotFoundException e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetGraphQLTypeName_noArgs_returnsString() {
        ColumnInfo columnInfo1 = new ColumnInfo();
        columnInfo1.setType("NUMBER");
        columnInfo1.setScale(1);
        String graphQLTypeName1 = columnInfo1.getGraphQLTypeName();
        Assert.assertEquals("Float", graphQLTypeName1);
        ColumnInfo columnInfo2 = new ColumnInfo();
        columnInfo2.setType("NUMBER");
        columnInfo2.setScale(0);
        String graphQLTypeName2 = columnInfo2.getGraphQLTypeName();
        Assert.assertEquals("Long", graphQLTypeName2);
    }

    @Test
    public void getAttrName() {
        ColumnInfo columnInfo = new ColumnInfo("name", "type", 1, 1, true, "comment");
        String attrName = columnInfo.getAttrName();
        Assert.assertNotNull(attrName);
    }
}