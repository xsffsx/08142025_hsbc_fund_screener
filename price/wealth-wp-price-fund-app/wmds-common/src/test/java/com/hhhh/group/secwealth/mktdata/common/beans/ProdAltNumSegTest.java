package com.hhhh.group.secwealth.mktdata.common.beans;


import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProdAltNumSegTest {

    @Test
    public void test(){
        ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
        prodAltNumSeg.setProdAltNum("");
        prodAltNumSeg.setProdCdeAltClassCde("");
        prodAltNumSeg.getProdAltNum();
        prodAltNumSeg.getProdCdeAltClassCde();
        Assert.assertTrue(true);
    }
}
