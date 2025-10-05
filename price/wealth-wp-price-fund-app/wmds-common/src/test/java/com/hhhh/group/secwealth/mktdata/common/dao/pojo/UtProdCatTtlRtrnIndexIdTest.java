package com.hhhh.group.secwealth.mktdata.common.dao.pojo;
import java.util.Date;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtProdCatTtlRtrnIndexIdTest {

    @Test
    public void  test(){
        UtProdCatTtlRtrnIndexId utProdCatTtlRtrnIndexId = new UtProdCatTtlRtrnIndexId();
        utProdCatTtlRtrnIndexId.setBatchId(0L);
        utProdCatTtlRtrnIndexId.sethhhhCategoryCode("");
        utProdCatTtlRtrnIndexId.setFrequency("");
        utProdCatTtlRtrnIndexId.setEndDate(new Date());

        utProdCatTtlRtrnIndexId.getBatchId();
        utProdCatTtlRtrnIndexId.gethhhhCategoryCode();
        utProdCatTtlRtrnIndexId.getFrequency();
        utProdCatTtlRtrnIndexId.getEndDate();
        Assert.assertTrue(true);

    }
}
