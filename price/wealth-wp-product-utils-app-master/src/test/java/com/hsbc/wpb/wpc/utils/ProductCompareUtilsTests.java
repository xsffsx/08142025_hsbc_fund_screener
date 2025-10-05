package com.dummy.wpb.wpc.utils;

import com.dummy.wpb.wpc.utils.model.Difference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

public class ProductCompareUtilsTests {

    private ProductCompareUtils productCompareUtils;

    @Before
    public void setUp() throws Exception {
        productCompareUtils = ProductCompareUtils.getInstance();
    }

    @Test
    public void testCompare_noArgs_returnsSet() {
        Set<Difference> compare = ProductCompareUtils.compare(new HashMap<>(), new HashMap<>());
        Assert.assertNotNull(compare);
    }

    @Test
    public void testDoCompare_givenStringAndMapAndMap_returnsSet() throws Exception{
        Map<String, Object> leftProd = new HashMap<>();
        Map<String, Object> rightProd = new HashMap<>();
        Method doCompare = productCompareUtils.getClass().getDeclaredMethod("doCompare", String.class, Map.class, Map.class);
        doCompare.setAccessible(true);
        leftProd.put("recCreatDtTm", new Object());
        rightProd.put("recCreatDtTm", new Object());
        rightProd.put("A", new Object());
        Set<Difference> set1 = (Set<Difference>) doCompare.invoke(productCompareUtils, "path", leftProd, rightProd);
        Assert.assertNotNull(set1);
        leftProd.clear();
        rightProd.clear();
        leftProd.put("A", new HashMap<>());
        rightProd.put("A", new HashMap<>());
        leftProd.put("B", new ArrayList<>());
        rightProd.put("B", new ArrayList<>());
        leftProd.put("C", new Object());
        rightProd.put("D", new Object());
        Set<Difference> set2 = (Set<Difference>) doCompare.invoke(productCompareUtils, "path", leftProd, rightProd);
        Assert.assertNotNull(set2);

    }

    @Test
    public void testCompareList_givenStringAndListAndListAndList_returnsSet() throws Exception {
        List<Object> leftList = new ArrayList<>();
        List<Object> rightList = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        Method compareList = productCompareUtils.getClass().getDeclaredMethod("compareList", String.class, List.class, List.class, List.class);
        compareList.setAccessible(true);
        Set<Difference> set1 = (Set<Difference>) compareList.invoke(productCompareUtils, "path", null, null, keys);
        Assert.assertNotNull(set1);
        Set<Difference> set2 = (Set<Difference>) compareList.invoke(productCompareUtils, "path", leftList, rightList, keys);
        Assert.assertNotNull(set2);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("A", new Object());
        Map<String, Object> map2 = new HashMap<>();
        map2.put("B", new Object());
        keys.add("A");
        keys.add("B");
        leftList.add(map1);
        rightList.add(map2);
        Set<Difference> set3 = (Set<Difference>) compareList.invoke(productCompareUtils, "path", leftList, rightList, keys);
        Assert.assertNotNull(set3);
    }

    @Test
    public void testGetResultWhenEitherIsNull_givenStringAndObjectAndObject_returnsSet() throws Exception {
        Method getResultWhenEitherIsNull = productCompareUtils.getClass().getDeclaredMethod("getResultWhenEitherIsNull", String.class, Object.class, Object.class);
        getResultWhenEitherIsNull.setAccessible(true);
        getResultWhenEitherIsNull.invoke(productCompareUtils, "path", new Object(), new Object());
        Object object = new Object();
        Object set = getResultWhenEitherIsNull.invoke(productCompareUtils, "path", object, object);
        Assert.assertNotNull(set);
    }

    @Test
    public void testGetPathInList_givenObjectAndList_returnsString() throws Exception {
        Method getPathInList = productCompareUtils.getClass().getDeclaredMethod("getPathInList", Object.class, List.class);
        getPathInList.setAccessible(true);
        Map<String, String> map = new HashMap<>();
        map.put("k", "v");
        List<String> keys = new ArrayList<>();
        keys.add("k");
        Object object1 = getPathInList.invoke(productCompareUtils, map, keys);
        Assert.assertNotNull(object1);
        keys.clear();
        Object object2 = getPathInList.invoke(productCompareUtils, map, keys);
        Assert.assertEquals("", object2);
        keys.add("key");
        Object object3 = getPathInList.invoke(productCompareUtils, map, keys);
        Assert.assertEquals("", object3);
    }

    @Test
    public void testEquals_givenObjectAndObject_returnsBoolean() throws Exception {
        Method equals = productCompareUtils.getClass().getDeclaredMethod("equals", Object.class, Object.class);
        equals.setAccessible(true);
        Object object = new Object();
        boolean bl = (boolean) equals.invoke(productCompareUtils, object, object);
        Assert.assertTrue(bl);
        boolean b2 = (boolean) equals.invoke(productCompareUtils, null, object);
        Assert.assertFalse(b2);
        Boolean b3 = (boolean) equals.invoke(productCompareUtils, LocalDateTime.now(), new Date());
        Assert.assertNotNull(b3);
        Boolean b4 = (boolean) equals.invoke(productCompareUtils, LocalDateTime.now(), object);
        Assert.assertFalse(b4);
    }

    @Test
    public void testHaveSameKeys_givenMapAndMapAndList_returnsBoolean() throws Exception {
        Map<String, Object> aMap = new HashMap<>();
        aMap.put("A", new Object());
        Map<String, Object> bMap = new HashMap<>();
        bMap.put("B", new Object());
        List<String> keys = new ArrayList<>();
        keys.add("A");
        Method haveSameKeys = productCompareUtils.getClass().getDeclaredMethod("haveSameKeys", Map.class, Map.class, List.class);
        haveSameKeys.setAccessible(true);
        boolean flag1 = (boolean) haveSameKeys.invoke(productCompareUtils, aMap, bMap, keys);
        Assert.assertFalse(flag1);
        keys.clear();
        keys.add("a");
        boolean flag2 = (boolean) haveSameKeys.invoke(productCompareUtils, aMap, bMap, keys);
        Assert.assertTrue(flag2);
    }

}
