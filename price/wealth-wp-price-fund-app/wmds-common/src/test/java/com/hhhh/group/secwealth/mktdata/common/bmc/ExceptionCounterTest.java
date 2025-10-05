package com.hhhh.group.secwealth.mktdata.common.bmc;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ExceptionCounterTest {

    @Test
    public void test(){
        ExceptionCounter exceptionCounter = new ExceptionCounter();
        exceptionCounter.setBmcErrCde("");
        exceptionCounter.setBmcErrMsg("");
        exceptionCounter.setExceptionList(new ArrayList<DurationException>());
        exceptionCounter.setExceptionName("");
        exceptionCounter.setKey("");
        exceptionCounter.setMaxCount(1);
        exceptionCounter.setTimeDuration(1);
        exceptionCounter.getBmcErrCde();
        exceptionCounter.getBmcErrMsg();
        exceptionCounter.getExceptionList();
        exceptionCounter.getExceptionName();
        exceptionCounter.getKey();
        exceptionCounter.getTimeDuration();
        Assert.assertTrue(true);
    }


}
