package com.hhhh.group.secwealth.mktdata.common.convertor;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ServiceKeyToProdCdeAltClassCdeConvertorTest {

    @Test
    public void test(){
        ServiceKeyToProdCdeAltClassCdeConvertor serviceKeyToProdCdeAltClassCdeConvertor = new ServiceKeyToProdCdeAltClassCdeConvertor();
        serviceKeyToProdCdeAltClassCdeConvertor.doConvert("abcd");
        Assert.assertTrue(true);
    }

}
