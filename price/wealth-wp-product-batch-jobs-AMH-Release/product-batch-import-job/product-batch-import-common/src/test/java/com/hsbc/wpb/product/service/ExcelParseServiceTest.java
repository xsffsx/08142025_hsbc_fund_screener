package com.dummy.wpb.product.service;

import com.dummy.wpb.product.model.ExcelColumnInfo;
import org.bson.Document;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class ExcelParseServiceTest {

    private ExcelParseService excelParseService = new ExcelParseService();


    @Test
    public void testParseExcelObject_blankExcelColumn() {
        ExcelColumnInfo excelColumnInfo = new ExcelColumnInfo();
        Assert.assertNotNull(excelParseService.parseExcelObject(Arrays.asList(excelColumnInfo)));
    }

    @Test
    public void testParseExcelObject() {
        List<ExcelColumnInfo> excelColumnInfoList = buildExcelList();
        Document document = excelParseService.parseExcelObject(excelColumnInfoList);
        Assert.assertEquals("UT", document.get("prodTypeCde"));
    }

    private List<ExcelColumnInfo> buildExcelList() {
        ExcelColumnInfo excelColumnInfo1 = new ExcelColumnInfo();
        excelColumnInfo1.setJsonPath("prodStatCde");
        ExcelColumnInfo excelColumnInfo2 = new ExcelColumnInfo();
        excelColumnInfo2.setJsonPath("prodTypeCde");
        excelColumnInfo2.setValue("UT");
        excelColumnInfo2.setDataType("String");
        ExcelColumnInfo excelColumnInfo3 = new ExcelColumnInfo();
        excelColumnInfo3.setJsonPath("prdInvstTnorNum");
        excelColumnInfo3.setValue("0");
        excelColumnInfo3.setDataType("Long");
        ExcelColumnInfo excelColumnInfo4 = new ExcelColumnInfo();
        excelColumnInfo4.setJsonPath("prdRtrnAvgNum");
        excelColumnInfo4.setValue("0.0");
        excelColumnInfo4.setDataType("Float");
        ExcelColumnInfo excelColumnInfo5 = new ExcelColumnInfo();
        excelColumnInfo5.setJsonPath("prodIssueCrosReferDt");
        excelColumnInfo5.setValue("2023-01-23");
        excelColumnInfo5.setDataType("Date");
        excelColumnInfo5.setDateFormat("yyyy-MM-dd");
        ExcelColumnInfo excelColumnInfo6 = new ExcelColumnInfo();
        excelColumnInfo6.setJsonPath("prodStatUpdtDtTm");
        excelColumnInfo6.setValue("2023-01-23 22:03:04");
        excelColumnInfo6.setDataType("DateTime");
        excelColumnInfo6.setDateFormat("yyyy-MM-dd HH:mm:ss");
        ExcelColumnInfo excelColumnInfo7 = new ExcelColumnInfo();
        excelColumnInfo7.setJsonPath("dtTm");
        excelColumnInfo7.setValue("3000");
        excelColumnInfo7.setDataType("Time");
        ExcelColumnInfo excelColumnInfo8 = new ExcelColumnInfo();
        excelColumnInfo8.setJsonPath("busEndTm");
        excelColumnInfo8.setValue("22:03:04");
        excelColumnInfo8.setDataType("LocalTime");
        excelColumnInfo8.setDateFormat("HH:mm:ss");
        ExcelColumnInfo excelColumnInfo9 = new ExcelColumnInfo();
        excelColumnInfo9.setJsonPath("ctryProdTradeCde");
        excelColumnInfo9.setValue("HK");
        excelColumnInfo9.setDataType("[String]");
        ExcelColumnInfo excelColumnInfo10 = new ExcelColumnInfo();
        excelColumnInfo10.setJsonPath("volatility");
        excelColumnInfo10.setValue("0.333333333333333333333333333333333333333");
        excelColumnInfo10.setDataType("BigDecimal");
        return Arrays.asList(excelColumnInfo1, excelColumnInfo2, excelColumnInfo3, excelColumnInfo4, excelColumnInfo5, excelColumnInfo6,
                excelColumnInfo7,excelColumnInfo8,excelColumnInfo9,excelColumnInfo10);
    }
}