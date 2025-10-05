package com.dummy.wpb.wpc.utils.validation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ErrorTest {

    @Mock
    private Object mockData;

    private Error errorUnderTest;

    @Before
    public void setUp() throws Exception {
        errorUnderTest = new Error("subject", "message", mockData);
    }

    @Test
    public void testEquals() {
        errorUnderTest = new Error("subject", "message");

        assertThat(errorUnderTest.equals("o")).isFalse();
    }

    @Test
    public void testCanEqual() {
        assertThat(errorUnderTest.canEqual("other")).isFalse();
    }

    @Test
    public void testHashCode() {
        errorUnderTest.hashCode();
        Assert.assertNotNull(mockData);
    }

    @Test
    public void testToString() {
        assertThat(errorUnderTest.toString()).isNotNull();
    }
}
