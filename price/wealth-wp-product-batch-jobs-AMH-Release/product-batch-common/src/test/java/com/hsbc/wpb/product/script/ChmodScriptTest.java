package com.dummy.wpb.product.script;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class ChmodScriptTest {

    @Test
    void testChmodScript_givenDumyPath_printIOExceptionLog() {
        try {
            ChmodScript.chmodScript("///a", "", "");
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }
}