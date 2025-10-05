package com.hhhh.group.secwealth.mktdata.common.convertor;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DateToStringConvertorTest {

    @Test
    public void test(){
        DateToStringConvertor dateToStringConvertor = new DateToStringConvertor();
        dateToStringConvertor.setDateFormat("123");
        dateToStringConvertor.setTimeZone("1");
        dateToStringConvertor.doConvert(new Date(),"123");
        Assert.assertTrue(true);
    }
}
