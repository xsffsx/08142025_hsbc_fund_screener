package com.dummy.wmd.wpc.graphql.utils;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class CodeUtilsTest {
    @Test
    public void testNormalizedJsonPath(){
        /**
         * eg. [ROOT].altId[0].prodAltNum => altId[*].prodAltNum
         * or altId[0].prodAltNum => altId[*].prodAltNum
         * @param jsonPath
         * @return
         */
        String path1 = CodeUtils.normalizedJsonPath("[ROOT].altId[0].prodAltNum");
        assertEquals("altId[*].prodAltNum", path1);

        String path2 = CodeUtils.normalizedJsonPath("altId[0].prodAltNum");
        assertEquals("altId[*].prodAltNum", path2);
    }

    @Test
    public void testValidateSyntax() {
        String[] rightStrings = {
            "",
            "RI",
            "Y",
            "N",
            "RI+(HK1373/HK1715/HK1372/HK8126/HK2225/HK8098/HK2168)",
            "HK1373/HK1715/HK1372/HK8126",
            "RI+((HK1870+HK1872)/(HK1753)/(HK1870+HK1846))",
            "RI+(WGPB1674A_000_ENHKG)+(PALL2404A_000_ENALL/PALL2412A_000_ENALL)+(GHSS2589E_000_XXALL_D)",
            "RI+(HK1373/HK1715/HK1372)/((HK1813/HK1452/HK2172)+(HK2387/HK7777))+ASS291020121020JM"
        };
        String[] wrongStrings = {
            "*",
            "()",
            "RI+(HK1373/HK1715/HK1372/HK8126/HK2225/HK8098/HK2168",
            "HK1373//HK1715/HK1372/HK8126",
            "RI+((HK1870+HK1872)/+(HK1753)/(HK1870+HK1846))",
            "RI+(WGPB1674A_000_ENHKG)+(PALL2404A_000_ENALL/PALL2412A_000_ENALL)+[GHSS2589E_000_XXALL_D]",
            "RI+(HK1373/HK1715/HK1372)/((HK1813/HK1452/HK2172)+(HK2387/HK7777))++ASS291020121020JM"
        };
        Arrays.stream(rightStrings).forEach(str -> assertTrue(CodeUtils.validateSyntax(str)));
        Arrays.stream(wrongStrings).forEach(str -> assertFalse(CodeUtils.validateSyntax(str)));
    }
}
