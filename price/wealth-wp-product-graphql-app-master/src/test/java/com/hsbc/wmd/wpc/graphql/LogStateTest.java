package com.dummy.wmd.wpc.graphql;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class LogStateTest {

    private LogState logState;

    @Before
    public void setUp() throws Exception {
        logState = new LogState();
    }
    @Test
    public void testGetStartTimeMillis_givenNull_returnsLong() {
        assertNotEquals(0L,logState.getStartTimeMillis());
    }

    @Test
    public void testGetMillisCost_givenNull_returnsLong() throws InterruptedException {
        assertNotEquals(100L,logState.getMillisCost());
    }
}
