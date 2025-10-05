package com.dummy.wpb.wpc.utils;

import oracle.sql.ROWID;
import oracle.sql.TIMESTAMP;
import oracle.xdb.XMLType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class DbUtilsTests {

    @Mock
    private ResultSet resultSet;
    @Mock
    private ResultSetMetaData metadata;
    @Mock
    private XMLType xmlType;

    @Test
    public void testRemoveFields_givenMapAndString_doesNotThrow() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("CTRY_REC_CDE", "GB");
            map.put("GRP_MEMBR_REC_CDE", "HBEU");
            map.put("TRADE_ELIG", "SECDF");
            map.put("FIELD_CDE", "accrPrdEndDt");
            map.put("FIELD_DATA_TYPE_TEXT", "String");
            map.put("FIELD_STRNG_VALUE_TEXT", "2020-08-10");
            DbUtils.removeFields(map, "TRADE_ELIG");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetStringObjectMap_givenResultSet_returnsMap() throws Exception {
        Mockito.when(resultSet.getMetaData()).thenReturn(metadata);
        Mockito.when(metadata.getColumnCount()).thenReturn(4);
        Mockito.when(metadata.getColumnLabel(anyInt())).thenReturn("columnLabel");
        Mockito.when(metadata.getScale(anyInt())).thenReturn(0);
        Mockito.when(resultSet.getObject(anyString())).thenReturn(new TIMESTAMP());
        DbUtils.getStringObjectMap(resultSet);
        Mockito.when(resultSet.getObject(anyString())).thenReturn(new Timestamp(System.currentTimeMillis()));
        DbUtils.getStringObjectMap(resultSet);
        Mockito.when(resultSet.getObject(anyString())).thenReturn(new BigDecimal("123.4"));
        DbUtils.getStringObjectMap(resultSet);
        Mockito.when(resultSet.getObject(anyString())).thenReturn(new ROWID());
        DbUtils.getStringObjectMap(resultSet);
        Mockito.when(resultSet.getObject(anyString())).thenReturn(xmlType);
        DbUtils.getStringObjectMap(resultSet);
        Mockito.when(metadata.getColumnLabel(1)).thenReturn("charColumn");
        Mockito.when(metadata.getColumnType(1)).thenReturn(java.sql.Types.CHAR);
        Mockito.when(resultSet.getObject("charColumn")).thenReturn("  charValue  ");

        // Mock ResultSet value
        Mockito.when(metadata.getColumnLabel(2)).thenReturn("unhandledColumn");
        Mockito.when(metadata.getColumnType(2)).thenReturn(java.sql.Types.VARCHAR);
        String unhandledValue = "unhandledValue";
        Mockito.when(resultSet.getObject("unhandledColumn")).thenReturn(unhandledValue);

        Mockito.when(metadata.getColumnLabel(3)).thenReturn("bigDecimalScale0");
        Mockito.when(metadata.getColumnLabel(4)).thenReturn("bigDecimalScaleNot0");
        Mockito.when(metadata.getScale(3)).thenReturn(0); // scale == 0
        Mockito.when(metadata.getScale(4)).thenReturn(2); // scale != 0
        Mockito.when(metadata.getColumnType(3)).thenReturn(java.sql.Types.NUMERIC);
        Mockito.when(metadata.getColumnType(4)).thenReturn(java.sql.Types.NUMERIC);
        Mockito.when(resultSet.getObject("bigDecimalScale0")).thenReturn(new BigDecimal("12345"));
        Mockito.when(resultSet.getObject("bigDecimalScaleNot0")).thenReturn(new BigDecimal("12345.67"));

        Map<String, Object> stringObjectMap = DbUtils.getStringObjectMap(resultSet);
        Assert.assertNotNull(stringObjectMap);
        Assert.assertEquals(12345L, stringObjectMap.get("bigDecimalScale0")); // longValue for scale == 0
        Assert.assertEquals(12345.67, stringObjectMap.get("bigDecimalScaleNot0")); // doubleValue for scale != 0
        Assert.assertEquals("charValue", stringObjectMap.get("charColumn"));
        Assert.assertEquals(unhandledValue, stringObjectMap.get("unhandledColumn"));
    }

    @Test
    public void testExtFieldToMap_givenListAndFieldList_returnsMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("CTRY_REC_CDE", "GB");
        map.put("GRP_MEMBR_REC_CDE", "HBEU");
        map.put("TRADE_ELIG", "SECDF");
        map.put("FIELD_CDE", "accrPrdEndDt");
        map.put("FIELD_DATA_TYPE_TEXT", "String");
        map.put("FIELD_STRNG_VALUE_TEXT", "2020-08-10");
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("FIELD_CDE", "accrPrdEndDt");
        map2.put("FIELD_DATA_TYPE_TEXT", "Char");
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("FIELD_CDE", "accrPrdEndDt");
        map3.put("FIELD_DATA_TYPE_TEXT", "Integer");
        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("FIELD_CDE", "accrPrdEndDt");
        map4.put("FIELD_DATA_TYPE_TEXT", "Decimal");
        HashMap<String, Object> map5 = new HashMap<>();
        map5.put("FIELD_CDE", "accrPrdEndDt");
        map5.put("FIELD_DATA_TYPE_TEXT", "Date");
        HashMap<String, Object> map6 = new HashMap<>();
        map6.put("FIELD_CDE", "accrPrdEndDt");
        map6.put("FIELD_DATA_TYPE_TEXT", "Timestamp");
        ArrayList<Map<String, Object>> mapList = new ArrayList<>();
        mapList.add(map);
        mapList.add(map2);
        mapList.add(map3);
        mapList.add(map4);
        mapList.add(map5);
        mapList.add(map6);
        List<String> fieldsList = Arrays.asList("FIELD_CDE", "FIELD_DATA_TYPE_TEXT", "FIELD_STRNG_VALUE_TEXT",
                "accrPrdEndDt", "accrPrdStartDt");
        Map<String, Object> stringObjectMap = DbUtils.extFieldToMap(mapList, fieldsList);
        Assert.assertNotNull(stringObjectMap);
    }

    @Test
    public void testToDate_givenString_returnsDate() throws Exception {

        DbUtils dbUtils = DbUtils.getInstance();
        Method toDate = dbUtils.getClass().getDeclaredMethod("toDate", String.class);
        toDate.setAccessible(true);
        try {
            toDate.invoke(dbUtils, "01  01 2020");
            toDate.invoke(dbUtils, "203-11-12");
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void testToDate_givenDate_returnsDate() {
        Date date = DbUtils.toUTCDate(new Date());
        Assert.assertNotNull(date);
    }

    @Test(expected = RuntimeException.class)
    public void testToDate_givenDate_throwException() {
        MockedConstruction<SimpleDateFormat> dateFormatMockedConstruction = null;
        try {
            dateFormatMockedConstruction = Mockito.mockConstruction(SimpleDateFormat.class, ((simpleDateFormat,
                    context) -> Mockito.when(simpleDateFormat.parse(any())).thenThrow(ParseException.class)));
            DbUtils.toUTCDate(new Date());
        } finally {
            dateFormatMockedConstruction.close();
        }
    }

}
