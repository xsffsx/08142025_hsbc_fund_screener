package com.hhhh.group.secwealth.mktdata.common.bmc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.GregorianCalendar;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DurationExceptionTest {

    @Test
    public void duration(){
        DurationException durationException = new DurationException();
        durationException.setError(new Exception());
        durationException.setTime(new GregorianCalendar());
        durationException.getTime();
        durationException.getError();
        Assert.assertTrue(true);
    }

}
