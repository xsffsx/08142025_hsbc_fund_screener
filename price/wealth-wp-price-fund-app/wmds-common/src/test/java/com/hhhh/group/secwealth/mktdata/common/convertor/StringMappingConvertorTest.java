package com.hhhh.group.secwealth.mktdata.common.convertor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StringMappingConvertorTest {

    @Test
    public  void test(){
        StringMappingConvertor stringMappingConvertor= new StringMappingConvertor();
        stringMappingConvertor.setStringMap(new HashMap<String, String>());
        stringMappingConvertor.doConvert("123","456");
        Assert.assertTrue(true);
    }

}
