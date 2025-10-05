package com.dummy.wmd.wpc.graphql.script;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ChmodScriptTest {

    @Test
    public void testChmodScript_givenDumyPath_printIOExceptionLog() {
        try {
            ChmodScript.chmodScript("///a", "", "");
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }
}
