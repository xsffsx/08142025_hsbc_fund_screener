package com.hhhh.group.secwealth.mktdata.common.util;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


 class StringUtilTest {

    @Test
    void testGetTokenName() {
        String tokenName = StringUtil.getTokenName("testToken123456789");
        assertNotNull(tokenName);
        assertEquals("testToken123456789_token", tokenName);
    }

    @Test
    void testGetOriginalName() {
        String originalName = StringUtil.getOriginalName("testOriginal");
        assertEquals("testOriginal_ORIGINAL", originalName);
    }

    @Test
    void testReplaceSpace() {
        String str = StringUtil.replaceSpace("test 123");
        assertEquals("test%20123", str);
    }

    @Test
    void testUpperCase() {
        String str = StringUtil.toUpperCase("code");
        assertEquals("CODE", str);
    }

    @Test
    void testIsNumeric() {
        boolean result = StringUtil.isNumeric("678");
        assertTrue(result);
    }

    @Test
    void testIsNotNumeric() {
        boolean result = StringUtil.isNumeric("java");
        assertFalse(result);
    }

    @Test
    void testToStringWithoutSpace() {
        String result = StringUtil.toStringWithoutSpace("computerdesk  ");
        assertEquals("computerdesk", result);
    }

    @Test
    void testCombineWithDot() {
        String result = StringUtil.combineWithDot("test","fund");
        assertEquals("test.fund", result);
    }

    @Test
    void testCombineWithUnderline() {
        String result = StringUtil.combineWithUnderline("test","prod");
        assertEquals("test_prod", result);
    }

    @Test
    void testIsChineseChar() {
        boolean result = StringUtil.isChineseChar("English");
        assertFalse(result);
    }

    @Test
    void testIsNotASCIIChar() {
        boolean result = StringUtil.isASCIIChar('C');
        assertTrue(result);
    }

    @Test
    void testIsASCIIChar() {
        boolean result = StringUtil.isASCIIChar((char) 32);
        assertTrue(result);
    }

    @Test
    void testIsInvalid() {
        boolean result = StringUtil.isInvalid(" ");
        assertTrue(result);
    }

    @Test
    void testIsValid() {
        boolean result = StringUtil.isValid("java");
        assertTrue(result);
    }

    @Test
    void testStringToBoolean() {
        boolean result = StringUtil.stringToBoolean("true");
        assertTrue(result);
    }

    @Test
    void testToStringAndCheckNull() {
        String result = StringUtil.toStringAndCheckNull(Integer.valueOf(123));
        assertNotNull(result);
    }

    @Test
    void testServerNameAndPath() {
        String result = StringUtil.getServerName();
        String result2 = StringUtil.getServerPath();
        assertNotNull(result);
        assertNotNull(result2);
    }

    @Test
    void testFilterSpecialString() {
        String[] filterSpecialString = StringUtil.filterSpecialString("test filter# special String@", '#', '@');
        assertNotNull(filterSpecialString);
    }

    @Test
    void testFormatHKStockSymbol() {
        String symbol = StringUtil.formatHKStockSymbol("3319");
        assertNotNull(symbol);
    }

    @Test
    void testTransformStringtoBoolean() {
        Boolean result = StringUtil.transformStringtoBoolean("Y");
        Boolean result2 = StringUtil.transformStringtoBoolean("false");
        assertTrue(result);
        assertFalse(result2);
    }

    @Test
    void testTransformYNIntoBoolean() {
        Boolean result = StringUtil.transformStringtoBoolean("N");
        Boolean result2 = StringUtil.transformStringtoBoolean("Y");
        assertFalse(result);
        assertTrue(result2);
    }

    @Test
    void testStringtoBoolean() {
        Boolean result = StringUtil.stringToBoolean("true");
        assertTrue(result);
    }

    @Test
    void TestStreamToStringConvert() throws IOException {
        List<String> lines = Arrays.asList("The first line", "The second line");
        Path file = Paths.get("testFile.txt");
        Files.write(file, lines, StandardCharsets.UTF_8);

        InputStream input = new FileInputStream(file.toFile());
        String result = StringUtil.streamToStringConvert(input);
        assertNotNull(result);
    }

    @Test
    void testCombineWithVertical() {
        String result = StringUtil.combineWithVertical("test","prod");
        assertEquals("test|prod", result);
    }

    @Test
    void testIfContainField() {
        boolean result = StringUtil.ifContainField(new String[] {"name","type"},"type");
        assertTrue(result);
    }

}
