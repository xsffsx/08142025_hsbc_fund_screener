package com.dummy.wpb.wpc.utils.constant;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;


public class DocTypeTests {

    @Test
    public void propertyTest_DoesNotThrow() {
        DocType[] docTypes = DocType.values();
        String docTypeStr = Arrays.toString(docTypes);
        assertNotNull(docTypeStr);
    }

}
