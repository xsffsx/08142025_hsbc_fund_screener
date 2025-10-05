/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalUtilTest {

    @Test
    public void testFromStringWithEmptyStrParameter() {
        String emptyStr = "";
        BigDecimal result = BigDecimalUtil.fromString(emptyStr);
        assertThat(result, nullValue());
    }

}
