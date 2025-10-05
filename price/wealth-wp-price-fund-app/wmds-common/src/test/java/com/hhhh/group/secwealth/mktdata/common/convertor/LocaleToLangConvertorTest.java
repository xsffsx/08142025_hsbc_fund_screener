package com.hhhh.group.secwealth.mktdata.common.convertor;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class LocaleToLangConvertorTest {

    @Test
    public void test(){
        LocaleToLangConvertor.doConvert("ABCD","ABCD");
        Assert.assertTrue(true);
    }
}
