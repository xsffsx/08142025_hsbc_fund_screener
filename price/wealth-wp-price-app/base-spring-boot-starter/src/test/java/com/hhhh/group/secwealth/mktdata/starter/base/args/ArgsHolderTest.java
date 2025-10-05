/*
 */
package com.hhhh.group.secwealth.mktdata.starter.base.args;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ArgsHolderTest {

    @Test
    public void testArgsHolder() {
        final String key = "KEY";
        final String value1 = "VALUE_1";
        final String value2 = "VALUE_2";
        Thread t1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    ArgsHolder.putArgs(key, value1);
                    String result = (String) ArgsHolder.getArgs(key);
                    assertEquals("Get unexcepted value", value1, result);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        fail("InterruptedException");
                    }
                    ArgsHolder.removeArgs();
                    result = (String) ArgsHolder.getArgs(key);
                    assertNull("Get unexcepted value", result);
                }
            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    ArgsHolder.putArgs(key, value2);
                    String result = (String) ArgsHolder.getArgs(key);
                    assertEquals("Get unexcepted value", value2, result);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        fail("InterruptedException");
                    }
                    ArgsHolder.removeArgs();
                    result = (String) ArgsHolder.getArgs(key);
                    assertNull("Get unexcepted value", result);
                }
            }
        };
        t1.start();
        t2.start();
        for (int i = 0; i < 50; i++) {
            String result = (String) ArgsHolder.getArgs(key);
            assertNull("Get unexcepted value", result);
        }
    }

}
