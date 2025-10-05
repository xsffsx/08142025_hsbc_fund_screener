package com.hhhh.group.secwealth.mktdata.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AdaptorUtilTest {

    @Test
    public void convertNameValuePairListToString_singlePair_returnsCorrectString() {
        assertEquals("key=value", AdaptorUtil.convertNameValuePairListToString(
                Collections.singletonList(new BasicNameValuePair("key", "value"))));
    }

    @Test
    public void convertNameValuePairListToString_multiplePairs_returnsCorrectString() {
        assertEquals("key1=value1&key2=value2", AdaptorUtil.convertNameValuePairListToString(
                Arrays.asList(new BasicNameValuePair("key1", "value1"), new BasicNameValuePair("key2", "value2"))));
    }

    @Test
    public void convertNameValuePairListToString_emptyList_returnsEmptyString() {
        assertEquals("", AdaptorUtil.convertNameValuePairListToString(Collections.emptyList()));
    }
}