package com.hhhh.group.secwealth.mktdata.common.dao.pojo;
import java.math.BigDecimal;
import java.sql.Timestamp;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdCatTtlRtrnIndexId;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtProdCatTtlRtrnIndexTest {

    @Test
    public void test(){
        UtProdCatTtlRtrnIndex utProdCatTtlRtrnIndex = new UtProdCatTtlRtrnIndex();
        utProdCatTtlRtrnIndex.setUtProdCatTtlRtrnIndexId(new UtProdCatTtlRtrnIndexId());
        utProdCatTtlRtrnIndex.setProdSbtypeCd("");
        utProdCatTtlRtrnIndex.setProductTypeCode("");
        utProdCatTtlRtrnIndex.setValue(new BigDecimal("0"));
        utProdCatTtlRtrnIndex.setUpdatedOn(new Timestamp(new java.util.Date().getTime()));
        utProdCatTtlRtrnIndex.setChange(new BigDecimal("0"));

        utProdCatTtlRtrnIndex.getUtProdCatTtlRtrnIndexId();
        utProdCatTtlRtrnIndex.getProdSbtypeCd();
        utProdCatTtlRtrnIndex.getProductTypeCode();
        utProdCatTtlRtrnIndex.getValue();
        utProdCatTtlRtrnIndex.getUpdatedOn();
        utProdCatTtlRtrnIndex.getChange();
        Assert.assertTrue(true);
    }

}
