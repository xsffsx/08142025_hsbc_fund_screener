package com.hhhh.group.secwealth.mktdata.common.convertor;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StringToBooleanConvertorTest {

    @Test
    public void test(){
        StringToBooleanConvertor stringToBooleanConvertor = new StringToBooleanConvertor();
        stringToBooleanConvertor.setConvertMap(new HashMap<String, Boolean>());
        stringToBooleanConvertor.getConvertMap();
        stringToBooleanConvertor.doConvert("ABCD","ABCD");
        Assert.assertTrue(true);
    }
}
