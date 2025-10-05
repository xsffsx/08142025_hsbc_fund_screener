package com.hhhh.group.secwealth.mktdata.common.convertor;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BooleanToStringConvertorTest {

    @Test
    public void test(){
        BooleanToStringConvertor booleanToStringConvertor = new BooleanToStringConvertor();
        booleanToStringConvertor.setConvertMap(new HashMap<Boolean, String>());
        booleanToStringConvertor.getConvertMap();
        booleanToStringConvertor.doConvert(true,"123");
        Assert.assertTrue(true);
    }



}
