package com.hhhh.group.secwealth.mktdata.common.dao.pojo;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtProdCatPerfmRtrnIdTest {

    @Test
    public void test(){
        UtProdCatPerfmRtrnId utProdCatPerfmRtrnId = new UtProdCatPerfmRtrnId();
        utProdCatPerfmRtrnId.setBatchId(0L);
        utProdCatPerfmRtrnId.sethhhhCategoryCode("");
        utProdCatPerfmRtrnId.setRtrnAmtTtpeCde("");

        utProdCatPerfmRtrnId.getBatchId();
        utProdCatPerfmRtrnId.gethhhhCategoryCode();
        utProdCatPerfmRtrnId.getRtrnAmtTtpeCde();
        Assert.assertTrue(true);

    }
}
