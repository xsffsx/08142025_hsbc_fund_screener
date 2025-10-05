package com.hhhh.group.secwealth.mktdata.common.convertor;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ConvertorUtilTest {

    @Test
    public void test(){
        ConvertorUtil convertorUtil = new ConvertorUtil();
        convertorUtil.setConvertors(new HashMap<String, Convertor<Object, Object>>());
        Assert.assertTrue(true);
    }
}
