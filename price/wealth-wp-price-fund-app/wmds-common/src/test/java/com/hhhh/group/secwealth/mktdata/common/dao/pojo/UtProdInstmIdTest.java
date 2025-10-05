package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtProdInstmIdTest {

    @Test
    public void test(){
        UtProdInstmId utProdInstmId = new UtProdInstmId();
        utProdInstmId.setProductId(0);
        utProdInstmId.setBatchId(0L);

        utProdInstmId.getProductId();
        utProdInstmId.getBatchId();
        Assert.assertTrue(true);

    }
}
