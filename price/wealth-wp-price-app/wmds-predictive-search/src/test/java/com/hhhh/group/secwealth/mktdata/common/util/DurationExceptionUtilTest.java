package com.hhhh.group.secwealth.mktdata.common.util;

import com.hhhh.group.secwealth.mktdata.common.bmc.DurationException;
import org.junit.Test;

import java.util.ArrayList;

public class DurationExceptionUtilTest {

    @Test
    public void testUpdateException(){
        Boolean flag=true;
        try {
            DurationExceptionUtil.updateException(new ArrayList<DurationException>() {{
                DurationException durationException = new DurationException();
                add(durationException);
            }},new Throwable(),1);
        } catch (Exception e) {
            flag=false;
        }
        org.junit.Assert.assertTrue(flag);
    }
}
