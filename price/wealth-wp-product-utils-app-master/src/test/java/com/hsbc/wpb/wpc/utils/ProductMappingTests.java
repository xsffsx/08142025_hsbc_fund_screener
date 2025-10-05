package com.dummy.wpb.wpc.utils;

import com.dummy.wpb.wpc.utils.model.ProductTableInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ProductMappingTests {

    @Test
    public void testGetTableInfoListByImportMethod_givenString_returnsEmptyList() {
        List<ProductTableInfo> emptyList = ProductMapping.getTableInfoListByImportMethod("nonExistentMethod");
        Assert.assertNotNull(emptyList);
        Assert.assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testGetTableInfoList_noArgs_returnstableInfoList() {
        List<ProductTableInfo> tableInfoList = ProductMapping.getTableInfoList();
        Assert.assertNotNull(tableInfoList);
    }

    @Test
    public void testGetTableInfo_givenTableName_returnsProductTableInfo() {
        ProductTableInfo tb_prod = ProductMapping.getTableInfo("TB_PROD");
        Assert.assertNotNull(tb_prod);
    }

    @Test
    public void testIsProdUnrelatedTable_givenTableName_returnsBoolean() {
        boolean isTrue = ProductMapping.isProdUnrelatedTable("TB_PROD");
        Assert.assertFalse(isTrue);
    }

    @Test
    public void testGetImportMethod_givenNonExistentTableName_returnsFalse() {
        Boolean result = ProductMapping.getImportMethod("NON_EXISTENT_TABLE", "importMasterTable");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsProdUnrelatedTable_givenNonExistentTableName_returnsTrue() {
        boolean result = ProductMapping.isProdUnrelatedTable("NON_EXISTENT_TABLE");
        Assert.assertTrue(result);
    }

    @Test
    public void testGetTableInfoListByImportMethod_givenString_returnsImportMasterTableList() {
        List<ProductTableInfo> importMasterTable = ProductMapping.getTableInfoListByImportMethod("importMasterTable");
        Assert.assertNotNull(importMasterTable);
    }

    @Test
    public void testGetImportMethod_givenTableNameAndImportMethod_returnsBoolean() {
        Boolean isTrue = ProductMapping.getImportMethod("TB_PROD", "importMasterTable");
        Assert.assertTrue(isTrue);
        Boolean isFalse = ProductMapping.getImportMethod("TB_PROD", "importMasterTabl");
        Assert.assertFalse(isFalse);
    }

    @Test
    public void testGetToAttributeCamelCase_givenTableName_returnsString() {
        String camelCase = ProductMapping.getToAttributeCamelCase("TB_PROD");
        Assert.assertNull(camelCase);
        String camelCase2 = ProductMapping.getToAttributeCamelCase("TB_PRO");
        Assert.assertNull(camelCase2);
    }

    @Test
    public void testGetProductIdFieldCamelCase_givenTableName_returnsString() {
        String camelCase = ProductMapping.getProductIdFieldCamelCase("TB_PROD");
        Assert.assertNotNull(camelCase);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetProductIdFieldCamelCase_givenTableName_throwException() {
        ProductMapping.getProductIdFieldCamelCase("TB_PRO");
    }

    @Test
    public void testGetProductIdField_givenTableName_returnsString() {
        String tb_prod = ProductMapping.getProductIdField("TB_PROD");
        Assert.assertNotNull(tb_prod);
    }

    @Test
    public void testGetTableKeyFields_givenTableName_returnskeyFieldsList() {
        List<String> keyFields = ProductMapping.getTableKeyFields("TB_PROD");
        Assert.assertNotNull(keyFields);
    }

    @Test
    public void testGetTableList_noArgs_returnsTableList() {
        Object tableList = ProductMapping.getTableList();
        Assert.assertNotNull(tableList);
    }

}
